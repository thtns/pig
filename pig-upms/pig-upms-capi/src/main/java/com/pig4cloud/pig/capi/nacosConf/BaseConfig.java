package com.pig4cloud.pig.capi.nacosConf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "basecfg")
public class BaseConfig {

	/*** 订单驳回间隔*/
	private int timeDaly;

	/*** 订单重试次数*/
	private int orderTryTimes;



}
