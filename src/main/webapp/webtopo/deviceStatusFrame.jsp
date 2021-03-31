<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<% 
String device_id= request.getParameter("device_id"); 
%>
<html>
<frameset rows="60%,40%" border="0">
	<frame name="searchForm" src="./webtop_DeviceStatus.jsp?title=¶Ë¿ÚÐÅÏ¢&className=ReadDeviceStatus&device_id=<%= device_id%>">
	<frame name="dataForm" src="../bbms/GetSnmpInfo.action?device_id=<%= device_id%>">
</frameset>
</html>
