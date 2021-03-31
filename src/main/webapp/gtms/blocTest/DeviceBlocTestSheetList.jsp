<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ϸ��Ϣ��ѯ</title>

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
	<caption>Ӧ���ն���Ϣ</caption>
	<thead>
		<tr>
			<th>�豸����</th>
			<th>OUI</th>
			<th>�豸����</th>
			<th>�豸����</th>
			<th>����汾</th>
			<th>Ӳ���汾</th>
			<th>�豸���к�</th>
			<th>ҵ������</th>
			<th>����ʱ��</th>
			<th>���Ӷ˿�</th>
			<th>����״̬</th>
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
				<td colspan=11>û�в�ѯ�������Ϣ!</td>
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