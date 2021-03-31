<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>for subnetDetail</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<style type="text/css">
</style>
</head>
<body>
<s:push value="subnetDetail">
	<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class=green_gargtd>
		<tr>
			<td width="162" align="center" class="title_bigwhite">子网基本信息</td>
			<td></td>
		</tr>
	</table>
	<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1"  bgcolor=#999999>
		<tr bgcolor="#FFFFFF">
			<td width="20%" align=right>子网掩码：</td>
			<td width="30%" align=left><s:property value="netmask" /></td>
			<td width="20%" align=right>广播地址：</td>
			<td width="30%" align=left><s:property value="highaddr" /></td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td width="20%" align=right>总地址数：</td>
			<td width="30%" align=left><s:property value="totaladdr" /></td>
			<td width="20%" align=right>最低地址：</td>
			<td width="30%" align=left><s:property value="lowaddr" /></td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td width="20%" align=right>最高地址：</td>
			<td width="30%" align=left><s:property value="highaddr" /></td>
			<td width="20%" align=right>子网个数：</td>
			<td width="30%" align=left><s:property value="childcount" /></td>
		</tr>
	</table>
</s:push>
</body>
</html>