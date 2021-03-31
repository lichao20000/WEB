<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备vlan信息界面</title>
<%
	/**
		 * 设备vlan信息界面
		 * 
		 * @author 陈仲民(5243)
		 * @version 1.0
		 * @since 2008-06-05
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
typeArr[typeArr.length] = "351";
typeArr[typeArr.length] = "352";
typeArr[typeArr.length] = "353";
//返回数据
var dataArr = new Array();

var count = 0;
var device_id = "";
//采集方式默认是snmp方式 0:snmp  1:tr069
var gatherType=0;

//初始化
$(function(){
	$("#vlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>正在加载......</td></tr></table>");
	device_id = '<s:property value="device_id" />';
	
	initData();
})

//更改采集方式
function changeGtType()
{
	count = 0;
	gatherType=$("select[@name='gatherType']").val();
	$("#vlanInfo").html("<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>正在加载......</td></tr></table>");
	$("#vlanCount").html("配置的vlan数量：0");
	
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
						//端口信息单独处理
						getVlanPort();
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
						//端口信息单独处理
						getVlanPort();
					}
				}
		});
	}
}

//加载端口信息
function getVlanPort(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getVlanPort.action"/>", 
			data: "device_id="+device_id,
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//SNMP采集MAC地址
					getMacInfo_snmp();
				},
			erro:
				function(xmlR,msg,other)
				{
					dataArr[dataArr.length] = "";
					//SNMP采集MAC地址
					getMacInfo_snmp();
				}
		});
}

//SNMP采集MAC地址
function getMacInfo_snmp(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getMacInfoSnmp.action"/>", 
			data: "device_id="+device_id,
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//采集vlan数量
					getVlanNum();
				},
			erro:
				function(xmlR,msg,other)
				{
					dataArr[dataArr.length] = "";
					//采集vlan数量
					getVlanNum();
				}
		});
}

//从数据库查询MAC地址
function getMacInfo(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getMacInfo.action"/>", 
			data: "device_id="+device_id,
			success:
				function(data)
				{
					dataArr[dataArr.length] = data;
					//全部加载完成后呈现数据
					showData();
				},
			erro:
				function(xmlR,msg,other)
				{
					dataArr[dataArr.length] = "";
					//全部加载完成后呈现数据
					showData();
				}
		});
}

//tr069方式获取数据
function initData_tr069(){
	
	var oid = "373#374#375#372";
	
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getInfoByTr069.action"/>", 
			data: "device_id="+device_id+"&oid_type="+oid,
			success:
				function(data)
				{
					dataArr = data.split("#");
					//从数据库查询mac地址信息
					getMacInfo();
				},
			erro:
				function(xmlR,msg,other)
				{
					for (var j=0;j<=typeArr.length;j++){
						dataArr[j] = "";
					}
					//从数据库查询mac地址信息
					getMacInfo();
				}
		});
}

var vlanNum = '暂不支持该属性采集';

function showData(){
	var vlanId = dataArr[0].split("<br>");
	var vlanIp = dataArr[1].split("<br>");
	var vlanMask = dataArr[2].split("<br>");
	var vlanPort = dataArr[3].split("<br>");
	var macInfo = dataArr[4].split("<cisco>");
	
	var len = vlanId.length;
	var dataSize = 0;
	var str = "";
	for (var i=0;i<len;i++){
		if (vlanId[i] != ""){
			str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'>";
			
			//VLAN ID
			str += "<tr bgcolor='#FFFFFF'><td class='column' width='30%'>VLAN ID：</td><td width='70%'>" + vlanId[i] + "</td>";
			//VLAN IP
			if (vlanIp.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN IP：</td><td>" + vlanIp[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN IP：</td><td></td>";
			}
			//VLAN 掩码
			if (vlanMask.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN 掩码：</td><td>" + vlanMask[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>VLAN 掩码：</td><td></td>";
			}
			//绑定端口列表
			if (vlanPort.length > i){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>绑定端口列表：</td><td>" + vlanPort[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>绑定端口列表：</td><td></td>";
			}
			//MAC地址
			if (dataArr[4].indexOf("<cisco>") != -1){
				str += "<tr bgcolor='#FFFFFF'><td class='column'>MAC地址：</td><td>" + macInfo[i] + "</td>";
			}
			else{
				str += "<tr bgcolor='#FFFFFF'><td class='column'>MAC地址：</td><td>" + dataArr[4] + "</td>";
			}
			str += "</table>";
			str += "<br>";
			dataSize ++;
		}
	}
	
	if (str == ""){
		str += "<table border=0 cellspacing=1 cellpadding=2 width='100%' bgcolor='#000000'><tr bgcolor='#FFFFFF'><td>采集数据失败，请重试！</td></tr></table>";
	}
	
	$("#vlanInfo").html(str);
	if (vlanNum == '暂不支持该属性采集' || vlanNum == '没有配置采集oid'){
		$("#vlanCount").html("配置的vlan数量："+dataSize);
	}
	else{
		$("#vlanCount").html("配置的vlan数量："+vlanNum);
	}
}

//采集vlan数量
function getVlanNum(){
	$.ajax({
			type: "POST", 
			url: "<s:url value="/bbms/GetSnmpInfo!getSnmpInfo.action"/>", 
			data: "device_id="+device_id+"&oid_type=606&type=0",
			success:
				function(data)
				{
					vlanNum = data;
					//全部加载完成后呈现数据
					showData();
				},
			erro:
				function(xmlR,msg,other)
				{
					vlanNum = 0;
					//全部加载完成后呈现数据
					showData();
				}
		});
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
				<tr><th>设备vlan信息</th></tr>
				<tr>
					<td bgcolor='#FFFFFF'>采集方式:
						<select name="gatherType" onchange="changeGtType();">
							<option value="0">SNMP</option>
							<option value="1">TR069</option>
						</select>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td><div id="vlanCount">配置的vlan数量：0</div></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>
<div id="vlanInfo"></div>
</form>
</body>
</html>