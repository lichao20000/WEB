<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��è���к�ƥ��״̬��Ϣ</title>
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
		<caption>��è���к�ƥ��״̬��Ϣ</caption>
		<thead>
			<tr>
				<th>LOID</th>
				<th>�豸�ϱ����к�</th>
				<th>���������к�</th>
				<th>У��ʱ��</th>
				<th>ƥ��״̬</th>
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