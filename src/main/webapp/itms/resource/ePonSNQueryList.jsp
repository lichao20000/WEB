<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>���к�����</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

</head>
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
	<tr bgcolor="#FFFFFF">
		<th>�豸���к�</th>
		<th>����</th>
		<th>��ϵ��</th>
		<th>��ϵ��ַ</th>
		<th>����</th>
	</tr>
	<s:iterator value="rsList">
		<tr bgcolor="#FFFFFF">
			<td class=column><s:property value="username" /></td>
			<td class=column><s:property value="cityName" /></td>
			<td class=column><s:property value="linkman" /></td>
			<td class=column><s:property value="linkaddress" /></td>
			<td class=column align="center">
				<a href="http://192.168.1.1/itms?username=telecomadmin&passwd=nE7jA%5m&sn=<s:property value="username"/>" target="_BLANK">
					�� ��
				</a>
			</td>
		</tr>
	</s:iterator>
</table>
</html>