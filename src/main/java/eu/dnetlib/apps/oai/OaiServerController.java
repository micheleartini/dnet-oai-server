package eu.dnetlib.apps.oai;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.dnetlib.apps.oai.domain.OaiError;
import eu.dnetlib.apps.oai.domain.OaiMetadataFormat;
import eu.dnetlib.apps.oai.domain.OaiPage;
import eu.dnetlib.apps.oai.domain.OaiRecord;
import eu.dnetlib.apps.oai.domain.OaiSet;
import eu.dnetlib.apps.oai.domain.OaiVerb;
import eu.dnetlib.apps.oai.domain.ResumptionToken;
import eu.dnetlib.apps.oai.utils.DateUtils;

@Controller
public class OaiServerController {

	private static final String DEFAULT_CONTENT_TYPE = "text/xml;charset=utf-8";

	@Autowired
	private OaiService oaiService;

	@Autowired
	private OaiServerConf oaiConf;

	private static final Log log = LogFactory.getLog(OaiServerController.class);

	@RequestMapping("/oai")
	public void oaiCall(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.setContentType(OaiServerController.DEFAULT_CONTENT_TYPE);

		final Map<String, String> params = cleanParameters(request.getParameterMap());

		try (final OutputStream out = response.getOutputStream()) {
			IOUtils.write(oaiResponse(params), out, StandardCharsets.UTF_8);
		}

	}

	@ExceptionHandler(Throwable.class)
	public void handleError(final HttpServletRequest req, final HttpServletResponse response, final Exception ex) throws IOException {
		log.error("Request: " + req.getRequestURL() + " raised " + ex.getMessage(), ex);

		try (final OutputStream out = response.getOutputStream()) {
			IOUtils.write(prepareErrorResponseXml(OaiError.badArgument), out, StandardCharsets.UTF_8);
		}
	}

	private String oaiResponse(final Map<String, String> params) {
		if (params == null) { return prepareErrorResponseXml(OaiError.badArgument); }

		final String verb = params.remove("verb");

		switch (OaiVerb.validate(verb)) {
		case IDENTIFY:
			return oaiIdentify(params);
		case LIST_METADATA_FORMATS:
			return oaiListMetadataFormats(params);
		case LIST_SETS:
			return oaiListSets(params);
		case GET_RECORD:
			return oaiGetRecord(params);
		case LIST_IDENTIFIERS:
			return oaiListIdentifiers(params);
		case LIST_RECORDS:
			return oaiListRecords(params);
		default:
			return prepareErrorResponseXml(OaiError.badVerb);
		}
	}

	private String oaiIdentify(final Map<String, String> params) {
		if (!params.isEmpty()) { return prepareErrorResponseXml(OaiError.badArgument); }

		final Document doc = genericOaiResponse(OaiVerb.IDENTIFY.getVerb());
		final Element dataNode = doc.getRootElement().addElement(OaiVerb.IDENTIFY.getVerb());

		dataNode.addElement("baseURL").setText(oaiConf.getBaseUrl());
		dataNode.addElement("repositoryName").setText(oaiConf.getRepositoryName());
		dataNode.addElement("protocolVersion").setText("2.0");
		dataNode.addElement("adminEmail").setText(oaiConf.getAdminEmail());
		dataNode.addElement("earliestDatestamp").setText("1900-01-01T00:00:00Z");
		dataNode.addElement("deletedRecord").setText("transient");
		dataNode.addElement("granularity").setText("YYYY-MM-DDThh:mm:ssZ");

		return doc.asXML();
	}

	private String oaiListMetadataFormats(final Map<String, String> params) {
		final String id = params.remove("identifier");
		if (!params.isEmpty()) { return prepareErrorResponseXml(OaiError.badArgument); }

		final Document doc = genericOaiResponse(OaiVerb.LIST_METADATA_FORMATS.getVerb());
		final Element dataNode = doc.getRootElement().addElement(OaiVerb.LIST_METADATA_FORMATS.getVerb());

		final OaiMetadataFormat[] formats =
			StringUtils.isBlank(id) ? this.oaiService.listMetadataFormats(id) : this.oaiService.listMetadataFormats();

		for (final OaiMetadataFormat oaiFormat : formats) {
			final Element formatNode = dataNode.addElement("metadataFormat");
			formatNode.addElement("metadataPrefix").setText(oaiFormat.getMetadataPrefix());
			formatNode.addElement("schema").setText(oaiFormat.getMetadataSchema());
			formatNode.addElement("metadataNamespace").setText(oaiFormat.getMetadataNamespace());
		}
		return doc.asXML();
	}

