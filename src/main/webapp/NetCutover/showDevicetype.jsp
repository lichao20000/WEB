<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	String strv_id = request.getParameter("vendor_id");	
	String strSQL = "select distinct devicetype_id, device_model + '(' + softwareversion + ')' as device_model from tab_devicetype_info where 1=1 ";
	if (strv_id != null){
		strSQL += " and vendor_id='" + strv_id + "'";
	}
	Cursor cursor = DataSetBean.getCursor(strSQL);
	String strModelList = FormUtil.createListBox(cursor,"devicetype_id", "device_model", true, "", "");
%>
<html>
<body>
<SPAN ID="child"><%=strModelList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_devicetype").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>