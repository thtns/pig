//package com.pig4cloud.pig.capi.utils.rocketmq;
//
//import com.aliyun.openservices.ons.api.PropertyKeyConst;
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.context.annotation.Configuration;
//import java.util.Properties;
//
//@Data
//@RefreshScope
//@Configuration
//@ConfigurationProperties(prefix = "rocketmq")
//public class MqConfig {
//
//	private String accessKey;
//	private String secretKey;
//	private String nameSrvAddr;
//	private String topic;
//	private String groupId;
//	private String tag;
//	private String timeTopic;
//	private String timeGroupId;
//	private String timeTag;
//
//	private String testTopic;
//	private String testTag;
//
//
//
//	public Properties getMqPropertie() {
//		Properties properties = new Properties();
//		properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
//		properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
//		properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
//		return properties;
//	}
//
//
//}
