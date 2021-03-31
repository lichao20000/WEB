<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%

request.setCharacterEncoding("GBK");

String models=request.getParameter("model");
String strSQL = "select distinct os_version from tab_devicetype_info where device_name='"+models+"'";

out.println(strSQL);
Cursor cursor = DataSetBean.getCursor(strSQL);
String strVersionList = FormUtil.createListBox(cursor, "os_version","os_version",true,"","os_version");
String RePos = request.getParameter("RePos");
%>

<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<SCRIPT LANGUAGE="JavaScript">

<!--

parent.idVersion.innerHTML = "<%=strVersionList%>";

var RePos = "<%=RePos%>";

if(RePos=="true"){

	//parent.RePos(parent.document.frm.os_version,"<%=request.getParameter("version")%>");
	parent.document.frm.os_version.value="<%=request.getParameter("version")%>";
}

//-->

</SCRIPT>