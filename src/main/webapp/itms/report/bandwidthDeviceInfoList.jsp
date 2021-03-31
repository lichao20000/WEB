<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����Ӧ�ն�ͳ��</title>

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
	$(function() {
		parent.dyniframesize();
	});

	function ToExcel() {
		parent.ToExcel();
	}
</script>
</head>

<body>
	<table class="listtable" id="listTable">
		<caption>�����Ӧ�ն�ͳ��</caption>
		<thead>
			<tr>
				<th>����</th>
				<th>����</th>
				<th>LOID</th>
				<th>�豸��ʶ��</th>
				<th>����˺�</th>
				<th>����</th>
				<th>�ͺ�</th>
				<th>��ǰ����</th>
				<th>�Ƿ�֧������</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="deviceList != null">
				<s:if test="deviceList.size() > 0">
					<s:iterator value="deviceList">
						<tr>
							<td><s:property value="parentCityName" /></td>
							<td><s:property value="cityName" /></td>
							<td><s:property value="loid" /></td>
							<td><s:property value="device_name" /></td>
							<td><s:property value="username" /></td>
							<td><s:property value="vendorName" /></td>
							<td><s:property value="deviceModel" /></td>
							<td><s:property value="down_bandwidth" /></td>
							<td><s:property value="isSpeedUp" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="9">δͳ�Ƶ��û���Ϣ</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>δͳ�Ƶ��û���Ϣ</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6" align="right"><IMG SRC="../../images/excel.gif"
					BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
				</td>
				<td colspan="3" align="right"><lk:pages
						url="/itms/report/bandwidthDeviceReport!goPage.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>