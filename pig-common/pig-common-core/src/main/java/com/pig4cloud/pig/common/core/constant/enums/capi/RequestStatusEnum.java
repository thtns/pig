package com.pig4cloud.pig.common.core.constant.enums.capi;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author Admin
 * @date 2020-03-28 10:34
 */
@Getter
@RequiredArgsConstructor
public enum RequestStatusEnum {

	/**
	 * 下单成功
	 */
    ORDER_SUCCESS(0, "已下单"),

	/**
	 * 下单失败
	 */
    ORDER_FAILURE(1,"下单失败"),

	/**
	 * 查询中
	 */
    QUERYING(2, "查询中"),

	/**
	 * 回调成功
	 */
    CALLBACK_SUCCESS(3, "回调成功"),

	/**
	 * 回调失败
	 */
    CALLBACK_FAILURE(4, "回调失败"),

	/**
	 * 此时段不支持查询,请联系客服人员
	 */
    TIME_NO_SUPPORT(5, "此时段不支持查询,请联系客服人员"),
//    PARSE_VIN_FAILURE(5, "VIN码解析失败"),
//    BRAND_UPPER_LIMIT(6, "品牌查询已上限"),
    ;

	/**
	 * 类型
	 */
	private final Integer type;

	/**
	 * 描述
	 */
	private final String description;
}
