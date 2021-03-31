<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒设备信息管理</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	change_select("vendor","-1");
	change_select("city","-1");
});

function change_select(type,selectvalue){
	switch (type){
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
function gwShare_parseMessage(ajax,field,selectvalue){
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
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//根据每组value和text标记的值创建一个option对象
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//根据每组value和text标记的值创建一个option对象
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

function getStbDeviceList(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var deviceMac = $("input[name=deviceMac]").val();
	var deviceSn = $("input[name=deviceSn]").val();
	var cityId = $("input[name=cityId]").val();
	var citynext = $("input[name=citynext]").val();
	var servAccount = $("input[name=servAccount]").val();
	if (!("" == deviceSn) && deviceSn.length <6){
		alert("设备序列号不为空的情况下，最低六位！");
		return;
	}
	if (!("" == deviceMac) && false == validateMac(deviceMac)){
		return;
	}
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!getStbDeviceList.action'/>";
	frm.submit();
}

function validateMac(mac) {  
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 17) {  
        alert("MAC地址只能A-F字母或者数字组成,样式：AA:AA:AA:AA:AA:AA！");  
        return false;  
    }
    return true;
}  

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}
function addDevice(){
	var strpage = "<s:url value='/gtms/stb/resource/stbGwDeviceQueryAdd.jsp'/>";
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
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
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							机顶盒设备信息管理</td>
						<td><img src="<s:url value="/images/attention_2.gif"/>"
							width="15" height="12" /> 开始时间和结束时间分别为用户添加的日期<font color="red">*</font></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr><th colspan="4">机顶盒设备信息查询</th></tr>
					<TR>
						<TD align="right" class=column width="15%">设备序列号</TD>
						<TD width="35%">
							<input type="text" id="deviceSn" name="deviceSn" class="bk" value="">
						</TD>
						<TD align="right" class=column width="15%">MAC地址</TD>
						<TD align="left" width="35%">
							<input type="text" id="deviceMac" name="deviceMac" class="bk" value="">
						</TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">厂 商</TD>
						<TD width="35%">
							<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">设备型号</TD>
						<TD align="left" width="35%">
							<select name="deviceModelId" class="bk">
								<option value="-1">请先选择厂商</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
						<TD align="right" class="column" width="15%">属 地</TD>
						<TD align="left" width="35%">
							<select name="cityId" id="cityId" class="bk" onchange="change_select('cityid','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</TD>
						<TD align="right" class="column" width="15%">下级属地</TD>
						<TD align="left" width="35%"><select name="citynext" class="bk">
								<option value="-1">请先选择属地</option>
						</select></TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">首次上报开始时间</TD>
						<TD width="35%">
							<input type="text" name="startTime" readonly
								value="<s:property value='startTime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</TD>
						<TD align="right" class="column" width="15%">首次上报结束时间</TD>
						<TD width="35%">
							<input type="text" name="endTime" readonly
								value="<s:property value='endTime'/>" /> <img
								name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
							<font color="red">*</font>
						</TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">业务账号</TD>
						<TD width="35%">
							<input type="text" id="servAccount" name="servAccount" class="bk" value="">
						</TD>
						<TD style="display: none"></TD>
						<TD width="35%" style="display: none">
<!-- 							<input type="text" id="servAccount" name="servAccount" class="bk" value=""> -->
						</TD>
<!-- 						<TD align="right" class="column" width="15%">首次上报时间</TD> -->
<!-- 						<TD width="35%"> -->
<!-- 							<input type="text" name="startTime" readonly -->
<%-- 								value="<s:property value='startTime'/>" /> <img --%>
<!-- 								name="shortDateimg" -->
<!-- 								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})" -->
<!-- 								src="../../../images/dateButton.png" width="15" height="12" -->
<!-- 								border="0" alt="选择" /> -->
<!-- 											— -->
<!-- 							<input type="text" name="endTime" readonly -->
<%-- 								value="<s:property value='endTime'/>" /> <img --%>
<!-- 								name="shortDateimg" -->
<!-- 								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})" -->
<!-- 								src="../../../images/dateButton.png" width="15" height="12" -->
<!-- 								border="0" alt="选择" /> -->
<!-- 							<font color="red">*</font> -->
<!-- 						</TD> -->
					</TR>
					<tr >
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:getStbDeviceList();" 
								name="gwShare_queryButton" style="CURSOR:hand"> 查 询 </button>&nbsp;&nbsp;
<!-- 								<button onclick="javascript:addDevice();"  -->
<!-- 								name="addTemp" style="CURSOR:hand" style="display:" > 新 增 </button> -->
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
			<td><iframe id="dataForm" name="dataForm" height="0"
					frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
		</tr>
	</table>
</form>

<!-- 	<div class="content"> -->
<!-- 		<iframe id="dataForm" name="dataForm" height="0" frameborder="0" -->
<!-- 			scrolling="no" width="100%" src=""></iframe> -->
<!-- 	</div> -->
</body>