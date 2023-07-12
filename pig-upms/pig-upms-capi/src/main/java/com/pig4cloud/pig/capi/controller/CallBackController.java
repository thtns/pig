package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizSupplier;
import com.pig4cloud.pig.capi.request.RobotCallbackRequest;
import com.pig4cloud.pig.capi.request.RobotCallbcakErroRequest;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizSupplierService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import com.pig4cloud.pig.capi.service.atripartite.CallBackManager;
import com.pig4cloud.pig.capi.service.atripartite.CallBackQuanManager;
import com.pig4cloud.pig.capi.service.atripartite.RedisOperationManager;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/callback")
public class CallBackController {

	private final MaintenanceService maintenanceService;

	private final CallBackQuanManager callBackQuanManager;

	private final CallBackManager callBackManager;

	private final BizBuyerOrderService bizBuyerOrderService;

	private final BizSupplierService bizSupplierService;

	private final RedisOperationManager redisOperationManager;

	/**
	 * 延迟队列回调
	 */
	@PostMapping("/callback")
	public String callback(@RequestBody String body) {
		log.info("@@@@@@@延迟队列回调开始，参数body：" + JSON.toJSONString(body));
		BizBuyerOrder merchantOrderRecordDO = JSON.parseObject(body, BizBuyerOrder.class);
//        merchantOrderRecordDO.insert();
		maintenanceService.processMaintenanceOrder(merchantOrderRecordDO);
		log.info("@@@@@@@延迟队列回调结束");
		return "";
	}

	/**
	 * 机器人查询结果回调
	 */
	@PostMapping("/yes")
	public R callback(HttpServletRequest request, @RequestBody RobotCallbackRequest rebotCallbackRequest) throws Exception {
		String ipAddr = RequestUtils.getIpAddress(request);
		log.info("callback yes ：机器人成功回调开始，参数：" + JSON.toJSONString(rebotCallbackRequest));
		log.info("callback yes ：机器人回调ip: {}", ipAddr);
		maintenanceService.robotRequestCallBack(rebotCallbackRequest);

		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(rebotCallbackRequest.getOrderId());
		callBackQuanManager.callBackQueueManage(bizBuyerOrder);// 清楚机器人key
		return R.ok();
	}


	@PostMapping("/error")
	public R callbackError(@RequestBody RobotCallbcakErroRequest robotError) {
		log.info("机器人失败回调开始，参数：" + JSON.toJSONString(robotError));
		Long orderId = JSON.parseObject(JSON.toJSONString(robotError.getData())).getLong("orderId");
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(orderId);
		Long supplierId = bizBuyerOrder.getSupplierId();
		if (robotError.getCode() == RequestStatusEnum.SERVER_LOGIN_FAILURE.getType() ||
				robotError.getCode() == RequestStatusEnum.SERVER_QUERY_FULL_ERROR.getType()) {
			log.info("机器人错误回调，机器人出问题。下架供应商ID：" + supplierId);

			bizSupplierService.shutDownSupplier(supplierId);
			// 如果登錄失敗，先置成查詢失敗狀態
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.ORDER_FAILURE.getType());
			//发送钉钉消息提醒
			//更新bizBuyerOrder
			bizBuyerOrderService.updateById(bizBuyerOrder);
			//重新发起请求
			maintenanceService.processMaintenanceOrder(bizBuyerOrder);
		} else if (robotError.getCode() == RequestStatusEnum.SERVER_UNKNOWN_ERROR.getType()
				|| robotError.getCode() == RequestStatusEnum.REBOT_SSL_ERROR.getType()
				|| robotError.getCode() == RequestStatusEnum.REBOT_SYSTEM_CONNECTION_ERROR.getType()) {
			if (Boolean.TRUE.equals(redisOperationManager.redisOperationUnknownErrorKey(supplierId))) {
				// 下线供应商
				log.info("机器人未知错误发生了2次，下架供应商ID：" + supplierId);
				bizSupplierService.shutDownSupplier(supplierId);
			}
			retryLogic(robotError, bizBuyerOrder);
		} else if (robotError.getCode() == RequestStatusEnum.REBOT_PROXY_CONNECTION_ERROR.getType()
				|| robotError.getCode() == RequestStatusEnum.REBOT_READ_TIMEOUT_ERROR.getType()) {
			// 清楚机器人redis占用队列
			callBackQuanManager.callBackQueueManage(bizBuyerOrder);
			// 其它错误回调
			log.info("错误回调给商家：供应商ID：" + supplierId);
			callBackManager.merchantCallBackError(bizBuyerOrder,
					RequestStatusEnum.CALLBACK_SUCCESS,
					Objects.requireNonNull(RequestStatusEnum.getStatusEnumByCode(robotError.getCode())));
		} else {
			// 清楚机器人redis占用队列
			callBackQuanManager.callBackQueueManage(bizBuyerOrder);
			// 其它错误回调
			log.info("错误回调给商家：供应商ID：" + supplierId);
			callBackManager.merchantCallBackError(bizBuyerOrder,
					RequestStatusEnum.CALLBACK_SUCCESS,
					Objects.requireNonNull(RequestStatusEnum.getStatusEnumByCode(robotError.getCode())));
		}
		return R.ok();
	}

	private void retryLogic(RobotCallbcakErroRequest robotError, BizBuyerOrder bizBuyerOrder) {
		// 判断是否重试过，如果重试过进行错误回调。没重试过，去重试。
		if (bizBuyerOrder.getRetryCount() > 1) {
			log.info("错误回调给商家：供应商ID：" + bizBuyerOrder.getSupplierId());
			callBackManager.merchantCallBackError(bizBuyerOrder,
					RequestStatusEnum.CALLBACK_SUCCESS,
					Objects.requireNonNull(RequestStatusEnum.getStatusEnumByCode(robotError.getCode())));
			callBackQuanManager.callBackQueueManage(bizBuyerOrder);
		} else {
			//重新发起请求
			maintenanceService.processMaintenanceOrder(bizBuyerOrder);
		}
	}
}
