<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.DeviceActUtil"%>
<html>
<% 
	DeviceActUtil actUtil = new DeviceActUtil();
//	String oui = request.getParameter("oui");
	String vendorId = request.getParameter("vendor_id");
	String device_mode_id = request.getParameter("device_mode_id");
	String strHTML = actUtil.getDeviceModelList(false,device_mode_id,"device_model",vendorId);
%>
<body>
	<%= strHTML%>
</body>
</html>