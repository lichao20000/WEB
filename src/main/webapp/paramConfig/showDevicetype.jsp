<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	String defaultValue = request.getParameter("defaultValue");
	if (defaultValue == null){
		defaultValue = "";
	}
	
	request.setCharacterEncoding("GBK");
	String strChildList = DeviceAct.getDeviceModelByOUI(request,defaultValue,"device_model_id");
%>
<html>
<body>
<SPAN ID="child"><%=strChildList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_devicetype").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>