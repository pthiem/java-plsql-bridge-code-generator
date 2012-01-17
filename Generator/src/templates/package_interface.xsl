<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" omit-xml-declaration = "yes"/>

<xsl:param name="targetPlatform" />
<xsl:param name="abstract" />

<xsl:template match="/">

// Generated source, do not edit, but you can extend.

package plsql_bridge.<xsl:value-of select="/PROCEDURES/PROCEDURE/PACKAGE_NAME_LOWER" />;

<xsl:choose>
<xsl:when test="$targetPlatform = 'oc4j1013'">
import javax.ejb.Local;
</xsl:when>
<xsl:otherwise>
</xsl:otherwise>
</xsl:choose>

<xsl:choose>
<xsl:when test="$targetPlatform = 'oc4j1013'">
@Local
</xsl:when>
<xsl:otherwise>
</xsl:otherwise>
</xsl:choose>
public interface <xsl:value-of select="/PROCEDURES/PROCEDURE/PACKAGE_NAME_FIRSTCAP" />
{

	<xsl:for-each select="/PROCEDURES/PROCEDURE" >

    public void <xsl:value-of select="PROCEDURE_NAME_LOWER" />(<xsl:value-of select="PROCEDURE_NAME_FIRSTCAP" />_arguments arguments) throws Exception;
 
	</xsl:for-each>
}

</xsl:template>

</xsl:stylesheet>