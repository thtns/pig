package com.pig4cloud.pig.capi.utils.rocketmq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.entity.BizRobotQueryRecord;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.MainCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OderMessageListener implements MessageListener {

	private final BizBuyerOrderService bizBuyerOrderService;

	private final MainCoreService mainCoreService;

	@Override
	public Action consume(Message message, ConsumeContext context) {
		String orderId = new String(message.getBody());
		log.info("解析MQ-Body自定义内容：{}", orderId);
		try {
			Long bizBuyerOrderId = Long.parseLong(orderId);
			BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(bizBuyerOrderId);
			if (bizBuyerOrder != null) {
				if(bizBuyerOrder.getRetryCount() < 5){
					mainCoreService.processOrder(bizBuyerOrder);
				}
			}else {
				log.error("订单id {} 不存在, 请检查。消费订单信息完成！", orderId);
			}
		}catch (NumberFormatException e) {
			log.error("无法解析订单id为Long类型：{}", orderId, e);
			return Action.ReconsumeLater;
		} catch (Exception e) {
			//消费失败
			return Action.ReconsumeLater;
		}
		return Action.CommitMessage;
	}

}