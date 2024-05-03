package eu.dnetlib.apps.oai.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class XsltTransformerFactoryTest {

	private XsltTransformerFactory factory;

	@BeforeEach
	void setUp() throws Exception {
		factory = new XsltTransformerFactory();
	}

	@Test
	final void testTransformer() throws DocumentException {
		final Function<String, String> transformer = factory.getTransformer("/xslt/identity.xslt", null);

		final String res = transformer.apply("<a><b>hello</b><c test='1' /></a>");

		final Document doc = DocumentHelper.parseText(res);

		assertEquals(2, doc.selectNodes("/a/*").size());
		assertEquals("hello", doc.valueOf("/a/b"));
		assertEquals("1", doc.valueOf("/a/c/@test"));
	}

}
