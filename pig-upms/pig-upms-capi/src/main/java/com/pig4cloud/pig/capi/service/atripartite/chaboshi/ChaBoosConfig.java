package com.pig4cloud.pig.capi.service.atripartite.chaboshi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties("chaboshi")
public class ChaBoosConfig {

	private String url;

	private String userId;

	private String keySecret;

	private boolean onLine;
}
