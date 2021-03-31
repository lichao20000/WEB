<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ include file="../../timelater.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<%@ include file="../head.jsp"%>
<title>QoSҵ������</title>
<script type="text/javascript">
<!--//
var device_id = '<s:property value="deviceId"/>';
var gw_type = '<s:property value="gw_type"/>';
var temp_id = '0';
tempId:temp_id

$(function(){
	parent.unblock();

	getQos();
});

//get data
function getQos() {

	var modeIptv = $("input[@name='modeIptv']");
	var modeVoip = $("input[@name='modeVoip']");

	$("div[@id='operResult']").html("���ڲɼ�...");
	if (modeIptv.attr("type")=="checkbox" && "1" == '<s:property value="modeIptv"/>') {
		modeIptv.attr("checked", true);
	}

	if (modeVoip.attr("type")=="checkbox" && "1" == '<s:property value="modeVoip"/>') {
		modeVoip.attr("checked", true);
	}	
	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("���ݲɼ�:");
	$("div[@id='operResult']").html("<s:property value='ajax'/>");

	setTimeout("clearResult()", 3000);
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}

//�ύ
function checkForm(){
	var modeIptv = $("input[@name='modeIptv']");
	var modeVoip = $("input[@name='modeVoip']");
	//var modeInternetValue = "";
	//var modeTr069Value = "";
	var qosModeValue = "";

	if(modeIptv.attr("type")=="checkbox" && modeVoip.attr("type")=="checkbox"){
		if (modeIptv.attr("checked") == true) {
			modeIptv.val("1");
		} else {
			modeIptv.val("0");
		}
		if (modeVoip.attr("checked") == true) {
			modeVoip.val("1");
		} else {
			modeVoip.val("0");
		}
	}
	else{
		var reg = /^[1-4]?$/;		
		var result = "";
		var modeArray=new Array(4);
		var modeReadyArray = new Array(4);
		$("input[@class='modeClass']").each(function(index){
			var currVal = $.trim($(this).val());
			if(!(reg.test(currVal))){
				alert("���ȼ�˳�����Ϊ1~4���������");
				result = "error";
				$(this).focus();
				return false;
			}
			if(arrIndexOf(modeArray,currVal) != -1 && currVal != ""){
				alert("���ȼ�˳������ͬ");
				result = "error";
				$(this).focus();
				return false;
			}
			if(currVal != ""){
				modeArray[index] = currVal;
				modeReadyArray[parseInt(currVal) - 1] = $(this).attr("readyValue");	
			}			
			
		});
		//qosMode=modeInternet.val()+","+modeTr069.val()+","+modeIptv.val()+","+modeVoip.val()
		if(result != ""){
			return;
		}
		for(var i=0;i<modeReadyArray.length;i++){
			if(modeReadyArray[i] != undefined){
				if(qosModeValue == ""){
					qosModeValue += modeReadyArray[i];
				}
				else{
					qosModeValue += "," + modeReadyArray[i];
				}
			}
		}		
	}

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("���ڱ���...");
	var url = '<s:url value="/gwms/config/qosConfig!servQosConfig.action"/>';
	$.post(url,{
		deviceId:device_id,
		modeIptv:modeIptv.val(),
		gw_type:gw_type,
		modeVoip:modeVoip.val(),
		tempId:temp_id,
		qosMode:qosModeValue
	},function(ajax){
		$("div[@id='operResult']").html(ajax);
	});

	//setTimeout("clearResult()", 3000);
}

function arrIndexOf(arr,val){	
	for(var i=0;i<arr.length; i++){
	  if(arr[i]==val){
	    return i;
	  }
	}
	return -1;
}

function getQosList(){
	
	$("div[@id='operResult']").html("���ڻ�ȡ...");

	var url = '<s:url value="/gwms/config/qosConfig!qosConfigList.action"/>';
	$.post(url,{
		deviceId:device_id,
		tempId:temp_id
	},function(ajax){
		$("div[@id='operResult']").html(ajax);
		parent.dyniframesize();
	});
}
//-->
</script>
</head>
<body>
<form name="frm" action="qosConfig">
<table border=0 cellspacing=0 cellpadding=0 width="100%" height="auto">
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:nonde;background-color:#33CC00'></div>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999>
		<table border="0" cellspacing="1" cellpadding="1" width="100%">
			<tr bgcolor=#ffffff>
				<th colspan=2>
					<div style='float:left;width:100%'>ҵ������</div>
					</th>
			</tr>
			<tr bgcolor=#ffffff>
				<td class=column colspan=1 width='25%' align=right>ҵ�����</td>				
				<td colspan=1 >
				<ms:inArea areaCode="jx_dx" notInMode="true">
					<INPUT TYPE="checkbox" NAME="INTERNET" checked disabled>INTERNET&nbsp;&nbsp;<br>
					<INPUT TYPE="checkbox" NAME="TR069" checked disabled>TR069&nbsp;&nbsp;<br>
					<INPUT TYPE="checkbox" NAME="modeIptv">IPTV&nbsp;&nbsp;<br>
					<INPUT TYPE="checkbox" NAME="modeVoip">VOIP&nbsp;&nbsp;
				</ms:inArea>
				<ms:inArea areaCode="jx_dx" notInMode="false">
					<label style="width: 60px;">INTERNET:</label><INPUT TYPE="text" NAME="modeInternet" class="modeClass" readyValue="INTERNET" value="<s:property value='modeInternet'/>"><br>
					<label style="width: 60px;">TR069:</label><INPUT TYPE="text" NAME="modeTr069" class="modeClass" readyValue="TR069" value="<s:property value='modeTr069'/>"><br>
					<label style="width: 60px;">IPTV:</label><INPUT TYPE="text" NAME="modeIptv" class="modeClass" readyValue="IPTV" value="<s:property value='modeIptv'/>"><br>
					<label style="width: 60px;">VOIP:</label><INPUT TYPE="text" NAME="modeVoip" class="modeClass" readyValue="VOIP" value="<s:property value='modeVoip'/>">									
				</ms:inArea>
				</td>													
			</tr>
			<tr bgcolor=#ffffff height='20'>
				<td colspan=2>��ע: 
				<ms:inArea areaCode="hb_dx" notInMode="false">
				���ȼ�  TR069  > VOIP  > IPTV > INTERNET
				</ms:inArea>
				<ms:inArea areaCode="jx_dx" notInMode="false">
				<s:if test="qosMode == ''">
				��ǰ���ȼ� ��ǰû���������ȼ� 
				</s:if>
				<s:else>
				��ǰ���ȼ� <s:property value="qosMode"/>
				</s:else>
				<img src="../../images/attention_2.gif" width="15" height="12" title="ͨ�������������������������1��2��3��4����������1��ߣ�4��͡��������ȼ��淶��TR069>VOIP>IPTV>INTERNET��"/>				
				<input type="hidden" name="qosMode">
				</ms:inArea>
				<ms:inArea areaCode="hb_dx,jx_dx" notInMode="true">
				���ȼ�  VOIP > IPTV > TR069 > INTERNET
				</ms:inArea>
				</td>
			</tr>
			<tr CLASS="green_foot">
				<td colspan=2 align="right">
					<input type="button" name="smit" value=" �� �� " onclick="checkForm()">&nbsp;&nbsp;
					
					<INPUT type="button" name="reset" onclick="resetParam()" VALUE=" �� �� ">
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>