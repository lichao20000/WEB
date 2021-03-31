<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>进程状态监控</title>
<link href="<s:url value='/css/iconfont/iconfont.css'/>" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="<s:url value='/css/liulu.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/listview.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_ico.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/user-defined.css'/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<Script>
	function queryProHistory(hostid,progressid,hostname,progresstype){
		var url = "<s:url value='/liposs/monitorReport/monitorAct!monitorProgressHistory.action?monitor_host='/>" + hostid + "&progress_type=" + progressid;
		window.open(url);
	}
	$(function() {
		parent.showIframe();
		var h = $(document.body).height();
		parent.setDataSize(h + 50);
	});
</Script>
</head>
<body>
<table border=0 cellspacing=1 cellpadding=2 bgcolor="#999999" width="100%" align="center">
	<thead>
		<tr>
			<th>主机名称</th>
			<th>进程</th>
			<th>状态</th>
			<th>历史详情</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="currProgressList!=null && currProgressList.size()>0">
			<s:iterator value="currProgressList" var="currProgress">
				<tr>
					<td class="column" align="center">
						<s:property value="hostname" />
					</td>
					<td class="column" align="center">
						<s:property value="progresstype" />
					</td>
					<td class="column" align="center">
						<s:if test="#currProgress.progressDesc == 'UP'">
							<font color="green" style="font-weight: bold;"><s:property value="progressDesc" /></font>
						</s:if>
						<s:elseif test="#currProgress.progressDesc == 'DOWN'">
							<font color="red" style="font-weight: bold;"><s:property value="progressDesc" /></font>
						</s:elseif>
						<s:else>
							<font color="orange" style="font-weight: bold;"><s:property value="progressDesc" /></font>
						</s:else>
					</td>
					<td class="column" align="center">
						<a href="javascript:void(0);" onclick="queryProHistory('<s:property value='hostid'/>','<s:property value='progressid'/>','<s:property value='hostname' />','<s:property value='progresstype' />')">历史详情</a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
				<tr>
					<td colspan="4" align="center">该主机没有需要监控的进程</td>
				</tr>
		</s:else>
	</tbody>
</table>
</body>
<%@ include file="../foot.jsp"%>
</html>