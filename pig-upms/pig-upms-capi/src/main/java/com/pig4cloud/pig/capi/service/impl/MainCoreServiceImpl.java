package com.pig4cloud.pig.capi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.request.RebotCallbackParames.RobotResponse;
import com.pig4cloud.pig.capi.service.*;
import com.pig4cloud.pig.capi.service.atripartite.CallBackManager;
import com.pig4cloud.pig.capi.utils.rocketmq.ProducerUtil;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainCoreServiceImpl implements MainCoreService {

	final int TIME_OUT = 2000;

	/*** 机器人查询记录 **/
	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	/*** 采购商记录 **/
	private final BizBuyerOrderService bizBuyerOrderService;

	/*** 品牌 **/
	private final BizCarBrandService bizCarBrandService;

	private final RedisTemplate<String, String> redisTemplate;

	private final CallBackManager callBackManager;

	private final ProducerUtil producerUtil;

	/**
	 * @param bizBuyerOrder
	 * @return
	 */
	@Override
	public R<Object> placeOrder(BizBuyerOrder bizBuyerOrder) {
		String vin = bizBuyerOrder.getVin();
		Long buyerId = bizBuyerOrder.getBuyerId();
		log.info("~~~~ Step2: 通过Vin：{} 查询机器人查询记录.", vin);
		BizRobotQueryRecord bizRobotQueryRecord = bizRobotQueryRecordService.getQueryRecordByVin(vin);
		if (Optional.ofNullable(bizRobotQueryRecord).isPresent()) {
			log.info("~~~~ Step2.1: 本地存在 Vin：{} 机器人查询记录. 加载本地结果数据.....", vin);
			localMysqlParsing(bizBuyerOrder, bizRobotQueryRecord); // 解析本地接口做回调
		} else {
			log.info("~~~~ Step2.1: 本地不存在 Vin：{}  机器人查询记录. ", vin);
			log.info("~~~~ Step2.2: 通过Vin：{} 和 BuyerId：{} 查询是否已下过的订单.... ", vin, buyerId);
			boolean isPlaceOrder = bizBuyerOrderService.isOrNotPlaceOrder(vin, buyerId);
			if (isPlaceOrder) {
				log.info("~~~~ Step2.3: 本地存在Vin：{} 和 BuyerId：{} 的订单. 发送订单到延迟消息队列. ", vin, buyerId);
//				sendDelayedMessage(bizBuyerOrder);    // 有同样请求,将消息发送到延迟队列, 等有结果后加载本地并回调商家
			} else {
				log.info("~~~~ Step2.3: 本地不存在Vin：{} 和 BuyerId：{} 的订单. 发起新机器人订单. ", vin, buyerId);
//				spiderServerRequest(bizBuyerOrder); // 新发起机器人请求
			}
		}

		return null;
	}

	/***
	 * 本地数据库数据解析
	 * @param bizBuyerOrder
	 * @param bizRobotQueryRecord
	 * @return
	 */
	private void localMysqlParsing(BizBuyerOrder bizBuyerOrder, BizRobotQueryRecord bizRobotQueryRecord) {
		String vin = bizBuyerOrder.getVin();
		Long order_id = bizBuyerOrder.getId();
		log.info("~~~~ Step3: 开始加载本地 Vin：{} 订单记录", vin);
		BizBuyerOrder successOrder = bizBuyerOrderService.getSuccessMerchantOrderByVin(vin);
		if (Objects.nonNull(successOrder)) {
			log.info("~~~~ Step3.1: 存在本地 Vin：{} 订单记录, 更新订单品牌信息....", vin);
			bizBuyerOrder.setBuyerId(successOrder.getBuyerId());
			bizBuyerOrder.setBuyerName(successOrder.getCarBrandName());
			bizBuyerOrder.setSupplierId(successOrder.getSupplierId());
			bizBuyerOrder.setSupplierName(successOrder.getSupplierName());
			bizBuyerOrder.setRobotId(successOrder.getRobotId());
		}
		//机器人记录赋值采购商订单成功结果
		if (Objects.nonNull(bizRobotQueryRecord.getResult())) {
			log.info("~~~~ Step3.2: 本地数据成功解析....同步到订单数据并回调给商户。");
			bizBuyerOrder.setResult(bizRobotQueryRecord.getResult());
			CompletableFuture.runAsync(() -> {
				log.info("#### Step3.3: 异步解析开始");
				try {
					Thread.sleep(TIME_OUT);
					AtomicInteger times = new AtomicInteger(1);
					RetryUtil.executeWithRetry(() -> {
						log.info("#### Step3.4.1: Vin：{} RetryUtil开始回调 第{}次", vin, times);
						times.addAndGet(1);
						RobotResponse robotResponse = JSON.parseObject(bizRobotQueryRecord.getResult(), RobotResponse.class);
						log.info("#### Step3.4.2:  order_id: {}，回调采购商维修数据 : {}", order_id
								, JSON.toJSONString(JSON.parseObject(bizRobotQueryRecord.getResult(), RobotResponse.class)));
						String result = callBackManager.merchantCallBack(bizBuyerOrder, robotResponse);
						JSONObject jsonObject = JSON.parseObject(result);
						if (Boolean.TRUE.equals(jsonObject.get("success"))){// 成功回调, 则更新订单状态
							bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_SUCCESS.getType());
							bizBuyerOrder.setResult(JSON.toJSONString(robotResponse));
							bizBuyerOrder.setCallbackTime(LocalDateTime.now());
							bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
						}else if (Boolean.FALSE.equals(jsonObject.get("success")) && times.get() >= 3){// 三次失败状态
							bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
							bizBuyerOrder.setFailureReason(JSON.toJSONString(jsonObject));
							bizBuyerOrder.setCallbackTime(LocalDateTime.now());
							bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
						}
						return null;
					}, 3, 1000L, false);
				} catch (Exception e) {
					// 更新失败原因和失败状态： 回调失败
					log.info("#### Step3.4.3: RetryUtil回调失败....");
					bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
					bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType())));
					bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
				}
			});
		}

		//机器人记录赋值采购商订单失败原因
		String failureReason = bizRobotQueryRecord.getFailureReason();
		if (StringUtils.isNotBlank(failureReason)) {
			log.info("~~~~ Step3.2: 本地数据失败解析....同步到订单数据并回调给商户。");
			bizBuyerOrder.setFailureReason(failureReason);
			CompletableFuture.runAsync(() -> {
				log.info("#### Step3.3: 错误回调开始");
				try {
					Thread.sleep(TIME_OUT);
					AtomicInteger times = new AtomicInteger(1);
					RetryUtil.executeWithRetry(() -> {
						log.info("#### Step3.4.1: Vin：{} RetryUtil开始回调 第{}次", vin, times);
						times.addAndGet(1);
						log.error("#### Step3.4.2:  order_id: {}，回调采购商维修数据 : {}", order_id, failureReason);
						String result = callBackManager.merchantCallBackError(bizBuyerOrder);
						JSONObject jsonObject = JSON.parseObject(result);
						if (Boolean.TRUE.equals(jsonObject.get("success"))){// 成功回调, 则更新订单状态
							bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
							bizBuyerOrder.setFailureReason(failureReason);
							bizBuyerOrder.setCallbackTime(LocalDateTime.now());
							bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
						}else if (Boolean.FALSE.equals(jsonObject.get("success")) && times.get() >= 3){// 三次失败状态 失败回调请求用户失败
							bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
							bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType())));
							bizBuyerOrder.setCallbackTime(LocalDateTime.now());
							bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
						}
						return null;
					}, 3, 1000L, false);
				} catch (Exception e) {
					// 更新失败原因和失败状态： 回调失败
					log.info("#### Step3.4.3: RetryUtil回调失败....");
					bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
					bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType())));
					bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
				}
			});
		}
		log.info("~~~~ Step4 本地数据解析结束, 商家数据赋值更新 ：" + JSON.toJSONString(bizBuyerOrder));
	}
}
