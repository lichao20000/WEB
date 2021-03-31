<%--
������ͨ�ֹ�����
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
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
var inArea='<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>';

String.prototype.replaceAll = function(oldStr,newStr) { 
    return this.replace(new RegExp(oldStr,"gm"),newStr); 
}

$(function() 
	{
		var gw_type = '<s:property value="gw_type" />';
		//��ͥ����
		if(gw_type == '1')
		{
			$("select[@name='obj.userType']").val('1');
			$("input[@name='obj.cmdId']").val('FROMWEB-0000002');
		}
		
		var dstr = "";
		var d = new Date();
		
		dstr += d.getFullYear();
		if(d.getMonth()+1<10){
			dstr += '0';
			dstr += d.getMonth()+1;
		}else{
			dstr += d.getMonth()+1;
		}
		
		if(d.getDate()<10){
			dstr += '0';
			dstr += d.getDate();
		}else{
			dstr += d.getDate();
		}
		
		if(d.getHours()<10){
			dstr += '0';
			dstr += d.getHours();
		}else{
			dstr += d.getHours();
		}
		
		if(d.getMinutes()<10){
			dstr += '0';
			dstr += d.getMinutes();
		}else{
			dstr += d.getMinutes();
		}
		
		dstr += "00";
		$("input[@name='obj.dealDate']").val(dstr);
	}
);

//ɾ��SIP����ҵ�񹤵�
function delSipSheet()
{
	if(!window.confirm("ȷ��Ҫɾ��������")){
		return false;
	}
	
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val()){
		alert("��ѡ���û����͡�");
		return false;
	}
	
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
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
	
	if("" == $("input[@name='obj.sipVoipUsername']").val())
	{
		alert("��֤�˺Ų���Ϊ�ա�");
		$("input[@name='obj.sipVoipUsername']").focus();
		return false;
	}
	
	
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!delSipSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.sipServTypeId":"14",
		"obj.sipOperateId":"3",
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.sipVoipUsername":$("input[@name='obj.sipVoipUsername']").val(),
		"obj.sipVoipPwd":$("input[@name='obj.sipVoipPwd']").val(),
		"obj.sipProxServ":$("input[@name='obj.sipProxServ']").val(),
		"obj.sipProxPort":$("input[@name='obj.sipProxPort']").val(),
		"obj.sipWanType":$("select[@name='obj.sipWanType']").val(),
		"obj.sipVlanId":$("input[@name='obj.sipVlanId']").val(),
		"obj.sipProtocol":$("select[@name='obj.sipProtocol']").val(),
		"obj.sipPortNum":$("input[@name='obj.sipPortNum']").val(),
		"obj.sipVoipUri":$("input[@name='obj.sipVoipUri']").val()
	},function(ajax){
		if(ajax==("1")){
			alert("SIP����ҵ�񹤵�ɾ���ɹ�! ");
			$("#sipServTypeId").attr("checked",false); 
		}else{
			alert("SIP����ҵ�񹤵�ɾ��ʧ��! " + ajax);	
		}
	});	
}

//ɾ��Iptvҵ�񹤵�
function delIptvSheet()
{
	if(!window.confirm("ȷ��Ҫɾ��������")){
		return false;
	}
	
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
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
	
	if("" == $("input[@name='obj.iptvUserName']").val())
	{
		alert("IPTV�����ʺŲ���Ϊ�ա�");
		$("input[@name='obj.iptvUserName']").focus();
		return false;
	}
	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!delIptvSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.iptvServTypeId": "21",
		"obj.iptvOperateId" : "3",
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.iptvUserName"  : $("input[@name='obj.iptvUserName']").val()
	},function(ajax){
		if(ajax==("1")){
			alert("Iptvҵ�񹤵�ɾ���ɹ�! ");
			$("#iptvServTypeId").attr("checked",false); 
		}else{
			alert("Iptvҵ�񹤵�ɾ��ʧ��! " + ajax);	
		}
	});
}


