package eu.dnetlib.apps.oai.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class GzipUtils {

	public static byte[] compress(final String str) {
		if (StringUtils.isBlank(str)) { return null; }

		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (final GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
				IOUtils.write(str.getBytes(Charset.defaultCharset()), gzip);
			}
			return baos.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException("error in gzip", e);
		}
	}

	public static String decompress(final byte[] compressed) {
		if (compressed == null || compressed.length == 0) { return null; }
		if (isCompressed(compressed)) {
			try (final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
				return IOUtils.toString(gis, Charset.defaultCharset());
			} catch (final IOException e) {
				throw new RuntimeException("error in gunzip", e);
			}
		} else {
			return new String(compressed);
		}
	}

	public static boolean isCompressed(final byte[] compressed) {
		return compressed[0] == (byte) GZIPInputStream.GZIP_MAGIC && compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
	}
}
