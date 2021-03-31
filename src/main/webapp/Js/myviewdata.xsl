<?xml version="1.0" encoding="gb2312"?>



<xsl:stylesheet xmlns:xsl="http://www.w3.org/TR/WD-xsl">



<xsl:template match="/">

    <xsl:apply-templates />

</xsl:template>



<xsl:template match="/Output">

  <TABLE CELLPADDING="3" CELLSPACING="1" BORDER="0" WIDTH="100%">

    <TR bgcolor="#FFFFFF">

      <TD>

        <xsl:attribute name="bgcolor"><xsl:value-of select="@bgcolor" /></xsl:attribute>

        <xsl:attribute name="colspan"><xsl:value-of select="@ColCount" /></xsl:attribute>

        <xsl:value-of select="@Title" />

      </TD>

    </TR>

    <xsl:apply-templates select="Columns"/>

    <xsl:apply-templates select="Row"/>

  </TABLE>

</xsl:template>



<xsl:template match="Columns">

    <TR bgcolor="#ccddee">

      <xsl:if test="/Output/@IsSelect[. !value()='true']">

        <TD class="dataHead" width="30">

          <xsl:attribute name="bgcolor"><xsl:value-of select="Column/@bgcolor" /></xsl:attribute>

        </TD>

      </xsl:if>

      <xsl:for-each select="Column"> 

        <TD class="dataHead">

          <xsl:attribute name="bgcolor"><xsl:value-of select="@bgcolor" /></xsl:attribute>

          <xsl:value-of select="@remark" />

        </TD>

      </xsl:for-each>

    </TR>    

</xsl:template>



<xsl:template match="Row">

    <TR>

      <xsl:attribute name="bgcolor"><xsl:value-of select="@bgcolor" /></xsl:attribute>

      <xsl:if test="/Output/@IsSelect[. !value()='true']">

        <TD align="center">

           <INPUT TYPE="radio"  onClick="onSelect()">

             <xsl:attribute name="value"><xsl:value-of select="@primarykey" /></xsl:attribute>

             <xsl:attribute name="name"><xsl:value-of select="/Output/@Name" /></xsl:attribute>

           </INPUT>

           

        </TD>

      </xsl:if>

      <xsl:for-each select="*">

        <TD>

          <xsl:attribute name="bgcolor"><xsl:value-of select="@bgcolor" /></xsl:attribute>

          <xsl:value-of select="@value" />

        </TD>

      </xsl:for-each>

    </TR>

    <xsl:apply-templates />

</xsl:template>



</xsl:stylesheet> 