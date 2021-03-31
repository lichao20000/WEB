<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%
request.setCharacterEncoding("GBK");
//modify 2005-9-8 by yuht
String strArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
String strAreaName = LipossGlobals.getLipossProperty("InstArea.Name");
String logo_img = "../../../images/logo.gif";
if(strArea!=null){
	if(strArea.indexOf("local") == -1){
		if(strArea.indexOf("dx") != -1)
			logo_img = "../../../images/logo_dx.gif";
		else if(strArea.indexOf("wt") != -1)
			logo_img = "../../../images/logo_wt.gif";
		else if(strArea.indexOf("yd") != -1)
			logo_img = "../../../images/logo_yd.gif";
	}
} 
if(strAreaName == null){
	strAreaName = "联创科技"; 
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style>
TD{
	font-size:12px;
}
A:visited {
	COLOR: white; TEXT-DECORATION: none
}
A:link {
	COLOR: white; TEXT-DECORATION: none
}
A:hover {
	COLOR: #FF9900; TEXT-DECORATION: underline
}
</style>
</head>
<body leftmargin=0 topmargin=0>
<TABLE width=100% border=0 cellpadding=0 cellspacing=0>
  <TR width=100%> 
    <TD rowspan=2><img src="../../../images/report_banner.jpg" ></TD>
	<TD valign=top align=right>
	  <TABLE border=0 cellspacing=0 cellpadding=0 align=right>
		<TR>
		  <TD width='18'><IMG SRC='../../../images/curve.gif' WIDTH=18 HEIGHT=20 BORDER=0></TD>
		  <TD style='background-color:#6386DE' >
			<TABLE id='idLSMenuPanel' border=0 cellspacing=0 cellpadding=0 width=210>
			<TR><TD align=center>
		    <a href="javascript:parent.navigate('../../../index.jsp');">网管主页</a></TD><TD align=center><FONT COLOR='white'>|</FONT></TD><TD align=center><a href="javascript:parent.navigate('../../../login.jsp');">重新登录</a></TD><TD align=center><FONT COLOR='white'>|</FONT></TD><TD align=center><a href="javascript:parent.close();">退出</a></TD></TR></TABLE>
		  </TD>
		</TR>
	  </TABLE>
	</TD>
  </TR>
  <TR><TD align=right><IMG SRC='<%=logo_img%>' alt="<%=strAreaName%>" BORDER=0></TD></TR>
</TABLE>
</body>
</html>

