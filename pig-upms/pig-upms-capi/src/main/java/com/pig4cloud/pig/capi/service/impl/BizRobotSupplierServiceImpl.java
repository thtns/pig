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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.BizCarBrandSupplier;
import com.pig4cloud.pig.admin.api.entity.BizRobotSupplier;
import com.pig4cloud.pig.admin.api.entity.BizSupplier;
import com.pig4cloud.pig.capi.mapper.BizRobotSupplierMapper;
import com.pig4cloud.pig.capi.service.BizRobotSupplierService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 机器人供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
public class BizRobotSupplierServiceImpl extends ServiceImpl<BizRobotSupplierMapper, BizRobotSupplier> implements BizRobotSupplierService {

	/*** 供应商机器人mapper **/
	BizRobotSupplierMapper bizRobotSupplierMapper;

	/**
	 * @param supplierId
	 * @return
	 */
	@Override
	public List<BizRobotSupplier> getBizRobotSupplierBySupplierId(Long supplierId) {
		QueryWrapper<BizRobotSupplier> robotSupplierWrapper = new QueryWrapper<>();
		robotSupplierWrapper.eq("supplier_id", supplierId);
		return bizRobotSupplierMapper.selectList(robotSupplierWrapper);
	}

	/**
	 * @param suppliers
	 * @return
	 */
	@Override
	public List<BizRobotSupplier> getBizRobotSupplierBySuppliers(List<BizSupplier> suppliers) {
		QueryWrapper<BizRobotSupplier> robotSupplierWrapper = new QueryWrapper<>();
		robotSupplierWrapper.in("supplier_id", suppliers.stream().map(BizSupplier::getId).collect(Collectors.toList()));
		return bizRobotSupplierMapper.selectList(robotSupplierWrapper);
	}
}
