package eu.dnetlib.apps.oai.domain;

import org.apache.commons.lang3.StringUtils;

public enum OaiVerb {

	IDENTIFY("Identify"),
	LIST_IDENTIFIERS("ListIdentifiers"),
	LIST_RECORDS("ListRecords"),
	LIST_METADATA_FORMATS("ListMetadataFormats"),
	LIST_SETS("ListSets"),
	GET_RECORD("GetRecord"),
	UNSUPPORTED_VERB("");

	private final String verb;

	public static OaiVerb validate(final String verb) {

		if (StringUtils.isBlank(verb)) { return UNSUPPORTED_VERB; }

		for (final OaiVerb v : OaiVerb.values()) {
			if (v.getVerb().equalsIgnoreCase(verb)) { return v; }
		}

		return UNSUPPORTED_VERB;
	}

	private OaiVerb(final String verb) {
		this.verb = verb;
	}

	public String getVerb() {
		return verb;
	}

}
