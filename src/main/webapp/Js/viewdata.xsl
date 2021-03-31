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

    <xsl:if test="@hasMore[. !value()='true']">

        <TR bgcolor="#FFFFFF">

          <TD align="right">

            <xsl:attribute name="bgcolor"><xsl:value-of select="@bgcolor" /></xsl:attribute>

            <xsl:attribute name="colspan"><xsl:value-of select="@ColCount" /></xsl:attribute>

            <a href="javascript:doSubmit();">下一页</a>

          </TD>

        </TR>

    </xsl:if>

  </TABLE>

</xsl:template>



<xsl:template match="Columns">

    <TR bgcolor="#ccddee">

      <xsl:if test="/Output/@IsSelect[. !value()='true']">

        <TD class="dataHead">

          <xsl:attribute name="bgcolor"><xsl:value-of select="Column/@bgcolor" /></xsl:attribute>

        </TD>

      </xsl:if>

      <xsl:for-each select="Column[$not$@display $or$ @display='']"> 

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

           <INPUT TYPE="radio">

             <xsl:attribute name="value"><xsl:value-of select="@primarykey" /></xsl:attribute>

             <xsl:attribute name="name"><xsl:value-of select="/Output/@Name" /></xsl:attribute>

           </INPUT>          

        </TD>

      </xsl:if>

      <xsl:for-each select="*[$not$@display $or$ @display='']">

        <TD>

          <xsl:attribute name="bgcolor"><xsl:value-of select="@bgcolor" /></xsl:attribute>

          <xsl:choose>

           <xsl:when test="@datatype[. !value()='TextImg']">

             <xsl:attribute name="align">center</xsl:attribute>

             <xsl:attribute name="class">TextImg</xsl:attribute>

             <B><FONT color="green"><xsl:value-of select="@value" /></FONT></B>

           </xsl:when>

           <xsl:when test="@datatype[. !value()='Img']">

             <xsl:attribute name="align">center</xsl:attribute>

             <IMG border="0"><xsl:attribute name="src">images/<xsl:value-of select="@value" /></xsl:attribute></IMG>

           </xsl:when>

           <xsl:otherwise>

             <xsl:value-of select="@value" />

           </xsl:otherwise>

         </xsl:choose>

        </TD>

      </xsl:for-each>

    </TR>

    <xsl:apply-templates />

</xsl:template>



</xsl:stylesheet> 