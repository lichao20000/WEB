<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
<title> IPTV�޷�ʹ����� </title>
<script type="text/javascript">
<!--//
//ҵ�����ͣ�����
var serv_type_id = "11";
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';
var gw_type = '1'; // �����ֻ��ITMS�У�����Ĭ��ֵΪ1

function clearDiv(){
	$("div[@id='divWireInfo']").html("");
	$("div[@id='divServParam']").html("");
	$("div[@id='divLanHostCheck']").html("");
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
	document.all("servCheckbox").checked = true;
	document.all("lanHostCheckbox").checked = true;
	
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
		var diagUrl = '<s:url value="/inmp/diagnostics/deviceDiagnostics!wireInfoCheck.action"/>';
		//��ѯ
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

//ҵ�����
function checkServParam(){
	$("div[@id='divServParam']").html("");
	if(document.all("servCheckbox").checked == true){
		$("div[@id='divServParam']").html("���ڻ�ȡ��...");
		var diagUrl = '<s:url value="/inmp/diagnostics/deviceDiagnostics!servParamCheck.action"/>';
		//��ѯ
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

//PC����״̬���
function lanHostCheck(){
	$("div[@id='divLanHostCheck']").html("");
	if(document.all("lanHostCheckbox").checked == true){
		$("div[@id='divLanHostCheck']").html("���ڻ�ȡ��...");
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

//����ҵ���·�
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
					<td colspan="4">IPTV�޷�ʹ�����</td>
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
				<td><input type="checkbox" name="wireCheckbox"> ��·�����</td>
			</tr>
			<tr>
				<td>
					<div id="divWireInfo"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td><input type="checkbox" name="servCheckbox"> ҵ����������</td>
			</tr>
			<tr>
				<td>
					<div id="divServParam"></div>
				</td>
			</tr>

			<tr CLASS="green_foot">
				<td><input type="checkbox" name="lanHostCheckbox"> IPTV���Ӽ��</td>
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