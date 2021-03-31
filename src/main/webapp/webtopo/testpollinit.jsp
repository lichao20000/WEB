<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
 
request.setCharacterEncoding("GBK");
DevTypeConfig devType = new DevTypeConfig();
int retflag = devType.devTypeConfigList(request); 

%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall="<%=retflag%>";
//-->
</SCRIPT>