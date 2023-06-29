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
import com.pig4cloud.pig.admin.api.entity.BizBuyerOrder;
import com.pig4cloud.pig.admin.api.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.mapper.BizBuyerOrderMapper;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import com.pig4cloud.pig.capi.response.MaintenanceOrderRes;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 采购商订单表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
public class BizBuyerOrderServiceImpl extends ServiceImpl<BizBuyerOrderMapper, BizBuyerOrder> implements BizBuyerOrderService {


	/**
	 * 通过vin查询成功用户查询
	 * @param vin
	 * @return
	 */
	@Override
	public BizBuyerOrder getSuccessMerchantOrderByVin(String vin) {
		LambdaQueryWrapper<BizBuyerOrder> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.likeRight(BizBuyerOrder::getRequestTime , DateUtil.today())
				.eq(BizBuyerOrder::getVin, vin)
				.eq(BizBuyerOrder::getRequestStatus, RequestStatusEnum.CALLBACK_SUCCESS.getType());
		List<BizBuyerOrder> list = this.list(queryWrapper);
		return list.stream().findFirst().orElse(null);//取第一个或者返回null
	}
}
