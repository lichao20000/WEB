<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.liposs.netcutover.*,
				java.util.*;"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.liposs.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
String message="��ip��δ���豸ռ��";

if(DeviceAct.checkIp(request)!=null)
	message="��ip�ѱ��豸:"+DeviceAct.checkIp(request)+" ռ��";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> ȡ����������豸 </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="dolphin">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312-80">
<META NAME="Description" CONTENT="ȡ���Ӵ�">
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
