<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>上行测速结果</title>
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
	<caption>测试结果</caption>
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>传输开始时间</th>
			<th>传输结束时间</th>
			<th>接收字节数</th>
			<th>TCP请求时间</th>
			<th>TCP响应时间</th>
			<th>上行速率(M/s)</th>
		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td><s:property value="httpMap['DevSn']" /></td>
							<td><s:property value="httpMap['BOMTime']" /></td>
							<td><s:property value="httpMap['EOMTime']" /></td>
							<td><s:property value="httpMap['TotalBytesSent']" /></td>
							<td><s:property value="httpMap['TCPOpenRequestTime']" /></td>
							<td><s:property value="httpMap['TCPOpenResponseTime']" /></td>
							<td><s:property value="httpMap['USpeed']" /></td>
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