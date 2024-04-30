package eu.dnetlib.apps.oai.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateUtils {

	public static LocalDate parseDate(final String s) {
		return LocalDate.parse(s, DateTimeFormatter.ISO_DATE);
	}

	public static String calculate_ISO8601(final LocalDateTime time) {
		final String result = time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return result.substring(0, result.length() - 2) + ":" + result.substring(result.length() - 2);
	}

	public static String calculate_ISO8601(final long l) {
		return calculate_ISO8601(LocalDateTime.ofInstant(Instant.ofEpochMilli(l), TimeZone
			.getDefault()
			.toZoneId()));
	}

	public static long now() {
		return Instant.now().toEpochMilli();
	}

	public static String now_ISO8601() {
		return calculate_ISO8601(now());
	}

}
