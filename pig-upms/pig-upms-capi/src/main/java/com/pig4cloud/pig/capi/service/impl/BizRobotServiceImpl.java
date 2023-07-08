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
import com.pig4cloud.pig.capi.entity.BizCarBrandSupplier;
import com.pig4cloud.pig.capi.entity.BizRobot;
import com.pig4cloud.pig.capi.entity.BizRobotSupplier;
import com.pig4cloud.pig.capi.mapper.BizCarBrandSupplierMapper;
import com.pig4cloud.pig.capi.mapper.BizRobotMapper;
import com.pig4cloud.pig.capi.mapper.BizRobotSupplierMapper;
import com.pig4cloud.pig.capi.service.BizRobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机器人
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizRobotServiceImpl extends ServiceImpl<BizRobotMapper, BizRobot> implements BizRobotService {

	/*** 机器人mapper **/
	private final BizRobotMapper bizRobotMapper;

	/*** 品牌供应商mapper **/
	private final BizCarBrandSupplierMapper bizCarBrandSupplierMapper;

	/*** 供应商机器人mapper **/
	private final BizRobotSupplierMapper bizRobotSupplierMapper;


	/**
	 * 根据供应商关系列表获取所有启动机器人
	 * @param bizRobotSuppliers 供应商关系列表
	 * @return
	 */
	@Override
	public List<BizRobot> getRobotsByRobotSuppliers(List<BizRobotSupplier> bizRobotSuppliers) {
		QueryWrapper<BizRobot> robotWrapper = new QueryWrapper<>();
		robotWrapper.eq("status", 1);// 启动
		robotWrapper.in("id", bizRobotSuppliers.stream().map(BizRobotSupplier::getRobotId).collect(Collectors.toList()));
		return bizRobotMapper.selectList(robotWrapper);
	}


	private List<BizRobot> getByRobotSupplierList(QueryWrapper<BizRobotSupplier> robotSupplierWrapper){
		List<BizRobotSupplier> robotSupplierList = bizRobotSupplierMapper.selectList(robotSupplierWrapper);
		// 根据供应商机器人关系获取机器人列表
		if (robotSupplierList.isEmpty()) {
			return Collections.emptyList();
		}
		QueryWrapper<BizRobot> robotWrapper = new QueryWrapper<>();
		robotWrapper.eq("status", 1);// 启动
		robotWrapper.in("id", robotSupplierList.stream().map(BizRobotSupplier::getRobotId).collect(Collectors.toList()));
		return bizRobotMapper.selectList(robotWrapper);
	}

	/**
	 * 根据品牌id获取所有可用机器人
	 * @param carBrandid
	 * @return
	 */
	@Override
	public List<BizRobot> getRobotsByCarBrandId(Long carBrandid) {
		// 获取品牌下供应商关系
		QueryWrapper<BizCarBrandSupplier> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("car_brand_id", carBrandid);
		List<BizCarBrandSupplier> bizCarBrandSuppliers = bizCarBrandSupplierMapper.selectList(queryWrapper);

		// 根据供应商获取 供应商机器人关系  // 这里可以做一个是否加权排序
		QueryWrapper<BizRobotSupplier> robotSupplierWrapper = new QueryWrapper<>();
		robotSupplierWrapper.in("supplier_id", bizCarBrandSuppliers.stream().map(BizCarBrandSupplier::getSupplierId).collect(Collectors.toList()));
		return getByRobotSupplierList(robotSupplierWrapper);
	}

	/**
	 * 根据供货商id获取可用机器人列表
	 * @param supplierId
	 * @return
	 */
	@Override
	public List<BizRobot> getRobotsBySupplierId(Long supplierId) {
		// 根据供应商获取 供应商机器人关系  // 这里可以做一个是否加权排序
		QueryWrapper<BizRobotSupplier> robotSupplierWrapper = new QueryWrapper<>();
		robotSupplierWrapper.eq("supplier_id", supplierId);
		return getByRobotSupplierList(robotSupplierWrapper);
	}

}
