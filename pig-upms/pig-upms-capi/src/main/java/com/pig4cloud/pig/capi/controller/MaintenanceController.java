package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.entity.BizBuyer;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.dto.request.MaintenanceOrderRequest;
import com.pig4cloud.pig.capi.dto.request.MaintenanceOrderResultRequest;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RequestUtils;
import com.pig4cloud.pig.common.core.util.UUidUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/maintenance")
@Tag(name = "车辆维修数据查询")
public class MaintenanceController {


	private final BizBuyerService bizBuyerService;

	private final BizBuyerOrderService bizBuyerOrderService;

	private final MaintenanceService maintenanceService;


	@PostMapping(value = {"/order"})
	public R order(HttpServletRequest request, @RequestBody MaintenanceOrderRequest req) {
		log.info("~~~~ Step1: 准备下单, 参数为: {}", JSON.toJSONString(req));
		String key = request.getHeader("app_key");
		String secret = request.getHeader("secret_key");
		if (StringUtils.isEmpty(key)||StringUtils.isEmpty(key)){
			return R.resultEnumType(null,RequestStatusEnum.MISS_REQUIRED_PARAMETERS.getType());
		}
		BizBuyer bizBuyer = bizBuyerService.getByAkSk(key, secret);
		Long orderId = UUidUtils.uuLongId();
		log.info("~~~~ Step1.1: 开始构建下单数据...");
		BizBuyerOrder bizBuyerOrder = BizBuyerOrder.builder()
				.id(orderId)                              //订单id
				.buyerId(bizBuyer.getId())                //用户id
				.buyerName(bizBuyer.getName())            //用户名称
				.requestIpAddress(RequestUtils.getIpAddress(request))                    //请求ip
				.carBrandName(req.getBrand())             //请求品牌
				.vin(req.getVin())                        //请求vin码
				.engineCode(req.getEngine_code())         //发动机码
				.callbackUrl(req.getCall_back_url())      //回调地址
				.requestParams(JSON.toJSONString(req))    //请求参数
				.requestHeader(JSON.toJSONString(RequestUtils.getHeadersInfo(request)))    //请求头
				.requestTime(LocalDateTime.now())         //请求时间
				.requestStatus(RequestStatusEnum.ORDER_SUCCESS.getType())                //订单状态
				.retryCount(0)                            //重试次数
				.build();
		log.info("~~~~ Step1.2: 构建下单数据结束.");
		return maintenanceService.processMaintenanceOrder(bizBuyerOrder);
	}

	@PostMapping(value = {"/result"})
	public R result(@RequestBody MaintenanceOrderResultRequest req) {
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(req.getOrder_id());
		if (bizBuyerOrder == null) {
			return R.failed("order_does_not_exist");
		} else {
			if (!RequestStatusEnum.QUERYING.equals(bizBuyerOrder.getRequestStatus()) || !RequestStatusEnum.CALLBACK_SUCCESS.equals(bizBuyerOrder.getRequestStatus())) {
				//这里需要给用户错误回调,不应该继续向里写了
			}
			maintenanceService.getResult(bizBuyerOrder);
		}
		return R.ok();
	}


}
