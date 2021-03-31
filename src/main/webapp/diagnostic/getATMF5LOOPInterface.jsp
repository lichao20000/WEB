<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.resource.*,java.util.*"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
//根据设备 由数据库中查询获得采集点
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_id);
FileSevice fileService = new FileSevice();
String strList = fileService.getATMF5LOOPInterfaceListBox(device_id);
fileService = null;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.setPingInterface){
	var _strList = "<%=strList%>";
	if(_strList == ""){
		alert("获取失败,设备正忙或者发生未知连接错误!");
	} else {
		parent.document.all("divATMF5Interface").innerHTML = _strList;
	}
	
}
//-->
</SCRIPT>