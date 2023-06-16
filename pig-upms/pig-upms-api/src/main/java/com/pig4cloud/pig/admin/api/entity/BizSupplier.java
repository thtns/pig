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

/**
 * 供应商表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Data
@TableName("biz_supplier")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "供应商表")
public class BizSupplier extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 供应商名称
     */
    @Schema(description ="供应商名称")
    private String supplierName;

    /**
     * 负责人姓名
     */
    @Schema(description ="负责人姓名")
    private String directorName;

    /**
     * 联系方式
     */
    @Schema(description ="联系方式")
    private String contactMobile;

    /**
     * 每日限单量
     */
    @Schema(description ="每日限单量")
    private Integer dailyLimitCount;

    /**
     * 状态 0；关闭 1，开启
     */
    @Schema(description ="状态 0；关闭 1，开启")
    private Integer status;

    /**
     * 供应商logo
     */
    @Schema(description ="供应商logo")
    private String logo;


}
