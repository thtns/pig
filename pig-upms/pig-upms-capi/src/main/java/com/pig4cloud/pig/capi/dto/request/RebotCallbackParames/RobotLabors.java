package com.pig4cloud.pig.capi.dto.request.RebotCallbackParames;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotLabors {

	/**
	 * 子项类型(项目类型)
	 */
    private String itemType;

	/**
	 * 子项描述
	 */
    private String description;

	/**
	 * vin码 车架号
	 */
    private String vin;
}