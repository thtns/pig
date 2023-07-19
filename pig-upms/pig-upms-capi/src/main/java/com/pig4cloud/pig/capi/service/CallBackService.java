package com.pig4cloud.pig.capi.service;

import com.pig4cloud.pig.capi.dto.request.RobotCallbackRequest;

public interface CallBackService {

	public void success(RobotCallbackRequest rebotCallbackRequest);

	public Integer sendChaBoss(String orderNo, int status, Object object) throws Exception;
}
