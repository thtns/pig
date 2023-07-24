package com.pig4cloud.pig.capi.service.atripartite;

import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.CallBackService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import com.pig4cloud.pig.common.core.constant.enums.capi.BaseConstants;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTask {
	private final BizBuyerOrderService bizBuyerOrderService;

	private final CallBackService callBackService;

	@Scheduled(cron = "0 0/2 * * * ?")
	public void rejectOrder() {
		log.info("ScheduledTask: 【rejectOrder】 开始执行");
		List<BizBuyerOrder> bizBuyerOrders = bizBuyerOrderService.getDalyOrder();
		log.info("ScheduledTask: 【rejectOrder】 需要驳回的订单总计数量为： {}", bizBuyerOrders.size());

		AtomicInteger currentIndex = new AtomicInteger(0);
		int totalSize = bizBuyerOrders.size();

		bizBuyerOrders.parallelStream()
				.forEach(order -> {
					int index = currentIndex.incrementAndGet();
					log.info("ScheduledTask: 【rejectOrder】 order_id - {} 当前进度： {}/{}", order.getId(), index, totalSize);
					callBackService.reject(order);// 无记录回调
				});

	}

//    @Scheduled(fixedDelay = 30000)
//    public void testTwo(){
//		log.info("每30秒执行一次");
//    }
//
//    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行
//    public void initTask(){
//        //执行任务
//		log.info("执行任务"+new Date());
//    }

}