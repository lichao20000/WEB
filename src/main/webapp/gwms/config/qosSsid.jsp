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

var gw_type = '<s:property value="gw_type"/>';


<!--//
$(function(){
	parent.unblock();
});
var device_id = '<s:property value="deviceId"/>';
var temp_id = '1';
tempId:temp_id
//提交
function checkForm(){
	var que = $("select[@name='queue']").val();
	var ssid = document.frm.ssid;
	var ssidTxt = '';
	if(que == -1){
		alert("请选择Qos队列");
		return false;
	}
	if(typeof(ssid) != 'undefined'){
		if(typeof(ssid.length) != 'undefined'){
			for (i=0; i<ssid.length; i++){
				if(true == ssid[i].checked){
					ssidTxt += ssid[i].value;
					ssidTxt += "\$";
				}
			}
			if(ssidTxt.indexOf("\$") != -1){
				ssidTxt = ssidTxt.substring(0, ssidTxt.length-1);
			}
		}else{
			//alert('ssid.value:' + ssid.value);
			ssidTxt = ssid.value;
		}
	}
	if('' == ssidTxt){
		alert("请选择SSID");
		return false;
	}

	var url = '<s:url value="/gwms/config/qosConfig!ssidQosConfig.action"/>';
	$.post(url,{
		deviceId:device_id,
		queue:que,
		gw_type:gw_type,
		ssidParam:ssidTxt,
		tempId:temp_id
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

function gatherWlan(){
	$("div[@id='divSsid']").html("正在获取SSID...");
	var url = '<s:url value="/gwms/config/qosConfig!gatherWlan.action"/>';
	//查询
	$.post(url,{
		deviceId:device_id
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divSsid']").html("");
		if('' == ajaxMesg){
			$("div[@id='divSsid']").append("设备上没有配置SSID,无法进行Qos下发");
		}else{
			var strHTML = htmlSsid(ajaxMesg);
			$("div[@id='divSsid']").append(strHTML);
			document.all('smit').style.display = '';
		}
		parent.dyniframesize();
	});
}

function htmlSsid(ajaxMesg){
	var res = '';
	if(-1 == ajaxMesg.indexOf("\$")){
		if(-1 == ajaxMesg.indexOf("|")){
			return ajaxMesg;
		}else{
			//只有一个ssid
			var ssid = ajaxMesg.split("\|");
			res += "<input type='checkbox' name='ssid' value='" + ajaxMesg + "'> " + ssid[0];
		}
	}else{
		//有多个ssid
		var meg = ajaxMesg.split("\$");
		var len = meg.length;
		for(var i = 0; i < len; i++){
			var ssid = meg[i].split("|");
			res += "<input type='checkbox' name='ssid' value='" + meg[i] + "'> " + ssid[0];
			res += "<br>";
		}
	}
	return res;
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
<body onload='gatherWlan();getQosList();'>
<form name="frm" action="qosConfig">
<table border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable" height="auto">
	<tr>
		<td bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=1 width="100%">
			<tr bgcolor=#ffffff>
				<th colspan="4">无线Qos结果</th>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999>
		<div id="divQosList"></div>
		</td>
	</tr>
	<tr CLASS="green_foot" bgcolor=#ffffff>
		<td align="right"><input type="button" value=" 刷 新 " onclick="getQosList()">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor=#999999>
		<table border="0" cellspacing="1" cellpadding="1" id="myTable"
			width="100%">
			<tr bgcolor=#ffffff>
				<th colspan=4>无线Qos配置</th>
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
				<td align="right">SSID&nbsp;</td>
				<td class=column colspan=3><div id="divSsid"></div></td>
			</tr>
			<tr CLASS="green_foot" bgcolor=#ffffff>
				<td colspan=4 align="right">
					<input type="button" name="smit" value=" 保 存 " onclick="checkForm()" style="display:none">&nbsp;
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>