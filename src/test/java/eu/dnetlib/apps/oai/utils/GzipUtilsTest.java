package eu.dnetlib.apps.oai.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GzipUtilsTest {

	@BeforeEach
	void setUp() throws Exception {}

	@Test
	final void testCompressAndDecompress() {
		final String message = "Hello world";

		final byte[] bytes = GzipUtils.compress(message);
		assertNotNull(bytes);
		assertTrue(bytes.length > 0);

		assertEquals(message, GzipUtils.decompress(bytes));
	}

	@Test
	final void testCompressNull() {
		assertNull(GzipUtils.compress(null));
	}

	@Test
	final void testDecompressNull() {
		assertNull(GzipUtils.decompress(null));
	}

	@Test
	final void testCompressEmpty() {
		assertNull(GzipUtils.compress(""));
	}

	@Test
	final void testDecompressEmpty() {
		assertNull(GzipUtils.decompress(new byte[] {}));
	}

}
