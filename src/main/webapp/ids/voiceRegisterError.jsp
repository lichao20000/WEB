<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音仿真</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		$("#querying1",parent.document).attr("style", "display:none");
		$("input[name='queryButton']",parent.document).attr("disabled", false);
		parent.dyniframesize();
	});
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th>错误信息</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><s:property value="map['RstMsg']" /></td>
			</tr>
		</tbody>

	</table>
</body>
</html>