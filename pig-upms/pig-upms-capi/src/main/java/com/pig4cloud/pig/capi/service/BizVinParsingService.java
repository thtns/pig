package com.pig4cloud.pig.capi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.BizVinParsing;

public interface BizVinParsingService extends IService<BizVinParsing> {

	public BizVinParsing getBizVinParsing(String vinCode);
}
