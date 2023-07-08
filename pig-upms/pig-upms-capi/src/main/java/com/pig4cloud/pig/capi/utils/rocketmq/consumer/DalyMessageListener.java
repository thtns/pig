package com.pig4cloud.pig.capi.utils.rocketmq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class DalyMessageListener implements MessageListener {

	private final BizBuyerOrderService bizBuyerOrderService;

	private final MaintenanceService maintenanceService;

	public Action consume(Message message, ConsumeContext consumeContext) {
		log.info("接收到MQ详细信息：{}", message);
		try {
			String orderId = new String(message.getBody());
			log.info("解析MQ-Body自定义内容：{}", orderId);
			log.info("orderId ：{}", Long.parseLong(orderId));
			BizBuyerOrder bizBuyerOrder = bizBuyerOrderService.getById(Long.parseLong(orderId));
			if (Objects.nonNull(bizBuyerOrder)){
				maintenanceService.processMaintenanceOrder(bizBuyerOrder);
			}
			return Action.CommitMessage;
		} catch (Exception e) {
			log.error("消费MQ消息失败，msgId:" + message.getMsgID() + "，ExceptionMsg：" + e.getMessage());
			return Action.ReconsumeLater;
		}
	}
}