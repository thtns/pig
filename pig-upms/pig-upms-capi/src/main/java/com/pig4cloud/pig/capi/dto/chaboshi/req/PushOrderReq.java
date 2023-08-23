package com.pig4cloud.pig.capi.dto.chaboshi.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PushOrderReq {

	@NotBlank(message = "orderno不能为空")
	@NotNull(message = "orderno不能为空")
	String orderno;

	@NotBlank(message = "vin不能为空")
	@NotNull(message = "vin不能为空")
	private String vin;

	@NotBlank(message = "brand不能为空")
	@NotNull(message = "brand不能为空")
	private String brand;

	@NotBlank(message = "manufacturer不能为空")
	@NotNull(message = "manufacturer不能为空")
	private String manufacturer;

	@NotBlank(message = "pushtime不能为空")
	@NotNull(message = "pushtime不能为空")
	private String pushtime;

	// 发动机号
	private String engineNo;


}
