<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.*,java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String gw_type = request.getParameter("gw_type");
//根据设备 由数据库中查询获得采集点
//DeviceAct act = new DeviceAct();
//HashMap deviceInfo= act.getDeviceInfo(device_id);
//String gather_id = (String)deviceInfo.get("gather_id");

//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
FileSevice fileService = new FileSevice();
//String strList = fileService.getPingInterfaceListBox(device_id,gather_id,ior);
String strList = fileService.getPingInterface(device_id, gw_type);
fileService = null;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.setPingInterface){
	parent.setPingInterface("<%=strList%>");
}
//-->
</SCRIPT>