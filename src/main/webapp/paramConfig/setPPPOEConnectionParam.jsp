<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import ="com.linkage.litms.paramConfig.WANConnDeviceAct"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
boolean flag = WANConnDeviceAct.setPPPConnectionParam(request,ior);
String strMsg = null;
if(flag){
	strMsg = "设置PPPOE连接属性成功.";
}else{
	strMsg = "设置PPPOE连接属性失败.";
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.closePMsgDlg != null)
	parent.closePMsgDlg();
alert("<%=strMsg%>");
//-->
</SCRIPT>