package com.pig4cloud.pig.admin.api.request;

import lombok.Data;

@Data
public class ListBuyerRequest {

	private long size = 10;

	/**
	 * 当前页
	 */
	private long current = 1;






}
