package com.pig4cloud.pig.capi.utils.rocketmq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.pig4cloud.pig.capi.entity.BizBuyerOrder;
import com.pig4cloud.pig.capi.nacosConf.BaseConfig;
import com.pig4cloud.pig.capi.service.BizBuyerOrderService;
import com.pig4cloud.pig.capi.service.MainCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TestMessageListener implements MessageListener {

	private final BizBuyerOrderService bizBuyerOrderService;

	private final MainCoreService mainCoreService;

	private final BaseConfig baseConfig;

	@Override
	public Action consume(Message message, ConsumeContext context) {
		String msg = new String(message.getBody());
		log.info("解析MQ-Body自定义内容：{}", msg);
		return Action.CommitMessage;
	}

}