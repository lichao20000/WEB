<%--
���������ֹ�����
Author: Jason
Version: 1.0.0
Date: 2012-09-14
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<HEAD>
<title>�ն�ҵ���·�</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

String.prototype.replaceAll = function(oldStr,newStr) { 
    return this.replace(new RegExp(oldStr,"gm"),newStr); 
}
var checkBoxObj ;
var gw_type = '<s:property value="gwType" />';
var spServ = ";";
var spPara = "=";

$(function() {
	checkBoxObj = document.frm.ipTVPort;
	for(var i=0; i<checkBoxObj.length; i++){
		if(checkBoxObj[i].value == "L2"){
			checkBoxObj[i].checked = "true";
		}
	}
	changeUserType(gw_type);
});
$(function() {
	if(gw_type == '1')
	{
		$("TD[@id='Converged']").css("display","");
	}
	else
		{
		//$("TD[@id='Converged']").css("display","none");
		$("TD[@id='Converged']").html("");
		}
});
//���Ƽ�ͥ���غ���ҵ������ʾ����
function changeUserType(type)
{
	if("1" == type)
	{
		$("tr[@id='egw_cust']").css("display","none");
		$("TD[@id='Converged']").css("display","");
	}
	else
	{
		$("tr[@id='egw_cust']").css("display","block");
		var line1 = "<select name=\"line_id_1\" id=\"line_id_1\" class=\"bk\"  >"
			+ "<option value=\"1\" selected=\"selected\">==һ·VOIP==</option>"
			+ "<option value=\"2\" >==��·VOIP==</option>"
			+ "<option value=\"3\" >==��·VOIP==</option>"
			+ "<option value=\"4\" >==��·VOIP==</option>"
			+ "<option value=\"5\" >==��·VOIP==</option>"
			+ "<option value=\"6\" >==��·VOIP==</option>"
			+ "<option value=\"7\" >==��·VOIP==</option>"
			+ "<option value=\"8\" >==��·VOIP==</option>"
			+ "<option value=\"9\" >==��·VOIP==</option>"
			+ "<option value=\"10\" >==ʮ·VOIP==</option>"
			+ "<option value=\"11\" >==ʮһ·VOIP==</option>"
			+ "<option value=\"12\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"13\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"14\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"15\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"16\" >==ʮ��·VOIP==</option>"
			+ "</select>"
		
		var line2 = "<select name=\"line_id_2\" id=\"line_id_2\" class=\"bk\" >"
			+ "<option value=\"1\" >==һ·VOIP==</option>"
			+ "<option value=\"2\" selected=\"selected\">==��·VOIP==</option>"
			+ "<option value=\"3\" >==��·VOIP==</option>"
			+ "<option value=\"4\" >==��·VOIP==</option>"
			+ "<option value=\"5\" >==��·VOIP==</option>"
			+ "<option value=\"6\" >==��·VOIP==</option>"
			+ "<option value=\"7\" >==��·VOIP==</option>"
			+ "<option value=\"8\" >==��·VOIP==</option>"
			+ "<option value=\"9\" >==��·VOIP==</option>"
			+ "<option value=\"10\" >==ʮ·VOIP==</option>"
			+ "<option value=\"11\" >==ʮһ·VOIP==</option>"
			+ "<option value=\"12\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"13\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"14\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"15\" >==ʮ��·VOIP==</option>"
			+ "<option value=\"16\" >==ʮ��·VOIP==</option>"
			+ "</select>"
			
		var tid = "<option value=\"-1\">==��ѡ��==</option>"
			+ "<option value=\"A0\">==A0==</option>"
			+ "<option value=\"A1\">==A1==</option>"
			+ "<option value=\"A2\">==A2==</option>"
			+ "<option value=\"A3\">==A3==</option>"
			+ "<option value=\"A4\">==A4==</option>"
			+ "<option value=\"A5\">==A5==</option>"
			+ "<option value=\"A6\">==A6==</option>"
			+ "<option value=\"A7\">==A7==</option>"
			+ "<option value=\"A8\">==A8==</option>"
			+ "<option value=\"A9\">==A9==</option>"
			+ "<option value=\"A10\">==A10==</option>"
			+ "<option value=\"A11\">==A11==</option>"
			+ "<option value=\"A12\">==A12==</option>"
			+ "<option value=\"A13\">==A13==</option>"
			+ "<option value=\"A14\">==A14==</option>"
			+ "<option value=\"A15\">==A15==</option>"
			+ "<option value=\"AG58900\">==AG58900==</option>"
			+ "<option value=\"AG58901\">==AG58901==</option>"
			+ "<option value=\"AG58902\">==AG58902==</option>"
			+ "<option value=\"AG58903\">==AG58903==</option>"
			+ "<option value=\"AG58904\">==AG58904==</option>"
			+ "<option value=\"AG58905\">==AG58905==</option>"
			+ "<option value=\"AG58906\">==AG58906==</option>"
			+ "<option value=\"AG58907\">==AG58907==</option>"
			+ "<option value=\"AG58908\">==AG58908==</option>"
			+ "<option value=\"AG58909\">==AG58909==</option>"
			+ "<option value=\"AG589010\">==AG589010==</option>"
			+ "<option value=\"AG589011\">==AG589011==</option>"
			+ "<option value=\"AG589012\">==AG589012==</option>"
			+ "<option value=\"AG589013\">==AG589013==</option>"
			+ "<option value=\"AG589014\">==AG589014==</option>"
			+ "<option value=\"AG589015\">==AG589015==</option>"
			+ "<option value=\"USER1001\">==USER1001==</option>"
			+ "<option value=\"USER1002\">==USER1002==</option>"
			+ "<option value=\"USER1003\">==USER1003==</option>"
			+ "<option value=\"USER1004\">==USER1004==</option>"
			+ "<option value=\"USER1005\">==USER1005==</option>"
			+ "<option value=\"USER1006\">==USER1006==</option>"
			+ "<option value=\"USER1007\">==USER1007==</option>"
			+ "<option value=\"USER1008\">==USER1008==</option>"
			+ "<option value=\"USER1009\">==USER1009==</option>"
			+ "<option value=\"USER10010\">==USER10010==</option>"
			+ "<option value=\"USER10011\">==USER10011==</option>"
			+ "<option value=\"USER10012\">==USER10012==</option>"
			+ "<option value=\"USER10013\">==USER10013==</option>"
			+ "<option value=\"USER10014\">==USER10014==</option>"
			+ "<option value=\"USER10015\">==USER10015==</option>"
			+ "<option value=\"USER10016\">==USER10016==</option>"
		$("tr[@id='trline3']").css("display","block");
		$("tr[@id='trline4']").css("display","block");
		$("tr[@id='trline5']").css("display","block");
		$("tr[@id='trline6']").css("display","block");
		$("tr[@id='trline7']").css("display","block");
		$("tr[@id='trline8']").css("display","block");
		$("tr[@id='trline9']").css("display","block");
		$("tr[@id='trline10']").css("display","block");
		$("tr[@id='trline11']").css("display","block");
		$("tr[@id='trline12']").css("display","block");
		$("tr[@id='trline13']").css("display","block");
		$("tr[@id='trline14']").css("display","block");
		$("tr[@id='trline15']").css("display","block");
		$("tr[@id='trline16']").css("display","block");
		
		$("td[@id='td1']").html(line1);
		$("td[@id='td2']").html(line2);
		for (var i =1;i<17;i++)
		{
			$("select[@name='H248DeviceType"+i+"']").html(tid);
		}
	}
}

