package com.pig4cloud.pig.capi.service;

import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.dto.request.RobotCallbackRequest;
import com.pig4cloud.pig.common.core.util.R;

public interface MaintenanceService  {

	/**
	 * 维修订单
	 * @param bizBuyerOrder
	 * @return
	 */
	public R<Object> processMaintenanceOrder(BizBuyerOrder bizBuyerOrder);

	public void getResult(BizBuyerOrder bizBuyerOrder);

	public void robotRequestCallBack(RobotCallbackRequest rebotCallbackRequest) throws Exception ;
}
