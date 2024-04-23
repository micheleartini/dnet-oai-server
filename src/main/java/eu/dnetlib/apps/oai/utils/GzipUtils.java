package eu.dnetlib.apps.oai.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class GzipUtils {

	public static byte[] compress(final String data) {
		if (StringUtils.isBlank(data)) { return null; }

		try (final ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
			final GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
			gzip.write(data.getBytes());
			gzip.flush();
			return bos.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException("error in gzip", e);
		}
	}

	public static String decompress(final byte[] bytes) {
		if (bytes == null || bytes.length == 0) { return null; }
		try (final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			final GZIPInputStream gis = new GZIPInputStream(bis)) {
			return IOUtils.toString(gis, "UTF-8");
		} catch (final IOException e) {
			throw new RuntimeException("error in gunzip", e);
		}
	}

}
