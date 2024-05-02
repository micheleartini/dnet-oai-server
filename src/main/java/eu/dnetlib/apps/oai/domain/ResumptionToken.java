package eu.dnetlib.apps.oai.domain;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class ResumptionToken {

	private static final String RES_TOKEN_SEPARATOR = "§";

	private String metadataPrefix;
	private String setSpec;
	private String from;
	private String until;
	private long cursor;

	public static ResumptionToken fromString(final String resumptionToken) {
		final String[] arr = new String(Base64.decodeBase64(resumptionToken)).split(RES_TOKEN_SEPARATOR);
		if (arr.length != 5) { throw new RuntimeException("INVALID RES TOKEN"); }
		return new ResumptionToken(
			valueString(arr[0]),
			valueString(arr[1]),
			valueString(arr[2]),
			valueString(arr[3]),
			valueLong(arr[4]));
	}

	public static ResumptionToken newInstance(final String metadataPrefix, final String setSpec, final String from, final String until, final long cursor) {
		return new ResumptionToken(valueString(metadataPrefix), valueString(setSpec), valueString(from), valueString(until), valueLong(cursor));
	}

	private ResumptionToken(final String metadataPrefix, final String setSpec, final String from, final String until, final long cursor) {
		this.metadataPrefix = metadataPrefix;
		this.setSpec = setSpec;
		this.from = from;
		this.until = until;
		this.cursor = cursor;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(final String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getSetSpec() {
		return setSpec;
	}

	public void setSetSpec(final String setSpec) {
		this.setSpec = setSpec;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(final String from) {
		this.from = from;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(final String until) {
		this.until = until;
	}

	public long getCursor() {
		return cursor;
	}

	public void setCursor(final long cursor) {
		this.cursor = cursor;
	}

	public String asString() {
		final List<String> list = Arrays.asList(metadataPrefix, setSpec, from, until, Long.toString(cursor));
		final String s = StringUtils.join(list, RES_TOKEN_SEPARATOR);
		return Base64.encodeBase64URLSafeString(s.getBytes());
	}

	private static String valueString(final String value) {
		return StringUtils.isNotBlank(value) ? value : null;
	}

	private static long valueLong(final String value) {
		return StringUtils.isNotBlank(value) ? Long.parseLong(value) : 0;
	}

	private static long valueLong(final Long value) {
		return value != null ? value : 0;
	}
}
