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
import com.google.protobuf.ServiceException;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizCarBrand;
import com.pig4cloud.pig.capi.service.apo.RebotInfo;

import java.util.List;


/**
 * 汽车品牌
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
public interface BizCarBrandService extends IService<BizCarBrand> {


	BizCarBrand getCarBrand(BizBuyerOrder bizBuyerOrder);

	/****
	 * 根据品牌名称获取品牌对象
	 * @param vinCode
	 * @return
	 */
	BizCarBrand getCarBrandByBrand(String vinCode);

	/**
	 *
	 * 根据厂商名称获取品牌对象
	 * @param manufacturer
	 * @return
	 */
	BizCarBrand getCarBrandByManufacturer(String manufacturer);

	/**
	 * 获取有效机器人（主业务使用）
	 * @param bizCarBrand
	 * @param bizBuyerOrder
	 * @return
	 */
	List<RebotInfo> getEffectiveRobot(BizCarBrand bizCarBrand, BizBuyerOrder bizBuyerOrder);

	public BizCarBrand matchVinBrand(BizBuyerOrder bizBuyerOrder) throws ServiceException;
}