	private String oaiListSets(final Map<String, String> params) {
		if (!params.isEmpty()) { return prepareErrorResponseXml(OaiError.badArgument); }

		final Document doc = genericOaiResponse(OaiVerb.LIST_SETS.getVerb());
		final Element dataNode = doc.getRootElement().addElement(OaiVerb.LIST_SETS.getVerb());

		for (final OaiSet oaiSet : this.oaiService.listSets()) {
			final Element setNode = dataNode.addElement("set");
			setNode.addElement("setSpec").setText(oaiSet.getSetSpec());
			setNode.addElement("setName").setText(oaiSet.getSetName());
			setNode.addElement("setDescription").setText(oaiSet.getDescription());
		}
		return doc.asXML();

	}

	private String oaiGetRecord(final Map<String, String> params) {
		final String prefix = params.remove("metadataPrefix");
		final String identifier = params.remove("identifier");
		if (!params.isEmpty() || StringUtils.isAnyBlank(prefix, identifier)) { return prepareErrorResponseXml(OaiError.badArgument); }

		final OaiRecord record = this.oaiService.getRecord(identifier, prefix);
		if (record == null) { return prepareErrorResponseXml(OaiError.idDoesNotExist); }

		final Document doc = genericOaiResponse(OaiVerb.GET_RECORD.getVerb());
		final Element dataNode = doc.getRootElement().addElement(OaiVerb.GET_RECORD.getVerb());

		insertSingleRecord(dataNode, record);

		return doc.asXML();
	}

	private String oaiListRecords(final Map<String, String> params) {
		final OaiPage page;
		if (params.containsKey("resumptionToken")) {
			final String resumptionToken = params.remove("resumptionToken");
			if (!params.isEmpty()) { return prepareErrorResponseXml(OaiError.badArgument); }
			page = this.oaiService.listRecords(ResumptionToken.fromString(resumptionToken), oaiConf.getPageSize());
		} else {

			final String metadataPrefix = params.remove("metadataPrefix");
			final String from = params.remove("from");
			final String until = params.remove("until");
			final String set = params.remove("set");

			if (!StringUtils.isNotBlank(metadataPrefix) || !this.oaiService.verifySet(set)) { return prepareErrorResponseXml(OaiError.badArgument); }
			page = this.oaiService.listRecords(metadataPrefix, set, from, until, oaiConf.getPageSize());
		}

		final Document doc = genericOaiResponse(OaiVerb.LIST_RECORDS.getVerb());
		final Element dataNode = doc.getRootElement().addElement(OaiVerb.LIST_RECORDS.getVerb());

		page.getList().forEach(record -> insertSingleRecord(dataNode, record));

		insertResumptionToken(dataNode, page);

		return doc.asXML();
	}

	private void insertSingleRecord(final Element parentNode, final OaiRecord record) {
		final Element recordNode = parentNode.addElement("record");
		insertRecordHeader(recordNode, record);
		if (!record.isDeleted()) {
			try {
				final Document doc2 = DocumentHelper.parseText(record.getBody());
				recordNode.addElement("metadata").add(doc2.getRootElement());
			} catch (final DocumentException e) {
				log.warn("Error parsing record: " + record.getBody());
			}
		}
	}

