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
		$("#tdData", parent.document).show();
		$("#button", parent.document).attr('disabled', false);
		parent.document.frm.button.disabled = false;
		parent.dyniframesize();
	});
</script>
</head>
<body>
	<s:if test='httpMap.message =="none" '>
		<table class="listtable" id="listTable">
			<caption>���Խ��</caption>
			<thead>
				<tr>
					<th>�豸���к�</th>
					<th>����ʼʱ��</th>
					<th>�������ʱ��</th>
					<th>����ֵƽ������(KBps)</th>
					<th>���ٽ��ֵ(KBps)</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td align="center"><s:property
							value="httpMap['deviceSerialnumber']" /></td>
					<td align="center"><s:property
							value="httpMap['TransportStartTime']" /></td>
					<td align="center"><s:property
							value="httpMap['TransportEndTime']" /></td>
					<td align="center"><s:property
							value="httpMap['SampledValues']" /></td>
					<td align="center"><s:property
							value="httpMap['SampledTotalValues']" /></td>
				</tr>

			</tbody>
		</table>
	</s:if>
	<s:else>
		<div align="left" style="background-color: #E1EEEE; height: 20">
			<s:property value="httpMap['message']" />
		</div>
	</s:else>
</body>
</html>