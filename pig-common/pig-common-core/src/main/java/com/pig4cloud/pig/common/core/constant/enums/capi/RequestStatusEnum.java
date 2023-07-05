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

	/*** 下单成功*/
    ORDER_SUCCESS(0, "已下单"),

	/*** 下单失败*/
    ORDER_FAILURE(1,"下单失败"),

	/*** 查询中*/
    QUERYING(2, "查询中"),

	/*** 回调成功*/
    CALLBACK_SUCCESS(3, "回调成功"),

	/*** 回调失败*/
    CALLBACK_FAILURE(4, "回调失败"),


//    PARSE_VIN_FAILURE(5, "VIN码解析失败"),
//    BRAND_UPPER_LIMIT(6, "品牌查询已上限"),
	/*** 此品牌暂不支持查询 */
	API_BRAND_NONSUPPORT( 1001, "此品牌暂不支持查询."),

	/*** 此时间段不支持查询，请联系客服.. */
	API_TIME_NONSUPPORT( 1002, "此时间段不支持查询，请联系客服.."),

	/*** vin码无法识别. */
	API_VIN_UNIDENTIFIABLE( 1003, "vin码无法识别."),

	/*** 下单失败，请联系元素客服. */
	API_ORDER_FAILURE( 1004, "下单失败，请联系元素客服."),

	/*** 回调商家失败. */
	API_CALLBACK_FAILURE( 1005, "回调商家失败."),

	/*** 未知错误. */
	SERVER_UNKNOWN_ERROR( 1200, "未知错误."),

	/*** 系统错误. */
	SERVER_SYSTEM_ERROR( 1201, "系统错误."),

	/*** 系统登录失败. */
	SERVER_LOGIN_FAILURE( 1202, "系统登录失败."),

	/*** 查询记录失败. */
	SERVER_QUERY_FAILURE( 1203, "查询记录失败."),

	/*** 查询无记录. */
	SERVER_NO_RESULT( 1204, "查询无记录."),

	/*** 查询超量失败. */
	SERVER_QUERY_FULL_ERROR( 1208, "查询超量失败."),

	/*** 代理连接异常. */
	REBOT_PROXY_CONNECTION_ERROR( 1240, "代理连接异常."),

	/*** 请求超时. */
	REBOT_READ_TIMEOUT_ERROR( 1241, "请求超时."),

	/*** SSL验证失败. */
	REBOT_SSL_ERROR( 1242, "SSL验证失败."),

	/*** 系统连接异常. */
	REBOT_SYSTEM_CONNECTION_ERROR( 1243, "系统连接异常."),

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

}