function checkLoid()
{
	if("" == Trim($("input[@name='loid']").val())){
		alert("LOID����Ϊ�գ�");
		return false;
	}
	cleanValue();
	var url = "<s:url value='/itms/service/bssSheetByHand4jx!checkLoid.action'/>";
	$.post(url, {
		loid : Trim($("input[@name='loid']").val()),
		gwType:gw_type
	}, function(ajax) {
		if(ajax != "-1"){
			var relt = ajax.split("|");
			//���Ϲ���
			if("" != relt[0])
			{
				var users = relt[0].split(spPara);
				$("input[@name='loid']").val(users[0]);
				$("select[@name='cityId']").val(users[1]);
				$("select[@name='jieRuType']").val(users[2]);
				$("input[@name='jieRuLinkMan']").val(users[3]);
				$("input[@name='jieRuLinPhone']").val(users[4]);
				$("input[@name='jieRuTelePhone']").val(users[6]);
				$("input[@name='jieRuAddress']").val(users[7]);
				$("input[@name='jieRuIDCard']").val(users[8]);
				$("select[@name='jieRuDeviceType']").val(users[9]);
				$("input[@name='customerId']").val(users[10]);
				//�޸Ĵ�
				$("select[@name='ConvergedUser']").val(users[10]);
				changeEle($("select[@name='jieRuDeviceType']").val());
			}
			if("" != relt[1])//���
			{
				$("#netarea").attr("checked",true);
				document.getElementById("internetBssSheet").style.display="";
				var nets = relt[1].split(spServ);
				for(var i=1;i<=nets.length;i++)
				{
					if("" != nets[i-1])
					{
						var vals = nets[i-1].split(spPara);
						$("input[@name='internetUser"+i+"']").val(vals[0]);
						$("input[@name='internetUser"+i+"']").attr("disabled", true);
						$("input[@name='internetPwd"+i+"']").val(vals[1]);
						$("input[@name='internetVLanID"+i+"']").val(vals[2]);
						$("select[@name='swType"+i+"']").val(vals[3]);  //������ʽ
					    $("select[@name='ipType"+i+"']").val(vals[4]);
						$("select[@name='aftrMode"+i+"']").val(vals[5]);
						$("input[@name='aftrIp"+i+"']").val(vals[6])
						$("select[@name='vpdn"+i+"']").val(vals[7]);
						$("#internetSheet"+i+"").attr("checked",true); 
						$("tr[@id='net"+i+"']").css("display","");
					}
				}
			}
			if("" != relt[2])//itv
			{
				var itvinfos = relt[2].split(spServ);
				
				$("#ipTVSheet").attr("checked",true);
				document.getElementById("ipTVBssSheet").style.display="";
				var itvs = itvinfos[0].split(spPara);
				$("input[@name='ipTVUser']").val(itvs[0]);
				$("input[@name='ipTVVLanID']").val(itvs[2]);
				/* $("input[@name='accessAccount']").val(itvs[3]);
				$("input[@name='accessPassword']").val(itvs[4]);
				$("input[@name='businessAccount']").val(itvs[5]);
				$("input[@name='businessPassword']").val(itvs[6]); */
				var itvPort = itvs[1].replaceAll("InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.","L").replaceAll("InternetGatewayDevice.LANDevice.1.WLANConfiguration.","SSID");
				var ipTVPortArr = itvPort.split(",");
				for(var i=0; i<ipTVPortArr.length; i++){
					var ipTVPortValue = ipTVPortArr[i];
					for(var z=0; z<checkBoxObj.length;z++){
						if(ipTVPortValue == checkBoxObj[z].value){
							checkBoxObj[z].checked = "true";
						}
					}
				}
			}
			if("" != relt[3])//voip
			{
				$("#h248Sheet").attr("checked",true);
				document.getElementById("H248BssSheet").style.display="";
				var voips = relt[3].split(spServ);
				for(var i=1;i<=voips.length;i++)
				{
					if("" != voips[i-1])
					{
						var voipstr = voips[i-1];
						if("" != voipstr)
						{
							var strvoips = voipstr.split(spPara);
							$("input[@name='h248Line"+strvoips[0]+"']").attr("checked",true);
							$("input[@name='h248Line"+strvoips[0]+"']").attr("disabled",true);
							$("select[@name='line_id_"+strvoips[0]+"']").val(strvoips[0]);
							$("input[@name='H248PhoneNum"+strvoips[0]+"']").val(strvoips[1]);
							$("input[@name='H248BiaoShi"+strvoips[0]+"']").val(strvoips[2]);
							$("input[@name='proxServ"+strvoips[0]+"']").val(strvoips[3]);
							$("input[@name='proxPort"+strvoips[0]+"']").val(strvoips[4]);
							$("input[@name='standProxServ"+strvoips[0]+"']").val(strvoips[5]);
							$("input[@name='standProxPort"+strvoips[0]+"']").val(strvoips[6]);
							$("select[@name='H248DeviceType"+strvoips[0]+"']").val(strvoips[7]);
							$("input[@name='H248VLanId']").val(strvoips[8]);       // H248 VLANID
							$("select[@name='wanType']").val(strvoips[9]);         // IP ��ȡ��ʽ
							if("3" == strvoips[9]){
								$("input[@name='ipaddress']").val(strvoips[10]);   // IP��ַ
								$("input[@name='ipmask']").val(strvoips[11]);      // ����
								$("input[@name='gateway']").val(strvoips[12]);     // ����
								$("input[@name='adslSer']").val(strvoips[13]);     // DNS
								$("tr[@id='tr01']").css("display","");
								$("tr[@id='tr02']").css("display","");
							}
							showNetForm($("input[@name='h248Line"+strvoips[0]+"']")[0],'voip'+strvoips[0]);
						}
					}
				}
				hiddenElements();
			}
			
		}else{
			alert("��LOID�����ڣ�");
		}
	});
	$("button[@name='subBtn']").attr("disabled", false);
}

// ���ڶ�̬��ʾ����ҵ�񣨿����IPTV��H248��
function showSheet(obj,name){
	//internetBssSheet,ipTVBssSheet,H248BssSheet
    if(obj.checked) {
        document.getElementById(name).style.display="";
        
      //  if("internetBssSheet"==name){
    	//	$("input[@id='h248Sheet']").attr("checked",false);
    	//	$("input[@id='ipTVSheet']").attr("checked",false);
    	//	document.getElementById("ipTVBssSheet").style.display="none";
    	//	document.getElementById("H248BssSheet").style.display="none";
    	//}else if("ipTVBssSheet"==name){
    	//	$("input[@id='netarea']").attr("checked",false);
    	//	$("input[@id='h248Sheet']").attr("checked",false);
    	//	document.getElementById("internetBssSheet").style.display="none";
    	//	document.getElementById("H248BssSheet").style.display="none";
    	//}else if("H248BssSheet"==name){
    	//	$("input[@id='netarea']").attr("checked",false);
    	//	$("input[@id='ipTVSheet']").attr("checked",false);
    	//	document.getElementById("internetBssSheet").style.display="none";
    	//	document.getElementById("ipTVBssSheet").style.display="none";
    	//}
    } else {
        document.getElementById(name).style.display="none";
    }
}
function showNetForm(obj,name)
{
	if(obj.checked)
	{
		$("tr[@id='"+name+"']").css("display","block");
	}
	else
	{
		$("tr[@id='"+name+"']").css("display","none");
	}
}

