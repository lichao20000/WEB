<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.*"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");

//根据设备 由数据库中查询获得采集点
//DeviceAct act = new DeviceAct();
//HashMap deviceInfo= act.getDeviceInfo(device_id);
//String gather_id = (String)deviceInfo.get("gather_id");

//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
FileSevice fileService = new FileSevice();
//String strList = fileService.getPingInterfaceListBox(device_id,gather_id,ior);
String strList = fileService.getDslWAN(device_id);
fileService = null;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.setDslWan){
	parent.setDslWan("<%=strList%>");
}
//-->
</SCRIPT>