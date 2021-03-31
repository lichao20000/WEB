<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
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
			<caption>测试结果</caption>
			<thead>
				<tr>
					<th>设备序列号</th>
					<th>请求开始时间</th>
					<th>请求结束时间</th>
					<th>采样值平均速率(KBps)</th>
					<th>测速结果值(KBps)</th>
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