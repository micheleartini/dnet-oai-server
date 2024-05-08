<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:oaf="http://namespace.openaire.eu/oaf"
	xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	exclude-result-prefixes="xsl oaf" version="2.0">

	<xsl:output indent="yes" method="xml"
		omit-xml-declaration="yes" />

	<xsl:template match="/">

		<xsl:for-each select="./oaf:entity/oaf:result[1]">

			<oai_dc:dc
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd">

				<!-- dc:title -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:title')" />
					<xsl:with-param name="nodes" select="./title[@classid='main title']" />
				</xsl:call-template>

				<!-- dc:creator -->
				<xsl:call-template name="sorted_dcterm">
					<xsl:with-param name="term" select="string('dc:creator')" />
					<xsl:with-param name="nodes" select="./creator" />
				</xsl:call-template>

				<!-- dc:subject: free keywords -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:subject')" />
					<xsl:with-param name="nodes" select="./subject[@classname='keyword']" />
				</xsl:call-template>

				<!-- dc:subject: with classification -->				
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:subject')" />
					<xsl:with-param name="nodes" select="./subject[not(@classname='keyword') and @classid != '']/concat('info:eu-repo/classification/', @classid, '/', normalize-space(.))" />
				</xsl:call-template>
				

				<!-- dc:description -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:description')" />
					<xsl:with-param name="nodes" select="./description" />
				</xsl:call-template>

				<!-- dc:publisher -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:publisher')" />
					<xsl:with-param name="nodes" select="./publisher" />
				</xsl:call-template>

				<!-- dc:contributor -->
				<!-- NB: no records matched !!! -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:contributor')" />
					<xsl:with-param name="nodes" select=".//rel[./to/@class='hasContributor']/fullname" />
				</xsl:call-template>

				<!-- dc:date -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:date')" />
					<xsl:with-param name="nodes" select="./dateofacceptance" />
				</xsl:call-template>
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:date')" />
					<xsl:with-param name="nodes" select="./embargoenddate/concat('info:eu-repo/date/embargoEnd/', normalize-space(.))" />
				</xsl:call-template>

				<!-- dc:type -->
				<!-- TODO: convert in info:eu-repo/semantics/XXX -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:type')" />
					<xsl:with-param name="nodes" select="./children/instance/instancetype/@classname" />
				</xsl:call-template>

				<!-- dc:format -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:format')" />
					<xsl:with-param name="nodes" select="./format" />
				</xsl:call-template>

				<!-- dc:identifier -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:identifier')" />
					<xsl:with-param name="nodes" select="./children/instance/webresource/url" />
				</xsl:call-template>

				<!-- dc:source -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:source')" />
					<xsl:with-param name="nodes" select="./source|./collectedfrom/@name" />
				</xsl:call-template>

				<!-- dc:language -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:language')" />
					<xsl:with-param name="nodes" select="./language/@classid" />
				</xsl:call-template>

				<!-- dc:coverage -->
				<!-- NOT USED -->

				<!-- dc:relation: projects -->
				<xsl:for-each
					select=".//rel[./to/@class='isProducedBy']/funding/funding_level_0">
					<xsl:variable name="funder"
						select="normalize-space(../funder/@shortname)" />
					<xsl:variable name="fundingProgramme"
						select="normalize-space(@name)" />
					<xsl:variable name="jurisdiction"
						select="normalize-space(../funder/@jurisdiction)" />
					<xsl:variable name="title"
						select="normalize-space(../../title)" />
					<xsl:variable name="acronym"
						select="normalize-space(../../acronym)" />
					<xsl:variable name="code"
						select="normalize-space(../../code)" />
					<dc:relation>
						<xsl:value-of
							select="concat('info:eu-repo/grantAgreement/', $funder, '/', $fundingProgramme, '/', $code, '/', $jurisdiction, '/' , $title, '/', $acronym)" />
					</dc:relation>
				</xsl:for-each>

				<!-- dc:relation: altIdentifiers -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:relation')" />
					<xsl:with-param name="nodes" select="./pid/concat('info:eu-repo/semantics/altIdentifier/',  normalize-space(@classid), '/', normalize-space(.))" />
				</xsl:call-template>

				<!-- dc:relation: reference -->
				<!-- NOT VERIFIED -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:relation')" />
					<xsl:with-param name="nodes" select="../extraInfo[@typology='citations']//citation/id[@type and @type = 'openaire']/concat('info:eu-repo/semantics/reference/openaire/oai:dnet:', @value)" />
				</xsl:call-template>
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:relation')" />
					<xsl:with-param name="nodes" select="../extraInfo[@typology='citations']//citation/id[@type and @type != 'openaire']/concat('info:eu-repo/semantics/reference/', @type, '/', @value)" />
				</xsl:call-template>

				<!-- dc:relation: dataset -->
				<xsl:call-template name="distinct_dcterm">
					<xsl:with-param name="term" select="string('dc:relation')" />
					<xsl:with-param name="nodes" select=".//rel[./resulttype/@classid='dataset']/to/concat('info:eu-repo/semantics/dataset/openaire/', normalize-space(.))" />
				</xsl:call-template>

				<!-- dc:rights -->
				<xsl:for-each select="./bestaccessright">
					<dc:rights>
						<xsl:choose>
							<xsl:when test="@classid='OPEN'">
								info:eu-repo/semantics/openAccess
							</xsl:when>
							<xsl:when test="@classid='CLOSED'">
								info:eu-repo/semantics/closedAccess
							</xsl:when>
							<xsl:when test="@classid='RESTRICTED'">
								info:eu-repo/semantics/restrictedAccess
							</xsl:when>
							<xsl:when test="@classid='EMBARGO'">
								info:eu-repo/semantics/embargoedAccess
							</xsl:when>
							<xsl:otherwise>
								info:eu-repo/semantics/closedAccess
							</xsl:otherwise>
						</xsl:choose>
					</dc:rights>
				</xsl:for-each>
			</oai_dc:dc>
		</xsl:for-each>
		
	</xsl:template>

	<xsl:template name="distinct_dcterm">
		<xsl:param name="term" />
		<xsl:param name="nodes" />
		
		<xsl:for-each select="distinct-values($nodes)">
			<xsl:element name="{$term}">
				<xsl:value-of select="normalize-space(.)" />
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="sorted_dcterm">
		<xsl:param name="term" />
		<xsl:param name="nodes" />
		
		<xsl:for-each select="$nodes">
			<xsl:sort select="@rank" />		
			<xsl:element name="{$term}">
				<xsl:value-of select="normalize-space(.)" />
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
