<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCControlManager"%>

<%
	request.setCharacterEncoding("GBK");
	String id = request.getParameter("id");	
	String type = request.getParameter("type");
	int flag = -1;
	MCControlManager mc = new MCControlManager(user.getAccount(),user.getPasswd());
	flag = (int)mc.ModifyDeviceAttr(id, 2, type);
%>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = "<%=flag%>";
if (flag == 0)
{
	parent.isCall = 1;
}
//-->
</SCRIPT>
</head>
<body>

</body>
</html>