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
			<th>�豸���к�</th>
			<th>����</th>
			<th>ǩԼ����</th>
			<th>����ƽ��ֵ</th>
			<th>���з�ֵ</th>
			<th>��ʼʱ��</th>
			<th>����ʱ��</th>
			<th>����IP</th>

		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td align="center"><s:property value="httpMap['DevSn']" /></td>
							<td align="center"><s:property value="httpMap['city']" /></td>
							<td align="center"><s:property value="httpMap['speed']" />m</td>
							<td align="center"><s:property value="httpMap['AvgSampledTotalValues']" /></td>
							<td align="center"><s:property value="httpMap['MaxSampledTotalValues']" /></td>
							<td align="center"><s:property value="httpMap['TransportStartTime']" /></td>
							<td align="center"><s:property value="httpMap['TransportEndTime']" /></td>
							<td align="center"><s:property value="httpMap['IP']" /></td>
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