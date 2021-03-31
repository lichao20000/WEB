<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String models=request.getParameter("model");

String strSQL = "select distinct os_version from tab_devicetype_info where device_name='"+models+"'";
//out.println(strSQL);
Cursor cursor = DataSetBean.getCursor(strSQL);
String strVersionList = FormUtil.createListBox(cursor, "os_version","os_version",true,"","os_version");

String bt = request.getParameter("bt");
String cmd_id = request.getParameter("cmd_id");
String isarg = request.getParameter("arg");
%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var bt = "<%=bt%>"
var cmd = "<%=cmd_id%>";
var arg = "<%=isarg%>";
parent.idVersion.innerHTML = "<%=strVersionList%>"+ "&nbsp;<FONT COLOR=\"red\">*</FONT>";
if(bt!=""&&bt!="null"){
	parent.RePos(parent.document.frm.os_version,bt);
	if(cmd!="" && cmd!="null")
		this.location = "filterCmd.jsp?model=<%=models%>&version=<%=bt%>&bt=<%=cmd_id%>";
	if(arg=="true")
		this.location = "filterArg.jsp?model=<%=models%>&version=<%=bt%>";
}
//-->
</SCRIPT>