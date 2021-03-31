<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<TABLE width="65%" border=0 cellspacing=0 cellpadding=0 align="center">
  <TR>
	<TD bgcolor=#999999>
	  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
		<TR bgcolor="#FFFFFF">
		  <TH>IP地址配置</TH>
		</TR>
		<TR bgcolor="#FFFFFF">
		  <TD class=column><a href="Legal_ipList.jsp">合法SnmpConf的客户IP地址</a></TD>
		</TR>
		<TR bgcolor="#FFFFFF">
		  <TD class=column><a href="Legal_clientList.jsp?type=97">合法97系统IP地址</a></TD>
		</TR>
		<TR bgcolor="#FFFFFF">
		  <TD class=column><a href="Legal_clientList.jsp?type=web">合法WEB端IP地址</a></TD>
		</TR>
	  </TABLE>
	</TD>
  </TR>
</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>