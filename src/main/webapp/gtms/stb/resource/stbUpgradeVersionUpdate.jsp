<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒升级版本修改</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<%
	request.setCharacterEncoding("GBK");
	String devicetype_id_old = request.getParameter("devicetype_id_old");
	String vendorName = request.getParameter("vendorName");
	String deviceModel = request.getParameter("deviceModel");
	String sourceDeviceTypeName = request.getParameter("sourceDeviceTypeName");
	String goalDeviceTypeName = request.getParameter("goalDeviceTypeName");
	String tempName = request.getParameter("tempName");
	tempName=java.net.URLDecoder.decode(tempName,"utf-8");
	String device_model_id = request.getParameter("device_model_id");
	String belongName = request.getParameter("belongName");
	String hardwareversion = request.getParameter("hardwareversion");
	String belong = request.getParameter("belong");
	String valid = request.getParameter("valid");
%>

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
	<tr><td colspan="2" class="title_1"  >机顶盒升级版本修改</td></tr>
	<TR >
		<TD align="right" class="title_2" width="15%">厂    商</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" disabled="disabled">
				<option value="-1" selected="selected"><%=vendorName%></option>
			</select>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" disabled="disabled">
				<option value=<%=device_model_id%> selected="selected"><%=deviceModel%></option>
			</select>
		</TD>
	</TR>
	<% if (LipossGlobals.inArea("hn_lt")||LipossGlobals.inArea("xj_dx")) { %>
		<TR>
			<TD align="right" class="title_2" width="15%">硬件版本</TD>
			<TD width="35%">
				<select name="belong" class="bk" disabled="disabled">
					<option value="1" selected="selected"><%=hardwareversion%></option>
				</select>
			</TD>
		</TR>
	<% } %>
	<TR>	
		<TD align="right" class="title_2" width="15%">原版本</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk" disabled="disabled">
				<option value=<%=devicetype_id_old%> selected="selected"><%=sourceDeviceTypeName%></option>
			</select>
		</TD>	
	</TR>
	<TR >	
		<TD align="right" class="title_2" width="15%">目标版本</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value=<%=devicetype_id_old%> selected="selected"><%=goalDeviceTypeName%></option>
			</select>
		</TD>
	</TR>
	<TR >
		<TD align="right" class="title_2" width="15%">软件升级类型</TD>
		<TD width="35%">
			<select name="temp_id" class="bk" disabled="disabled">
				<option value="1" selected="selected"><%=tempName%></option>
			</select>
		</TD>
	</TR>
	<% if (LipossGlobals.inArea("hn_lt")) { %>
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">所属平台</TD>
		<TD width="35%">
			<select name="platformId" id="platformId" class="bk">
				<option value=<%=belong%> selected="selected"><%=belongName%></option>
			</select>
		</TD>
	</TR>
	<TR id="tr_belong">
		<TD align="right" class="title_2" width="15%">是否生效</TD>
		<TD width="35%">
			<select name="valid" class="bk">
			<% if ("生效".equals(valid)) { %>
				<option value="1" selected="selected">生效</option>
				<option value="0">失效</option>
			<% }else{ %>
				<option value="0" selected="selected">失效</option>
				<option value="1">生效</option>
			<% } %>
			</select>
		</TD>
	</TR>
	<% } %>
	
	
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:updateTemp();" 
				name="addButton" style="CURSOR:hand" style="display:" > 修 改 </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>

<SCRIPT LANGUAGE="JavaScript">
//系统类型
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
$(function(){
	if("hn_lt"==instAreaName){
		$("tr[id=tr_belong]").attr("style","display: display"); 
		intPlatformType();
	}else{
		$("tr[id=tr_belong]").attr("style","display: none"); 
	}
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getDevicetype.action'/>";
	$.post(url,{
		gwShare_deviceModelId:deviceModelId
	},function(ajax){
		gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),<%=devicetype_id_old%>);
	});
});
function updateTemp(){
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var platformId = $.trim($("select[@name='platformId']").val());
	var valid = $.trim($("select[@name='valid']").val());
	
	if(source_devicetypeId == goal_devicetypeId){
		alert("原始版本与目标版本一致，请重新选择！");
		return;
	}
	if("-1" == goal_devicetypeId){
		alert("请选择目标版本！");
		return;
	}
	if("-1" == platformId){
		alert("请选择所属平台！");
		return;
	}
	if("-1" == valid){
		alert("请选择是否生效！");
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!modifyUpgradeTemp.action'/>";
	$.post(url,{
		source_devicetypeId : source_devicetypeId,
		platformId : platformId,
		goal_devicetypeId : goal_devicetypeId,
		valid:valid
	},function(ajax){
		if("1"== ajax){
			alert("修改成功！");
		}else{
			alert("修改失败！");
		}
		window.close();
	});
}

function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
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

function intPlatformType(){
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getPlatformS.action'/>";
	
	$.post(url,{},function(ajax){
			if(ajax!=""){
				gwShare_parseMessage(ajax,$("select[@name='platformId']"),<%=belong%>);
			}else{
				$("select[@id='platformId']").empty();
				var optionValue = "<option value='0' >请选择平台类型</option>";
				$("select[@id='platformId']").append(optionValue);
			}
		});
	
}
</SCRIPT>