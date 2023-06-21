package com.pig4cloud.pig.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lengleng
 * @date 2020/10/4
 * <p>
 * 网关配置文件
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

	/**
	 * 网关解密登录前端密码 秘钥 {@link com.pig4cloud.pig.admin.service.impl.BizBuyerServiceImpl}
	 */
	private String encodeKey;

	/**
	 * 网关不需要校验验证码的客户端 {@link com.pig4cloud.pig.admin.service.impl.BizBuyerServiceImpl}
	 */
	private List<String> ignoreClients;

}
