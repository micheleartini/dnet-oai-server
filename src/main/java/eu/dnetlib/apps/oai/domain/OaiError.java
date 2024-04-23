package eu.dnetlib.apps.oai.domain;

public enum OaiError {

	badArgument(
		"The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax."),
	badVerb("Value of the verb argument is not a legal OAI-PMH verb, the verb argument is missing, or the verb argument is repeated."),
	cannotDisseminateFormat(
		"The metadata format identified by the value given for the metadataPrefix argument is not supported by the item or by the repository."),
	idDoesNotExist("The value of the identifier argument is unknown or illegal in this repository."),
	noMetadataFormats("There are no metadata formats available for the specified item."),
	noSetHierarchy("The repository does not support sets."),
	noRecordsMatch("The combination of the values of the from, until, set and metadataPrefix arguments results in an empty list."),
	badResumptionToken("The value of the resumptionToken argument is invalid or expired.");

	private final String message;

	OaiError(final String message) {
		this.message = message;
	}

	public final String getMessage() {
		return message;
	}

}
