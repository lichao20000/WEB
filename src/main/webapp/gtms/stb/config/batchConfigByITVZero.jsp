<!DOCTYPE html PUBLIC "-//W3C//Dtd HTML 4.01 transitional//EN" "http://www.w3.org/tr/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
 			dyniframe[i].style.display="block"
 			//如果用户的浏览器是NetScape
 			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
  				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
  			//如果用户的浏览器是IE
 			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
  				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			 }
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
		tempobj.style.display="block"
		}
	}
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

//系统类型
var instAreaName = <%= LipossGlobals.getLipossProperty("InstArea.ShortName")%>;
//全选
function selectAllModel(checkOBJ){
	if(checkOBJ.checked){
		 $("input[@name='deviceModelId']").attr("checked",true); 
	}else{
		$("input[@name='deviceModelId']").attr("checked",false);
	}
}
/**
**根据厂商获取设备型号，并以复选框的形式表现出来
**/ 
function getdeviceModel(){
	var vendorId = $("select[@name='vendorId']").val();
	var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!getDeviceModel.action'/>";
	if(vendorId!="-1"){
		$("div[@id='adaptModelType']").html("");
		$.post(url,{
			vendorId:vendorId
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					var checkboxtxt = "<input type='checkbox' name='allDeviceModel' onclick='javascript:selectAllModel(this);getSoftVersion()'>全选 &nbsp;&nbsp;&nbsp;";
					$("div[@id='adaptModelType']").append(checkboxtxt);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						var checkboxtxt = "<input type='checkbox' name='deviceModelId' value='"+xValue+"' onclick='getSoftVersion()'>"+xText+"  ";
						$("div[@id='adaptModelType']").append(checkboxtxt);
					}
				}else{
					$("div[@id='adaptModelType']").append("该厂商没有对应型号！");
				}
			}else{
				$("div[@id='adaptModelType']").append("该厂商没有对应型号！");
			}
		});
	}else{
		$("div[@id='adaptModelType']").html("请选择厂商");
	}
}
//全选
function selectAllSoft(checkOBJ){
	if(checkOBJ.checked){
		 $("input[@name='deviceTypeId']").attr("checked",true); 
	}
	else{
		$("input[@name='deviceTypeId']").attr("checked",false);
	}
}
/**
**根据设备型号获取软件版本，并以复选框的形式表现出来
**/ 
function getSoftVersion(){
	var deviceModelIds ="";
	$("input[@name='deviceModelId'][@checked]").each(function(){ 
    	deviceModelIds += $(this).val()+",";
    });
	if(deviceModelIds!=""){
		deviceModelIds=deviceModelIds.substring(0,deviceModelIds.length-1);
	}
	var url = "<s:url value='/gtms/stb/config/batchCustomNodeConfig!getSoftVersion.action'/>";
	if(deviceModelIds!=""){
		$("div[@id='softVersion']").html("");
		$.post(url,{
			deviceModelIds : deviceModelIds
		},function(ajax){
			if(ajax!=""){
				var lineData = ajax.split("#");
				if(typeof(lineData)&&typeof(lineData.length)){
					var checkboxSoft ="<input type='checkbox' name='allSoftVersion' onclick='javascript:selectAllSoft(this)'>全选 &nbsp;&nbsp;&nbsp;";
					$("div[@id='softVersion']").append(checkboxSoft);
					for(var i=0;i<lineData.length;i++){
						var oneElement = lineData[i].split("$");
						var xValue = oneElement[0];
						var xText = oneElement[1];
						checkboxSoft = "<input type='checkbox' name='deviceTypeId' value='"+xValue+"'>"+xText+"  ";
						$("div[@id='softVersion']").append(checkboxSoft);
					}
				}else{
					$("div[@id='softVersion']").append("该型号没有对应软件版本！");
				}
			}else{
				$("div[@id='softVersion']").append("该型号没有对应软件版本！");
			}
		});
	}else{
		$("div[@id='softVersion']").html("请选择型号");
	}
}
//定制
function ExecMod(){
	//策略名
	var taskName =  $("#taskName").val();
	//属地
	var cityId = $("select[@name='cityId']").val();
	//厂商
	var vendorId = $("select[@name='vendorId']").val();
	//型号
	var deviceModelIds = "";
	//版本
	var deviceTypeIds ="";
	//参数节点路径
    var paramNodePath = ""; 
    //参数值
	var paramValue = "";
    //参数类型
	var paramType = "";
	$("input[@name='deviceModelId'][@checked]").each(function(){ 
    	deviceModelIds += $(this).val()+",";
    });
    $("input[@name='deviceTypeId'][@checked]").each(function(){ 
    	deviceTypeIds += $(this).val()+",";
    });
	if("" == trim(taskName)){
		alert("策略名不能为空！");
		return false; 
	} 
	if("" == cityId || "-1" == cityId){
		alert("请选择属地");
		return false;
	}
    if(deviceModelIds==""){
		alert("请选择适用型号!");
		return false;
	}
    if(deviceModelIds.length > 0){
    	$("input[@name='deviceModelIds']").attr("value",deviceModelIds.substring(0,deviceModelIds.length-1));
    }
    if(deviceTypeIds==""){
		alert("请选择软件版本!");
		return false;
	}
    if(deviceTypeIds.length>0){
    	$("input[@name='deviceTypeIds']").attr("value",deviceTypeIds.substring(0,deviceTypeIds.length-1));
    }
    if(!checkForm()){
    	return false;
    }
	//遍历配置	
    for(var i = 0; i <= 3; i++){
		paramNodePath += "," + $("#paramNodePath" + i).val();
		paramValue += "," + $("#paramValue" + i).val();
		paramType += "," + $("#paramType" + i).val();
    }
    $("input[@name='paramNodePath']").attr("value",paramNodePath);
 	$("input[@name='paramValue']").attr("value",paramValue);
	$("input[@name='paramType']").attr("value",paramType);
	$("form[@name='batchexform']").attr("action","batchCustomNodeConfig!doConfigByITV.action");
	$("form[@name='batchexform']").submit();
	
	$("#ExecModBtn").attr("disabled", true);
	setTimeout("disableOK()",2000);
}

