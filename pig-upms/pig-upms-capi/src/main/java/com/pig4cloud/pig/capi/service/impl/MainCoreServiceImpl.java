package com.pig4cloud.pig.capi.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.pig4cloud.pig.capi.entity.*;
import com.pig4cloud.pig.capi.dto.request.RebotCallbackParames.RobotResponse;
import com.pig4cloud.pig.capi.nacosConf.BaseConfig;
import com.pig4cloud.pig.capi.service.*;
import com.pig4cloud.pig.capi.service.apo.RebotInfo;
import com.pig4cloud.pig.capi.service.apo.RedisKeyDefine;
import com.pig4cloud.pig.capi.service.atripartite.CallBackManager;
import com.pig4cloud.pig.capi.service.atripartite.EasyepcDataManager;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.DateTimeUitils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetryUtil;
import com.pig4cloud.pig.common.core.util.mq.ProducerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainCoreServiceImpl implements MainCoreService {

	final int TIME_OUT = 2000;

	final int ROBOT_TIME_OUT = 1000;

	private ExecutorService executorService = Executors.newFixedThreadPool(30); // 创建线程池

	private final RedisTemplate<String, String> redisTemplate;

	/*** 机器人查询记录 **/
	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	/*** 采购商记录 **/
	private final BizBuyerOrderService bizBuyerOrderService;


	private final CallBackManager callBackManager;

	/*** 消息队列 **/
	private final ProducerUtil producerUtil;

	/*** 品牌 **/
	private final BizCarBrandService bizCarBrandService;

	/*** vin解析服务 **/
	private final BizVinParsingService bizVinParsingService;

	/*** 精友数据 **/
	private final EasyepcDataManager easyepcDataManager;

	/*** 供应商 **/
	private final BizSupplierService bizSupplierService;

	/*** 机器人 **/
	private final BizRobotService bizRobotService;

	/*** 成功回调 **/
	private final CallBackService callBackService;

	private final BaseConfig baseConfig;


	/**
	 * @param bizBuyerOrder
	 * @return
	 */
	@Override
	public BizBuyerOrder placeOrder(BizBuyerOrder bizBuyerOrder) {
		String vin = bizBuyerOrder.getVin();
		String brandName = bizBuyerOrder.getCarBrandName();
		String manufacturer = bizBuyerOrder.getManufacturer();
		BizCarBrand carBrand = null;
		log.info("下单的品牌名称: 【{}】. 厂商名称:【{}】", brandName, manufacturer);
		String getName = brandName;
		if ("大众".equals(getName) || "丰田".equals(brandName)) {
			getName = manufacturer;
			carBrand = bizCarBrandService.getCarBrandByManufacturer(getName);
		}else{
			carBrand = bizCarBrandService.getCarBrandByBrand(getName);
		}

		if (carBrand == null) {
			BizVinParsing bizVinParsing = bizVinParsingService.getBizVinParsing(bizBuyerOrder.getVin());//解析结果
			getName = bizVinParsing.getBrand();
			if (Optional.ofNullable(bizVinParsing).isPresent()) {
				if ("大众".equals(getName) || "丰田".equals(brandName)) {
					getName = bizVinParsing.getSubBrand();
					carBrand = bizCarBrandService.getCarBrandByManufacturer(getName);
				}else{
					carBrand = bizCarBrandService.getCarBrandByBrand(brandName);//根据品牌名查询BizCarBrand对象
				}
			} else {
				BizVinParsing vinParsing = easyepcDataManager.getSaleVinInfo(bizBuyerOrder.getVin());
				if (vinParsing == null) {
					bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_VIN_UNIDENTIFIABLE.getType());
					return bizBuyerOrder;
				}
				getName = vinParsing.getBrand();
				if ("大众".equals(getName) || "丰田".equals(brandName)){
					getName = vinParsing.getSubBrand();
					carBrand = bizCarBrandService.getCarBrandByManufacturer(getName);
				}else{
					carBrand = bizCarBrandService.getCarBrandByBrand(getName);//根据品牌名查询BizCarBrand对象
				}
			}
			if (Objects.isNull(carBrand)) {
				log.error("不存在Vin：【{}】 本地品牌信息, 不支持该品牌,退出下单. ", vin);
				bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_BRAND_NONSUPPORT.getType());
				return bizBuyerOrder;
			}
		}
		// 根据品牌获取供应商
		Long bizCarBrandId = carBrand.getId();
		bizBuyerOrder.setCarBrandId(carBrand.getId());
		bizBuyerOrder.setCarBrandName(carBrand.getBrand());

		// 根据品牌获取供应商
		List<BizSupplier> bizSuppliers = bizSupplierService.getSupplierByCarBrandId(bizCarBrandId);
		if (bizSuppliers.isEmpty()) {
			log.error("品牌【{}】没有找到供应商,退出下单. ", brandName);
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_TIME_NONSUPPORT.getType());
			return bizBuyerOrder;
		}

		// 执行: 供货商限量计算逻辑
