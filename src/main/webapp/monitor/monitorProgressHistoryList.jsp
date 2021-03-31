<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>监控平台</title>
<link href="<s:url value='/css/iconfont/iconfont.css'/>" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="<s:url value='/css/liulu.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/listview.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_ico.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/user-defined.css'/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckForm.js"/>"></SCRIPT>
<Script>
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
			<th>主机</th>
			<th>进程</th>
			<th>状态</th>
			<th>采集时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="progressHistoryList!=null && progressHistoryList.size()>0">
			<s:iterator value="progressHistoryList" var="progressHistory">
				<tr>
					<td class="column" align="center"><s:property value="monitor_name" /></td>
					<td class="column" align="center"><s:property value="progress_name" /></td>
					<td class="column" align="center">
						<s:if test="#progressHistory.progressDesc == 'UP'">
							<font color="green" style="font-weight: bold;"><s:property value="progressDesc" /></font>
						</s:if>
						<s:elseif test="#progressHistory.progressDesc == 'DOWN'">
							<font color="red" style="font-weight: bold;"><s:property value="progressDesc" /></font>
						</s:elseif>
						<s:else>
							<font color="orange" style="font-weight: bold;"><s:property value="progressDesc" /></font>
						</s:else>
					</td>
					<td class="column" align="center"><s:property value="gathertime" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="4">
					<div style="text-align: center">查询无数据</div>
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<s:if test="progressHistoryList.size()>0">
			<tr>
				<td class="foot" colspan="4">
				    <div style="float: right;">
						<lk:pages
							url="/liposs/monitorReport/monitorAct!monitorProgressHistoryList.action"
							 styleClass="" showType="" isGoTo="true" changeNum="true"/>
					</div>	
				</td>
			</tr>
		</s:if>
	</tfoot>
</table>
</body>
</html>