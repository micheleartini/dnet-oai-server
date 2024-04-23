package eu.dnetlib.apps.oai.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OaiRecord implements Serializable {

	private static final long serialVersionUID = -8383104201424481929L;

	private String id;

	private String body;

	private LocalDateTime date;

	private String oaiSet;

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

	public String getOaiSet() {
		return this.oaiSet;
	}

	public void setOaiSet(final String oaiSet) {
		this.oaiSet = oaiSet;
	}

	public List<String> getSets() {
		return Arrays.asList(this.oaiSet);
	}

	public boolean isDeleted() {
		return false;
	}
}
