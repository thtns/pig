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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pig4cloud.pig.admin.api.entity.BizCarBrand;
import com.pig4cloud.pig.admin.api.request.AddBizCarBrandSupplierRequest;
import com.pig4cloud.pig.admin.api.request.AddCarBrandRequest;
import com.pig4cloud.pig.admin.mapper.BizCarBrandMapper;
import com.pig4cloud.pig.admin.service.BizCarBrandService;
import com.pig4cloud.pig.admin.service.BizCarBrandSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 汽车品牌
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizCarBrandServiceImpl extends ServiceImpl<BizCarBrandMapper, BizCarBrand> implements BizCarBrandService {


	private final BizCarBrandSupplierService bizCarBrandSupplierService;

	@Override
	public void add(AddCarBrandRequest request) {
		BizCarBrand bizCarBrand = new BizCarBrand();
		BeanUtils.copyProperties(request, bizCarBrand);
		save(bizCarBrand);
	}

}
