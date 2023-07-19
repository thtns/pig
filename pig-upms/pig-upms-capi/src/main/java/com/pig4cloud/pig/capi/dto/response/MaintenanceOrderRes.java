package com.pig4cloud.pig.capi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaintenanceOrderRes {
	/**
	 * 车辆vin码
	 */
	private String vin;

	/**
	 * 订单id
	 */
	private Long order_id;
}