	private String oaiListIdentifiers(final Map<String, String> params) {

		final OaiPage page;

		if (params.containsKey("resumptionToken")) {
			final String resumptionToken = params.remove("resumptionToken");
			if (!params.isEmpty()) { return prepareErrorResponseXml(OaiError.badArgument); }
			page = this.oaiService.listRecords(ResumptionToken.fromString(resumptionToken), oaiConf.getPageSize());
		} else {
			final String metadataPrefix = params.remove("metadataPrefix");
			final String from = params.remove("from");
			final String until = params.remove("until");
			final String set = params.remove("set");

			if (!StringUtils.isNotBlank(metadataPrefix) || !this.oaiService.verifySet(set)) { return prepareErrorResponseXml(OaiError.badArgument); }

			page = this.oaiService.listRecords(metadataPrefix, set, from, until, oaiConf.getPageSize());
		}

		final Document doc = genericOaiResponse(OaiVerb.LIST_IDENTIFIERS.getVerb());
		final Element dataNode = doc.getRootElement().addElement(OaiVerb.LIST_IDENTIFIERS.getVerb());

		page.getList().forEach(r -> insertRecordHeader(dataNode, r));

		insertResumptionToken(dataNode, page);

		return doc.asXML();
	}

	private void insertRecordHeader(final Element parentNode, final OaiRecord r) {
		final Element headerNode = parentNode.addElement("header");

		headerNode.addElement("identifier").setText(r.getId());
		headerNode.addElement("datestamp").setText(DateUtils.calculate_ISO8601(r.getDate()));
		if (r.getOaiSets() != null) {
			for (final String s : r.getOaiSets()) {
				headerNode.addElement("setSpec").setText(s);
			}
		}

		if (r.isDeleted()) {
			headerNode.addAttribute("status", "deleted");
		}
	}

	private void insertResumptionToken(final Element parentNode, final OaiPage page) {
		if (page.getResumptionToken() != null) {
			final Element tokenNode = parentNode.addElement("resumptionToken");
			tokenNode.addAttribute("completeListSize", Long.toString(page.getTotal()));
			tokenNode.addAttribute("cursor", Long.toString(page.getCursor()));
			tokenNode.setText(page.getResumptionToken().asString());
		}
	}

	private Map<String, String> cleanParameters(final Map<?, ?> startParams) {
		final HashMap<String, String> params = new HashMap<>();
		final Iterator<?> iter = startParams.entrySet().iterator();
		while (iter.hasNext()) {
			final Entry<?, ?> entry = (Entry<?, ?>) iter.next();
			final String key = entry.getKey().toString();
			final String[] arr = (String[]) entry.getValue();
			if (arr.length == 0) { return null; }
			final String value = arr[0];
			if ("verb".equals(key)) {
				params.put("verb", value);
			} else if ("from".equals(key)) {
				params.put("from", value);
			} else if ("until".equals(key)) {
				params.put("until", value);
			} else if ("metadataPrefix".equals(key)) {
				params.put("metadataPrefix", value);
			} else if ("identifier".equals(key)) {
				params.put("identifier", value);
			} else if ("set".equals(key)) {
				params.put("set", value);
			} else if ("resumptionToken".equals(key)) {
				params.put("resumptionToken", value);
			} else {
				return null;
			}
		}
		return params;
	}

	private Document genericOaiResponse(final String verb) {

		final Document doc = DocumentHelper.createDocument();

		final Map<String, String> xsltAttrs = new HashMap<>();
		xsltAttrs.put("type", "text/xsl");
		xsltAttrs.put("href", "oai/oaitohtml.xsl");
		doc.addProcessingInstruction("xml-stylesheet", xsltAttrs);

		final Element root = doc.addElement("OAI-PMH", "http://www.openarchives.org/OAI/2.0/");

		root.addAttribute(new QName("schemaLocation", new Namespace("xsi",
			"http://www.w3.org/2001/XMLSchema-instance")), "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd");

		root.addElement("responseDate").setText(DateUtils.now_ISO8601());

		final Element reqNode = root.addElement("request");
		reqNode.setText(oaiConf.getBaseUrl());
		if (StringUtils.isNotBlank(verb)) {
			reqNode.addAttribute("verb", verb);
		}

		return doc;

	}

	private String prepareErrorResponseXml(final OaiError error) {
		final Document doc = genericOaiResponse(null);
		final Element errorNode = doc.getRootElement().addElement("error");
		errorNode.addAttribute("code", error.name());
		errorNode.setText(error.getMessage());
		return doc.asXML();
	}

}
