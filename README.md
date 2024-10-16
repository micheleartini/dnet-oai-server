# dnet-oai-server

The application permits to export metadata records according to the OAI protocol.


## Requirements

* Java 8 (suggested 11)
* PostgresDB (suggested 12)
* Maven (only for development )

## Configuration

### Database
An external application must populate a Postgres table inserting/updating/deleting the Metadata Records.

You can create the required table (**oai_data**) using the following sql file:
<pre>src/main/resources/sql/schema.sql</pre>
Example:
<pre>
> psql oai_server_test -f src/main/resources/sql/schema.sql
</pre>

### application.properties
The standard Spring Boot Configuration file, available at:
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
This file contains the list of the Metadata Formats that are managed by the application.

Example:
<pre>
[
        {
                "metadataPrefix"   : "oai_dc",
                "metadataSchema"   : "http://www.openarchives.org/OAI/2.0/oai_dc.xsd",
                "metadataNamespace": "http://www.openarchives.org/OAI/2.0/oai_dc/",
                "xsltPath"         : "/xslt/oaf_to_dc.xslt"
        },
        {
                "metadataPrefix"   : "oaf",
                "metadataSchema"   : "https://www.openaire.eu/schema/1.0/oaf-1.0.xsd",
                "metadataNamespace": "http://namespace.openaire.eu/oaf",
                "xsltPath"         : "/xslt/identity.xslt"
        }
]
</pre>
You can store records in a native format, when the application must serve an OAI request
the records are converted in the requested format using an XSLT tranformation. 

### oai-sets.json
This file contains the list of the OAI sets that are managed by the application.

Example:
<pre>
[
        {
                "setSpec"   : "setO1",
                "setName"   : "First Set",
                "description": "This is the description of set01"
        },
        {
                "setSpec"   : "setO2",
                "setName"   : "Second Set",
                "description": "This is the description of set02"
        },
        {
                "setSpec"   : "setO3",
                "setName"   : "Third Set",
                "description": "This is the description of set03"
        }
]
</pre>
The default confguration file contains an empty list.

A record can be associated to more sets populating the **sets** columnn of the **oai_data** table. 

### Execution

In a development environment you can launch the application using:
<pre>mvn spring-boot:run</pre>
For deployment in a production environment, prease refer to the SprinBoot Official Documentation.
