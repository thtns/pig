package com.pig4cloud.pig.capi.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizCarBrand;
import com.pig4cloud.pig.capi.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.request.RebotCallbackParames.RobotResponse;
import com.pig4cloud.pig.capi.request.RobotCallbackRequest;
import com.pig4cloud.pig.capi.response.MaintenanceOrderRes;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.BizCarBrandService;
import com.pig4cloud.pig.capi.service.BizRobotQueryRecordService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import com.pig4cloud.pig.capi.service.apo.RebotInfo;
import com.pig4cloud.pig.capi.service.apo.RedisKeyDefine;
import com.pig4cloud.pig.capi.service.atripartite.CallBackManager;
import com.pig4cloud.pig.capi.utils.rocketmq.ProducerUtil;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetryUtil;
import com.pig4cloud.pig.common.core.util.UUidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {
	final int TIME_OUT = 2000;


	/*** 机器人查询记录 **/
	private final BizRobotQueryRecordService bizRobotQueryRecordService;

	/*** 采购商记录 **/
	private final BizBuyerOrderService bizBuyerOrderService;

	/*** 品牌 **/
	private final BizCarBrandService bizCarBrandService;

	private final RedisTemplate<String, String> redisTemplate;

	private final CallBackManager callBackManager;

	private final ProducerUtil producerUtil;


	public R<Object> processMaintenanceOrder(BizBuyerOrder bizBuyerOrder) {
		try {
			String vin = bizBuyerOrder.getVin();
			Long buyerId = bizBuyerOrder.getBuyerId();
			log.info("~~~~ Step2: 通过Vin：{} 查询机器人查询记录.", vin);
			BizRobotQueryRecord bizRobotQueryRecord = bizRobotQueryRecordService.getQueryRecordByVin(vin);

			if (Optional.ofNullable(bizRobotQueryRecord).isPresent()) {
				log.info("~~~~ Step2.1: 本地存在 Vin：{} 机器人查询记录. 加载本地结果数据.....", vin);
				localMysqlParsing(bizBuyerOrder, bizRobotQueryRecord);
			} else {
				log.info("~~~~ Step2.1: 本地不存在 Vin：{}  机器人查询记录. ", vin);
				log.info("~~~~ Step2.2: 通过Vin：{} 和 BuyerId：{} 查询是否已下过的订单.... ", vin, buyerId);
				boolean isPlaceOrder = bizBuyerOrderService.isOrNotPlaceOrder(vin, buyerId);
				if (isPlaceOrder) {
					log.info("~~~~ Step2.3: 本地存在Vin：{} 和 BuyerId：{} 的订单. 发送订单到延迟消息队列. ", vin, buyerId);
					sendDelayedMessage(bizBuyerOrder);    // 有同样请求,将消息发送到延迟队列, 等有结果后加载本地并回调商家
				} else {
					log.info("~~~~ Step2.3: 本地不存在Vin：{} 和 BuyerId：{} 的订单. 发起新机器人订单. ", vin, buyerId);
					spiderServerRequest(bizBuyerOrder); // 新发起机器人请求
				}
			}
		} catch (RuntimeException runtimeException) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orderId", bizBuyerOrder.getId());
			return R.resultEnumType(jsonObject, RequestStatusEnum.ORDER_FAILURE.getType());
		}
		int code = RequestStatusEnum.ORDER_SUCCESS.getType();
		if (!Objects.equals(bizBuyerOrder.getRequestStatus(), RequestStatusEnum.QUERYING.getType())
				|| !Objects.equals(bizBuyerOrder.getRequestStatus(), RequestStatusEnum.ORDER_SUCCESS.getType())
				|| !Objects.equals(bizBuyerOrder.getRequestStatus(), RequestStatusEnum.CALLBACK_SUCCESS.getType())) {
			code = bizBuyerOrder.getRequestStatus();
		}
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("data", buildMaintenanceOrderRes(bizBuyerOrder));
		return R.ok(obj, "操作成功!");
	}

	private void sendDelayedMessage(BizBuyerOrder bizBuyerOrder) {
		long delayTime = System.currentTimeMillis() + (300 * 1000);
		producerUtil.sendTimeMsg(String.valueOf(bizBuyerOrder.getId()), delayTime);
	}

	private MaintenanceOrderRes buildMaintenanceOrderRes(BizBuyerOrder bizBuyerOrder){
		boolean falg = true;
		while (falg)
			try{
				Thread.sleep(TIME_OUT);
				bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
				falg = false;
			}catch (Exception e){

			}

		return MaintenanceOrderRes.builder()
				.order_id(bizBuyerOrder.getId())
				.vin(bizBuyerOrder.getVin())
				.build();
	}


	/**
	 * 根据订单id主动查询订单结果回调
	 *
	 * @param bizBuyerOrder
	 */
	@Override
	public void
	getResult(BizBuyerOrder bizBuyerOrder) {
		log.info("主动回调商家：order：" + bizBuyerOrder.getId());
		processMaintenanceOrder(bizBuyerOrder);// 执行
	}

	/***
	 * 本地数据库数据解析
	 * @param bizBuyerOrder
	 * @param bizRobotQueryRecord
	 * @return
	 */
	private void localMysqlParsing(BizBuyerOrder bizBuyerOrder, BizRobotQueryRecord bizRobotQueryRecord) {
		String vin = bizBuyerOrder.getVin();
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
		log.info("~~~~ Step3.2: 本地数据解析....");
		CompletableFuture.runAsync(() -> {
			log.info("#### Step3.3: 异步解析开始");
			try {
				Thread.sleep(TIME_OUT);
				RetryUtil.executeWithRetry(() -> {
					log.info("#### Step3.4.1: Vin：{} RetryUtil开始回调 ", vin);
					RobotResponse robotResponse = JSONObject.parseObject(bizRobotQueryRecord.getResult(), RobotResponse.class);
					log.info("#### Step3.4.2:  order_id: {}，回调采购商维修数据 : {}", vin
							, JSON.toJSONString(JSONObject.parseObject(bizRobotQueryRecord.getResult(), RobotResponse.class)));
					return callBackManager.merchantCallBack(bizBuyerOrder, robotResponse);
				}, 3, 1000L, false);
			} catch (Exception e) {
				// 更新失败原因和失败状态： 回调失败
				log.info("#### Step3.4.3: RetryUtil回调失败....");
				bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_FAILURE.getType());
				bizBuyerOrder.setFailureReason(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.CALLBACK_FAILURE.getType())));
				bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);
			}
		});

		//机器人记录赋值采购商订单成功结果
		if (Objects.nonNull(bizRobotQueryRecord.getResult())) {
			bizBuyerOrder.setResult(bizRobotQueryRecord.getResult());
		}
		//机器人记录赋值采购商订单失败原因
		if (StringUtils.isNotBlank(bizRobotQueryRecord.getFailureReason())) {
			bizBuyerOrder.setFailureReason(bizRobotQueryRecord.getFailureReason());
		}

		bizBuyerOrder.setCallbackTime(LocalDateTime.now());
		bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_SUCCESS.getType());
		log.info("~~~~ Step4 本地数据解析结束, 商家数据赋值更新 ：" + JSON.toJSONString(bizBuyerOrder));
	}


	/**
	 * 机器人请求供应商
	 *
	 * @param bizBuyerOrder
	 */
	private void spiderServerRequest(BizBuyerOrder bizBuyerOrder) {
		log.info("~~~~ Step3: 新发起机器人订单开始. ");
		bizBuyerOrder.setRetryCount(bizBuyerOrder.getRetryCount() + 1);
		BizCarBrand carBrand = null;
		try {
			carBrand = bizCarBrandService.getCarBrand(bizBuyerOrder);
			log.info("~~~~ Step3.1.4: 解析后的品牌信息: {}.", JSON.toJSONString(carBrand));
		} catch (RuntimeException runtimeException) { // 不支持该品牌
			// 里面抛错这里直接返回在外面保存更新
			return;
		}

		log.info("~~~~ Step3.2: 开始匹配有效机器人. ");
		long startTimeEffective = System.currentTimeMillis();
		List<RebotInfo> robotList = null;
		try {
			robotList = bizCarBrandService.getEffectiveRobot(carBrand, bizBuyerOrder);
			log.info("选举机器人运行时间：" + (System.currentTimeMillis() - startTimeEffective) + "ms");
		} catch (RuntimeException runtimeException) { // 没有可用的机器人
			return;
		}

		//异步请求机器人
		List<RebotInfo> finalRobotList = robotList;
		CompletableFuture.runAsync(() -> {
			long startTime = System.currentTimeMillis();
			log.info("~~~~ Step3.3: 异步请求机器人. ");
			syncRequestRobot(finalRobotList, bizBuyerOrder);
			log.info("~~~~ Step3.4: 异步流程运行时间：{} ms", (System.currentTimeMillis() - startTime));
		});
		log.info("~~~~ Step3.5:异步请求机器人完成~~");
	}

	private void syncRequestRobot(List<RebotInfo> robotList, BizBuyerOrder bizBuyerOrder) {
		log.info(">>> 异步请求开始.");
		log.info(">>> 未上限的供应商机器人个数：" + robotList.size());
		boolean requestRobotFlag = false;
		for (RebotInfo bizRobotInfo : robotList) {
			bizBuyerOrder.setSupplierId(bizRobotInfo.getSupplierId());              // 供应商id
			bizBuyerOrder.setSupplierName(bizRobotInfo.getSupplierName());          // 供应商名称
			bizBuyerOrder.setRobotId(bizRobotInfo.getId());
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.QUERYING.getType());
			bizBuyerOrderService.saveOrUpdate(bizBuyerOrder);                       // 更新订单信息

			// 队列验证
			if (!checkQueue(bizBuyerOrder)) {
				log.info(">>> 机器人被占用，请检查REDIS...");
				continue;
			}

			String[] robotProxies = bizRobotInfo.getRobotProxies().split(";");
			for (String robotProxy : robotProxies) {
				requestRobotFlag = robotRequest(bizBuyerOrder, bizRobotInfo, robotProxy);
			}
		}
		if (!requestRobotFlag) {// 说明所有请求都是失败的
			log.info(">>> 异步请求机器人服务失败，准备解锁REDIS...");
			UnlockQueue(bizBuyerOrder);
			log.info(">>> 解锁REDIS 完成...");
			bizBuyerOrder.setRequestStatus(RequestStatusEnum.ORDER_FAILURE.getType()); //更新下单失败
			bizBuyerOrderService.updateById(bizBuyerOrder);
			log.info(">>> 更新订单状态 {} 完成...", RequestStatusEnum.ORDER_FAILURE.getDescription());
		}else{
			log.info(">>> 异步请求机器人服务成功. 等待回调");
		}
	}

	private Boolean checkQueue(BizBuyerOrder bizBuyerOrder) {
		//检查此品牌的机器人在redis是否标识可用。
		String queueKey = RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + bizBuyerOrder.getSupplierId();
		String queue = redisTemplate.opsForValue().get(queueKey);

		if (Objects.isNull(queue) || queue.equals(RedisKeyDefine.ENABLE)) {
			redisTemplate.opsForValue().set(RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + bizBuyerOrder.getSupplierId(),
					RedisKeyDefine.DISABLE, 8, TimeUnit.MINUTES);
			return true;
		}
		return false;
	}

	private void UnlockQueue(BizBuyerOrder bizBuyerOrder) {
		String queueKey = RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + bizBuyerOrder.getSupplierId();
		String queue = redisTemplate.opsForValue().get(queueKey);
		if (RedisKeyDefine.DISABLE.equals(queue)) {
			log.info("~~~解锁redis ：{}", queueKey);
			redisTemplate.opsForValue().set(queueKey, RedisKeyDefine.ENABLE, 8, TimeUnit.MINUTES);
		}
	}

	private Boolean checkProxy(String proxyUrl) {
		return true;
//		if (StringUtils.isBlank(proxyUrl)) {
//			return false;
//		}
//		log.info("~~~~开始检查代理地址是否可用：" + proxyUrl);
//		String hostname = "";
//		int port = 0;
//		String account = null;
//		String password = "";
//
//		if (proxyUrl.contains("@")) {
//			log.info("带帐号密码...");
//			String[] accountInfo = proxyUrl.split("@");
//			log.info(accountInfo[0]);
//			if (accountInfo.length > 0) {
//				String[] split = accountInfo[0].split(":");
//				log.info(JSON.toJSONString(split));
//				account = split[1].substring(2);
//				password = split[2];
//			}
//			if (accountInfo.length > 1) {
//				String[] addrInfo = accountInfo[1].split(":");
//				hostname = addrInfo[0];
//				port = Integer.parseInt(addrInfo[1]);
//				//构造proxy的地址和端口并返回
//				SocketAddress socketAddress = new InetSocketAddress(hostname,port);
//			}
//		}
//		else {
//			String[] split = proxyUrl.split(":");
//			log.info(JSON.toJSONString(split));
//			hostname = split[0];
//			port = Integer.parseInt(split[1]);
//		}
//		try {
//			HttpHost proxy = new HttpHost(hostname, port, "http");
//			CredentialsProvider provider = new BasicCredentialsProvider();
//			provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(account, password));
//			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
//			RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
//			CloseableHttpResponse response = null;
//			HttpGet get = new HttpGet("http://www.baidu.com");
//			get.setConfig(requestConfig);
//			response = httpClient.execute(get);
//
//			InputStream inputStream = response.getEntity().getContent();
//			String s = IOUtils.toString(inputStream);
//			if (s.indexOf("百度") > 0 || s.indexOf("baidu") > 0) {
//				log.info("~~~~可用");
//				return true;
//			}
//		}catch (IOException e){
//			e.printStackTrace();
//		}
//		log.info("~~~~不可用");
//		return false;
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
		JSONObject object = JSONObject.parseObject(robotAccountPassword);
		Map<String, Object> paramMap = new HashMap<>(object.size());
		paramMap.putAll(object);

		paramMap.put("vin", bizBuyerOrder.getVin());
		paramMap.put("host", bizRobotInfo.getHost());
		if (StringUtils.isNotBlank(bizBuyerOrder.getEngineCode())) {
			paramMap.put("engine_no", bizBuyerOrder.getEngineCode());
		}

		if (StringUtils.isNotBlank(robotProxy) && Objects.equals(bizRobotInfo.getNeedDynamicProxy(), BaseConstants.NEED_PROXY)) {
			log.debug("机器人代理名称：" + bizRobotInfo.getRobotProxiesName());
			paramMap.put("proxy", robotProxy);
		} else {
			paramMap.put("proxy", robotProxy);
		}
		paramMap.put("task_id", bizBuyerOrder.getId());

		log.info("#### 【异步】{} 请求机器人开始：", bizBuyerOrder.getVin());
		log.info("#### 【异步】{} 机器人请求url：{}", bizBuyerOrder.getVin(), bizRobotInfo.getRobotUrl());
		log.info("#### 【异步】{} 机器人请求数据：{}", bizBuyerOrder.getVin(), JSON.toJSONString(paramMap));
		String result = HttpRequest.post(bizRobotInfo.getRobotUrl())
				.body(JSON.toJSONString(paramMap))
				.contentType("application/json")
				.execute().body();
		log.info("#### 【异步】{} 请求机器人结束", bizBuyerOrder.getVin());
		log.info("#### 【异步】{} 机器人查询数据：{}", bizBuyerOrder.getVin(), result);
		JSONObject jsonObject = JSONObject.parseObject(result);
		return Boolean.TRUE.equals(jsonObject.get("success"));
	}

	public void robotRequestCallBack(RobotCallbackRequest rebotCallbackRequest) throws Exception {
		BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(rebotCallbackRequest.getOrderId());
		RobotResponse robotResponse = rebotCallbackRequest.getRobotResponse();// 机器人数据

		BizRobotQueryRecord bizRobotQueryRecord = BizRobotQueryRecord
				.builder()
				.id(UUidUtils.uuLongId())
				.vin(bizBuyerOrder.getVin())
				.supplierId(bizBuyerOrder.getSupplierId())
				.supplierName(bizBuyerOrder.getSupplierName())
				.robotId(bizBuyerOrder.getRobotId())
				.resultStatus(BaseConstants.ROBOT_QUERY_STATUS_SUCCESS)
				.result(JSON.toJSONString(robotResponse))
				.querytime(LocalDateTime.now())
				.build();
		log.info("vin {{}} robotRequestCallBack 保存机器人查询记录： {}", bizBuyerOrder.getVin(), JSON.toJSONString(JSON.toJSONString(robotResponse)));
		//保存机器人请求成功结果
		CompletableFuture<Void> cf = CompletableFuture.runAsync(() ->
				bizRobotQueryRecordService.save(bizRobotQueryRecord)
		);

		// 异步回调商家
		RetryUtil.asyncExecuteWithRetry(() -> callBackManager.merchantCallBack(bizBuyerOrder, robotResponse),
				3, 3000L, false, 6000L, null);

		bizBuyerOrder.setRequestStatus(RequestStatusEnum.CALLBACK_SUCCESS.getType());
		bizBuyerOrder.setResult(JSON.toJSONString(robotResponse));
		bizBuyerOrder.setCallbackTime(LocalDateTime.now());
		bizBuyerOrderService.updateById(bizBuyerOrder);
		log.info("vin {{}} robotRequestCallBack 更新订单状态： {}", bizBuyerOrder.getVin(), RequestStatusEnum.CALLBACK_SUCCESS.getDescription());
	}

}
