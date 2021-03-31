<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import= "com.linkage.litms.webtopo.MCControlManager"%>

<%
	request.setCharacterEncoding("GBK");
	String customid = request.getParameter("customid");
	String obj_id = request.getParameter("obj_id");
	
	boolean flag = false;
	MCControlManager mc = new MCControlManager(user.getAccount(),user.getPasswd());
	flag=mc.setDevCustomid(obj_id,customid);
%>
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = "<%=flag%>";

if (flag == "true")
{
	parent.isCall = 1;
}
else
{
	parent.isCall = -1;
}
//-->
</SCRIPT>
</head>
<body>

</body>
</html>