<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<title> 有线无法上网诊断 </title>
<script type="text/javascript">
<!--//

//业务类型，上网
var serv_type_id = "10";
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';

var gw_type = '1';  // 此处默认为ITMS

var wire_type = '1';//有线

function clearDiv(){
	$("div[@id='divWireInfo']").html("");
	$("div[@id='divServParam']").html("");
	$("div[@id='divConnErrorCheck']").html("");
	$("div[@id='divPingCheck1']").html("");
	$("div[@id='divPingCheck2']").html("");
	$("div[@id='divDhcpCheck']").html("");
	$("div[@id='divLanHostCheck']").html("");
	$("div[@id='divDnsCheck']").html("");
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
	document.all("connErrorCheckbox").checked = true;
	document.all("pingCheckbox1").checked = true;
	document.all("pingCheckbox2").checked = true;
	document.all("lanHostCheckbox").checked = true;
	document.all("dhcpCheckbox").checked = true;
	document.all("dnsCheckbox").checked = true;
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

	if(document.all("wireCheckbox").checked == true){
		$("div[@id='divWireInfo']").html("正在获取...");
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!wireInfoCheck.action"/>';
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
	
	if(document.all("servCheckbox").checked == true){
		$("div[@id='divServParam']").html("正在获取中...");
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!servParamCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			servTypeId:serv_type_id,
			gw_type:gw_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divServParam']").html("");
			$("div[@id='divServParam']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				checkConnError();
			}else{
				state_able(false);
			}
		});
	}else{
		checkConnError();
	}
}
//拨号错误
function checkConnError(){
	if(document.all("connErrorCheckbox").checked == true){
		$("div[@id='divConnErrorCheck']").html("正在获取中...");
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!connErrorCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			gw_type:gw_type,
			servTypeId:serv_type_id
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divConnErrorCheck']").html("");
			$("div[@id='divConnErrorCheck']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				pingCheck("dnsAddress");
			}else{
				state_able(false);
			}
		});
	}else{
		pingCheck("dnsAddress");
	}
}
//pingDNS地址
function pingCheck(ping_addr_type){
	if(document.all("pingCheckbox1").checked == true){
		$("div[@id='divPingCheck1']").html("正在获取中...");
		var _pingAddrDns = $("input[@name='pingAddrDns']").val();
		var _packageSizeDns = $("input[@name='packageSizeDns']").val();
		var _pingTimesDns = $("input[@name='pingTimesDns']").val();
		var _timeOutDns = $("input[@name='timeOutDns']").val();
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!pingCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			servTypeId:serv_type_id,
			pingAddrType:ping_addr_type,
			pingAddrDns:_pingAddrDns,
			packageSizeDns:_packageSizeDns,
			pingTimesDns:_pingTimesDns,
			timeOutDns:_timeOutDns,
			gw_type:gw_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divPingCheck1']").html("");
			$("div[@id='divPingCheck1']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				pingSpecial();
			}else{
				state_able(false);
			}
		});
	}else{
		pingSpecial();
	}
}
//ping特定域名
function pingSpecial(){
	if(document.all("pingCheckbox2").checked == true){
		$("div[@id='divPingCheck2']").html("正在获取中...");
		var _pingAddr = $("input[@name='pingAddr']").val();
		var _packageSize = $("input[@name='packageSize']").val();
		var _pingTimes = $("input[@name='pingTimes']").val();
		var _timeOut = $("input[@name='timeOut']").val();
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!pingCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			servTypeId:serv_type_id,
			pingAddrType:"specialDomain",
			pingAddr:_pingAddr,
			packageSize:_packageSize,
			pingTimes:_pingTimes,
			timeOut:_timeOut,
			gw_type:gw_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divPingCheck2']").html("");
			$("div[@id='divPingCheck2']").append(ajaxMesg);
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
	if(document.all("lanHostCheckbox").checked == true){
		$("div[@id='divLanHostCheck']").html("正在获取中...");
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!lanHostCheck.action"/>';
		$.post(diagUrl,{
			deviceId:device_id,
			gw_type:gw_type,
			wireType:wire_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divLanHostCheck']").html("");
			$("div[@id='divLanHostCheck']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				dhcpCheck();
			}else{
				state_able(false);
			}
		});
	}else{
		dhcpCheck();
	}
}
//DHCP诊断
function dhcpCheck(){
	if(document.all("dhcpCheckbox").checked == true){
		$("div[@id='divDhcpCheck']").html("正在获取中...");
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!dhcpCheck.action"/>';
		$.post(diagUrl,{
			deviceId:device_id,
			gw_type:gw_type,
			wireType:wire_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divDhcpCheck']").html("");
			$("div[@id='divDhcpCheck']").append(ajaxMesg);
			parent.dyniframesize();
			if(true == pass(ajaxMesg)){
				dnsCheck();
			}else{
				state_able(false);
			}
		});
	}else{
		dnsCheck();
	}
}
//DNS诊断
function dnsCheck(){
	if(document.all("dnsCheckbox").checked == true){
		$("div[@id='divDnsCheck']").html("正在获取中...");
		var diagUrl = '<s:url value="/gwms/diagnostics/deviceDiagnostics!dnsCheck.action"/>';
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			gw_type:gw_type,
			servTypeId:serv_type_id
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divDnsCheck']").html("");
			$("div[@id='divDnsCheck']").append(ajaxMesg);
			parent.dyniframesize();
			state_able(false);
		});
	}else{
		state_able(false);
	}
}
//链接业务下发
function servConfig(){
	var page = '<s:url value="/gwms/config/ServiceDone.jsp?gw_type=1"/>';
	//var page = url + "?deviceId=" + device_id + "&sheetType=1";
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
	<table border=0 cellspacing=1 cellpadding=2 width="100%" height="auto">
		<tr><td bgcolor=#ffffff>
		<table border=0 cellspacing=1 cellpadding=3 width="100%">
			<tr>
				<td colspan="4">有线无法上网诊断&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td align="right">
					<div id="divStep" onclick="javascript:stepDiag();" style="cursor:hand">【分步诊断】</div>
				</td>
				<td><div id="divAuto" onclick="javascript:aotuDiag();" style="cursor:hand">【自动诊断】</div></td>
				<td colspan="4" align="right"><font color="#FF0000">注：只能在路由上网模式下使用</font></td>
			</tr>
		</table>
		</td></tr>
		<tr><td bgcolor=#ffffff>
		<table border=0 cellspacing=0 cellpadding=0 width="100%">
			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="wireCheckbox"> 链路检查结果</td>
			</tr>
			<tr>
				<td colspan="8">
					<div id="divWireInfo"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="servCheckbox"> 业务参数检查结果</td>
			</tr>
			<tr>
				<td colspan="8">
					<div id="divServParam"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="connErrorCheckbox"> 拨号检查结果</td>
			</tr>
			<tr>
				<td colspan="8">
					<div id="divConnErrorCheck"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="pingCheckbox1"> PING检查结果(DNS)</td>
			</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="right" width="10%">目的地址&nbsp;</td>
						<td width="20%">
							<input type="text" name="pingAddrDns" value="<s:property value="pingAddrDns"/>">&nbsp;
						</td>
						<td class=column align="right">包大小(字节)&nbsp;</td>
						<td>
							<input type="text" name="packageSizeDns" size=10 value="<s:property value="packageSizeDns"/>">
						</td>
						<td class=column align="right">次数&nbsp;</td>
						<td>
							<input type="text" name="pingTimesDns" size=10 value="<s:property value="pingTimesDns"/>">
						</td>
						<td class=column align="right">时延(毫秒)&nbsp;</td>
						<td>
							<input type="text" name="timeOutDns" size=10 value="<s:property value="timeOutDns"/>">
						</td>
					</tr>
			<tr>
				<td colspan="8">
					<div id="divPingCheck1"></div>
				</td>
			</tr>
			
			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="pingCheckbox2"> PING检查结果(域名)</td>
			</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="right" width="10%">目的地址&nbsp;</td>
						<td width="20%">
							<input type="text" name="pingAddr" value="<s:property value="pingAddr"/>">&nbsp;
						</td>
						<td class=column align="right" >包大小(字节)&nbsp;</td>
						<td>
							<input type="text" name="packageSize" size=10 value="<s:property value="packageSize"/>">
						</td>
						<td class=column align="right">次数&nbsp;</td>
						<td>
							<input type="text" name="pingTimes" size=10 value="<s:property value="pingTimes"/>">
						</td>
						<td class=column align="right">时延(毫秒)&nbsp;</td>
						<td>
							<input type="text" name="timeOut" size=10 value="<s:property value="timeOut"/>">
						</td>
					</tr>
			<tr>
				<td colspan="8">
					<div id="divPingCheck2"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="lanHostCheckbox"> PC连接检查</td>
			</tr>
			<tr>
				<td colspan="8">
					<div id="divLanHostCheck"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="dhcpCheckbox"> DHCP检查</td>
			</tr>
			<tr>
				<td colspan="8">
					<div id="divDhcpCheck"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="dnsCheckbox"> DNS检查</td>
			</tr>
			<tr>
				<td colspan="8">
					<div id="divDnsCheck"></div>
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