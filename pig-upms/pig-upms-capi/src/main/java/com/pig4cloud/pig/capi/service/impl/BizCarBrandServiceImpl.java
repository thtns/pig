package com.pig4cloud.pig.capi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.capi.entity.*;
import com.pig4cloud.pig.capi.mapper.BizCarBrandMapper;
import com.pig4cloud.pig.capi.service.*;
import com.pig4cloud.pig.capi.service.atripartite.EasyepcDataManager;
import com.pig4cloud.pig.capi.service.apo.RebotInfo;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 汽车品牌
 *
 * @author pig code generator
 * @date 2023-06-16 20:59:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BizCarBrandServiceImpl extends ServiceImpl<BizCarBrandMapper, BizCarBrand> implements BizCarBrandService {

	/*** vin解析服务 **/
	BizVinParsingService bizVinParsingService;

	/*** 采购商订单服务 **/
	BizBuyerOrderService bizBuyerOrderService;

	/*** 精友数据 **/
	EasyepcDataManager easyepcDataManager;

	/*** 品牌供应商 **/
	BizCarBrandSupplierService bizCarBrandSupplierService;


	/*** 供应商 **/
	BizSupplierService bizSupplierService;

	/*** 供应商机器人 **/
	BizRobotSupplierService bizRobotSupplierService;

	/*** 机器人 **/
	BizRobotService bizRobotService;

	/*** 供应商机器人查询记录 **/
	BizRobotQueryRecordService bizRobotQueryRecordService;

	/**
	 * 获取品牌
	 *
	 * @param bizBuyerOrder
	 * @return
	 */
	@Override
	public BizCarBrand getCarBrand(BizBuyerOrder bizBuyerOrder) {
		//本地查询vin解析结果表获取品牌
		BizVinParsing bizVinParsing = bizVinParsingService.getBizVinParsing(bizBuyerOrder.getVin());//解析结果
		log.info("~~~~~~通过Vin Parsing Service解析  brand...\n Brand : " + bizVinParsing.getBrand());

		BizCarBrand carBrandByWmi;
		if (null != bizVinParsing) {
			log.info("~~~~~~解析正常，开始查询...");
			carBrandByWmi = this.getCarBrandByBrand(bizVinParsing.getBrand());//根据品牌名查询BizCarBrand对象
		} else {// 通过三方服务 vin解析品牌
			log.info("~~~~~~三方解析，开始查询...");
			String brand = easyepcDataManager.getSaleVinInfo(bizBuyerOrder.getVin());//精友数据查询vin 获取品牌
			if (brand == null) {
				bizBuyerOrder.setRequestStatus(RequestStatusEnum.ORDER_FAILURE.getType());
				bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_VIN_UNIDENTIFIABLE.getType())));
				bizBuyerOrderService.updateById(bizBuyerOrder);
				throw new RuntimeException(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_VIN_UNIDENTIFIABLE.getType())));
			}
			carBrandByWmi = this.getCarBrandByBrand(brand);//根据品牌名查询BizCarBrand对象
		}

		//不存在解析品牌结果,订单状态和失败原因填充,并抛错
		if (Objects.isNull(carBrandByWmi)) {
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.ORDER_FAILURE.getType());
			bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_BRAND_NONSUPPORT.getType())));
			bizBuyerOrderService.updateById(bizBuyerOrder);
			throw new RuntimeException(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_BRAND_NONSUPPORT.getType())));
		}

		//存在解析品牌结果,则将品牌id和品牌名称写入
		bizBuyerOrder.setCarBrandId(carBrandByWmi.getId());
		bizBuyerOrder.setCarBrandName(carBrandByWmi.getBrand());
		bizBuyerOrderService.updateById(bizBuyerOrder);
		return carBrandByWmi;
	}

	public BizCarBrand getCarBrandByBrand(String brand) {
		LambdaQueryWrapper<BizCarBrand> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(BizCarBrand::getBrand, brand);
		return this.getOne(queryWrapper);
	}

	/**
	 * @param bizCarBrand
	 * @param bizBuyerOrder
	 * @return
	 */
	@Override
	public List<RebotInfo> getEffectiveRobot(BizCarBrand bizCarBrand, BizBuyerOrder bizBuyerOrder) {
		// 根据品牌获取供应商
		List<BizSupplier> bizSuppliers = bizSupplierService.getSupplierByCarBrandId(bizCarBrand.getId());

		//按照权重供应商分组
		LinkedHashMap<Integer, List<BizSupplier>> supplierMap = bizSuppliers.stream().
				collect(Collectors.groupingBy(BizSupplier::getWeight, LinkedHashMap::new, Collectors.toList()));
		List<BizSupplier> supplierList = new ArrayList<>();
		for (Integer key : supplierMap.keySet()) {
			List<BizSupplier> value = supplierMap.get(key);
			Collections.shuffle(value); // 洗牌
			supplierList.addAll(value); // 放入新列表
		}

		// 处理重试的逻辑【如果此请求是重试，要排除上一次请求的supplier】(注意：第一次请求retryCount = 1，每次重试+1)
		if (bizBuyerOrder.getRetryCount() > 1) {
			supplierList.removeIf(supplierDO -> supplierDO.getId().equals(bizBuyerOrder.getSupplierId()));
		}

		//如果是空则直接返回
		if (supplierList.isEmpty()) {
			log.info("无可用供货商：supplierList is Empty !");
			return null;
		}

		// 采购商订单限制,暂时不不限制(会在后台设置大限量)
		boolean buyerFlag = bizBuyerOrderService.cheackBuyerOrderLimi(bizBuyerOrder);
		if (!buyerFlag) {// 采购商上限限制
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.SERVER_QUERY_FULL_ERROR.getType());
			bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.SERVER_QUERY_FULL_ERROR.getType())));
			bizBuyerOrderService.updateById(bizBuyerOrder);
			throw new RuntimeException(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.SERVER_QUERY_FULL_ERROR.getType())));
		}

		List<BizSupplier> usableSupplierList = new ArrayList<>();
		// 执行: 供货商限量计算逻辑
		long startTime = System.currentTimeMillis();
		for (BizSupplier bizSupplier : supplierList) {
			long startTimeAa = System.currentTimeMillis();
			int todayCountBySupplier = bizRobotQueryRecordService.getTodayCountBySupplier(bizSupplier.getId());// 查询此供应商已用数量
			log.info("当前供应商：" + bizSupplier.getSupplierName() + "日限单量：" + bizSupplier.getDailyLimitCount());
			log.info("当前供应商：" + bizSupplier.getSupplierName() + "今日单量：" + todayCountBySupplier);
			if (bizSupplier.getDailyLimitCount() > todayCountBySupplier) {
				usableSupplierList.add(bizSupplier);
			}
			log.info("&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*对比单量一次所用的时间：" + (System.currentTimeMillis() - startTimeAa) + "ms");
		}
		long endTime = System.currentTimeMillis();
		log.info("&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*对比单量所用的总时间：" + (endTime - startTime) + "ms");

		//如果是空则直接返回
		if (usableSupplierList.isEmpty()) {
			log.info("无可用供货商：usableSupplierList is Empty !");
			return null;
		}

		// 组装机器人信息
		List<RebotInfo> robotEffectiveList = new ArrayList<>();
		for (BizSupplier supplierDO : usableSupplierList) {
			BizRobot bizRobot = bizRobotService.getRobotsBySupplierId(supplierDO.getId()).stream().findFirst().orElse(null);
			if (bizRobot != null){
				RebotInfo rebotInfo = (RebotInfo) bizRobot.clone();
				rebotInfo.setSupplierId(supplierDO.getId());				//添加供应商id
				rebotInfo.setSupplierName(supplierDO.getSupplierName());	//添加供应商名称
				robotEffectiveList.add(rebotInfo);
			}
		}
		return robotEffectiveList;
	}
}
