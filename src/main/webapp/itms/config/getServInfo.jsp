<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
function comfirmServ(){
	var serInfo = $("input[@name='radioDeviceId'][@checked]").val();
	if( "" == serInfo || null == serInfo){
		alert("请选择设备！");
		return false;
	}
	
	window.returnValue = serInfo;
	window.close();
}
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form action="">
		  
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<td colspan="5" align="center" class="green_foot" height="20" width="100%">
			查询结果条数为：<s:property value="servList.size()"/>！
		</td>
	</tr>
	<tr>
		<th align="center">
		</th>
		<th align="center">Loid</th>
		<th align="center">业务帐号</th>
		<th align="center">vlan</th>
		<th align="center">上网方式</th>
	</tr>
	
	<s:iterator value="servList">
		<tr bgcolor="#FFFFFF">
			<td class=column1>
				<input type="radio" name="radioDeviceId" value="<s:property value="vlanid"/>" /></td>
			<td class=column1><s:property value="loid"/></td>
			<td class=column1><s:property value="username"/></td>
			<td class=column1><s:property value="vlanid"/></td>
			<td class=column1><s:property value="wan_type_str"/></td>
		</tr>
	</s:iterator>
	
	
	<tr bgcolor="#FFFFFF">
		<td colspan="5" align="right" class="green_foot" width="100%">
			<input type="button" onclick="comfirmServ()" class=jianbian name="gwShare_deviceConfirm" value=" 确 定 " />
			<input type="button" onclick="window.close()" class=jianbian name="gwShare_canel" value=" 取 消 " />
		</td>
	</tr>
</table>
<br>
</form>
</body>