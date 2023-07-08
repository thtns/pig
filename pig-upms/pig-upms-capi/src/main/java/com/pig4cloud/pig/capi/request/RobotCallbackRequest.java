package com.pig4cloud.pig.capi.request;

import com.pig4cloud.pig.capi.request.RebotCallbackParames.RobotResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RobotCallbackRequest {

	@NotBlank(message = "orderId不能为空")
	@NotNull(message = "orderId不能为空")
	private Long orderId;

	@NotNull(message = "维修数据")
	private RobotResponse robotResponse;
}
