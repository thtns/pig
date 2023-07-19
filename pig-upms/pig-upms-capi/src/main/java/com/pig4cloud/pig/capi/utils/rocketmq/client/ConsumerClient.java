package com.pig4cloud.pig.capi.utils.rocketmq.client;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.pig4cloud.pig.capi.utils.rocketmq.consumer.OderMessageListener;
import com.pig4cloud.pig.capi.utils.rocketmq.consumer.DalyMessageListener;
import com.pig4cloud.pig.capi.utils.rocketmq.MqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//项目中加上 @Configuration 注解，这样服务启动时consumer也启动了
@Configuration
public class ConsumerClient {

    @Autowired
    private MqConfig mqConfig;

    @Autowired
    private OderMessageListener messageListener;

	@Autowired
    private DalyMessageListener dalyMessageListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = mqConfig.getMqPropertie();
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getGroupId());
        //将消费者线程数固定为20个 20为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");
		properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, "5");
        consumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<Subscription, MessageListener>();
        Subscription subscription = new Subscription();
        subscription.setTopic(mqConfig.getTopic());
        subscription.setExpression(mqConfig.getTag());
        subscriptionTable.put(subscription, messageListener);
        //订阅多个topic如上面设置

		// 延迟top
		Subscription dalySubscription = new Subscription();
		dalySubscription.setTopic(mqConfig.getTimeTopic());
		dalySubscription.setExpression(mqConfig.getTimeTag());
		subscriptionTable.put(dalySubscription, dalyMessageListener);

        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;
    }

}