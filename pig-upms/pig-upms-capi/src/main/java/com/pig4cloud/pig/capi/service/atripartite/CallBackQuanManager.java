package com.pig4cloud.pig.capi.service.atripartite;


import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import com.pig4cloud.pig.capi.service.apo.RedisKeyDefine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallBackQuanManager {

	private final RedisTemplate<String, String> redisTemplate;

	private final MaintenanceService maintenanceService;

	private final BizBuyerOrderService bizBuyerOrderService;

	public void callBackQueueManage(BizBuyerOrder bizBuyerOrder) {
		//回调后如品牌使用队列模式；1、删除redis队列标示。2、查询是否有排队的数据执行。
		log.info("~~~回调---进入队列判断");
		String queueKey = RedisKeyDefine.QUEUE + bizBuyerOrder.getCarBrandId() + bizBuyerOrder.getSupplierId();
		String queue = redisTemplate.opsForValue().get(queueKey);
		log.info("~~~redis中队列数据：" + queue);
		if (RedisKeyDefine.DISABLE.equals(queue)) {
			log.info("~~~更改redis中数据");
			redisTemplate.opsForValue().set(queueKey, RedisKeyDefine.ENABLE, 8, TimeUnit.MINUTES);
		}
		// 查询数据库排队的数据
		log.info("~~~查询数据库");
		BizBuyerOrder newOrder = bizBuyerOrderService.getSameCarBrandOrder(bizBuyerOrder);
		if (Objects.nonNull(newOrder)) {
			log.info("~~~查询数据库中有数据");
			CompletableFuture.runAsync(() ->
				maintenanceService.processMaintenanceOrder(newOrder)
			);
		}
	}
}
