<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>机顶盒升级版本配置</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>"  rel="stylesheet"  type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<%-- <script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script> --%>
<SCRIPT LANGUAGE="JavaScript">
//系统类型
var instAreaName = '<%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
$(function(){
	change_select("vendor","-1");
	if("hn_lt"==instAreaName){
		intPlatformType();
	}
});

//软件版本改变触发
function intPlatformType(){
	
	var url = "<s:url value='/gtms/stb/resource/PreMacDeviceACT!getPlatformS.action'/>";
	
	$.post(url,{},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					$("select[@id='platformId']").empty();
					var optionValue = "<option value='-1' >请选择平台类型</option>";
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
					var optionValue = "<option value='-1' >请选择平台类型</option>";
					$("select[@id='platformId']").append(optionValue);
				}
			}else{
				$("select[@id='platformId']").empty();
				var optionValue = "<option value='-1' >请选择平台类型</option>";
				$("select[@id='platformId']").append(optionValue);
			}
		});
	
}
/*------------------------------------------------------------------------------
//函数名:		deviceSelect_change_select
//参数  :	type 
	            vendor      加载设备厂商
	            deviceModel 加载设备型号
	            devicetype  加载设备版本
//功能  :	加载页面项（厂商、型号级联）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function change_select(type,selectvalue){
	switch (type){
		case "platform":
			var url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
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

/*------------------------------------------------------------------------------
//函数名:		deviceSelect_parseMessage
//参数  :	ajax 
            	类似于XXX$XXX#XXX$XXX
            field
            	需要加载的jquery对象
//功能  :	解析ajax返回参数
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
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

function getTempList(){
	var vendorId = $.trim($("select[@name='vendorId']").val());
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());
	var source_devicetypeId = $.trim($("select[@name='source_devicetypeId']").val());
	var goal_devicetypeId = $.trim($("select[@name='goal_devicetypeId']").val());
	var temp_id = $.trim($("select[@name='temp_id']").val());
	var hardwareversion = $.trim($("select[@name='hardwareversion']").val());
	var platformId = $.trim($("select[@name='platformId']").val());
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbUpgradeVersion!getStbUpgradeTempList.action'/>";
	frm.submit();
}

function addTemp()
{
	var strpage = "<s:url value='/gtms/stb/resource/stbUpgradeVersionAdd.jsp'/>?operateType=add";
	window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
/*------------------------------------------------------------------------------
//函数名:		trim
//参数  :	str 待检查的字符串
//功能  :	根据传入的参数进行去掉左右的空格
//返回值:		trim（str）
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
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
<form name="frm" id="frm" target="dataForm" method="post">

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">机顶盒升级版本查询</td></tr>
	<TR id="gwShare_tr22" >
		<TD align="right" class="title_2" width="15%">厂    商</TD>
		<TD width="35%">
			<select name="vendorId" class="bk" onchange="change_select('deviceModel','-1')">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
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
	<%if (LipossGlobals.inArea("hn_lt")) {%>
		<TR>	
			<TD align="right" class="title_2" width="15%">硬件版本</TD>
			<TD width="35%">
				<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
					<option value="-1">请先选择设备型号</option>
				</select>
			</TD>	
			<TD align="right" class="title_2" width="15%">所属平台</TD>
			<TD width="35%">
				<select name="platformId" id="platformId" class="bk">
					<option value="-1">==请选择==</option>
				</select>
			</TD>
		</TR>	
	<%
		}
	%>
	<%if (LipossGlobals.inArea("xj_dx")) {%>
		<TR>	
			<TD align="right" class="title_2" width="15%">硬件版本</TD>
			<TD width="35%">
				<select name="hardwareversion" class="bk" onchange="change_select('softwareversion','-1')">
					<option value="-1">请先选择设备型号</option>
				</select>
			</TD>	
			<TD align="right" class="title_2" width="15%"></TD>
			<TD width="35%"></TD>
		</TR>
	
	<%
		}
	%>
	
	<TR id="gwShare_tr23" >	
		<TD align="right" class="title_2" width="15%">原版本</TD>
		<TD width="35%">
			<select name="source_devicetypeId" class="bk">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>	
		<TD align="right" class="title_2" width="15%">目标版本</TD>
		<TD width="35%">
			<select name="goal_devicetypeId" class="bk">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr24" >
		<% if (!LipossGlobals.inArea("jx_dx")) { %>
		<TD align="right" class="title_2" width="15%">软件升级类型</TD>
		<TD width="35%">
			<select name="temp_id" class="bk">
				<option value="-1">==请选择==</option>
				<option value="1" selected="selected">普通软件升级</option>
				<option value="2">业务相关软件升级</option>
				<option value="3">非业务相关软件升级</option>
			</select>
		</TD>
		<% } %>
		<% if (LipossGlobals.inArea("hn_lt")) { %>
		<TD align="right" class="title_2" width="15%">是否生效</TD>
		<TD width="35%">
			<select name="valid" class="bk">
				<option value="-1">全部</option>
				<option value="1">生效</option>
				<option value="0">失效</option>
			</select>
		</TD>
		<% }else{ %>
		<TD align="right" class="title_2" width="15%"></TD>
		<TD width="35%"></TD>
		<% } %>
	</TR>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<% if (LipossGlobals.inArea("jx_dx")|| LipossGlobals.inArea("xj_dx")) { %>
				<input type="button" onclick="javascript:addTemp();" 
				name="addTemp_Config" style="CURSOR:hand" style="display:" class="jianbian" value="新 增">
				<%} %>
				<input type="button" onclick="javascript:getTempList();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" class="jianbian" value="查 询">
				<input type="button" onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" class="jianbian" value="重 置"> 
			</div>
		</td>
	</tr>
</TABLE>
</form>

<div class="content">
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
			scrolling="no" width="100%" src=""></iframe>
	</div>

</body>