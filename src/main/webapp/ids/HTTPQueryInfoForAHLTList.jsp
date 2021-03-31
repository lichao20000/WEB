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
		$("#tdData",parent.document).show();
		$("#button",parent.document).attr('disabled',false);
		parent.dyniframesize();
	});
	
</script>
</head>
<body>
<s:if test='code=="1"'>
<table class="listtable" id="listTable">
	<caption>测试结果</caption>
	<thead>
		<tr>
			<th>处理状态</th>
			<th>测试账号</th>
			<th>测试IP</th>
			<th>平均下载速率(M)</th>
			<th>用户签约速率(M)</th>
			<th>当前下载速率(M)</th>
			<th>最大速率(M)</th>
			<th>开始时间</th>
			<th>结束时间</th>
		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td align="center"><s:property value="httpMap['diagnosticsState']" /></td>
							<td align="center"><s:property value="httpMap['pppoeName']" /></td>
							<td align="center"><s:property value="httpMap['pppoeIP']" /></td>
							<td align="center"><s:property value="httpMap['Aspeed']" /></td>
							<td align="center"><s:property value="httpMap['Bspeed']" /></td>
							<td align="center"><s:property value="httpMap['Cspeed']" /></td>
							<td align="center"><s:property value="httpMap['maxspeed']" /></td>
							<td align="center"><s:property value="httpMap['starttime']" /></td>
							<td align="center"><s:property value="httpMap['endtime']" /></td>
						</tr>
					
				
	</tbody>
</table>
</s:if>	
<s:elseif test='code!="1"'>
	<div align="left"  style="background-color: #E1EEEE;height: 20">
		<s:property value="httpMap['errMessage']" />
	</div>
</s:elseif>
</body>
</html>