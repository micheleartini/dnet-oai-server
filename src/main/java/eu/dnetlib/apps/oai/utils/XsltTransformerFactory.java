package eu.dnetlib.apps.oai.utils;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XmlProcessingError;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

@Component
public class XsltTransformerFactory {

	public Function<String, String> getTransformerByXSLT(final String xsltText, final Map<String, Object> params) {
		final Processor processor = new Processor(false);

		final List<XmlProcessingError> errorList = new ArrayList<>();

		final XsltCompiler comp = processor.newXsltCompiler();
		comp.setErrorList(errorList);
		params.forEach((k, v) -> comp.setParameter(new QName(k), XdmAtomicValue.makeAtomicValue(v)));

		try {
			final XsltExecutable xslt = comp.compile(new StreamSource(IOUtils.toInputStream(xsltText, StandardCharsets.UTF_8)));
			return xml -> {
				try {
					final XdmNode source = processor
						.newDocumentBuilder()
						.build(new StreamSource(IOUtils.toInputStream(xml, StandardCharsets.UTF_8)));

					final XsltTransformer trans = xslt.load();
					trans.setInitialContextNode(source);

					final StringWriter output = new StringWriter();
					final Serializer out = processor.newSerializer(output);
					out.setOutputProperty(Serializer.Property.METHOD, "xml");
					out.setOutputProperty(Serializer.Property.INDENT, "yes");

					trans.setDestination(out);
					trans.transform();

					return output.toString();
				} catch (final SaxonApiException e) {
					throw new RuntimeException(e);
				}
			};
		} catch (final Throwable e) {
			final StringWriter sw = new StringWriter();
			sw.append("XSLT failure");
			errorList.forEach(err -> sw.append("\n\t[XSLT ERR] " + err.getMessage()));
			throw new RuntimeException(sw.toString(), e);
		}
	}

}
