<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>入网终端台账查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>"  rel="stylesheet"  type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	change_select("vendor","-1");
});

function addFilePath(){
	var strpage = "<s:url value='/itms/resource/deviceTestAccountAdd.jsp'/>?operateType=add";
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
function change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='hardVersion']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='hardVersion']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				$("select[@name='hardVersion']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			var url = "<s:url value="/itms/resource/deviceTestAccountACT!getDeviceHardVersion.action"/>";
			//var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			
			if("-1"==deviceModelId){
					$("select[@name='hardVersion']").html("<option value='-1'>==请先选择设备型号==</option>");
				
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				//if(instArea=="hn_lt"){
					gwShare_parseMessage(ajax,$("select[@name='hardVersion']"),selectvalue);
				/* }else{
					gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
				} */
			});
			break;	
		case "soft_version":
			var url = "<s:url value="/itms/resource/deviceTestAccountACT!getSoftVersion.action"/>";
			var hardVersion = $("select[@name='hardVersion']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==hardVersion){
				$("select[@name='goal_devicetypeId']").html("<option value='-1'>==请先选择硬件版本==</option>");
				break;
			}
			
			$.post(url,{
				gwShare_hardwareVersion:hardVersion,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='goal_devicetypeId']"),selectvalue);
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

function getList(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/itms/resource/deviceTestAccountACT!getList.action'/>";
	frm.submit();
}

function trim(str){
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
<form name="frm" id="frm" method="post" target="dataForm">
<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
<TABLE width="98%" class="querytable" align="center">
	<tr>
		<td colspan="4" class="title_1">入网终端台账查询</td>
	</tr>
	<TR>
		<TD align="right" class="title_2" width="15%">厂 商</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="deviceModelId" class="bk" onchange="change_select('devicetype','-1')">
				<option value="-1">请先选择厂商</option>
			</select>
		</TD>
	</TR>
	<TR>	
		<TD align="right" class="title_2" width="15%">硬件版本</TD>
		<TD width="35%">
			<select name="hardVersion" class="bk" onchange="change_select('soft_version','-1')">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">软件版本</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk"">
				<option value="-1">请先选硬件版本</option>
			</select>
		</TD>
	</TR>
	<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value="starttime"/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly value="<s:property value="endtime"/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
					</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<input type="button" onclick="javascript:getList();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" class="jianbian" value="查 询" > 
				<input type="button" onclick="javascript:addFilePath();" 
				name="addFilePath_jx" style="CURSOR:hand" style="display:" class="jianbian" value="新 增" > 
			</div>
		</td>
	</tr>
</TABLE>
</form>
<br>
<div>
	<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
</div>
</body>