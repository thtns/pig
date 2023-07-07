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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.capi.entity.BizBuyer;
import com.pig4cloud.pig.capi.mapper.BizBuyerMapper;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * 采购商表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizBuyerServiceImpl extends ServiceImpl<BizBuyerMapper, BizBuyer> implements BizBuyerService {

	private final BizBuyerMapper bizBuyerMapper;
	/**
	 * @param ak
	 * @param sk
	 * @return
	 */
	@Override
	public BizBuyer getByAkSk(String ak, String sk) {
		LambdaQueryWrapper<BizBuyer> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(BizBuyer::getClientKey, ak);
		queryWrapper.eq(BizBuyer::getAecSecret, sk);
		List<BizBuyer> list = this.list(queryWrapper);
		return list.stream().findFirst().orElse(null);
	}
}
