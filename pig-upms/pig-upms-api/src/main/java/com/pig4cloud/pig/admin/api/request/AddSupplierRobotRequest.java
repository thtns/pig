package com.pig4cloud.pig.admin.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class AddSupplierRobotRequest {


	@Schema(description = "供应商id")
	private Long supplierId;

	@Schema(description = "机器人ids")
	private List<Long> robotIds;


}
