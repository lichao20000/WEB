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
var defHostId = "<s:property value='monitor_host' />";
function showIframe() {
	$("#dataDiv").hide();
	$("#dataForm").show();
}

function setDataSize(dataHeight) {
	$("#dataForm").height(dataHeight);
}

function monitorHostChange(hostid){
	
	if(hostid == '-1'){
    	return false;
	}
	$("#dataForm").hide("");
	$("#dataDiv").show();
	
	var url = "<s:url value='/liposs/monitorReport/monitorAct!progressMonitorList.action'/>";
	$("#frm").attr("action", url);
	$("#frm").submit();
}

$(function(){
	if(defHostId != '' ){
		$("#monitor_host").val(defHostId);
		monitorHostChange(defHostId);
	}
});
</Script>
</head>
<body>
<table width="98%" border="0" cellspacing="0" cellpadding="0" align=center>
	<tr><td height=20>&nbsp;&nbsp;</td></tr>
	<tr>
  		<td>
			<table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						主机进程状态
					</td>
					<td>
						<img src="<s:url value='/images/attention_2.gif'/>" width="15" height="12">以下显示的是主机进程信息
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" target="dataForm" method="post">
			<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
				<tr bgcolor=#ffffff>
					<td class=column align="right" width="15%">
						主机
					</td>
					<td width="35%">
						<s:select list="monitorHostList" name="monitor_host" id="monitor_host" headerKey="-1" headerValue="请选择主机" listKey="hostid" listValue="hostname"
							value="-1" cssClass="bk" onchange="monitorHostChange(this.value)"></s:select>&nbsp;<font color="red">*</font>			
					</td>
					<td colspan="2"></td>
				</tr>
			</table>
			</form>
		</td>
	</tr>
	<tr>
		<td>
			<div id="dataDiv" style="text-align: center;display: none;">
				正常查询请稍候				
			</div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%"></iframe>
		</td>
	</tr>
</table>
</body>
<%@ include file="../foot.jsp"%>
</html>