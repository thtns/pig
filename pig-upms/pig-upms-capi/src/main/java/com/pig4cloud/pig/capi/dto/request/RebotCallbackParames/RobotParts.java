package com.pig4cloud.pig.capi.dto.request.RebotCallbackParames;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotParts {
	/**
	 * 组件名称
	 */
    private String name;

	/**
	 * 数量
	 */
    private Integer count;

	/**
	 * 价格
	 */
    private String price;
}