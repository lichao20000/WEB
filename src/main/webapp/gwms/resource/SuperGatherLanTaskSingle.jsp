<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>单台设备LAN1采集</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
<form action="">
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<th align="center">属地</th>
		<th align="center">设备序列号</th>
		<th align="center">厂商</th>
		<th align="center">型号</th>
		<th align="center">版本(软件)</th>
		<th align="center">用户账号</th>
		<th align="center">连接速率（LAN1）</th>
		<th align="center">是否千兆属性</th>
		<th align="center">采集时间</th>
		<th align="center">采集结果</th>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td class=column1><s:property value="deviceMap.city_name"/></td>
		<td class=column1><s:property value="deviceMap.device_serialnumber"/></td>
		<td class=column1><s:property value="deviceMap.vendor_add"/></td>
		<td class=column1><s:property value="deviceMap.device_model"/></td>
		<td class=column1><s:property value="deviceMap.softwareversion"/></td>
		<td class=column1><s:property value="deviceMap.username"/></td>
		<td class=column1><s:property value="deviceMap.lan1"/></td>
		<td class=column1><s:property value="deviceMap.gigabit_port"/></td>
		<td class=column1><s:property value="deviceMap.gather_time"/></td>
		<td class=column1><s:property value="deviceMap.status"/></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="10" align="right" class="green_foot" width="100%">
			<input type="button" onclick="javascript:window.close();" class=jianbian name="gwShare_canel" value=" 关 闭 " />
		</td>
	</tr>
</table>
<br>
</form>
</body>