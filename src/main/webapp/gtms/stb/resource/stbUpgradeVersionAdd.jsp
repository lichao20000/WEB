<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒升级版本新增</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
//系统类型
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
$(function(){
	change_select("vendor","-1");
	if("hn_lt"==instAreaName){
		$("tr[id=tr_belong]").attr("style","display: display"); 
		intPlatformType();
	}else{
		$("tr[id=tr_belong]").attr("style","display: none"); 
	}
	if("jx_dx"==instAreaName){
		$("tr[id=tr_type]").attr("style","display: none"); 
	}
});

function intPlatformType(){
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getPlatformS.action'/>";
	
	$.post(url,{},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='platformId']").empty();
					var optionValue = "<option value='0' >请选择平台类型</option>";
					$("select[@id='platformId']").append(optionValue);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var optionValue = "<option value='"+xValue+"' >"+xText+"</option>  ";
						$("select[@id='platformId']").append(optionValue);
					}
				}else{
					$("select[@id='platformId']").empty();
					var optionValue = "<option value='0' >请选择平台类型</option>";
					$("select[@id='platformId']").append(optionValue);
				}
			}else{
				$("select[@id='platformId']").empty();
				var optionValue = "<option value='0' >请选择平台类型</option>";
				$("select[@id='platformId']").append(optionValue);
			}
		});
	
}

function change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='source_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
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
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
				break;
			}
			$.post(url,{
				gwShare_hardwareVersion : hardwareversion
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='source_devicetypeId']"),selectvalue);
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("未知查询选项！");
			break;
	}	
}

function checkDeviceTypeId(){
	var source_devicetypeId = $("select[@name='source_devicetypeId']").val();
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!checkDeviceTypeId.action'/>";
	$.post(url,{
		source_devicetypeId : source_devicetypeId
	},function(ajax){
		if("1"== ajax){
			alert("当前原始版本已存在，请重新选择");
			$('#addButton').attr("disabled",true);
		}else{
			$('#addButton').attr("disabled",false);
		}
	});
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

function insertTemp()
{
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var temp_id = $.trim($("select[@name='temp_id']").val());
	var valid = -1;
	var platformId = 0;
	if("hn_lt"==instAreaName){
		platformId = $.trim($("select[@name='platformId']").val());
		if(platformId == "-1"){
			alert("请选择业务平台！");
			return;
		}
		
		valid = $.trim($("select[@name='valid']").val());
		if(valid == "-1"){
			alert("请选择是否生效！");
			return;
		}
	}
	if(source_devicetypeId == goal_devicetypeId){
		alert("原始版本与目标版本一致，请重新选择！");
		return;
	}
	
	if(source_devicetypeId == "-1"|| "-1" == goal_devicetypeId){
		alert("请选择原始版本及目标版本！");
		return;
	}
	if(temp_id == "-1"){
		alert("请选择软件升级类型！");
		return;
	}
	
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!addUpgradeTemp.action'/>";
	if("hn_lt"==instAreaName)
	{
		$.post(url,{
			vendorId : vendorId,
			deviceModelId : deviceModelId,
			source_devicetypeId : source_devicetypeId,
			goal_devicetypeId : goal_devicetypeId,
			platformId : platformId,
			temp_id : temp_id,
			valid:valid
		},function(ajax){
			if("1"== ajax){
				alert("新增成功！");
			}else{
				alert("新增失败！");
			}
			window.close();
		});
	}
	else
	{
		$.post(url,{
			vendorId : vendorId,
			deviceModelId : deviceModelId,
			source_devicetypeId : source_devicetypeId,
			goal_devicetypeId : goal_devicetypeId,
			platformId : platformId,
			temp_id : temp_id
		},function(ajax){
			if("1"== ajax){
				alert("新增成功！");
			}else{
				alert("新增失败！");
			}
			window.close();
		});
	}
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
<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="2" class="title_1" >机顶盒升级版本新增</td></tr>
	<TR >
		<TD align="right" class="title_2" width="15%">厂 商</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<%
				if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) {
			%>
				<select name="deviceModelId" class="bk" onchange="change_select('hardwareversion','-1')">
				<option value="-1">请先选择厂商</option>
			</select>
			<%
				}else{
			%>
			<select name="deviceModelId" class="bk" onchange="change_select('devicetype','-1')">
				<option value="-1">请先选择厂商</option>
			</select>
			<%
				}
			%>
		</TD>
	</TR>
	<%
		if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) {
	%>
		<TR>	
			<TD align="right" class="title_2" width="15%">硬件版本</TD>
			<TD width="35%">
				<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
					<option value="-1">请先选择设备型号</option>
				</select>
			</TD>	
		</TR>	
	<%
		}
	%>
	<TR>	
		<TD align="right" class="title_2" width="15%">原版本</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk" onchange="checkDeviceTypeId()">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>	
	</TR>
	<TR >	
		<TD align="right" class="title_2" width="15%">目标版本</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>
	</TR>
	<TR id="tr_type">
		<TD align="right" class="title_2" width="15%">软件升级类型</TD>
		<TD width="35%">
			<select name="temp_id" class="bk">
				<option value="-1">==请选择==</option>
				<option value="1">普通软件升级</option>
				<option value="2">业务相关软件升级</option>
				<option value="3">非业务相关软件升级</option>
			</select>
		</TD>
	</TR>
	
	<% if (LipossGlobals.inArea("hn_lt")) { %>
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">所属平台</TD>
		<TD width="35%">
			<select name="platformId" id="platformId" class="bk">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
	</TR>
	
	
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">是否生效</TD>
		<TD width="35%">
			<select name="valid" class="bk">
				<option value="-1">==请选择==</option>
				<option value="1">生效</option>
				<option value="0">失效</option>
			</select>
		</TD>
	</TR>
	<% } %>
		
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:insertTemp();" 
				name="addButton" id="addButton" style="CURSOR:hand" style="display:" > 新 增 </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>