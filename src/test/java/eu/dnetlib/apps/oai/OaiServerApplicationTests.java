package eu.dnetlib.apps.oai;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import eu.dnetlib.apps.oai.utils.GzipUtils;

@Disabled
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class OaiServerApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeAll
	void setUp() throws Exception {
		final byte[] body = GzipUtils.compress(IOUtils.toString(getClass().getResourceAsStream("record01.xml"), "UTF-8"));
		jdbcTemplate.update("delete from oai_data where id like 'test-oai::%'");
		for (int i = 0; i < 1000; i++) {
			jdbcTemplate.update("insert into oai_data(id, body) values (?,?)", "test-oai::" + i, body);
		}
	}

	@AfterAll
	public void cleanUp() {
		// jdbcTemplate.update("delete from oai_data where id like 'test-oai::%'");
	}

	@Test
	public void contextLoads() {}

}
