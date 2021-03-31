<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.liposs.netcutover.*,
				java.util.*;"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.liposs.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
String message="此ip还未被设备占用";

if(DeviceAct.checkIp(request)!=null)
	message="此ip已被设备:"+DeviceAct.checkIp(request)+" 占用";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> 取得网域里的设备 </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="dolphin">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="取得子串">
</HEAD>

<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><font color="red"><img
			src="../system/images/attention.gif"><%=message%></font></SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("check_ip_info").innerHTML = child.innerHTML;
//-->
</SCRIPT>

</BODY>
</HTML>