//ɾ��H248��������
function delHvoipSheet()
{
	if(!window.confirm("ȷ��Ҫɾ��������")){
		return false;
	}
	
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
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
	if("" == $("input[@name='obj.hvoipPhone']").val())
	{
		alert("ҵ��绰���벻��Ϊ�ա�");
		$("input[@name='obj.hvoipPhone']").focus();
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!delHvoipSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.hvoipServTypeId": "15",
		"obj.hvoipOperateId" : "3",
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.hvoipPhone":$("input[@name='obj.hvoipPhone']").val()
	},function(ajax){
		if(ajax==("1")){
			alert("H248����ҵ�񹤵�ɾ���ɹ�! ");
			$("#hvoipServTypeId").attr("checked",false); 
		}else{
			alert("H248����ҵ�񹤵�ɾ��ʧ��! " + ajax);	
		}
	});
}
//ɾ�����繤��
function delNetSheet()
{
	if(!window.confirm("ȷ��Ҫɾ��������")){
		return false;
	}
	
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
	if("-1" == $("select[@name='obj.cityId']").val())
	{
		alert("���ز���Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	if("" == $("input[@name='obj.netUsername']").val())
	{
		alert("����˺Ų���Ϊ�ա�");
		$("input[@name='obj.netUsername']").focus();
		return false;
	}
	if("-1" == $("select[@name='obj.accessStyle']").val())
	{
		alert("�ն˽������Ͳ���Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!delNetSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.netServTypeId": "22",
		"obj.netOperateId" : "3",
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.netUsername":$("input[@name='obj.netUsername']").val()
	},function(ajax){
		if(ajax==("1")){
			alert("���ҵ�񹤵�ɾ���ɹ�! ");
			$("#netServTypeId").attr("checked",false); 
		}else{
			alert("���ҵ�񹤵�ɾ��ʧ��! " + ajax);	
		}
	});	
}

//ɾ�����繤��
function delwifiSheet()
{
	if(!window.confirm("ȷ��Ҫɾ��������")){
		return false;
	}
	
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
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
	
	if("" == $("input[@name='obj.wifiUsername']").val())
	{
		alert("�����˺Ų���Ϊ�ա�");
		$("input[@name='obj.wifiUsername']").focus();
		return false;
	}
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!delWifiSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.wifiServTypeId":"23",
		"obj.wifiOperateId" : "3",
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.ssidName":$("input[@name='obj.ssidName']").val(),
		"obj.wifiNum":$("input[@name='obj.wifiNum']").val(),
		"obj.wifiUsername":$("input[@name='obj.wifiUsername']").val(),
		"obj.wifiPassword":$("input[@name='obj.wifiPassword']").val(),
		"obj.wifiVlanId":$("input[@name='obj.wifiVlanId']").val(),
		"obj.wifiWanType":$("select[@name='obj.wifiWanType']").val()
	},function(ajax){
		if(ajax==("1")){
			alert("����wifiҵ�񹤵�ɾ���ɹ�! ");	
			$("#wifiServTypeId").attr("checked",false);
		}else{
			alert("����wifiҵ�񹤵�ɾ��ʧ��! " + ajax);	
		}
	});	
}

