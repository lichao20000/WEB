<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import = "com.linkage.litms.webtopo.QOSConfigInfoAct"%>
<%
request.setCharacterEncoding("GBK");
//String str_device_id = request.getParameter("device_id");
//String title = request.getParameter("ifindex");


QOSConfigInfoAct qosConfig = new QOSConfigInfoAct(request);
//通知后台重新采集设备
qosConfig.I_InformGather();
%>
<script language="JavaScript">
<!--
	alert("已通知后台对此设备的QOS配置信息进行重新采集，请稍后进行查看");
	parent.close();
//-->
</script>
