<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.webtopo.IpCheck" %>
<%@ page contentType="text/html;charset=GBK"%>

<%
 
request.setCharacterEncoding("GBK");
IpCheck ipconfig = new IpCheck();
int retflag = ipconfig.ipConfig(request);

%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall="<%=retflag%>";
//-->
</SCRIPT>