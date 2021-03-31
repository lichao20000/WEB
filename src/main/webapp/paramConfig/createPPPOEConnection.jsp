<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import ="com.linkage.litms.paramConfig.WANConnDeviceAct"%>

<%
request.setCharacterEncoding("GBK");
String WANConnection = request.getParameter("wanconnection");
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
boolean flag = WANConnDeviceAct.createPPPConnection(device_id,gather_id,ior,WANConnection);
String strMsg = null;
if(flag){
	strMsg = "创建PPP连接成功.";
}else{
	strMsg = "创建PPP连接失败.";
}
%>
<TABLE border=0 cellspacing=1 cellpadding=2 width="90%" align=center valign=middle bgcolor="#999999">
	<TR>
		<TH align="center">操作提示信息</TH>
	</TR>
		<TR  height="50" bgcolor="#ffffff">
			<TD align=center valign=middle class=column><%=strMsg%></TD>
		</TR>
	<TR bgcolor="#ffffff">
		<TD class=foot align="right">&nbsp;</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(parent.closePMsgDlg != null){
	parent.closePMsgDlg();
}
alert("<%=strMsg%>");

//-->
</SCRIPT>