//		List<BizSupplier> usableSupplierList = bizSuppliers.stream()
//				.filter(bizSupplier -> bizRobotQueryRecordService.getTodayCountBySupplier(bizSupplier.getId()) < bizSupplier.getDailyLimitCount())
//				.collect(Collectors.toList());

		List<BizSupplier> usableSupplierList = bizSuppliers.stream()
				.filter(bizSupplier -> bizSupplier.getDailyCount() <= bizSupplier.getDailyLimitCount())
				.collect(Collectors.toList());

		if (usableSupplierList.isEmpty()) {
			log.error("品牌【{}】没有可用供应商, usableSupplierList is Empty. 退出下单. ", brandName);
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_TIME_NONSUPPORT.getType());
			return bizBuyerOrder;
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
			log.info("品牌【{}】没有有效机器人, robotEffectiveList is Empty. 退出下单.", brandName);
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_TIME_NONSUPPORT.getType());
			return bizBuyerOrder;
		}
		bizBuyerOrder.setRequestStatus(RequestStatusEnum.ORDER_SUCCESS.getType()); // 成功下单
		bizBuyerOrderService.save(bizBuyerOrder);
		// 这里添加消息任务 通过消息队列来消费
		producerUtil.sendMsg(String.valueOf(bizBuyerOrder.getId()));
		return bizBuyerOrder;
	}

	public BizCarBrand getCarBrand(String brandName, String manufacturer){
		if ("大众".equals(brandName) || "丰田".equals(brandName)) {
			return bizCarBrandService.getCarBrandByManufacturer(manufacturer);
		}else{
			return bizCarBrandService.getCarBrandByBrand(brandName);
		}
	}
	/***
	 * 处理订单请求
	 * @param bizBuyerOrder 买家订单对象
	 */
	public void processOrder(BizBuyerOrder bizBuyerOrder) {
		try {
			bizBuyerOrder.setRetryCount(bizBuyerOrder.getRetryCount() + 1); // 重试次数累加
			String vin = bizBuyerOrder.getVin();
			BizRobotQueryRecord bizRobotQueryRecord = bizRobotQueryRecordService.getQueryRecordByVin(vin);
			if (Optional.ofNullable(bizRobotQueryRecord).isPresent() && !bizRobotQueryRecord.getResult().equals("null") && !bizRobotQueryRecord.getResult().equals("")) {
				localMysqlParsing(bizBuyerOrder, bizRobotQueryRecord);
			} else {
				robot_request(bizBuyerOrder);
			}
		} catch (RuntimeException e) {
			log.error("处理订单异常: {}", e.getMessage());
			// 这里添加消息任务 通过消息队列来消费重试
			bizBuyerOrderService.updateById(bizBuyerOrder);
			// 发送延迟一分钟的消息队列
			long delayTime = System.currentTimeMillis() + (60 * 1000);
			producerUtil.sendTimeMsg(String.valueOf(bizBuyerOrder.getId()), delayTime);
		}
	}

	public void robot_request(BizBuyerOrder bizBuyerOrder) {
		// 采购商订单限制,暂时不不限制(会在后台设置大限量)
		boolean buyerFlag = bizBuyerOrderService.checkBuyerOrderLimit(bizBuyerOrder);
		if (!buyerFlag) {
			log.error("采购商请求已达上限. 退出机器人查询！");
			bizBuyerOrderService.updateById(bizBuyerOrder);
			return;
		}

		String brandName = bizBuyerOrder.getCarBrandName();// 品牌名称
		Long bizCarBrandId = bizBuyerOrder.getCarBrandId();    // 品牌id
		log.info("robot_request的品牌名称: 【{}】.", brandName);

		// 根据品牌获取供应商
		List<BizSupplier> bizSuppliers = bizSupplierService.getSupplierByCarBrandId(bizCarBrandId);
		if (bizSuppliers.isEmpty()) {
			log.error("品牌【{}】没有找到供应商,退出机器人查询！ ", brandName);
			bizBuyerOrderService.updateById(bizBuyerOrder);
			return;
		}

		//按照权重供应商分组
		LinkedHashMap<Integer, List<BizSupplier>> supplierMap = bizSuppliers.stream().
				collect(Collectors.groupingBy(BizSupplier::getWeight, LinkedHashMap::new, Collectors.toList()));

		// 打乱排序, 虽然打乱但是权重依然有序 当权重相同时,也会打乱权重
		List<BizSupplier> supplierList = supplierMap.entrySet().stream()
				.flatMap(entry -> {
					int weight = entry.getKey();
					List<BizSupplier> suppliers = entry.getValue();
					List<AbstractMap.SimpleEntry<Integer, BizSupplier>> weightedSuppliers = suppliers.stream()
							.map(supplier -> new AbstractMap.SimpleEntry<>(weight, supplier))
							.collect(Collectors.toList());

					// Shuffle the weighted suppliers when weights are the same
					if (weightedSuppliers.size() > 1) {
						int firstWeight = weightedSuppliers.get(0).getKey();
						boolean allSameWeight = weightedSuppliers.stream().allMatch(weientry -> weientry.getKey() == firstWeight);

						if (allSameWeight) {
							Collections.shuffle(weightedSuppliers);
						}
					}

					return weightedSuppliers.stream();
				})
				.sorted(Comparator.comparingInt(AbstractMap.SimpleEntry::getKey))
				.map(AbstractMap.SimpleEntry::getValue)
				.collect(Collectors.toList());

		// 处理重试的逻辑【如果此请求是重试，要排除上一次请求的supplier】(注意：第一次请求retryCount = 1，每次重试+1)
		if (bizBuyerOrder.getRetryCount() > 1 && supplierList.size() > 1) { // 当只有一个的则不移除了
			log.info("重试, 移除上次请求供应商... ");
			supplierList.removeIf(supplierDO -> supplierDO.getId().equals(bizBuyerOrder.getSupplierId()));
		}

		if (supplierList.isEmpty()) {
			log.error("supplierList is Empty ! 退出机器人查询！");
			bizBuyerOrderService.updateById(bizBuyerOrder);
			return;
		}

		// 执行: 供货商限量计算逻辑
//		List<BizSupplier> usableSupplierList = supplierList.stream()
//				.filter(bizSupplier -> bizRobotQueryRecordService.getTodayCountBySupplier(bizSupplier.getId()) < bizSupplier.getDailyLimitCount())
//				.collect(Collectors.toList());

		List<BizSupplier> usableSupplierList = supplierList.stream()
				.filter(bizSupplier -> bizSupplier.getDailyCount() <= bizSupplier.getDailyLimitCount())
				.collect(Collectors.toList());

		if (usableSupplierList.isEmpty()) {
			log.error("品牌【{}】没有可用供应商, usableSupplierList is Empty. 退出机器人查询！ ", brandName);
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_TIME_NONSUPPORT.getType());
			return;
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
			log.error("品牌【{}】没有有效机器人, robotEffectiveList is Empty. 退出机器人查询！", brandName);
			bizBuyerOrderService.updateById(bizBuyerOrder);
			// 这里添加消息任务 通过消息队列来消费重试
			return;
		}

		//异步请求机器人
		log.info("~~~~ 发起异步请求机器人开始. ");
		List<RebotInfo> finalRobotList = robotEffectiveList;
		CompletableFuture.runAsync(() -> {
			long startTime = System.currentTimeMillis();
			log.info("~~~~ 异步请求机器人. ");
			syncRequestRobot(finalRobotList, bizBuyerOrder);
			log.info("~~~~ 异步流程运行时间：{} ms", (System.currentTimeMillis() - startTime));
		}, executorService);
		log.info("发起异步请求机器人结束~~");
	}


	private void syncRequestRobot(List<RebotInfo> robotList, BizBuyerOrder bizBuyerOrder) {
		log.info("syncRequestRobot >>> 异步请求开始.");
		log.info("syncRequestRobot >>> 未上限的供应商机器人个数：" + robotList.size());
		boolean queueStatus = true;
		//默认0, 无机器人可用1, 2请求失败
		for (RebotInfo bizRobotInfo : robotList) {
			bizBuyerOrder.setSupplierId(bizRobotInfo.getSupplierId());              // 供应商id
			bizBuyerOrder.setSupplierName(bizRobotInfo.getSupplierName());          // 供应商名称
			bizBuyerOrder.setRobotId(bizRobotInfo.getId());
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.QUERYING.getType());

			// 队列验证
			queueStatus = checkQueue(bizBuyerOrder);
			if (!queueStatus) {
				log.info("syncRequestRobot >>> 机器人被占用，请检查REDIS...");
				continue;
			}

			String[] robotProxies = bizRobotInfo.getRobotProxies().split(";");
			for (String robotProxy : robotProxies) {
				if (Boolean.TRUE.equals(robotRequest(bizBuyerOrder, bizRobotInfo, robotProxy))) {
					log.info(">>> 异步请求机器人服务成功. 等待回调");
					bizBuyerOrderService.updateById(bizBuyerOrder);    // 更新订单信息
					return;
				}
			}
		}
		if (queueStatus) {// 无成功返回
			log.info("syncRequestRobot >>> 异步请求机器人失败, 下单失败...");
			bizBuyerOrderService.updateById(bizBuyerOrder);    // 更新订单信息
			// 发送延迟一分钟的消息队列
			long delayTime = System.currentTimeMillis() + (60 * 1000);
			producerUtil.sendTimeMsg(String.valueOf(bizBuyerOrder.getId()), delayTime);
		}
	}

	private Boolean checkQueue(BizBuyerOrder bizBuyerOrder) {
		//检查此品牌的机器人在redis是否标识可用。
		String queueKey = RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + "-" + bizBuyerOrder.getSupplierId();
		String queue = redisTemplate.opsForValue().get(queueKey);

		if (Objects.isNull(queue) || queue.equals(RedisKeyDefine.ENABLE)) {
			redisTemplate.opsForValue().set(RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + bizBuyerOrder.getSupplierId(),
					RedisKeyDefine.DISABLE, 3, TimeUnit.MINUTES);
			return true;
		}
		return false;
	}

	/**
	 * 请求机器人
	 *
	 * @param bizBuyerOrder 订单信息
	 * @param bizRobotInfo  机器人信息
	 * @param robotProxy    代理
	 * @return
	 */
	private Boolean robotRequest(BizBuyerOrder bizBuyerOrder, RebotInfo bizRobotInfo, String robotProxy) {
		log.info("#### 【异步】第一步：异步请求机器人~~~ {}", bizBuyerOrder.getVin());
		String robotAccountPassword = bizRobotInfo.getRobotAccountPassword();
		JSONObject object = JSON.parseObject(robotAccountPassword);
		Map<String, Object> paramMap = new HashMap<>(object.size());
		paramMap.putAll(object);

		paramMap.put("vin", bizBuyerOrder.getVin());
		paramMap.put("brand", bizBuyerOrder.getCarBrandName());
		paramMap.put("host", bizRobotInfo.getHost());
		if (StringUtils.isNotBlank(bizBuyerOrder.getEngineCode())) {
			paramMap.put("engine_no", bizBuyerOrder.getEngineCode());
		}

		if (StringUtils.isNotBlank(robotProxy) && Objects.equals(bizRobotInfo.getNeedDynamicProxy(), BaseConstants.NEED_PROXY)) {
			log.info("机器人代理名称：" + bizRobotInfo.getRobotProxiesName());
			paramMap.put("proxy", robotProxy);
		} else {
			paramMap.put("proxy", robotProxy);
		}
		paramMap.put("task_id", bizBuyerOrder.getId());

		log.info("#### 【异步】{} 请求机器人开始：", bizBuyerOrder.getVin());
		log.info("#### 【异步】{} 机器人请求url：{}", bizBuyerOrder.getVin(), bizRobotInfo.getRobotUrl());
		log.info("#### 【异步】{} 机器人请求数据：{}", bizBuyerOrder.getVin(), JSON.toJSONString(paramMap));
		String result = "";
		try {
			result = HttpRequest.post(bizRobotInfo.getRobotUrl())
					.body(JSON.toJSONString(paramMap))
					.contentType("application/json")
					.timeout(baseConfig.getRequestTimeout() * 1000)
					.execute().body();
		} catch (Exception e) {
			log.error("#### 【异步】{} 机器人请求异常, 异常信息:{}", bizBuyerOrder.getVin(), e.getMessage());
			log.error("#### 【异步】{} 请求机器人结束", bizBuyerOrder.getVin());
			return Boolean.FALSE;
		}
		log.info("#### 【异步】{} 机器人查询数据：{}", bizBuyerOrder.getVin(), result);
		log.info("#### 【异步】{} 请求机器人结束", bizBuyerOrder.getVin());
		JSONObject jsonObject = JSON.parseObject(result);
		return Boolean.TRUE.equals(jsonObject.get("success"));
	}

	/***
	 * 本地数据库数据解析
	 * @param bizBuyerOrder
	 * @param bizRobotQueryRecord
	 * @return
	 */
	private void localMysqlParsing(BizBuyerOrder bizBuyerOrder, BizRobotQueryRecord bizRobotQueryRecord) {
		String vin = bizBuyerOrder.getVin();
		Long order_id = bizBuyerOrder.getId();
		log.info("~~~~ Step3: 开始加载本地 Vin：{} 订单记录", vin);
		BizBuyerOrder successOrder = bizBuyerOrderService.getSuccessMerchantOrderByVin(vin);
		if (Objects.nonNull(successOrder)) {
			log.info("~~~~ Step3.1: 存在本地 Vin：{} 订单记录, 更新订单品牌信息....", vin);
			bizBuyerOrder.setBuyerId(successOrder.getBuyerId());
			bizBuyerOrder.setBuyerName(successOrder.getCarBrandName());
			bizBuyerOrder.setSupplierId(successOrder.getSupplierId());
			bizBuyerOrder.setSupplierName(successOrder.getSupplierName());
			bizBuyerOrder.setRobotId(successOrder.getRobotId());
		}
		//机器人记录赋值采购商订单成功结果
		if (Objects.nonNull(bizRobotQueryRecord.getResult())) {
			log.info("~~~~ Step3.2: 本地数据成功解析....同步到订单数据并回调给商户。");
			String result = bizRobotQueryRecord.getResult();
			bizBuyerOrder.setResult(result);
			CompletableFuture.runAsync(() -> {
				log.info("#### Step3.3: 异步解析开始");
				try {
					AtomicInteger times = new AtomicInteger(1);
//					RetryUtil.executeWithRetry(() -> {
						log.info("#### Step3.4.1: Vin：{} RetryUtil开始回调 第{}次", vin, times);
						times.addAndGet(1);
						RobotResponse robotResponse = JSON.parseObject(result, RobotResponse.class);
						log.info("#### Step3.4.2:  order_id: {}，回调采购商维修数据 : {}", order_id
								, JSON.toJSONString(JSON.parseObject(result, RobotResponse.class)));
						Integer status = callBackService.anyDataMerchantCallBack(bizBuyerOrder, robotResponse);
						log.info("#### Step3.4.3:  order_id: {}，回调采购商返回状态 :status is  【{}】", order_id, status);
						if (status.equals(200)) {// 成功回调, 则更新订单状态
							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_SUCCESS, null);
						} else if (status.equals(200) && times.get() >= 3) {// 三次失败状态
							String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType()));
							updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
						} else if (!status.equals(200)) {
							throw new RuntimeException("返回状态码不正确，重试！");
						}
//						return null;
//					}, 3, 3000L, false);
				} catch (Exception e) {
					// 更新失败原因和失败状态： 回调失败
					log.info("#### Step3.4.3: RetryUtil回调失败....");
					String failureReason = JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_CALLBACK_FAILURE.getType()));
					updateOrderStatus(bizBuyerOrder, RequestStatusEnum.CALLBACK_FAILURE, failureReason);
				}
			});
		}
	}

	/**
	 * 累加供应商请求次数
	 * @param bizBuyerOrder
	 */
	private void addSupplierReqCount(BizBuyerOrder bizBuyerOrder){
		Long id = bizBuyerOrder.getSupplierId();
		bizSupplierService.addSupplierCount(id);
	}

	private void updateOrderStatus(BizBuyerOrder bizBuyerOrder, RequestStatusEnum status, String failureReason) {
		bizBuyerOrder.setRequestStatus(status.getType());
		bizBuyerOrder.setFailureReason(failureReason);
		LocalDateTime nowDateTime = LocalDateTime.now();
		bizBuyerOrder.setCallbackTime(nowDateTime);
		bizBuyerOrder.setSpendTime(DateTimeUitils.localDateTimeBetweenSeconds(bizBuyerOrder.getRequestTime(), nowDateTime));
		bizBuyerOrderService.updateById(bizBuyerOrder);
	}
}
