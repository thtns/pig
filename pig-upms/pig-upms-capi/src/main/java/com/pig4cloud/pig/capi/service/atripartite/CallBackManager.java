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
			return merchantCallBackError(bizBuyerOrder, RequestStatusEnum.CALLBACK_NO_RESULT, RequestStatusEnum.CALLBACK_NO_RESULT);
		}
		log.info("####第三步（回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("order_id", bizBuyerOrder.getId());
		paramMap.put("maintain_data", robotResponse);
		R<Map<String, Object>> result = R.ok(paramMap);

		log.info("####第四步（回调商家）：给商家最终结果" + JSON.toJSONString(result));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(result)).contentType("application/json").execute().body();
	}

	/**
	 * 给商家做错误回调请求
	 *
	 * @param bizBuyerOrder 采购商订单
	 * @param statusCode    请求状态
	 * @param failCode      错误编码
	 * @return
	 */
	public String merchantCallBackError(BizBuyerOrder bizBuyerOrder, RequestStatusEnum statusCode, RequestStatusEnum failCode) {
		String failureReasonStr = JSON.toJSONString(R.resultEnumType(null, failCode.getType()));
		//更新采购订单id
		bizBuyerOrder.setRequestStatus(statusCode.getType());
		bizBuyerOrder.setFailureReason(failureReasonStr);
		bizBuyerOrder.setCallbackTime(LocalDateTime.now());
		bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);

		log.info("异常回调merchantOrderRecordDO ：" + JSON.toJSONString(bizBuyerOrder));
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

		log.info("####第三步（回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("order_id", bizBuyerOrder.getId());
		paramMap.put("maintain_data", null);
		R<Map<String, Object>> result = R.resultEnumType(paramMap, failCode.getType());

		log.info("####第四步（回调商家）：给商家最终结果" + JSON.toJSONString(result));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(result)).contentType("application/json").execute().body();
	}


}
