<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
	/**
	 * e8-c业务查询
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
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
		$("#button",parent.document).attr('disabled',false);
		parent.dyniframesize();
	});
	
</script>
</head>
<body>
<s:if test='code=="0"'>
<table class="listtable" id="listTable">
	<caption>诊断结果</caption>
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>成功数</th>
			<th>失败数</th>
			<th>平均响应时间</th>
			<th>最小响应时间</th>
			<th>最大响应时间</th>
			<th>丢包率(%)</th>
			<th>IP地址或域名</th>
		</tr>
	</thead>
		<tbody>
						<tr>
							<td><s:property value="pingMap['DevSn']" /></td>
							<td><s:property value="pingMap['SuccesNum']" /></td>
							<td><s:property value="pingMap['FailNum']" /></td>
							<td><s:property value="pingMap['AvgResponseTime']" /></td>
							<td><s:property value="pingMap['MinResponseTime']" /></td>
							<td><s:property value="pingMap['MaxResponseTime']" /></td>
							<td><s:property value="pingMap['PacketLossRate']" /></td>
							<td><s:property value="pingMap['IPOrDomainName']" /></td>
						</tr>
	</tbody>
</table>
</s:if>
<s:elseif test='code=="1"'>
	<div align="left"  style="background-color: #E1EEEE;height: 20">
		<s:property value="pingMap['errMessage']" />
	</div>
</s:elseif>
</body>
</html>