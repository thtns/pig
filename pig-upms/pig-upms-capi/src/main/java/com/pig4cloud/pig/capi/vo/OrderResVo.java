package com.pig4cloud.pig.capi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResVo {
	/**
	 * 车辆vin码
	 */
	private String vin;

	/**
	 * 订单id
	 */
	private String order_id;
}
