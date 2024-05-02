package eu.dnetlib.apps.oai;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.dnetlib.apps.oai.domain.OaiMetadataFormat;
import eu.dnetlib.apps.oai.domain.OaiPage;
import eu.dnetlib.apps.oai.domain.OaiRecord;
import eu.dnetlib.apps.oai.domain.OaiSet;
import eu.dnetlib.apps.oai.utils.DateUtils;
import eu.dnetlib.apps.oai.utils.GzipUtils;
import eu.dnetlib.apps.oai.utils.XsltTransformerFactory;

@Service
public class OaiService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private XsltTransformerFactory xsltTransformerFactory;

	private static final String RES_TOKEN_SEPARATOR = "§";

	public OaiRecord getRecord(final String id, final String metadataPrefix) {
		final String sql = "SELECT * FROM oai_data WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper(metadataPrefix), id);
	}

	public OaiPage listRecords(final String metadataPrefix, final String setSpec, final String from, final String until, final long pageSize) {
		return listRecords(metadataPrefix, setSpec, from, until, 0, pageSize);
	}

	public OaiPage listRecords(final String resumptionToken) {
		final String[] arr = new String(Base64.decodeBase64(resumptionToken)).split(RES_TOKEN_SEPARATOR);
		if (arr.length != 6) { throw new RuntimeException("INVALID RES TOKEN"); }
		final String metadataPrefix = arr[0];
		final String setSpec = arr[1];
		final String from = arr[2];
		final String until = arr[3];
		final long cursor = Long.parseLong(arr[4]);
		final long pageSize = Long.parseLong(arr[5]);

		return listRecords(metadataPrefix, setSpec, from, until, cursor, pageSize);
	}

	private OaiPage listRecords(final String metadataPrefix,
		final String setSpec,
		final String from,
		final String until,
		final long cursor,
		final long pageSize) {

		final LocalDate fromDate = StringUtils.isNotBlank(from) ? DateUtils.parseDate(from) : LocalDate.MIN;
		final LocalDate untilDate = StringUtils.isNotBlank(until) ? DateUtils.parseDate(until).plusDays(1) : LocalDate.MAX;

		final RowMapper<OaiRecord> rowMapper = rowMapper(metadataPrefix);

		final OaiPage page = new OaiPage();

		long total;
		final List<OaiRecord> list;
		if (StringUtils.isNotBlank(setSpec)) {
			final String sql = "SELECT * FROM oai_data WHERE ? = ANY(sets) AND date >= ? AND date <= ? ORDER BY id LIMIT ? OFFSET ?";
			final String sqlTotal = "SELECT count(*) FROM oai_data WHERE ? = ANY(sets) AND date >= ? AND date <= ?";

			list = jdbcTemplate.query(sql, rowMapper, fromDate, setSpec, untilDate, pageSize, cursor);
			total = jdbcTemplate.queryForObject(sqlTotal, Long.class, setSpec, fromDate, untilDate);
		} else {
			final String sql = "SELECT * FROM oai_data WHERE date >= ? AND date <= ? ORDER BY id LIMIT ? OFFSET ?";
			final String sqlTotal = "SELECT count(*) FROM oai_data WHERE date >= ? AND date <= ?";

			list = jdbcTemplate.query(sql, rowMapper, fromDate, untilDate, pageSize, cursor);
			total = jdbcTemplate.queryForObject(sqlTotal, Long.class, fromDate, untilDate);
		}

		page.setList(list);

		if (cursor + pageSize < total) {
			final String nextToken = Base64.encodeBase64URLSafeString(StringUtils
				.join(Arrays.asList(metadataPrefix, setSpec, from, until, Long.toString(cursor + pageSize), Long.toString(pageSize)), RES_TOKEN_SEPARATOR)
				.getBytes());

			page.setCursor(cursor);
			page.setTotal(total);
			page.setResumptionToken(nextToken);
		}

		return page;
	}

	public boolean verifySet(final String set) {
		if (StringUtils.isBlank(set)) { return true; }
		for (final OaiSet s : listSets()) {
			if (set.equals(s.getSetSpec())) { return true; }
		}
		return false;
	}

	public OaiSet[] listSets() {
		try {
			return new ObjectMapper().readerForArrayOf(OaiSet.class).readValue(getClass().getResourceAsStream("/oai-sets.json"));
		} catch (final IOException e) {
			throw new RuntimeException("Error obtaining oai sets");
		}
	}

	public OaiMetadataFormat[] listMetadataFormats(final String id) {
		return listMetadataFormats();
	}

	public OaiMetadataFormat[] listMetadataFormats() {
		try {
			return new ObjectMapper().readerForArrayOf(OaiMetadataFormat.class).readValue(getClass().getResourceAsStream("/oai-metadata-formats.json"));
		} catch (final IOException e) {
			throw new RuntimeException("Error obtaining oai sets");
		}
	}

	private RowMapper<OaiRecord> rowMapper(final String metadataPrefix) {
		final Function<String, String> mapper = prepareXsltMapper(metadataPrefix);
		return (rs, rowNum) -> {
			final String xml = GzipUtils.decompress(rs.getBytes("body"));

			final OaiRecord r = new OaiRecord();
			r.setId(rs.getString("id"));
			r.setBody(mapper != null ? mapper.apply(xml) : xml);
			r.setDate(Instant.ofEpochMilli(rs.getLong("date"))
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime());
			r.setOaiSets((String[]) rs.getArray("sets").getArray());

			return r;
		};
	}

	public String nextResumptionToken(final String metadataPrefix,
		final String set,
		final LocalDateTime from,
		final LocalDateTime until,
		final long nextCursor,
		final long pageSize) {
		final List<String> list = Arrays.asList(metadataPrefix, set, from.toString(), until.toString(), Long.toString(nextCursor), Long.toString(pageSize));

		final String s = StringUtils.join(list, RES_TOKEN_SEPARATOR);

		return Base64.encodeBase64URLSafeString(s.getBytes());
	}

	private Function<String, String> prepareXsltMapper(final String metadataPrefix) {
		return Arrays.stream(listMetadataFormats())
			.filter(f -> f.getMetadataPrefix().equalsIgnoreCase(metadataPrefix))
			.map(OaiMetadataFormat::getXsltPath)
			.map(xsltPath -> this.xsltTransformerFactory.getTransformer(xsltPath, null))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("Invalid metadata format: " + metadataPrefix));
	}

}
