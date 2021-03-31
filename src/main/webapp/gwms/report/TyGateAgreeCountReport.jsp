<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>天翼网关版本统一率报表</title>
<%-- <SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jsDate/WdatePicker.js"/>"></SCRIPT> --%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/progress.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript">

$(function(){
});

function doQuery(){
	$("#button1").attr("disabled", true);
	var startTime=$("input[@name='startTime']").val();
	var endTime=$("input[@name='endTime']").val();
	var cityId=$("input[@name='cityId']").val();
	var gwType = "<s:property value='gwType' />";
	var recent_start_Time=$("input[@name='recent_start_Time']").val();
	var recent_end_Time=$("input[@name='recent_end_Time']").val();
	var obj = document.getElementsByName("isExcludeUpgrade");
	var isExcludeUpgrade = '';
	for (var i = 0; i < obj.length; i++) {
		if (obj[i].checked) {
			isExcludeUpgrade += "" + obj[i].value + "";
		}
	}
	queryData(cityId,startTime,endTime,gwType,recent_start_Time,recent_end_Time,isExcludeUpgrade);
	$("#button1").attr("disabled", false);
}

// 查询列表数据
function queryData(cityId,startTime,endTime,gwType,recent_start_Time,recent_end_Time,isExcludeUpgrade){
	$("div[@id='resultData']").html("正在获取统计数据...");
	startProgress();
	var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!getReportData.action'/>";
	$.post(url,{
		cityId:cityId,
		startTime:startTime,
		endTime:endTime,
		gwType:gwType,
		recent_start_Time:recent_start_Time,
		recent_end_Time:recent_end_Time,
		isExcludeUpgrade:isExcludeUpgrade
	},function(ajax){
	    $("div[@id='resultData']").html("");
		$("div[@id='resultData']").append(ajax);
		stopProgress();
	});	
}
// 导出列表数据
function queryDataForExcel(cityId,startTime,endTime,gwType,isExcludeUpgrade,recent_start_Time,recent_end_Time){
	var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!getReportData.action'/>";
	document.reportForm.action = url+"?reportType=excel&cityId="+cityId+"&startTime="+startTime+"&endTime="+endTime+"&gwType="+gwType+"&isExcludeUpgrade="+isExcludeUpgrade+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time;
	document.reportForm.method = "post";
	document.reportForm.submit();
}

// 查询当前地市详情数据
function getDetailList(cityId,startTime,endTime,isNewVersion,gwType,isExcludeUpgrade,recent_start_Time,recent_end_Time){
	var page = "<s:url value='/gwms/report/tyGateAgreeCountReport!getTyDetailCountList.action'/>?cityId=" + cityId+"&startTime="+startTime+"&endTime="+endTime+"&isNewVersion="+isNewVersion+"&gwType="+gwType+"&isExcludeUpgrade="+isExcludeUpgrade+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

// 导出当前地市详情数据
function queryDetailDataForExcel(cityId,startTime,endTime,isNewVersion,gwType,isExcludeUpgrade,recent_start_Time,recent_end_Time){
	var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!getTyDetailCountList.action'/>";
	document.reportForm.action = url+"?reportType=excel&cityId="+cityId+"&startTime="+startTime+"&endTime="+endTime+"&isNewVersion="+isNewVersion+"&gwType="+gwType+"&isExcludeUpgrade="+isExcludeUpgrade+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time;
	document.reportForm.method = "post";
	document.reportForm.submit();
}
</SCRIPT>
	
</head>
<body>
<form name="frm" action="" method="post">
<br>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
 		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">天翼网关版本统一率报表</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"/></td>
					</tr>
				</table>
			</td>
		</tr>
 		<tr>
  			<td>
  	 			<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
			  	 	<tr>
			  	 		<TH colspan="5">天翼网关版本统一率报表</TH>
			  	 	</tr>
					
					<TR>
							<td class=column width="12%" align='right'>BSS受理时间</td>
							<td width="30%" class=column>
							<input type="text" name="startTime" readonly  value="<s:property value="startTime" />">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择">&nbsp;~&nbsp; 
							<input type="text" name="endTime" readonly  value="<s:property value="endTime" />">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择">
							</td>
							
							<td class=column width="12%" align='right'>最近上报时间</td>
							<td width="30%" class=column>
							<input type="text" name="recent_start_Time" value='' readonly >
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.recent_start_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择">&nbsp;~&nbsp;
							<input type="text" name="recent_end_Time" value='' readonly>
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.recent_end_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择"> 
							</td>
							
							<td class=column width="16%" align='left'>
								<input  type="checkbox" name="isExcludeUpgrade" value="1" /><strong>排除3个月内有过升级</strong>
							</td>
							</TR>
					<tr bgcolor=#ffffff>
						<td class=column colspan="5" align="right">
							<button id="button1" onclick="doQuery()">&nbsp;查 询&nbsp;</button>
							<input type="hidden" value="<s:property value="cityId"/>" name="cityId"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height = "20">
			</td>
		</tr>
		<tr>
			<td align="center">
				<div id="resultData" ></div>
				<div id="progress"></div>
			</td>
		</tr>
	</TABLE>
</form>
<form name="reportForm"></form>
</body>
</html>
<%@ include file="../foot.jsp"%>
