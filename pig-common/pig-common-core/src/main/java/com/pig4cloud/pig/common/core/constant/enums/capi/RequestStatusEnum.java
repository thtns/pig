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

	/*** 下单失败*/
	ORDER_FAILURE(-100,"下单失败"),

	/*** 下单中*/
    ORDER_PLACING(100, "下单中"),

	/*** 下单成功*/
    ORDER_SUCCESS(101, "已下单"),

	/*** 查询中*/
    QUERYING(102, "查询中"),

	/*** 回调成功*/
    CALLBACK_SUCCESS(103, "回调成功"),

	/*** 回调失败*/
    CALLBACK_FAILURE(104, "回调失败"),

	/*** 查询无记录. */
	CALLBACK_NO_RESULT( 105, "查询无记录."),





//    PARSE_VIN_FAILURE(5, "VIN码解析失败"),
//    BRAND_UPPER_LIMIT(6, "品牌查询已上限"),
	/*** 此品牌暂不支持查询 */
	API_BRAND_NONSUPPORT( 4001, "此品牌暂不支持查询."),

	/*** 此时间段不支持查询，请联系客服.. */
	API_TIME_NONSUPPORT( 4002, "此时间段不支持查询，请联系客服.."),

	/*** vin码无法识别. */
	API_VIN_UNIDENTIFIABLE( 4003, "vin码无法识别."),

	/*** 下单失败，请联系元素客服. */
	API_ORDER_FAILURE( 4004, "下单失败，请联系元素客服."),

	/*** 回调商家失败. */
	API_CALLBACK_FAILURE( 4005, "回调商家失败."),

	/*** 订单超时. */
	API_ORDER_LONG_TIME( 4006, "订单超时驳回."),

	/*** 未知错误. */
	SERVER_UNKNOWN_ERROR( 4200, "未知错误."),

	/*** 系统错误. */
	SERVER_SYSTEM_ERROR( 4201, "系统错误."),

	/*** 系统登录失败. */
	SERVER_LOGIN_FAILURE( 4202, "系统登录失败."),

	/*** 查询记录失败. */
	SERVER_QUERY_FAILURE( 4203, "查询记录失败."),

	/*** 查询无记录. */
	SERVER_NO_RESULT( 4204, "查询无记录."),

	/*** 查询超量失败. */
	SERVER_QUERY_FULL_ERROR( 4208, "查询超量失败."),

	/*** 代理连接异常. */
	REBOT_PROXY_CONNECTION_ERROR( 4240, "代理连接异常."),

	/*** 请求超时. */
	REBOT_READ_TIMEOUT_ERROR( 4241, "请求超时."),

	/*** SSL验证失败. */
	REBOT_SSL_ERROR( 4242, "SSL验证失败."),

	/*** 系统连接异常. */
	REBOT_SYSTEM_CONNECTION_ERROR( 4243, "系统连接异常."),

	/*** 缺少必填参数. */
	MISS_REQUIRED_PARAMETERS( 4244, "缺少必填秘钥参数."),

	/*** 缺少必填参数. */
	AKSK_FAIL( 4244, "无效的秘钥."),

	/*** 您的IP不是白名单. */
	IP_ERROR( 1901, "您的IP不是白名单."),
    ;



	/**
	 * 类型
	 */
	private final Integer type;

	/**
	 * 描述
	 */
	private final String description;

	public static String getDesc(Integer code) {
		for (RequestStatusEnum ele : values()) {
			if(ele.getType().equals(code)) return ele.getDescription();
		}
		return null;
	}

	public static RequestStatusEnum getStatusEnumByCode(Integer code) {
		for (RequestStatusEnum ele : values()) {
			if(ele.getType().equals(code)) return ele;
		}
		return null;
	}

}
