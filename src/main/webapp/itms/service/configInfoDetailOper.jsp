<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<jsp:useBean id="base64" scope="request"
	class="com.linkage.litms.common.util.Base64" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务配置详细信息</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%
	String result = null;
	try {
		String password = request.getParameter("password");
		if (password == null || password.trim().length() == 0) {
			result = "密码为空";
		} else {
			result = base64.decode(password);
		}
	} catch (Exception e) {
		e.printStackTrace();
		result = "密码解密失败";
	}
%>
</head>
<body>
	<SPAN ID="child"><%=result%></SPAN>
</body>
<script type="text/javascript">
	parent.document.all("td_password").innerHTML = child.innerHTML;
</script>
</html>