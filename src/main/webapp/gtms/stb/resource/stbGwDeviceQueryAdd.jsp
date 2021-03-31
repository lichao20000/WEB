<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒新增</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
//系统类型
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
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
				$("select[@name='citynext']").html("<option value='-1'>==请先选择属地==</option>");
			});
			break;
		case "cityid":
			var url = "<s:url value='/gtms/stb/resource/userMessage!getCityNext.action'/>";
			var cityId = $("select[@name='cityId']").val();
			if ("-1" == cityId) {
				$("select[@name='citynext']").html("<option value='-1'>==请先选择属地==</option>");
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
				$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
			});
			break;
		case "hardwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='hardwareversion']"),selectvalue);
			});
			break;
		case "softwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetypeByHardVersion.action"/>";
			var hardwareversion = $("select[@name='hardwareversion']").val();
			if("-1"==hardwareversion){
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
				break;
			}
			$.post(url,{
				gwShare_hardwareVersion : hardwareversion
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
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

function insertDevice(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var deviceMac = $("input[name=deviceMac]").val();
	var deviceSn = $("input[name=deviceSn]").val();
	var oui = $("input[name=oui]").val();
	var completeTime = $("input[name=completeTime]").val();
	var citynext = $("select[name=citynext]").val();
// 	var temp = /[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}/;
// 	if (!temp.test(deviceMac))
// 	{
// 	     return false;
// 	}

	if ("-1" == vendorId){
		alert("请选择厂商！");
		return;
	}
	if ("-1" == deviceModelId){
		alert("请选择型号！");
		return;
	}
	if ("-1" == source_devicetypeId){
		alert("请选择软件版本！");
		return;
	}
	if ("-1" == cityId){
		alert("请选择属地！");
		return;
	}
	if ("" == deviceMac){
		alert("MAC地址不可为空");
		return;
	}
	if(false == validateMac(deviceMac)){
		return;
	}
	if ("" == oui || oui.length >6){
		alert("oui不可为空且长度不大于6位");
		return;
	}
	if ("" == deviceSn || deviceSn.length <6){
		alert("设备序列号不可为空且长度不小于6位");
		return;
	}
	if ("" == completeTime){
		alert("注册时间不可为空");
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!add.action'/>";
	$.post(url,{
		vendorId : vendorId,
		deviceModelId : deviceModelId,
		deviceTypeId : source_devicetypeId,
		cityId : cityId,
		citynext : citynext,
		deviceMac : deviceMac.toUpperCase(),
		oui : oui,
		deviceSn : deviceSn,
		completeTime : completeTime
	},function(ajax){
		if("2"== ajax){
			alert("设备序列号或mac地址已存在！");
		}else if("1"== ajax){
			alert("新增成功！");
			window.close();
		}else{
			alert("新增失败！");
		}
	});
}

function validateMac(mac) {  
    mac = mac.toUpperCase();  
    var expre = /[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}:[A-F\d]{2}/;  
    var regexp = new RegExp(expre);  
    if (!regexp.test(mac) || mac.length != 17) {  
        alert("MAC地址错误或含有非法字符，请检查");  
        return false;  
    }
    return true;
}  

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}
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
<form name="frm" id="frm" >

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="2" class="title_1" >机顶盒新增</td></tr>
	<tr>
		<TD align="right" class="column" width="15%">设备序列号</TD>
		<TD width="35%">
			<input type="text" id="deviceSn" name="deviceSn" class="bk" value=""> <font color="red">*至少输入后六位</font>
		</TD>
	</tr>
	<tr>
		<TD align="right" class="column" width="15%">OUI</TD>
		<TD width="35%">
			<input type="text" id="oui" name="oui" class="bk" value=""> <font color="red">*长度不大于六位</font>
		</TD>
	</tr>
	<tr>
		<TD align="right" class="column" width="15%">MAC地址</TD>
		<TD width="35%">
			<input type="text" id="deviceMac" name="deviceMac" class="bk" value=""> <font color="red">*MAC地址只能A-F字母或者数字组成,样式：AA:AA:AA:AA:AA:AA！</font>
		</TD>
	</tr>
	<TR >
		<TD align="right" class="column" width="15%">厂 商</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==请选择==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('hardwareversion','-1')">
				<option value="-1">请先选择厂商</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR>	
		<TD align="right" class="column" width="15%">硬件版本</TD>
		<TD width="35%">
			<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
				<option value="-1">请先选择设备型号</option>
			</select><font color="red">*</font>
		</TD>	
	</TR>	
	<TR>	
		<TD align="right" class="column" width="15%">软件版本</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk">
				<option value="-1">请先选择硬件版本</option>
			</select><font color="red">*</font>
		</TD>	
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">属地</TD>
		<TD width="35%">
			<select name="cityId" class="bk" onchange="change_select('cityid','-1')">
				<option value="-1">==请选择==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">下级属地</TD>
		<TD width="35%">
			<select name="citynext" class="bk">
				<option value="-1">==请选择==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR id="tr_belong">
		<TD align="right" class="column" width="15%">注册时间</TD>
		<TD width="35%">
			<input type="text" name="completeTime" readonly
							value="<s:property value='completeTime'/>" /> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.completeTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择" /><font color="red">*</font>
		</TD>
	</TR>
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insertDevice();" 
				name="addButton" id="addButton" style="CURSOR:hand" style="display:" > 新 增 </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>
</body>