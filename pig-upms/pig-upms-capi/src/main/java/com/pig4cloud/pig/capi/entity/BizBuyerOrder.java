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
package com.pig4cloud.pig.capi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 采购商订单表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Data
@TableName("biz_buyer_order")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "采购商订单表")
public class BizBuyerOrder extends BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description ="主键id")
    private Long id;

	@Schema(description ="三方订单编号")
	private String orderNo;

    /**
     * 采购商Id
     */
    @Schema(description ="采购商Id")
    private Long buyerId;

    /**
     * 采购商名称
     */
    @Schema(description ="采购商名称")
    private String buyerName;

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
     * 品牌id
     */
    @Schema(description ="品牌id")
    private Long carBrandId;

    /**
     * 品牌名称
     */
    @Schema(description ="品牌名称")
    private String carBrandName;

    /**
     * 机器人ID
     */
    @Schema(description ="机器人ID")
    private Long robotId;

    /**
     * 请求IP地址信息
     */
    @Schema(description ="请求IP地址信息")
    private String requestIpAddress;

    /**
     * 请求时间
     */
    @Schema(description ="请求时间")
    private LocalDateTime requestTime;

    /**
     * 请求参数
     */
    @Schema(description ="请求参数")
    private String requestParams;

    /**
     * 请求header信息
     */
    @Schema(description ="请求header信息")
    private String requestHeader;

    /**
     * VIN码
     */
    @Schema(description ="VIN码")
    private String vin;

    /**
     * 发动机号
     */
    @Schema(description ="发动机号")
    private String engineCode;

    /**
     * 回调地址
     */
    @Schema(description ="回调地址")
    private String callbackUrl;

	/**
	 * 订单类型
	 */
	@Schema(description ="订单类型用与区分订单商户")
	private String orderType;

    /**
     * 结果状态码：1：下单成功，2：下单失败，3：回调成功，4：回调失败
     */
    @Schema(description ="结果状态码：0：下单成功，1：下单失败，2：查询中，3：回调成功，4：回调失败")
    private Integer requestStatus;

    /**
     * 失败原因
     */
    @Schema(description ="失败原因")
    private String failureReason;

    /**
     * 查询成功结果
     */
    @Schema(description ="查询成功结果")
    private String result;

    /**
     * 回调时间
     */
    @Schema(description ="回调时间")
    private LocalDateTime callbackTime;

    /**
     * 订单消耗时长
     */
    @Schema(description ="订单消耗时长")
    private Long spendTime;

    /**
     * 重试次数
     */
    @Schema(description ="重试次数")
    private Integer retryCount;


}
