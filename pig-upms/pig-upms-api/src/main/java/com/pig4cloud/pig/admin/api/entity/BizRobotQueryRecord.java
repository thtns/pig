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
 * 机器人查询记录表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("biz_robot_query_record")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "机器人查询记录表")
public class BizRobotQueryRecord extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

	/**
	 * vin码
	 */
	@Schema(description ="vin")
	private String vin;

    /**
     * 供应商ID
     */
    @Schema(description ="供应商ID")
    private Long supplierId;

    /**
     * 供应商名称
     */
    @Schema(description ="供应商名称")
    private String supplierName;

    /**
     * 机器人id
     */
    @Schema(description ="机器人id")
    private String robotId;

    /**
     * 结果状态码：0：失败，1：成功，2：无结果
     */
    @Schema(description ="结果状态码：0：失败，1：成功，2：无结果")
    private Integer resultStatus;

    /**
     * 失败原因
     */
    @Schema(description ="失败原因")
    private String failureReason;

    /**
     * 成功结果
     */
    @Schema(description ="成功结果")
    private String result;

    /**
     * 查询时间
     */
    @Schema(description ="查询时间")
    private LocalDateTime querytime;


}
