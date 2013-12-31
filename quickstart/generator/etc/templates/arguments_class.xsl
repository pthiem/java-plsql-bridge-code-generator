<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text" omit-xml-declaration = "yes"/>

<xsl:param name="targetProcedureName" />

<xsl:template match="/">

<xsl:for-each select="./PROCEDURES/PROCEDURE[PROCEDURE_NAME=$targetProcedureName]">

// Generated source, do not edit, but you can extend.

package plsql_bridge.<xsl:value-of select="./PACKAGE_NAME_LOWER" />;

import java.util.List;
import java.util.Map;
import java.util.Date;

public class <xsl:value-of select="./PROCEDURE_NAME_FIRSTCAP" />_arguments {

	// Accessors
	<xsl:for-each select="./ARGUMENTS/ARGUMENTS_ROW" >
	
	<xsl:choose>
        
	<xsl:when test="DATA_TYPE='REF CURSOR' or DATA_TYPE='TABLE'">
	
	<xsl:if test="count(./ARGUMENTS/ARGUMENTS_ROW[DATA_TYPE='PL/SQL RECORD' or DATA_TYPE='TABLE']) > 0">
	// Inner class, for use with typed cursor lists
	public static class <xsl:value-of select="ARGUMENT_FIRSTCAP" /> {
	
		// Accessors
		<xsl:for-each select="./ARGUMENTS/ARGUMENTS_ROW/ARGUMENTS/ARGUMENTS_ROW" >
		
		public <xsl:call-template name="ora_type_to_java_type" /><xsl:text> </xsl:text><xsl:value-of select="ARGUMENT_LOWER" />;

		public void set<xsl:value-of select="ARGUMENT_FIRSTCAP" />(<xsl:call-template name="ora_type_to_java_type" /><xsl:text> </xsl:text><xsl:value-of select="ARGUMENT_LOWER" />) {
			this.<xsl:value-of select="ARGUMENT_LOWER" /> = <xsl:value-of select="ARGUMENT_LOWER" />;
		}

		public <xsl:call-template name="ora_type_to_java_type" /> get<xsl:value-of select="ARGUMENT_FIRSTCAP" />() {
			return <xsl:value-of select="ARGUMENT_LOWER" />;
		}
		
		</xsl:for-each>
	}
	</xsl:if>
        
	</xsl:when>

	<xsl:when test="DATA_TYPE='PL/SQL RECORD'">
	
	// Inner class, for use with typed record lists
	public static class <xsl:value-of select="ARGUMENT_FIRSTCAP" /> {
	
		// Accessors
		<xsl:for-each select="./ARGUMENTS/ARGUMENTS_ROW" >
		
		public <xsl:call-template name="ora_type_to_java_type" /><xsl:text> </xsl:text><xsl:value-of select="ARGUMENT_LOWER" />;

		public void set<xsl:value-of select="ARGUMENT_FIRSTCAP" />(<xsl:call-template name="ora_type_to_java_type" /><xsl:text> </xsl:text><xsl:value-of select="ARGUMENT_LOWER" />) {
			this.<xsl:value-of select="ARGUMENT_LOWER" /> = <xsl:value-of select="ARGUMENT_LOWER" />;
		}

		public <xsl:call-template name="ora_type_to_java_type" /> get<xsl:value-of select="ARGUMENT_FIRSTCAP" />() {
			return <xsl:value-of select="ARGUMENT_LOWER" />;
		}
		
		</xsl:for-each>
	}
        
	</xsl:when>

        
	</xsl:choose>
	
	
	public <xsl:call-template name="ora_type_to_java_type" /><xsl:text> </xsl:text><xsl:value-of select="ARGUMENT_LOWER" />;

        public void set<xsl:value-of select="ARGUMENT_FIRSTCAP" />(<xsl:call-template name="ora_type_to_java_type" /><xsl:text> </xsl:text><xsl:value-of select="ARGUMENT_LOWER" />) {
            this.<xsl:value-of select="ARGUMENT_LOWER" /> = <xsl:value-of select="ARGUMENT_LOWER" />;
        }
    
        public <xsl:call-template name="ora_type_to_java_type" /> get<xsl:value-of select="ARGUMENT_FIRSTCAP" />() {
            return <xsl:value-of select="ARGUMENT_LOWER" />;
        }
	
	</xsl:for-each>
	
}

</xsl:for-each>


</xsl:template>

<xsl:template name="ora_type_to_java_type" >
	<xsl:choose>

            <xsl:when test="DATA_TYPE='REF CURSOR'">
            
                <xsl:choose>
            
                    <xsl:when test="count(./ARGUMENTS/ARGUMENTS_ROW[DATA_TYPE='PL/SQL RECORD']) > 0">List &lt; <xsl:value-of select="ARGUMENT_FIRSTCAP" /> &gt; </xsl:when> 
                    <xsl:otherwise>List &lt; Map &gt; </xsl:otherwise>
            
                </xsl:choose>
                
            </xsl:when>
            
            <xsl:when test="DATA_TYPE='NUMBER' and DATA_SCALE!=''">Double</xsl:when>
            <xsl:when test="DATA_TYPE='NUMBER'">Long</xsl:when>
            <xsl:when test="DATA_TYPE='DATE'">Date</xsl:when>
            <xsl:when test="DATA_TYPE='PL/SQL RECORD'"><xsl:value-of select="ARGUMENT_FIRSTCAP" /></xsl:when> 
            <xsl:when test="DATA_TYPE='TABLE'">List &lt; <xsl:value-of select="ARGUMENT_FIRSTCAP" /> &gt; </xsl:when> 
            <xsl:when test="DATA_TYPE='VARCHAR2'">String</xsl:when> 
            <xsl:when test="DATA_TYPE='BLOB'">byte[]</xsl:when> 
            <xsl:when test="DATA_TYPE='CLOB'">String</xsl:when> 
            <xsl:when test="DATA_TYPE='CHAR'">String</xsl:when> 
            <xsl:when test="DATA_TYPE='FLOAT'">Double</xsl:when> 
            <xsl:when test="DATA_TYPE='NCHAR'">String</xsl:when> 
            <xsl:when test="DATA_TYPE='NVARCHAR2'">String</xsl:when> 
            <xsl:when test="DATA_TYPE='NCLOB'">String</xsl:when> 
    
            <xsl:otherwise>Object /* Unsupported Type Error - <xsl:value-of select="DATA_TYPE" /> */ </xsl:otherwise>

	</xsl:choose>
</xsl:template>

</xsl:stylesheet>