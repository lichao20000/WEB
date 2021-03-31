<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.host.*" %>

<%
	ObjectTypeAct ota = new ObjectTypeAct();
	ota.setRequest(request);
	int retflag = ota.UpdateObjectType();
%>

<SCRIPT LANGUAGE="JavaScript">

	parent.isCall="<%=retflag%>";

</SCRIPT>