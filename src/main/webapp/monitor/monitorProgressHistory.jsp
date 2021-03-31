<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>进程历史信息</title>
<link href="<s:url value='/css/iconfont/iconfont.css'/>" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="<s:url value='/css/liulu.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/tab.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/listview.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/css_ico.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css/user-defined.css'/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/date/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckForm.js"/>"></SCRIPT>

<Script>
	var defHostId = "<s:property value='monitor_host' />";
	var defProgressId = "<s:property value='progress_type' />";
	var defHostName = "<s:property value='monitor_name' />";
	var defProgressName = "<s:property value='progress_name' />";
	
	function doQuery(){
		
		var hostId = $.trim($("#monitor_host").val());
		if(hostId == '-1'){
			alert("请选择需要查询的主机");
	    	return false;
		}
		var progressId = $.trim($("#progress_type").val());
		if(progressId == '-1'){
			alert("请选择需要查询的进程");
	    	return false;
		}
		var startTime = $.trim($("input[@name='startTime']").val());
		var endTime = $.trim($("input[@name='endTime']").val());
		
		
		if(startTime == '' || endTime == ''){
			alert("开始时间和结束时间均不能为空");
	    	return false;
		}
		
		if(startTime > endTime){
	    	alert("开始时间不能大于结束时间");
	    	return false;
	    }
		
		if(validateOneMonth(startTime,endTime)){
	    	alert("时间跨度不能超过一个月");
	    	return false;
	    }
		$("#dataForm").hide("");
		$("#dataDiv").show();
		
		var url = "<s:url value='/liposs/monitorReport/monitorAct!monitorProgressHistoryList.action'/>";
		$("#frm").attr("action", url);
		$("#frm").submit();
	}
	
	function showIframe() {
		$("#dataDiv").hide();
		$("#dataForm").show();
	}
	
	function setDataSize(dataHeight) {
		$("#dataForm").height(dataHeight);
	}
	
	function monitorHostChange(hostid){
		var opts='<option value="-1">请选择进程</option>';
		if(hostid!="-1"){
			var url="<s:url value='/liposs/monitorReport/monitorAct!moitorProgressType.action'/>";
			$.ajaxSettings.async = false; 
			$.post(url,{
				monitor_host:hostid
				},function(ajax){
				if(ajax=="{}" || ajax==""){
					alert('获取进程类型失败或该主机没有需要监控的进程');
					return;
				}
				var arr = ajax.split(",");
				
				for(var i=0;i<arr.length;i++){
					var ar=arr[i].split("#");
					opts+='<option value="'+ar[0]+'">'+ar[1]+'</option>';
				}
			});
			$.ajaxSettings.async = true; 
		}
		$("#progress_type").html(opts);
	}
	$(function(){
		if(defHostId != '' && defProgressId != ''){
			$("#monitor_host").val(defHostId);
			monitorHostChange(defHostId);
			$("#progress_type").val(defProgressId);
			doQuery();
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
						进程历史信息查询
					</td>
					<td>
						<img src="<s:url value='/images/attention_2.gif'/>" width="15" height="12">以下显示的是进程历史信息
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
					<td class=column align="right" width="15%">
						进程
					</td>
					<td width="35%">
						<select name="progress_type" id="progress_type" class="bk">
							<option value="-1">请选择进程</option>
						</select>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class="column" align="right">
						开始时间
					</td>
					<td>
						<input type="text" name="startTime" id="startTime" class='bk' readonly
							value="<s:property value='startTime'/>">
						<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择">&nbsp;<font color="red">*</font>
					</td>
					<td class="column" align="right">
						结束时间
					</td>
					<td>
						<input type="text" name="endTime" id="endTime" class='bk' readonly
							value="<s:property value='endTime'/>">
						<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择">&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<TR>
					<td align="right" class='green_foot' colspan="4">
						<input name="button" type="button" onclick="doQuery();"
							class="jianbian" value=" 查 询 ">
					</td>
				</TR>
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