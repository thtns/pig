package com.pig4cloud.pig.capi.service;

import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.common.core.util.R;

public interface MainCoreService {

	public R<Object> placeOrder(BizBuyerOrder bizBuyerOrder);
}
