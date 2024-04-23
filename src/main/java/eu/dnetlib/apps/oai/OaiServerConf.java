package eu.dnetlib.apps.oai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "oai.server")
public class OaiServerConf {

	private String baseUrl;

	private String repositoryName;

	private String adminEmail;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(final String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(final String adminEmail) {
		this.adminEmail = adminEmail;
	}

}
