package com.pig4cloud.pig.admin.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class AddCarBrandRequest {

	/**
	 * 车辆品牌
	 */
	@Schema(description ="车辆品牌")
	private String brand;

	/**
	 * 车辆品牌首字母
	 */
	@Schema(description ="车辆品牌首字母")
	private String letter;

	/**
	 * 车辆品牌logo地址
	 */
	@Schema(description ="车辆品牌logo地址")
	private String logoUrl;

	/**
	 * wmi即vin码前三位
	 */
	@Schema(description ="wmi即vin码前三位")
	private String wmi;

	/**
	 * 类型 10:国产,20:合资
	 */
	@Schema(description ="类型 10:国产,20:合资")
	private Integer type;


}
