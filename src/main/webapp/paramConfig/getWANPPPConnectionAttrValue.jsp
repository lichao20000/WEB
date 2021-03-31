<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import ="com.linkage.litms.paramConfig.WANConnDeviceAct"%>
<%@ page import ="java.util.Map"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String WANConnection = request.getParameter("wanconnection");
String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
Map resultMap = WANConnDeviceAct.getParamValueMap(WANConnection,ior,device_id,gather_id);
String ConnectionType = (String)resultMap.get("ConnectionType");
String Name = (String)resultMap.get("Name");
String Username = (String)resultMap.get("Username");
String Enable = (String)resultMap.get("Enable");

ConnectionType = ConnectionType == null ? "" : ConnectionType;
Name = Name == null ? "" : Name;
Username = Username == null ? "" : Username;
Enable = Enable == null ? "1" : Enable;

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.setWANAttrValue){
	parent.setWANAttrValue("<%=ConnectionType%>","<%=Name%>","<%=Username%>","<%=Enable%>");
}
if(parent.closePMsgDlg != null)
	parent.closePMsgDlg();
//-->
</SCRIPT>