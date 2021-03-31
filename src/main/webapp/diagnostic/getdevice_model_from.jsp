<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("GBK");
	//String strChildList = DeviceAct.getOnlyDeviceModelByOUI(request,"","devicetype_id");
	String oui = request.getParameter("vendor_id");
	String strChildList = DeviceAct.getDeviceModel(oui);
%>
<html>
<body>
<SPAN ID="child"><%=strChildList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_devicetype").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>