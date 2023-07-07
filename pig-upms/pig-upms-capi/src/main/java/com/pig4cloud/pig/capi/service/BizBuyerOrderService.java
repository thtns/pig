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
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.response.MaintenanceOrderRes;

/**
 * 采购商订单表
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
public interface BizBuyerOrderService extends IService<BizBuyerOrder> {


	/**
	 * 通过vin查询成功用户查询
	 * @param vin
	 * @return
	 */
	BizBuyerOrder getSuccessMerchantOrderByVin(String vin);

	/**
	 * 根据商户id和vin查询是否有订单记录
	 * @param vin
	 * @param bizBuyerId
	 * @return
	 */
	boolean isOrNotPlaceOrder(String vin, Long bizBuyerId);

	/**
	 * 判断采购商订单上限
	 * @param bizBuyerOrder
	 * @return
	 */
	Boolean cheackBuyerOrderLimi(BizBuyerOrder bizBuyerOrder);

}
