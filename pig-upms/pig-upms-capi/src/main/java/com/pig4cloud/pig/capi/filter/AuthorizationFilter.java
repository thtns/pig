package com.pig4cloud.pig.capi.filter;


import com.alibaba.fastjson.JSON;
import com.pig4cloud.pig.capi.entity.BizBuyer;
import com.pig4cloud.pig.capi.service.BizBuyerService;
import com.pig4cloud.pig.common.core.constant.enums.capi.RequestStatusEnum;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@@WebFilter(urlPatterns = "/*")表示要拦截的请求，/*表示拦截所有请求
@Slf4j
@Component
//@WebFilter(urlPatterns = "/maintenance")
@RequiredArgsConstructor
public class AuthorizationFilter implements Filter {

	private final BizBuyerService bizBuyerService;

	@Override
	public void init(FilterConfig filterConfig){
		log.info("初始化过滤器!");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		//将形参servletRequest强制转换为HttpServletRequest类型，以便获取请求的URL地址和请求头
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestURI = request.getRequestURI();
		log.info("api getMethod ：{}", requestURI);
		if (requestURI.contains("/maintenance")) {
			String key = request.getHeader("app_key");
			String secret = request.getHeader("secret_key");
			log.info(" AccessKeyId {}, AccessKeySecret {}", key, secret);
			if (StringUtils.isBlank(key) || StringUtils.isBlank(secret)) {
				log.error(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.MISS_REQUIRED_PARAMETERS.getType())));
				response.reset();
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().write(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.MISS_REQUIRED_PARAMETERS.getType())));                    // 发送字符实体内容
				response.setStatus(RequestStatusEnum.MISS_REQUIRED_PARAMETERS.getType());
				return;
			}
			BizBuyer bizBuyer = bizBuyerService.getByAkSk(key, secret);
			if (bizBuyer == null) {
				log.error(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.AKSK_FAIL.getType())));
				response.reset();
				response.setContentType("application/json;charset=utf-8");
				response.getWriter().write(JSON.toJSONString(R.resultEnumType(null, RequestStatusEnum.AKSK_FAIL.getType())));                    // 发送字符实体内容
				response.setStatus(RequestStatusEnum.AKSK_FAIL.getType());
				return;
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
