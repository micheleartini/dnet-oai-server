# dnet-oai-server

Simple implementation of an OAI-PMH server. 

## Requirements

...

## Configuration

### Database
The Metadata Records must be stored in Postgres Database; the database schema can be generated using the following sql file:
<pre>src/main/resources/sql/schema.sql</pre>
Example:
<pre>
> createdb oai_server;
> psql oai_server -f src/main/resources/sql/schema.sql
</pre>

### application.properties
It is the standard Spring Boot Configuration file, available at:
<pre>src/main/resources/application.properties</pre>
You can modify and/or override its values according to your deployement.
Example:
<pre>
spring.application.name=dnet-oai-server

# Description of the oai server
oai.server.baseUrl = http://localhost:8080/oai
oai.server.repositoryName = TEST repository
oai.server.adminEmail = test@test
oai.server.pageSize = 100

# Properties to access the database
spring.datasource.url=jdbc:postgresql://localhost:5432/oai_server_test
spring.datasource.username=
spring.datasource.password=

# Other properties
spring.jpa.hibernate.ddl-auto = validate
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.open-in-view=true
spring.jpa.properties.hibernate.show_sql=false
spring.jpa.properties.hibernate.use_sql_comments=false
spring.jpa.properties.hibernate.format_sql=false 
</pre>

### oai-metadata-formats.json
...

### oai-sets.json
...




