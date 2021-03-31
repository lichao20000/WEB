<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ include file="../../timelater.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<%@ include file="../head.jsp"%>
<title>SSID Qos配置</title>
<script type="text/javascript">
<!--//
//业务类型，上网
var device_id = '<s:property value="deviceId"/>';
var gw_type = '<s:property value="gw_type"/>';
var temp_id = '2';
//提交
function checkForm(){
	var que = $("select[@name='queue']").val();
	var iface = 2;
	if(que == -1){
		alert("请选择Qos队列");
		return false;
	}
	var url = '<s:url value="/gwms/config/qosConfig!iptvQosConfig.action"/>';
	$.post(url,{
		deviceId:device_id,
		queue:que,
		gw_type:gw_type,
		iptvInter:iface,
		tempId:temp_id
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

function getQosList(){
	var url = '<s:url value="/gwms/config/qosConfig!qosConfigList.action"/>';
	//查询
	$.post(url,{
		deviceId:device_id,
		tempId:temp_id
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divQosList']").html("");
		$("div[@id='divQosList']").append(ajaxMesg);
		parent.dyniframesize();
	});
}
//-->
</script>
</head>
<body onload="getQosList();">
<form name="frm" action="qosConfig">
<table border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable" height="auto">
	<tr>
		<td bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=1 width="100%">
			<tr bgcolor=#ffffff>
				<th colspan="4">Iptv业务Qos结果</th>
			</tr>
		</table>
		</td>
	</tr>
	<tr CLASS="green_foot" bgcolor=#ffffff>
		<td align="right"><input type="button" value="刷新" onclick="getQosList()">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor=#999999>
		<div id="divQosList"></div>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999>
		<table border="0" cellspacing="1" cellpadding="1" id="myTable"
			width="100%">
			<tr bgcolor=#ffffff>
				<th colspan=4>Iptv业务Qos配置</th>
			</tr>
			<tr bgcolor=#ffffff>
				<td align="right" width="20%">队列&nbsp;</td>
				<td class=column colspan=3>
					<select name="queue">
					<option value="-1">==请选择==</option>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					</select>
				</td>
			</tr>
			<tr bgcolor=#ffffff>
				<td align="right">Iptv绑定端口&nbsp;</td>
				<td class=column colspan=3>Lan2口</td>
			</tr>
			<tr CLASS="green_foot" bgcolor=#ffffff>
				<td colspan=4 align="right"><input type="button" value="提交配置" onclick="checkForm()">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>