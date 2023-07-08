package com.pig4cloud.pig.capi.utils.rocketmq.client;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.pig4cloud.pig.capi.utils.rocketmq.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerClient {

	@Autowired
    private MqConfig mqConfig;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(mqConfig.getMqPropertie());
        return producer;
    }
}