package com.pig4cloud.pig.capi.dto.request;

import lombok.Data;

@Data
public class RobotCallbcakErroRequest {
	/***
	 * {
	 *    "code": code,
	 *    "msg": message,
	 *    "data": {"orderId": task_id}
	 * }
	 */

	private int code;
	private String msg;
	private Object data;


}
