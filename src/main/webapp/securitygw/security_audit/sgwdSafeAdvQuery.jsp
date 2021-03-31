<!-- 
@author 何茂才(5159)
@version 1.0
@since 2008-05-06
@category 安全事件高级查询
 -->
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>江苏电信网管专家系统</title>
<link href="<s:url value="/model_vip/css/liulu.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/tablecss.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/model_vip/css/css_blue.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/Calendar.js"/>"></SCRIPT>
<script type="text/javascript"
 src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript">
function Query(){
	var adv_stime = $.dateToLong($("#start")) / 1000;
	var adv_etime = $.dateToLong($("#end")) / 1000;
	
	var url="<s:url value="/securitygw/SafeReport!getSafeAdvQuery.action"><s:param name="deviceid" value="deviceid"/></s:url>&adv_stime=" + adv_stime + "&adv_etime=" + adv_etime +"&tt=" + new Date();	
	$("#chartAdv").show();
	$("#chartAdv").attr("src",url);

	getTableData("<s:url value="/securitygw/SafeReport!getSafeAdvTable.action"/>","#tableAdv",adv_stime,adv_etime);
}
//获取ajax数据，加载到div中
function getTableData(url,tableId,adv_stime,adv_etime){
	$(tableId).html("数据生成中...");
	$.post(
		url,
		{
			deviceid:"<s:property value="deviceid" />",
			adv_stime:adv_stime,
			adv_etime:adv_etime
		},
		function(data){
			$(tableId).html(data);
	});
}
</script>
<style type="text/css">
<!--
body {
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;
}
a{margin-bottom:-1px;}
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
<table width=98% style="margin-left: 5px;" align=center>
	<tr class="tab_title">
		<td colspan="2" class="title_white">企业安全事件报表
			 <INPUT TYPE="text" id="start" class=bk value="<s:property value="nowDateString" />">
			 <INPUT TYPE="button" value="▼" class=jianbian onclick="showCalendar('day',event)">
			 至<INPUT TYPE="text" id="end" class=bk value="<s:property value="nowDateString" />">
			 <INPUT TYPE="button" value="▼" class=jianbian onclick="showCalendar('day',event)">
			 <input name="Submit" type="button" onclick="Query()" class="jianbian" value="查询">
		</td>
	</tr>
	<tr>
		<td class="trOver_blue" colspan="2">
			<div align="center">
			  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
				<tr>
				  <td><div align="center">报表</div></td>
				  <td width="6%"><a href="#"><img src="./images/button_back.gif" width="15" height="10" border="0">Top</a></td>
				</tr>
			  </table>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center">
				<span class="style2">
					<a href="#" >
						<img border=0 id="chartAdv" src="../images/loading.gif" style="display:none;"/>
					</a>
				</span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="tr_white" colspan="2">
			<div align="center" id="tableAdv">
			</div>
		</td>
	</tr>
</table>
</html>
