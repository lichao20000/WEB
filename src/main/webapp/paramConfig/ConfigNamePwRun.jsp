<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<%@ page import="java.util.Map,java.util.Iterator,com.linkage.litms.resource.FileSevice,java.util.Set,com.linkage.litms.common.util.Encoder,com.linkage.litms.system.dbimpl.LogItem"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"	class="com.linkage.litms.resource.DeviceAct" />

<%
	request.setCharacterEncoding("GBK");

	String path = request.getParameter("path");
	String device_id = request.getParameter("device_id");
	String pppConnectionType = request.getParameter("pppConnectionType");
	String pppLanInterface = request.getParameter("pppLanInterface");
	String pppServiceList = request.getParameter("pppServiceList");
	String pppEnable = request.getParameter("pppEnable");
	String pppUsername = request.getParameter("pppUsername");
	String pppPassword = request.getParameter("pppPassword");
	
	
	HashMap deviceInfo= DeviceAct.getDeviceInfo(device_id);
	String gather_id = (String)deviceInfo.get("gather_id");
	String devicetype_id = (String)deviceInfo.get("devicetype_id");
	String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
	
	String name1 = path +"ConnectionType";
	String name2 = path +"X_CT-COM_LanInterface";
	String name3 = path +"X_CT-COM_ServiceList";
	String name4 = path +"Enable";
	String name5 = path +"Username";
	String name6 = path +"Password";
	String[] names = {name1, name2, name3, name4, name5, name6};
	for (int i =0 ; i< names.length; i++) {
	}
	
	String[] values = {pppConnectionType, pppLanInterface, pppServiceList, pppEnable, pppUsername, pppPassword};
	for (int i =0 ; i< values.length; i++) {
	}
	
	FileSevice fs = new FileSevice();
	int result = fs.setParamValuePPP(names, values, device_id, gather_id,ior);
	if (result == 1) {
		%>
		<SCRIPT LANGUAGE="JavaScript">
		parent.alert("设置成功！");
		</SCRIPT>
		<%
	} else {
		%>
		<SCRIPT LANGUAGE="JavaScript">
		parent.alert("设置失败！");
		</SCRIPT>
		<%
	}

%>

<body>
</body>
</html>