function disableOK(){
	$("#ExecModBtn").attr("disabled","");
}

function checkForm(){
	var result = true;
	for(var i = 0; i <= 3; i++){
		//遍历配置
		var tempPath = $("#paramNodePath" + i).val();
		var tempValue = $("#paramValue" + i).val();
		var tempType = $("#paramType" + i).val();
		if(tempPath == ""){
			alert("第" + (i+1) + "个参数节点路径为空！");
			result = false;
			return false;
		}
		else if(tempValue == ""){
			alert("第" + (i + 1) + "个参数值为空！");
			result = false;
			return false;
		}
		else if(tempType == "-1"){
			alert("第" + ( i + 1) + "个参数类型为空！");
			result = false;
			return false;
		}
	}
	return result;
}
function softdivcl(){
	$("div[@id='div_css']").hide();
}
//去掉空格
function trim(str){
    	return str.replace(/(^\s*)|(\s*$)/g,"");
}
</script>
</head>
<body>
	<table width="98%" align="center" class="querytable">
		<tr>
			<td>
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
				您当前的位置：ITV零配置下发节点
			</td>
		</tr>
	</table>
	<br>
	<s:form action="batchCustomNodeConfig!initBatchCfgByITV.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="" target="dataForm">
		<input type="hidden" name="deviceModelIds" value=""/>
		<input type="hidden" name="paramNodePath" value="">
		<input type="hidden" name="paramValue" value="">
		<input type="hidden" name="paramType" value="">
		<input type="hidden" name="deviceTypeIds" value="">
		<input type="hidden" name="macSG" value="">
		<input type="hidden" name="ipSG"   value="">
		<input type="hidden" name="ipCheck" value="">
		<input type="hidden" name="macCheck" value="">
		<input type="hidden" name="btnValue4MAC" value="">
		<input type="hidden" name="btnValue4IP" value="">
		<table class="querytable" width="98%" align="center">
			<tr>
				<td colspan="4" class="title_1">
					ITV零配置下发节点
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">任务名称</td>
				<td colspan="3"> <input type="text" id="taskName" name="taskName" width="500"> </td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					属地
				</td>
			<td width="85%" colspan="3">
					<s:select list="cityList" name="cityId" headerKey="-1"
						headerValue="请选择属地" listKey="city_id" listValue="city_name"
						value="cityId" cssClass="bk" theme="simple"></s:select>
				</td>
		    </tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					厂商
				</td>
				<td width="85%" colspan="3">
					<s:select list="vendorList" name="vendorId" headerKey="-1"
						headerValue="请选择厂商" listKey="vendor_id" listValue="vendor_add"
						value="vendorId" cssClass="bk" onchange="getdeviceModel();"
						theme="simple">
				</s:select>
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					型号
				</td>
				<td width="85%" colspan="3">
					<div id="adaptModelType">
						 请选择厂商
					</div>
				</td>
			</tr>
			<tr>
				<td class="title_2" align="center" width="15%">
					软件版本
				</td>
				<td width="85%" colspan="3">
					<div id="softVersion">
						  请选择型号
					</div>
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数节点路径
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数值
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue0" name="paramValue0" value=""/>
				</td>
				<td  class="title_2" width="15%">
					参数类型
				</td>
				<td  width="35%">
					<select name="paramType0" id="paramType0" class="bk">
					 	<option value="-1">==请选择==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数节点路径
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath1" name="paramNodePath1" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数值
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue1" name="paramValue1" value=""/>
				</td>
				<td  class="title_2" width="15%">
					参数类型
				</td>
				<td width="35%" >
					<select name="paramType1" id="paramType1" class="bk">
					 	<option value="-1">==请选择==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					 </select>
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数节点路径
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath2" name="paramNodePath2" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数值
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue2" name="paramValue2" value=""/>
				</td>
				<td  class="title_2" width="15%">
					参数类型
				</td>
				<td width="35%" >
					<select name="paramType2" id="paramType2" class="bk">
					 	<option value="-1">==请选择==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					</select>
				</td>
			</tr>
			
			<tr>
				<td width="15%" class="title_2">
					参数节点路径
				</td>
				<td width="85%" colspan="3" >
					<input type="text" id="paramNodePath3" name="paramNodePath3" style="width:800px;"> 
				</td>
			</tr>
			<tr>
				<td width="15%" class="title_2">
					参数值
				</td>
				<td  width="35%" >
					<input type="text" id="paramValue3" name="paramValue3" value=""/>
				</td>
				<td  class="title_2" width="15%">
					参数类型
				</td>
				<td width="35%" >
					<select name="paramType3" id="paramType3" class="bk">
					 	<option value="-1">==请选择==</option>
					 	<option value="1">string</option>
					 	<option value="2">int</option>
					 	<option value="3">unsignedInt</option>
					 	<option value="4">boolean</option>
					</select>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button onclick="ExecMod()" id="ExecModBtn">
							下发
						</button>
					</div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td colspan="4" align="left" class="foot">
					<div id="resultDIV" ></div>
				</td>
			</tr>
		</table>
	</s:form>
	<div>
		<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
		scrolling="no" width="100%" src=""></iframe>
	</div>
	<br>  
	<br>
	<div id="divDetail"
		style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
	<div id="div_css" style="position: absolute; z-index: 255; top: 200px; left: 250px; width: 55%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
		<table class="querytable" align="center" width="100%">
			<tr>
				<td width="30%" id="ventd" class="title_2" align="center"> 
				</td>
				<td id="softVershow" class="title_2" align="center">	
				</td>
			</tr>
			<tr>
				<td colspan="2" class="title_2" align="center">
					<button name="softdivtbn" onclick="softdivcl()">
						关闭
					</button>
				</td>
			</tr>
		</table>				
	</div>
</body>
</html>