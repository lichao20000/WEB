<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>零配置更多信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		parent.dyniframesize();
	});
</SCRIPT>

</head>
<body>
<table class="listtable" width="100%" align="center">
	<thead>
		<tr>
		<th align="center">属地</th>
	    <th align="center">流程编号</th>
	    <th align="center">业务账号</th>
		<th align="center">设备序列号</th>
		<th align="center">开始时间</th>
		<th align="center">结束时间</th>
		<th align="center">业务类型</th>
		<th align="center">绑定方式</th>
		<th align="center">状态</th>
		<th align="center" colspan="2">零配置配置结果</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="zerogwInfoList.size>0">
			<s:iterator var="list" value="zerogwInfoList">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="#list.city_name" /></td>
				    <td><s:property value="#list.buss_id" /></td>
				    <td><s:property value="#list.serv_account" /></td>
					<td><s:property value="#list.device_serialnumber" /></td>
					<td><s:property value="#list.start_time" /></td>
					<td><s:property value="#list.fail_time" /></td>
					<td><s:property value="#list.bind_type" /></td>
					<td><s:property value="#list.bind_way" /></td>
					<td><s:property value="#list.fail_reason_id" /></td>
					<td colspan="2"><s:property value="#list.fail_reason_id" />,<s:property value="#list.reason_desc" />(返回值[<s:property value="#list.return_value" />])</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="11" align="right" nowrap="nowrap">
				<lk:pages url="/gtms/stb/resource/gwDeviceQueryStb!querygwZeroDetailPage.action?deviceId=<s:property value='deviceId'/>" styleClass="" showType="" isGoTo="true" changeNum="true"/>
			</td>
		</tr>
	</tfoot>
</table>
</body>
