package com.pig4cloud.pig.capi.service;

import com.pig4cloud.pig.capi.dto.request.RobotCallbackRequest;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;

public interface CallBackService {

	/**
	 * 数据查询成功，回调
	 * @param rebotCallbackRequest
	 */
	public void success(RobotCallbackRequest rebotCallbackRequest);

	/**
	 * 驳回订单
	 * @param bizBuyerOrder
	 */
	public void reject(BizBuyerOrder bizBuyerOrder);

	/**
	 * 数据查询无记录，回调
	 * @param bizBuyerOrder
	 */
	public void noData(BizBuyerOrder bizBuyerOrder);


	public Integer sendChaBoss(String orderNo, int status, Object object) throws Exception;
}
