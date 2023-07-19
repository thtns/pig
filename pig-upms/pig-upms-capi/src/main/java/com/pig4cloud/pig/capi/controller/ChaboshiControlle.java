package com.pig4cloud.pig.capi.controller;

import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.dto.ResultVo;
import com.pig4cloud.pig.capi.dto.chaboshi.req.PushOrderReq;
import com.pig4cloud.pig.capi.dto.chaboshi.res.PushOrderRes;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.service.MainCoreService;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.RequestUtils;
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
@RequestMapping("/chiBoss")
@Tag(name = "查博士订单推送")
public class ChaboshiControlle {

	private final MainCoreService mainCoreService;

	@PostMapping(value = "/push_order")
	public ResultVo pushOrder(HttpServletRequest request, @RequestBody PushOrderReq req) {
		log.info("chiBoss push_order, 参数为: {}", JSON.toJSONString(req));
		PushOrderRes res = new PushOrderRes();
		res.setOrderno(req.getOrderno());
		res.setVin(req.getVin());
		try {
			BizBuyerOrder bizBuyerOrder = BizBuyerOrder.builder()
					.orderNo(req.getOrderno())                  // 查博士订单号
					.buyerId(1001L)                              //用户id 查博士默认id
					.buyerName("查博士")                      //用户名称
					.requestIpAddress(RequestUtils.getIpAddress(request))                    //请求ip
					.carBrandName(req.getBrand())             //请求品牌
					.vin(req.getVin())                        //请求vin码
					.requestParams(JSON.toJSONString(req))    //请求参数
					.requestHeader(JSON.toJSONString(RequestUtils.getHeadersInfo(request)))    //请求头
					.requestTime(LocalDateTime.now())         //请求时间
					.requestStatus(RequestStatusEnum.ORDER_PLACING.getType())                //订单状态
					.retryCount(0)                            //重试次数
					.build();

			mainCoreService.placeOrder(bizBuyerOrder);

			if (RequestStatusEnum.ORDER_SUCCESS.getType().equals(bizBuyerOrder.getRequestStatus())) {
				return ResultVo.success(res);
			} else {
				return ResultVo.failed(res,"暂不支持");
			}
		} catch (Exception e) {
			log.error("查博士订单推送异常处理", e);
			return ResultVo.failed(res,"暂不支持");
		}
	}
}
