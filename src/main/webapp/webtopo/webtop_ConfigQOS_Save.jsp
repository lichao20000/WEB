<%@ include file="../timelater.jsp"%>
<%@ page import ="com.linkage.litms.vipms.flux.ManagerQOSConfig" %>
<%@ page contentType="text/html;charset=gb2312"%>
<%
	request.setCharacterEncoding("GBK");
	ManagerQOSConfig mfc=new ManagerQOSConfig(request);
	mfc.setSerial("");
	mfc.saveDeviceOperator();
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
	window.alert("�豸�����������!");
	parent.window.close();
//-->
</SCRIPT>