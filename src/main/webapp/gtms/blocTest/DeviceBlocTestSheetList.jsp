<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>详细信息查询</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});
function showSheetContent(sheetContent) {
	$('#"sheetContent"', window.parent.document).show();
	var url = "<s:url value='/itms/service/sheetContent.jsp'/>?sheetContent="+sheetContent;
	$.post(url, {
	}, function(mesg) {
		$('#sheetContent', window.parent.document).show();
		$('#sheetContent', window.parent.document).html(mesg);
	});
}
</script>

</head>

<body>

<table class="listtable" id="listTable">
	<caption>应用终端信息</caption>
	<thead>
		<tr>
			<th>设备名称</th>
			<th>OUI</th>
			<th>设备厂商</th>
			<th>设备类型</th>
			<th>软件版本</th>
			<th>硬件版本</th>
			<th>设备序列号</th>
			<th>业务类型</th>
			<th>上线时间</th>
			<th>连接端口</th>
			<th>在线状态</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceInfoList!=null">
			<s:if test="deviceInfoList.size()>0">
				<s:iterator value="deviceInfoList">
					<tr>
						<td><s:property value="device_id" /></td>
						<td><s:property value="oui" /></td>
						<td><s:property value="friendly_name" /></td>
						<td><s:property value="device_type" /></td>
						<td><s:property value="software_version" /></td>
						<td><s:property value="hardware_version" /></td>
						<td><s:property value="serial_number" /></td>
						<td><s:property value="product_class" /></td>
						<td><s:property value="up_time" /></td>
						<td><s:property value="attached_port" /></td>
						<td><s:property value="is_online" /></td>
					</tr>
				</s:iterator>
			</s:if>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11>没有查询到相关信息!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>

		</tr>
	</tfoot>
</table>
</body>

</html>