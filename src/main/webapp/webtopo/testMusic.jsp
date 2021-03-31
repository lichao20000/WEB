<%@ page contentType="text/html;charset=gb2312"%>
<%
	request.setCharacterEncoding("GBK");

	String name=request.getParameter("name");

%>
<html>
	<head>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
	</head>
	<body>
		
		<embed src="./sound/<%=name%>" width="0" height="0" hidden loop="false" autostart="true"  id="play2"></embed> 
		
		
	</body>
</html>