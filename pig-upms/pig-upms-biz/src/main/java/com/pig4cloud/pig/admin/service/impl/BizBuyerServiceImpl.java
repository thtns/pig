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

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.BizBuyer;
import com.pig4cloud.pig.admin.api.request.AddBuyerRequest;
import com.pig4cloud.pig.admin.api.request.ListBuyerRequest;
import com.pig4cloud.pig.admin.config.GatewayConfigProperties;
import com.pig4cloud.pig.admin.mapper.BizBuyerMapper;
import com.pig4cloud.pig.admin.service.BizBuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;


/**
 * 采购商表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Service
@RequiredArgsConstructor
public class BizBuyerServiceImpl extends ServiceImpl<BizBuyerMapper, BizBuyer> implements BizBuyerService {
	private final GatewayConfigProperties gatewayConfig;
	/**
	 * 编码格式
	 */
	public static final String ENCODING = "utf-8";

	private static final String KEY_ALGORITHM = "AES";

//	private final GatewayConfigProperties gatewayConfig;


	@Override
	public IPage<BizBuyer> list(ListBuyerRequest request) {


		Page page = new Page(request.getCurrent(), request.getSize());


		return page(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void add(AddBuyerRequest request) {

		String clientKey = UUID.randomUUID().toString().replace("-", "");
		String clientSecret = UUID.randomUUID().toString().replace("-", "");
		String aecSecret = Base64.encode(new AES(Mode.CFB, Padding.NoPadding,
				new SecretKeySpec(gatewayConfig.getEncodeKey().getBytes(), KEY_ALGORITHM),
				new IvParameterSpec(gatewayConfig.getEncodeKey().getBytes())).encrypt(clientSecret));

		BizBuyer buyer = new BizBuyer();
		BeanUtils.copyProperties(request, buyer);
		buyer.setClientKey(clientKey);
		buyer.setClientSecret(clientSecret);
		buyer.setAecSecret(aecSecret);
		save(buyer);
	}

}
