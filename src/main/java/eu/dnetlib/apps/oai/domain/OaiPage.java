package eu.dnetlib.apps.oai.domain;

import java.io.Serializable;
import java.util.List;

public class OaiPage implements Serializable {

	private static final long serialVersionUID = -7512951692582271344L;

	private List<OaiRecord> list;

	private long total;

	private long cursor;

	private String resumptionToken;

	public List<OaiRecord> getList() {
		return list;
	}

	public void setList(final List<OaiRecord> list) {
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(final long total) {
		this.total = total;
	}

	public long getCursor() {
		return cursor;
	}

	public void setCursor(final long cursor) {
		this.cursor = cursor;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	public void setResumptionToken(final String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

}
