package com.pig4cloud.pig.capi.dto.access.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PushOrderVinReq {

	@NotBlank(message = "orderno不能为空")
	@NotNull(message = "orderno不能为空")
	String orderno;

	@NotBlank(message = "vin不能为空")
	@NotNull(message = "vin不能为空")
	private String vin;

	private String brand;

	private String manufacturer;

	@NotBlank(message = "callbackurl不能为空")
	@NotNull(message = "callbackurl不能为空")
	private String callbackurl;

	// 发动机号
	private String engineNo;
}
