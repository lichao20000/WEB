<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>集团光宽批量重启详情</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<% 
request.setCharacterEncoding("GBK");
%>

<script type="text/javascript">
function ToExcel(){
	var taskId = $("#bind_taskId").val();
	var type = $("#bind_type").val();
	var page="<s:url value='/gwms/resource/batchRestartManagerACT!qryDetailExcel.action'/>?taskId="+taskId
			+"&type="+type
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
<input type="hidden" id="bind_taskId" value="<s:property value="taskId"/>" />
<input type="hidden" id="bind_type" value="<s:property value="type"/>" />
	<table class="listtable" id="listTable">
	<caption>集团光宽批量重启详情</caption>
	<thead>
		<tr>
			<th align="center">设备厂商</th>
			<th align="center">设备型号</th>
			<th align="center">软件版本</th>
			<th align="center">属地</th>
			<th align="center">设备序列号</th>
			<th align="center">LOID</th>
			<th align="center">设备版本类型</th>
			<th align="center">设备更新时间</th>
			<th align="center">重启结果</th>
			<th align="center">失败原因</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskDetailList!=null">
			<s:if test="taskDetailList.size()>0">
				<s:iterator value="taskDetailList">
					<tr>
						<td class=column1 align="center"><s:property value="vendor_name"/></td>
						<td class=column1 align="center"><s:property value="device_model"/></td>
						<td class=column1 align="center"><s:property value="softwareversion"/></td>
						<td class=column1 align="center"><s:property value="city_name"/></td>
						<td class=column1 align="center"><s:property value="devsn"/></td>
						<td class=column1 align="center"><s:property value="LOID"/></td>
						<td class=column1 align="center"><s:property value="device_version_type"/></td>
						<td class=column1 align="center"><s:property value="time"/></td>
						<td class=column1 align="center"><s:property value="restart_res"/></td>
						<td class=column1 align="center"><s:property value="faile_reason"/></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=12>没有相关查询信息信息</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=12>没有相关查询信息</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
			<tr>
				<td colspan="12" align="right"><lk:pages
						url="/gwms/resource/batchRestartManagerACT!qryDetail.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>

		 <tr>
				<td colspan="12" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel()"></td>
			</tr> 
		</tfoot>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>
	</table>
</body>
</html>