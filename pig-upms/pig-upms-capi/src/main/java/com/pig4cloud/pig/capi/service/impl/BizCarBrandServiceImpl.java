package com.pig4cloud.pig.capi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.capi.entity.*;
import com.pig4cloud.pig.capi.mapper.BizCarBrandMapper;
import com.pig4cloud.pig.capi.service.*;
import com.pig4cloud.pig.capi.service.apo.RebotInfo;
import com.pig4cloud.pig.capi.service.atripartite.CallBackManager;
import com.pig4cloud.pig.capi.service.atripartite.EasyepcDataManager;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
	private final BizVinParsingService bizVinParsingService;

	/*** 采购商订单服务 **/
	private final BizBuyerOrderService bizBuyerOrderService;

	/*** 精友数据 **/
	private final EasyepcDataManager easyepcDataManager;

	/*** 供应商 **/
	private final BizSupplierService bizSupplierService;

	/*** 机器人 **/
	private final BizRobotService bizRobotService;

	/*** 供应商机器人查询记录 **/
	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	private final CallBackManager callBackManager;


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
		BizCarBrand carBrandByWmi;
		if (Optional.ofNullable(bizVinParsing).isPresent()) {
			log.info("~~~~~~通过Vin Parsing Service解析  brand...\n Brand : " + bizVinParsing.getBrand());
			log.info("~~~~~~解析正常，开始查询...");
			carBrandByWmi = getCarBrandByBrand(bizVinParsing.getSubBrand());//根据品牌名查询BizCarBrand对象
		} else {
			log.info("~~~~~~三方解析，开始查询...");
			String brand = easyepcDataManager.getSaleVinInfo(bizBuyerOrder.getVin());
			if (brand == null) {
				handleOrderFailure(bizBuyerOrder, RequestStatusEnum.API_VIN_UNIDENTIFIABLE);
			}
			carBrandByWmi = getCarBrandByBrand(brand);
		}
		if (Objects.isNull(carBrandByWmi)) {
			handleOrderFailure(bizBuyerOrder, RequestStatusEnum.API_BRAND_NONSUPPORT);
		}

		bizBuyerOrder.setCarBrandId(carBrandByWmi.getId());
		bizBuyerOrder.setCarBrandName(carBrandByWmi.getBrand());
		bizBuyerOrderService.updateById(bizBuyerOrder);
		return carBrandByWmi;
	}

	private void handleOrderFailure(BizBuyerOrder bizBuyerOrder, RequestStatusEnum failureReason) {
		callBackManager.merchantCallBackError(bizBuyerOrder, RequestStatusEnum.ORDER_FAILURE, failureReason);
		throw new RuntimeException(JSON.toJSONString(R.resultEnumType(null, failureReason.getType())));
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

		if (bizSuppliers.isEmpty()) {
			handleEmptySupplierList(bizBuyerOrder, "bizSuppliers is Empty !");
		}

		//按照权重供应商分组
		LinkedHashMap<Integer, List<BizSupplier>> supplierMap = bizSuppliers.stream().
				collect(Collectors.groupingBy(BizSupplier::getWeight, LinkedHashMap::new, Collectors.toList()));

		List<BizSupplier> supplierList = supplierMap.entrySet().stream()
				.flatMap(entry -> {
					int weight = entry.getKey();
					List<BizSupplier> suppliers = entry.getValue();
					Collections.shuffle(suppliers); // 对供应商列表洗牌
					return suppliers.stream().map(supplier -> new AbstractMap.SimpleEntry<>(weight, supplier));
				})
				.sorted(Comparator.comparingInt(AbstractMap.SimpleEntry::getKey)) // 按权重顺序排序
				.map(AbstractMap.SimpleEntry::getValue)
				.collect(Collectors.toList());

		// 处理重试的逻辑【如果此请求是重试，要排除上一次请求的supplier】(注意：第一次请求retryCount = 1，每次重试+1)
		if (bizBuyerOrder.getRetryCount() > 1) {
			supplierList.removeIf(supplierDO -> supplierDO.getId().equals(bizBuyerOrder.getSupplierId()));
		}

		if (supplierList.isEmpty()) {
			handleEmptySupplierList(bizBuyerOrder, "supplierList is Empty !");
		}

		// 采购商订单限制,暂时不不限制(会在后台设置大限量)
		boolean buyerFlag = bizBuyerOrderService.checkBuyerOrderLimit(bizBuyerOrder);
		if (!buyerFlag) {// 采购商上限限制
			callBackManager.merchantCallBackError(bizBuyerOrder, RequestStatusEnum.SERVER_QUERY_FULL_ERROR, RequestStatusEnum.SERVER_QUERY_FULL_ERROR);
		}

		// 执行: 供货商限量计算逻辑
		long startTime = System.currentTimeMillis();
		List<BizSupplier> usableSupplierList = supplierList.stream()
				.filter(bizSupplier -> bizRobotQueryRecordService.getTodayCountBySupplier(bizSupplier.getId()) < bizSupplier.getDailyLimitCount())
				.collect(Collectors.toList());
		long endTime = System.currentTimeMillis();
		log.info("对比单量所用的总时间：" + (endTime - startTime) + "ms");


		if (usableSupplierList.isEmpty()) {
			handleEmptySupplierList(bizBuyerOrder, "usableSupplierList is Empty !");
		}

		List<RebotInfo> robotEffectiveList = usableSupplierList.stream()
				.map(supplierDO -> {
					BizRobot bizRobot = bizRobotService.getRobotsBySupplierId(supplierDO.getId()).stream().findFirst().orElse(null);
					if (bizRobot != null) {
						RebotInfo rebotInfo = new RebotInfo();
						BeanUtils.copyProperties(bizRobot, rebotInfo);
						rebotInfo.setSupplierId(supplierDO.getId());
						rebotInfo.setSupplierName(supplierDO.getSupplierName());
						return rebotInfo;
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		if (robotEffectiveList.isEmpty()) {
			handleEmptySupplierList(bizBuyerOrder, "robotEffectiveList is Empty !");
		}
		return robotEffectiveList;
	}

	private void handleEmptySupplierList(BizBuyerOrder bizBuyerOrder, String errorMessage) {
		log.info("无可用供货商：" + errorMessage);
		if (bizBuyerOrder.getRetryCount() > 1) {
			handleOrderFailure(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE);
		}
		handleOrderFailure(bizBuyerOrder, RequestStatusEnum.API_TIME_NONSUPPORT);
	}
}
