<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
	request.setCharacterEncoding("GBK");
	Flux_Config flux_con = new Flux_Config();
	flux_con.setRequest(request);
	int retflag = flux_con.action_delPort();
%>

<SCRIPT LANGUAGE="JavaScript">

	parent.isCall="<%=retflag%>";

</SCRIPT>