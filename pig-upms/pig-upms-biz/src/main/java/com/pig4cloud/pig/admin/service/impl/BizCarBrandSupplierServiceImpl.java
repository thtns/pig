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

import com.pig4cloud.pig.admin.api.entity.BizCarBrandSupplier;
import com.pig4cloud.pig.admin.api.request.AddBizCarBrandSupplierRequest;
import com.pig4cloud.pig.admin.mapper.BizCarBrandSupplierMapper;
import com.pig4cloud.pig.admin.service.BizCarBrandSupplierService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 品牌供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
public class BizCarBrandSupplierServiceImpl extends ServiceImpl<BizCarBrandSupplierMapper, BizCarBrandSupplier> implements BizCarBrandSupplierService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(AddBizCarBrandSupplierRequest request) {


		LambdaQueryWrapper<BizCarBrandSupplier> wrapper = Wrappers.<BizCarBrandSupplier>query().lambda().eq(BizCarBrandSupplier::getCarBrandId, request.getCarBrandId());

		remove(wrapper);

		ArrayList<BizCarBrandSupplier> objects = Lists.newArrayList();

		request.getSupplierIds().forEach(supplier -> {

			BizCarBrandSupplier brandSupplier = new BizCarBrandSupplier();

			brandSupplier.setCarBrandId(request.getCarBrandId());
			brandSupplier.setSupplierId(supplier);
			objects.add(brandSupplier);
		});

		saveBatch(objects);

	}
}
