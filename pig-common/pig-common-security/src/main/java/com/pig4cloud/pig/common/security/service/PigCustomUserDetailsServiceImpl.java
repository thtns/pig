package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.admin.api.entity.BizBuyer;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Slf4j
@RequiredArgsConstructor
public class PigCustomUserDetailsServiceImpl implements PigUserDetailsService {

	private final RemoteUserService remoteUserService;

	private final CacheManager cacheManager;

	/**
	 * 用户名密码登录
	 * @param username 用户名
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {

		R<BizBuyer> result = remoteUserService.custom(username);
		// 根据 result 构建security 框架需要的 用户对象
		BizBuyer custom = result.getData();
		if (custom == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		// 构造security用户
		return new PigUser(custom.getId(), null, custom.getClientKey(), SecurityConstants.BCRYPT + custom.getClientSecret(), null, true, true,
				true, true, AuthorityUtils.NO_AUTHORITIES);
	}

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @param grantType
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return "custom".equals(clientId);
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE + 1;
	}

}