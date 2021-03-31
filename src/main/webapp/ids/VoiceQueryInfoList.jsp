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
	<caption>仿真结果</caption>
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>仿真测试类型</th>
			<th>被叫号码</th>
			<th>按键确认</th>
			<th>仿真状态</th>
			<th>仿真结果</th>
			<th>拨号确认结果</th>
			<th>错误原因</th>
		</tr>
	</thead>
		<tbody>
			<tr>
				<td><s:property value="voiceMap['DevSn']" /></td>
				<td><s:property value="voiceMap['TestType']" /></td>
				<td><s:property value="voiceMap['CalledNumber']" /></td>
				<td><s:property value="voiceMap['DialDTMFConfirmEnable']" /></td>
				<td><s:property value="voiceMap['Status']" /></td>
				<td><s:property value="voiceMap['Conclusion']" /></td>
				<td><s:property value="voiceMap['DialDTMFConfirmResult']" /></td>
				<td><s:property value="voiceMap['CallerFailReason']" /></td>
			</tr>
				
		</tbody>
</table>
</s:if>
<s:elseif test='code=="1"'>
	<div align="left"  style="background-color: #E1EEEE;height: 20">
		<s:property value="voiceMap['errMessage']" />
	</div>
</s:elseif>
</body>
</html>