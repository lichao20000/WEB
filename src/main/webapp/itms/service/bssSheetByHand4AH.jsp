<%--
���յ����ֹ�����
Author: Jason
Version: 1.0.0
Date: 2012-09-14
--%>

<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
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

$(function() {
	
	var gw_type = '<s:property value="gw_type" />';
	//��ͥ����
	if(gw_type == '1')
	{
		$("select[@name='obj.userType']").val('1');
		$("input[@name='obj.cmdId']").val('FROMWEB-0000002');
		$("#siptab").show();
		
	}
	if(gw_type == '2')//��ҵ����
	{
		$("select[@name='obj.userType']").val('2');
		//�޸Ĵ� os_jiangk
		$("#siptab").show();
	}
	changeUserType(gw_type);
	
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
	$("#delbutton").hide();
});

//���Ƽ�ͥ���غ���ҵ������ʾ����
function changeUserType(type)
{
	if("1" == type)
	{
		$("tr[@id='egw_cust']").css("display","none");
		//$("tr[@id='h248tab']").css("display","none");
		//$("tbody[@id='H248BssSheet']").css("display","none");
		//$("#hvoipServTypeId").attr("checked",false);
		//$("tr[@id='siptab']").css("display","block");
		$("TD[@id='speechgw1']").css("display","");
		$("TD[@id='speechgw2']").css("display","none");
	}
	else
	{
		$("tr[@id='egw_cust']").css("display","block");
		//$("tr[@id='h248tab']").css("display","block");
		//$("tr[@id='siptab']").css("display","none");
		//$("tbody[@id='sipBssSheet']").css("display","none");
		//$("#sipServTypeId").attr("checked",false);
		$("TD[@id='speechgw1']").css("display","none");
		$("TD[@id='speechgw2']").css("display",""); 
	}
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
function checkLoid()
{
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�գ�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4AH!checkLoid.action'/>";
	$.post(url,{
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":$("input[@name='obj.loid']").val()
	},function(ajax){
		if("000" == ajax)
		{
			alert("LOID����ʹ�á�");
		}
		else
		{
			//alert(ajax);
			$("#netServTypeId").attr("checked",false); 
			$("#hvoipServTypeId").attr("checked",false); 
			$("#sipServTypeId").attr("checked",false); 
			var relt = ajax.split("|");
			$("input[@name='obj.userOperateId']").val(relt[4]);
			//$("input[@name='obj.dealDate']").val();
			//$("select[@name='obj.userType']").val();
			//$("input[@name='obj.loid']").val();
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
			$("input[@name='obj.customerId']").val(relt[18]);
			$("input[@name='obj.customerAccount']").val(relt[19]);
			//$("input[@name='obj.customerPwd']").val();
			$("select[@name='obj.specId']").val(relt[20]);
			if("22" == relt[21])
			{
				$("#netServTypeId").attr("checked",true); 
				$("input[@name='obj.netOperateId']").val(relt[22]);
				$("input[@name='obj.netUsername']").val(relt[23]);
				$("input[@name='obj.netPasswd']").val(relt[24]);
				$("input[@name='obj.netVlanId']").val(relt[25]);
				$("select[@name='obj.netWanType']").val(relt[26]);
				$("input[@name='obj.netIpaddress']").val(relt[27]);
				$("input[@name='obj.netIpmask']").val(relt[28]);
				$("input[@name='obj.netGateway']").val(relt[29]);
				$("input[@name='obj.netIpdns']").val(relt[30]);
				$("input[@name='obj.standNetIpdns']").val(relt[74]);
				//showSheet('netServTypeId','internetBssSheet');
				if("3" == relt[26])
				{
					changeStatic(relt[26],'netip','netDns');
					showStandDns(relt[26],'standnetDns');
				}
			}
			if("15" == relt[31])
			{
				$("#hvoipServTypeId").attr("checked",true); 
				$("input[@name='obj.hvoipOperateId']").val(relt[32]);
				$("input[@name='obj.hvoipPhone']").val(relt[33]);
				$("input[@name='obj.hvoipRegId']").val(relt[34]);
				$("input[@name='obj.hvoipRegIdType']").val(relt[35]);
				$("input[@name='obj.hvoipMgcIp']").val(relt[36]);
				$("input[@name='obj.hvoipMgcPort']").val(relt[37]);
				$("input[@name='obj.hvoipStandMgcIp']").val(relt[38]);
				$("input[@name='obj.hvoipStandMgcPort']").val(relt[39]);
				$("select[@name='obj.hvoipPort']").val(relt[40]);
				$("input[@name='obj.hvoipVlanId']").val(relt[41]);
				$("select[@name='obj.hvoipWanType']").val(relt[42]);
				$("input[@name='obj.hvoipIpaddress']").val(relt[43]);
				$("input[@name='obj.hvoipIpmask']").val(relt[44]);
				$("input[@name='obj.hvoipGateway']").val(relt[45]);
				$("input[@name='obj.hvoipIpdns']").val(relt[46]);
				$("input[@id='isH248SendPost']").val("2");
				//showSheet('hvoipServTypeId','H248BssSheet');
			}
			if("14" == relt[47])
			{
				$("#sipServTypeId").attr("checked",true); 
				changeIms(relt[65]);
				$("input[@name='obj.sipOperateId']").val(relt[48]);
				$("input[@name='obj.sipVoipPhone']").val(relt[49]);
				$("input[@name='obj.sipVoipUsername']").val(relt[50]);
				$("input[@name='obj.sipVoipPwd']").val(relt[51]);
				$("input[@name='obj.sipProxServ']").val(relt[52]);
				$("input[@name='obj.sipProxPort']").val(relt[53]);
				$("input[@name='obj.sipStandProxServ']").val(relt[54]);
				$("input[@name='obj.sipStandProxPort']").val(relt[55]);
				$("select[@name='obj.sipVoipPort']").val(relt[56]);
				$("select[@name='obj.sipVoipPort1']").val(relt[56]);
				//$("select[@name='obj.lineList']").val(relt[56]);
				$("input[@name='obj.sipRegiServ']").val(relt[57]);
				$("input[@name='obj.sipRegiPort']").val(relt[58]);
				$("input[@name='obj.sipStandRegiServ']").val(relt[59]);
				$("input[@name='obj.sipStandRegiPort']").val(relt[60]);
				$("input[@name='obj.sipOutBoundProxy']").val(relt[61]);
				$("input[@name='obj.sipOutBoundPort']").val(relt[62]);
				$("input[@name='obj.sipStandOutBoundProxy']").val(relt[63]);
				$("input[@name='obj.sipStandOutBoundPort']").val(relt[64]);
				$("select[@name='obj.sipProtocol']").val(relt[65]);
				$("input[@name='obj.sipVlanId']").val(relt[66]);
				$("select[@name='obj.sipWanType']").val(relt[67]);
				$("input[@name='obj.sipIpaddress']").val(relt[68]);
				$("input[@name='obj.sipIpmask']").val(relt[69]);
				$("input[@name='obj.sipGateway']").val(relt[70]);
				$("input[@name='obj.sipIpdns']").val(relt[71]);
				$("input[@name='obj.sipVoipUri']").val(relt[72]);
				$("input[@name='obj.sipUserAgentDomain']").val(relt[73]);
				$("input[@id='isSipSendPost']").val("2");
				
				showSheet('sipServTypeId','sipBssSheet');
				$("#delbutton").show();
			}
		}
	});
}
//���customerId�Ƿ����
function checkCustomerId()
{
	if("" == $("input[@name='obj.customerId']").val())
	{
		alert("�ͻ�id�������롣");
		$("input[@name='obj.customerId']").focus();
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4AH!checkCustomerId.action'/>";
	$.post(url,{
		"obj.customerId":$("input[@name='obj.customerId']").val()
	},function(ajax){
		if("-1" == ajax)
		{
			alert("�ͻ�ID�Ѿ����ڣ����������롣");
			$("input[@name='obj.customerId']").focus();
		}
		else
		{
			alert("�ͻ�ID����ʹ�á�");
		}
	});
}

//�ύҵ��
function doBusiness()
{
	var loidvalue = "";
	var gw_type = '<s:property value="gw_type" />';
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	if("2" == $("select[@name='obj.userType']").val() && "" == $("input[@name='obj.customerId']").val())
	{
		alert("�ͻ�ID����Ϊ�ա�");
		$("input[@name='obj.customerId']").focus();
		return false;
	}
	//��ҵ���أ�lan���У�û��loid=��bbms+�ͻ�id������ͥ���ز���
	if("2"!=$("select[@name='obj.accessStyle']").val() )
	{
		if("" == $("input[@name='obj.loid']").val())
		{
			alert("LOID����Ϊ�ա�");
			$("input[@name='obj.loid']").focus();
			return false;
		}
		loidvalue = $("input[@name='obj.loid']").val();	
	}
	else
	{
		loidvalue = "bbms"+$("input[@name='obj.customerId']").val();
	}
	if("1" == $("select[@name='obj.userType']").val())
	{
		loidvalue = $("input[@name='obj.loid']").val();	
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
	var netServTypeId = $("input[@name='obj.netServTypeId'][checked]").val();
	var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
	var sipServTypeId = $("input[@name='obj.sipServTypeId'][checked]").val();
	if("22" != netServTypeId)
	{
		netServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.netUsername']").val())
		{
			alert("����˺Ų���Ϊ�ա�");
			$("input[@name='obj.netUsername']").focus();
			return false;
		}
		if("" == $("input[@name='obj.netPasswd']").val())
		{
			alert("������벻��Ϊ�ա�");
			$("input[@name='obj.netPasswd']").focus();
			return false;
		}
		if("" == $("input[@name='obj.netVlanId']").val())
		{
			alert("VLANDID����Ϊ�ա�");
			$("input[@name='obj.netVlanId']").focus();
			return false;
		}
		if("-1" == $("select[@name='obj.netWanType']").val())
		{
			alert("������ʽ����Ϊ�ա�");
			$("select[@name='obj.netWanType']").focus();
			return false;
		}
	}
	if("15" != hvoipServTypeId)
	{
		hvoipServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.hvoipPhone']").val())
		{
			alert("ҵ��绰���벻��Ϊ�ա�");
			$("input[@name='obj.hvoipPhone']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipRegId']").val())
		{
			alert("�ն˱�ʾ��������Ϊ�ա�");
			$("input[@name='obj.hvoipRegId']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipRegIdType']").val())
		{
			alert("�ն˱�ʾ���Ͳ���Ϊ�ա�");
			$("input[@name='obj.hvoipRegIdType']").focus();
			return false;
		}
		if("-1" == $("select[@name='obj.hvoipPort']").val())
		{
			alert("�ն������ʾ����Ϊ�ա�");
			return false;
		}
		if("" == $("input[@name='obj.hvoipMgcIp']").val())
		{
			alert("����MGC��ַ����Ϊ�ա�");
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
		if("" == $("select[@name='obj.hvoipWanType']").val())
		{
			alert("���з�ʽ����Ϊ�ա�");
			$("select[@name='obj.hvoipWanType']").focus();
			return false;
		}
	}
	if("14" != sipServTypeId)
	{
		sipServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.sipVoipPhone']").val())
		{
			alert("ҵ��绰���벻��Ϊ�ա�");
			$("input[@name='obj.sipVoipPhone']").focus();
			return false;
		}
	
		if("" == $("input[@name='obj.sipVoipUsername']").val())
		{
			alert("��֤�˺Ų���Ϊ�ա�");
			$("input[@name='obj.sipVoipUsername']").focus();
			return false;
		}
		var _sipVoipPwd = $("input[@name='obj.sipVoipPwd']").val();
		if("" == _sipVoipPwd)
		{
			alert("��֤���벻��Ϊ�ա�");
			$("input[@name='obj.sipVoipPwd']").focus();
			return false;
		}
		if (!/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])^[0-9A-Za-z]{10,}$/.test(_sipVoipPwd)) {
			alert("��֤������������Сд�ַ������֣��ҳ��Ȳ���С��10λ");
			$("input[@name='obj.sipVoipPwd']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipProxServ']").val())
		{
			alert("���ô������������Ϊ�ա�");
			$("input[@name='obj.sipProxServ']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipStandProxServ']").val())
		{
			alert("���ô������������Ϊ�ա�");
			$("input[@name='obj.sipStandProxServ']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipRegiServ']").val())
		{
			alert("����ע�����������Ϊ�ա�");
			$("input[@name='obj.sipRegiServ']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipStandRegiServ']").val())
		{
			alert("����ע�����������Ϊ��");
			$("input[@name='obj.sipStandRegiServ']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipOutBoundProxy']").val())
		{
			alert("���ð󶨷���������Ϊ�ա�");
			$("input[@name='obj.sipOutBoundProxy']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipStandOutBoundPort']").val())
		{
			alert("���ð󶨷���������Ϊ�ա�");
			$("input[@name='obj.sipStandOutBoundPort']").focus();
			return false;
		}
		if("-1" == $("select[@name='obj.sipProtocol']").val())
		{
			alert("Э�����ͱ���ѡ��");
			$("select[@name='obj.sipProtocol']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipVoipUri']").val() && "0" == $("select[@name='obj.sipProtocol']").val())
		{
			alert("URI����Ϊ�ա�");
			$("input[@name='obj.sipVoipUri']").focus();
			return false;
		}
	}
	//$("#lineList option:selected").text().trim()
	var sipVoipPort="";
	//ҵ�񴥷�
	if(gw_type=="2")
		{
		//sipVoipPort=$("#VoipPort option:selected").text().trim();
		sipVoipPort=$("select[@name='obj.sipVoipPort1']").val();
		}
	else
		{
		sipVoipPort=$("select[@name='obj.sipVoipPort']").val();
		}
	
	if("14" == sipServTypeId)
		{
	 if(sipVoipPort=="-1")
		{
			alert("�����˿ڱ���ѡ��");
			$("select[@name='obj.sipVoipPort']").focus();
			return false;
		}
		}
		var url = "<s:url value='/itms/service/bssSheetByHand4AH!doBusiness.action'/>";
		$.post(url,{
			"obj.cmdId":$("input[@name='obj.cmdId']").val(),
			"obj.authUser":$("input[@name='obj.authUser']").val(),
			"obj.authPwd":$("input[@name='obj.authPwd']").val(),
			"obj.userServTypeId":$("input[@name='obj.userServTypeId']").val(),
			"obj.userOperateId":$("input[@name='obj.userOperateId']").val(),
			"obj.dealDate":$("input[@name='obj.dealDate']").val(),
			"obj.userType":$("select[@name='obj.userType']").val(),
			"obj.loid":loidvalue,
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
			"obj.customerId":$("input[@name='obj.customerId']").val(),
			"obj.customerAccount":$("input[@name='obj.customerAccount']").val(),
			//"obj.customerPwd":$("input[@name='obj.customerPwd']").val(),
			"obj.specId":$("select[@name='obj.specId']").val(),
			
			"obj.netServTypeId":netServTypeId,
			"obj.netOperateId":$("input[@name='obj.netOperateId']").val(),
			"obj.netUsername":$("input[@name='obj.netUsername']").val(),
			"obj.netPasswd":$("input[@name='obj.netPasswd']").val(),
			"obj.netVlanId":$("input[@name='obj.netVlanId']").val(),
			"obj.netWanType":$("select[@name='obj.netWanType']").val(),
			"obj.netIpaddress":$("input[@name='obj.netIpaddress']").val(),
			"obj.netIpmask":$("input[@name='obj.netIpmask']").val(),
			"obj.netGateway":$("input[@name='obj.netGateway']").val(),
			"obj.netIpdns":$("input[@name='obj.netIpdns']").val(),
			"obj.standNetIpdns":$("input[@name='obj.standNetIpdns']").val(),
			
			"obj.hvoipServTypeId":hvoipServTypeId,
			"obj.hvoipOperateId":$("input[@name='obj.hvoipOperateId']").val(),
			"obj.hvoipPhone":$("input[@name='obj.hvoipPhone']").val(),
			"obj.hvoipRegId":$("input[@name='obj.hvoipRegId']").val(),
			"obj.hvoipRegIdType":$("input[@name='obj.hvoipRegIdType']").val(),
			"obj.hvoipMgcIp":$("input[@name='obj.hvoipMgcIp']").val(),
			"obj.hvoipMgcPort":$("input[@name='obj.hvoipMgcPort']").val(),
			"obj.hvoipStandMgcIp":$("input[@name='obj.hvoipStandMgcIp']").val(),
			"obj.hvoipStandMgcPort":$("input[@name='obj.hvoipStandMgcPort']").val(),
			"obj.hvoipPort":$("select[@name='obj.hvoipPort']").val(),
			"obj.hvoipVlanId":$("input[@name='obj.hvoipVlanId']").val(),
			"obj.hvoipWanType":$("select[@name='obj.hvoipWanType']").val(),
			"obj.hvoipIpaddress":$("input[@name='obj.hvoipIpaddress']").val(),
			"obj.hvoipIpmask":$("input[@name='obj.hvoipIpmask']").val(),
			"obj.hvoipGateway":$("input[@name='obj.hvoipGateway']").val(),
			"obj.hvoipIpdns":$("input[@name='obj.hvoipIpdns']").val(),
			
			"obj.sipServTypeId":sipServTypeId,
			"obj.sipOperateId":$("input[@name='obj.sipOperateId']").val(),
			"obj.sipVoipPhone":$("input[@name='obj.sipVoipPhone']").val(),
			"obj.sipVoipUsername":$("input[@name='obj.sipVoipUsername']").val(),
			"obj.sipVoipPwd":$("input[@name='obj.sipVoipPwd']").val(),
			"obj.sipProxServ":$("input[@name='obj.sipProxServ']").val(),
			"obj.sipProxPort":$("input[@name='obj.sipProxPort']").val(),
			"obj.sipStandProxServ":$("input[@name='obj.sipStandProxServ']").val(),
			"obj.sipStandProxPort":$("input[@name='obj.sipStandProxPort']").val(),
			//"obj.sipVoipPort":$("select[@name='obj.sipVoipPort']").val(),
			"obj.sipVoipPort":sipVoipPort,
			//�޸Ĵ�
			"obj.sipRegiServ":$("input[@name='obj.sipRegiServ']").val(),
			"obj.sipRegiPort":$("input[@name='obj.sipRegiPort']").val(),
			"obj.sipStandRegiServ":$("input[@name='obj.sipStandRegiServ']").val(),
			"obj.sipStandRegiPort":$("input[@name='obj.sipStandRegiPort']").val(),
			"obj.sipOutBoundProxy":$("input[@name='obj.sipOutBoundProxy']").val(),
			"obj.sipOutBoundPort":$("input[@name='obj.sipOutBoundPort']").val(),
			"obj.sipStandOutBoundProxy":$("input[@name='obj.sipStandOutBoundProxy']").val(),
			"obj.sipStandOutBoundPort":$("input[@name='obj.sipStandOutBoundPort']").val(),
			"obj.sipProtocol":$("select[@name='obj.sipProtocol']").val(),
			"obj.sipVlanId":$("input[@name='obj.sipVlanId']").val(),
			"obj.sipWanType":$("select[@name='obj.sipWanType']").val(),
			"obj.sipIpaddress":$("input[@name='obj.sipIpaddress']").val(),
			"obj.sipIpmask":$("input[@name='obj.sipIpmask']").val(),
			"obj.sipGateway":$("input[@name='obj.sipGateway']").val(),
			"obj.sipIpdns":$("input[@name='obj.sipIpdns']").val(),
			"obj.sipVoipUri":$("input[@name='obj.sipVoipUri']").val(),
			"obj.sipUserAgentDomain":$("input[@name='obj.sipUserAgentDomain']").val()
		},function(ajax){
			alert(ajax);
		});
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
}
//�˿ڱ������̬���´˶˿ڵ�ҵ����Ϣ�����������������
function changeVoipPort(voipPort,voipType)
{
	var url = "<s:url value='/itms/service/bssSheetByHand4AH!changeVoipPort.action'/>";
	var loid = $("input[@name='obj.loid']").val();
	var userType = $("select[@name='obj.userType']").val();
	//����������������첽���󣬻�ȡ����
	var sipProtocol = $("select[@name='obj.sipProtocol']").val();
	var sipSendPost = $("#isSipSendPost").val();
	var h248SendPost = $("#isH248SendPost").val();
	if("sip" == voipType && "2" == sipSendPost)
	{	
		$.post(url,{
			"obj.loid":loid,
			"obj.sipServTypeId":"14",
			"obj.sipVoipPort":voipPort,
			"obj.userType":userType,
			"obj.sipProtocol":sipProtocol
		},function(ajax){
			if("-1" == ajax)
			{
				//�������ͣ�1���������ֶ��ÿ���
				$("input[@name='obj.sipOperateId']").val("1");
				$("input[@name='obj.sipVoipPhone']").val("");
				$("input[@name='obj.sipVoipUsername']").val("");
				$("input[@name='obj.sipVoipPwd']").val("");
				$("input[@name='obj.sipProxServ']").val("");
				$("input[@name='obj.sipProxPort']").val("5060");
				$("input[@name='obj.sipStandProxServ']").val("");
				$("input[@name='obj.sipStandProxPort']").val("5060");
				$("input[@name='obj.sipRegiServ']").val("");
				$("input[@name='obj.sipRegiPort']").val("5060");
				$("input[@name='obj.sipStandRegiServ']").val("");
				$("input[@name='obj.sipStandRegiPort']").val("5060");
				$("input[@name='obj.sipOutBoundProxy']").val("");
				$("input[@name='obj.sipOutBoundPort']").val("5060");
				$("input[@name='obj.sipStandOutBoundProxy']").val("");
				$("input[@name='obj.sipStandOutBoundPort']").val("5060");
				$("select[@name='obj.sipProtocol']").val("-1");
				$("input[@name='obj.sipVlanId']").val("47");
				$("select[@name='obj.sipWanType']").val("4");
				$("input[@name='obj.sipIpaddress']").val("");
				$("input[@name='obj.sipIpmask']").val("");
				$("input[@name='obj.sipGateway']").val("");
				$("input[@name='obj.sipIpdns']").val("");
				$("input[@name='obj.sipVoipUri']").val("");
				$("input[@name='obj.sipUserAgentDomain']").val("ah.ctcims.cn");
				$("#delbutton").hide();
			}
			else
			{
				//��ʾԭʼ����
				var relt = ajax.split("|");
				changeIms(relt[24]);
				$("input[@name='obj.sipOperateId']").val("2");
				$("input[@name='obj.sipVoipPhone']").val(relt[1]);
				$("input[@name='obj.sipVoipUsername']").val(relt[2]);
				$("input[@name='obj.sipVoipPwd']").val(relt[3]);
				$("input[@name='obj.sipProxServ']").val(relt[4]);
				$("input[@name='obj.sipProxPort']").val(relt[5]);
				$("input[@name='obj.sipStandProxServ']").val(relt[6]);
				$("input[@name='obj.sipStandProxPort']").val(relt[7]);
				$("input[@name='obj.sipRegiServ']").val(relt[8]);
				$("input[@name='obj.sipRegiPort']").val(relt[9]);
				$("input[@name='obj.sipStandRegiServ']").val(relt[10]);
				$("input[@name='obj.sipStandRegiPort']").val(relt[11]);
				$("input[@name='obj.sipOutBoundProxy']").val(relt[12]);
				$("input[@name='obj.sipOutBoundPort']").val(relt[13]);
				$("input[@name='obj.sipStandOutBoundProxy']").val(relt[14]);
				$("input[@name='obj.sipStandOutBoundPort']").val(relt[15]);
				$("input[@name='obj.sipVlanId']").val(relt[16]);
				$("select[@name='obj.sipWanType']").val(relt[17]);
				$("input[@name='obj.sipIpaddress']").val(relt[18]);
				$("input[@name='obj.sipIpmask']").val(relt[19]);
				$("input[@name='obj.sipGateway']").val(relt[20]);
				$("input[@name='obj.sipIpdns']").val(relt[21]);
				$("input[@name='obj.sipVoipUri']").val(relt[22]);
				$("input[@name='obj.sipUserAgentDomain']").val(relt[23]);
				$("select[@name='obj.sipProtocol']").val(relt[24]);
				$("#delbutton").show();
			}
		});
	}
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
				$("input[@name='obj.hvoipRegIdType']").val("");
				$("input[@name='obj.hvoipMgcIp']").val("");
				$("input[@name='obj.hvoipMgcPort']").val("5060");
				$("input[@name='obj.hvoipStandMgcIp']").val("");
				$("input[@name='obj.hvoipStandMgcPort']").val("5060");
				$("input[@name='obj.hvoipVlanId']").val("");
				$("select[@name='obj.hvoipWanType']").val("-1");
				$("input[@name='obj.hvoipIpaddress']").val("");
				$("input[@name='obj.hvoipIpmask']").val("");
				$("input[@name='obj.hvoipGateway']").val("");
				$("input[@name='obj.hvoipIpdns']").val("");
			}
			else
			{
				//��ʾԭʼ����
				var relt = ajax.split("|");
				$("input[@name='obj.hvoipOperateId']").val(relt[0]);
				$("input[@name='obj.hvoipPhone']").val(relt[1]);
				$("input[@name='obj.hvoipRegId']").val(relt[2]);
				$("input[@name='obj.hvoipRegIdType']").val(relt[3]);
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
			}
		});
	}
}


