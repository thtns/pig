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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.pig4cloud.pig.admin.api.entity.BizSupplier;
import com.pig4cloud.pig.admin.api.request.AddSupplierRequest;
import com.pig4cloud.pig.admin.mapper.BizSupplierMapper;
import com.pig4cloud.pig.admin.service.BizSupplierService;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 供应商表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizSupplierServiceImpl extends ServiceImpl<BizSupplierMapper, BizSupplier> implements BizSupplierService {

	@Override
	public void add(AddSupplierRequest request) {
		BizSupplier bizSupplier = new BizSupplier();
		BeanUtils.copyProperties(request, bizSupplier);
		save(bizSupplier);
	}

	/**
	 *
	 */
	@Override
	public void closeAll(String type) {
		UpdateWrapper<BizSupplier> updateWrapper = new UpdateWrapper<>();
		updateWrapper.setSql("status = " + type); // 设置累加的表达式
		// 执行更新操作
		this.update(updateWrapper);
	}
}
