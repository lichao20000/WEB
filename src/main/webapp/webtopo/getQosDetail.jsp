<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import = "com.linkage.litms.webtopo.QOSConfigInfoAct"%>
<%
request.setCharacterEncoding("GBK");
String str_device_id = request.getParameter("device_id");
String str_qosindex = request.getParameter("qosindex");

String strCmd = "";
QOSConfigInfoAct qosConfig = new QOSConfigInfoAct(request);
// 获得树状结构索引字符串
strCmd = qosConfig.getQOSDetail();


if(strCmd.equals("<br>"))
	strCmd = "获取设备QOS配置指令描述出错，请与管理员联系。";
%>
<script language="JavaScript">
<!--
	alert("<%=strCmd%>");
//-->
</script>