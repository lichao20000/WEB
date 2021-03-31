
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.resource.*,com.linkage.litms.midware.*,java.util.Map,java.util.HashMap"%>
<%
	request.setCharacterEncoding("gbk");
	String area_id = request.getParameter("area_id");
	String login = request.getParameter("login");
	String password = request.getParameter("password");
	String status = request.getParameter("status");
	String permit = request.getParameter("permit");

	MidWareManager midWareManager = new MidWareManager(request);
	String msg = midWareManager.getUserInfo();
	%>
	<SCRIPT LANGUAGE="JavaScript">
	<!--
	parent.alert('·µ»ØÐÅÏ¢£º' + '<%=msg%>');
	//-->
	</SCRIPT>
	<%


%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//-->
</SCRIPT>