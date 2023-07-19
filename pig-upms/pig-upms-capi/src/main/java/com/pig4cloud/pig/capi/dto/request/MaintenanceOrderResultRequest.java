package com.pig4cloud.pig.capi.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MaintenanceOrderResultRequest {

	@NotBlank(message = "orderId不能为空")
	@NotNull(message = "orderId不能为空")
	private Long order_id;


}
