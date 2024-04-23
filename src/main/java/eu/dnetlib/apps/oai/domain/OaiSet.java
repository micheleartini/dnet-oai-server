package eu.dnetlib.apps.oai.domain;

import java.io.Serializable;

public class OaiSet implements Serializable {

	private static final long serialVersionUID = 1995486356252936048L;

	private String setSpec;

	private String setName;

	private String description;

	public String getSetSpec() {
		return this.setSpec;
	}

	public void setSetSpec(final String setSpec) {
		this.setSpec = setSpec;
	}

	public String getSetName() {
		return this.setName;
	}

	public void setSetName(final String setName) {
		this.setName = setName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}
