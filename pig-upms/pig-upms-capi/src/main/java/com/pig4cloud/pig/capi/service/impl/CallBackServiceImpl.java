package com.pig4cloud.pig.capi.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chaboshi.web.mdp.builder.CBSBuilder;
import com.pig4cloud.pig.capi.dto.request.RebotCallbackParames.RobotResponse;
import com.pig4cloud.pig.capi.dto.request.RobotCallbackRequest;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import com.pig4cloud.pig.capi.service.CallBackService;
import com.pig4cloud.pig.capi.service.atripartite.CallBackQuanManager;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetryUtil;
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

	private final BizBuyerOrderService bizBuyerOrderService;

	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	private final CallBackQuanManager callBackQuanManager;

	private final String chaboshi_url = "/middleAgentApi/completeOrder";


	public void success(RobotCallbackRequest rebotCallbackRequest) {
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(rebotCallbackRequest.getOrderId());
		RobotResponse robotResponse = rebotCallbackRequest.getRobotResponse();// 机器人数据
		saveRecord(bizBuyerOrder, robotResponse); // 保存查询记录
		callbackMerchant(bizBuyerOrder, robotResponse); // 异步回调商户
		callBackQuanManager.callBackQueueManage(bizBuyerOrder);// 移除机器人key
	}

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
			log.info("callback yes ：机器人查询无记录,修改查询结果为无记录...");
			bizRobotQueryRecord.setResultStatus(BaseConstants.ROBOT_QUERY_STATUS_NO_RESULT);
		}
		//保存机器人请求成功结果
		bizRobotQueryRecordService.save(bizRobotQueryRecord);
		log.info("callback yes ：vin {{}} robotRequestCallBack 保存机器人查询记录： {}", bizBuyerOrder.getVin(), JSON.toJSONString(JSON.toJSONString(robotResponse)));
	}

	public void callbackMerchant(BizBuyerOrder bizBuyerOrder, RobotResponse robotResponse) {
		CompletableFuture.runAsync(() -> {
				log.info("callback yes ： 开始异步回调商家");
				try {
					AtomicInteger times = new AtomicInteger(1);
					RetryUtil.executeWithRetry(() -> {
						log.info("callback yes : RetryUtil开始回调 第{}次", times);
						times.addAndGet(1);
						log.info("callback yes :  order_no: {}，回调采购商维修数据 : {}", bizBuyerOrder.getId(), JSON.toJSONString(robotResponse));

						Integer status = merchantCallBack(bizBuyerOrder, robotResponse);
						if (status.equals(200)) {// 成功回调, 则更新订单状态
							bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_SUCCESS.getType());
							bizBuyerOrder.setResult(JSON.toJSONString(robotResponse));
							bizBuyerOrder.setCallbackTime(LocalDateTime.now());
							bizBuyerOrderService.updateById(bizBuyerOrder);
						} else if (!status.equals(200) && times.get() >= 3) {// 三次失败状态
							bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
							bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType())));
							bizBuyerOrder.setCallbackTime(LocalDateTime.now());
							bizBuyerOrderService.updateById(bizBuyerOrder);
						}
						log.info("vin {{}} robotRequestCallBack 更新订单状态： {}", bizBuyerOrder.getVin(), RequestStatusEnum.getStatusEnumByCode(bizBuyerOrder.getRequestStatus()));
						return null;
					}, 3, 3000L, false);
				} catch (Exception e) {
					// 更新失败原因和失败状态： 回调失败
					log.error("callback yes : RetryUtil回调商户错误....");
					String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.CALLBACK_FAILURE.getType()));
					log.info("vin {{}} robotRequestCallBack 更新订单错误原因： {}", bizBuyerOrder.getVin(), failureReason); // 不修改状态,等待10分钟回调驳回
					bizBuyerOrder.setFailureReason(failureReason);
					bizBuyerOrderService.updateById(bizBuyerOrder);
				}
			}
		);
	}

	/***
	 * 给商家做回调请求
	 * @param bizBuyerOrder
	 * @param robotResponse
	 */
	public Integer merchantCallBack(BizBuyerOrder bizBuyerOrder, RobotResponse robotResponse) throws Exception {
		// 这里只有成功有记录的
		if (bizBuyerOrder.getOrderType().equals(BaseConstants.CHA_BO_SHI)) {// 查博士
			return sendChaBoss(bizBuyerOrder.getOrderNo(), 0, robotResponse);
		} else {
			log.info("#### merchantCallBack（成功回调商家）：开始");
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("order_id", bizBuyerOrder.getId());
			paramMap.put("maintain_data", robotResponse);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("code", CommonConstants.SUCCESS);
			resultMap.put("data", paramMap);

			log.info("#### merchantCallBack（成功回调商家）：给商家最终结果：{}", JSON.toJSONString(resultMap));
			HttpResponse result = HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(resultMap)).contentType("application/json").execute();
			Integer status = result.getStatus();
			String content = result.body();
			log.info("回调商户接口返回状态：{}, 返回内容：{}", status, content);
			return status;
		}
	}

	public Integer sendChaBoss(String orderNo, int status, Object object) throws Exception {
		String userId = "85";
		String keySecret = "ceea05985af94f6aaf0beccf051bde7e";
		CBSBuilder cbsBuilder = CBSBuilder.newCBSBuilder(userId, keySecret, false);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("orderno", orderNo);
		if (object != null){
			params.put("content", JSON.toJSONString(object));
		}
		params.put("reportstatus", status);
		String data = cbsBuilder.sendPost(chaboshi_url, params);
		JSONObject obj = JSON.parseObject(data);
		if (obj.getInteger("messageCode").equals(4010)) {
			return 200;
		} else {
			return 0;
		}
	}
}
