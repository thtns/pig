package com.pig4cloud.pig.capi.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.admin.api.entity.BizBuyer;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import com.pig4cloud.pig.common.core.exception.ErrorCodes;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/maintenance")
@Tag(name = "车辆维修模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class MaintenanceApi {

	/**
	 * 编码格式
	 */
	public static final String ENCODING = "utf-8";

	private static final String KEY_ALGORITHM = "AES";


	private final BizBuyerService bizBuyerService;


	@GetMapping(value = {"/order"})
	public R<BizBuyer> order() {
		String username = SecurityUtils.getUser().getUsername();
		BizBuyer custom = bizBuyerService.getOne(Wrappers.<BizBuyer>lambdaQuery()
				.eq(BizBuyer::getClientKey, username));
		if (custom == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
		}
		return R.ok(custom);
	}

	@GetMapping(value = {"/aesSecret"})
	public R<BizBuyer> aesSecret() {
		String username = SecurityUtils.getUser().getUsername();
		BizBuyer custom = bizBuyerService.getOne(Wrappers.<BizBuyer>lambdaQuery()
				.eq(BizBuyer::getClientKey, username));
		if (custom == null) {
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
		}

		return R.ok(custom);
	}


}
