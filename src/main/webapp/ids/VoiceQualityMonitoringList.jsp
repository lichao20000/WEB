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
			<th>记录时间</th>
			<th>发送包数</th>
			<th>接收包数</th>
			<th>平均时延</th>
			<th>平均抖动</th>
			<th>丢包率，单位：%</th>
			<th>本端IP</th>
			<th>本端端口</th>
			<th>远端IP</th>
			<th>远端端口</th>
			<th>编解码</th>
		</tr>
	</thead>
		<tbody>
			<tr>
				<td><s:property value="voiceMonitoringMap['DevSn']" /></td>
				<td><s:property value="voiceMonitoringMap['StatTime']" /></td>
				<td><s:property value="voiceMonitoringMap['TxPackets']" /></td>
				<td><s:property value="voiceMonitoringMap['RxPackets']" /></td>
				<td><s:property value="voiceMonitoringMap['MeanDelay']" /></td>
				<td><s:property value="voiceMonitoringMap['MeanJitter']" /></td>
				<td><s:property value="voiceMonitoringMap['FractionLoss']" /></td>
				<td><s:property value="voiceMonitoringMap['LocalIPAddress']" /></td>
				<td><s:property value="voiceMonitoringMap['LocalUDPPort']" /></td>
				<td><s:property value="voiceMonitoringMap['FarEndIPAddress']" /></td>
				<td><s:property value="voiceMonitoringMap['FarEndUDPPort']" /></td>
				<td><s:property value="voiceMonitoringMap['Codec']" /></td>
			</tr>
				
		</tbody>
</table>
</s:if>
<s:elseif test='code=="1"'>
	<div align="left"  style="background-color: #E1EEEE;height: 20">
		<s:property value="voiceMonitoringMap['errMessage']" />
	</div>
</s:elseif>
</body>
</html>