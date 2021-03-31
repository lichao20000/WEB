<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.linkage.litms.system.dbimpl.DbUserRes"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>

<%
	request.setCharacterEncoding("GBK");

	String username = (String) request.getParameter("username");
	String strDevice = "";

	DbUserRes objUserRes = (DbUserRes) session.getAttribute("curUser");
	List gather_id = objUserRes.getUserProcesses();
	
	strDevice = GeneralNetPerf.getDeviceByUser(username, gather_id);
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
		parent.document.all("deviceByusername").innerHTML = child.innerHTML;
		</SCRIPT>
	</BODY>
</HTML>
