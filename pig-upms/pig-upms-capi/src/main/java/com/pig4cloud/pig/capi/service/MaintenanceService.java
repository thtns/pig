package com.pig4cloud.pig.capi.service;

import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.response.MaintenanceOrderRes;

public interface MaintenanceService  {

	/**
	 * 维修订单
	 * @param bizBuyerOrder
	 * @return
	 */
	public MaintenanceOrderRes maintenanceOrder(BizBuyerOrder bizBuyerOrder);

	public void getResult(BizBuyerOrder bizBuyerOrder);
}
