<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<title> �쳣���߶���� </title>
<script type="text/javascript">
<!--//
//ҵ�����ͣ�����
var serv_type_id = '<s:property value="servTypeId"/>';
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';

function clearDiv(){
	$("div[@id='divWireInfo']").html("");
	$("div[@id='divPingCheck1']").html("");
	$("div[@id='divPingCheck2']").html("");
	$("div[@id='divPingCheck']").html("");
}

function pass(strHtml){
/**
	if(strHtml.match("color") != null){
		if(strHtml.match("red") != null){
			//��ϲ�ͨ��
			return false;
		}
	}**/
	return true;
}

function state_able(state){
	//��ϰ�ťʧЧ
	document.getElementById("divStep").disabled = state;
	document.getElementById("divAuto").disabled = state;
	if(false == state){
		parent.unblock();
	}else{
		parent.block(); 
	}
}

//�Զ�
function aotuDiag(){
	document.all("wireCheckbox").checked = true;
	document.all("pingCheckbox1").checked = true;
	document.all("pingCheckbox2").checked = true;
	document.all("pingCheckbox").checked = true;
	
	stepDiag();
}
//�ֲ�
function stepDiag(){
	if(document.getElementById("divStep").disabled == true){
		return false;
	}else{
		clearDiv();
		state_able(true);
		gatherWireInfo();
	}
}

//��·��Ϣ���
function gatherWireInfo(){
	$("div[@id='divWireInfo']").html("");
	if(document.all("wireCheckbox").checked == true){
		$("div[@id='divWireInfo']").html("���ڻ�ȡ...");
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!wireInfoCheck.action"/>';
		//��ѯ
		$.post(diagUrl,{
			deviceId:device_id
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divWireInfo']").html("");
			$("div[@id='divWireInfo']").append(ajaxMesg);
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

//pingDNS��ַ
function pingCheck(ping_addr_type){
	if(document.all("pingCheckbox1").checked == true){
		$("div[@id='divPingCheck1']").html("���ڻ�ȡ��...");
		var _pingAddrDns = $("input[@name='pingAddrDns']").val();
		var _packageSizeDns = $("input[@name='packageSizeDns']").val();
		var _pingTimesDns = $("input[@name='pingTimesDns']").val();
		var _timeOutDns = $("input[@name='timeOutDns']").val();
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!pingCheck.action"/>';
		//��ѯ
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
//ping�ض�����
function pingSpecial(){
	if(document.all("pingCheckbox2").checked == true){
		$("div[@id='divPingCheck2']").html("���ڻ�ȡ��...");
		var _pingAddr = $("input[@name='pingAddr']").val();
		var _packageSize = $("input[@name='packageSize']").val();
		var _pingTimes = $("input[@name='pingTimes']").val();
		var _timeOut = $("input[@name='timeOut']").val();
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!pingCheck.action"/>';
		//��ѯ
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
				pingLan();
			}else{
				state_able(false);
			}
		});
	}else{
		pingLan();
	}
}
//LAN��ping
function pingLan(){
	if(document.all("pingCheckbox").checked == true){
		$("div[@id='divPingCheck']").html("���ڻ�ȡ��...");
		var _pingAddrLan = $("input[@name='pingAddrLan']").val();
		var _packageSizeLan = $("input[@name='packageSizeLan']").val();
		var _pingTimesLan = $("input[@name='pingTimesLan']").val();
		var _timeOutLan = $("input[@name='timeOutLan']").val();
		var diagUrl = '<s:url value="/bbms/diag/deviceDiag!pingCheck.action"/>';
		//��ѯ
		$.post(diagUrl,{
			deviceId:device_id,
			pingAddrType:"lanPing",
			pingAddrLan:_pingAddrLan,
			packageSizeLan:_packageSizeLan,
			pingTimesLan:_pingTimesLan,
			timeOutLan:_timeOutLan
		},function(ajaxMesg){
			//alert(ajaxMesg);
			$("div[@id='divPingCheck']").html("");
			$("div[@id='divPingCheck']").append(ajaxMesg);
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
<form action="deviceDiagnostics">
<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center" height="auto">
	<tr><td bgcolor=#999999>
	<table border=0 cellspacing=1 cellpadding=2 width="100%" id="myTable" height="auto">
		<tr><td bgcolor=#ffffff>
		<table border=0 cellspacing=1 cellpadding=3 width="100%">
			<tr>
				<td colspan="4">�쳣���߶����</td>
				<td align="right">
					<div id="divStep" onclick="javascript:stepDiag();" style="cursor:hand">���ֲ���ϡ�</div>
				</td>
				<td><div id="divAuto" onclick="javascript:aotuDiag();" style="cursor:hand">���Զ���ϡ�</div></td>
				<td colspan="4">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td></tr>
		<tr><td bgcolor=#ffffff>
			<table border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr CLASS="green_foot">
					<td colspan="8"><input type="checkbox" name="wireCheckbox"> ��·�����</td>
				</tr>
				<tr>
					<td colspan="8">
						<div id="divWireInfo"></div>
					</td>
				</tr>
	
				<tr CLASS="green_foot">
				<td colspan="8"><input type="checkbox" name="pingCheckbox1"> PING�����(DNS)</td>
			</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="right" width="10%">Ŀ�ĵ�ַ&nbsp;</td>
						<td width="20%">
							<input type="text" name="pingAddrDns" value="<s:property value="pingAddrDns"/>">&nbsp;
						</td>
						<td class=column align="right">����С(�ֽ�)&nbsp;</td>
						<td>
							<input type="text" name="packageSizeDns" size=10 value="<s:property value="packageSizeDns"/>">
						</td>
						<td class=column align="right">����&nbsp;</td>
						<td>
							<input type="text" name="pingTimesDns" size=10 value="<s:property value="pingTimesDns"/>">
						</td>
						<td class=column align="right">ʱ��(����)&nbsp;</td>
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
					<td colspan="8"><input type="checkbox" name="pingCheckbox2"> PING�����(����)</td>
				</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="right" width="10%">Ŀ�ĵ�ַ&nbsp;</td>
						<td width="20%">
							<input type="text" name="pingAddr" value="<s:property value="pingAddr"/>">&nbsp;
						</td>
						<td class=column align="right" >����С(�ֽ�)&nbsp;</td>
						<td>
							<input type="text" name="packageSize" size=10 value="<s:property value="packageSize"/>">
						</td>
						<td class=column align="right">����&nbsp;</td>
						<td>
							<input type="text" name="pingTimes" size=10 value="<s:property value="pingTimes"/>">
						</td>
						<td class=column align="right">ʱ��(����)&nbsp;</td>
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
					<td colspan="8"><input type="checkbox" name="pingCheckbox"> LAN��PING���Խ��</td>
				</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="right" width="10%">Ŀ�ĵ�ַ&nbsp;</td>
						<td width="20%">
							<input type="text" name="pingAddrLan" value="192.168.1.2">&nbsp;
						</td>
						<td class=column align="right">����С(�ֽ�)&nbsp;</td>
						<td>
							<input type="text" name="packageSizeLan" size=10 value="<s:property value="packageSizeLan"/>">
						</td>
						<td class=column align="right">����&nbsp;</td>
						<td>
							<input type="text" name="pingTimesLan" size=10 value="<s:property value="pingTimesLan"/>">
						</td>
						<td class=column align="right">ʱ��(����)&nbsp;</td>
						<td>
							<input type="text" name="timeOutLan" size=10 value="<s:property value="timeOutLan"/>">
						</td>
					</tr>
				<tr>
					<td colspan="8">
						<div id="divPingCheck"></div>
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