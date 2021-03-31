<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
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
			<th>请求收到时间</th>
			<th>传输开始时间</th>
			<th>传输结束时间</th>
			<th>接收字节数（包括控制头）</th>
			<th>接收字节数</th>
			<th>TCP请求时间</th>
			<th>TCP响应时间</th>
			<%if("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
					<th>平均下载速率(Mbps)</th>
					<th>最大下载速率(Mbps)</th>												
			<%
				}else{ 
			%>
				<th>下载速率(M/s)</th>
			<%
				}
			%>
		</tr>
	</thead>
		<tbody>
					
						<tr>
							<td><s:property value="httpMap['DevSn']" /></td>
							<td><s:property value="httpMap['RequestsReceivedTime']" /></td>
							<td><s:property value="httpMap['TransportStartTime']" /></td>
							<td><s:property value="httpMap['TransportEndTime']" /></td>
							<td><s:property value="httpMap['ReceiveByteContainHead']" /></td>
							<td><s:property value="httpMap['ReceiveByte']" /></td>
							<td><s:property value="httpMap['TCPRequestTime']" /></td>
							<td><s:property value="httpMap['TCPResponseTime']" /></td>
							<%if("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								<td><s:property value="httpMap['AvgSampledValues']" /></td>
								<td><s:property value="httpMap['MaxSampledValues']" /></td>											
							<%
								}else{ 
							%>
								<td><s:property value="httpMap['downPert']" /></td>
							<%
								}
							%>
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