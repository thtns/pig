package com.pig4cloud.pig.capi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.capi.entity.BizVinParsing;
import com.pig4cloud.pig.capi.mapper.BizVinParsingMapper;
import com.pig4cloud.pig.capi.service.BizVinParsingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BizVinParsingServiceImpl extends ServiceImpl<BizVinParsingMapper, BizVinParsing>  implements BizVinParsingService {

	/**
	 * @param vinCode
	 * @return
	 */
	@Override
	public BizVinParsing getBizVinParsing(String vinCode) {
		log.info("~~~~~~~VIN码：{}", vinCode);
		LambdaQueryWrapper<BizVinParsing> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(BizVinParsing::getVinCode, vinCode);
		BizVinParsing bizVinParsing = this.getOne(queryWrapper);
		log.info("~~~~~~~VIN Parsing Code result ：{}", bizVinParsing.getSubBrand());
		return Optional.ofNullable(bizVinParsing).orElse(null);
	}
}
