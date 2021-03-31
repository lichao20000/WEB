<%--
@author 王志猛
@version 1.0
@since 2008-04-07
@category 用户病毒top10
 --%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>苏州安全网关系统</title>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>"
	rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>"
	rel="stylesheet" type="text/css">
<link href="../css/menu_list.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/Calendar.js"/>"></SCRIPT>
<script type="text/javascript">
function query(time,vtype)
{
	var stamp = new Date().getTime();
	var url ="<s:url value="/securitygw/UserTopReport!virusTopDayChart.action"><s:param name="device_id" value="device_id"/></s:url>";
 	$("#chartDay").attr("src",url+"&strTime="+time+"&vt="+vtype+"&stamp="+stamp);
 	var turl ="<s:url value="/securitygw/UserTopReport!virusTopDayTable.action"></s:url>";
	$.post(
		turl,
		{
			device_id:"<s:property value="device_id" />",
			strTime:time,
			vt:vtype,
			stamp:stamp
		},
		function(data){
			$("#tableDay").html(data);
	});
	time+=(24*3600000);//周报表和月报表需要和日报表保持一致的结束时间
	var wurl = "<s:url value="/securitygw/UserTopReport!virusTopWeekChart.action"><s:param name="device_id" value="device_id"/></s:url>";
	 $("#chartWeek").attr("src",wurl+"&strTime="+time+"&vt="+vtype+"&stamp="+stamp);
	 	var wturl ="<s:url value="/securitygw/UserTopReport!virusTopWeekTable.action"></s:url>";
	 	$.post(
		wturl,
		{
			device_id:"<s:property value="device_id" />",
			strTime:time,
			vt:vtype,
			stamp:stamp
		},
		function(data){
			$("#tableWeek").html(data);
	});
	var murl = "<s:url value="/securitygw/UserTopReport!virusTopMonthChart.action"><s:param name="device_id" value="device_id"/></s:url>";
	 $("#chartMonth").attr("src",murl+"&strTime="+time+"&vt="+vtype+"&stamp="+stamp);
	 	var mturl ="<s:url value="/securitygw/UserTopReport!virusTopMonthTable.action"></s:url>";
	 	$.post(
		mturl,
		{
			device_id:"<s:property value="device_id" />",
			strTime:time,
			vt:vtype,
			stamp:stamp
		},
		function(data){
			$("#tableMonth").html(data);
	});
}
function Query()
{
var time=$.dateToLong($("#start"));
var type=$("#virusType").val();
query(time,type);
}
$(function(){
var d = new Date();
var time = d.getTime();
$("#start").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
var type= $("#virusType").val();
query(time,type);
});
function adQuery()
{
$.open("<s:url value="/securitygw/UserTopReport!virusAD.action"><s:param name="device_id" value="device_id"/></s:url>","740px","400px","200px","200px","true");
}
</script>
<style type="text/css">
<!--
@import url(../css/css_ico.css);

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

a {
	margin-bottom: -1px;
}

.style2 {
	font-size: 10pt;
	font-weight: bold;
}

