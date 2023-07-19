package com.pig4cloud.pig.capi.service;

import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.common.core.util.R;

public interface MainCoreService {

	public BizBuyerOrder placeOrder(BizBuyerOrder bizBuyerOrder);

	public void processOrder(BizBuyerOrder bizBuyerOrder);
}