function delUserSheet()
{
	if(!window.confirm("ȷ��Ҫɾ��������"))
	{
		return false;
	}
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
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
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!delUserSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.userServTypeId":"20",
		"obj.userOperateId":"3",
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val()
	},function(ajax){
		if(ajax==("1")){
			alert("�û����Ϲ���ɾ���ɹ�! ");	
		}else{
			alert("�û����Ϲ���ɾ��ʧ��! " + ajax);	
		}
	});		
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
	if("3"==wanType){
		$("tr[@id='"+strdns+"']").css("display","block");
	}else{
		$("tr[@id='"+strdns+"']").css("display","none");
	}
}
// ���ڶ�̬��ʾ����ҵ�񣨿��,IPTV,VPDN,H248,sip��
function showSheet(objId,name){
    if($("#"+objId).attr("checked")) {
       document.getElementById(name).style.display="";
    } else {
        document.getElementById(name).style.display="none";
    }
}
// reg_verify - ��ȫ��������ʽ���ж�һ���ַ����Ƿ��ǺϷ���IP��ַ��
function reg_verify(addr){
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;
    if(addr.match(reg)) {
        return true;
    } else {
         return false;
    }
}
//�����ims sip ��ʾuri ��domain
function changeIms(protocol)
{
	if("0"==protocol)
	{
		$("tr[@id='ims']").css("display","block");
		$("input[@name='obj.sipUserAgentDomain']").val("");
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
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!checkLoid.action'/>";
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
			$("#netServTypeId").attr("checked",false); 
			$("#hvoipServTypeId").attr("checked",false); 
			
			var relt = ajax.split("|");
			$("input[@name='obj.userOperateId']").val(relt[4]);
			$("select[@name='obj.cityId']").val(relt[8]);
			$("select[@name='obj.accessStyle']").val(relt[11]);
			$("input[@name='obj.linkman']").val(relt[12]);
			$("input[@name='obj.linkPhone']").val(relt[13]);
			$("input[@name='obj.email']").val(relt[14]);
			$("input[@name='obj.mobile']").val(relt[15]);
			$("input[@name='obj.linkAddress']").val(relt[16]);
			$("input[@name='obj.linkmanCredno']").val(relt[17]);
			
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
			showSheet('hvoipServTypeId','H248BssSheet');
				if("3" == relt[42])
				{
					changeStatic(relt[42],'h248IP','h248Dns');
				}
			}
			
			if("22" == relt[21])
			{
				$("#netServTypeId").attr("checked",true); 
				$("input[@name='obj.netOperateId']").val(relt[22]);
				$("input[@name='obj.netUsername']").val(relt[23]);
				$("input[@name='obj.netPasswd']").val(relt[24]);
				$("input[@name='obj.netVlanId']").val(relt[25]);
				$("select[@name='obj.netWanType']").val(relt[26]);
				showSheet('netServTypeId','internetBssSheet');
			}
			
		}
	});
}
//�ύҵ��
function doBusiness()
{
	var loidvalue = "";
	if("-1" == $("select[@name='obj.userType']").val())
	{
		alert("��ѡ���û����͡�");
		return false;
	}
	
	
	if("" == $("input[@name='obj.loid']").val())
	{
		alert("LOID����Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	loidvalue = $("input[@name='obj.loid']").val();	
	
	if("-1" == $("select[@name='obj.cityId']").val())
	{
		alert("���ز���Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	var cityid=$("select[@name='obj.cityId']").val()
	
	if("-1" == $("select[@name='obj.accessStyle']").val())
	{
		alert("�ն˽������Ͳ���Ϊ�ա�");
		$("input[@name='obj.loid']").focus();
		return false;
	}
	var netServTypeId = $("input[@name='obj.netServTypeId'][checked]").val();
	var iptvServTypeId = $("input[@name='obj.iptvServTypeId'][checked]").val();
	var hvoipServTypeId = $("input[@name='obj.hvoipServTypeId'][checked]").val();
	var sipServTypeId = $("input[@name='obj.sipServTypeId'][checked]").val();
	var wifiServTypeId = $("input[@name='obj.wifiServTypeId'][checked]").val();
	var netWanType="";
	if("21" != iptvServTypeId)
	{
		iptvServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.iptvUserName']").val())
		{
			alert("IPTV�����ʺŲ���Ϊ�ա�");
			$("input[@name='obj.iptvUserName']").focus();
			return false;
		}
		if("" == $("input[@name='obj.iptvVlanId']").val())
		{
			alert("VlanId����Ϊ�ա�");
			$("input[@name='obj.iptvVlanId']").focus();
			return false;
		}
	}
	
	
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
		if("1" == $("select[@name='obj.netWanType']").val()){
			netWanType="0";//�Ž�
		}else if ("2" == $("select[@name='obj.netWanType']").val()) {
			netWanType="1";//·��
		}else if ("5" == $("select[@name='obj.netWanType']").val()) {
			netWanType="2";//�Ž�+·��
		}
		
	}
	
	if("23" != wifiServTypeId)
	{
		wifiServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.ssidName']").val())
		{
			alert("�������Ʋ���Ϊ�ա�");
			$("input[@name='obj.ssidName']").focus();
			return false;
		}
		if("" == $("input[@name='obj.wifiNum']").val())
		{
			alert("����������������Ϊ�ա�");
			$("input[@name='obj.netNum']").focus();
			return false;
		}
		
		if("" == $("input[@name='obj.wifiUsername']").val())
		{
			alert("�����˺Ų���Ϊ�ա�");
			$("input[@name='obj.wifiUsername']").focus();
			return false;
		}
		if("" == $("input[@name='obj.wifiPassword']").val())
		{
			alert("�������벻��Ϊ�ա�");
			$("input[@name='obj.wifiPassword']").focus();
			return false;
		}
		
		
		if("" == $("input[@name='obj.wifiVlanId']").val())
		{
			alert("VLANDID����Ϊ�ա�");
			$("input[@name='obj.wifiVlanId']").focus();
			return false;
		}
		if("-1" == $("select[@name='obj.wifiWanType']").val())
		{
			alert("������ʽ����Ϊ�ա�");
			$("select[@name='obj.netWanType']").focus();
			return false;
		}
		if("1" == $("select[@name='obj.netWanType']").val()){
			netWanType="0";//�Ž�
		}else if ("2" == $("select[@name='obj.netWanType']").val()) {
			netWanType="1";//·��
		}else if ("5" == $("select[@name='obj.netWanType']").val()) {
			netWanType="2";//�Ž�+·��
		}
		
	}
	
	if("15" != hvoipServTypeId)
	{
		hvoipServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.hvoipVlanId']").val())
		{
			alert("VLANDID����Ϊ��");
			$("input[@name='obj.hvoipVlanId']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipIpaddress']").val())
		{
			alert("IP��ַ����Ϊ��");
			$("input[@name='obj.hvoipIpaddress']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipIpmask']").val())
		{
			alert("���벻��Ϊ��");
			$("input[@name='obj.hvoipIpmask']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipGateway']").val())
		{
			alert("���ز���Ϊ��");
			$("input[@name='obj.hvoipGateway']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipIpdns']").val())
		{
			alert("DNS����Ϊ��");
			$("input[@name='obj.hvoipIpdns']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipStandMgcIp']").val())
		{
			alert("����IP����Ϊ��");
			$("input[@name='obj.hvoipStandMgcIp']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipUserAgentDomain']").val())
		{
			alert("��������Ϊ��");
			$("input[@name='obj.sipUserAgentDomain']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipPort']").val())
		{
			alert("���ض˿ڲ���Ϊ��");
			$("input[@name='obj.hvoipPort']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipMgcPort']").val())
		{
			alert("����MGC�Ķ˿ڲ���Ϊ��");
			$("input[@name='obj.hvoipMgcPort']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipStandMgcPort']").val())
		{
			alert("����MGC�˿ڲ���Ϊ��");
			$("input[@name='obj.hvoipStandMgcPort']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipEid']").val())
		{
			alert("TID����Ϊ��");
			$("input[@name='obj.hvoipEid']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipRtpPrefix']").val())
		{
			alert("�ն�RTP�ս���ʶǰ׺����Ϊ��");
			$("input[@name='obj.hvoipRtpPrefix']").focus();
			return false;
		}
		if("" == $("input[@name='obj.hvoipMgcIp']").val())
		{
			alert("����MGCIP����Ϊ��");
			$("input[@name='obj.hvoipMgcIp']").focus();
			return false;
		}

	}
	if("14" != sipServTypeId)
	{
		sipServTypeId = "";
	}
	else
	{
		if("" == $("input[@name='obj.sipVoipUsername']").val())
		{
			alert("��֤�˺Ų���Ϊ�ա�");
			$("input[@name='obj.sipVoipUsername']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipVoipPwd']").val())
		{
			alert("��֤���벻��Ϊ�ա�");
			$("input[@name='obj.sipVoipPwd']").focus();
			return false;
		}
		if("" == $("input[@name='obj.sipProxServ']").val())
		{
			alert("���ô������������Ϊ�ա�");
			$("input[@name='obj.sipProxServ']").focus();
			return false;
		}
		if("-1" == $("select[@name='obj.sipProtocol']").val())
		{
			alert("Э�����ͱ���ѡ��");
			$("select[@name='obj.sipProtocol']").focus();
			return false;
		}
		if("-1" == $("select[@name='obj.sipPortNum']").val())
		{
			alert("������·������Ϊ�ա�");
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
	
	
	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!doBusiness.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.userServTypeId":$("input[@name='obj.userServTypeId']").val(),
		"obj.userOperateId":$("input[@name='obj.userOperateId']").val(),
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":cityid,
		"obj.accessStyle":$("select[@name='obj.accessStyle']").val(),
		"obj.linkman":$("input[@name='obj.linkman']").val(),
		"obj.email":$("input[@name='obj.email']").val(),
		"obj.linkPhone":$("input[@name='obj.linkPhone']").val(),
		"obj.linkAddress":$("input[@name='obj.linkAddress']").val(),
		"obj.linkmanCredno":$("input[@name='obj.linkmanCredno']").val(),
		
		"obj.netServTypeId":netServTypeId,
		"obj.netOperateId":$("input[@name='obj.netOperateId']").val(),
		"obj.netUsername":$("input[@name='obj.netUsername']").val(),
		"obj.netPasswd":$("input[@name='obj.netPasswd']").val(),
		"obj.netVlanId":$("input[@name='obj.netVlanId']").val(),
		"obj.netWanType":netWanType,
		"obj.netNum":$("input[@name='obj.netNum']").val(),
		
		"obj.hvoipServTypeId":hvoipServTypeId,
		"obj.hvoipOperateId":$("input[@name='obj.hvoipOperateId']").val(),
		"obj.hvoipPhone":$("input[@name='obj.hvoipPhone']").val(),
		"obj.hvoipRegId":$("input[@name='obj.hvoipRegId']").val(),
		"obj.hvoipVlanId":$("input[@name='obj.hvoipVlanId']").val(),
		"obj.hvoipIpaddress":$("input[@name='obj.hvoipIpaddress']").val(),
		"obj.hvoipIpmask":$("input[@name='obj.hvoipIpmask']").val(),
		"obj.hvoipGateway":$("input[@name='obj.hvoipGateway']").val(),
		"obj.hvoipIpdns":$("input[@name='obj.hvoipIpdns']").val(),
		"obj.hvoipStandMgcIp":$("input[@name='obj.hvoipStandMgcIp']").val(),
		"obj.sipUserAgentDomain":$("input[@name='obj.sipUserAgentDomain']").val(),
		"obj.hvoipPort":$("input[@name='obj.hvoipPort']").val(),
		"obj.hvoipMgcPort":$("input[@name='obj.hvoipMgcPort']").val(),
		"obj.hvoipStandMgcPort":$("input[@name='obj.hvoipStandMgcPort']").val(),
		"obj.hvoipEid":$("input[@name='obj.hvoipEid']").val(),
		"obj.hvoipRtpPrefix":$("input[@name='obj.hvoipRtpPrefix']").val(),
		"obj.hvoipMgcIp":$("input[@name='obj.hvoipMgcIp']").val(),

		"obj.sipServTypeId":sipServTypeId,
		"obj.sipOperateId":$("input[@name='obj.sipOperateId']").val(),
		"obj.sipVoipUsername":$("input[@name='obj.sipVoipUsername']").val(),
		"obj.sipVoipPwd":$("input[@name='obj.sipVoipPwd']").val(),
		"obj.sipProxServ":$("input[@name='obj.sipProxServ']").val(),
		"obj.sipProxPort":$("input[@name='obj.sipProxPort']").val(),
		"obj.sipWanType":$("select[@name='obj.sipWanType']").val(),
		"obj.sipVlanId":$("input[@name='obj.sipVlanId']").val(),
		"obj.sipProtocol":$("select[@name='obj.sipProtocol']").val(),
		"obj.sipPortNum":$("input[@name='obj.sipPortNum']").val(),
		"obj.sipVoipUri":$("input[@name='obj.sipVoipUri']").val(),

		
		"obj.iptvServTypeId": iptvServTypeId,
		"obj.iptvOperateId" : $("input[@name='obj.iptvOperateId']").val(),
		"obj.iptvUserName"  : $("input[@name='obj.iptvUserName']").val(),
		"obj.iptvVlanId"  : $("input[@name='obj.iptvVlanId']").val(),
		
		"obj.wifiServTypeId":wifiServTypeId,
		"obj.wifiOperateId" : $("input[@name='obj.wifiOperateId']").val(),
		"obj.ssidName":$("input[@name='obj.ssidName']").val(),
		"obj.wifiNum":$("input[@name='obj.wifiNum']").val(),
		"obj.wifiUsername":$("input[@name='obj.wifiUsername']").val(),
		"obj.wifiPassword":$("input[@name='obj.wifiPassword']").val(),
		"obj.wifiVlanId":$("input[@name='obj.wifiVlanId']").val(),
		"obj.wifiWanType":$("select[@name='obj.wifiWanType']").val()
		
		
	},function(ajax){
		alert(ajax);
	});
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
}
//�˿ڱ������̬���´˶˿ڵ�ҵ����Ϣ�����������������
function changeVoipPort(voipPort,voipType)
{
	var url = "<s:url value='/itms/service/bssSheetByHand4JXLT!changeVoipPort.action'/>";
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
							<td>
								<img src="../../images/attention_2.gif" width="15" height="12">
							</td>
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
							<div style="float:left">�û����Ϲ���</div>
							<div style="float:right !important">
								<button onclick="delUserSheet()">&nbsp;ɾ��&nbsp;</button>
							</div>
						</td>
					</tr>
					<tbody id="jirRuBssSheet" >
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">�û����ͣ�</TD>
							<TD width="35%" >
								<SELECT name="obj.userType"  class="bk">
									<OPTION value="1">��ͥ����</OPTION>
								</SELECT><font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">����ʱ�䣺</TD>
							<TD width="35%" >
								<input type="text" name="obj.dealDate" class=bk value=""><font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">�ն�Ψһ��ʶ:</TD>
							<TD width="35%" >
								<input type="text" id="loid" name="obj.loid" class=bk value="">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button type="button" name="subButton" onclick="checkLoid()">���Ψһ��ʶ�Ƿ����</button>
							</TD>
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<s:select list="cityList" name="obj.cityId"
									headerKey="-1" headerValue="��ѡ������" listKey="city_id"
									listValue="city_name" value="cityId" cssClass="bk">
								</s:select><font color="#FF0000">*</font>
							</TD>
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
									<OPTION value="5">VDSL</OPTION>
								</SELECT>&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��ϵ��:</TD>
							<TD width="35%" >
								<input type="text" name="obj.linkman" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">��ϵ�绰:</TD>
							<TD width="35%" >
								<input type="text" name="obj.linkPhone" class=bk value="" maxlength="15">
							</TD>
							<TD width="15%" class=column align="right">��ַ:</TD>
							<TD  width="35%" >
								<input type="text" name="obj.linkAddress" size="55" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">��������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.email" class=bk value="">
							</TD>
							<TD class=column align="right" nowrap width="15%">֤������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.linkmanCredno" class=bk value="">
								<input type="text" name="obj.linkmanCredno" class=bk value="">
							</TD>
							
						</TR>
					</tbody>
					
					
					<tr id="nettab" align="left">
						<td colspan="4" class="green_title_left">
							<input type="hidden" id="netOperateId" name = "obj.netOperateId" value="1"/>
							<div style="float:left !important">
								<input type="checkbox" name="obj.netServTypeId" value="22" id="netServTypeId" onclick="showSheet('netServTypeId','internetBssSheet');"/>
								���ҵ�񹤵�
							</div>
							<div>
								<button style="float:right !important" onclick="delNetSheet()">&nbsp;ɾ��&nbsp;</button>
							</div>
						</td>
					</tr>
					<tbody id="internetBssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����ʺ�:</TD>
							<TD width="35%" >
								<input type="text" name="obj.netUsername" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%">
								<input type="text" name="obj.netPasswd" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD width="15%" class=column align="right">������ʽ��</TD>
							<TD width="35%" >
								<SELECT name="obj.netWanType" class="bk">
									<option value="2">==·��==</option>
								</SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%"></TD>
							<TD width="35%" >
							</TD>
						</TR>
					</tbody>
					
					<tr id="h248tab" align="left">
						<td colspan="4" class="green_title_left">
							<input type="hidden" id="hvoipOperateId" name = "obj.hvoipOperateId" value="1"/>
							<input type="hidden" name = "isH248SendPost" id="isH248SendPost" value="0">
							<div style="float:left !important" >
								<input type="checkbox" name="obj.hvoipServTypeId" value="15" id="hvoipServTypeId" onclick="showSheet('hvoipServTypeId','H248BssSheet');"/>
								H248����ҵ�񹤵�</div>
							<div>
								<button style="float:right !important" onclick="delHvoipSheet()">&nbsp;ɾ��&nbsp;</button>
							</div>
						</td>
					</tr>
					<tbody id="H248BssSheet" style="display:none">
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">ҵ��绰����:</TD>
						<TD width="35%" >
							<input type="text" name="obj.hvoipPhone" class=bk value="">&nbsp;<font color="#FF0000">*</font>
						</TD>
						<TD width="15%" class=column align="right">�ն˱�ʶ����:</TD>
						<TD width="35%">
							<input type="text" name="obj.hvoipRegId" class=bk value="">&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
						<TR bgcolor="#FFFFFF">	
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<select name="obj.hvoipWanType" class="bk"  onChange="changeStatic(this.value,'h248IP','h248Dns')">
									<option value="-1">==��ѡ���������==</option>
									<option value="1">==�Ž�==</option>
									<option value="2">==·��==</option>
									<option value="3">==STATIC==</option>
									<option value="4">==DHCP==</option>
								</select>&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">VLANID</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipVlanId" class=bk>&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipIpaddress" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">���ض˿�:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipPort" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>

						</TR>
						<TR  bgcolor="#FFFFFF">
							<TD width="15%" class=column align="right">����MGCIP:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipMgcIp" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����MGC�Ķ˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipMgcPort" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
							</TD>

						</TR>
						<TR  bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����MGCIP:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipStandMgcIp" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">����MGC�˿�:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipStandMgcPort" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>

						</TR>

						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipGateway" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpdns" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpmask" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipUserAgentDomain" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>

						<TR  bgcolor="#FFFFFF">

							<TD width="15%" class=column align="right">TID:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipEid" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">�ն�RTP�ս���ʶǰ׺:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipRtpPrefix" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>

						</TR>

					</tbody>	
					
					<TR align="left" id="doBiz" >
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doBusiness()">��&nbsp;&nbsp;��</button>
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