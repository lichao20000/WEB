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
	strMsg = "����PPPOE�������Գɹ�.";
}else{
	strMsg = "����PPPOE��������ʧ��.";
}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.closePMsgDlg != null)
	parent.closePMsgDlg();
alert("<%=strMsg%>");
//-->
</SCRIPT>