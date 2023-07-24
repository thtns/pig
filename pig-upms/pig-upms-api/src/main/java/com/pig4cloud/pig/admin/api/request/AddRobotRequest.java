package com.pig4cloud.pig.admin.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema
public class AddRobotRequest {


	/**
	 * 状态:启用/禁用
	 */
	@Schema(description ="状态:启用/禁用")
	private Boolean status;

	/**
	 * 机器人访问地址
	 */
	@Schema(description ="机器人访问地址")
	private String robotUrl;

	/**
	 * 代理后访问的ip+port
	 */
	@Schema(description ="host机器人访问的ip+端口")
	private String host;

	/**
	 * robotProxies
	 */
	@Schema(description ="robotProxies")
	private String robotProxies;

	/**
	 * 账号-密码（多个）
	 */
	@Schema(description ="账号-密码（多个）")
	private String robotAccountPassword;

	/**
	 * 代理名称
	 */
	@Schema(description ="代理名称")
	private String robotProxiesName;

	/**
	 * 是否需要代理 0；关闭 1，开启
	 */
	@Schema(description ="是否需要代理 0；关闭 1，开启")
	private Integer needDynamicProxy;

	/**
	 * 测试VIN码
	 */
	@Schema(description ="测试VIN码")
	private String testVin;

	/**
	 * 机器人服务开始时间
	 */
	@Schema(description ="机器人服务开始时间")
	private LocalDateTime serviceStartTime;

	/**
	 * 机器人服务结束时间
	 */
	@Schema(description ="机器人服务结束时间")
	private LocalDateTime serviceEndTime;


}
