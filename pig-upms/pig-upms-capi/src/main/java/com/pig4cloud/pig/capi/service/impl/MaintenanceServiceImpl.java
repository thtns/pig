package com.pig4cloud.pig.capi.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.pig4cloud.pig.admin.api.entity.BizBuyerOrder;
import com.pig4cloud.pig.admin.api.entity.BizCarBrand;
import com.pig4cloud.pig.admin.api.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.response.MaintenanceOrderRes;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizCarBrandService;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import com.pig4cloud.pig.capi.service.apo.RebotInfo;
import com.pig4cloud.pig.capi.service.apo.RedisKeyDefine;
import com.pig4cloud.pig.capi.utils.rocketmq.ProducerUtil;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.LongByteUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import com.pig4cloud.pig.common.core.util.RetryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Slf4j
public class MaintenanceServiceImpl implements MaintenanceService {
	final int TIME_OUT = 2000;



	/*** 机器人查询记录 **/
	BizRobotQueryRecordService bizRobotQueryRecordService;

	/*** 采购商记录 **/
	BizBuyerOrderService bizBuyerOrderService;

	/*** 品牌 **/
	BizCarBrandService bizCarBrandService;

	RedisUtils redisUtils;

	@Override
	public MaintenanceOrderRes maintenanceOrder(BizBuyerOrder bizBuyerOrder) {
		BizRobotQueryRecord bizRobotQueryRecord = bizRobotQueryRecordService.getQueryRecordByVin(bizBuyerOrder.getVin());
		// 数据库机器人有vin查询记录
		if (Objects.nonNull(bizRobotQueryRecord)) {
			localMysqlParsing(bizBuyerOrder, bizRobotQueryRecord);
			return MaintenanceOrderRes.builder().order_id(String.valueOf(bizBuyerOrder.getId())).vin(bizBuyerOrder.getVin()).build();
		}
		/***
		 * 因为有重试机制和队列机制存在，并且采购商可以主动发起回调
		 * 		因此，我们会通过采购商id和vin判断今天是否有订单，存在的话就放入延迟队列中；
		 * 			延迟队列触发之后也会回调 maintenanceOrder,
		 * 			所以当greater为false时，则是新发起，为true时则是客户回调请求或 延迟队列调用
		 */
		Boolean greater = bizBuyerOrderService.isOrNotPlaceOrder(bizBuyerOrder.getVin(), bizBuyerOrder.getBuyerId());
		if (greater) {
			// 加入延时队列，5分钟后执行。
			ProducerUtil.sendExcarTimeMsg(Long.toString(bizBuyerOrder.getId()), Long.toString(bizBuyerOrder.getId()),
					LongByteUtils.toByteArray(bizBuyerOrder.getId()), System.currentTimeMillis() + (5 * 60 * 1000));
			return MaintenanceOrderRes.builder().order_id(String.valueOf(bizBuyerOrder.getId())).vin(bizBuyerOrder.getVin()).build();
		} else {
			// 设置请求次数，用户后续判断
			bizBuyerOrder.setRetryCount(bizBuyerOrder.getRetryCount() + 1);
			robotRequest(bizBuyerOrder);
		}

		return MaintenanceOrderRes.builder().order_id(String.valueOf(bizBuyerOrder.getId())).vin(bizBuyerOrder.getVin()).build();
	}

	/**
	 * 根据订单id主动查询订单结果回调
	 *
	 * @param bizBuyerOrder
	 */
	@Override
	public void getResult(BizBuyerOrder bizBuyerOrder) {
		log.info("主动回调商家：order：" + bizBuyerOrder.getId());
		maintenanceOrder(bizBuyerOrder);// 执行
	}

