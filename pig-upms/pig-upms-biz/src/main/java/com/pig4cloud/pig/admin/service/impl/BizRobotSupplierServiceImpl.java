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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pig4cloud.pig.admin.api.entity.BizRobotSupplier;
import com.pig4cloud.pig.admin.api.request.AddSupplierRobotRequest;
import com.pig4cloud.pig.admin.mapper.BizRobotSupplierMapper;
import com.pig4cloud.pig.admin.service.BizRobotSupplierService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 机器人供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
public class BizRobotSupplierServiceImpl extends ServiceImpl<BizRobotSupplierMapper, BizRobotSupplier> implements BizRobotSupplierService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(AddSupplierRobotRequest request) {

		LambdaQueryWrapper<BizRobotSupplier> wrapper = Wrappers.<BizRobotSupplier>query().lambda().eq(BizRobotSupplier::getSupplierId, request.getSupplierId());
		remove(wrapper);
		ArrayList<BizRobotSupplier> objects = Lists.newArrayList();
		request.getRobotIds().forEach(id -> {
			BizRobotSupplier supplier = new BizRobotSupplier();
			supplier.setSupplierId(request.getSupplierId());
			supplier.setRobotId(id);
			objects.add(supplier);
		});

		saveBatch(objects);

	}
}
