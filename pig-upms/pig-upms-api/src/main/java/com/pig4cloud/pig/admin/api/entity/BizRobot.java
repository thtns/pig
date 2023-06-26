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
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Time;
import java.time.LocalDateTime;

/**
 * 机器人
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Data
@TableName("biz_robot")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "机器人")
public class BizRobot extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

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
    private Time serviceStartTime;

    /**
     * 机器人服务结束时间
     */
    @Schema(description ="机器人服务结束时间")
    private Time serviceEndTime;


}
