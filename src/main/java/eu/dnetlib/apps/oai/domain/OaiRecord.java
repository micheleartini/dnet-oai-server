package eu.dnetlib.apps.oai.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

public class OaiRecord implements Serializable {

	private static final long serialVersionUID = -8383104201424481929L;

	private String id;

	private String body;

	private LocalDateTime date;

	private String[] oaiSets;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public void setDate(final LocalDateTime date) {
		this.date = date;
	}

	public String[] getOaiSets() {
		return this.oaiSets;
	}

	public void setOaiSets(final String[] oaiSets) {
		this.oaiSets = oaiSets;
	}

	public boolean isDeleted() {
		return StringUtils.isBlank(body);
	}
}
