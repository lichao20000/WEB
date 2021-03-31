<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@ page import="java.util.Map" %>
<%
Map<String,String> data = (Map)request.getAttribute("data");
String category = data.get("category");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒修改</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
	
<SCRIPT LANGUAGE="JavaScript">
//系统类型
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';

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
	<tr><td colspan="2" class="title_1" >机顶盒修改</td></tr>
	<tr>
		<TD align="right" class="column" width="15%">设备序列号</TD>
		<TD width="35%">
			<input type="hidden" name="deviceId" value=<s:property value="data.deviceId" /> >
			<input type="text" id="deviceSn" name="deviceSn" class="bk" value="<s:property value="data.deviceSerialnumber" />"> <font color="red">*至少输入后六位</font>
		</TD>
	</tr>
	<tr>
		<TD align="right" class="column" width="15%">OUI</TD>
		<TD width="35%">
			<input type="text" id="oui" name="oui" class="bk" value="<s:property value="data.oui" />"> <font color="red">*长度不大于六位</font>
		</TD>
	</tr>
	<tr>
		<TD align="right" class="column" width="15%">MAC地址</TD>
		<TD width="35%">
			<input type="text" id="deviceMac" name="deviceMac" class="bk" value="<s:property value="data.cpe_mac" />" disabled="disabled"> 
			<font color="red">*MAC地址只能A-F字母或者数字组成,样式：AA:AA:AA:AA:AA:AA！</font>
		</TD>
	</tr>
	<TR >
		<TD align="right" class="column" width="15%">厂 商</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1','2')">
				<option value="<s:property value="data.vendorId" />">==请选择==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('hardwareversion','-1','2')">
				<option value="<s:property value="data.deviceModelId" />">请先选择厂商</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR>	
		<TD align="right" class="column" width="15%">硬件版本</TD>
		<TD width="35%">
			<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1','2')">
				<option value="<s:property value="data.hardwareversion" />">请先选择设备型号</option>
			</select><font color="red">*</font>
		</TD>	
	</TR>	
	<TR>	
		<TD align="right" class="column" width="15%">软件版本</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk">
				<option value="<s:property value="data.devicetypeId" />">请先选择硬件版本</option>
			</select><font color="red">*</font>
		</TD>	
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">属地</TD>
		<TD width="35%">
			<select name="cityId" class="bk" onchange="change_select('cityid','-1','2')">
				<option value="<s:property value="data.parentcity" />">==请选择==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="column" width="15%">下级属地</TD>
		<TD width="35%">
			<select name="citynext" class="bk">
				<option value="<s:property value="data.citynext" />">==请选择==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<TR id="category" style="display:none">
		<TD align="right" class="column" width="15%">类别</TD>
		<TD width="35%">
			<select name="category" class="bk">
				<option value="0" <%=("0".equals(category))?"selected":""%> >== 行 ==</option>
				<option value="1" <%=("1".equals(category))?"selected":""%> >== 串 ==</option>
				<option value="2" <%=("1".equals(category))?"selected":""%> >== 未知 ==</option>
			</select><font color="red">*</font>
		</TD>
	</TR>
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insertDevice();" 
				name="addButton" id="addButton" style="CURSOR:hand" style="display:" > 修 改 </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>
</body>
<SCRIPT LANGUAGE="JavaScript">
//系统属地
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';

$(function(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var hardwareversion = $.trim($("select[@name='hardwareversion']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var citynext = $.trim($("select[@name='citynext']").val());
	change_select("vendor",vendorId,"1");
	change_select("deviceModel",deviceModelId,"1");
	change_select("hardwareversion",hardwareversion,"1");
	change_select("softwareversion",devicetypeId,"1");
	change_select("city",cityId,"1");
	change_select("cityid",citynext,"1");
	//湖南联通 机顶盒行货串货管控
	if("hn_lt"==instAreaName)
	{
		$("tr[id*=category]").attr("style","display:");
		var category = $.trim($("select[@name='category']").val());
	}
});

function change_select(type,selectvalue,isChange){
	switch (type){
		case "city":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
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
				if("2" == isChange){
					$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
					$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
					$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
				}
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
				if("2" == isChange){
					$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
					$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");		
				}
			});
			break;
		case "hardwareversion":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceHardVersion.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='hardwareversion']").html("<option value='-1'>==请先选择设备型号==</option>");
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
	var deviceId = $.trim($("input[@name='deviceId']").val());
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var deviceMac = $("input[name=deviceMac]").val();
	var deviceSn = $("input[name=deviceSn]").val();
	var oui = $("input[name=oui]").val();
	var citynext = $("select[name=citynext]").val();
	var category = $("select[name=category]").val();
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
	
	var url = "<s:url value='/gtms/stb/resource/stbGwDeviceQuery!modifyDevice.action'/>";
	$.post(url,{
		deviceId : deviceId,
		vendorId : vendorId,
		deviceModelId : deviceModelId,
		deviceTypeId : source_devicetypeId,
		cityId : cityId,
		citynext : citynext,
		deviceMac : deviceMac,
		oui : oui,
		deviceSn : deviceSn,
		category : category
	},function(ajax){
		if("2"== ajax){
			alert("设备序列号或mac地址已存在！");
		}else if("1"== ajax){
			alert("修改成功！");
			window.close();
		}else{
			alert("修改失败！");
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