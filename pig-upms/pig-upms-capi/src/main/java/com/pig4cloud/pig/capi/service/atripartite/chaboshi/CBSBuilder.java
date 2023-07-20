package com.pig4cloud.pig.capi.service.atripartite.chaboshi;


import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class CBSBuilder {
	private final String CBS_TEST = "http://jxmdp.chaboshi.cn";

	private final String CBS_ONLINE = "https://mdp.chaboshi.cn";

	private String userId;

	private String keySecret;

	private String URL = "https://mdp.chaboshi.cn";

	private static CBSBuilder cbsBuilder = null;

	private CBSBuilder(String userid, String keySecret, boolean onLine) {
		this.userId = userid;
		this.keySecret = keySecret;
		if (!onLine)
			this.URL = "http://jxmdp.chaboshi.cn";
	}

	public static synchronized CBSBuilder newCBSBuilder(String userId, String keySecret, boolean onLine) {
		if (userId == null || keySecret == null || userId.isEmpty() || keySecret.isEmpty())
			throw new RuntimeException("构造参数错误！");
		if (cbsBuilder == null || !userId.equals(cbsBuilder.userId))
			cbsBuilder = new CBSBuilder(userId, keySecret, onLine);
		return cbsBuilder;
	}

	public String sendPost(String suffix) throws Exception {
		return sendPost(suffix, null);
	}

	public String sendGet(String suffix) throws Exception {
		return sendGet(suffix, null);
	}

	public String sendPost(String suffix, HashMap<String, Object> params) throws Exception {
		String paramsStr = sign(params);
		return HttpRequest.sendPost(this.URL + suffix, paramsStr);
	}

	public String sendGet(String suffix, HashMap<String, Object> params) throws Exception {
		String paramsStr = sign(params);
		return HttpRequest.sendGet(this.URL + suffix, paramsStr);
	}

	public String getReportUrl(String suffix, HashMap<String, Object> params) {
		try {
			String paramsStr = sign(params);
			return this.URL + suffix + "?" + paramsStr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	String sign(HashMap<String, Object> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("userid").append("=").append(this.userId);
		if (params != null)
			for (String key : params.keySet()) {
				Object value = params.get(key);
				if (value == null || StringUtils.isBlank(String.valueOf(value)))
					continue;
				sb.append("&").append(key).append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
			}
		long timestamp = System.currentTimeMillis();
		String nonce = UUID.randomUUID().toString();
		sb.append("&").append("timestamp").append("=").append(timestamp);
		sb.append("&").append("nonce").append("=").append(nonce);
		String signature = SignUtil.getSignature(this.keySecret, sb.toString());
		sb.append("&").append("signature").append("=").append(signature);
		return sb.toString();
	}
}
