package eu.dnetlib.apps.oai.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.micrometer.common.util.StringUtils;

public class DateUtilsTest {

	@BeforeEach
	void setUp() throws Exception {}

	@Test
	final void testParseDate() {
		final LocalDate d1 = LocalDate.of(2020, 2, 25);
		final LocalDate d2 = DateUtils.parseDate("2020-02-25");
		assertTrue(d1.isEqual(d2));
	}

	@Test
	final void testCalculate_ISO8601LocalDateTime() {
		final LocalDateTime time = LocalDateTime.of(2007, 2, 25, 12, 30);
		final String s = DateUtils.calculate_ISO8601(time);
		assertEquals("2007-02-25T12:30::00", s);
	}

	@Test
	final void testCalculate_ISO8601Long() {
		final long time = LocalDateTime.of(2007, 2, 25, 12, 30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		final String s = DateUtils.calculate_ISO8601(time);
		assertEquals("2007-02-25T12:30::00", s);
	}

	@Test
	final void testNow() {
		final long t1 = System.currentTimeMillis();
		final long t2 = DateUtils.now();

		assertTrue(t2 >= t1 && t2 - t1 < 1000);
	}

	@Test
	final void testNow_ISO8601() {
		assertTrue(StringUtils.isNotBlank(DateUtils.now_ISO8601()));
	}

}
