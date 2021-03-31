<%--
������ͨ�ֹ�����
Author: xiangzl
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
var loginCityId = "-1";
$(function() {
	var gw_type = '<s:property value="gw_type" />';
	loginCityId = '<s:property value="cityId" />';
	$("select[@name='obj.cityId']").val(loginCityId);
	setTime();
	//��ͥ����
	if(gw_type == '1')
	{
		$("select[@name='obj.userType']").val('1');
		$("#h248tab").show();
	}
});

function reset(){
	$("input[@name='obj.loid']").val("");
	cleanValue();
}

function cleanValue()
{
	setTime();
	$("#H248BssSheet").hide();
	
	$("select[@name='obj.cityId']").val(loginCityId);
	$("input[@name='obj.linkman']").val("");
	$("input[@name='obj.linkPhone']").val("");
	$("input[@name='obj.email']").val("");
	$("input[@name='obj.mobile']").val("");
	$("input[@name='obj.linkAddress']").val("");
	$("input[@name='obj.linkmanCredno']").val("");
	$("select[@name='obj.deviceType']").val("1");
	
	$("input[@name='obj.hvoipServTypeId']").attr("checked",false);
	$("select[@name='obj.hvoipRegIdType']").val("-1");
	$("input[@name='obj.hvoipRegId']").val("");
	$("input[@name='obj.hvoipMgcIp']").val("");
	$("input[@name='obj.hvoipMgcPort']").val("2944");
	$("input[@name='obj.hvoipStandMgcIp']").val("");
	$("input[@name='obj.hvoipStandMgcPort']").val("2944");
	$("input[@name='obj.hvoipIpaddress']").val("");
	$("input[@name='obj.hvoipIpmask']").val("");
	$("input[@name='obj.hvoipGateway']").val("");
	$("input[@name='obj.hvoipVlanId']").val("");
	$("input[@name='obj.hvoipPhone']").val("");
	$("input[@name='obj.hvoipPort']").val("");
	$("input[@name='obj.voipPwd']").val("");
}


function setTime(){
	var dstr = "";
	var d = new Date();
	
	dstr += d.getFullYear();
	if(d.getMonth()+1<10)
	{
		dstr += '0';
		dstr += d.getMonth()+1;
	}
	else
	{
		dstr += d.getMonth()+1;
	}
	
	if(d.getDate()<10)
	{
		dstr += '0';
		dstr += d.getDate();
	}
	else
	{
		dstr += d.getDate();
	}
	
	if(d.getHours()<10)
	{
		dstr += '0';
		dstr += d.getHours();
	}
	else
	{
		dstr += d.getHours();
	}
	
	if(d.getMinutes()<10)
	{
		dstr += '0';
		dstr += d.getMinutes();
	}
	else
	{
		dstr += d.getMinutes();
	}
	
	dstr += "00";
	$("input[@name='obj.dealDate']").val(dstr);
	$("input[@name='obj.cmdId']").val(dstr);
}
//��̬ip����ʾ�����ֶ�
function changeStatic(wanType,strip,strdns)
{
	if("3"==wanType)
	{
		$("tr[@id='"+strip+"']").css("display","block");
		$("tr[@id='"+strdns+"']").css("display","block");
	}
	else
	{
		$("tr[@id='"+strip+"']").css("display","none");
		$("tr[@id='"+strdns+"']").css("display","none");
	}
}
//���Ʊ���dns�Ƿ���ʾ
function showStandDns(wanType,strdns)
{
	if("3"==wanType)
	{
		$("tr[@id='"+strdns+"']").css("display","block");
	}
	else
	{
		$("tr[@id='"+strdns+"']").css("display","none");
	}
}
// ���ڶ�̬��ʾ����ҵ�񣨿����IPTV��H248,sip��
function showSheet(objId,name){
    if($("#"+objId).attr("checked")) {
       document.getElementById(name).style.display="";
    } else {
        document.getElementById(name).style.display="none";
    }
}
// reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
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
//�����ims sip ��ʾuri ��domain
function changeIms(protocol)
{
	if("0"==protocol)
	{
		$("tr[@id='ims']").css("display","block");
		$("input[@name='obj.sipUserAgentDomain']").val("ah.ctcims.cn");
	}
	else
	{
		$("tr[@id='ims']").css("display","none");
		$("input[@name='obj.sipUserAgentDomain']").val("");
	}
}

