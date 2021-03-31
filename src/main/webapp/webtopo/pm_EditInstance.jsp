<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>

<%
	Pm_DevList pmdev = new Pm_DevList(request);
	int retflag = pmdev.save_Instance(request);
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.isCall="<%=retflag%>";
//-->
</SCRIPT>