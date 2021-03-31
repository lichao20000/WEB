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
			<th>测速状态</th>
			<th>采集开始时间</th>
			<th>采集结束时间</th>
			<th>下载字节数</th>
		 <s:if test="null != httpMap['httpSpeedRet'] && httpMap['httpSpeedRet'] != ''">
		    <th>测速下载速率（Mbps）</th>
			<th>总体速率（Mbps）</th>
			<th>是否达标</th>
		  </s:if>
		  <s:else>
		    <th>测速下载速率（KBps）</th>
			<th>总体速率（KBps）</th>
		  </s:else>
		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td align="center"><s:property value="httpMap['DiagnosticsState']" /></td>
							<td align="center"><s:property value="httpMap['TransportStartTime']" /></td>
							<td align="center"><s:property value="httpMap['TransportEndTime']" /></td>
							<td align="center"><s:property value="httpMap['ReceiveByte']" /></td>
							<td align="center"><s:property value="httpMap['AvgSampledValues']" /></td>
							<td align="center"><s:property value="httpMap['AvgSampledTotalValues']" /></td>
							<s:if test="null != httpMap['httpSpeedRet'] && httpMap['httpSpeedRet'] != ''">
		                      <td align="center"><s:property value="httpMap['httpSpeedRet']" /></td>
		                   </s:if>
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