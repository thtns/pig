package com.pig4cloud.pig.admin.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema
public class AddBizCarBrandSupplierRequest {

	@Schema(description = "品牌id")
	private Long carBrandId;

	@Schema(description = "供应商ids")
	private List<Long> supplierIds;


}
