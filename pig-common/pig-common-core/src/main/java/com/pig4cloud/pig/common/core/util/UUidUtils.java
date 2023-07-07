package com.pig4cloud.pig.common.core.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUidUtils {

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间-分割.
	 */
	public static String uuidOriginal() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static Long uuLongId(){
		return (long) Math.abs(UUID.randomUUID().hashCode());
	}
}
