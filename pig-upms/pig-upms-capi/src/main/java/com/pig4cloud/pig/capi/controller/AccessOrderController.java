package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.dto.ResultVo;
import com.pig4cloud.pig.capi.dto.access.req.PushOrderReq;
import com.pig4cloud.pig.capi.dto.access.req.PushOrderVinReq;
import com.pig4cloud.pig.capi.dto.access.res.PushOrderRes;
import com.pig4cloud.pig.capi.entity.BizBuyer;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import com.pig4cloud.pig.capi.service.MainCoreService;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/access")
@Tag(name = "接入订单推送")
public class AccessOrderController {

	private final BizBuyerService bizBuyerService;


	private final MainCoreService mainCoreService;

	@PostMapping(value = "/push_order")
	public ResultVo pushOrder(HttpServletRequest request, @RequestBody PushOrderReq req) {
		log.info("Access push_order, 参数为: {}", JSON.toJSONString(req));
		PushOrderRes res = new PushOrderRes();
		res.setOrderno(req.getOrderno());
		res.setVin(req.getVin());
		String key = request.getHeader("app_key");
		String secret = request.getHeader("secret_key");
		BizBuyer bizBuyer = bizBuyerService.getByAkSk(key, secret);
		if (bizBuyer == null) {
			return ResultVo.failed(res, "非法秘钥,请检查秘钥。");
		}
		try {
			if ("雪佛兰".equals(req.getBrand()) || "别克".equals(req.getBrand()) || "凯迪拉克".equals(req.getBrand()) || "日产".equals(req.getBrand())
					|| "丰田".equals(req.getBrand())) {
				return ResultVo.failed(res, "暂不支持");
			}
			BizBuyerOrder bizBuyerOrder = BizBuyerOrder.builder()
					.orderNo(req.getOrderno())                //订单号
					.buyerId(bizBuyer.getId())                //用户id
					.buyerName(bizBuyer.getName())            //用户名称
					.carBrandName(req.getBrand())             //请求品牌
					.manufacturer(req.getManufacturer())      //请求厂商
					.vin(req.getVin())                        //请求vin码
					.engineCode(req.getEngineNo())              //发动机号
					.callbackUrl(req.getCallbackurl())        //回调地址
					.requestParams(JSON.toJSONString(req))    //请求参数
					.requestTime(LocalDateTime.now())         //请求时间
					.requestStatus(RequestStatusEnum.ORDER_PLACING.getType())                //订单状态
					.orderType(BaseConstants.ACCESS)          //订单类型 默认接入类型
					.retryCount(0)                            //重试次数
					.build();

			mainCoreService.placeOrder(bizBuyerOrder);

			if (RequestStatusEnum.ORDER_SUCCESS.getType().equals(bizBuyerOrder.getRequestStatus())) {
				return ResultVo.success(res);
			} else {
				return ResultVo.failed(res, "暂不支持");
			}
		} catch (Exception e) {
			log.error("Access push_order 订单推送异常处理", e);
			return ResultVo.failed(res, "暂不支持");
		}
	}

	@PostMapping(value = "/push_order_vin")
	public ResultVo pushOrderOnlyVin(HttpServletRequest request, @RequestBody PushOrderVinReq req) {
		log.info("Access push_order, 参数为: {}", JSON.toJSONString(req));
		PushOrderRes res = new PushOrderRes();
		res.setOrderno(req.getOrderno());
		res.setVin(req.getVin());
		String key = request.getHeader("app_key");
		String secret = request.getHeader("secret_key");
		BizBuyer bizBuyer = bizBuyerService.getByAkSk(key, secret);
		if (bizBuyer == null) {
			return ResultVo.failed(res, "非法秘钥,请检查秘钥。");
		}
		try {
			BizBuyerOrder bizBuyerOrder = BizBuyerOrder.builder()
					.orderNo(req.getOrderno())                //订单号
					.buyerId(bizBuyer.getId())                //用户id
					.buyerName(bizBuyer.getName())            //用户名称
					.vin(req.getVin())                        //请求vin码
					.engineCode(req.getEngineNo())              //发动机号 因为精友数据里不包含发动机号，所以需要发动机号的单独发送
					.callbackUrl(req.getCallbackurl())        //回调地址
					.requestParams(JSON.toJSONString(req))    //请求参数
					.requestTime(LocalDateTime.now())         //请求时间
					.requestStatus(RequestStatusEnum.ORDER_PLACING.getType())                //订单状态
					.orderType(BaseConstants.ACCESS)          //订单类型 默认接入类型
					.retryCount(0)                            //重试次数
					.build();

			mainCoreService.placeOrder(bizBuyerOrder);

			if (RequestStatusEnum.ORDER_SUCCESS.getType().equals(bizBuyerOrder.getRequestStatus())) {
				return ResultVo.success(res);
			} else {
				return ResultVo.failed(res, "暂不支持");
			}
		} catch (Exception e) {
			log.error("Access push_order 订单推送异常处理", e);
			return ResultVo.failed(res, "暂不支持");
		}
	}
}
