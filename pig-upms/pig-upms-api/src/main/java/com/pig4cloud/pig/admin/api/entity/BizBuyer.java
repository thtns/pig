/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 采购商表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("biz_buyer")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "采购商表")
public class BizBuyer extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 商家API对接key
     */
    @Schema(description ="商家API对接key")
    private String clientKey;

    /**
     * 商家API对接secret
     */
    @Schema(description ="商家API对接secret")
    private String clientSecret;

	/**
	 * 商家API加密后secret
	 */
	@Schema(description ="商家API加密后aecSecret")
	private String aecSecret;

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
