<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒零配置下发参数信息管理</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	change_select("vendor","-1");
	change_select("city","-1");
});

function change_select(type,selectvalue)
{
	switch (type)
	{
		case "city":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNextChild.action'/>";
			$.post(url, {}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='cityId']"),selectvalue);
				$("select[@name='citynext']").html(
						"<option value='-1'>==请先选择属地==</option>");
			});
			break;
			
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
			var cityId = $("select[@name='cityId']").val();
			if ("-1" == cityId) {
				$("select[@name='citynext']").html(
						"<option value='-1'>==请先选择属地==</option>");
				break;
			}
			$.post(url, {
				citynext : cityId
			}, function(ajax) {
				gwShare_parseMessage(ajax, $("select[@name='citynext']"),selectvalue);
			});
			break;
			
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
			});
			break;
			
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
			});
			break;
		
		default:
			alert("未知查询选项！");
			break;
	}
}

//解析查询设备型号返回值的方法
function gwShare_parseMessage(ajax,field,selectvalue)
{
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	
	field.html("");
	option = "<option value='-1' selected>==请选择==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++)
	{
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		
		try{
			field.append(option);
		}catch(e){
			alert("设备型号检索失败！");
		}
	}
	
	if(flag){
		field.attr("value","-1");
	}
}

function getConfParamList()
{
	var deviceMac = trim($("input[name=deviceMac]").val());
	var deviceSn = trim($("input[name=deviceSn]").val());
	
	if (!("" == deviceSn) && deviceSn.length <6){
		alert("设备序列号不为空的情况下，最低六位！");
		return;
	}
	
	if (!("" == deviceMac) && !validateMac(deviceMac)){
		return;
	}
	
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConfParamList.action'/>";
	frm.submit();
}

function getConParamInfo(deviceId)
{
	var page = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConParamInfo.action'/>?"
					+"deviceId=" + deviceId;
	window.open(page, "","left=200,top=100,width=900,height=300,resizable=yes,scrollbars=yes");
}

function deleteConParamInfo(deviceId,mac)
{
	if(!confirm("确定删除设备["+mac+"]的零配置参数？")){
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbSetConfParam!deleteConParamInfo.action'/>";
	$.post(url,{
		deviceId:deviceId
	},function(ajax){
		alert(ajax);
		getConfParamList();
	});
}

function validateMac(mac) 
{
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 17) {  
        alert("MAC地址只能A-F字母或者数字组成,样式：AA:AA:AA:AA:AA:AA！");  
        return false;  
    }
    return true;
}  

function trim(str)
{
     return str.replace(/(^\s*)|(\s*$)/g,"");
}


//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(window).resize(function() {
	dyniframesize();
});
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>

<body>
<form name="frm" id="frm" target="dataForm">
	<input type="hidden" name="showType" value=<s:property value="showType"/> >
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							机顶盒零配置下发参数信息管理</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" /> 
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr>
						<th colspan="4">机顶盒零配置下发参数信息查询</th>
					</tr>
					<tr>
						<td align="right" class=column width="15%">设备序列号</td>
						<td width="35%">
							<input type="text" id="deviceSn" name="deviceSn" class="bk" value="">
						</td>
						<td align="right" class=column width="15%">MAC地址</td>
						<td align="left" width="35%">
							<input type="text" id="deviceMac" name="deviceMac" class="bk" value="">
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">厂 商</td>
						<td width="35%">
							<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</td>
						<td align="right" class=column width="15%">设备型号</td>
						<td align="left" width="35%">
							<select name="deviceModelId" class="bk">
								<option value="-1">请先选择厂商</option>
							</select>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<td align="right" class="column" width="15%">属 地</td>
						<td align="left" width="35%">
							<select name="cityId" id="cityId" class="bk" onchange="change_select('cityid','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</td>
						<td align="right" class="column" width="15%">下级属地</td>
						<td align="left" width="35%"><select name="citynext" class="bk">
								<option value="-1">请先选择属地</option>
						</select></td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">最近修改开始时间</td>
						<td width="35%">
							<input type="text" name="updateStartTime" readonly value="<s:property value='updateStartTime'/>" />
							 <img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.updateStartTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" />
						</td>
						<td align="right" class="column" width="15%">最近修改结束时间</td>
						<td width="35%">
							<input type="text" name="updateEndTime" readonly value="<s:property value='updateEndTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.updateEndTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" />
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">最近生效开始时间</td>
						<td width="35%">
							<input type="text" name="setLastStartTime" readonly value="<s:property value='setLastStartTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.setLastStartTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" />
						</td>
						<td align="right" class="column" width="15%">最近生效结束时间</td>
						<td width="35%">
							<input type="text" name="setLastEndTime" readonly value="<s:property value='setLastEndTime'/>" /> 
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.setLastEndTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="选择" />
						</td>
					</tr>
					
					<tr>
						<td align="right" class=column width="15%">自动待机开关</td>
						<td width="35%">
							<select name='auto_sleep_mode'>
								<option value='-1'>请选择</option>
								<option value='1'>开</option>
								<option value='0'>关</option>
							</select>
						</td>
						<td align="right" class="column" width="15%">自动待机关闭时长</td>
						<td width="35%">
							<select name='auto_sleep_time'>
								<option value='-1'>请选择</option>
								<option value='3600'>1小时</option>
								<option value='1800'>30分钟</option>
								<option value='600'>10分钟</option>
								<option value='300'>5分钟</option>
								<option value='180'>3分钟</option>
								<option value='60'>1分钟</option>
								<option value='30'>30秒</option>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right" class=column width="15%">有线网络类型</td>
						<td width="35%">
							<select name='ip_protocol_version_lan'>
								<option value='-1'>请选择</option>
								<option value='1'>IPv4</option>
								<option value='2'>IPv6</option>
								<option value='3'>IPv4/v6</option>
							</select>
						</td>
						<td align="right" class="column" width="15%">无线网络类型</td>
						<td width="35%">
							<select name='ip_protocol_version_wifi'>
								<option value='-1'>请选择</option>
								<option value='1'>IPv4</option>
								<option value='2'>IPv6</option>
								<option value='3'>IPv4/v6</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td align="right" class=column width="15%">业务账号</td>
						<td width="35%">
							<input type="text" name="servAccount" class="bk" value="">
						</td>
					</tr>
					<tr >
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:getConfParamList();" 
								name="gwShare_queryButton" style="CURSOR:hand"> 查 询 </button>&nbsp;&nbsp;
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>


</body>