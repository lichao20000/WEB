<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.util.*"%>
<%@ page import="com.linkage.litms.resource.GeneralNetPerf"%>

<%
	request.setCharacterEncoding("GBK");

	String city_id = request.getParameter("city_id");
	String ifcontainChild = request.getParameter("ifcontainChild");
	String device_vendor_id = request.getParameter("device_vendor_id");

	String strDeviceType = "";
	Cursor cursor = null;

	cursor = GeneralNetPerf.getDeviceType(device_vendor_id, city_id,
			ifcontainChild);

	strDeviceType = FormUtil.createListBoxForm(cursor, "device_model",
			"device_model", true, "", "", "");
%>
<HTML>
	<HEAD>
		<TITLE>取得表达式相关设备类型</TITLE>
		<META NAME="Generator" CONTENT="EditPlus">
		<META NAME="Author" CONTENT="dolphin">
		<META HTTP-EQUIV="Content-Type"
			CONTENT="text/html; charset=gb_2312-80">
		<META NAME="Description" CONTENT="取得表达式相关设备类型">
	</HEAD>
	<BODY BGCOLOR="#FFFFFF">
		<FORM name=frm>
			<SPAN ID="child"><%=strDeviceType%>
			</SPAN>
		</FORM>
		<SCRIPT LANGUAGE="JavaScript">
		parent.document.all("device_type").innerHTML = child.innerHTML;
		</SCRIPT>
	</BODY>
</HTML>
