package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.admin.api.entity.BizBuyer;
import com.pig4cloud.pig.admin.api.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import com.pig4cloud.pig.capi.vo.OrderReqVo;
import com.pig4cloud.pig.capi.vo.OrderResVo;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RequestUtils;
import com.pig4cloud.pig.common.core.util.UUidUtils;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/maintenance")
@Tag(name = "车辆维修数据查询")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class MaintenanceApi {


	private final BizBuyerService bizBuyerService;

	private final BizBuyerOrderService bizBuyerOrderService;


	@PostMapping(value = {"/order"})
	public R<OrderResVo> order(HttpServletRequest request, @RequestBody OrderReqVo req) {
		log.info("~~~~第一步：下单参数:"+ JSON.toJSONString(req));
		String ip = RequestUtils.getIpAddress(request);
		String username = SecurityUtils.getUser().getUsername();
		BizBuyer custom = bizBuyerService.getOne(Wrappers.<BizBuyer>lambdaQuery()
				.eq(BizBuyer::getClientKey, username));
		if (custom == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.INVALID_PARAMETER_ERROR));
		}
		Long orderId = Long.valueOf(UUidUtils.uuid());
		BizBuyerOrder bizBuyerOrder =  BizBuyerOrder.builder()
				.id(orderId)  							//订单id
				.buyerId(custom.getId())				//用户id
				.buyerName(custom.getName())			//用户名称
				.requestIpAddress(ip)					//请求ip
				.carBrandName(req.getBrand())			//请求品牌
				.vin(req.getVin())						//请求vin码
				.engineCode(req.getEngine_code())		//发动机码
				.callbackUrl(req.getCall_back_url())    //回调地址
				.requestParams(JSON.toJSONString(req))  //请求参数
				.requestHeader(JSON.toJSONString(RequestUtils.getHeadersInfo(request)))	//请求头
				.requestTime(LocalDateTime.now())		//请求时间
				.requestStatus(RequestStatusEnum.ORDER_SUCCESS.getType())				//订单状态
				.retryCount(0)							//重试次数
				.build();

		return R.ok(bizBuyerOrderService.maintenanceOrder(bizBuyerOrder));

	}


}
