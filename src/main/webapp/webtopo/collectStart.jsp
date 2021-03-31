<%@ include file="../timelater.jsp"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>

<%
	 Pm_DevList pmd = new Pm_DevList(request);
	 int retflag = pmd.CollectStart();
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall="<%=retflag%>";
//-->
</SCRIPT>