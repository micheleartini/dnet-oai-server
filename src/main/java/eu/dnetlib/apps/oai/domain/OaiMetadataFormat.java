package eu.dnetlib.apps.oai.domain;

import java.io.Serializable;

public class OaiMetadataFormat implements Serializable {

	private static final long serialVersionUID = -3225471556469204065L;

	private String metadataPrefix;

	private String metadataSchema;

	private String metadataNamespace;

	private String xsltPath;

	public String getMetadataPrefix() {
		return this.metadataPrefix;
	}

	public void setMetadataPrefix(final String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getMetadataSchema() {
		return this.metadataSchema;
	}

	public void setMetadataSchema(final String metadataSchema) {
		this.metadataSchema = metadataSchema;
	}

	public String getMetadataNamespace() {
		return this.metadataNamespace;
	}

	public void setMetadataNamespace(final String metadataNamespace) {
		this.metadataNamespace = metadataNamespace;
	}

	public String getXsltPath() {
		return this.xsltPath;
	}

	public void setXslt(final String xsltPath) {
		this.xsltPath = xsltPath;
	}

}
