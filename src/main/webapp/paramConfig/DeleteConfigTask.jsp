<%@ page contentType="text/html;charset=gbk"%>
<%@page import="com.linkage.litms.paramConfig.BatchCongigDbAct"%>
<html>
<head>
<title>еДжц╫А╧Ш</title>
<%
	String task_id = request.getParameter("task_id");
	if(task_id != null && !"".equals(task_id)){
		BatchCongigDbAct.deleteConfigTask(Long.valueOf(task_id));
	}
	
%>
</head>
<script language="javascript">
<!--
	//window.history.go(-1);
	window.location = "BatchNTPConfig.jsp";
//-->
</script>
<body>

</body>
</html>