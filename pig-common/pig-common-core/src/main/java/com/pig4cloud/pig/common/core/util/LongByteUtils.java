package com.pig4cloud.pig.common.core.util;

import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;

@UtilityClass
public class LongByteUtils {

	public static byte[] toByteArray(long value) {
		return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(value).array();
	}

	public static long byteArrayToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes, 0, bytes.length);
		buffer.flip();
		return buffer.getLong();
	}

}
