package com.pig4cloud.pig.capi.dto.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class MaintenanceOrderRequest {

	@NotBlank(message = "VIN码不能为空")
	private String vin;

	@NotBlank(message = "回调地址不能为空")
	private String call_back_url;

	private String brand;

	private String engine_code;
}