function hiddenElements(){
	var _wanType = $("select[@name='wanType']");	// IP��ȡ��ʽ
	
	if(_wanType.val() == "-1" || _wanType.val() == "3"){
		$("tr[@id='tr01']").css("display","");
		$("tr[@id='tr02']").css("display","");
	}else{
		$("tr[@id='tr01']").css("display","none");
		$("tr[@id='tr02']").css("display","none");
	}
}

//ҵ���·�
//modify by zhangsb3 2014��4��21�� 
//�޸ĺ󣬿����IPTV��VOIPֻ�ᵥ����ȥִ��
function doBusiness(){
	var userInfo = "";
	var netInfo = "";
	var itvInfo = "";
	var voipInfo = "";
	
	var lineId = ",";//����У���Ƿ��ظ�ѡ����·
	var tid = "";//����У��tid�Ƿ��ظ�
	var voipCount = 0;//����У���ն˹�����·������
	
	if($("input[@name='loid']").val() == ""){
		alert("LOID����Ϊ��!");
		$("input[@name='loid']").focus();
		return false;
	}
	if($("select[@name='cityId']").val() == "" || $("select[@name='cityId']").val() == "-1"){
		alert("��ѡ������");
		$("select[@name='cityId']").focus();
		return false;
	}
	if($("select[@name='jieRuType']").val() == "" || $("select[@name='jieRuType']").val() == "-1"){
		alert("��ѡ���ն˽�������!");
		$("select[@name='jieRuType']").focus();
		return false;
	}	
	if($("select[@name='jieRuDeviceType']").val() == "" || $("select[@name='jieRuDeviceType']").val() == "-1"){
		alert("��ѡ���ն˹��!");
		return false;
	}
	if("2" == gw_type && "" == $("input[@name='customerId']").val())
	{
		alert("�ͻ�id�������롣");
		$("input[@name='customerId']").focus();
		return false;
	}
	if("1" == gw_type)
		{
	if($("select[@name='ConvergedUser']").val() == "" || $("select[@name='ConvergedUser']").val() == "-1"){
		alert("��ѡ���Ƿ����ں��û�");
		$("select[@name='ConvergedUser']").focus();
		return false;
	}
		}
	userInfo += Trim($("input[@name='loid']").val()) +spPara;
	userInfo += $("select[@name='cityId']").val() +spPara;
	userInfo += $("select[@name='jieRuType']").val() +spPara;
	userInfo += $("input[@name='jieRuLinkMan']").val() +spPara;//3
	userInfo += $("input[@name='jieRuLinPhone']").val() +spPara;
	userInfo += $("select[@name='jieRuDeviceType']").val() +spPara;
	userInfo += $("input[@name='jieRuTelePhone']").val() +spPara;//6
	userInfo += $("input[@name='jieRuIDCard']").val() +spPara;
	userInfo += $("input[@name='jieRuAddress']").val() +spPara;
	userInfo += $("input[@name='customerId']").val() +spPara;
	userInfo += $("input[@name='customerAccount']").val() +spPara+"end";
	//�޸Ĵ�
	if(gw_type=="1")
		{
	userInfo += spPara+ $("select[@name='ConvergedUser']").val() +spPara;
		}
	var voiceNumList = $("#voiceNumList").val();
	//alert(voiceNumList);
	var voiceNumListArr = voiceNumList.split("####");
	for(var i=0; i<voiceNumListArr.length; i++){
		var voiceNumArr = voiceNumListArr[i].split("##");
		if($("select[@name='jieRuDeviceType']").val()==voiceNumArr[0]){
			voipCount = parseInt(voiceNumArr[1]);
		}
	}
	// voipCount = $("select[@name='jieRuDeviceType']").val().charAt($("select[@name='jieRuDeviceType']").val().length-1);
	//alert(voipCount);
	if("22" == $("input[@id='netarea'][checked]").val())   //���ҵ��
	{
		for(var i=1;i<=4;i++)
		{
			var internetSheet = $("input[@name='internetSheet"+i+"'][checked]").val();
			if(i == internetSheet)
			{
				if("" == $("input[@name='internetUser"+i+"']").val()){
					alert("����ʺŲ���Ϊ��!");
					$("input[@name='internetUser"+i+"']").focus();
					return false;
				}
				if("" == $("input[@name='internetVLanID"+i+"']").val()){
					alert("���VLANID����Ϊ��!");
					$("input[@name='internetVLanID"+i+"']").focus();
					return false;
				}
				if("1"==$("select[@name='aftrMode"+i+"']").val()||"4"==$("select[@name='ipType"+i+"']").val()){
					if(""==$.trim($("input[@name='aftrIp"+i+"']").val())){
						alert("��aftr_modeѡ���˹����û�IP����ѡ��DS-Liteʱ��aftr_ip����Ϊ�գ�");
						$("input[@name='aftrIp"+i+"']").focus();
						return false;
					}
				}
				netInfo += $("input[@name='internetUser"+i+"']").val()+spPara;      // ����ʺŻ�ר�߽����ʺ�
				netInfo += $("input[@name='internetPwd"+i+"']").val()+spPara;         // �������
				netInfo += $("input[@name='internetVLanID"+i+"']").val()+spPara;  // ���VLANID
				netInfo += $("select[@name='swType"+i+"']").val()+spPara;
				netInfo += $("select[@name='ipType"+i+"']").val()+spPara;
				netInfo += $("select[@name='aftrMode"+i+"']").val()+spPara;
				netInfo += $("input[@name='aftrIp"+i+"']").val()+spPara;
				netInfo += $("select[@name='vpdn"+i+"']").val()+spPara+"end"+spServ;
			}
		}
		if("" == netInfo)
		{
			alert("û�й�ѡ�����Ϣ");
			return false;
		}
	}
	
	if("21" == $("input[@id='ipTVSheet'][checked]").val())   //itv
	{
		// ��ͨ�˿�
		var _ipTVPort = "";
		for(var j=0; j<checkBoxObj.length;j++){
			if(checkBoxObj[j].checked){
				if(_ipTVPort == ""){
					_ipTVPort = checkBoxObj[j].value;
				}else{
					_ipTVPort = _ipTVPort + "," + checkBoxObj[j].value;
				}
			}
		}
		if($("input[@name='ipTVUser']").val() == ""){
			alert("IPTV��������ʺŲ���Ϊ��!");
			$("input[@name='ipTVUser']").focus();
			return false;
		}
		if($("input[@name='ipTVPort']").val() == ""){
			alert("��ͨ�˿ڲ���Ϊ��!");
			$("input[@name='ipTVPort']").focus();
			return false;
		}
		if($("input[@name='ipTVVLanID']").val() == ""){
			alert("VLANID����Ϊ��!");
			$("input[@name='ipTVVLanID']").focus();
			return false;
		}
		//�޸Ĵ�
		/* if($("select[@name='ConvergedUser']").val()=="1")
		{
			var accessAccount=$("input[@name='accessAccount']").val();
			var accessPassword=$("input[@name='accessPassword']").val();
			var businessAccount=$("input[@name='businessAccount']").val();
			var businessPassword=$("input[@name='businessPassword']").val();
			if(accessAccount==null||accessAccount=="")
			{
				alert("������д�ں��ն˽����˺ţ�");
				$("input[@name='accessAccount']").focus();
				return false;
			}
			if(accessPassword==null||accessPassword=="")
			{
				alert("������д�ں��ն˽������룡");
				$("input[@name='accessPassword']").focus();
				return false;
				
			}
			if(businessAccount==null||businessAccount=="")
			{
				alert("������д�ں��ն�ҵ���˺ţ�");
				$("input[@name='businessAccount']").focus();
				return false;
				
			}
			if(businessPassword==null||businessPassword=="")
			{
				alert("������д�ں��ն�ҵ�����룡");
				$("input[@name='businessPassword']").focus();
				return false;
				
			}
		} */
		itvInfo += $("input[@name='ipTVUser']").val() + spPara;
		itvInfo += _ipTVPort + spPara;
		itvInfo += $("input[@name='ipTVVLanID']").val()+spPara+"end" + spServ;
		//�޸Ĵ�
		/* itvInfo +=spPara+ $("input[@name='accessAccount']").val() + spPara;
		itvInfo += $("input[@name='accessPassword']").val() + spPara;
		itvInfo += $("input[@name='businessAccount']").val() + spPara;
		itvInfo += $("input[@name='businessPassword']").val() + spPara; */
	}
	
	if("15" == $("input[@id='h248Sheet'][checked]").val())  //h248
	{
		if("" == $("select[@name='wanType']").val() || "-1" == $("select[@name='wanType']").val()){
			alert("��ѡ��IP��ȡ��ʽ��");
			$("select[@name='wanType']").focus();
			return false;
		}
		if($("input[@name='H248VLanId']").val() == ""){
			alert("VLANID����Ϊ��!");
			$("input[@name='H248VLanId']").focus();
			return false;
		}
		if($("select[@name='wanType']").val() == "3")
		{
			if(!IsNull($("input[@name='ipaddress']").val(), "IP��ַ")){
				$("input[@name='ipaddress']").focus();
				return false;
			}else if(!reg_verify($("input[@name='ipaddress']").val())){
				$("input[@name='ipaddress']").focus();
				alert("������Ϸ���IP��ַ��");
				return false;
			}
			// ����
			if(!IsNull($("input[@name='ipmask']").val(), "����")){
				$("input[@name='ipmask']").focus();
				return false;
			}
			// ����
			if(!IsNull($("input[@name='gateway']").val(), "����")){
				$("input[@name='gateway']").focus();
				return false;
			}else if(!reg_verify($("input[@name='gateway']").val())){
				$("input[@name='gateway']").focus();
				alert("������Ϸ������ص�ַ��");
				return false;
			}
		}
		var strPhone = "";
		for (var vi = 1;vi<17;vi++)
		{
			if(vi == $("input[@name='h248Line"+vi+"'][checked]").val())
			{
				if(parseInt($("select[@name='line_id_"+vi+"']").val()) > parseInt(voipCount))//���ݹ��У����·
				{
					alert("������·�����ն˶˿��������ύʧ��");
					return false;
				}
				if(strPhone.indexOf($("input[@name='H248PhoneNum"+vi+"']").val()+",") == -1)//У���ظ��绰����
				{
					strPhone += $("input[@name='H248PhoneNum"+vi+"']").val()+",";
				}
				else
				{
					alert("�绰�����ظ�");
					$("input[@name='H248PhoneNum"+vi+"']").focus();
					return false;
				}

				// ��ֹ���� 1,2,16,4,5,6  6��ʱ��У�鱨��
				var tempLineId = "," + $("select[@name='line_id_"+vi+"']").val() + ",";
				if(lineId.indexOf(tempLineId) == "-1")//У���ظ���·
				{
					lineId += $("select[@name='line_id_"+vi+"']").val() + ",";
				}
				else
				{
					alert("����·�Ѿ�ѡ��");
					$("select[@name='line_id_"+vi+"']")[0].focus();
					return false;
				}
				if(tid.indexOf($("select[@name='H248DeviceType"+vi+"']").val()) == "-1")//У���ظ�tid
				{
					tid += $("select[@name='H248DeviceType"+vi+"']").val() + ",";
				}
				else
				{
					alert("�˱�ʾ�Ѿ�ѡ��");
					$("select[@name='H248DeviceType"+vi+"']")[0].focus();
					return false;
				}
				if("" == $("input[@name='H248PhoneNum"+vi+"']").val()){
					alert("ҵ��绰�Ų���Ϊ��!");
					$("input[@name='H248PhoneNum"+vi+"']").focus();
					return false;
				}
				if("" == $("input[@name='H248BiaoShi"+vi+"']").val()){
					alert("�ն˱�ʶ����Ϊ��!");
					$("input[@name='H248BiaoShi"+vi+"']").focus();
					return false;
				}
				if("" == $("input[@name='proxServ"+vi+"']").val()){
					alert("����MGC��ַ����Ϊ��!");
					$("input[@name='proxServ"+vi+"']").focus();
					return false;
				}
				if("" == $("input[@name='proxPort"+vi+"']").val()){
					alert("����MGC�˿ڲ���Ϊ��!");
					$("input[@name='proxPort"+vi+"']").focus();
					return false;
				}
				if("" == $("input[@name='standProxServ"+vi+"']").val()){
					alert("����MGC��ַ����Ϊ��!");
					$("input[@name='standProxServ"+vi+"']").focus();
					return false;
				}
				if("" == $("input[@name='standProxPort"+vi+"']").val()){
					alert("����MGC�˿ڲ���Ϊ��!");
					$("input[@name='standProxPort"+vi+"']").focus();
					return false;
				}
				if("-1" == $("select[@name='H248DeviceType"+vi+"']").val()){
					alert("��ѡ���ն������ʶ!");
					$("select[@name='H248DeviceType"+vi+"']").focus();
					return false;
				}
				voipInfo += $("input[@name='h248Line"+vi+"'][checked]").val() + spPara;
				voipInfo += $("select[@name='line_id_"+vi+"']").val() + spPara;
				voipInfo += $("input[@name='H248PhoneNum"+vi+"']").val() + spPara;
				voipInfo += $("input[@name='H248BiaoShi"+vi+"']").val() + spPara;
				voipInfo += $("input[@name='proxServ"+vi+"']").val() + spPara;
				voipInfo += $("input[@name='proxPort"+vi+"']").val() + spPara;//5
				voipInfo += $("input[@name='standProxServ"+vi+"']").val() + spPara;
				voipInfo += $("input[@name='standProxPort"+vi+"']").val() + spPara;
				voipInfo += $("select[@name='H248DeviceType"+vi+"']").val() + spPara;
				voipInfo += $("select[@name='wanType']").val() + spPara;
				voipInfo += $("input[@name='H248VLanId']").val() + spPara;//10
				voipInfo += $("input[@name='ipaddress']").val() + spPara;
				voipInfo += $("input[@name='ipmask']").val() + spPara;
				voipInfo += $("input[@name='gateway']").val() + spPara;
				voipInfo += $("input[@name='adslSer']").val()+spPara+"end" + spServ;
			}
		}
		if("" == voipInfo)
		{
			alert("û�й�ѡ����ҵ��");
			return false;
		}
	}
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4jx!doBusiness.action'/>";
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
	$.post(url,{
		gwType:gw_type,
		userInfo:userInfo,
		netInfo:netInfo,
		itvInfo:itvInfo,
		voipInfo:voipInfo
	},function(ajax){
		$("button[@name='subBtn']").attr("disabled", false);
		alert(ajax);
	});
	
	
}

