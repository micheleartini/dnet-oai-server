package eu.dnetlib.apps.oai;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.dnetlib.apps.oai.domain.OaiMetadataFormat;
import eu.dnetlib.apps.oai.domain.OaiPage;
import eu.dnetlib.apps.oai.domain.OaiRecord;
import eu.dnetlib.apps.oai.domain.OaiSet;

@Service
public class OaiService {

	public boolean verifySet(final String set) {
		// TODO Auto-generated method stub
		return false;
	}

	public OaiPage listRecords(final String resumptionToken) {
		// TODO Auto-generated method stub
		return null;
	}

	public OaiPage listRecords(final String metadataPrefix, final String set, final String from, final String until) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OaiMetadataFormat> listMetadataFormats(final String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OaiMetadataFormat> listMetadataFormats() {
		// TODO Auto-generated method stub
		return null;
	}

	public OaiRecord getRecord(final String identifier, final String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	public OaiSet[] listSets() {
		// TODO Auto-generated method stub
		return null;
	}

}
