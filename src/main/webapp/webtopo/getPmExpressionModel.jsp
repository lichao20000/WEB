<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.common.*" %>
<%
	Pm_DevList pmDev = new Pm_DevList(request);
	Cursor cursor = pmDev.getExpressionInfo();
	String strChildList = FormUtil.createListBox(cursor,"expressionid","name",true,"","expression");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<BODY BGCOLOR="#FFFFFF">
<FORM name=frm >
<SPAN ID="child"><%=strChildList%></SPAN>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("expressionList").innerHTML = child.innerHTML;
//-->
</SCRIPT>

</BODY>
</HTML>