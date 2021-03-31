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
<s:if test='code=="0"'>
<table class="listtable" id="listTable">
	<caption>测试结果</caption>
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>地市</th>
			<th>签约带宽</th>
			<th>下行平均值</th>
			<th>下行峰值</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>测速IP</th>

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