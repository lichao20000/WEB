<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");
String UserName = request.getParameter("UserName");
if(UserName != null){
	UserName = UserName.trim();
}
String device_id = "";
String device_info = "";
String gather_id = "";
Map field = DeviceAct.getDeviceInfoByHgwUserState(request,UserName,"1");
if(field != null){
	device_id = (String)field.get("device_id");
	gather_id = (String)field.get("gather_id");
	device_info = (String)field.get("oui")+"-" + (String)field.get("device_serialnumber");
}

DeviceAct = null;
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.setDevice != null){
	parent.setDevice("<%=device_id%>","<%=gather_id%>","<%=device_info%>");
}
//-->
</SCRIPT>