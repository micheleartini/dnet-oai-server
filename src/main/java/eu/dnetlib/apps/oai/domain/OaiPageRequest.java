package eu.dnetlib.apps.oai.domain;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import eu.dnetlib.apps.oai.utils.DateUtils;

public class OaiPageRequest {

	private static final String SEPARATOR = "@@@";

	private String metadataPrefix;
	private String set;
	private LocalDate from;
	private LocalDate until;
	private int pageNumber;
	private int pageSize;

	private OaiPageRequest(final String metadataPrefix, final String set, final LocalDate from, final LocalDate until, final int pageNumber,
		final int pageSize) {
		super();
		this.metadataPrefix = metadataPrefix;
		this.set = set;
		this.from = from;
		this.until = until;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	public static OaiPageRequest prepareRequest(final String metadataPrefix,
		final String set,
		final LocalDate from,
		final LocalDate until,
		final int pageNumber,
		final int pageSize) {
		return new OaiPageRequest(metadataPrefix, set, from, until, pageNumber, pageSize);
	}

	public static OaiPageRequest fromResumptionToken(final String resumptionToken) {

		final String[] arr = StringUtils.split(new String(Base64.decodeBase64(resumptionToken)), SEPARATOR);

		if (arr.length != 6) { throw new RuntimeException("Invalid token"); }
		final String metadataPrefix = arr[0];
		final String set = arr[1];
		final LocalDate from = DateUtils.parseDate(arr[2]);
		final LocalDate until = DateUtils.parseDate(arr[3]);
		final int pageNumber = Integer.parseInt(arr[4]);
		final int pageSize = Integer.parseInt(arr[5]);

		return new OaiPageRequest(metadataPrefix, set, from, until, pageNumber, pageSize);
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(final String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getSet() {
		return set;
	}

	public void setSet(final String set) {
		this.set = set;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(final LocalDate from) {
		this.from = from;
	}

	public LocalDate getUntil() {
		return until;
	}

	public void setUntil(final LocalDate until) {
		this.until = until;
	}

	public String nextResumptionToken() {
		final List<String> list = Arrays.asList(metadataPrefix, set, from.toString(), until.toString(), Integer.toString(pageNumber + 1), Integer
			.toString(pageSize));

		final String s = StringUtils.join(list, SEPARATOR);

		return Base64.encodeBase64URLSafeString(s.getBytes());
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(final int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}
}
