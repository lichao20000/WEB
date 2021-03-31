<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒版本文件修改</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<%
	request.setCharacterEncoding("GBK");
	String path_id = request.getParameter("path_id");
	String vendorName = request.getParameter("vendorName");
	String deviceModel = request.getParameter("deviceModel");
	String goalDeviceTypeName = request.getParameter("goalDeviceTypeName");
	String goal_devicetype_id = request.getParameter("goal_devicetype_id");
	String device_model_id = request.getParameter("device_model_id");
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
	<tr><td colspan="2" class="title_1" id="gwShare_thTitle">机顶盒版本文件修改</td></tr>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">厂 商</TD>
		<TD width="35%">
			<input type="hidden" name="pathId" value=<%=path_id%> >
			<input type="hidden" name="deviceModelId" value=<%=device_model_id%> >
			<input type="hidden" name="pathId" value=<%=path_id%> >
			<select name="vendorId" class="bk" disabled="disabled">
				<option value="-1" selected="selected"><%=vendorName%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" disabled="disabled">
				<option value=<%=device_model_id%> selected="selected"><%=deviceModel%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">目标版本</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk" disabled="disabled">
				<option value=<%=goal_devicetype_id%> selected="selected"><%=goalDeviceTypeName%></option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">升级路径</TD>
		<TD width="35%">
			<select name="filePath" class="bk">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>	
	</TR>
	<tr >
		<td colspan="2" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:updateTemp();" 
				name="addButton" style="CURSOR:hand" style="display:" > 修 改 </button>
				<button onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" > 重 置 </button>
			</div>
		</td>
	</tr>
</TABLE>
</body>

<SCRIPT LANGUAGE="JavaScript">

$(function(){
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!getPathByModelId.action'/>";
	$.post(url,{
		deviceModelId : deviceModelId
	},function(ajax){
		gwShare_parseMessage(ajax,$("select[@name='filePath']"),"-1");
	});
});
function updateTemp(){
	var pathId = $.trim($("input[@name='pathId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var pathIdNew = $.trim($("select[@name='filePath']").val());
	if ("-1" == pathIdNew){
		alert("请选择文件路径");
		return;
	}
	var url = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!modifyUpgradeFilePath.action'/>";
	$.post(url,{
		pathId : pathId,
		pathIdNew : pathIdNew,
		deviceModelId : deviceModelId,
		goal_devicetypeId : goal_devicetypeId 
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
</SCRIPT>