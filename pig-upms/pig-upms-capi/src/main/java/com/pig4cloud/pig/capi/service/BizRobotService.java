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

package com.pig4cloud.pig.capi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.capi.entity.BizRobot;
import com.pig4cloud.pig.capi.entity.BizRobotSupplier;
import com.pig4cloud.pig.admin.api.request.AddRobotRequest;

import java.util.List;


/**
 * 机器人
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
public interface BizRobotService extends IService<BizRobot> {


	/***
	 * 根据供应商关系表获取机器人
	 * @param bizRobotSuppliers	供应商关系列表
	 * @return
	 */
	public List<BizRobot> getRobotsByRobotSuppliers(List<BizRobotSupplier> bizRobotSuppliers);

	/**
	 * 根据品牌id获取可用机器人列表
	 * @param carBrandid
	 * @return
	 */
	public List<BizRobot> getRobotsByCarBrandId(Long carBrandid);

	/**
	 * 根据供货商id获取可用机器人列表
	 * @param supplierId
	 * @return
	 */
	public List<BizRobot> getRobotsBySupplierId(Long supplierId);
}