	/***
	 * 本地数据库数据解析
	 * @param bizBuyerOrder
	 * @param bizRobotQueryRecord
	 * @return
	 */
	private void localMysqlParsing(BizBuyerOrder bizBuyerOrder, BizRobotQueryRecord bizRobotQueryRecord) {
		// 验证vin+merchantId+createDate是否有数据，如果有addJob走延时队列
//		BizBuyerOrder successMerchantOrderByVin = bizBuyerOrderService.getSuccessMerchantOrderByVin(bizBuyerOrder.getVin());
		log.info("~~~~第三步【本地数据库有vin请求数据】：本地数据解析....");
		// 异步执行解析
		CompletableFuture.runAsync(() -> {
			log.info("####第四步【本地数据库有vin:%s请求数据】（异步）：异步解析开始");
			try {
				Thread.sleep(TIME_OUT);
				String res = RetryUtil.executeWithRetry(() -> {
					log.info("####第二步【本地数据库有vin请求数据】（异步）：RetryUtil开始回调，回调采购商维修数据");
					return merchantCallBack(bizBuyerOrder, bizRobotQueryRecord);
				}, 3, 1000L, false);
			} catch (Exception e) {
				// 更新失败原因和失败状态： 回调失败
				bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
				bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.CALLBACK_FAILURE.getType())));
				bizBuyerOrderService.updateById(bizBuyerOrder);
			}
		});

		//机器人记录赋值采购商订单成功结果
		if (Objects.nonNull(bizRobotQueryRecord.getResult())) {
			bizBuyerOrder.setResult(JSON.toJSONString(bizRobotQueryRecord.getResult()));
		}
		//机器人记录赋值采购商订单失败原因
		if (StringUtils.isNotBlank(bizRobotQueryRecord.getFailureReason())) {
			bizBuyerOrder.setFailureReason(JSON.toJSONString(bizRobotQueryRecord.getFailureReason()));
		}

		bizBuyerOrder.setCallbackTime(LocalDateTime.now());
		bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_SUCCESS.getType());
		bizBuyerOrderService.updateById(bizBuyerOrder);
		log.info("~~~~第四步【数据库有数据】：结束，商家访问数据库更新成功" + JSON.toJSONString(bizBuyerOrder));
	}

	/***
	 * 给商家做回调请求
	 * @param bizBuyerOrder
	 * @param bizRobotQueryRecord
	 */
	public String merchantCallBack(BizBuyerOrder bizBuyerOrder, BizRobotQueryRecord bizRobotQueryRecord) {
		// 如果回调明细为空，则回调查无记录
		if (Objects.isNull(bizRobotQueryRecord.getResult())) {
			return merchantCallBackError(bizBuyerOrder, RequestStatusEnum.ORDER_SUCCESS.getType(), RequestStatusEnum.SERVER_NO_RESULT.getType());
		}

		// 假如有三方名称和状态的话，这里需要同步;暂时没有移除
//		StandardBrand standardBrand = new StandardBrand();
//		standardBrand.setSub_brand(merchantOrderRecordDO.getSubBrand());
//		standardBrand.setBrand_code(merchantOrderRecordDO.getPrefix());
//		standardBrand.setBrand(merchantOrderRecordDO.getBrand());
//		robotResponse.setStandard_brand(standardBrand);
		log.info("####第三步（回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("order_id", bizBuyerOrder.getId());
		paramMap.put("maintain_data", bizRobotQueryRecord.getResult());
		R result = R.ok(paramMap);

		log.info("####第四步（回调商家）：给商家最终结果" + JSON.toJSONString(result));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(result)).contentType("application/json").execute().body();
	}

	/**
	 * 给商家做回调请求
	 * 错误回调
	 *
	 * @param bizBuyerOrder 采购商订单
	 * @param statusCode    请求状态
	 * @param errorCode     错误编码
	 * @return
	 */
	public String merchantCallBackError(BizBuyerOrder bizBuyerOrder, Integer statusCode, Integer errorCode) {
		String failureReason = JSON.toJSONString(R.resultEnumType(null, errorCode));
		//更新采购订单id
		bizBuyerOrder.setFailureReason(failureReason);
		bizBuyerOrder.setRequestStatus(statusCode);
		bizBuyerOrder.setCallbackTime(LocalDateTime.now());
		bizBuyerOrderService.updateById(bizBuyerOrder);

		log.info("异常回调merchantOrderRecordDO ：" + JSON.toJSONString(bizBuyerOrder));
		if (errorCode.equals(RequestStatusEnum.SERVER_NO_RESULT.getType())) {
			//调查无记录,存储本次查询
			bizRobotQueryRecordService.save(
					BizRobotQueryRecord.builder()
							.vin(bizBuyerOrder.getVin())
							.robotId(bizBuyerOrder
									.getRobotId())
							.supplierId(bizBuyerOrder
									.getSupplierId())
							.supplierName(bizBuyerOrder.getSupplierName())
							.resultStatus(0)                          // 0失败,1成功
							.failureReason(failureReason)             // 失败原因； 如果成功的话则需要设置result
							.querytime(LocalDateTime.now()).build()); // 当前时间
		}

		log.info("####第三步（回调商家）：开始");
		Map<String, Object> paramMap = new HashMap<>(16);
		paramMap.put("order_id", bizBuyerOrder.getId());
		paramMap.put("maintain_data", null);
		R result = R.resultEnumType(paramMap, errorCode);

		log.info("####第四步（回调商家）：给商家最终结果" + JSON.toJSONString(result));
		return HttpRequest.post(bizBuyerOrder.getCallbackUrl()).body(JSON.toJSONString(result)).contentType("application/json").execute().body();
	}

	/**
	 * 机器人请求供应商
	 *
	 * @param bizBuyerOrder
	 */
	private void robotRequest(BizBuyerOrder bizBuyerOrder) {
		//解析VIN，获取品牌
		BizCarBrand carBrand = bizCarBrandService.getCarBrand(bizBuyerOrder);
		log.info("~~~~第三步：品牌" + JSON.toJSONString(carBrand));

		//匹配机器人，获取有效的供应商【后期可以加入机器人选举策略】
		long startTimeEffective = System.currentTimeMillis();
		List<RebotInfo> robotList = bizCarBrandService.getEffectiveRobot(carBrand, bizBuyerOrder);
		log.info("&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*选举机器人运行时间：" + (System.currentTimeMillis() - startTimeEffective) + "ms");

		// 未找到可用机器人
		if (robotList.isEmpty()) {
			if (bizBuyerOrder.getRetryCount() > 1) {
				log.info("错误回调给商家：供应商ID：" + bizBuyerOrder.getSupplierId());
				if (bizBuyerOrder.getRetryCount() > 1) {
					log.info("错误回调给商家：供应商ID：" + bizBuyerOrder.getSupplierId());
					merchantCallBackError(bizBuyerOrder, RequestStatusEnum.ORDER_SUCCESS.getType(), RequestStatusEnum.SERVER_QUERY_FAILURE.getType());
				}
				bizBuyerOrder.setRequestStatus(RequestStatusEnum.API_TIME_NONSUPPORT.getType());
				bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_TIME_NONSUPPORT.getType())));
				bizBuyerOrderService.updateById(bizBuyerOrder);
				throw new RuntimeException(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.API_TIME_NONSUPPORT.getType())));
			}
		}

		//异步请求机器人
		CompletableFuture.runAsync(() -> {
			long startTime = System.currentTimeMillis();
			log.info("第三步：异步请求机器人~~");
			syncRequestRobot(robotList, bizBuyerOrder);
			log.info("&*&*&*&*&*&*&*&*&*&*&*&*&*&*&*整个异步流程运行时间：" + (System.currentTimeMillis() - startTime) + "ms");
		});
		log.info("第四步：异步请求机器人完成~~");
	}

	private void syncRequestRobot(List<RebotInfo> robotList, BizBuyerOrder bizBuyerOrder) {
		log.info(">>> 未上限的供应商机器人个数：" + robotList.size());
		boolean queueStatus = true;
		for (int i = 0; i < robotList.size(); i++) {
			RebotInfo bizRobotInfo = robotList.get(i);
			bizBuyerOrder.setSupplierId(bizRobotInfo.getSupplierId());        //供应商id
			bizBuyerOrder.setSupplierName(bizRobotInfo.getSupplierName());    //供应商名称
			bizBuyerOrder.setRobotId(bizRobotInfo.getId());
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.QUERYING.getType());
			bizBuyerOrderService.updateById(bizBuyerOrder); //更新订单信息

			// 队列验证
			queueStatus = checkQueue(bizRobotInfo, bizBuyerOrder);
			if (!queueStatus) {
				log.info("####机器人被占用，请检查REDIS...");
				continue;
			}

			String[] robotProxies = bizRobotInfo.getRobotProxies().split(";");
			// 如果无代理， 或者代理可用
			if (robotProxies.length == 0 && robotRequest(bizBuyerOrder, bizRobotInfo, null)) {
				return;
			}
			for (String robotProxy : robotProxies) {
				if (checkProxy(robotProxy)) {
					if (robotRequest(bizBuyerOrder, bizRobotInfo, robotProxy)) {
						return;
					}
				}
			}
		}
	}

	private Boolean checkQueue(RebotInfo rebotInfo, BizBuyerOrder bizBuyerOrder) {
		//检查此品牌的机器人在redis是否标识可用。

		String queue = RedisUtils.get(RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + rebotInfo.getSupplierId());

		if (Objects.isNull(queue) || queue.equals(RedisKeyDefine.ENABLE)) {
			RedisUtils.set(RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + rebotInfo.getSupplierId(),
					RedisKeyDefine.DISABLE, 8, TimeUnit.MINUTES);
			return true;
		}
		return false;
	}

	private Boolean checkProxy(String proxyUrl) {
		if (StringUtils.isBlank(proxyUrl)) {
			return false;
		}

		log.info("~~~~开始检查代理地址是否可用：" + proxyUrl);
		String headerKey = "Proxy-Authorization";
		String headerValue = "";
		InetSocketAddress addr = null;
		URL url = null;
		try {
			url = new URL("http://www.baidu.com");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (proxyUrl.contains("@")) {
			log.info("带帐号密码...");
			String[] accountInfo = proxyUrl.split("@");
			log.info(accountInfo[0]);
			if (accountInfo.length > 0) {
				String[] split = accountInfo[0].split(":");
				log.info(JSON.toJSONString(split));
				String account = split[1].substring(2);
				String password = split[2];

				headerValue = "Basic " + Base64.encode(account + ":" + password);
			}
			if (accountInfo.length > 1) {
				String[] addrInfo = accountInfo[1].split(":");
				addr = new InetSocketAddress(addrInfo[0], Integer.parseInt(addrInfo[1]));
			}
		} else {
			String[] split = proxyUrl.split(":");
			log.info(JSON.toJSONString(split));
			addr = new InetSocketAddress(split[1].substring(2), Integer.parseInt(split[2]));
		}

		Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
		URLConnection conn;
		try {
			conn = url.openConnection(proxy);
			conn.setConnectTimeout(6000);
			conn.setReadTimeout(6000);
			conn.setRequestProperty(headerKey, headerValue);
			InputStream in = conn.getInputStream();
			String s = IOUtils.toString(in);
			if (s.indexOf("百度") > 0 || s.indexOf("baidu") > 0) {
				log.info("~~~~可用");
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("~~~~不可用");
		return false;
	}

	/**
	 * 请求机器人
	 * @param bizBuyerOrder		订单信息
	 * @param bizRobotInfo		机器人信息
	 * @param robotProxy		代理
	 * @return
	 */
	private Boolean robotRequest(BizBuyerOrder bizBuyerOrder, RebotInfo bizRobotInfo, String robotProxy){
		log.info("####第一步【异步】：异步请求机器人~~~ vin");
		String robotAccountPassword = bizRobotInfo.getRobotAccountPassword();
		JSONObject object = JSONObject.parseObject(robotAccountPassword);

		Map<String, Object> paramMap = new HashMap<>(16);
		object.forEach(paramMap::put);

		paramMap.put("host", bizRobotInfo.getRobotUrl());
		if (StringUtils.isNotBlank(bizBuyerOrder.getEngineCode())) {
			paramMap.put("engine_no", bizBuyerOrder.getEngineCode());
		}

		if (StringUtils.isEmpty(robotProxy) && bizRobotInfo.getNeedDynamicProxy() == BaseConstants.NEED_PROXY) {
			log.debug("机器人代理名称：" + bizRobotInfo.getRobotProxiesName());
			paramMap.put("proxy", robotProxy);
		} else {
			paramMap.put("proxy", robotProxy);
		}
		paramMap.put("task_id", bizBuyerOrder.getId());

		log.info("####请求机器人开始" + bizBuyerOrder.getVin());
		String result = HttpRequest.post(bizRobotInfo.getRobotUrl() + "/" + bizBuyerOrder.getVin())
				.body(JSON.toJSONString(paramMap))
				.contentType("application/json")
				.execute().body();
		log.info("####请求机器人结束" + bizBuyerOrder.getVin());
		log.info("####机器人请求url：" + bizRobotInfo.getRobotUrl() + "/" + bizBuyerOrder.getVin());
		log.info("####机器人请求数据：" + JSON.toJSONString(paramMap));
		log.info("####机器人查询数据：" + result);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if ((Boolean) jsonObject.get("success")) {
			return true;
		}
		return false;
	}
}
