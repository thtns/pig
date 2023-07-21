package com.pig4cloud.pig.common.core.util;

import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;


@UtilityClass
public class DateTimeUitils {

	public static long localDateTimeBetweenDays(LocalDateTime ldt1, LocalDateTime ldt2) {
		Duration duration = Duration.between(ldt1, ldt2);
		return duration.toDays();
	}

	public static long localDateTimeBetweenHours(LocalDateTime ldt1, LocalDateTime ldt2) {
		Duration duration = Duration.between(ldt1, ldt2);
		return duration.toHours();
	}

	public static long localDateTimeBetweenMinutes(LocalDateTime ldt1, LocalDateTime ldt2) {
		Duration duration = Duration.between(ldt1, ldt2);
		return duration.toMinutes();
	}

	public static long localDateTimeBetweenSeconds(LocalDateTime ldt1, LocalDateTime ldt2) {
		Duration duration = Duration.between(ldt1, ldt2);
		return duration.getSeconds();
	}
}
