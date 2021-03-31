<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		$("#querying", parent.document).css("display","none");
		$("input[name='queryButton']", parent.document).attr("disabled", false);
		parent.dyniframesize();
	});
</script>
</head>
<body>
	<s:if test='code=="0"'>
		<table class="listtable" id="listTable">
			<thead>
				<tr>
					<th>�豸���к�</th>
					<th>ע�������</th>
					<th>��Ͻ��</th>
					<th>ʧ��ԭ��</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><s:property value="map['DevSn']" /></td>
					<td><s:property value="map['RegisterServer']" /></td>
					<td><s:property value="map['DiagnosticResult']" /></td>
					<td><s:property value="map['DiagnosticReason']" /></td>
				</tr>
			</tbody>

		</table>
	</s:if>
	<s:elseif test='code!="0"'>
		<div align="left" style="background-color: #E1EEEE; height: 20">
			<s:property value="map['RstMsg']" />
		</div>

	</s:elseif>
</body>
</html>