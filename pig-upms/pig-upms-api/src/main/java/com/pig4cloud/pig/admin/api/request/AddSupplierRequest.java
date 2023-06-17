package com.pig4cloud.pig.admin.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class AddSupplierRequest {


	/**
	 * 供应商名称
	 */
	@Schema(description ="供应商名称")
	private String supplierName;

	/**
	 * 负责人姓名
	 */
	@Schema(description ="负责人姓名")
	private String directorName;

	/**
	 * 联系方式
	 */
	@Schema(description ="联系方式")
	private String contactMobile;

	/**
	 * 每日限单量
	 */
	@Schema(description ="每日限单量")
	private Integer dailyLimitCount;

	/**
	 * 状态 0；关闭 1，开启
	 */
	@Schema(description ="状态 0；关闭 1，开启")
	private Integer status;

	/**
	 * 供应商logo
	 */
	@Schema(description ="供应商logo")
	private String logo;






}
