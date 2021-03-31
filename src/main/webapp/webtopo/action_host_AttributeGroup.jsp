<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
	AttributeInstanceAct aia = new AttributeInstanceAct();
	aia.setRequest(request);
	int retflag = aia.UpdateAttrGroup();
%>

<SCRIPT LANGUAGE="JavaScript">

	parent.isCall="<%=retflag%>";

</SCRIPT>