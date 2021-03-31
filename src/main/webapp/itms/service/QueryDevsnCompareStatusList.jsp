<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>光猫序列号匹配状态信息</title>
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
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>光猫序列号匹配状态信息</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>设备上报序列号</th>
				<th>工单绑定序列号</th>
				<th>校验时间</th>
				<th>匹配状态</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><s:property value="loid" /></td>
				<td><s:property value="devsnfrominform" /></td>
				<td><s:property value="devsnfromeserver" /></td>
				<td><s:property value="comparetime" /></td>
				<td><s:property value="comeparestatus" /></td>
			</tr>
		</tbody>
	</table>
</body>
</html>