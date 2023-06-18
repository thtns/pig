package com.pig4cloud.pig.admin.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema
public class AddBuyerRequest {


	/**
	 * 采购商名称
	 */
	@Schema(description ="采购商名称")
	private String name;

	/**
	 * 注册手机号
	 */
	@Schema(description ="注册手机号")
	private String mobile;

	/**
	 * 负责人名称
	 */
	@Schema(description ="负责人名称")
	private String directorName;

	/**
	 * 负责人联系方式
	 */
	@Schema(description ="负责人联系方式")
	private String directorMobile;

	/**
	 * 每日限单数量
	 */
	@Schema(description ="每日限单数量")
	private Integer dailylimitCount;

	/**
	 * 状态:正常/禁用/已过期
	 */
	@Schema(description ="状态:正常/禁用/已过期")
	private Integer status;

	/**
	 * 有效期开始日期
	 */
	@Schema(description ="有效期开始日期")
	private LocalDateTime validityStart;

	/**
	 * 有效期结束日期
	 */
	@Schema(description ="有效期结束日期")
	private LocalDateTime validityEnd;

	/**
	 * 备注
	 */
	@Schema(description ="备注")
	private String remark;


}
