<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("GBK");
	//String strChildList = DeviceAct.getDeviceSoftVersionByOUIAndDeviceModel(request,"","softwareversion");
	String oui = request.getParameter("vendor_id");
	String device_model = request.getParameter("devicetype_id");
	String strChildList = DeviceAct.getDeviceSoftware(oui, device_model);

%>
<html>
<body>
<SPAN ID="child"><%=strChildList%></SPAN>

<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_deviceversion").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>