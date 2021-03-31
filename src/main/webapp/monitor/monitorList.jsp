<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
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
<Script>
var currHour = "<s:property value='currHour' />";
var preHour = "<s:property value='preHour' />";
function queryMonitorPro(hostid)
{
	var url = "<s:url value='/liposs/monitorReport/monitorAct!progressMonitor.action?monitor_host='/>"
				+ hostid;
	window.open(url);
}

function queryMonitorServ(hostid)
{
	var url = "<s:url value='/monitor/MonitorQuery.jsp?hostid='/>"
				+ hostid + "&currHour=" + currHour + "&preHour="
				+ preHour + "&monitorControl=1";
	window.open(url);
}

function showLogDetail(hostname,hostid)
{
	var url = "<s:url value='/monitor/logQuery.jsp?hostid='/>"+hostid+"&hostname="+hostname;
	window.open(url);
}
</Script>
</head>
<body>
<table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr><td height=20>&nbsp;&nbsp;</td></tr>
	<tr>
  		<td>
			<table width="100%" align=center  height="30" border="0" 
					cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						主机监控平台
					</td>
					<td>
						<img src="<s:url value='/images/attention_2.gif'/>" width="15" height="12">
						以下显示的是主机监控信息
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
	<td>
		<table border=0 cellspacing=1 cellpadding=2 bgcolor="#999999" width="100%" align="center">
			<thead>
				<tr>
					<th>主机</th>
					<th>CPU(利用率%)</th>
					<th>内存(利用率%)</th>
					<th>硬盘(利用率%)</th>
					<th>日志</th>
					<th>ntp同步</th>
					<th>业务状态</th>
					<th>进程状态</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="monitorMapList!=null && monitorMapList.size()>0">
					<s:iterator value="monitorMapList" var="monitorMap">
						<tr>
							<td class="column" align="center">
								<s:property value="hostname" />
							</td>
							<td class="column" align="center">
								<s:property value="cpu" />
							</td>
							<td class="column" align="center">
								<s:property value="mem" />
							</td>
							<td class="column" align="center">
								<s:property value="hard" />
							</td>
							<td class="column" align="center">
								<img src="<s:url value='/images/view.gif'/>" border="0" style="cursor:hand" 
									onclick="showLogDetail('<s:property value="hostname" />','<s:property value='hostid'/>')" alt="查看" >
							</td>
							<td class="column" align="center">
								<s:if test="#monitorMap.ntp == '异常'">
									<font color="red"><s:property value="ntp" /></font>
								</s:if>
								<s:elseif test="#monitorMap.ntp == '未知'">
									<font color="orange"><s:property value="ntp" /></font>
								</s:elseif>
								<s:else>
									<s:property value="ntp" />
								</s:else>
							</td>
							<td class="column" align="center">
								<a href="javascript:void(0);" onclick="queryMonitorServ('<s:property value='hostid' />')">
									<s:if test="#monitorMap.serv == '告警'">
										<font color="red"><s:property value="serv" /></font>
									</s:if>
									<s:elseif test="#monitorMap.serv == '未知'">
										<font color="orange"><s:property value="serv" /></font>
									</s:elseif>
									<s:else>
										<s:property value="serv" />
									</s:else>
								</a>
							</td>
							<td class="column" align="center">
								<a href="javascript:void(0);" onclick="queryMonitorPro('<s:property value='hostid' />')">
									<s:if test="#monitorMap.progress == '异常'">
										<font color="red"><s:property value="progress" /></font>
									</s:if>
									<s:elseif test="#monitorMap.progress == '未知'">
										<font color="orange"><s:property value="progress" /></font>
									</s:elseif>
									<s:else>
										<s:property value="progress" />
									</s:else>
								</a>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan="8">无数据</td>
					</tr>
				</s:else>
			</tbody>
		</table>
	</td>
	</tr>
</table>
</body>
<%@ include file="../foot.jsp"%>
</html>