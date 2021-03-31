<%-- 
@author 王志猛
@version 1.0
@since 2008-05-06
@category 攻击top10高级查询
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
function query(stime,etime,atype)
{
	var stamp = new Date().getTime();
	var url ="<s:url value="/securitygw/UserTopReport!attackTopADChart.action"><s:param name="device_id" value="device_id"/></s:url>";
 	$("#chartDay").attr("src",url+"&strTime="+stime+"&at="+atype+"&stamp="+stamp+"&endTime="+etime);
 	var turl ="<s:url value="/securitygw/UserTopReport!attackTopADTable.action"></s:url>";
	$.post(
		turl,
		{
			device_id:"<s:property value="device_id" />",
			strTime:stime,
			endTime:etime,
			at:atype,
			stamp:stamp
		},
		function(data){
			$("#tableDay").html(data);
	});
}
function Query()
{
var stime=$.dateToLong($("#start"));
var etime=$.dateToLong($("#end"));
var type=$("#attackType").val();
query(stime,etime,type);
}
$(function(){
var d = new Date();
$("#start").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
$("#end").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate());
});

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
<body>
<table width=98% style="margin-left: 5px;"
	align=center>
	<tr class="tab_title">
		<td colspan="2" class="title_white">攻击用户排名 <INPUT TYPE="text"
			id="start" class=bk value="" size="10"> <INPUT TYPE="button" value="▼"
			class=jianbian onclick="showCalendar('day',event)">至<INPUT TYPE="text"
			id="end" class=bk value="" size="10"><INPUT TYPE="button" value="▼"
			class=jianbian onclick="showCalendar('day',event)"><select
			name="attackType" id="attackType">
			<s:iterator value="attackType">
				<option value="<s:property value="attacktype"/>"><s:property
					value="attackname" /></option>
			</s:iterator>
		</select> <input name="Submit" type="button" onclick="Query()" class="jianbian"
			value="查询"></td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
		<div align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<div align="center">查询报表</div>
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
</table>
</html>
