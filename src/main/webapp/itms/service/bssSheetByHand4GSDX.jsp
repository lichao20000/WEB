<%--
���������ֹ�����
Author: xiangzl
Version: 1.0.0
Date: 2012-09-14
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
	
	
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delSipSheet.action'/>";
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
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delIptvSheet.action'/>";
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
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delHvoipSheet.action'/>";
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
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delNetSheet.action'/>";
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
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delWifiSheet.action'/>";
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
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delUserSheet.action'/>";
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


//ɾ�����繤��
function delCloudNetSheet()
{
    var cloudNetAppType = "";
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

	if("" == $("input[@name='obj.cloudNetAccount']").val())
	{
		alert("����������˺Ų���Ϊ�ա�");
		$("input[@name='obj.cloudNetAccount']").focus();
		return false;
	}
	if("" == $("input[@name='obj.cloudNetPass']").val())
    {
        alert("������������벻��Ϊ�ա�");
        $("input[@name='obj.cloudNetPass']").focus();
        return false;
    }
    if("" == $("input[@name='obj.cloudNetUpBandwidth']").val())
    {
        alert("���п������Ϊ�ա�");
        $("input[@name='obj.cloudNetUpBandwidth']").focus();
        return false;
    }
    if("" == $("input[@name='obj.cloudNetDownBandwidth']").val())
    {
        alert("���п������Ϊ�ա�");
        $("input[@name='obj.cloudNetDownBandwidth']").focus();
        return false;
    }
    if("" == $("input[@name='obj.cloudNetVlanId']").val())
    {
        alert("vlanId����Ϊ�ա�");
        $("input[@name='obj.cloudNetVlanId']").focus();
        return false;
    }
    if("" == $("input[@name='obj.cloudNetDscp']").val())
    {
        alert("DSCP����Ϊ�ա�");
        $("input[@name='obj.cloudNetDscp']").focus();
        return false;
    }

    if("" == $("input[@name='obj.cloudNetAppType']").val())
    {
        alert("ҵ�����Ͳ���Ϊ�ա�");
        $("input[@name='obj.cloudNetAppType']").focus();
        return false;
    }
    cloudNetAppType =$("input[@name='obj.cloudNetAppType']").val();


	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!delCloudNetSheet.action'/>";
	$.post(url,{
		"obj.cmdId":$("input[@name='obj.cmdId']").val(),
		"obj.cloudNetServTypeId":"47",
		"obj.cloudNetOperateId" : "C",
		"obj.dealDate":$("input[@name='obj.dealDate']").val(),
		"obj.userType":$("select[@name='obj.userType']").val(),
		"obj.loid":loidvalue,
		"obj.cityId":$("select[@name='obj.cityId']").val(),
		"obj.cloudNetAccount":$("input[@name='obj.cloudNetAccount']").val(),
        "obj.cloudNetPass":$("input[@name='obj.cloudNetPass']").val(),
        "obj.cloudNetUpBandwidth":$("input[@name='obj.cloudNetUpBandwidth']").val(),
        "obj.cloudNetDownBandwidth":$("input[@name='obj.cloudNetDownBandwidth']").val(),
        "obj.cloudNetVlanId":$("input[@name='obj.cloudNetVlanId']").val(),
        "obj.cloudNetDscp":$("input[@name='obj.cloudNetDscp']").val(),
        "obj.cloudNetAppType":cloudNetAppType
	},function(ajax){
		if(ajax==("�ɹ�")){
			alert("���������ҵ�񹤵�ɾ���ɹ�! ");
			$("#cloudNetServTypeId").attr("checked",false);
		}else{
			alert("���������ҵ�񹤵�ɾ��ʧ��! " + ajax);
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
// ���ڶ�̬��ʾ����ҵ�񣨿��,IPTV,VPDN,H248,sip��(����-���������)
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
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!checkLoid.action'/>";
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
			$("#iptvServTypeId").attr("checked",false); 
			$("#hvoipServTypeId").attr("checked",false); 
			$("#sipServTypeId").attr("checked",false); 
			$("#wifiServTypeId").attr("checked",false); 
			
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
			
			if("22" == relt[21])
			{
				$("#netServTypeId").attr("checked",true); 
				$("input[@name='obj.netOperateId']").val(relt[22]);
				$("input[@name='obj.netUsername']").val(relt[23]);
				$("input[@name='obj.netPasswd']").val(relt[24]);
				$("input[@name='obj.netVlanId']").val(relt[25]);
				$("select[@name='obj.netWanType']").val(relt[26]);
				
				var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!queryNetParam.action'/>";
				$.post(url,{
					"obj.userType":$("select[@name='obj.userType']").val(),
					"obj.loid":$("input[@name='obj.loid']").val(),
					"obj.netUsername":relt[23],
					"obj.netServTypeId":"10"
				},function(ajax){
					var relt = ajax.split("|");
					$("input[@name='obj.netNum']").val(relt[0]);
				});
				
				showSheet('netServTypeId','internetBssSheet');
				if("3" == relt[26])
				{
					changeStatic(relt[26],'netip','netDns');
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
				showSheet('hvoipServTypeId','H248BssSheet');
				if("3" == relt[42])
				{
					changeStatic(relt[42],'h248IP','h248Dns');
				}
			}
			
			if("14" == relt[47])
			{
				$("#sipServTypeId").attr("checked",true); 
				changeIms(relt[65]);
				$("input[@name='obj.sipOperateId']").val(relt[48]);
				$("input[@name='obj.sipVoipPhone']").val(relt[49]);
				$("input[@name='obj.sipVoipUsername']").val(relt[50]);
				$("input[@name='obj.sipVoipPwd']").val('******');
				$("input[@name='obj.sipProxServ']").val(relt[52]);
				$("input[@name='obj.sipProxPort']").val(relt[53]);
				$("input[@name='obj.sipStandProxServ']").val(relt[54]);
				$("input[@name='obj.sipStandProxPort']").val(relt[55]);
				$("select[@name='obj.sipVoipPort']").val(relt[56]);
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
				var sipPortNum=relt[56].replace("V","");
				$("input[@name='obj.sipPortNum']").val(sipPortNum);
				showSheet('sipServTypeId','sipBssSheet');
			}
			
			if("21" == relt[75])
			{
				$("#iptvServTypeId").attr("checked",true); 
				$("input[@name='obj.iptvOperateId']").val('1');
				$("input[@name='obj.iptvUserName']").val(relt[77]);
				$("input[@name='obj.iptvPort']").val(relt[78]);
				$("input[@name='obj.iptvVlanId']").val(relt[79]);
				$("input[@name='obj.iptvNum']").val(relt[80]);
				showSheet('iptvServTypeId','iptvBssSheet');
			}
			
			if("23" == relt[106])
			{
				$("#wifiServTypeId").attr("checked",true); 
				$("input[@name='obj.wifiOperateId']").val('1');
				$("input[@name='obj.wifiUsername']").val(relt[108]);
				$("input[@name='obj.wifiPassword']").val('******');
				$("input[@name='obj.wifiVlanId']").val(relt[110]);
				$("select[@name='obj.wifiWanType']").val(relt[111]);
				var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!queryNetParam.action'/>";
				$.post(url,{
					"obj.userType":$("select[@name='obj.userType']").val(),
					"obj.loid":$("input[@name='obj.loid']").val(),
					"obj.netUsername":relt[108],
					"obj.netServTypeId":"20"
				},function(ajax){
					var relt = ajax.split("|");
					$("input[@name='obj.wifiNum']").val(relt[0]);
					$("input[@name='obj.ssidName']").val(relt[1]);
				});
				showSheet('wifiServTypeId','wifiBssSheet');
			}


			if("47" == relt[119])
            {
                $("#cloudNetServTypeId").attr("checked",true);
                $("input[@name='obj.cloudNetOperateId']").val('Z');
                $("input[@name='obj.cloudNetAccount']").val(relt[111]);
                $("input[@name='obj.cloudNetPass']").val(relt[112]);
                $("input[@name='obj.cloudNetUpBandwidth']").val(relt[113]);
                $("input[@name='obj.cloudNetDownBandwidth']").val(relt[114]);
                $("input[@name='obj.cloudNetVlanId']").val(relt[115]);
                $("input[@name='obj.cloudNetDscp']").val(relt[116]);
                $("input[@name='obj.cloudNetAppType']").val(relt[117]);
                showSheet('cloudNetServTypeId','cloudNetBssSheet');
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
	if("00"==cityid){
		cityid="1";
	}else if ("930"==cityid) {
		cityid="093001";
	}else if ("931"==cityid) {
		cityid="093101";
	}else if ("932"==cityid) {
		cityid="093201";
	}else if ("933"==cityid) {
		cityid="093301";
	}else if ("934"==cityid) {
		cityid="093401";
	}else if ("935"==cityid) {
		cityid="093501";
	}else if ("936"==cityid) {
		cityid="093601";
	}else if ("937"==cityid) {
		cityid="093701";
	}else if ("938"==cityid) {
		cityid="093801";
	}else if ("939"==cityid) {
		cityid="093901";
	}else if ("941"==cityid) {
		cityid="094101";
	}else if ("943"==cityid) {
		cityid="094301";
	}else if ("945"==cityid) {
		cityid="094501";
	}else if ("947"==cityid) {
		cityid="094701";
	}
	
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
	var cloudNetServTypeId = $("input[@name='obj.cloudNetServTypeId'][checked]").val();
	var cloudNetAppType='';
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
		
		if("" == $("input[@name='obj.netNum']").val())
		{
			alert("������������Ϊ�ա�");
			$("input[@name='obj.netNum']").focus();
			return false;
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

	if("47" != cloudNetServTypeId)
    {
        cloudNetServTypeId = "";
    }
    else
    {
        if("" == $("input[@name='obj.cloudNetAccount']").val())
        {
            alert("����������˺Ų���Ϊ�ա�");
            $("input[@name='obj.cloudNetAccount']").focus();
            return false;
        }
        if("" == $("input[@name='obj.cloudNetPass']").val())
        {
            alert("������������벻��Ϊ�ա�");
            $("input[@name='obj.cloudNetPass']").focus();
            return false;
        }
        if("" == $("input[@name='obj.cloudNetUpBandwidth']").val())
        {
            alert("���п������Ϊ�ա�");
            $("input[@name='obj.cloudNetUpBandwidth']").focus();
            return false;
        }
        if("" == $("input[@name='obj.cloudNetDownBandwidth']").val())
        {
            alert("���п������Ϊ�ա�");
            $("input[@name='obj.cloudNetDownBandwidth']").focus();
            return false;
        }
        if("" == $("input[@name='obj.cloudNetVlanId']").val())
        {
            alert("vlanId����Ϊ�ա�");
            $("input[@name='obj.cloudNetVlanId']").focus();
            return false;
        }
        if("" == $("input[@name='obj.cloudNetDscp']").val())
        {
            alert("DSCP����Ϊ�ա�");
            $("input[@name='obj.cloudNetDscp']").focus();
            return false;
        }

        if("" == $("input[@name='obj.cloudNetAppType']").val())
        {
            alert("ҵ�����Ͳ���Ϊ�ա�");
            $("input[@name='obj.cloudNetAppType']").focus();
            return false;
        }
        cloudNetAppType =$("input[@name='obj.cloudNetAppType']").val();
    }

	//ҵ�񴥷�
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!doBusiness.action'/>";
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
		"obj.netVlanId":$("input[@name='obj.netVlanId']").val(),
		"obj.netWanType":netWanType,
		"obj.netNum":$("input[@name='obj.netNum']").val(),
		
		"obj.hvoipServTypeId":hvoipServTypeId,
		"obj.hvoipOperateId":$("input[@name='obj.hvoipOperateId']").val(),
		"obj.hvoipPhone":$("input[@name='obj.hvoipPhone']").val(),
		"obj.hvoipRegId":$("input[@name='obj.hvoipRegId']").val(),
		"obj.hvoipVlanId":$("input[@name='obj.hvoipVlanId']").val(),
		"obj.hvoipWanType":$("select[@name='obj.hvoipWanType']").val(),
		
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
		"obj.wifiWanType":$("select[@name='obj.wifiWanType']").val(),
		
		"obj.cloudNetServTypeId":cloudNetServTypeId,
        "obj.cloudNetOperateId" : $("input[@name='obj.cloudNetOperateId']").val(),
        "obj.cloudNetAccount":$("input[@name='obj.cloudNetAccount']").val(),
        "obj.cloudNetPass":$("input[@name='obj.cloudNetPass']").val(),
        "obj.cloudNetUpBandwidth":$("input[@name='obj.cloudNetUpBandwidth']").val(),
        "obj.cloudNetDownBandwidth":$("input[@name='obj.cloudNetDownBandwidth']").val(),
        "obj.cloudNetVlanId":$("input[@name='obj.cloudNetVlanId']").val(),
        "obj.cloudNetDscp":$("input[@name='obj.cloudNetDscp']").val(),
        "obj.cloudNetAppType":cloudNetAppType

	},function(ajax){
		alert(ajax);
	});
	//�һ���ť
	$("button[@name='subBtn']").attr("disabled", true);
}
//�˿ڱ������̬���´˶˿ڵ�ҵ����Ϣ�����������������
function changeVoipPort(voipPort,voipType)
{
	var url = "<s:url value='/itms/service/bssSheetByHand4GSDX!changeVoipPort.action'/>";
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
							<TD width="15%" class=column align="right">LOID:</TD>
							<TD width="35%" >
								<input type="text" id="loid" name="obj.loid" class=bk value="">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button type="button" name="subButton" onclick="checkLoid()">���LOID�Ƿ����</button>
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
								<SELECT name="obj.accessStyle" class="bk"  disabled="disabled">
									<OPTION value="-1">--��ѡ��--</OPTION>
									<OPTION value="1">ADSL</OPTION>
									<OPTION value="2">LAN</OPTION>
									<OPTION value="3" selected="selected">EPON</OPTION>
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
							<TD class=column align="right" nowrap width="15%">����ʺŻ�ר�߽����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.netUsername" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��������:</TD>
							<TD width="35%">
								<input type="text" name="obj.netNum" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="obj.netVlanId" class=bk value="41" disabled="disabled">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">������ʽ��</TD>
							<TD width="35%" >
								<SELECT name="obj.netWanType" class="bk">
									<option value="-1">==��ѡ���������==</option>
	                                <ms:hasAuth authCode="ShowBridgeConn">
										<option value="1">==�Ž�==</option>
	                                </ms:hasAuth>
									<option value="2">==·��==</option>
									<option value="5">==�Ž�+·��==</option>
								</SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>
					
					
					<tr id="iptvtab" align="left" >
						<td colspan="4" class="green_title_left">
							<input type="hidden" id="iptvOperateId" name = "obj.iptvOperateId" value="1"/>
							<div style="float:left !important" >
								<input type="checkbox" name="obj.iptvServTypeId" value="21" id="iptvServTypeId" onclick="showSheet('iptvServTypeId','iptvBssSheet');"/>
								IPTVҵ�񹤵�
							</div>
							<div>
								<button style="float:right !important" onclick="delIptvSheet()">&nbsp;ɾ��&nbsp;</button>
							</div>
						</td>
					</tr>
					<tbody id="iptvBssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">IPTV��������ʺ�:</TD>
							<TD width="35%" >
								<input type="text" name="obj.iptvUserName" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="obj.iptvVlanId" class=bk value="43" disabled="disabled">&nbsp;<font color="#FF0000">*</font>
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
								<select name="obj.hvoipWanType" class="bk" disabled="disabled" onChange="changeStatic(this.value,'h248IP','h248Dns')">
									<option value="-1">==��ѡ���������==</option>
									<option value="1">==�Ž�==</option>
									<option value="2">==·��==</option>
									<option value="3" selected="selected">==STATIC==</option>
									<option value="4">==DHCP==</option>
								</select>&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">VLANID</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipVlanId" class=bk value="45" disabled="disabled">&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="h248IP" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipIpaddress" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpmask" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="h248Dns" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.hvoipGateway" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.hvoipIpdns" class=bk value="" disabled>&nbsp;<font color="#FF0000"></font>
							</TD>
						</TR>
					</tbody>	
					
					
					<tr id="siptab" align="left">
						<td colspan="4" class="green_title_left">
							<input type="hidden" id="sipOperateId" name = "obj.sipOperateId" value="1"/>
							<input type="hidden" name = "isSipSendPost" id="isSipSendPost" value="0">
							<div style="float:left !important" >
								<input type="checkbox" name="obj.sipServTypeId" value="14" id="sipServTypeId" onclick="showSheet('sipServTypeId','sipBssSheet');"/>
								SIP����ҵ�񹤵�
							</div>
							<div>
								<button style="float:right !important" onclick="delSipSheet()">&nbsp;ɾ��&nbsp;</button>
							</div>
						</td>
					</tr>
					<tbody id="sipBssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD width="15%" class=column align="right">��֤�˺�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipVoipUsername" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��֤����</TD>
							<TD width="35%">
								<input type="text" name="obj.sipVoipPwd" class=bk value="">&nbsp;<font color="#FF0000">*  (���벻��ʱ���뱣��******)</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">���ô��������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipProxServ" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">���ô���������˿�:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipProxPort" class=bk value="5060">&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
							<TD width="35%" >
								<select name="obj.sipWanType" class="bk" disabled="disabled" >
									<option value="-1">==��ѡ���������==</option>
									<option value="1">==�Ž�==</option>
									<option value="2">==·��==</option>
									<option value="3">==STATIC==</option>
									<option value="4" selected="selected">==DHCP==</option>
								</select>&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">VLANID</TD>
							<TD width="35%">
								<input type="text" name="obj.sipVlanId" class=bk value="45" disabled="disabled">&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>Э������</TD>
							<TD >
								<select name="obj.sipProtocol" class="bk" onChange="changeIms(this.value)">
									<option value="-1">==��ѡ��Э������==</option>
									<option value="0">==IMS SIP==</option>
								</select>&nbsp;&nbsp;<font color="#FF0000">* </font>
							</TD>
							<TD width="15%" class=column align="right">������·��</TD>
							<TD width="35%">
								<input type="text" name="obj.sipPortNum" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
							</TD>
							
						</TR>
						
						<TR id="sipIP" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">IP��ַ��</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipIpaddress" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">����:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipIpmask" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR id="sipDns" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" nowrap width="15%">����:</TD>
							<TD width="35%" >
								<input type="text" name="obj.sipGateway" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">DNS:</TD>
							<TD width="35%">
								<input type="text" name="obj.sipIpdns" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="ims" style="display: none">
							<TD class=column align="right" nowrap>URI(�����绰����)</TD>
							<TD colspan="3">
								<INPUT TYPE="text" NAME="obj.sipVoipUri" maxlength=20 class=bk value="">&nbsp; <font color="#FF0000">* </font>
							</TD>
						</TR>
					</tbody>
					
					<tr id="wifitab" align="left">
						<td colspan="4" class="green_title_left">
							<input type="hidden" id="wifiOperateId" name = "obj.wifiOperateId" value="1"/>
							<div style="float:left !important">
								<input type="checkbox" name="obj.wifiServTypeId" value="23" id="wifiServTypeId" onclick="showSheet('wifiServTypeId','wifiBssSheet');"/>
								����wifiҵ�񹤵�
							</div>
							<div>
								<button style="float:right !important" onclick="delwifiSheet()">&nbsp;ɾ��&nbsp;</button>
							</div>
						</td>
					</tr>
					<tbody id="wifiBssSheet" style="display:none">
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">��������:</TD>
							<TD width="35%" >
								<input type="text" name="obj.ssidName" class=bk value="">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">��������:</TD>
							<TD width="35%">
								<input type="text" name="obj.wifiNum" class=bk value="">
							</TD>
						</TR>
						
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">�����˺�:</TD>
							<TD width="35%" >
								<input type="text" name="obj.wifiUsername" class=bk value="">
							</TD>
							<TD width="15%" class=column align="right">��������:</TD>
							<TD width="35%">
								<input type="text" name="obj.wifiPassword" class=bk value="">&nbsp;<font color="#FF0000">*  (���벻��ʱ���뱣��******)</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap width="15%">VLANID:</TD>
							<TD width="35%" >
								<input type="text" name="obj.wifiVlanId" class=bk value="42" disabled="disabled">&nbsp;<font color="#FF0000">*</font>
							</TD>
							<TD width="15%" class=column align="right">������ʽ��</TD>
							<TD width="35%" >
								<SELECT name="obj.wifiWanType" class="bk" disabled="disabled">
									<option value="-1">==��ѡ���������==</option>
	                                <ms:hasAuth authCode="ShowBridgeConn">
										<option value="1">==�Ž�==</option>
	                                </ms:hasAuth>
									<option value="2">==·��==</option>
									<option value="5" selected="selected">==�Ž�+·��==</option>
								</SELECT>&nbsp;&nbsp;&nbsp;<font color="#FF0000">*</font>
							</TD>
						</TR>
					</tbody>

					<tr id="cloudNettab" align="left">
                        <td colspan="4" class="green_title_left">
                            <input type="hidden" id="cloudNetOperateId" name = "obj.cloudNetOperateId" value="Z"/>
                            <div style="float:left !important">
                                <input type="checkbox" name="obj.cloudNetServTypeId" value="47" id="cloudNetServTypeId" onclick="showSheet('cloudNetServTypeId','cloudNetBssSheet');"/>
                                ���������ҵ�񹤵�
                            </div>
                            <div>
                                <button style="float:right !important" onclick="delCloudNetSheet()">&nbsp;ɾ��&nbsp;</button>
                            </div>
                        </td>
                    </tr>
                    <tbody id="cloudNetBssSheet" style="display:none">
                        <TR bgcolor="#FFFFFF">
                            <TD class=column align="right" nowrap width="15%">����������˺�:</TD>
                            <TD width="35%" >
                                <input type="text" name="obj.cloudNetAccount" class=bk value="">&nbsp;<font color="#FF0000">*</font>
                            </TD>
                            <TD width="15%" class=column align="right">�������������:</TD>
                            <TD width="35%">
                                <input type="text" name="obj.cloudNetPass" class=bk value="">&nbsp;<font color="#FF0000">*</font>
                            </TD>
                        </TR>

                        <TR bgcolor="#FFFFFF">
                            <TD class=column align="right" nowrap width="15%">���п��:</TD>
                            <TD width="35%" >
                                <input type="text" name="obj.cloudNetUpBandwidth" class=bk value="">&nbsp;<font color="#FF0000">*</font>
                            </TD>
                            <TD width="15%" class=column align="right">���п��:</TD>
                            <TD width="35%">
                                <input type="text" name="obj.cloudNetDownBandwidth" class=bk value="">&nbsp;<font color="#FF0000">*</font>
                            </TD>
                        </TR>
                        <TR bgcolor="#FFFFFF">
                            <TD class=column align="right" nowrap width="15%">VLANID:</TD>
                            <TD width="35%" >
                                <input type="text" name="obj.cloudNetVlanId" class=bk value="" >&nbsp;<font color="#FF0000">*</font>
                            </TD>
                            <TD width="15%" class=column align="right">DSCP��</TD>
                            <TD width="35%" >
                                <input type="text" name="obj.cloudNetDscp" class=bk value="">&nbsp;<font color="#FF0000">*</font>
                            </TD>
                        </TR>
                        <TR bgcolor="#FFFFFF">
                            <TD class=column align="right" nowrap width="15%">ҵ������:</TD>
                            <TD width="35%" >
                                <input type="text" name="obj.cloudNetAppType" class=bk value="">&nbsp;<font color="#FF0000">*</font>
                            </TD>
                            <TD width="15%" class=column align="right"></TD>
                            <TD width="35%" >
                            </TD>
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
<%@ include file="../../../foot.jsp"%>