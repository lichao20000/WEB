<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.host.*" %>

<%
	ProcessDescAct pda = new ProcessDescAct();
	pda.setRequest(request);
	int retflag = pda.UpdateProcess();
%>

<SCRIPT LANGUAGE="JavaScript">

	parent.isCall="<%=retflag%>";

</SCRIPT>