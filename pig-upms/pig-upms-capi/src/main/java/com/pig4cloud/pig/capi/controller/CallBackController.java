package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.dto.request.RobotCallbackRequest;
import com.pig4cloud.pig.capi.dto.request.RobotCallbcakErroRequest;
import com.pig4cloud.pig.capi.service.*;
import com.pig4cloud.pig.capi.service.atripartite.CallBackManager;
import com.pig4cloud.pig.capi.service.atripartite.CallBackQuanManager;
import com.pig4cloud.pig.capi.service.atripartite.RedisOperationManager;
import com.pig4cloud.pig.capi.utils.rocketmq.ProducerUtil;
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

	private final CallBackService callBackService;

	private final MainCoreService mainCoreService;

	private final ProducerUtil producerUtil;


	/**
	 * 机器人查询结果回调
	 */
	@PostMapping("/yes")
	public R callback(HttpServletRequest request, @RequestBody RobotCallbackRequest rebotCallbackRequest) throws Exception {
		String ipAddr = RequestUtils.getIpAddress(request);
		log.info("callback yes ：机器人成功回调开始，参数：" + JSON.toJSONString(rebotCallbackRequest));
		log.info("callback yes ：机器人回调ip: {}", ipAddr);
		// 成功记录保存,并回调请求商户,并清楚redis的机器人队列
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
			callFailure(bizBuyerOrder, supplierId, robotError);
		} else {
			callFailure(bizBuyerOrder, supplierId, robotError);
		}
		return R.ok();
	}

	public void callFailure(BizBuyerOrder bizBuyerOrder, Long supplierId, RobotCallbcakErroRequest robotError) {
		// 清楚机器人redis占用队列
		callBackQuanManager.callBackQueueManage(bizBuyerOrder);
		// 其它错误回调
		log.info("错误回调给商家：供应商ID：" + supplierId);
		callBackManager.merchantCallBackErrorWithCode(bizBuyerOrder,
				RequestStatusEnum.CALLBACK_FAILURE,
				Objects.requireNonNull(RequestStatusEnum.getStatusEnumByCode(robotError.getCode())));

	}

	private void retryLogic(RobotCallbcakErroRequest robotError, BizBuyerOrder bizBuyerOrder) {
		// 判断是否重试过，如果重试过进行错误回调。没重试过，去重试。
		if (bizBuyerOrder.getRetryCount() > 1) {
			log.info("错误回调给商家：供应商ID：" + bizBuyerOrder.getSupplierId());
			callBackManager.merchantCallBackErrorWithCode(bizBuyerOrder,
					RequestStatusEnum.CALLBACK_SUCCESS,
					Objects.requireNonNull(RequestStatusEnum.getStatusEnumByCode(robotError.getCode())));
			callBackQuanManager.callBackQueueManage(bizBuyerOrder);
		} else {
			//重新发起请求
			maintenanceService.processMaintenanceOrder(bizBuyerOrder);
		}
	}

	@PostMapping("/success")
	public void success(HttpServletRequest request, @RequestBody RobotCallbackRequest rebotCallbackRequest) {
		String ipAddr = RequestUtils.getIpAddress(request);
		log.info("callback success ：机器人成功回调开始，参数：" + JSON.toJSONString(rebotCallbackRequest));
		log.info("callback success ：机器人回调ip: {}", ipAddr);
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(rebotCallbackRequest.getOrderId());
		callBackQuanManager.callBackQueueManage(bizBuyerOrder);// 移除机器人key
		// 成功记录保存,并回调请求商户,并清楚redis的机器人队列
		callBackService.success(rebotCallbackRequest);
	}

	@PostMapping("/fail")
	public void fail(@RequestBody RobotCallbcakErroRequest robotError) throws Exception {
		log.info("机器人失败回调开始，参数：" + JSON.toJSONString(robotError));
		Long orderId = JSON.parseObject(JSON.toJSONString(robotError.getData())).getLong("orderId");
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(orderId);
		Long supplierId = bizBuyerOrder.getSupplierId();
		String supplierName = bizBuyerOrder.getSupplierName();
		if (robotError.getCode() == RequestStatusEnum.SERVER_NO_RESULT.getType()) { // 无记录则回调
			callBackService.noData(bizBuyerOrder);// 无记录回调
		} else {
			if (robotError.getCode() == RequestStatusEnum.SERVER_LOGIN_FAILURE.getType() ||
					robotError.getCode() == RequestStatusEnum.REBOT_PROXY_CONNECTION_ERROR.getType()) {
				log.info("机器人错误回调，机器人出问题。下架供应商ID：{}, 供应商名称： {}" ,supplierId, supplierName);
				bizSupplierService.shutDownSupplier(supplierId);
				sendTimeOrderOnMinute(orderId);
			} else if (robotError.getCode() == RequestStatusEnum.SERVER_UNKNOWN_ERROR.getType()
					|| robotError.getCode() == RequestStatusEnum.REBOT_SSL_ERROR.getType()
					|| robotError.getCode() == RequestStatusEnum.REBOT_SYSTEM_CONNECTION_ERROR.getType()) {
				if (Boolean.TRUE.equals(redisOperationManager.redisOperationUnknownErrorKey(supplierId))) {
					log.info("机器人未知错误发生了3次，下架供应商ID：{}, 供应商名称： {}" ,supplierId, supplierName);
					bizSupplierService.shutDownSupplier(supplierId);
				}
				//重新发起请求
				sendTimeOrderOnMinute(orderId);
			} else if (robotError.getCode() == RequestStatusEnum.REBOT_READ_TIMEOUT_ERROR.getType()) {
				reDo(bizBuyerOrder);
			} else if (robotError.getCode() == RequestStatusEnum.SERVER_QUERY_FULL_ERROR.getType()){
				log.info("机器人查询以达到上线,下架供应商ID：{}, 供应商名称： {}" ,supplierId, supplierName);
				bizSupplierService.shutDownSupplier(supplierId);
				sendTimeOrderOnMinute(orderId);
			}else {
				reDo(bizBuyerOrder);
			}
		}
	}

	public void sendTimeOrderOnMinute(Long orderId){
		producerUtil.sendTimeMsg(String.valueOf(orderId), System.currentTimeMillis() + 60 * 1000);
	}

	public void reDo(BizBuyerOrder bizBuyerOrder){
		callBackQuanManager.callBackQueueManage(bizBuyerOrder);
		//重新发起请求
		sendTimeOrderOnMinute(bizBuyerOrder.getId());
	}
}
