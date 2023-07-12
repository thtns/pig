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
package com.pig4cloud.pig.capi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.capi.entity.BizBuyer;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.mapper.BizBuyerOrderMapper;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.hutool.core.date.DateUtil.today;

/**
 * 采购商订单表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizBuyerOrderServiceImpl extends ServiceImpl<BizBuyerOrderMapper, BizBuyerOrder> implements BizBuyerOrderService {

	private final BizBuyerService bizBuyerService;

	@Override
	public BizBuyerOrder getSuccessMerchantOrderByVin(String vin) {
		return this.lambdaQuery()
				.likeRight(BizBuyerOrder::getRequestTime, today())
				.eq(BizBuyerOrder::getVin, vin)
				.eq(BizBuyerOrder::getRequestStatus, RequestStatusEnum.CALLBACK_SUCCESS.getType())
				.orderByDesc(BizBuyerOrder::getRequestTime) // 按时间倒序排序
				.last("LIMIT 1")
				.one();
	}

	/**
	 * 根据商户id和vin查询是否有订单记录
	 * @param vin
	 * @param bizBuyerId
	 * @return
	 */
	@Override
	public boolean isOrNotPlaceOrder(String vin, Long bizBuyerId) {
		return this.lambdaQuery()
				.likeRight(BizBuyerOrder::getRequestTime, today())
				.eq(BizBuyerOrder::getVin, vin)
				.eq(BizBuyerOrder::getBuyerId, bizBuyerId)
				.and(w -> w.eq(BizBuyerOrder::getRequestStatus, RequestStatusEnum.ORDER_SUCCESS.getType())
						.or()
						.eq(BizBuyerOrder::getRequestStatus, RequestStatusEnum.QUERYING.getType())
				)
				.exists();
	}

	/**
	 * 判断采购商上限
	 * @param bizBuyerOrder
	 * @return
	 */
	@Override
	public Boolean checkBuyerOrderLimit(BizBuyerOrder bizBuyerOrder) {
		Long buyerId = bizBuyerOrder.getBuyerId();
		BizBuyer bizBuyer = bizBuyerService.getById(buyerId);

		// 判断采购商上限 不包含查詢失敗的和此時段無法訪問兩種狀態的數據，即查詢失敗的
		LambdaQueryWrapper<BizBuyerOrder> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.likeRight(BizBuyerOrder::getRequestTime , today())
				.eq(BizBuyerOrder::getBuyerId, buyerId)
				.ne(BizBuyerOrder::getRequestStatus, RequestStatusEnum.API_BRAND_NONSUPPORT.getType())
				.ne(BizBuyerOrder::getRequestStatus, RequestStatusEnum.ORDER_FAILURE.getType());
		return bizBuyer.getDailylimitCount() > this.count(queryWrapper);
	}

	public BizBuyerOrder getSameCarBrandOrder(BizBuyerOrder bizBuyerOrder) {
		return this.lambdaQuery()
				.likeRight(BizBuyerOrder::getRequestTime, today())
				.eq(BizBuyerOrder::getCarBrandId, bizBuyerOrder.getCarBrandId())
				.eq(BizBuyerOrder::getRequestStatus, RequestStatusEnum.QUERYING.getType())
				.ne(BizBuyerOrder::getId, bizBuyerOrder.getId()) // 排除传入的bizBuyerOrder
				.orderByAsc(BizBuyerOrder::getRequestTime)
				.last("LIMIT 1")
				.one();
	}
}