.style1 {
	font-size: 10pt;
	font-weight: bold;
}
-->
</style>
</head>
<body onclick="parent.document_click()">
<table width=98% style="margin-left: 5px; margin-top: 10px;"
	align=center>
	<tr>
		<td width="50%">
		<table width="100%" border="1" cellpadding="0" cellspacing="0"
			class="table">
			<tr class="tab_title">
				<td colspan="5" class="title_white">信息</td>
			</tr>
			<tr class="tr_white">
				<td class="tr_yellow">设备 IP</td>
				<td colspan="4" class="text"><s:property
					value="funInfo.loopback_ip" /></td>
			</tr>
			<tr class="tr_glory">
				<td class="tr_yellow">设备型号</td>
				<td colspan="4" class="text"><s:property
					value="funInfo.device_model" /></td>
			</tr>
			<tr class="trOver_blue">
				<td class="tr_yellow">运行概况</td>
				<td class="text">病毒</td>
				<td class="text">垃圾邮件</td>
				<td class="text">攻击事件</td>
				<td class="text">过虑</td>
			</tr>
			<tr class="tr_glory">
				<td class="tr_yellow">个数</td>
				<td class="tr_green"><a class="black" href="#"><s:property
					value="funInfo.virustimes" /></a></td>
				<td class="tr_green"><a class="black" href="#"><s:property
					value="funInfo.ashmailtimes" /></a></td>
				<td class="tr_green"><a class="black" href="#"><s:property
					value="funInfo.attacktimes" /></a></td>
				<td class="tr_green"><a class="black" href="#"><s:property
					value="funInfo.filtertimes" /></a></td>
			</tr>
			<tr class="tr_white">
				<td class="tr_yellow">当前状态</td>
				<td class="" nowrap colspan="4">
				<table width="89" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<s:if test="funInfo.severity>=4">
							<td class="back_red"><a class="black" href="#"
								onclick="gopage()">Major</a></td>
						</s:if>
						<s:elseif test="funInfo.severity==3">
							<td class="back_org"><a class="black" href="#"
								onclick="gopage()">Minor</a></td>
						</s:elseif>
						<s:elseif test="funInfo.severity==2">
							<td class="back_yel"><a class="black" href="#"
								onclick="gopage()">Warning</a></td>
						</s:elseif>
						<s:else>
							<td class="back_green"><a class="black" href="#"
								onclick="gopage()">Normal</a></td>
						</s:else>
					</tr>
				</table>
				</td>
			</tr>
			<tr class="tr_glory">
				<td class="tr_yellow">备注</td>
				<td class="text" colspan="4"><s:url id="link"
					value="/securitygw/entSecStat!redirect.action">
					<s:param name="deviceId" value="device_id"></s:param>
					<s:param name="costomerName" value="funInfo.customname"></s:param>
				</s:url> <s:a href="%{link}">
					<s:property value="funInfo.customname" />
				</s:a></td>
			</tr>
		</table>
		</td>
		<td>
		<table width="100%" border="1" cellpadding="0" cellspacing="0"
			class="table">

			<s:url value="/securitygw/VirusReport.action" var="VirusReportHref">
				<s:param name="deviceid" value="device_id" />
			</s:url>

			<s:url value="/securitygw/TrashMailReport.action"
				   var="TrashMailReportHref">
				<s:param name="deviceid" value="device_id" />
			</s:url>

			<s:url value="/securitygw/AttackReport.action" var="AttackReportHref">
				<s:param name="deviceid" value="device_id" />
			</s:url>

			<s:url value="/securitygw/FilterReport.action" var="FilterReportHref">
				<s:param name="deviceid" value="device_id" />
			</s:url>

			<s:url value="/securitygw/SafeReport.action" var="SafeReportHref">
				<s:param name="deviceid" value="device_id" />
			</s:url>
			<tr class="tab_title">
				<td colspan="3" class="title_white">用户安全事件</td>
			</tr>
			<tr class="tr_white">
				<td nowrap class="text"><a
					href="<s:property value="#VirusReportHref+'&remark='+funInfo.customname" />"><img
					src="./images/bullet_main_02.gif" width="17" height="13" border="0">安全事件统计</a></td>
				<td nowrap class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/UserTopReport!virusTop.action"><s:param name="device_id" value="device_id"/></s:url>">病毒用户排名</a></td>
				<td nowrap class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/entSecStat!getSpamMailOrginData.action"><s:param name="deviceId" value="device_id"/></s:url>">垃圾邮件详情</a></td>
			</tr>
			<tr class="tr_glory">
				<td class="text"><a
					href="<s:property value="#VirusReportHref+'&remark='+funInfo.customname" />"><img
					src="./images/bullet_main_02.gif" width="17" height="13" border="0">病毒事件统计</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/UserTopReport!asmailTop.action"><s:param  name="device_id" value="device_id"/></s:url>">垃圾邮件用户排名</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/entSecStat!getAttackOrginData.action"><s:param name="deviceId" value="device_id"/></s:url>">攻击详情</a></td>
			</tr>
			<tr class="tr_white">
				<td class="text"><a
					href="<s:property value="#TrashMailReportHref+'&remark='+funInfo.customname" />"><img
					src="./images/bullet_main_02.gif" width="17" height="13" border="0">垃圾邮件统计</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/UserTopReport!attackTop.action"><s:param name="device_id" value="device_id"/></s:url>">攻击用户排名</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/entSecStat!getFilterOrginData.action"><s:param name="deviceId" value="device_id"/></s:url>">过滤详情</a></td>
			</tr>
			<tr class="tr_glory">
				<td class="text"><a
					href="<s:property value="#AttackReportHref+'&remark='+funInfo.customname" />"><img
					src="./images/bullet_main_02.gif" width="17" height="13" border="0">攻击事件统计</a></td>
				<td class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/UserTopReport!filterTop.action"><s:param name="device_id" value="device_id"/></s:url>">过滤用户排名</a></td>
				<td class="text">&nbsp;</td>
			</tr>
			<tr class="tr_white">
				<td class="text"><a
					href="<s:property value="#FilterReportHref+'&remark='+funInfo.customname" />"><img
					src="./images/bullet_main_02.gif" width="17" height="13" border="0">过滤事件统计</a></td>
				<td nowrap class="text">－－－－－－－－－</td>
				<td nowrap class="text">&nbsp;</td>
			</tr>
			<tr class="tr_glory">
				<td class="text">－－－－－－－－－</td>
				<td class="text"><img src="./images/bullet_main_02.gif"
					width="17" height="13" border="0"><a
					href="<s:url value="/securitygw/entSecStat!getVirusOrginData.action"><s:param name="deviceId" value="device_id"/></s:url>">病毒详情</a></td>
				<td class="text">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr class="tab_title">
		<td colspan="2" class="title_white">病毒用户排名 <INPUT TYPE="text"
			id="start" class=bk value=""> <INPUT TYPE="button" value="▼"
			class=jianbian onclick="showCalendar('day',event)"> <select
			name="virusType" id="virusType">
			<s:iterator value="virusType">
				<option value="<s:property value="virustype"/>"><s:property
					value="virusname" /></option>
			</s:iterator>
		</select> <input name="Submit" type="button" onclick="Query()" class="jianbian"
			value="查询">&nbsp;<input type="button" class="jianbian" value="高级查询" onclick="adQuery()"/></td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
		<div align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<div align="center">日报表</div>
				</td>
				<td width="6%"><a href="#"><img
					src="./images/button_back.gif" width="15" height="10" border="0">TOP</a></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
		<div align="center"><span class="style2"> <a href="#">
		<img border=0 id="chartDay" src="../images/loading.gif" /> </a> </span></div>
		<div align="center" id="tableDay"></div>
		</td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
		<div align="center"><span class="style2"></span>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<div align="center">周报表</div>
				</td>
				<td width="6%"><a href="#"><img
					src="./images/button_back.gif" width="15" height="10" border="0">TOP</a></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
		<div align="center"><span class="style2"> <a href="#">
		<img border=0 id="chartWeek" src="../images/loading.gif"> </a> </span></div>
		<div align="center" id="tableWeek"></div>
		</td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
		<div align="center"><span class="style2"></span>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<div align="center">月报表</div>
				</td>
				<td width="6%"><a href="#"><img
					src="./images/button_back.gif" width="15" height="10" border="0">TOP</a></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
		<div align="center"><span class="style2"> <a href="#">
		<img border=0 id="chartMonth" src="../images/loading.gif" /> </a> </span></div>
		<div align="center" id="tableMonth"></div>
		</td>
	</tr>
</table>
</html>
