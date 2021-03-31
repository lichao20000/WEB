<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.system.dbimpl.DbUserRes"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>

<%
	request.setCharacterEncoding("GBK");

	String city_id = (String) request.getParameter("city_id");
	String ifcontainChild = (String) request
			.getParameter("ifcontainChild");
	String device_model = (String) request.getParameter("device_model");
	String strDevice = "";

	DbUserRes objUserRes = (DbUserRes) session.getAttribute("curUser");
	List gather_id = objUserRes.getUserProcesses();

	//Cursor cursor = GeneralNetPerf.getDeviceNameByType(device_model,city_id, ifcontainChild, gather_id);
	//strDevice = FormUtil.createListBox1(cursor, "loopback_ip","device_name", true, "loopback_ip", "dev_id", true);
	strDevice = GeneralNetPerf.getDeviceNameByType(device_model,city_id, ifcontainChild, gather_id);
%>
<HTML>
	<HEAD>
		<TITLE>取得表达式相关设备</TITLE>
		<META NAME="Generator" CONTENT="EditPlus">
		<META NAME="Author" CONTENT="dolphin">
		<META HTTP-EQUIV="Content-Type"
			CONTENT="text/html; charset=gb_2312-80">
		<META NAME="Description" CONTENT="取得表达式相关设备">
	</HEAD>
	<BODY BGCOLOR="#FFFFFF">
		<FORM name=frm>
			<SPAN ID="child"><%=strDevice%>
			</SPAN>
		</FORM>
		<SCRIPT LANGUAGE="JavaScript">
		parent.document.all("device_ip").innerHTML = child.innerHTML;
		</SCRIPT>
	</BODY>
</HTML>
