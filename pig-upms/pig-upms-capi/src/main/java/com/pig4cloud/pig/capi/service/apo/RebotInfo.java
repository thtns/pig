package com.pig4cloud.pig.capi.service.apo;

import com.pig4cloud.pig.capi.entity.BizRobot;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
public class RebotInfo extends BizRobot {

	/**
	 * 供应商ID
	 */
	private Long supplierId;

	/**
	 * 供应商名称
	 */
	private String supplierName;
}
