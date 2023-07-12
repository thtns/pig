package com.pig4cloud.pig.capi.service.atripartite;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.request.RebotCallbackParames.RobotResponse;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizCarBrandService;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import com.pig4cloud.pig.capi.service.apo.RedisKeyDefine;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@RequiredArgsConstructor
public class CallBackManager {

	/*** 机器人查询记录 **/
	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	/*** 采购商记录 **/
	private final BizBuyerOrderService bizBuyerOrderService;



	/***
	 * 给商家做回调请求
	 * @param bizBuyerOrder
	 * @param robotResponse
	 */
	public String merchantCallBack(BizBuyerOrder bizBuyerOrder, RobotResponse robotResponse) {
		// 如果回调明细为空，则回调查无记录
		if (Objects.isNull(robotResponse)) {
			return merchantCallBackErrorWithCode(bizBuyerOrder, RequestStatusEnum.CALLBACK_NO_RESULT, RequestStatusEnum.CALLBACK_NO_RESULT);
		}
		log.info("#### merchantCallBack（成功回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("order_id", bizBuyerOrder.getId());
		paramMap.put("maintain_data", robotResponse);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("code", CommonConstants.SUCCESS);
		resultMap.put("data", paramMap);

		log.info("#### merchantCallBack（成功回调商家）：给商家最终结果：{}", JSON.toJSONString(resultMap));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(resultMap)).contentType("application/json").execute().body();
	}

	/**
	 * 给商家做错误回调请求
	 *
	 * @param bizBuyerOrder 采购商订单
	 * @param statusCode    请求状态
	 * @param failCode      错误编码
	 * @return
	 */
	public String merchantCallBackErrorWithCode(BizBuyerOrder bizBuyerOrder, RequestStatusEnum statusCode, RequestStatusEnum failCode) {
		String failureReasonStr = JSON.toJSONString(R.resultEnumType(null, failCode.getType()));
		//更新采购订单id
		bizBuyerOrder.setRequestStatus(statusCode.getType());
		bizBuyerOrder.setFailureReason(failureReasonStr);
		bizBuyerOrder.setCallbackTime(LocalDateTime.now());
		bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);

		log.info("#### merchantCallBackErrorWithCode 异常回调订单信息 ：" + JSON.toJSONString(bizBuyerOrder));
		if (failCode.getType().equals(RequestStatusEnum.SERVER_NO_RESULT.getType())) {//机器人无记录的话
			//调查无记录,存储本次查询
			bizRobotQueryRecordService.save(
					BizRobotQueryRecord.builder()
							.vin(bizBuyerOrder.getVin())
							.robotId(bizBuyerOrder
									.getRobotId())
							.supplierId(bizBuyerOrder
									.getSupplierId())
							.supplierName(bizBuyerOrder.getSupplierName())
							.resultStatus(RequestStatusEnum.CALLBACK_NO_RESULT.getType()) // 0失败,1成功，5无记录
							.failureReason(failureReasonStr)          // 失败原因； 如果成功的话则需要设置result
							.querytime(LocalDateTime.now()).build()); // 当前时间
		}

		log.info("#### merchantCallBackErrorWithCode（失败回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("order_id", bizBuyerOrder.getId());
//		paramMap.put("maintain_data", null);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("code", failCode.getType());
		resultMap.put("msg", RequestStatusEnum.getDesc(failCode.getType()));
		resultMap.put("data", paramMap);

		log.info("#### merchantCallBackErrorWithCode（失败回调商家）：给商家最终结果：{}", JSON.toJSONString(resultMap));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(resultMap)).contentType("application/json").execute().body();
	}

	public String merchantCallBackError(BizBuyerOrder bizBuyerOrder) {
		log.info("#### merchantCallBackError 异常回调订单信息 ：" + JSON.toJSONString(bizBuyerOrder));
		log.info("#### merchantCallBackError（失败回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("order_id", bizBuyerOrder.getId());

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("code", RequestStatusEnum.API_ORDER_FAILURE.getType());
		resultMap.put("msg", bizBuyerOrder.getFailureReason());
		resultMap.put("data", paramMap);

		log.info("#### merchantCallBackError（失败回调商家）：给商家最终结果" + JSON.toJSONString(resultMap));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(resultMap)).contentType("application/json").execute().body();
	}


}
