package com.pig4cloud.pig.capi.dto.request;

import com.pig4cloud.pig.capi.dto.request.RebotCallbackParames.RobotResponse;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RobotCallback2Request {

	//orderId
	private Long order_id;

	@NotNull(message = "维修数据")
	// 维修数据
	private RobotResponse maintain_data;
}
