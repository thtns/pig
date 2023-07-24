package com.pig4cloud.pig.common.core.util.mq;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "rocketmq")
public class MqConfig {

	private String accessKey;
	private String secretKey;
	private String nameSrvAddr;
	private String topic;
	private String groupId;
	private String tag;
	private String rejectTopic;//驳回通道
	private String rejectTag; // 驳回标签
	private String timeTopic;
	private String timeGroupId;
	private String timeTag;

	private String testTopic;
	private String testTag;



	public Properties getMqPropertie() {
		Properties properties = new Properties();
		properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
		properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
		properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
		return properties;
	}


}
