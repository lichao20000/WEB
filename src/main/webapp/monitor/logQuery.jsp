<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>监控日志</title>
<link href="<s:url value='/css/iconfont/iconfont.css'/>" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="<s:url value='/css/liulu.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/listview.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_ico.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/user-defined.css'/>" type="text/css">
<%
	String host_id=request.getParameter("hostid");
	String hostname=request.getParameter("hostname");
%>
<Script>
var hostid = <%=host_id%>;

function showLog(progress_type)
{
	var url = "<s:url value='/liposs/monitorReport/monitorAct!queryLog.action?monitor_host='/>"
		+ hostid+"&progress_type="+progress_type;
	window.open(url);
}
</Script>
</head>

<body>
<table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr>
		<td height=20>&nbsp;&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td>
						<table width="100%" align=center  height="30" border="0" 
							cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									<%=hostname%>主机日志
								</td>
								<td>
									<img src="<s:url value='/images/attention_2.gif'/>" 
										width="15" height="12">
									以下显示的是主机最近日志信息
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table class="querytable" width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr>
  		<td class=column>
			 <input type="button" value=" 系统日志 " class="jianbian" 
			 	onclick="javascript:showLog('systemLog');">
		</td>
		<%if("1".equals(host_id)){%>
			<td class=column>
				<input type="button" value=" acs1日志 " class="jianbian" 
					onclick="javascript:showLog('acs1Log');">
			</td>
			<td class=column>
				<input type="button" value=" acs2日志 " class="jianbian" 
					onclick="javascript:showLog('acs2Log');">
			</td>
		<%}else if("2".equals(host_id)){%>
			<td class=column>
				<input type="button" value=" acs3日志 " class="jianbian" 
					onclick="javascript:showLog('acs3Log');">
			</td>
			<td class=column>
				<input type="button" value=" acs4日志 " class="jianbian" 
					onclick="javascript:showLog('acs4Log');">
			</td>
		<%}else if("19".equals(host_id)){%>
			<td class=column>
				<input type="button" value=" oracle1日志 " class="jianbian" 
					onclick="javascript:showLog('oracle1Log');">
			</td>
		<%}else if("20".equals(host_id)){%>
			<td class=column>
				<input type="button" value=" oracle2日志 " class="jianbian" 
					onclick="javascript:showLog('oracle2Log');">
			</td>
		<%}%>
		<td class=column>
			<input type="button" value=" 备份文件名 " class="jianbian" 
				onclick="javascript:alert('logbak.tar.gz');">
		</td>
	</tr>
</table>
</body>
</html>