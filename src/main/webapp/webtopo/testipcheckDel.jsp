<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
 
request.setCharacterEncoding("GBK");
IpCheck ipconfig = new IpCheck();
int retflag = ipconfig.ipConfigDel(request);
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall="<%=retflag%>";
//-->
</SCRIPT>