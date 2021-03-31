<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<%
String device_id = request.getParameter("device_id");

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备wlan信息界面</title>
<%
	/**
		 * 设备wlan信息界面
		 * 
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-06-24
		 * @category
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
//类型数组
var typeArr = new Array();
typeArr[typeArr.length] = "601";
typeArr[typeArr.length] = "602";
typeArr[typeArr.length] = "603";
typeArr[typeArr.length] = "604";
typeArr[typeArr.length] = "605";
//返回数据
var dataArr = new Array();

var count = 0;
var device_id = "";
//采集方式默认是snmp方式 0:snmp  1:tr069
var gatherType=0;

//初始化
$(function(){
	$("#wlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>正在加载......</td></tr></table>");
	device_id = '<%=device_id%>';
	
	initData();
})

//更改采集方式
function changeGtType()
{
	count = 0;
	gatherType=$("select[@name='gatherType']").val();
	$("#wlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>正在加载......</td></tr></table>");
	
	if (gatherType == '0'){
		initData();
	}
	else{
		initData_tr069();
	}
}

//加载数据
function initData(){
	if (count < typeArr.length){
		$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getSnmpInfo.action"/>", 
			data: "device_id="+device_id+"&oid_type="+typeArr[count]+"&type=0",
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//加载下一条数据
					count++;
					if(count < typeArr.length){
						initData();
					}
					else{
						//显示数据
						showData();
					}
				},
			erro:
				function(xmlR,msg,other){
					dataArr[dataArr.length] = "";
					//加载下一条数据
					count++;
					if(count < typeArr.length){
						initData();
					}
					else{
						//显示数据
						showData();
					}
				}
		});
	}
}

//tr069方式获取数据
function initData_tr069(){
	
	var oid = "606#607#608#609#610";
	
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getInfoByTr069.action"/>", 
			data: "device_id="+device_id+"&oid_type="+oid,
			success:
				function(data)
				{
					dataArr = data.split("#");
					//显示数据
					showData();
				},
			erro:
				function(xmlR,msg,other)
				{
					for (var j=0;j<=typeArr.length;j++){
						dataArr[j] = "";
					}
					//显示数据
					showData();
				}
		});
}

//状态数组
var statusArr = new Array();
statusArr[0] = "disable";
statusArr[1] = "enable";
//模式数组
var modelArr = new Array();
modelArr[0] = "disable";
modelArr[1] = "802.11 B/G mixed";
modelArr[2] = "802.11 B only";
modelArr[3] = "802.11 G only";
modelArr[4] = "802.11 A only";
modelArr[5] = "802.11 N only";
modelArr[6] = "802.11 G/N mixed";
modelArr[7] = "802.11 A/N mixed";
modelArr[8] = "802.11 B/G/N mixed";
//广播模式数组
var broadArr = new Array();
broadArr[0] = "Broadcast";
broadArr[1] = "nonBroadcast";

function showData(){
	var walnStatus = dataArr[0].substr(0,1);
	var wlanmodel = dataArr[1].substr(0,1);
	var ssidIndex = dataArr[2].split("<br>");
	var ssidName = dataArr[3].split("<br>");
	var ssidBroad = dataArr[4].split("<br>");
	
	var str = "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'>";
			
	if (typeof(statusArr[walnStatus]) == 'undefined'){
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>无线使能状态</td><td width='70%'>无法采集到数据</td></tr>";
	}
	else{
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>无线使能状态</td><td width='70%'>" + statusArr[walnStatus] + "</td></tr>";
	}
	if (typeof(modelArr[wlanmodel]) == 'undefined'){
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>无线网络模式</td><td width='70%'>无法采集到数据</td></tr>";
	}
	else{
		str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>无线网络模式</td><td width='70%'>" + modelArr[wlanmodel] + "</td></tr>";
	}
	str += "</table><br>";
	
	var len = ssidIndex.length;
	for (var i=0;i<len;i++){
		if (ssidIndex[i] != ""){
			str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'>";
			
			//SSID index
			str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>SSID index：</td><td width='70%'>" + ssidIndex[i] + "</td>";
			//SSID名稱
			if (ssidName.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID名称：</td><td>" + ssidName[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID名称：</td><td></td>";
			}
			//SSID廣播
			if (ssidBroad.length > i){
				if (typeof(broadArr[ssidBroad[i].substr(0,1)]) == 'undefined'){
					str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID广播：</td><td>无法采集到数据</td>";
				}
				else{
					str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID广播：</td><td>" + broadArr[ssidBroad[i].substr(0,1)] + "</td>";
				}
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>SSID广播：</td><td></td>";
			}
			str += "</table>";
			str += "<br>";
		}
	}
	
	if (str == ""){
		str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>采集数据失败，请重试！</td></tr></table>";
	}
	
	$("#wlanInfo").html(str);
}
</script>
</head>

<body>
<form action="">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td height="20">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
				bgcolor="#000000">
				<tr><th>设备wlan信息</th></tr>
				<tr>
					<td bgcolor='#FFFFFF'>采集方式:
						<select name="gatherType" onchange="changeGtType();">
							<option value="0">SNMP</option>
							<option value="1">TR069</option>
						</select>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
<div id="wlanInfo"></div>
</form>
</body>
</html>