//���LOID�Ƿ����
function checkLoid(flag)
{
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	if("" == $("input[@name='obj.loid']").val().trim())
	{
		alert("LOID����Ϊ�գ�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	cleanValue();
	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4NXLT!checkLoid.action'/>";
	$.post(url,{
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":$("input[@name='obj.loid']").val().trim()
	},function(ajax){
		if("000" == ajax)
		{
			alert("LOID������,����ʹ��");
		}
		else
		{
			if(flag){
				alert("ϵͳ�д��ڸ�lOID");
			}
			
			$("#hvoipServTypeId").attr("checked",false); 
			var relt = ajax.split("|");
			//loid���ڣ����޸�
			$("#userOperateId").val(relt[4]);
			$("select[@name='obj.cityId']").val(relt[8]);
			$("input[@name='obj.officeId']").val(relt[9]);
			$("input[@name='obj.areaId']").val(relt[10]);
			$("select[@name='obj.accessStyle']").val(relt[11]);
			$("input[@name='obj.linkman']").val(relt[12]);
			$("input[@name='obj.linkPhone']").val(relt[13]);
			$("input[@name='obj.email']").val(relt[14]);
			$("input[@name='obj.mobile']").val(relt[15]);
			$("input[@name='obj.linkAddress']").val(relt[16]);
			$("input[@name='obj.linkmanCredno']").val(relt[17]);
			var specId = relt[20].replace(/,/g,"|");
 			$("select[@name='obj.specId']").val(specId);
			$("select[@name='obj.deviceType']").val(relt[84]);
			
			//h248����
			if("15" == relt[31])
			{
				$("#hvoipServTypeId").attr("checked",true); 
				$("input[@name='obj.hvoipOltFactory']").val(relt[97]);
				$("input[@name='obj.hvoipOperateId']").val(relt[32]);
				$("input[@name='obj.hvoipPhone']").val(relt[33]);
				$("input[@name='obj.hvoipRegId']").val(relt[34]);
				if("0"==relt[35]){
					$("select[@name='obj.hvoipRegIdType']").val("1");
				}
				else if("1"==relt[35]){
					$("select[@name='obj.hvoipRegIdType']").val("2");
				}
				else{
					$("select[@name='obj.hvoipRegIdType']").val("-1");
				}
				$("input[@name='obj.hvoipMgcIp']").val(relt[36]);
				$("input[@name='obj.hvoipMgcPort']").val(relt[37]);
				$("input[@name='obj.hvoipStandMgcIp']").val(relt[38]);
				$("input[@name='obj.hvoipStandMgcPort']").val(relt[39]);
				$("input[@name='obj.hvoipPort']").val(relt[40]);
				$("input[@name='obj.hvoipVlanId']").val(relt[41]);
				$("select[@name='obj.hvoipWanType']").val(relt[42]);
				$("input[@name='obj.hvoipIpaddress']").val(relt[43]);
				$("input[@name='obj.hvoipIpmask']").val(relt[44]);
				$("input[@name='obj.hvoipGateway']").val(relt[45]);
				$("input[@name='obj.hvoipIpdns']").val(relt[46]);
				$("input[@name='obj.voipPwd']").val(relt[relt.length - 1]);
				$("input[@name='isH248SendPost']").val("2");
 				showSheet('hvoipServTypeId','H248BssSheet');
				/* if("3" == relt[42])
				{
					changeStatic(relt[42],'h248IP','h248Dns');
				} */
 				changeStatic(relt[42],'h248IP','h248Dns');
			}
		}
	});
}
 
//ҵ�������ύ
function delBusiness()
{
	var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
	var loid = $("input[@name='obj.loid']").val().trim();
	var cityId = $("select[@name='obj.cityId']").val();
	
	
	if("" == loid)
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}

	if("-1" == $("select[@name='obj.cityId']").val())
	{
		alert("���ز���Ϊ�ա�");
		$("select[@name='obj.cityId']").focus();
		return false;
	}
	
	var alertstr = "ȷ��Ҫɾ��";
	if(hvoipServTypeId != ""){
		alertstr = alertstr+"VOIP����";
	}
	var r=confirm(alertstr+"ҵ���� ��");
  	if (r==false){
    	return ;
    } 
	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4NXLT!delBusiness.action'/>";
	$.post(url,{
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.loid":loid,
		"obj.cityId":cityId,
		"obj.hvoipServTypeId":hvoipServTypeId,
		"obj.hvoipPhone":$("input[@name='obj.hvoipPhone']").val()
	},function(ajax){
		alert(ajax);
		$("button[@name='delBtn']").attr("disabled", true);
		checkLoid(false);
	});
	//�һ���ť
	$("button[@name='delBtn']").attr("disabled", true);
}



//����ҵ���ύ
function doBusiness()
{
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	loidvalue = $("input[@name='obj.loid']").val();	 
	if("" == loidvalue || loidvalue == null || loidvalue == undefined){
		alert("Loid����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	if("-1" == $("select[@name='obj.cityId']").val())
	{
		alert("���ز���Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	if("-1" == $("select[@name='obj.accessStyle']").val())
	{
		alert("�ն˽������Ͳ���Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	
	var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
	if("" == $("input[@name='obj.hvoipPhone']").val())
	{
		alert("�绰ҵ���߼��Ų���Ϊ�ա�");
		$("input[@name='obj.hvoipPhone']").focus();
		return false;
	}
	if("" == $("input[@name='obj.hvoipRegId']").val())
	{
		alert("�ն˱�ʾ��������Ϊ�ա�");
		$("input[@name='obj.hvoipRegId']").focus();
		return false;
	}
	else if(!reg_verify($("input[@name='obj.hvoipRegId']").val())){
		alert("�ն˱�ʾ�������ǺϷ�IP��ַ��");
		$("input[@name='obj.hvoipRegId']").focus();
		return false;
	}
	if("" == $("select[@name='obj.hvoipRegIdType']").val())
	{
		alert("�ն˱�ʾ���Ͳ���Ϊ�ա�");
		$("select[@name='obj.hvoipRegIdType']").focus();
		return false;
	}
	if("" == $("input[@name='obj.hvoipMgcIp']").val())
	{
		alert("����MGC��ַ����Ϊ�ա�");
		$("input[@name='obj.hvoipMgcIp']").focus();
		return false;
	}
	else if(!reg_verify($("input[@name='obj.hvoipMgcIp']").val())){
		alert("����MGC��ַ���ǺϷ�IP��ַ��");
		$("input[@name='obj.hvoipMgcIp']").focus();
		return false;
	}
	if("" == $("input[@name='obj.hvoipMgcPort']").val())
	{
		alert("����MGC�˿ڲ���Ϊ�ա�");
		$("input[@name='obj.hvoipMgcPort']").focus();
		return false;
	}
	if("" == $("input[@name='obj.hvoipStandMgcIp']").val())
	{
		alert("����MGC��ַ����Ϊ�ա�");
		$("input[@name='obj.hvoipStandMgcIp']").focus();
		return false;
	}
	else if(!reg_verify($("input[@name='obj.hvoipStandMgcIp']").val())){
		alert("����MGC��ַ���ǺϷ�IP��ַ��");
		$("input[@name='obj.hvoipStandMgcIp']").focus();
		return false;
	}
	if("" == $("input[@name='obj.hvoipStandMgcPort']").val())
	{
		alert("����MGC�˿ڲ���Ϊ�ա�");
		$("input[@name='obj.hvoipStandMgcPort']").focus();
		return false;
	}
	if("" == $("input[@name='obj.hvoipVlanId']").val())
	{
		alert("VLANDID����Ϊ��");
		$("input[@name='obj.hvoipVlanId']").focus();
		return false;
	}
	
	var wanType = $("select[@name='obj.hvoipWanType']").val();
	if(wanType == 3){
		if("" == $("input[@name='obj.hvoipIpaddress']").val())
		{
			alert("ont������ҵ���ַ����Ϊ�ա�");
			$("input[@name='obj.hvoipIpaddress']").focus();
			return false;
		}
		else if(!reg_verify($("input[@name='obj.hvoipIpaddress']").val())){
			alert("ont������ҵ���ַ���ǺϷ�IP��ַ��");
			$("input[@name='obj.hvoipIpaddress']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipIpmask']").val())
		{
			alert("ont������ҵ���ַ���벻��Ϊ�ա�");
			$("input[@name='obj.hvoipIpmask']").focus();
			return false;
		}
		else if(!reg_verify($("input[@name='obj.hvoipIpmask']").val())){
			alert("ont������ҵ���ַ���벻�ǺϷ�IP��ַ��");
			$("input[@name='obj.hvoipIpmask']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipGateway']").val())
		{
			alert("ont����ҵ�����ز���Ϊ�ա�");
			$("input[@name='obj.hvoipGateway']").focus();
			return false;
		}
		else if(!reg_verify($("input[@name='obj.hvoipGateway']").val())){
			alert("ont������ҵ�����ز��ǺϷ�IP��ַ��");
			$("input[@name='obj.hvoipGateway']").focus();
			return false;
		} 
	}
	var specId = $("select[@name='obj.specId']").val();
  	if(specId != "" && specId != null && specId != undefined){
  		specId = specId.trim().split("|")[2];
  	}
	var alertstr = "ȷ��Ҫ�ύ";
	if(hvoipServTypeId != ""){
		alertstr = alertstr+"VOIP����";
	}
	var r=confirm(alertstr+"ҵ���� ��");
  	if (r==false){
    	return ;
    }
  	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4NXLT!doBusiness.action'/>";
	$.post(url,{
		"obj.deviceType":$("select[@name='obj.deviceType']").val(),
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.authUser":$("input[@name='obj.authUser']").val(),
		"obj.authPwd":$("input[@name='obj.authPwd']").val(),
		"obj.userServTypeId":$("input[@name='obj.userServTypeId']").val(),
		"obj.userOperateId":$("input[@name='obj.userOperateId']").val(),
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue.trim(),
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.officeId":$("input[@name='obj.officeId']").val(),
		"obj.areaId":$("input[@name='obj.areaId']").val(),
		"obj.accessStyle":$("select[@name='obj.accessStyle']").val(),
		"obj.linkman":$("input[@name='obj.linkman']").val(),
		"obj.linkPhone":$("input[@name='obj.linkPhone']").val(),
		"obj.email":$("input[@name='obj.email']").val(),
		"obj.mobile":$("input[@name='obj.mobile']").val(),
		"obj.linkAddress":$("input[@name='obj.linkAddress']").val(),
		"obj.linkmanCredno":$("input[@name='obj.linkmanCredno']").val(),
 		"obj.specId":specId,
		
		"obj.hvoipServTypeId":hvoipServTypeId,
		"obj.hvoipOperateId":$("input[@name='obj.hvoipOperateId']").val(),
		"obj.hvoipOltFactory":$("input[@name='obj.hvoipOltFactory']").val(),
		"obj.hvoipPhone":$("input[@name='obj.hvoipPhone']").val(),
		"obj.hvoipRegId":$("input[@name='obj.hvoipRegId']").val(),
		"obj.hvoipRegIdType":$("select[@name='obj.hvoipRegIdType']").val(),
		"obj.hvoipMgcIp":$("input[@name='obj.hvoipMgcIp']").val(),
		"obj.hvoipMgcPort":$("input[@name='obj.hvoipMgcPort']").val(),
		"obj.hvoipStandMgcIp":$("input[@name='obj.hvoipStandMgcIp']").val(),
		"obj.hvoipStandMgcPort":$("input[@name='obj.hvoipStandMgcPort']").val(),
		"obj.hvoipPort":$("input[@name='obj.hvoipPort']").val(),
		"obj.hvoipVlanId":$("input[@name='obj.hvoipVlanId']").val(),
		"obj.hvoipWanType":$("select[@name='obj.hvoipWanType']").val(),
		"obj.hvoipIpaddress":$("input[@name='obj.hvoipIpaddress']").val(),
		"obj.hvoipIpmask":$("input[@name='obj.hvoipIpmask']").val(),
		"obj.hvoipGateway":$("input[@name='obj.hvoipGateway']").val(),
		"obj.hvoipIpdns":$("input[@name='obj.hvoipIpdns']").val(),
		"obj.voipPwd":$("input[@name='obj.voipPwd']").val()
	},function(ajax){
		alert(ajax);
		//�һ���ť
		$("button[@name='subBtn']").attr("disabled", false);
		checkLoid(false);
	});
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
}
//�˿ڱ������̬���´˶˿ڵ�ҵ����Ϣ�����������������
function changeVoipPort(voipPort,voipType)
{
	var url = "<s:url value='/itms/service/bssSheetByHand4NXLT!changeVoipPort.action'/>";
	var loid = $("input[@name='obj.loid']").val();
	var userType = $("select[@name='obj.userType']").val();
	//����������������첽���󣬻�ȡ����
	var sipProtocol = $("select[@name='obj.sipProtocol']").val();
	var sipSendPost = $("#isSipSendPost").val();
	var h248SendPost = $("#isH248SendPost").val();
	if("h248" == voipType && "2" == h248SendPost)
	{
		$.post(url,{
			"obj.loid":loid,
			"obj.hvoipServTypeId":"14",
			"obj.sipVoipPort":voipPort,
			"obj.userType":userType,
			"obj.sipProtocol":"2"
		},function(ajax){
			if("-1" == ajax)
			{
				//�������ͣ�1���������ֶ��ÿ���
				var relt = ajax.split("|");
				$("input[@name='obj.hvoipOperateId']").val("1");
				$("input[@name='obj.hvoipPhone']").val("");
				$("input[@name='obj.hvoipRegId']").val("");
				$("select[@name='obj.hvoipRegIdType']").val("");
				$("input[@name='obj.hvoipMgcIp']").val("");
				$("input[@name='obj.hvoipMgcPort']").val("");
				$("input[@name='obj.hvoipStandMgcIp']").val("");
				$("input[@name='obj.hvoipStandMgcPort']").val("");
				$("input[@name='obj.hvoipVlanId']").val("");
				$("select[@name='obj.hvoipWanType']").val("-1");
				$("input[@name='obj.hvoipIpaddress']").val("");
				$("input[@name='obj.hvoipIpmask']").val("");
				$("input[@name='obj.hvoipGateway']").val("");
				$("input[@name='obj.hvoipIpdns']").val("");
				
				changeStatic(-1,'h248IP','h248Dns')
			}
			else
			{
				//��ʾԭʼ����
				var relt = ajax.split("|");
				$("input[@name='obj.hvoipOperateId']").val(relt[0]);
				$("input[@name='obj.hvoipPhone']").val(relt[1]);
				$("input[@name='obj.hvoipRegId']").val(relt[2]);
				$("select[@name='obj.hvoipRegIdType']").val(relt[3]);
				$("input[@name='obj.hvoipMgcIp']").val(relt[4]);
				$("input[@name='obj.hvoipMgcPort']").val(relt[5]);
				$("input[@name='obj.hvoipStandMgcIp']").val(relt[6]);
				$("input[@name='obj.hvoipStandMgcPort']").val(relt[7]);
				$("input[@name='obj.hvoipVlanId']").val(relt[8]);
				$("select[@name='obj.hvoipWanType']").val(relt[9]);
				$("input[@name='obj.hvoipIpaddress']").val(relt[10]);
				$("input[@name='obj.hvoipIpmask']").val(relt[11]);
				$("input[@name='obj.hvoipGateway']").val(relt[12]);
				$("input[@name='obj.hvoipIpdns']").val(relt[13]);
				
				changeStatic(relt[9],'h248IP','h248Dns');
				
			}
		});
	}
}

</script>
</HEAD>
<body>
<FORM NAME="frm" action="" method="post">
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
						<div align="center" class="title_bigwhite">�ֹ�����</div>
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
						<input type="hidden" id="userServTypeId" name = "obj.userServTypeId" value="20">
						<input type="hidden" id="userOperateId" name = "obj.userOperateId" value="1">
						<input type="hidden"  name = "obj.cmdId" value="">
						<input type="hidden"  name = "obj.authUser" value="itms">
						<input type="hidden"  name = "obj.authPwd" value="123">
						<td colspan="4" class="green_title_left">
						�û����Ϲ���
						</td>
					</tr>
					<tbody id="jirRuBssSheet" >
						<TR bgcolor="#FFFFFF" >
							<TR bgcolor="#FFFFFF" >
									<TD width="15%" class=column align="right">�û����ͣ�</TD>
									<TD width="35%" >
										<SELECT name="obj.userType"  class="bk" disabled>
											<OPTION value="1">��ͥ����</OPTION>
										</SELECT>
										<font color="#FF0000">*</font>
									</TD>
									<TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
									<TD width="35%" >
										<input type="text" name="obj.dealDate" class=bk value="" disabled><font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD width="15%" class=column align="right">LOID:</TD>
									<TD width="35%" >
										<input type="text" id="loid" name="obj.loid" class=bk value="">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="button" name="subButton" onclick="checkLoid(true)" value='���LOID�Ƿ����'>
									</TD>
									<TD class=column align="right" nowrap width="15%">����:</TD>
									<TD width="35%" >
										<s:select list="cityList" name="obj.cityId"
										headerKey="-1" headerValue="��ѡ������" listKey="city_id"
										listValue="city_name" value="cityId" cssClass="bk"></s:select>
										<font color="#FF0000">*</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">�����ʾ:</TD>
									<TD width="35%" ><input type="text" name="obj.officeId" class=bk value=""></TD>
									<TD class=column align="right" nowrap width="15%">�����ʾ:</TD>
									<TD width="35%" ><input type="text" name="obj.areaId" class=bk value=""></TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">�ն˽�������:</TD>
									<TD width="35%" >
										<SELECT name="obj.accessStyle" class="bk" >
											<OPTION value="-1">--��ѡ��--</OPTION>
											<OPTION value="1">ADSL</OPTION>
											<OPTION value="2">LAN</OPTION>
											<OPTION value="3">EPON</OPTION>
											<OPTION value="4">GPON</OPTION>
										</SELECT>
										&nbsp;<font color="#FF0000">*</font>
									</TD>
									<TD width="15%" class=column align="right">�ն˹��:</TD>
									<TD id='specid' width="35%" >
										<s:select list="specIdList" name="obj.specId" headerKey="-1" headerValue="��ѡ��" 
										listKey="spec" listValue="spec_name" value="spec_name"  
										cssClass="bk">
										</s:select>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">��ϵ�绰:</TD>
									<TD width="35%" >
										<input type="text" name="obj.linkPhone" class=bk value="">
									</TD>
									<TD width="15%" class=column align="right">��ϵ��:</TD>
									<TD width="35%" >
										<input type="text" name="obj.linkman" class=bk value="">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">�ֻ�:</TD>
									<TD width="35%" >
										<input type="text" name="obj.mobile" class=bk value="" maxlength="15">
									</TD>
									<TD class=column align="right" nowrap width="15%">��������:</TD>
									<TD width="35%" >
										<input type="text" name="obj.email" class=bk value="">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD class=column align="right" nowrap width="15%">֤������:</TD>
									<TD width="35%" >
										<input type="text" name="obj.linkmanCredno" class=bk value="">
									</TD>
									<TD width="15%" class=column align="right">��ַ:</TD>
									<TD  width="35%" >
										<input type="text" name="obj.linkAddress" size="55" class=bk value="">
									</TD>
								</TR>
							</tbody>
							
					<tr id="h248tab" align="left" style="display:none">
						<td colspan="4" class="green_title_left">
						<input type="hidden" id="hvoipOperateId" name = "obj.hvoipOperateId" value="1"/>
						<input type="hidden" name = "isH248SendPost" id="isH248SendPost" value="0">
						<input type="checkbox" name="obj.hvoipServTypeId" value="15" id="hvoipServTypeId" onclick="showSheet('hvoipServTypeId','H248BssSheet');"/>
						H248����ҵ�񹤵�
						</td>
					</tr>
					<tbody id="H248BssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">�ն˱�ʶ������:</TD>
							<TD width="35%" >
								<select name="obj.hvoipRegIdType" class="bk" >
									<option value="-1">==��ѡ��==</option>
									<option value="1">==IP==</option>
									<option value="2">==DomainName==</option>
								</select>
							</TD>
							<TD width="15%" class=column align="right">�ն˱�ʶ����:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipRegId" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipMgcIp" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipMgcPort" class=bk value="2944">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����MGC��ַ:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipStandMgcIp" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipStandMgcPort" class=bk value="2944">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">	
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<select name="obj.hvoipWanType" class="bk" onChange="changeStatic(this.value,'h248IP','h248Dns')">
								<option value="-1">==��ѡ���������==</option>
								<option value="1">==�Ž�==</option>
								<option value="2">==·��==</option>
								<option value="3">==STATIC==</option>
								<option value="4">==DHCP==</option>
							</select>
							&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">VLANID</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipVlanId" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="h248IP" bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipIpaddress" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpmask" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="h248Dns" bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipGateway" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpdns" class=bk value="">
								&nbsp;<font color="#FF0000"></font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">�ն������ʾ:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipPort" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipPhone" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%">
								<input type="password" name="obj.voipPwd" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%"></TD>
							<TD width="35%">
							</TD>
						</TR>
					</tbody>	
					<TR align="left" id="doBiz" >
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" onclick="delBusiness()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" onclick="reset()">��&nbsp;&nbsp;��</button>
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
<%@ include file="../../../foot.jsp"%>