<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��̨�豸LAN1�ɼ�</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
</head>
<body>
<form action="">
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<th align="center">����</th>
		<th align="center">�豸���к�</th>
		<th align="center">����</th>
		<th align="center">�ͺ�</th>
		<th align="center">�汾(���)</th>
		<th align="center">�û��˺�</th>
		<th align="center">�������ʣ�LAN1��</th>
		<th align="center">�Ƿ�ǧ������</th>
		<th align="center">�ɼ�ʱ��</th>
		<th align="center">�ɼ����</th>
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
			<input type="button" onclick="javascript:window.close();" class=jianbian name="gwShare_canel" value=" �� �� " />
		</td>
	</tr>
</table>
<br>
</form>
</body>