function doReset(){
	$("input[@name='loid']").val("");
	
	cleanValue();
}
function cleanValue()
{
	$("select[@name='cityId']").val("-1");
	$("select[@name='jieRuType']").val("");
	$("input[@name='jieRuLinkMan']").val("");
	$("input[@name='jieRuLinPhone']").val("");
	$("input[@name='jieRuTelePhone']").val("");
	$("input[@name='jieRuAddress']").val("");
	$("input[@name='jieRuIDCard']").val("");
	$("select[@name='jieRuDeviceType']").val("e8cp42");
	$("input[@name='internetUser']").val("");
	$("input[@name='internetPwd']").val("");
	$("input[@name='internetVLanID']").val("41");
	$("input[@name='ipTVUser']").val("");
	$("input[@name='ipTVVLanID']").val("45");
	
	//�޸Ĵ�
	$("select[@name='ConvergedUser']").val("-1");
	/* $("input[@name='accessAccount']").val("");
	$("input[@name='accessPassword']").val("");
	$("input[@name='businessAccount']").val("");
	$("input[@name='businessPassword']").val(""); */
	
	var ipTVPortCheckBoxObj = document.frm.ipTVPort;
	for(var i=0; i<ipTVPortCheckBoxObj.length; i++){
		if(ipTVPortCheckBoxObj[i].value == "L2"){
			ipTVPortCheckBoxObj[i].checked = "true";
		}
	}
	
	$("#netarea").attr("checked",false);
	$("#ipTVSheet").attr("checked",false);
	$("#h248Sheet").attr("checked",false);
	document.getElementById("internetBssSheet").style.display="none";
	document.getElementById("ipTVBssSheet").style.display="none";
	document.getElementById("H248BssSheet").style.display="none";
	
	for(var i=1;i<=4;i++)
	{
		$("input[@name='internetSheet"+i+"']").attr("checked",false);
		$("tr[@id='net"+i+"']").css("display","none");
		$("input[@name='internetUser"+i+"']").attr("disabled", false);//ҳ��ӵ�ʱ��,�����ó�true.�����ԭ
		
		$("input[@name='internetUser"+i+"']").val("");
		$("input[@name='internetPwd"+i+"']").val("");
		$("input[@name='internetVLanID"+i+"']").val("");
	}
	for (var vi = 1;vi<17;vi++)
	{
		$("input[@name='h248Line"+vi+"']").attr("checked",false);
		$("tr[@id='voip"+vi+"']").css("display","none");
		$("input[@name='h248Line"+vi+"']").attr("disabled",false);
		
		$("input[@name='H248PhoneNum"+vi+"']").val("");
		$("input[@name='H248BiaoShi"+vi+"']").val("");
		$("input[@name='proxServ"+vi+"']").val("");
		$("input[@name='proxPort"+vi+"']").val("");
		$("input[@name='standProxServ"+vi+"']").val("");
		$("input[@name='standProxPort"+vi+"']").val("");
		$("select[@name='H248DeviceType"+vi+"']").val("-1");
	}
	$("select[@name='wanType']").val("-1");
	$("input[@name='H248VLanId']").val("");
	$("input[@name='ipaddress']").val("");
	$("input[@name='ipmask']").val("");
	$("input[@name='gateway']").val("");
	$("input[@name='adslSer']").val("");
	
	$("input[@name='ipTVUser']").val("");
	$("input[@name='ipTVVLanID']").val("");
	
}

