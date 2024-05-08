<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:oaf="http://namespace.openaire.eu/oaf"
	xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	exclude-result-prefixes="oaf" version="2.0">
	<xsl:output indent="yes" method="xml" omit-xml-declaration="yes" />
	<xsl:variable name="openaireNamespace" select="string('oai:dnet:')" />
	
	
	<xsl:template match="/oaf:entity/oaf:result">
		<oai_dc:dc xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">

			<!--
			<dc:title>Studies of Unicorn Behaviour</dc:title>
            <dc:identifier>http://repository.example.org/2003292</dc:identifier>
            <dc:creator>Jane, Doe</dc:creator>
            <dc:creator>John, Doe</dc:creator>
            <dc:description>
                Lorem ipsum dolor...
            </dc:description>
            <dc:subject>info:eu-repo/classification/ddc/590</dc:subject>
            <dc:subject>Unicorns</dc:subject>
            <dc:relation>info:eu-repo/grantAgreement/EC/FP7/1234556789/EU//UNICORN</dc:relation>
            <dc:relation>info:eu-repo/semantics/altIdentifier/eissn/1234-5678</dc:relation>
            <dc:relation>info:eu-repo/semantics/altIdentifier/pmid/123456789</dc:relation>
            <dc:relation>info:eu-repo/semantics/altIdentifier/doi/10.1000/182</dc:relation>
            <dc:relation>info:eu-repo/semantics/reference/doi/10.1234/789.1</dc:relation>
            <dc:relation>info:eu-repo/semantics/dataset/doi/10.1234/789.1</dc:relation>
            <dc:rights>info:eu-repo/semantics/openAccess</dc:rights>
            <dc:rights>http://creativecommons.org/licenses/by-sa/2.0/uk/</dc:rights>
            <dc:source>Journal Of Unicorn Research</dc:source>
            <dc:publisher>Unicorn Press</dc:publisher>
            <dc:date>2013</dc:date>
            <dc:type>info:eu-repo/semantics/article</dc:type>
			-->

			<!-- dc:title -->
			<xsl:for-each select="./title[@classid='main title']">
				<dc:title>
					<xsl:value-of select="normalize-space(.)" />
				</dc:title>
			</xsl:for-each>
			
			<!-- dc:creator -->
			<xsl:for-each select="./creator">
				<xsl:sort select="@rank" />
				<dc:creator>
					<xsl:value-of select="normalize-space(.)" />
				</dc:creator>
			</xsl:for-each>
			
			<!-- dc:subject: free keywords -->
			<xsl:for-each select="./subject[@classname='keyword']">
				<dc:subject>
					<xsl:value-of select="normalize-space(./text())" />
				</dc:subject>
			</xsl:for-each>

			<!-- dc:subject: with classification -->
			<xsl:for-each select="./subject[not(@classname='keyword') and @classid != '']">
				<dc:subject>
					<xsl:value-of select="concat('info:eu-repo/classification/', @classid, '/', normalize-space(./text()))" />
				</dc:subject>
			</xsl:for-each>
			
			<!-- dc:description -->
			<xsl:for-each select="./description">
				<dc:description>
					<xsl:value-of select="normalize-space(.)" />
				</dc:description>
			</xsl:for-each>
				
			<!-- dc:publisher -->
			<xsl:for-each select="./publisher">
				<dc:publisher>
					<xsl:value-of select="normalize-space(.)" />
				</dc:publisher>
			</xsl:for-each>
			
			<!-- dc:contributor -->
			
			<!-- dc:date -->
			<xsl:for-each select="./dateofacceptance">
				<dc:date>
					<xsl:value-of select="normalize-space(.)" />
				</dc:date>
			</xsl:for-each>
			<xsl:for-each select="./embargoenddate">
				<dc:date>
					<xsl:value-of select="concat('info:eu-repo/date/embargoEnd/', normalize-space(.))" />
				</dc:date>
			</xsl:for-each>
			
			<!-- dc:type -->
			
			<!-- dc:format -->
			<xsl:for-each select="./format">
				<dc:format>
					<xsl:value-of select="normalize-space(.)" />
				</dc:format>
			</xsl:for-each>
			
			<!-- dc:identifier -->
			<xsl:for-each select="./children/instance/webresource/url">
				<dc:identifier>
					<xsl:value-of select="normalize-space(.)" />
				</dc:identifier>
			</xsl:for-each>
			
			<!-- dc:source -->
			<xsl:for-each select="./source">
				<dc:source>
					<xsl:value-of select="normalize-space(.)" />
				</dc:source>
			</xsl:for-each>
			
			<!-- dc:language -->
			<xsl:for-each select="./language">
				<dc:language>
					<xsl:value-of select="./@classid" />
				</dc:language>
			</xsl:for-each>
			
			<!-- dc:coverage -->
			
			<!-- dc:relation: projects -->
			<xsl:for-each select=".//rel[./to/@class='isProducedBy']">
				<xsl:for-each select="./funding/funding_level_0">
					<xsl:variable name="funder" select="../funder/@shortname" />
					<xsl:variable name="fundingProgramme" select="@name" />
					<xsl:variable name="jurisdiction" select="../funder/@jurisdiction" />
					<xsl:variable name="title" select="../../title" />
					<xsl:variable name="acronym" select="../../acronym" />
					<xsl:variable name="code" select="../../code" />
					<dc:relation>
						<xsl:value-of
							select="concat('info:eu-repo/grantAgreement/', $funder, '/', $fundingProgramme, '/', $code, '/', $jurisdiction, '/' , $title, '/', $acronym)" />
					</dc:relation>
				</xsl:for-each>
			</xsl:for-each>
			
			<!-- dc:relation: altIdentifiers -->
			
			<!-- dc:relation: reference -->
			
			<!-- dc:relation: dataset -->
			
			<!-- dc:rights -->
			<xsl:for-each select="./bestaccessright">
				<dc:rights>
					<xsl:choose>
						<xsl:when test="@classid='OPEN'">info:eu-repo/semantics/openAccess</xsl:when>
						<xsl:when test="@classid='CLOSED'">info:eu-repo/semantics/closedAccess</xsl:when>
						<xsl:when test="@classid='RESTRICTED'">info:eu-repo/semantics/restrictedAccess</xsl:when>
						<xsl:when test="@classid='EMBARGO'">info:eu-repo/semantics/embargoedAccess</xsl:when>
						<xsl:otherwise>info:eu-repo/semantics/closedAccess</xsl:otherwise>
					</xsl:choose>
				</dc:rights>
			</xsl:for-each>
		</oai_dc:dc>
	</xsl:template>

	
	
	
	
	
	
	
	
	
	
	<xsl:template name="orderedAuthors">
		<xsl:for-each
			select="./rel[./to/@class='hasAuthor']">
			<xsl:sort select="ranking" />
			<dc:creator>
				<xsl:value-of select="./fullname" />
			</dc:creator>
		</xsl:for-each>
	</xsl:template><!-- Titles (M): based on title/@classname <dc:title>main 
		title:subtitle</dc:title> <dc:title>alternative title</dc:title> <dc:title>translated 
		title</dc:title> We have only "main title" for now: let's keep it simple 
		here... -->
	<!-- Contributors (R) not currently available -->
	<xsl:template
		match="rel[./to/@class='hasContributor']">
		<dc:contributor>
			<xsl:value-of select="./fullname" />
		</dc:contributor>
	</xsl:template><!-- projectId (M w A) <dc:relation>info:eu-repo/grantAgreement/Funder/FundingProgram/ProjectID/[Jurisdiction]/[ProjectName]/[ProjectAcronym]</dc:relation> 
		<dc:relation>info:eu-repo/grantAgreement/EC/FP7/12345//The project name/ACRO</dc:relation> 
		<dc:relation>info:eu-repo/grantAgreement/EC/FP7/67891/EU/The 2nd project 
		name/ACRO2</dc:relation> The rel to project: <rel> <to scheme="dnet:result_project_relations" 
		class="isProducedBy" type="project">project_openaireid</to> <code>249516</code> 
		<acronym>VOICE</acronym> <title>xxx</title> <contracttype schemename="ec:FP7contractTypes" 
		classname="aaa" schemeid="ec:FP7contractTypes" classid="aaa"/> <funding> 
		<funding_level_0>FP7</funding_level_0> <funding_level_1>FP7::SP2</funding_level_1> 
		<funding_level_2>FP7::SP2::ERC</funding_level_2> </funding> </rel> The funding 
		program is funding_level_0. The funder must be inferred from the contract 
		type - until the Funders won't be added explicitely in the record. -->
	

	<!-- <xsl:template match="oaf:result/originalId/text()"> 
		<dc:identifier> <xsl:value-of select="normalize-space(.)"/> </dc:identifier> 
		</xsl:template> --><!-- Alternative Identifier (R), idType from pid@classid 
		<dc:relation> info:eu-repo/semantics/altIdentifier/[idType]/[ID] </dc:relation> -->
	<xsl:template match="oaf:pid[./text()]">
		<dc:relation>
			<xsl:value-of
				select="concat('info:eu-repo/semantics/altIdentifier/', @classid, '/', ./text())" />
		</dc:relation>
	</xsl:template>
	<!--Referenced Publication (R), from extraInfo[@typology='citations']/citation, 
		idType from citation/id/@type, ID from citation/id/@value <dc:relation> info:eu-repo/semantics/reference/[idType]/[ID] 
		</dc:relation> <dc:relation> info:eu-repo/semantics/reference/doi/10.1234/789.1 
		</dc:relation> <dc:relation> info:eu-repo/semantics/reference/pmid/1234567 
		</dc:relation> -->
	<xsl:template
		match="extraInfo[@typology='citations']//citation/id[./@type]">
		<xsl:choose>
			<xsl:when test="@type='openaire'">
				<dc:relation>
					<xsl:value-of
						select="concat('info:eu-repo/semantics/reference/', @type, '/', $openaireNamespace, @value)" />
				</dc:relation>
			</xsl:when>
			<xsl:otherwise>
				<dc:relation>
					<xsl:value-of
						select="concat('info:eu-repo/semantics/reference/', @type, '/', @value)" />
				</dc:relation>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template><!-- Dataset reference (R), from: rel[./resulttyype/@classname='dataset'] 
		<dc:relation>info:eu-repo/semantics/dataset/[idType]/[ID]</dc:relation> only 
		available when we'll expand the PIDs in the relationship <dc:relation>info:eu-repo/semantics/dataset/doi/10.1234/789.1</dc:relation> 
		For now we can only use openaire ids, from rel[./resulttyype/@classname='dataset']/to/text() 
		<dc:relation>info:eu-repo/semantics/dataset/openaire/oai:dnet:[OPENAIRE_ID]</dc:relation> -->
	<xsl:template
		match="oaf:result//rel[./resulttype/@classid='dataset']">
		<dc:relation>
			<xsl:value-of
				select="concat('info:eu-repo/semantics/dataset/openaire/', $openaireNamespace, ./to/text())" />
		</dc:relation>
	</xsl:template><!-- Subjects (M w A) <dc:subject>bag of words</dc:subject> 
		If subject@classname != keywords, then we have a classification scheme to 
		encode <dc:subject>info:eu-repo/classification/[scheme]/[value]</dc:subject> 
		<dc:subject>info:eu-repo/classification/dcc/whatever</dc:subject> -->
	<!--description (M w A) from description -->
	<!--publisher (M w A) from publisher -->
	<!-- Publication date (M) from dateofacceptance -->
	<!-- Publication type (M) where [type] is /instancetype[1]/classname 
		(dnet:publication_resource vocabulary). A second dc:type (uncontrolled) can 
		be used. <dc:type>info:eu-repo/semantics/[type]</dc:type> -->
	<xsl:template
		match="oaf:result/children/instance/instancetype">
		<xsl:variable name="theInstanceType">
			<xsl:choose>
				<xsl:when test="@classname='Unknown'">
					<xsl:value-of select="string('other')" />
				</xsl:when>
				<xsl:when test="@classname=''">
					<xsl:value-of select="string('other')" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="ConvertWordsToCamelCase">
						<xsl:with-param name="text">
							<xsl:value-of select="lower-case(./@classname)" />
						</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<dc:type>
			<xsl:value-of
				select="concat('info:eu-repo/semantics/',$theInstanceType)" />
		</dc:type>
	</xsl:template><!-- <xsl:template match="oaf:result/resulttype"> <dc:type> 
		<xsl:value-of select="./@classname"/> </dc:type> </xsl:template> --><!-- Format (R), in theory from format, not sure we are filling 
		it <dc:format>mediaType of the digital manifestation of the resource</dc:format> 
		<dc:format>application/pdf</dc:format> -->
	
	<xsl:template match="oaf:result/collectedfrom">
		<dc:source>
			<xsl:value-of select="normalize-space(@name)" />
		</dc:source>
	</xsl:template><!-- Language (R) from language@classid -->
	<!-- Override default template -->
	<xsl:template match="text()|@*" /><!-- =========================================================================== --><!-- === Convert camel case 
		text to cameCaseText === --><!-- === Modified from http://blog.inventic.eu/2013/08/xslt-snippet-to-convert-string-from-hyphens-to-camelcase/ 
		=== --><!-- === (c) Inventic s.r.o. 
		ORM Designer team (http://www.orm-designer.com) === --><!-- =========================================================================== -->
	<xsl:template name="ConvertWordsToCamelCase">
		<xsl:param name="text" />
		<xsl:param name="firstLower" select="true()" />
		<xsl:variable name="Upper">
			ABCDEFGHIJKLMNOPQRSTUVQXYZ
		</xsl:variable>
		<xsl:variable name="Lower">
			abcdefghijklmnopqrstuvwxyz
		</xsl:variable>
		<xsl:for-each select="tokenize($text,' ')">
			<xsl:choose>
				<xsl:when test="position()=1 and $firstLower = true()">
					<xsl:value-of select="substring(.,1,1)" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of
						select="translate(substring(.,1,1),$Lower,$Upper)" />
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="substring(.,2,string-length(.))" />
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>