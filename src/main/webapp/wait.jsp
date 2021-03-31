<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../timelater.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="refresh" content="1;url=<s:url includeParams="all"/>" />

<title>wait page</title>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
//-->
</script>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=40>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="450" border=0 cellspacing=1 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=1 cellspacing=0 cellpadding=2 width="100%">
					<TR>
						<TH align="center" colspan="2">&nbsp;</TH>
					</TR>
						<TR height="50">
							<TD align=center valign=middle class=column width="20%">
								<img src="<s:url value="/images/wait.gif"/>" width="64" height="64">
							</TD>
							<TD class=column>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"><font color="#33689C" size="2pt"><strong> 正在处理中，请等待......</strong></font>
							</TD>
							
						</TR>
					<TR>
						<TD class=foot align="right" colspan="2">
							<input type="submit" name="Submit"
							value="刷 新" onclick="window.location.href='<s:url includeParams="all"/>'">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
