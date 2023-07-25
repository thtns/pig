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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.capi.entity.BizCarBrandSupplier;
import com.pig4cloud.pig.capi.entity.BizSupplier;
import com.pig4cloud.pig.capi.mapper.BizCarBrandSupplierMapper;
import com.pig4cloud.pig.capi.mapper.BizSupplierMapper;
import com.pig4cloud.pig.capi.service.BizSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 供应商表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizSupplierServiceImpl extends ServiceImpl<BizSupplierMapper, BizSupplier> implements BizSupplierService {


	/*** 品牌供应商mapper **/
	private final BizCarBrandSupplierMapper bizCarBrandSupplierMapper;

	private final BizSupplierMapper bizSupplierMapper;


	@Override
	public List<BizSupplier> getSupplierByCarBrandId(Long carBrandId) {
		QueryWrapper<BizCarBrandSupplier> carBrandQueryWrapper = new QueryWrapper<>();
		carBrandQueryWrapper.eq("car_brand_id", carBrandId);
		List<BizCarBrandSupplier> bizCarBrandSuppliers = bizCarBrandSupplierMapper.selectList(carBrandQueryWrapper);
		if (bizCarBrandSuppliers.isEmpty()) {
			return Collections.emptyList();
		}
		QueryWrapper<BizSupplier> bizSupplierQueryWrapper = new QueryWrapper<>();
		bizSupplierQueryWrapper.eq("status", 1);
		bizSupplierQueryWrapper.in("id", bizCarBrandSuppliers.stream().map(BizCarBrandSupplier::getSupplierId).collect(Collectors.toList()));
		return bizSupplierMapper.selectList(bizSupplierQueryWrapper);

	}

	public void shutDownSupplier(Long id){
//		BizSupplier bizSupplier = this.getById(id);
//		if (Objects.isNull(bizSupplier)){
//			return;
//		}
//		bizSupplier.setStatus(0);
//		this.updateById(bizSupplier);
		// 构造UpdateWrapper
		UpdateWrapper<BizSupplier> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id) // 根据ID条件
				.setSql("status = " + 0); // 设置累加的表达式
		// 执行更新操作
		this.update(null, updateWrapper);
	}

	/**
	 * @param id
	 */
	@Override
	public void addSupplierCount(Long id) {
		BizSupplier bizSupplier = this.getById(id);
		int newValue = bizSupplier.getDailyCount() + 1;
		if (newValue >= bizSupplier.getDailyLimitCount()){
			bizSupplier.setStatus(0);
		}
		bizSupplier.setDailyCount(newValue);
		this.updateById(bizSupplier);
	}
}
