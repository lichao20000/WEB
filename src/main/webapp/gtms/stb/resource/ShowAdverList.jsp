<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>开机广告下发结果查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	$(function() {
		parent.document.selectForm.queryButton.disabled = false;
		parent.dyniframesize();
	})
	
	function getTaskDetail(taskId) {
		var page = "<s:url value='/gtms/stb/resource/showAdver!getShowAdverDetail.action'/>?taskId="+taskId;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	};

</SCRIPT>

</head>
<body>
	<table width="98%" class="listtable" align="center">
		<thead>
			<tr>
				<th align="center" width="8.3%">属地</th>
				<th align="center" width="8.3%">厂商</th>
				<th align="center" width="8.3%">设备型号</th>
				<th align="center" width="8.3%">软件版本</th>
				<th align="center" width="12.3%">设备序列号</th>
				<th align="center" width="8.3%">MAC</th>
				<th align="center" width="8.3%">业务账号</th>
				<th align="center" width="8.3%">设备IP</th>
				<th align="center" width="8.3%">配置结果</th>
				<th align="center" width="12.6%">配置时间</th>
				<th align="center" width="8.3%">执行策略名称</th>
			</tr>
		</thead>
		<s:if test="tasklist!=null">
			<s:if test="tasklist.size()>0">
				<tbody>
					<s:iterator value="tasklist">
						<tr>
							<td align="center"><s:property value="cityName" /></td>
							<td align="center"><s:property value="vendorName" /></td>
							<td align="center"><s:property value="deviceModel" /></td>
							<td align="center"><s:property value="deviceTypeName" /></td>
							<td align="center"><s:property value="deviceSerialNumber" /></td>
							<td align="center"><s:property value="cpe_mac" /></td>
							<td align="center"><s:property value="servAccount" /></td>
							<td align="center"><s:property value="loopback_ip" /></td>
							<td align="center"><s:property value="result" /></td>
							<td align="center"><s:property value="update_time" /></td>
							<td align="center"><a href="javascript:void(0);"
								onclick="getTaskDetail('<s:property value="task_id" />')"><s:property
										value="task_name" /></a></td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot>
					<tr bgcolor="#FFFFFF">
						<td colspan="11" align="right"><lk:pages
								url="/gtms/stb/resource/showAdver!adverResultList.action"
								styleClass="" showType="" isGoTo="true" changeNum="false" /></td>
					</tr>
				</tfoot>
			</s:if>
			<s:else>
				<tbody>
					<TR>
						<TD colspan="11"><font color="red">没有定制的开机广告下发结果</font></TD>
					</TR>
				</tbody>
			</s:else>
		</s:if>
	</table>
</body>