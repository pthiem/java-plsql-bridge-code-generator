<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" omit-xml-declaration = "yes"/>

<xsl:param name="targetPlatform" />
<xsl:param name="abstract" />

<xsl:template match="/">

// Generated source, do not edit, but you can extend.

package plsql_bridge.<xsl:value-of select="/PROCEDURES/PROCEDURE/PACKAGE_NAME_LOWER" />;

// Imports
<xsl:choose>
<xsl:when test="$targetPlatform = 'ejb3-nointerface'">
import javax.ejb.Stateless;
</xsl:when>
<xsl:otherwise>
</xsl:otherwise>
</xsl:choose>

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import plsql_bridge.PlsqlJavaBridgeExecutor;

// Annotations
<xsl:choose>
<xsl:when test="$targetPlatform = 'ejb3-nointerface'">
@Stateless
</xsl:when>
<xsl:otherwise>
</xsl:otherwise>
</xsl:choose>
public <xsl:if test="$abstract='true'">abstract</xsl:if> class <xsl:value-of select="/PROCEDURES/PROCEDURE/PACKAGE_NAME_FIRSTCAP" /><xsl:if test="$abstract='true'">_abstract</xsl:if>
{

    private Log logger = LogFactory.getFactory().getInstance(getClass());

    public <xsl:value-of select="/PROCEDURES/PROCEDURE/PACKAGE_NAME_FIRSTCAP" /><xsl:if test="$abstract='true'">_abstract</xsl:if>() {
    }  

    <xsl:for-each select="/PROCEDURES/PROCEDURE" >

    public void <xsl:value-of select="PROCEDURE_NAME_LOWER" />(<xsl:value-of select="PROCEDURE_NAME_FIRSTCAP" />_arguments arguments) throws Exception {
  
        PlsqlJavaBridgeExecutor.getInstance().execute("<xsl:value-of select="PACKAGE_NAME_LOWER" />","<xsl:value-of select="PROCEDURE_NAME_LOWER" />", arguments);
        
    }
    
    </xsl:for-each>
        
}

</xsl:template>

</xsl:stylesheet>