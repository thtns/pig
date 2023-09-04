package com.pig4cloud.pig.admin.controller.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyerOrderExcelVo {

	@ExcelProperty("品牌名称")
	private String carBrandName;

	@ExcelProperty("厂商名称")
	private String manufacturer;

	@ExcelProperty("订单号")
	private String orderNo;

	@ExcelProperty("推送时间")
	private LocalDateTime requestTime;

	@ExcelProperty("VIN")
	private String vin;

	@ExcelProperty("价格")
	private String price = "10";
}
