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
import com.pig4cloud.pig.capi.entity.BizRobotSupplier;
import com.pig4cloud.pig.capi.entity.BizSupplier;

import java.util.List;


/**
 * 机器人供应商关系表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
public interface BizRobotSupplierService extends IService<BizRobotSupplier> {

	/***
	 * 根据供应商id获取机器人供应商关系表
	 * @param supplierId 供应商id
	 * @return
	 */
	public List<BizRobotSupplier> getBizRobotSupplierBySupplierId(Long supplierId);

	/**
	 * 根据供应商列表获取机器人供应商关系表
	 * @param suppliers	供应商列表
	 * @return
	 */
	public List<BizRobotSupplier> getBizRobotSupplierBySuppliers(List<BizSupplier> suppliers);

}
