<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="device_wan_wlan" scope="request" class="com.linkage.litms.paramConfig.Device_wan_wlan"/>
<%
	request.setCharacterEncoding("GBK");

	//是否需要用设备ID进行过滤路径
	String needFilter = request.getParameter("needFilter");
	if (null == needFilter) {
		needFilter = "false";
	}
	boolean needFilter_b = Boolean.valueOf(needFilter).booleanValue();
	
	String strList= null;
	strList = device_wan_wlan.getDevice_id(request, needFilter_b);
	
%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_device").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>