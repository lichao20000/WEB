<%@ include file="../timelater.jsp"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerQOSQueueConfig" %>
<%@ page contentType="text/html;charset=gb2312"%>
<%
	request.setCharacterEncoding("GBK");
	ManagerQOSQueueConfig mfc=new ManagerQOSQueueConfig(request);
	mfc.setSerial("");
	mfc.saveDeviceOperator();
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	window.alert("设备流量配置完成!");
	parent.window.close();
//-->
</SCRIPT>