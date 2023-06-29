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

/**
 * 品牌供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("biz_car_brand_supplier")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "品牌供应商关系表")
public class BizCarBrandSupplier extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

    /**
     * 品牌id
     */
    @Schema(description ="品牌id")
    private Long carBrandId;

    /**
     * 供应商id
     */
    @Schema(description ="供应商id")
    private Long supplierId;


}
