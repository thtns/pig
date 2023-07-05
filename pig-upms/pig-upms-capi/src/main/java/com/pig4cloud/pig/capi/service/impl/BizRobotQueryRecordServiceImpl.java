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

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.mapper.BizRobotQueryRecordMapper;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 机器人查询记录表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
public class BizRobotQueryRecordServiceImpl extends ServiceImpl<BizRobotQueryRecordMapper, BizRobotQueryRecord> implements BizRobotQueryRecordService {

	@Override
	public BizRobotQueryRecord getQueryRecordByVin(String vin) {
		LambdaQueryWrapper<BizRobotQueryRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(BizRobotQueryRecord::getVin, vin);
		queryWrapper.gt(BizRobotQueryRecord::getQuerytime, DateUtil.offsetDay(DateUtil.date(),-3));
		queryWrapper.orderByDesc(BizRobotQueryRecord::getQuerytime);
		List<BizRobotQueryRecord> list = this.list(queryWrapper);
		return list.stream().findFirst().orElse(null);
	}

	/**
	 * @param supplierId
	 * @return
	 */
	@Override
	public Integer getTodayCountBySupplier(Long supplierId) {
		LambdaQueryWrapper<BizRobotQueryRecord> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.likeRight(BizRobotQueryRecord::getQuerytime , DateUtil.today())
				.eq(BizRobotQueryRecord::getSupplierId, supplierId);

		return Math.toIntExact(this.count(queryWrapper));
	}
}
