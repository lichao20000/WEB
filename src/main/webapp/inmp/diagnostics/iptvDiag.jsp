<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
<title> IPTV无法使用诊断 </title>
<script type="text/javascript">
<!--//
//业务类型，上网
var serv_type_id = "11";
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';
var gw_type = '1'; // 此诊断只有ITMS有，所以默认值为1

function clearDiv(){
	$("div[@id='divWireInfo']").html("");
	$("div[@id='divServParam']").html("");
	$("div[@id='divLanHostCheck']").html("");
}

function pass(strHtml){
/**
	if(strHtml.match("color") != null){
		if(strHtml.match("red") != null){
			//诊断不通过
			return false;
		}
	}**/
	return true;
}

function state_able(state){
	//诊断按钮失效
	document.getElementById("divStep").disabled = state;
	document.getElementById("divAuto").disabled = state;
	if(false == state){
		parent.unblock();
	}else{
		parent.block(); 
	}
}

//自动
function aotuDiag(){
	document.all("wireCheckbox").checked = true;
	document.all("servCheckbox").checked = true;
	document.all("lanHostCheckbox").checked = true;
	
	stepDiag();
}
//分步
function stepDiag(){
	if(document.getElementById("divStep").disabled == true){
		return false;
	}else{
		clearDiv();
		state_able(true);
		gatherWireInfo();
	}
}

//线路信息诊断
function gatherWireInfo(){
	$("div[@id='divWireInfo']").html("");
	if(document.all("wireCheckbox").checked == true){
		$("div[@id='divWireInfo']").html("正在获取...");
		var diagUrl = '<s:url value="/inmp/diagnostics/deviceDiagnostics!wireInfoCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			gw_type:gw_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divWireInfo']").html("");
			$("div[@id='divWireInfo']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				checkServParam();
			}else{
				state_able(false);
			}
		});
	}else{
		checkServParam();
	}
}

//业务参数
function checkServParam(){
	$("div[@id='divServParam']").html("");
	if(document.all("servCheckbox").checked == true){
		$("div[@id='divServParam']").html("正在获取中...");
		var diagUrl = '<s:url value="/inmp/diagnostics/deviceDiagnostics!servParamCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			gw_type:gw_type,
			servTypeId:serv_type_id
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divServParam']").html("");
			$("div[@id='divServParam']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				lanHostCheck();
			}else{
				state_able(false);
			}
		});
	}else{
		lanHostCheck();
	}
}

//PC连接状态诊断
function lanHostCheck(){
	$("div[@id='divLanHostCheck']").html("");
	if(document.all("lanHostCheckbox").checked == true){
		$("div[@id='divLanHostCheck']").html("正在获取中...");
		var diagUrl = '<s:url value="/inmp/diagnostics/deviceDiagnostics!iptvConnCheck.action"/>';
		$.post(diagUrl,{
			deviceId:device_id,
			gw_type:gw_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divLanHostCheck']").html("");
			$("div[@id='divLanHostCheck']").append(ajaxMesg);
			parent.dyniframesize();
			state_able(false);
		});
	}else{
		state_able(false);
	}
}

//链接业务下发
function servConfig(){
	var page = '<s:url value="../bss/ServiceDone.jsp?gw_type=1"/>';
	//var page = url + "?deviceId=" + device_id + "&sheetType=2";
	var height = 600;
	var width = 800;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="
			+height+",resizable=yes,scrollbars=yes,toolbar=no");
}

//-->
</script>
</head>
<body>
<form action="deviceDiagnostics">
<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center" height="auto">
	<tr><td bgcolor=#999999>
	<table border=0 cellspacing=1 cellpadding=2 width="100%" id="myTable" height="auto">
		<tr><td bgcolor=#ffffff>
			<table border=0 cellspacing=1 cellpadding=3 width="100%">
				<tr>
					<td colspan="4">IPTV无法使用诊断</td>
					<td align="right">
						<div id="divStep" onclick="javascript:stepDiag();" style="cursor:hand">【分步诊断】</div>
					</td>
					<td><div id="divAuto" onclick="javascript:aotuDiag();" style="cursor:hand">【自动诊断】</div></td>
					<td colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
			</table>
		</td></tr>
		<tr><td bgcolor=#ffffff>
		<table border=0 cellspacing=0 cellpadding=0 width="100%">
			<tr CLASS="green_foot">
				<td><input type="checkbox" name="wireCheckbox"> 链路检查结果</td>
			</tr>
			<tr>
				<td>
					<div id="divWireInfo"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td><input type="checkbox" name="servCheckbox"> 业务参数检查结果</td>
			</tr>
			<tr>
				<td>
					<div id="divServParam"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td><input type="checkbox" name="lanHostCheckbox"> IPTV连接检查</td>
			</tr>
			<tr>
				<td>
					<div id="divLanHostCheck"></div>
				</td>
			</tr>

		</table>
		</td></tr>
	</table>
	</td></tr>
</table>
</form>
</body>
</html>