<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����ɼ����������б�</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function() {
	//parent.dyniframesize();
});

function ListToExcel(taskId) {
	var page="<s:url value='/gwms/resource/superGatherLanTask!getExcel.action'/>?"
		+ "taskId=" + taskId
	document.all("childFrm").src=page;
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
<table class="listtable">
	<tr>
		<th align="center">����</th>
		<th align="center">�豸���к�</th>
		<th align="center">����</th>
		<th align="center">�ͺ�</th>
		<th align="center">�汾�������</th>
		<th align="center">�û��˺�</th>
		<th align="center">�������ʣ�Lan1��</th>
		<th align="center">�Ƿ�ǧ������</th>
		<th align="center">�ɼ�ʱ��</th>
		<th align="center">�ɼ����</th>
	</tr>
	<s:iterator value="taskDetailMapList">
		<tr bgcolor="#FFFFFF">
			<td align="center" class="column1"><s:property value="city_name"/></td>
			<td align="center" class="column1"><s:property value="device_serialnumber"/></td>
			<td align="center" class="column1"><s:property value="vendor_add"/></td>
			<td align="center" class="column1"><s:property value="device_model"/></td>
			<td align="center" class="column1"><s:property value="softwareversion"/></td>
			<td align="center" class="column1"><s:property value="username"/></td>
			<td align="center" class="column1"><s:property value="lan1"/></td>
			<td align="center" class="column1"><s:property value="gigabit_port"/></td>
			<td align="center" class="column1"><s:property value="update_time"/></td>
			<td align="center" class="column1"><s:property value="status"/></td>
		</tr>
	</s:iterator>
	<tr bgcolor="#FFFFFF" <s:if test="noSplitFlag=='yes'">style="display: none;"</s:if>>
		<td colspan="10" align="right">
			<lk:pages url="/gwms/resource/superGatherLanTask!taskDeatil.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			<img src="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ListToExcel('<s:property value="taskId"/>')">
		</td>
	</tr>
	<tr STYLE="display: none">
		<td colspan="10">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
</body>