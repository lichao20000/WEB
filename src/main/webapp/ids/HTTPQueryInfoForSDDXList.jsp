<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.closeMessageInfo();
		$("#tdData",parent.document).show();
		$("#button",parent.document).attr('disabled',false);
		parent.dyniframesize();
	});
	
</script>
</head>
<body>
<s:if test='code=="0"'>
<table class="listtable" id="listTable">
	<caption>���Խ��</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>����</th>
			<th>�ͺ�</th>
			<th>����汾</th>
			<th>LOID</th>
			<th>ҵ���˺�</th>
			<th>����</th>

		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td align="center"><s:property value="httpMap['city_name']" /></td>
							<td align="center"><s:property value="httpMap['vendor_name']" /></td>
							<td align="center"><s:property value="httpMap['device_model']" /></td>
							<td align="center"><s:property value="httpMap['softwareversion']" /></td>
							<td align="center"><s:property value="httpMap['loid']" /></td>
							<td align="center"><s:property value="httpMap['username']" /></td>
							<td align="center"><s:property value="httpMap['speed']" /></td>
						</tr>
					
				
	</tbody>
</table>
</s:if>	
<s:elseif test='code=="1"'>
	<div align="left"  style="background-color: #E1EEEE;height: 20">
		<s:property value="httpMap['errMessage']" />
	</div>
</s:elseif>
</body>
</html>