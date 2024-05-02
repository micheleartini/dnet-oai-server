package eu.dnetlib.apps.oai.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResumptionTokenTest {

	@BeforeEach
	void setUp() throws Exception {}

	@Test
	final void testResumptionToken_01() {
		final ResumptionToken rt1 = ResumptionToken.newInstance("oai_dc", null, null, null, 12345);
		final ResumptionToken rt2 = ResumptionToken.fromString(rt1.asString());

		assertEquals(rt1.getMetadataPrefix(), rt2.getMetadataPrefix());
		assertEquals(rt1.getSetSpec(), rt2.getSetSpec());
		assertEquals(rt1.getFrom(), rt2.getFrom());
		assertEquals(rt1.getUntil(), rt2.getUntil());
		assertEquals(rt1.getCursor(), rt2.getCursor());

		assertEquals("oai_dc", rt2.getMetadataPrefix());
		assertEquals(null, rt2.getSetSpec());
		assertEquals(null, rt2.getFrom());
		assertEquals(null, rt2.getUntil());
		assertEquals(12345, rt2.getCursor());
	}

	@Test
	final void testResumptionToken_02() {
		final ResumptionToken rt1 = ResumptionToken.newInstance("oai_dc", "test", null, null, 12345);
		final ResumptionToken rt2 = ResumptionToken.fromString(rt1.asString());

		assertEquals(rt1.getMetadataPrefix(), rt2.getMetadataPrefix());
		assertEquals(rt1.getSetSpec(), rt2.getSetSpec());
		assertEquals(rt1.getFrom(), rt2.getFrom());
		assertEquals(rt1.getUntil(), rt2.getUntil());
		assertEquals(rt1.getCursor(), rt2.getCursor());

		assertEquals("oai_dc", rt2.getMetadataPrefix());
		assertEquals("test", rt2.getSetSpec());
		assertEquals(null, rt2.getFrom());
		assertEquals(null, rt2.getUntil());
		assertEquals(12345, rt2.getCursor());
	}

	@Test
	final void testResumptionToken_03() {
		final ResumptionToken rt1 = ResumptionToken.newInstance("oai_dc", "test", "2012-01-31", null, 12345);
		final ResumptionToken rt2 = ResumptionToken.fromString(rt1.asString());

		assertEquals(rt1.getMetadataPrefix(), rt2.getMetadataPrefix());
		assertEquals(rt1.getSetSpec(), rt2.getSetSpec());
		assertEquals(rt1.getFrom(), rt2.getFrom());
		assertEquals(rt1.getUntil(), rt2.getUntil());
		assertEquals(rt1.getCursor(), rt2.getCursor());

		assertEquals("oai_dc", rt2.getMetadataPrefix());
		assertEquals("test", rt2.getSetSpec());
		assertEquals("2012-01-31", rt2.getFrom());
		assertEquals(null, rt2.getUntil());
		assertEquals(12345, rt2.getCursor());
	}

	@Test
	final void testResumptionToken_04() {
		final ResumptionToken rt1 = ResumptionToken.newInstance("oai_dc", "test", null, "2012-04-15", 12345);
		final ResumptionToken rt2 = ResumptionToken.fromString(rt1.asString());

		assertEquals(rt1.getMetadataPrefix(), rt2.getMetadataPrefix());
		assertEquals(rt1.getSetSpec(), rt2.getSetSpec());
		assertEquals(rt1.getFrom(), rt2.getFrom());
		assertEquals(rt1.getUntil(), rt2.getUntil());
		assertEquals(rt1.getCursor(), rt2.getCursor());

		assertEquals("oai_dc", rt2.getMetadataPrefix());
		assertEquals("test", rt2.getSetSpec());
		assertEquals(null, rt2.getFrom());
		assertEquals("2012-04-15", rt2.getUntil());
		assertEquals(12345, rt2.getCursor());
	}

	@Test
	final void testResumptionToken_05() {
		final ResumptionToken rt1 = ResumptionToken.newInstance("oai_dc", null, "2012-01-31", "2012-04-15", 12345);
		final ResumptionToken rt2 = ResumptionToken.fromString(rt1.asString());

		assertEquals(rt1.getMetadataPrefix(), rt2.getMetadataPrefix());
		assertEquals(rt1.getSetSpec(), rt2.getSetSpec());
		assertEquals(rt1.getFrom(), rt2.getFrom());
		assertEquals(rt1.getUntil(), rt2.getUntil());
		assertEquals(rt1.getCursor(), rt2.getCursor());

		assertEquals("oai_dc", rt2.getMetadataPrefix());
		assertEquals(null, rt2.getSetSpec());
		assertEquals("2012-01-31", rt2.getFrom());
		assertEquals("2012-04-15", rt2.getUntil());
		assertEquals(12345, rt2.getCursor());
	}

	@Test
	final void testResumptionToken_06() {
		final ResumptionToken rt1 = ResumptionToken.newInstance("oai_dc", "test", "2012-01-31", "2012-04-15", 12345);
		final ResumptionToken rt2 = ResumptionToken.fromString(rt1.asString());

		assertEquals(rt1.getMetadataPrefix(), rt2.getMetadataPrefix());
		assertEquals(rt1.getSetSpec(), rt2.getSetSpec());
		assertEquals(rt1.getFrom(), rt2.getFrom());
		assertEquals(rt1.getUntil(), rt2.getUntil());
		assertEquals(rt1.getCursor(), rt2.getCursor());

		assertEquals("oai_dc", rt2.getMetadataPrefix());
		assertEquals("test", rt2.getSetSpec());
		assertEquals("2012-01-31", rt2.getFrom());
		assertEquals("2012-04-15", rt2.getUntil());
		assertEquals(12345, rt2.getCursor());
	}

}