function changeEle(devValue){
	var ipTVPortCheckBoxObj = document.frm.ipTVPort;
	if("e8cp21" == devValue){
		for(var i=0; i<ipTVPortCheckBoxObj.length; i++){
			if(ipTVPortCheckBoxObj[i].value == "L3"){
				ipTVPortCheckBoxObj[i].checked = false;
				ipTVPortCheckBoxObj[i].disabled = true;
				
			}
			if(ipTVPortCheckBoxObj[i].value == "L4"){
				ipTVPortCheckBoxObj[i].checked = false;
				ipTVPortCheckBoxObj[i].disabled = true;
				
			}
			if(ipTVPortCheckBoxObj[i].value == "SSID2"){
				ipTVPortCheckBoxObj[i].checked = false;
				ipTVPortCheckBoxObj[i].disabled = true;
			}
		}
	}else{
		for(var i=0; i<ipTVPortCheckBoxObj.length; i++){
			if(ipTVPortCheckBoxObj[i].value == "L3"){
				ipTVPortCheckBoxObj[i].checked = false;
				ipTVPortCheckBoxObj[i].disabled = false;
				
			}
			if(ipTVPortCheckBoxObj[i].value == "L4"){
				ipTVPortCheckBoxObj[i].checked = false;
				ipTVPortCheckBoxObj[i].disabled = false;
				
			}
			if(ipTVPortCheckBoxObj[i].value == "SSID2"){
				ipTVPortCheckBoxObj[i].checked = false;
				ipTVPortCheckBoxObj[i].disabled = false;
			}
		}
	}
}

/* reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
������򷵻�true�����򣬷���false��*/
function reg_verify(addr){
	//������ʽ
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg)) {
    	//IP��ַ��ȷ
        return true;
    } else {
    	//IP��ַУ��ʧ��
         return false;
    }
}

