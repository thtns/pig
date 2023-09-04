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
package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.BizBuyerOrder;
import com.pig4cloud.pig.admin.controller.bean.BuyerOrderExcelVo;
import com.pig4cloud.pig.admin.mapper.BizBuyerOrderMapper;
import com.pig4cloud.pig.admin.service.BizBuyerOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 采购商订单表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
public class BizBuyerOrderServiceImpl extends ServiceImpl<BizBuyerOrderMapper, BizBuyerOrder> implements BizBuyerOrderService {
	/**
	 * @param bizBuyerOrder
	 * @return
	 */
	@Override
	public List<BuyerOrderExcelVo> export(BizBuyerOrder bizBuyerOrder) {
		QueryWrapper<BizBuyerOrder> queryWrapper = new QueryWrapper<>();
		// 构建查询条件
		if (bizBuyerOrder.getBuyerId() != null) {// 采购人id
			queryWrapper.eq("buyer_id", bizBuyerOrder.getBuyerId());
		}
		if (bizBuyerOrder.getCarBrandId() != null) {// 品牌id
			queryWrapper.eq("car_brand_id", bizBuyerOrder.getCarBrandId());
		}
		if (bizBuyerOrder.getCarBrandName() != null) {
			queryWrapper.like("car_brand_name", bizBuyerOrder.getCarBrandName());
		}
		if (bizBuyerOrder.getManufacturer() != null) {
			queryWrapper.like("manufacturer", bizBuyerOrder.getManufacturer());
		}
		if (bizBuyerOrder.getSupplierName() != null) {
			queryWrapper.like("supplier_name", bizBuyerOrder.getSupplierName());
		}
		if (bizBuyerOrder.getVin() != null) {
			queryWrapper.like("vin", bizBuyerOrder.getVin());
		}
		// 时间段查询
		if (bizBuyerOrder.getRequestTime() != null && bizBuyerOrder.getCallbackTime() != null) {
			queryWrapper.between("request_time", bizBuyerOrder.getRequestTime(), bizBuyerOrder.getCallbackTime());
		}
		if (bizBuyerOrder.getRequestStatus() != null) {
			queryWrapper.eq("request_status", bizBuyerOrder.getRequestStatus());
		}
		if (bizBuyerOrder.getAnyData() != null) {
			queryWrapper.eq("any_data", bizBuyerOrder.getAnyData());
		}

		queryWrapper.orderByDesc("request_time");

		List<BizBuyerOrder> orders = this.list(queryWrapper);

		return orders.stream()
				.map(order -> {
					BuyerOrderExcelVo orderExcelVo = new BuyerOrderExcelVo();
					BeanUtils.copyProperties(order, orderExcelVo);
					orderExcelVo.setPrice("10");
					return orderExcelVo;
				})
				.collect(Collectors.toList());
	}
}
