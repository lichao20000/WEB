<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta http-equiv="pragram" content="no-cache">
<title>ҵ�����ô������</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<%
 String deviceSN = request.getParameter("deviceSN");
%>
</head>

<body>

	<table class="querytable" width="100%" height="70%">
		<TR height="10">
			<th>�豸 <%=deviceSN %> �������
			</th>
		</TR>
		
		<TR height="90%">
			<td><s:property value='solutionData'/> </td>
		</TR>
	</table>
</body>