<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.host.*" %>

<%
	AlertDefAct ada = new AlertDefAct();
	ada.setRequest(request);
	int retflag = ada.UpdateAlert();
%>

<SCRIPT LANGUAGE="JavaScript">

	parent.isCall="<%=retflag%>";

</SCRIPT>