function deleteBusiness()
{
	if(confirm("���Ҫɾ����������\n��������ɾ���Ĳ��ָܻ�������"))
	{
		var url = "<s:url value='/itms/service/bssSheetByHand4AH!deleteVoipPort.action'/>";
		var loid = $("input[@name='obj.loid']").val();
		var userType = $("select[@name='obj.userType']").val();
		var sipVoipUsername = $("input[@name='obj.sipVoipUsername']").val();
		var cityId = $("select[@name='obj.cityId']").val();
		var sipVoipPort = $("select[@name='obj.sipVoipPort']").val();
		//�޸Ĵ�
		//var lineId=$(("select[@name='lineList']")).val();
		$.post(url,{
			"obj.loid":loid,
			"obj.dealDate":$("input[@name='obj.dealDate']").val(),
			"obj.cmdId":$("input[@name='obj.cmdId']").val(),
			"obj.sipServTypeId":"14",
			"obj.sipVoipPort":sipVoipPort,
			"obj.userType":userType,
			"obj.sipVoipUsername":sipVoipUsername,
			"obj.cityId":cityId
		},function(ajax){
			alert(ajax);
			$("#delbutton").hide();
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
						<input type="hidden"  name = "obj.cmdId" value="FROMWEB-0000001">
						<input type="hidden"  name = "obj.authUser" value="itms">
						<input type="hidden"  name = "obj.authPwd" value="123">
						<td colspan="4" class="green_title_left">
						�û����Ϲ���
						</td>
					</tr>
					<tbody id="jirRuBssSheet" >
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">�û����ͣ�</TD>
							<TD width="35%" >
								<!--  -->
								<SELECT name="obj.userType" onChange="changeUserType(this.value)" class="bk" disabled>
								<OPTION value="-1">--��ѡ��--</OPTION>
								<OPTION value="1">��ͥ����</OPTION>
								<OPTION value="2">��ҵ����</OPTION>
								</SELECT>
								<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
							<TD width="35%" >
								<input type="text" name="obj.dealDate" class=bk value="">
							<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">LOID:</TD>
							<TD width="35%" >
								<input type="text" id="loid" name="obj.loid" class=bk value="">&nbsp;
								
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button type="button" name="subButton" onclick="checkLoid()">���LOID�Ƿ����</button>
								<!-- onblur="checkLoid()" -->
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
							<TD width="15%" class=column align="right">��ϵ��:</TD>
							<TD width="35%" >
								<input type="text" name="obj.linkman" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">��ϵ�绰:</TD>
							<TD width="35%" >
								<input type="text" name="obj.linkPhone" class=bk value="">
							</TD>
							<TD class=column align="right" nowrap width="15%">��������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.email" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">�ֻ�:</TD>
							<TD width="35%" >
								<input type="text" name="obj.mobile" class=bk value="" maxlength="15">
							</TD>
							<TD width="15%" class=column align="right">��ַ:</TD>
							<TD  width="35%" >
								<input type="text" name="obj.linkAddress" size="55" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">֤������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.linkmanCredno" class=bk value="">
							</TD>
							<TD width="15%" class=column align="right">�ն˹��:</TD>
							<TD width="35%" >
								<SELECT name="obj.specId"  class="bk">
								<OPTION value="-1">--��ѡ��--</OPTION>
								<OPTION value="e8cp42">e8cp42</OPTION>
								<OPTION value="e8cp21">e8cp21</OPTION>
								</SELECT>
							</TD>
						</TR>
						<TR id="egw_cust" bgcolor="#FFFFFF" >
							<TD id ="customerId" class=column align="right" nowrap width="15%">�ͻ�ID��</TD>
							<TD id ="customerIdValue" width="35%" >
								<input type="text" name="obj.customerId" class=bk value="">
							<font color="#FF0000">*</font>
							<button type="button" name="subButton" onclick="checkCustomerId()">���ͻ�ID�Ƿ����</button>
							</TD>
							<TD id ="customerId" class=column align="right" nowrap width="15%">�ͻ����ƣ�</TD>
							<TD id ="customerIdValue" width="35%" >
								<input type="text" name="obj.customerAccount" class=bk value="">
							</TD>
						</TR>
					</tbody>
					
					
					<tr align="left" style="display:none">
						<td colspan="4" class="green_title_left">
						<input type="hidden" id="netOperateId" name = "obj.netOperateId" value="1"/>
						<input type="checkbox" name="obj.netServTypeId" value="22" id="netServTypeId" onclick="showSheet('netServTypeId','internetBssSheet');"/>
						���ҵ�񹤵�
						</td>
					</tr>
					<tbody id="internetBssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.netUsername" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%">
								<input type="text" name="obj.netPasswd" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="obj.netVlanId" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">������ʽ��</TD>
							<TD width="35%" >
							<SELECT name="obj.netWanType" onChange="changeStatic(this.value,'netip','netDns');showStandDns(this.value,'standnetDns')" class="bk">
								<option value="-1">==��ѡ���������==</option>
								<option value="1">==�Ž�==</option>
								<option value="2">==·��==</option>
								<option value="3">==STATIC==</option>
								<option value="4">==DHCP==</option>
								</SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="netip" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
							<TD width="35%" >
								<input type="text" name="obj.netIpaddress" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.netIpmask" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="netDns" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.netGateway" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.netIpdns" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="standnetDns" bgcolor="#FFFFFF" style="display:none">
							<TD width="15%" class=column align="right">����DNS:</TD>
							<TD width="35%" colspan="3">
								<input type="text" name="obj.standNetIpdns" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>
					
					
					<!-- <tr align="left">
						<td colspan="4" class="green_title_left">
						<input type="checkbox" name="ipTVSheet" value="1" id="ipTVSheet" onclick="showSheet(this,'ipTVBssSheet');"/>
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
							<TD width="15%" class=column align="right"><br></TD>
							<TD width="35%" ><br></TD>
						</TR>
					</tbody>
					 -->
					
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
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipPhone" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�ն˱�ʶ����:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipRegId" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">�ն˱�ʶ������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipRegIdType" class=bk value="1">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">�ն������ʶ:</TD>
							<TD  width="35%" >
								<select name="obj.hvoipPort" class="bk" onChange="changeVoipPort(this.value,'h248')">
									<option value="-1">==��ѡ��==</option>
									<option value="A1">==A1==</option>
									<option value="A2">==A2==</option>
									<option value="A3">==A3==</option>
									<option value="A4">==A4==</option>
									<option value="A5">==A5==</option>
									<option value="A6">==A6==</option>
									<option value="A7">==A7==</option>
									<option value="A8">==A8==</option>
								</select>
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
								<input type="text" name="obj.hvoipMgcPort" class=bk value="">
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
								<input type="text" name="obj.hvoipStandMgcPort" class=bk value="">
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
						<TR id="h248IP" bgcolor="#FFFFFF" style="display:none">
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
						<TR id="h248Dns" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipGateway" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpdns" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>	
					
					<tr id="siptab" align="left" style="display:none">
						<td colspan="4" class="green_title_left">
						<input type="hidden" id="sipOperateId" name = "obj.sipOperateId" value="1"/>
						<input type="hidden" name = "isSipSendPost" id="isSipSendPost" value="0">
						<input type="checkbox" name="obj.sipServTypeId" value="14" id="sipServTypeId" onclick="showSheet('sipServTypeId','sipBssSheet');"/>
						SIP����ҵ�񹤵�
						</td>
					</tr>
					<tbody id="sipBssSheet" style="display:none">
						<tr bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipVoipPhone" class=bk value="">&nbsp;
								<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%" >�����˿�:</TD>
							<TD width="35%" id="speechgw1" >
								<select name="obj.sipVoipPort" id="VoipPort" class="bk" onChange="changeVoipPort(this.value,'sip')">
									<option value="-1">==��ѡ��==</option>
									<option value="V1">==V1==</option>
									<option value="V2">==V2==</option>
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD>  
							<TD width="35%" id="speechgw2" style="none">
								<select name="obj.sipVoipPort1" id="VoipPort" class="bk" onChange="changeVoipPort(this.value,'sip')">
									<option value="-1">==��ѡ��==</option>
									<option value="V1">==V1==</option>
									<option value="V2">==V2==</option>
									<option value="V3">==V3==</option>
									<option value="V4">==V4==</option>
									<option value="V5">==V5==</option>
									<option value="V6">==V6==</option>
									<option value="V7">==V7==</option>
									<option value="V8">==V8==</option>
									<option value="V9">==V9==</option>
									<option value="V10">==V10==</option>
									<option value="V11">==V11==</option>
									<option value="V12">==V12==</option>
									<option value="V13">==V13==</option>
									<option value="V14">==V14==</option>
									<option value="V15">==V15==</option>
									<option value="V16">==V16==</option>
									<option value="V17">==V17==</option>
									<option value="V18">==V18==</option>
									<option value="V19">==V19==</option>
									<option value="V20">==V20==</option>
									<option value="V21">==V21==</option>
									<option value="V22">==V22==</option>
									<option value="V23">==V23==</option>
									<option value="V24">==V24==</option>
									<option value="V25">==V25==</option>
									<option value="V26">==V26==</option>
									<option value="V27">==V27==</option>
									<option value="V28">==V28==</option>
									<option value="V29">==V29==</option>
									<option value="V30">==V30==</option>
									<option value="V31">==V31==</option>
									<option value="V32">==V32==</option>
								</select>
								&nbsp;<font color="#FF0000">*</font>
							</TD> 
						</tr>
						<TR bgcolor="#FFFFFF">
							<TD width="15%" class=column align="right">��֤�˺�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipVoipUsername" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��֤����</TD>
							<%String InstArea=LipossGlobals.getLipossProperty("InstArea.ShortName");
					if("ah_dx".equals(InstArea)){%>
							<TD width="35%"> 
								<input type="password" name="obj.sipVoipPwd" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
					<%}else{%> 
							<TD width="35%">
								<input type="text" name="obj.sipVoipPwd" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
					<%} %>
							
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">���ô��������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipProxServ" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">���ô���������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipProxPort" class=bk value="5060">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">���ô����������ַ:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipStandProxServ" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">���ô���������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipStandProxPort" class=bk value="5060">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����ע�������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipRegiServ" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע��������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipRegiPort" class=bk value="5060">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����ע���������ַ:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipStandRegiServ" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����ע��������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipStandRegiPort" class=bk value="5060">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">���ð󶨷�����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipOutBoundProxy" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">���ð󶨷������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipOutBoundPort" class=bk value="5060">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">���ð󶨷�������ַ:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipStandOutBoundProxy" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">���ð󶨷������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipStandOutBoundPort" class=bk value="5060">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>Э������</TD>
							<TD>
								<select name="obj.sipProtocol" class="bk" onChange="changeIms(this.value)">
								<option value="-1">==��ѡ��Э������==</option>
								<option value="0">==IMS SIP==</option>
								<option value="1">==���� SIP==</option>
								</select>
								&nbsp;&nbsp;<font color="#FF0000">* </font>
							</TD>
							<TD width="15%" class=column align="right">VLANID</TD>
							<TD width="35%">
								<input type="text" name="obj.sipVlanId" class=bk value="47" disabled>
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD colspan="3" width="35%" >
								<select name="obj.sipWanType" class="bk" onChange="changeStatic(this.value,'sipIP','sipDns')" disabled >
								<option value="-1">==��ѡ���������==</option>
								<option value="1">==�Ž�==</option>
								<option value="2">==·��==</option>
								<option value="3">==STATIC==</option>
								<option value="4" selected>==DHCP==</option>
							</select>
							&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="sipIP" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipIpaddress" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipIpmask" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="sipDns" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipGateway" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipIpdns" class=bk value="">
								&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="ims" style="display: none">
							<TD class=column align="right" nowrap>URI</TD>
							<TD><INPUT TYPE="text" NAME="obj.sipVoipUri" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>UserAgentDomain</TD>
							<TD><INPUT TYPE="text" NAME="obj.sipUserAgentDomain" maxlength=20 class=bk value="ah.ctcims.cn" disabled>
							</TD>
						</TR>
					</tbody>
					
					<TR align="left" id="doBiz" >
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" onclick="doReset()">��&nbsp;&nbsp;��</button>
							<button type="button" name="subBtn" id = "delbutton" onclick="deleteBusiness()">��&nbsp;&nbsp;��</button>
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