</script>
</HEAD>
<body>
<FORM NAME="frm" >
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD colspan="4">
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">�ֹ�����
						<input type="hidden" id="voiceNumList" value="<s:property value="voiceNumList" />"></div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15" height="12"></td>
					</tr>
				</table>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr align="left">
						<td colspan="4" class="green_title_left">
						�û����Ϲ���
						</td>
					</tr>
					<tbody id="jirRuBssSheet" >
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">LOID:</TD>
							<TD width="35%" >
								<input type="text" name="loid" class=bk onblur="checkLoid()" value="">&nbsp;
								<font color="#FF0000">*</font>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button type="button" name="subButton" >���LOID�Ƿ����</button>
								<!-- onblur="checkLoid()" -->
							</TD>
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<s:select list="cityList" name="cityId"
								headerKey="-1" headerValue="��ѡ������" listKey="city_id"
								listValue="city_name" value="cityId" cssClass="bk"></s:select>
							<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">�ն˽�������:</TD>
							<TD width="35%" >
								<SELECT name="jieRuType" class="bk">
								<OPTION value="-1">--��ѡ��--</OPTION>
								<OPTION value="1">ADSL</OPTION>
								<OPTION value="2">LAN</OPTION>
								<OPTION value="3">EPON</OPTION>
								<OPTION value="4">GPON</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��ϵ��:</TD>
							<!-- ������������ -->
							<ms:inArea areaCode="jx_dx" notInMode="false">
								<TD width="35%" >
									<input type="password" name="jieRuLinkMan" class=bk value="">
								</TD>
							</ms:inArea>
							<ms:inArea areaCode="jx_dx" notInMode="true">
								<TD width="35%" >
									<input type="text" name="jieRuLinkMan" class=bk value="">
								</TD>
							</ms:inArea>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">��ϵ�绰:</TD>
							<!-- ������������ -->
							<ms:inArea areaCode="jx_dx" notInMode="false">
								<TD width="35%" >
									<input type="password" name="jieRuLinPhone" class=bk value="">
								</TD>
							</ms:inArea>
							<ms:inArea areaCode="jx_dx" notInMode="true">
								<TD width="35%" >
									<input type="text" name="jieRuLinPhone" class=bk value="">
								</TD>
							</ms:inArea>
							<TD width="15%" class=column align="right">�ն˹��:</TD>
							<TD width="35%" >
								<s:select list="deviceType" name="jieRuDeviceType" headerKey="-1"
								headerValue="--��ѡ��--" listKey="id" listValue="spec_name"
								value="specId" cssClass="bk" onChange="changeEle(this.value)"></s:select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						
						<!-- ������������ -->
						<ms:inArea areaCode="jx_dx" notInMode="false">
							<TR bgcolor="#FFFFFF" >
								<TD class=column align="right" nowrap width="15%">�ֻ�:</TD>
					
								<TD width="35%" >
									<input type="password" name="jieRuTelePhone" class=bk value="">
								</TD>
								<TD class=column align="right" nowrap width="15%">֤������:</TD>
								<TD width="35%" >
									<input type="password" name="jieRuIDCard" class=bk value="">
								</TD>
							</TR>
						</ms:inArea>
						<ms:inArea areaCode="jx_dx" notInMode="true">
							<TR bgcolor="#FFFFFF" >
								<TD class=column align="right" nowrap width="15%">�ֻ�:</TD>
					
								<TD width="35%" >
									<input type="text" name="jieRuTelePhone" class=bk value="">
								</TD>
								<TD class=column align="right" nowrap width="15%">֤������:</TD>
								<TD width="35%" >
									<input type="text" name="jieRuIDCard" class=bk value="">
								</TD>
							</TR>
						</ms:inArea>
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">��ַ:</TD>
							<ms:inArea areaCode="jx_dx" notInMode="false">
								<TD  width="35%" >
									<input type="password" name="jieRuAddress" size="55" class=bk value="">
								</TD>
							</ms:inArea>
							<ms:inArea areaCode="jx_dx" notInMode="true">
								<TD  width="35%" >
									<input type="text" name="jieRuAddress" size="55" class=bk value="">
								</TD>
							</ms:inArea>
							<!-- �޸Ĵ� -->
							<TD class=column align="right" nowrap width="15%" id="Converged" >�ں��û�:</TD>
							<TD width="35%" id="Converged" >
								<SELECT name="ConvergedUser" class="bk">
								<OPTION value="0">��</OPTION>
								<OPTION value="1">��</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					
						<TR id="egw_cust" bgcolor="#FFFFFF" >
							<TD id ="customerId" class=column align="right" nowrap width="15%">�ͻ�ID��</TD>
							<TD id ="customerIdValue" width="35%"  colspan="3">
								<input type="text" name="customerId" class=bk value="">
							<font color="#FF0000">*</font>
							</TD>
							<!--
							<TD id ="customerId" class=column align="right" nowrap width="15%">�ͻ����ƣ�</TD>
							<TD id ="customerIdValue" width="35%" >
								<input type="text" name="customerAccount" class=bk value="">
							</TD>
							 -->
						</TR>
					</tbody>
					
					
					<tr align="left">
						<td colspan="4" class="green_title_left">
						<input type="checkbox" id="netarea" value="22" onclick="showSheet(this,'internetBssSheet');"/>
						���ҵ�񹤵�
						</td>
					</tr>
					<tbody id="internetBssSheet" style="display:none">
					
						<TR style="background-color: #F0FFFF">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="internetSheet1" value="1" id="internetSheet1"  onclick="showNetForm(this,'net1');"/>
								<input type="hidden" name="netOpera1" value="1">
							</TD>
							<TD width="85%"  colspan="3">
								���ҵ��һ
							</TD>
						</TR>
					
						<TR id = 'net1' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
							<TD width="35%" >
								<input type="text" name="internetUser1" class=bk value="">
								<input type="hidden" name="intenetUserOld1" value="" >
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%">
								<input type="password" name="internetPwd1" class=bk value="">
								&nbsp;
							</TD>
						</TR>
						<TR id = 'net1' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="internetVLanID1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<SELECT name="swType1" class="bk">
								<OPTION value="1" selected="selected">--�Ž�--</OPTION>
								<OPTION value="2">--·��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net1' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">�û�IP����:</TD>
							<TD width="35%" >
								<SELECT name="ipType1" class="bk">
								<OPTION value="0" selected="selected">--������ջ--</OPTION>
								<OPTION value="1">--����˫ջ--</OPTION>
								<OPTION value="2">--˽������--</OPTION>
								<OPTION value="3">--˽��˫��--</OPTION>
								<OPTION value="4">--DS-Lite--</OPTION>
								<OPTION value="5">--��V6--</OPTION>
								<OPTION value="6">--LAFT6--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">AFTR_MODE:</TD>
							<TD width="35%" >
								<SELECT name="aftrMode1" class="bk">
								<OPTION value="0" selected="selected">--�Զ���ȡ--</OPTION>
								<OPTION value="1">--�˹�����--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net1' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">AFTR_IP:</TD>
							<TD width="35%" >
								<input type="text" name="aftrIp1" size="55" class=bk value="aftr123.ctcms.cn">
							</TD>
							<TD class=column align="right" nowrap width="15%">VPDN:</TD>
							<TD width="35%" >
								<SELECT name=vpdn1 class="bk">
								<OPTION value="0" selected="selected">--��--</OPTION>
								<OPTION value="1">--��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						
						<TR style="background-color: #F0FFFF">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="internetSheet2" value="2" id="internetSheet2"  onclick="showNetForm(this,'net2');"/>
								<input type="hidden" name="netOpera2" value="1">
							</TD>
							<TD width="85%"  colspan="3">
								���ҵ���
							</TD>
						</TR>
						
						<TR id = 'net2' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
							<TD width="35%" >
								<input type="text" name="internetUser2" class=bk value="">
								<input type="hidden" name="intenetUserOld2" value="" >
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%">
								<input type="password" name="internetPwd2" class=bk value="">
								&nbsp;
							</TD>
						</TR>
						<TR id = 'net2' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="internetVLanID2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<SELECT name="swType2" class="bk">
								<OPTION value="1" selected="selected">--�Ž�--</OPTION>
								<OPTION value="2">--·��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						
						<TR id = 'net2' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">�û�IP����:</TD>
							<TD width="35%" >
								<SELECT name="ipType2" class="bk">
								<OPTION value="0" selected="selected">--������ջ--</OPTION>
								<OPTION value="1">--����˫ջ--</OPTION>
								<OPTION value="2">--˽������--</OPTION>
								<OPTION value="3">--˽��˫��--</OPTION>
								<OPTION value="4">--DS-Lite--</OPTION>
								<OPTION value="5">--��V6--</OPTION>
								<OPTION value="6">--LAFT6--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">AFTR_MODE:</TD>
							<TD width="35%" >
								<SELECT name="aftrMode2" class="bk">
								<OPTION value="0" selected="selected">--�Զ���ȡ--</OPTION>
								<OPTION value="1">--�˹�����--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net2' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">AFTR_IP:</TD>
							<TD width="35%" >
								<input type="text" name="aftrIp2" size="55" class=bk value="aftr123.ctcms.cn">
							</TD>
							<TD class=column align="right" nowrap width="15%">VPDN:</TD>
							<TD width="35%" >
								<SELECT name=vpdn2 class="bk">
								<OPTION value="0" selected="selected">--��--</OPTION>
								<OPTION value="1">--��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR style="background-color: #F0FFFF">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="internetSheet3" value="3" id="internetSheet3"  onclick="showNetForm(this,'net3');"/>
								<input type="hidden" name="netOpera3" value="1">
							</TD>
							<TD width="85%"  colspan="3">
								���ҵ����
							</TD>
						</TR>
						
						<TR id = 'net3' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
							<TD width="35%" >
								<input type="text" name="internetUser3" class=bk value="">
								<input type="hidden" name="intenetUserOld3" value="" >
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%">
								<input type="password" name="internetPwd3" class=bk value="">
								&nbsp;
							</TD>
						</TR>
						<TR id = 'net3' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="internetVLanID3" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<SELECT name="swType3" class="bk">
								<OPTION value="1" selected="selected">--�Ž�--</OPTION>
								<OPTION value="2">--·��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net3' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">�û�IP����:</TD>
							<TD width="35%" >
								<SELECT name="ipType3" class="bk">
								<OPTION value="0" selected="selected">--������ջ--</OPTION>
								<OPTION value="1">--����˫ջ--</OPTION>
								<OPTION value="2">--˽������--</OPTION>
								<OPTION value="3">--˽��˫��--</OPTION>
								<OPTION value="4">--DS-Lite--</OPTION>
								<OPTION value="5">--��V6--</OPTION>
								<OPTION value="6">--LAFT6--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">AFTR_MODE:</TD>
							<TD width="35%" >
								<SELECT name="aftrMode3" class="bk">
								<OPTION value="0" selected="selected">--�Զ���ȡ--</OPTION>
								<OPTION value="1">--�˹�����--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net3' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">AFTR_IP:</TD>
							<TD width="35%" >
								<input type="text" name="aftrIp3" size="55" class=bk value="aftr123.ctcms.cn">
							</TD>
							<TD class=column align="right" nowrap width="15%">VPDN:</TD>
							<TD width="35%" >
								<SELECT name=vpdn3 class="bk">
								<OPTION value="0" selected="selected">--��--</OPTION>
								<OPTION value="1">--��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR style="background-color: #F0FFFF">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="internetSheet4" value="4" id="internetSheet4"  onclick="showNetForm(this,'net4');"/>
								<input type="hidden" name="netOpera4" value="1">
							</TD>
							<TD width="85%"  colspan="3">
								���ҵ����
							</TD>
						</TR>
						
						<TR id = 'net4' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
							<TD width="35%" >
								<input type="text" name="internetUser4" class=bk value="">
								<input type="hidden" name="intenetUserOld4" value="" >
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%">
								<input type="password" name="internetPwd4" class=bk value="">
								&nbsp;
							</TD>
						</TR>
						<TR id = 'net4' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="internetVLanID4" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<SELECT name="swType4" class="bk">
								<OPTION value="1" selected="selected">--�Ž�--</OPTION>
								<OPTION value="2">--·��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net4' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">�û�IP����:</TD>
							<TD width="35%" >
								<SELECT name="ipType4" class="bk">
								<OPTION value="0" selected="selected">--������ջ--</OPTION>
								<OPTION value="1">--����˫ջ--</OPTION>
								<OPTION value="2">--˽������--</OPTION>
								<OPTION value="3">--˽��˫��--</OPTION>
								<OPTION value="4">--DS-Lite--</OPTION>
								<OPTION value="5">--��V6--</OPTION>
								<OPTION value="6">--LAFT6--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">AFTR_MODE:</TD>
							<TD width="35%" >
								<SELECT name="aftrMode4" class="bk">
								<OPTION value="0" selected="selected">--�Զ���ȡ--</OPTION>
								<OPTION value="1">--�˹�����--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id = 'net4' bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">AFTR_IP:</TD>
							<TD width="35%" >
								<input type="text" name="aftrIp4" size="55" class=bk value="aftr123.ctcms.cn">
							</TD>
							<TD class=column align="right" nowrap width="15%">VPDN:</TD>
							<TD width="35%" >
								<SELECT name=vpdn4 class="bk">
								<OPTION value="0" selected="selected">--��--</OPTION>
								<OPTION value="1">--��--</OPTION>
								</SELECT>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>
					<tr align="left">
						<td colspan="4" class="green_title_left">
						<input type="checkbox" name="ipTVSheet" value="21" id="ipTVSheet" onclick="showSheet(this,'ipTVBssSheet');"/>
						<input type="hidden" name="itvOpera" value="1">
						IPTVҵ�񹤵�
						</td>
					</tr>
					<tbody id="ipTVBssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">IPTV��������ʺ�:</TD>
							<TD width="35%" >
								<input type="text" name="ipTVUser" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��ͨ�˿�:</TD>
							<TD id="iptvTD1" width="35%" >
								<input type="checkbox" name="ipTVPort" value="L2" class=bk />L2&nbsp;
								<input type="checkbox" name="ipTVPort" value="L3" class=bk />L3&nbsp;
								<input type="checkbox" name="ipTVPort" value="L4" class=bk />L4&nbsp;
								<input type="checkbox" name="ipTVPort" value="SSID2" class=bk />SSID2&nbsp;
							&nbsp;<font color="#FF0000">*
							</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="ipTVVLanID" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						 <!-- �޸Ĵ� -->
							<TD class=column align="right" nowrap width="15%"><br></TD>
							<TD width="35%" >
							</TD>
						</TR>
					</tbody>
					
					<tr align="left">
						<td colspan="4" class="green_title_left">
						<input type="checkbox" name="h248Sheet" value="15" id="h248Sheet" onclick="showSheet(this,'H248BssSheet');"/>
						H248����ҵ�񹤵�
						</td>
					</tr>
					<tbody id="H248BssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">IP��ȡ��ʽ:</TD>
							<TD width="35%" >
								<select name="wanType" class="bk" onChange="hiddenElements()">
								<option value="-1">==��ѡ���������==</option>
								<option value="3">==STATIC==</option>
								<option value="4">==DHCP==</option>
							</select>
							&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">VLANID</TD>
							<TD width="35%">
								<input type="text" name="H248VLanId" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="tr01" style="display: none">
							<TD class=column align="right" nowrap>IP��ַ</TD>
							<TD><INPUT TYPE="text" NAME="ipaddress" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>����</TD>
							<TD><INPUT TYPE="text" NAME="ipmask" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="tr02" style="display: none">
							<TD class=column align="right" nowrap>����</TD>
							<TD><INPUT TYPE="text" NAME="gateway" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>DNSֵ</TD>
							<TD><INPUT TYPE="text" NAME="adslSer" maxlength=20
								class=bk value=""></TD>
						</TR>
						<TR style="background-color: #F0FFFF">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line1" value="1"  onclick="showNetForm(this,'voip1');"/>
							</TD>
							<TD width="85%" id="td1" colspan="3">
								<select name="line_id_1" id="line_id_1" class="bk" >
									<option value="1" selected="selected">==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip1" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum1" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip1" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip1" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort1" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip1" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId1" width="35%" >
								<select name="H248DeviceType1" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="A0">==A0==</option>
									<option value="A1">==A1==</option>
									<option value="AG58900">==AG58900==</option>
									<option value="AG58901">==AG58901==</option>
									<option value="USER1001">==USER1001==</option>
									<option value="USER1002">==USER1002==</option>
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>

						<TR style="background-color: #F0FFFF">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line2" value="2" onclick="showNetForm(this,'voip2');"/>
							</TD>
							<TD width="85%" id="td2" colspan="3">
								<select name="line_id_2" id="line_id_2" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" selected="selected">==��·VOIP==</option>
								</select>
							</TD>
						</TR>
							
						<TR id="voip2" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum2" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip2" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip2" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort2" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip2" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId2"  width="35%" >
								<select name="H248DeviceType2" class="bk">
									<option value="-1">==��ѡ��==</option>
									<option value="A0">==A0==</option>
									<option value="A1">==A1==</option>
									<option value="AG58900">==AG58900==</option>
									<option value="AG58901">==AG58901==</option>
									<option value="USER1001">==USER1001==</option>
									<option value="USER1002">==USER1002==</option>
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						<!-- ########################################################################## -->
						<TR style="background-color: #F0FFFF" style="display:none" id="trline3">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line3" value="3" onclick="showNetForm(this,'voip3');"/>
							</TD>
							<TD width="85%" id="td3" colspan="3">
								<select name="line_id_3" id="line_id_3" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" selected="selected">==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip3" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum3" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi3" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip3" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ3" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort3" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip3" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ3" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort3" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip3" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId3"  width="35%" >
								<select name="H248DeviceType3" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline4">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line4" value="4" onclick="showNetForm(this,'voip4');"/>
							</TD>
							<TD width="85%" id="td4" colspan="3">
								<select name="line_id_4" id="line_id_4" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" selected="selected">==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip4" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum4" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi4" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip4" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ4" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort4" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip4" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ4" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort4" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip4" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId4"  width="35%" >
								<select name="H248DeviceType4" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline5">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line5" value="5" onclick="showNetForm(this,'voip5');"/>
							</TD>
							<TD width="85%" id="td5" colspan="3">
								<select name="line_id_5" id="line_id_5" class="bk"  >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" selected="selected">==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip5" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum5" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi5" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip5" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ5" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort5" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip5" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ5" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort5" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip5" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId5"  width="35%" >
								<select name="H248DeviceType5" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline6">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line6" value="6" onclick="showNetForm(this,'voip6');"/>
							</TD>
							<TD width="85%" id="td6" colspan="3">
								<select name="line_id_6" id="line_id_6" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" selected="selected">==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip6" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum6" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi6" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip6" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ6" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort6" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip6" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ6" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort6" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip6" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId6"  width="35%" >
								<select name="H248DeviceType6" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF"  style="display:none" id="trline7">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line7" value="7" onclick="showNetForm(this,'voip7');"/>
							</TD>
							<TD width="85%" id="td7" colspan="3">
								<select name="line_id_7" id="line_id_7" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" selected="selected">==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip7" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum7" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi7" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip7" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ7" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort7" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip7" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ7" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort7" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip7" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId7"  width="35%" >
								<select name="H248DeviceType7" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						<TR style="background-color: #F0FFFF" style="display:none" id="trline8">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line8" value="8" onclick="showNetForm(this,'voip8');"/>
							</TD>
							<TD width="85%" id="td8" colspan="3">
								<select name="line_id_8" id="line_id_8" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" selected="selected">==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip8" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum8" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi8" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip8" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ8" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort8" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip8" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ8" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort8" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip8" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId8"  width="35%" >
								<select name="H248DeviceType8" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						
						
						
						
						
						
						
						
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline9">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line9" value="9" onclick="showNetForm(this,'voip9');"/>
							</TD>
							<TD width="85%" id="td9" colspan="3">
								<select name="line_id_9" id="line_id_9" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" selected="selected">==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip9" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum9" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi9" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip9" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ9" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort9" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip9" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ9" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort9" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip9" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId9"  width="35%" >
								<select name="H248DeviceType9" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline10">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line10" value="10" onclick="showNetForm(this,'voip10');"/>
							</TD>
							<TD width="85%" id="td10" colspan="3">
								<select name="line_id_10" id="line_id_10" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" selected="selected">==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip10" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum10" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi10" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip10" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ10" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort10" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip10" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ10" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort10" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip10" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId10"  width="35%" >
								<select name="H248DeviceType10" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline11">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line11" value="11" onclick="showNetForm(this,'voip11');"/>
							</TD>
							<TD width="85%" id="td11" colspan="3">
								<select name="line_id_11" id="line_id_11" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" selected="selected">==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip11" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum11" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi11" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip11" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ11" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort11" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip11" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ11" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort11" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip11" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId11"  width="35%" >
								<select name="H248DeviceType11" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline12">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line12" value="12" onclick="showNetForm(this,'voip12');"/>
							</TD>
							<TD width="85%" id="td12" colspan="3">
								<select name="line_id_12" id="line_id_12" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" selected="selected">==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip12" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum12" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi12" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip12" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ12" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort12" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip12" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ12" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort12" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip12" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId12"  width="35%" >
								<select name="H248DeviceType12" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline13">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line13" value="13" onclick="showNetForm(this,'voip13');"/>
							</TD>
							<TD width="85%" id="td13" colspan="3">
								<select name="line_id_13" id="line_id_13" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" selected="selected">==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip13" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum13" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi13" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip13" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ13" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort13" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip13" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ13" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort13" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip13" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId13"  width="35%" >
								<select name="H248DeviceType13" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline14">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line14" value="14" onclick="showNetForm(this,'voip14');"/>
							</TD>
							<TD width="85%" id="td14" colspan="3">
								<select name="line_id_14" id="line_id_14" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" selected="selected">==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip14" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum14" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi14" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip14" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ14" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort14" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip14" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ14" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort14" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip14" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId14"  width="35%" >
								<select name="H248DeviceType14" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline15">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line15" value="15" onclick="showNetForm(this,'voip15');"/>
							</TD>
							<TD width="85%" id="td15" colspan="3">
								<select name="line_id_15" id="line_id_15" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" selected="selected">==ʮ��·VOIP==</option>
									<option value="16" >==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip15" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum15" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi15" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip15" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ15" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort15" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip15" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ15" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort15" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip15" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId15"  width="35%" >
								<select name="H248DeviceType15" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						<TR style="background-color: #F0FFFF" style="display:none" id="trline16">
							<TD width="15%" align='left'>
								&nbsp;&nbsp;
								<input type="checkbox" name="h248Line16" value="16" onclick="showNetForm(this,'voip16');"/>
							</TD>
							<TD width="85%" id="td16" colspan="3">
								<select name="line_id_16" id="line_id_16" class="bk" >
									<option value="1" >==һ·VOIP==</option>
									<option value="2" >==��·VOIP==</option>
									<option value="3" >==��·VOIP==</option>
									<option value="4" >==��·VOIP==</option>
									<option value="5" >==��·VOIP==</option>
									<option value="6" >==��·VOIP==</option>
									<option value="7" >==��·VOIP==</option>
									<option value="8" >==��·VOIP==</option>
									<option value="9" >==��·VOIP==</option>
									<option value="10" >==ʮ·VOIP==</option>
									<option value="11" >==ʮһ·VOIP==</option>
									<option value="12" >==ʮ��·VOIP==</option>
									<option value="13" >==ʮ��·VOIP==</option>
									<option value="14" >==ʮ��·VOIP==</option>
									<option value="15" >==ʮ��·VOIP==</option>
									<option value="16" selected="selected">==ʮ��·VOIP==</option>
								</select>
							</TD>
						</TR>
						<TR id="voip16" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="H248PhoneNum16" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע������:</TD>
							<TD width="35%">
								<input type="text" name="H248BiaoShi16" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip16" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="proxServ16" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="proxPort16" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip16" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="standProxServ16" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="standProxPort16" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="voip16" bgcolor="#FFFFFF" style="display:none" >
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD id="H248DeviceTypeId16"  width="35%" >
								<select name="H248DeviceType16" class="bk">
									
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%"><br></TD>
						</TR>
						
						
						
						
						
						
						
						
						
						
						
					</tbody>
					
					<TR align="left" id="doBiz" >
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" onclick="doReset()">��&nbsp;&nbsp;��</button>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
<%@ include file="../../foot.jsp"%>