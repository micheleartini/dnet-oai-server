package eu.dnetlib.apps.oai.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;

public class GzipUtils {

	public static byte[] compress(final String str) {
		if (StringUtils.isBlank(str)) { return null; }

		try {
			final ByteArrayOutputStream obj = new ByteArrayOutputStream();
			final GZIPOutputStream gzip = new GZIPOutputStream(obj);
			gzip.write(str.getBytes("UTF-8"));
			gzip.flush();
			gzip.close();
			return obj.toByteArray();
		} catch (final IOException e) {
			throw new RuntimeException("error in gzip", e);
		}
	}

	public static String decompress(final byte[] compressed) {
		final StringBuilder outStr = new StringBuilder();
		if (compressed == null || compressed.length == 0) { return null; }
		try {
			if (isCompressed(compressed)) {
				final GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

				String line;
				while ((line = bufferedReader.readLine()) != null) {
					outStr.append(line);
				}
			} else {
				outStr.append(compressed);
			}
			return outStr.toString();
		} catch (final IOException e) {
			throw new RuntimeException("error in gunzip", e);
		}
	}

	public static boolean isCompressed(final byte[] compressed) {
		return compressed[0] == (byte) GZIPInputStream.GZIP_MAGIC && compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8);
	}
}
