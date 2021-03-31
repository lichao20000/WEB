<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>带宽对应终端统计</title>

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
		<caption>带宽对应终端统计</caption>
		<thead>
			<tr>
				<th>属地</th>
				<th>区域</th>
				<th>LOID</th>
				<th>设备标识码</th>
				<th>宽带账号</th>
				<th>厂家</th>
				<th>型号</th>
				<th>当前带宽</th>
				<th>是否支持提速</th>
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
						<td colspan="9">未统计到用户信息</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>未统计到用户信息</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6" align="right"><IMG SRC="../../images/excel.gif"
					BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
				</td>
				<td colspan="3" align="right"><lk:pages
						url="/itms/report/bandwidthDeviceReport!goPage.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</body>
</html>