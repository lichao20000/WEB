<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����ɼ������ѯ</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function() {
	parent.dyniframesize();
});

function deleteTask(taskId){
	parent.deleteTask(taskId);
}

function detailTask(taskId){
	parent.detailTask(taskId);
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
		<th align="center">����ID</th>
		<th align="center">��������</th>
		<th align="center">����״̬</th>
		<th align="center">��������</th>
		<th align="center">��ִ�д���</th>
		<th align="center">������</th>
		<th align="center">����ʱ��</th>
		<th align="center">����</th>
	</tr>
	<s:iterator value="taskMapList">
		<tr bgcolor="#FFFFFF">
			<td align="center" class="column1"><s:property value="taskId"/></td>
			<td align="center" class="column1"><s:property value="taskType"/></td>
			<td align="center" class="column1"><s:property value="statusName"/></td>
			<td align="center" class="column1"><s:property value="taskDesc"/></td>
			<td align="center" class="column1"><s:property value="executeCount"/></td>
			<td align="center" class="column1"><s:property value="createOperator"/></td>
			<td align="center" class="column1"><s:property value="createTime"/></td>
			<td align="center" class="column1">
				<a href="#" onclick="detailTask('<s:property value="taskId"/>')">����</a>
				<s:if test="status == 0">
					<a href="#" onclick="deleteTask('<s:property value="taskId"/>')">ɾ��</a>
				</s:if>
			</td>
		</tr>
	</s:iterator>
	<tr bgcolor="#FFFFFF" <s:if test="noSplitFlag=='yes'">style="display: none;"</s:if>>
		<td colspan="8" align="right">
			<lk:pages url="/gwms/resource/superGatherLanTask!taskList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/>
		</td>
	</tr>
</table>
</body>