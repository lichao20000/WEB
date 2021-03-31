
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.linkage.litms.resource.*,com.linkage.litms.midware.*,java.util.Map,java.util.HashMap"%>
<%

	request.setCharacterEncoding("gbk");	
	MidWareManager midWareManager = new MidWareManager(request);
	
	String msg = midWareManager.getMidMsg();
	
	
	%>
	<SCRIPT LANGUAGE="JavaScript">
	<!--
	parent.alert("调用结果：\n" + "<%=msg%>");
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