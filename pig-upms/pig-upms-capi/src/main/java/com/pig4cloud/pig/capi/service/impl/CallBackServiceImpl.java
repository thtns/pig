package com.pig4cloud.pig.capi.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.capi.dto.request.RebotCallbackParames.RobotResponse;
import com.pig4cloud.pig.capi.dto.request.RobotCallbackRequest;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import com.pig4cloud.pig.capi.service.BizSupplierService;
import com.pig4cloud.pig.capi.service.CallBackService;
import com.pig4cloud.pig.capi.service.atripartite.chaboshi.CBSBuilder;
import com.pig4cloud.pig.capi.service.atripartite.chaboshi.ChaBoosConfig;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.DateTimeUitils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.UUidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallBackServiceImpl implements CallBackService {

	private final ChaBoosConfig chaBoosConfig;

	private final BizBuyerOrderService bizBuyerOrderService;

	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	/*** 供应商 **/
	private final BizSupplierService bizSupplierService;


	/**
	 * 累加供应商请求次数
	 * @param bizBuyerOrder
	 */
	private void addSupplierReqCount(BizBuyerOrder bizBuyerOrder){
		Long id = bizBuyerOrder.getSupplierId();
		bizSupplierService.addSupplierCount(id);
	}

	public void success(RobotCallbackRequest rebotCallbackRequest) {
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(rebotCallbackRequest.getOrderId());
		RobotResponse robotResponse = rebotCallbackRequest.getRobotResponse();// 机器人数据
		saveRecord(bizBuyerOrder, robotResponse); // 保存查询记录
		successCallbackMerchant(bizBuyerOrder, robotResponse); // 异步回调商户 - 成功结果
		addSupplierReqCount(bizBuyerOrder); //计算成功单量
	}

	public void reject(BizBuyerOrder bizBuyerOrder) {
		rejectCallbackMerchant(bizBuyerOrder); // 异步回调商户 - 驳回
	}

	public void noData(BizBuyerOrder bizBuyerOrder) {
		saveRecord(bizBuyerOrder, null); // 保存查询记录
		noDataCallbackMerchant(bizBuyerOrder); // 异步回调商户 - 无记录
		addSupplierReqCount(bizBuyerOrder); //计算成功单量
	}


	/**
	 * 保存查询记录
	 *
	 * @param bizBuyerOrder
	 * @param robotResponse
	 */
	public void saveRecord(BizBuyerOrder bizBuyerOrder, RobotResponse robotResponse) {
		BizRobotQueryRecord bizRobotQueryRecord = BizRobotQueryRecord
				.builder()
				.id(UUidUtils.uuLongId())
				.vin(bizBuyerOrder.getVin())
				.supplierId(bizBuyerOrder.getSupplierId())
				.supplierName(bizBuyerOrder.getSupplierName())
				.robotId(bizBuyerOrder.getRobotId())
				.resultStatus(BaseConstants.ROBOT_QUERY_STATUS_SUCCESS)
				.result(JSON.toJSONString(robotResponse))
				.querytime(LocalDateTime.now())
				.build();
		if (Objects.isNull(robotResponse)) {
			log.info("callback success ：机器人查询无记录,修改查询结果为无记录...");
			bizRobotQueryRecord.setResultStatus(BaseConstants.ROBOT_QUERY_STATUS_NO_RESULT);
		}
		//保存机器人请求成功结果
		bizRobotQueryRecordService.save(bizRobotQueryRecord);
		log.info("callback success ：vin 【{}】 robotRequestCallBack 保存机器人查询记录.", bizBuyerOrder.getVin());
	}

	/***
	 * 成功回调商户
	 * @param bizBuyerOrder
	 * @param robotResponse
	 */
	public void successCallbackMerchant(BizBuyerOrder bizBuyerOrder, RobotResponse robotResponse) {
		CompletableFuture.runAsync(() -> {
					log.info("successCallbackMerchant ： 开始异步回调商家");
					// 标识有记录
					bizBuyerOrder.setResult(JSON.toJSONString(robotResponse));
					bizBuyerOrder.setAnyData(BaseConstants.ANY_DATA_TRUE);
					try {
						AtomicInteger times = new AtomicInteger(1);
//						RetryUtil.executeWithRetry(() -> {
						log.info("successCallbackMerchant : RetryUtil开始回调 第{}次", times);
						times.addAndGet(1);
						log.info("successCallbackMerchant :  order_id: {}", bizBuyerOrder.getId());

						Integer status = anyDataMerchantCallBack(bizBuyerOrder, robotResponse);// 有数据回调
//						if (status.equals(200)) {// 成功回调, 则更新订单状态
//							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_SUCCESS, null);
//						} else {// 三次失败状态
//							String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType()));
//							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
//						}
						updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_SUCCESS, null);
						log.info("successCallbackMerchant vin {{}} robotRequestCallBack 更新订单状态： {}", bizBuyerOrder.getVin(), RequestStatusEnum.getStatusEnumByCode(bizBuyerOrder.getRequestStatus()));
//							return null;
//						}, 3, 3000L, false);
					} catch (Exception e) {
						// 更新失败原因和失败状态： 回调失败
						log.error("successCallbackMerchant : RetryUtil回调商户错误....");
						String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType()));
						log.info("successCallbackMerchant vin {{}} robotRequestCallBack 更新订单错误原因： {}", bizBuyerOrder.getVin(), failureReason); // 不修改状态,等待10分钟回调驳回
						updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);

					}
				}
		);
	}

	/***
	 * 驳回订单回调商户
	 * @param bizBuyerOrder
	 */
	public void rejectCallbackMerchant(BizBuyerOrder bizBuyerOrder) {
		CompletableFuture.runAsync(() -> {
					log.info("rejectCallbackMerchant ： 开始异步回调商家 - 驳回");
					bizBuyerOrder.setAnyData(BaseConstants.ANY_DATA_FALSE);
					try {
						AtomicInteger times = new AtomicInteger(1);
//						RetryUtil.executeWithRetry(() -> {
						log.info("rejectCallbackMerchant : RetryUtil开始回调 第{}次", times);
						times.addAndGet(1);
						log.info("rejectCallbackMerchant :  order_id: {}", bizBuyerOrder.getId());
						Integer status = rejectMerchantCallBack(bizBuyerOrder);
//						if (status.equals(200)) {// 成功回调, 则更新订单状态
//							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_REJECT, null);
//						} else {// 三次失败状态
//							String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType()));
//							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
//						}
						updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_REJECT, null);
						log.info("rejectCallbackMerchant vin {{}} robotRequestCallBack 更新订单状态： {}", bizBuyerOrder.getVin(), RequestStatusEnum.getStatusEnumByCode(bizBuyerOrder.getRequestStatus()));
//							return null;
//						}, 3, 3000L, false);
					} catch (Exception e) {
						// 更新失败原因和失败状态： 回调失败
						log.error("rejectCallbackMerchant : RetryUtil回调商户错误....");
						String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.CALLBACK_FAILURE.getType()));
						log.info("rejectCallbackMerchant vin {{}} robotRequestCallBack 更新订单错误原因： {}", bizBuyerOrder.getVin(), failureReason); // 不修改状态,等待10分钟回调驳回
						updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
					}
				}
		);
	}

	/***
	 * 无记录回调商户
	 * @param bizBuyerOrder
	 */
	public void noDataCallbackMerchant(BizBuyerOrder bizBuyerOrder) {
		CompletableFuture.runAsync(() -> {
					log.info("noDataCallbackMerchant ： 开始异步回调商家 - 无记录");
					bizBuyerOrder.setAnyData(BaseConstants.ANY_DATA_FALSE);
					try {
						AtomicInteger times = new AtomicInteger(1);
//						RetryUtil.executeWithRetry(() -> {
						log.info("noDataCallbackMerchant : RetryUtil开始回调 第{}次", times);
						times.addAndGet(1);
						log.info("noDataCallbackMerchant :  order_id: {}", bizBuyerOrder.getId());
						Integer status = noDataMerchantCallBack(bizBuyerOrder);
//						if (status.equals(200)) {// 成功回调, 则更新订单状态
//							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_SUCCESS, null);
//						} else {// 三次失败状态
//							String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType()));
//							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
//						}
						updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_SUCCESS, null);
						log.info("noDataCallbackMerchant vin {{}} robotRequestCallBack 更新订单状态： {}", bizBuyerOrder.getVin(), RequestStatusEnum.getStatusEnumByCode(bizBuyerOrder.getRequestStatus()));
//							return null;
//						}, 3, 3000L, false);
					} catch (Exception e) {
						// 更新失败原因和失败状态： 回调失败
						log.error("noDataCallbackMerchant : RetryUtil回调商户错误....");
						String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.CALLBACK_FAILURE.getType()));
						log.info("noDataCallbackMerchant vin {{}} robotRequestCallBack 更新订单错误原因： {}", bizBuyerOrder.getVin(), failureReason); // 不修改状态,等待10分钟回调驳回
						updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
					}
				}
		);
	}

	private void updateOrderStatus(BizBuyerOrder bizBuyerOrder, RequestStatusEnum status, String failureReason) {
		bizBuyerOrder.setRequestStatus(status.getType());
		bizBuyerOrder.setFailureReason(failureReason);
		LocalDateTime nowDateTime = LocalDateTime.now();
		bizBuyerOrder.setCallbackTime(nowDateTime);
		bizBuyerOrder.setSpendTime(DateTimeUitils.localDateTimeBetweenSeconds(bizBuyerOrder.getRequestTime(), nowDateTime));
		bizBuyerOrderService.updateById(bizBuyerOrder);
	}


	/***
	 * 成功，有记录给商家做回调请求
	 * @param bizBuyerOrder
	 * @param robotResponse
	 */
	public Integer anyDataMerchantCallBack(BizBuyerOrder bizBuyerOrder, RobotResponse robotResponse) throws Exception {
		log.info("#### 上传记录 anyDataMerchantCallBack 开始执行...");
		// 这里只有成功有记录的
		int status = RequestStatusEnum.ORDER_REPORT_STATUS_ZONE.getType();
		if (bizBuyerOrder.getOrderType().contains("chaboshi")) {// 查博士
			return sendChaBoss(bizBuyerOrder.getOrderType(), bizBuyerOrder.getOrderNo(), status, robotResponse);
		} else {
			return sendMerchant(bizBuyerOrder, status, robotResponse);
		}
	}

	/***
	 * 驳回订单给商家做回调请求
	 * @param bizBuyerOrder
	 */
	public Integer rejectMerchantCallBack(BizBuyerOrder bizBuyerOrder) throws Exception {
		log.info("#### 驳回 rejectMerchantCallBack 开始执行...");
		// 这里只有驳回的
		int status = RequestStatusEnum.ORDER_REPORT_STATUS_ONE.getType();
		if (bizBuyerOrder.getOrderType().contains("chaboshi")) {// 查博士
			return sendChaBoss(bizBuyerOrder.getOrderType(), bizBuyerOrder.getOrderNo(), status, null);
		} else {
			return sendMerchant(bizBuyerOrder, status, null);
		}
	}

	/***
	 * 无记录给商家做回调请求
	 * @param bizBuyerOrder
	 */
	public Integer noDataMerchantCallBack(BizBuyerOrder bizBuyerOrder) throws Exception {
		log.info("#### 无记录 noDataMerchantCallBack 开始执行...");
		// 这里只有无记录的
		int status = RequestStatusEnum.ORDER_REPORT_STATUS_TWO.getType();
		if (bizBuyerOrder.getOrderType().contains("chaboshi")) {// 查博士
			return sendChaBoss(bizBuyerOrder.getOrderType(), bizBuyerOrder.getOrderNo(), status, null);
		} else {
			return sendMerchant(bizBuyerOrder, status, null);
		}
	}


	/**
	 * 提交订单数据查博士
	 *
	 * @param orderNo
	 * @param status
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public Integer sendChaBoss(String type, String orderNo, int status, Object object) throws Exception {
		log.info("#### sendChaBoss 开始执行... orderNo： 【{}】, status： 【{}】", orderNo, status);
		String userId;
		String secret;
		if (type.equals(BaseConstants.CHA_BO_SHI)) {
			userId = chaBoosConfig.getUserId();
			secret = chaBoosConfig.getKeySecret();
		} else {
			userId = chaBoosConfig.getUserId2();
			secret = chaBoosConfig.getKeySecret2();
		}
		CBSBuilder cbsBuilder = CBSBuilder.newCBSBuilder(userId, secret, chaBoosConfig.isOnLine());
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("orderno", orderNo);
		if (object != null) {
			// 将对象转换为 JSON 字符串并保持字段顺序
			params.put("content", JSON.toJSONString(object));
		}
		params.put("reportstatus", status);
		String data = cbsBuilder.sendPost(chaBoosConfig.getUrl(), params);
		JSONObject obj = JSON.parseObject(data);
		Integer messageCode = obj.getInteger("messageCode");
		log.info("#### sendChaBoss 执行结束... messageCode： 【{}】", messageCode);
		Integer result_code = 0;
		if (RequestStatusEnum.ORDER_REPORT_STATUS_ZONE.getType().equals(status)) {
			if (messageCode.equals(4010)) {
				result_code = 200;
			}
		} else if (RequestStatusEnum.ORDER_REPORT_STATUS_ONE.getType().equals(status)) {
			if (messageCode.equals(4011)) {
				result_code = 200;
			}
		} else if (RequestStatusEnum.ORDER_REPORT_STATUS_TWO.getType().equals(status)) {
			if (messageCode.equals(4012)) {
				result_code = 200;
			}
		}
		return result_code;
	}

	/**
	 * 提交订单数据商家
	 *
	 * @param bizBuyerOrder
	 * @return
	 */
	public Integer sendMerchant(BizBuyerOrder bizBuyerOrder, int orderStatus, RobotResponse robotResponse) {
		log.info("#### merchantCallBack（回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("orderno", bizBuyerOrder.getId());
		paramMap.put("reportstatus", orderStatus);
		paramMap.put("content", robotResponse);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("code", CommonConstants.API_SUCCESS);
		resultMap.put("data", paramMap);

		log.info("#### merchantCallBack（成功回调商家）：订单id 【{}】", bizBuyerOrder.getId());
		HttpResponse result = HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(resultMap)).contentType("application/json").execute();
		Integer status = result.getStatus();
		String content = result.body();
		log.info("回调商户接口返回状态：{}, 返回内容：{}", status, content);
		return status;
	}

}
