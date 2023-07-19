package com.pig4cloud.pig.common.core.constant.enums.capi;

public interface BaseConstants {

	/**
	 * 机器人需要代理 1 需要 0不需要
	 */
	Integer NEED_PROXY = 1;

	Integer NOT_NEED_PROXY = 0;

	/**
	 * 0：失败，1：成功，2：无结果
	 */
	Integer ROBOT_QUERY_STATUS_FAIL = 0;
	Integer ROBOT_QUERY_STATUS_SUCCESS = 1;
	Integer ROBOT_QUERY_STATUS_NO_RESULT= 2;

	String CHA_BO_SHI = "chaboshi";
}
