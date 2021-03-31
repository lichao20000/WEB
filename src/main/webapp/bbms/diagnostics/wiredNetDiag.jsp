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
var serv_type_id = '<s:property value="servTypeId"/>';
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';
var wire_type = '1';//有线

function clearDiv(){
	$("div[@id='divWireInfo']").html("");
	$("div[@id='divServParam']").html("");
	$("div[@id='divPingCheck1']").html("");
	$("div[@id='divPingCheck2']").html("");
	$("div[@id='divDhcpCheck']").html("");
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
	document.all("pingCheckbox1").checked = true;
	document.all("pingCheckbox2").checked = true;
	document.all("lanHostCheckbox").checked = true;
	document.all("dhcpCheckbox").checked = true;
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
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!wireInfoCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id
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
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!servParamCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			servTypeId:serv_type_id
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divServParam']").html("");
			$("div[@id='divServParam']").append(ajaxMesg);
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
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!pingCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			servTypeId:serv_type_id,
			pingAddrType:ping_addr_type,
			pingAddrDns:_pingAddrDns,
			packageSizeDns:_packageSizeDns,
			pingTimesDns:_pingTimesDns,
			timeOutDns:_timeOutDns
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
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!pingCheck.action"/>';
		//查询
		$.post(diagUrl,{
			deviceId:device_id,
			userId:user_id,
			servTypeId:serv_type_id,
			pingAddrType:"specialDomain",
			pingAddr:_pingAddr,
			packageSize:_packageSize,
			pingTimes:_pingTimes,
			timeOut:_timeOut
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
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!lanHostCheck.action"/>';
		$.post(diagUrl,{
			deviceId:device_id,
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
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!dhcpCheck.action"/>';
		$.post(diagUrl,{
			deviceId:device_id,
			wireType:wire_type
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divDhcpCheck']").html("");
			$("div[@id='divDhcpCheck']").append(ajaxMesg);
			parent.dyniframesize();
			state_able(false);
		});
	}else{
		state_able(false);
	}
}

//-->
</script>
</head>
<body>
<form action="deviceDiag">
<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center" height="auto">
	<tr><td bgcolor=#999999>
	<table border=0 cellspacing=1 cellpadding=2 width="100%" height="auto">
		<tr><td bgcolor=#ffffff>
		<table border=0 cellspacing=1 cellpadding=3 width="100%">
			<tr>
				<td colspan="4">有线无法上网诊断</td>
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

		</table>
		</td></tr>
	</table>
	</td></tr>
</table>
</form>
</body>
</html>