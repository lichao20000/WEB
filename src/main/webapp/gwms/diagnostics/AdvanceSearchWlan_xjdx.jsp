<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<HEAD>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = '<s:property value="gw_type"/>';
$(function(){
	init();
});

//get data
function init()
{
	parent.unblock();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("���ݲɼ�:");
	$("div[@id='operResult']").append("<s:property value='result'/>");

	$("select[@name='apEnable'] option").each(function(){
		if($(this).val() == "<s:property value='apEnable'/>"){
			$(this).attr("selected",true);
			return;
		}
	});

	$("select[@name='hide'] option").each(function(){
		if($(this).val() == "<s:property value='hide'/>"){
			$(this).attr("selected",true);
			return;
		}
	});

	setTimeout("clearResult()", 5000);
}

//config.
function config(lanId, lanWlanId)
{
	var deviceId = $("input[@name='deviceId']").val();
	var apEnable = $("select[@name='apEnable']").val();
	var hide = $("select[@name='hide']").val();
	var powerlevel = $("input[@name='powerlevel']").val().trim();
	var wpsKeyWord = $("input[@name='wpsKeyWord']").val().trim();

	if (IsNumber2(powerlevel,"ģ�鹦�� ") == false) {
		powerlevel.focus();
		return;
	}

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("���ڱ༭WLAN...");

	var url = "<s:url value='/gwms/diagnostics/wlanACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId,
		lanId:lanId,
		lanWlanId:lanWlanId,
		apEnable:apEnable,
		hide:hide,
		powerlevel:powerlevel,
		wpsKeyWord:wpsKeyWord,
		gw_type:gw_type
	},function(ajax){
		if("false"==ajax.split("|")[0]){
			alert("����ɹ�!");
			var url="<s:url value='/gwms/diagnostics/wlanACT.action'/>"
					+"?deviceId="+deviceId
					+"&queryFrom=db"
					+"&gw_type="+gw_type;
			location.replace(url);
		}else{
			alert("����ʧ��!");
		}
		$("div[@id='operResult']").html(ajax.split("|")[1]);
		setTimeout("clearResult()", 5000);
	});
}

function chg_encr(enc_value)
{
	var trs = document.getElementById("tb2").getElementsByTagName("tr");
	for (var i=0; i<trs.length; i++)
	{
		if("tr_encr"==trs[i].isShow)
		{
			if ("WEP"==enc_value)
			{
				var basicAuthMode=document.all("basicAuthMode").value;
				var opt="";
				if('Both'==basicAuthMode){
					opt="<OPTION value='Both' slected>OPEN+SHARE</OPTION>"
						+ "<OPTION value='OpenSystem'>OPEN</OPTION>"
						+ "<OPTION value='SharedKey'>SHARE</OPTION>";
				}else if('SharedKey'==basicAuthMode){
					opt="<OPTION value='SharedKey' slected>SHARE</OPTION>"
						+ "<OPTION value='OpenSystem'>OPEN</OPTION>"
						+ "<OPTION value='Both'>OPEN+SHARE</OPTION>";
				}else{
					opt="<OPTION value='OpenSystem' slected>OPEN</OPTION>"
						+ "<OPTION value='SharedKey'>SHARE</OPTION>"
						+ "<OPTION value='Both'>OPEN+SHARE</OPTION>";
				}

				$("select[@name='encr_mode']").html("");
				$("select[@name='encr_mode']").append(opt);

				trs[i].style.display = "";
				tr_encr.style.display = "";
			}
			else if ("WPA-PSK"==enc_value || "WPA2-PSK"==enc_value || "WPA-PSK/WPA2-PSK"==enc_value)
			{
				var wpaEncrMode=document.all("wpaEncrMode").value;
				var opt="";
				if('AESEncryption'==wpaEncrMode){
					opt="<OPTION value='AESEncryption' selected>AES</OPTION>"
						+ "<OPTION value='TKIPEncryption'>TKIP</OPTION>"
						+ "<OPTION value='TKIPandAESEncryption'>TKIP+AES</OPTION>";
				}else if('TKIPEncryption'==wpaEncrMode){
					opt="<OPTION value='TKIPEncryption' selected>TKIP</OPTION>"
						+"<OPTION value='AESEncryption'>AES</OPTION>"
						+ "<OPTION value='TKIPandAESEncryption'>TKIP+AES</OPTION>";
				}else{
					opt="<OPTION value='TKIPandAESEncryption' selected>TKIP+AES</OPTION>"
						+ "<OPTION value='AESEncryption'>AES</OPTION>"
						+ "<OPTION value='TKIPEncryption'>TKIP</OPTION>";
				}

				$("select[@name='encr_mode']").html("");
				$("select[@name='encr_mode']").append(opt);

				trs[i].style.display = "";
				tr_encr.style.display = "";
			}else{
				trs[i].style.display = "none";
				tr_encr.style.display = "none";
			}
		}

		if("WEP"==enc_value){
			if ("wep_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else if("WPA-PSK"==enc_value || "WPA2-PSK"==enc_value || "WPA-PSK/WPA2-PSK"==enc_value){
			if ("wep_key"==trs[i].isShow) {
				trs[i].style.display = "none";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "";
			}
		}else{
			if ("wep_key"==trs[i].isShow || "wpa_key"==trs[i].isShow) {
				trs[i].style.display = "none";
			}
		}
	}

	if("WEP"==enc_value){
		chg_auth("OpenSystem");
	}else if("None"!=enc_value){
		chg_auth("AESEncryption");
	}
}

// chg
function chg_auth(enc_value)
{
	var trs = document.getElementById("tb2").getElementsByTagName("tr");
	for (var i=0; i<trs.length; i++) {
		if ( "OpenSystem"==enc_value ||"SharedKey"==enc_value ||"Both"==enc_value) {
			if ( "wep_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else{
			if ( "wep_key"==trs[i].isShow) {
				trs[i].style.display = "none";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "";
			}
		}
	}
	parent.dyniframesize();
}

//del.
function del(lanId, lanWlanId)
{
	if(!confirm("��ȷʵҪɾ����")){
		return false;
	}
	undis();

	var deviceId = $("input[@name='deviceId']").val();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("����ɾ��SSID...");

	var url = "<s:url value='/gwms/diagnostics/wlanACT!del.action'/>";
	$.post(url,{
		deviceId:deviceId,
		lanId:lanId,
		lanWlanId:lanWlanId
	},function(ajax){
		if("false"==ajax.split("|")[0]){
			alert("ɾ���ɹ�!");
			var url="<s:url value='/gwms/diagnostics/wlanACT.action'/>"
					+"?deviceId="+deviceId
					+"&queryFrom=db"
					+"&gw_type="+gw_type;
			location.replace(url);
		}else{
			alert("ɾ��ʧ��!");
		}
		$("div[@id='operResult']").html(ajax.split("|")[1]);
		setTimeout("clearResult()", 5000);
	});
}

//add.
function add()
{
	var deviceId = $("input[@name='deviceId']").val();
	var lanId = $("input[@name='lanId']").val();
	var encr_mode=$("select[@name='encr_mode']").val();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("��������SSID...");


	var ssid = $("input[@name='ssid']").val().trim();
	ssid = ssid.replace(/(^\s*)|(\s*$)/g, "");
	if(""==ssid||null==ssid||"null"==ssid){
		alert("����дSSID��");
		return false;
	}

	var beacontype = $("select[@name='beacontype']").val();
	var key="";
	if("WEP"==beacontype)
	{
		var wep_key = $("input[@name='wep_key']").val().trim();
		var wep_key_sec = $("input[@name='wep_key_sec']").val().trim();

		if(""==wep_key || null==wep_key){
			alert("����дWEPģʽ��Կ��");
			return false;
		}

		if(wep_key!=wep_key_sec){
			alert("�����������Կ��һ����");
			return false;
		}

		var wepEncrLevel=document.all("wepEncrLevel").value;
		if('40-bit'==wepEncrLevel){
			if(wep_key.length!=5){
				alert("������5λ�����ַ���");
				return false;
			}
		}else{
			if(wep_key.length!=13){
				alert("������13λ�����ַ���");
				return false;
			}
		}

		key=wep_key;
	}
	else if("WPA-PSK"==beacontype || "WPA2-PSK"==beacontype || "WPA-PSK/WPA2-PSK"==beacontype)
	{
		var wpa_key = $("input[@name='wpa_key']").val().trim();
		var wpa_key_sec = $("input[@name='wpa_key_sec']").val().trim();

		if(""==wpa_key || null==wpa_key){
			alert("����дWPAģʽ��Կ��");
			return false;
		}

		if(wpa_key!=wpa_key_sec){
			alert("�����������Կ��һ����");
			return false;
		}

		if(wpa_key.length<8 || wpa_key.length>63){
			alert("������8-63λ�����ַ���");
			return false;
		}

		key=wpa_key;
	}

	var url = "<s:url value='/gwms/diagnostics/wlanACT!add.action'/>";
	$.post(url,{
		deviceId:deviceId,
		lanId:lanId,
		ssid:ssid,
		beacontype:beacontype,
		encr_mode:encr_mode,
		key:key,
		gw_type:gw_type
	},function(ajax){
		if("false"==ajax.split("|")[0]){
			alert("�����ɹ�!");
			undis();
			var url="<s:url value='/gwms/diagnostics/wlanACT.action'/>"
					+"?deviceId="+deviceId
					+"&queryFrom=db"
					+"&gw_type="+gw_type;
			location.replace(url);
		}else{
			alert("����ʧ��!");
		}
		$("div[@id='operResult']").html(ajax.split("|")[1]);
		setTimeout("clearResult()", 5000);
	});
}

//edit.
function edit()
{
	var deviceId = $("input[@name='deviceId']").val();
	var lanId = $("input[@name='lanId']").val();
	var lanWlanId = $("input[@name='lanWlanId']").val();
	var ssid = $("input[@name='ssid']").val().trim();
	var encr_mode=$("select[@name='encr_mode']").val();
	var beacontype = $("select[@name='beacontype']").val();
	var wpa_key = $("input[@name='wpa_key']").val().trim();
	var wpa_key_sec = $("input[@name='wpa_key_sec']").val().trim();
	var wep_key = $("input[@name='wep_key']").val().trim();
	var wep_key_sec = $("input[@name='wep_key_sec']").val().trim();

	ssid = ssid.replace(/(^\s*)|(\s*$)/g, "");
	if(""==ssid || null==ssid || "null"==ssid){
		alert("����дSSID��");
		return false;
	}

	var key="";
	if("WEP"==beacontype)
	{
		if(""==wep_key || null==wep_key){
			alert("����дWEPģʽ��Կ��");
			return false;
		}

		if(wep_key!=wep_key_sec){
			alert("�����������Կ��һ����");
			return false;
		}

		var wepEncrLevel=document.all("wepEncrLevel").value;
		if('40-bit'==wepEncrLevel){
			if(wep_key.length!=5){
				alert("������5λ�����ַ���");
				return false;
			}
		}else{
			if(wep_key.length!=13){
				alert("������13λ�����ַ���");
				return false;
			}
		}

		key=wep_key;
	}
	else if("WPA-PSK"==beacontype || "WPA2-PSK"==beacontype || "WPA-PSK/WPA2-PSK"==beacontype)
	{
		if(""==wpa_key || null==wpa_key){
			alert("����дWPAģʽ��Կ��");
			return false;
		}

		if(wpa_key!=wpa_key_sec){
			alert("�����������Կ��һ����");
			return false;
		}

		if(wpa_key.length<8 || wpa_key.length>63){
			alert("������8-63λ�����ַ���");
			return false;
		}

		key=wpa_key;
	}

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("���ڱ༭SSID...");

	var url = "<s:url value='/gwms/diagnostics/wlanACT!edit.action'/>";
	$.post(url,{
		deviceId:deviceId,
		lanId:lanId,
		lanWlanId:lanWlanId,
		ssid:ssid,
		beacontype:beacontype,
		encr_mode:encr_mode,
		key:key,
		gw_type:gw_type
	},function(ajax){
		if("false"==ajax.split("|")[0]){
			alert("�༭�ɹ�!");
			undis();
			var url="<s:url value='/gwms/diagnostics/wlanACT.action'/>"
					+"?deviceId="+deviceId
					+"&queryFrom=db"
					+"&gw_type="+gw_type;
			location.replace(url);
		}else{
			alert("�༭ʧ��!");
		}
		$("div[@id='operResult']").html(ajax.split("|")[1]);
		setTimeout("clearResult()", 5000);
	});
}

//type 1:add; 2:edit.
function chgForm (type, lanId, lanWlanId, ssid, beacontype, wep_key, wpa_key,
		channel,wpaEncrMode,basicAuthMode) {
	var subTabForm = document.getElementById("subTabForm");
	subTabForm.style.display = "block";
	$("input[@name='type']").val(type);
	$("input[@name='ssid']").val(ssid);
	$("input[@name='lanId']").val(lanId);
	$("input[@name='lanWlanId']").val(lanWlanId);
	$("select[@name='beacontype']").val(beacontype);
	$("input[@name='channel']").val(channel);
	$("input[@name='wpaEncrMode']").val(wpaEncrMode);
	$("input[@name='basicAuthMode']").val(basicAuthMode);

	$("input[@name='wpa_key']").val(wpa_key);
	$("input[@name='wpa_key_sec']").val(wpa_key);
	$("input[@name='wep_key']").val(wep_key);
	$("input[@name='wep_key_sec']").val(wep_key);

	if (type == 1)
	{
		var tb2 = document.getElementById("tb2");
		var trs = tb2.getElementsByTagName("tr");
		tb2.style.display = "";

		for (var i=0; i<trs.length; i++) {
			if ( "all"==trs[i].isShow || "add"==trs[i].isShow) {
				trs[i].style.display = "";
			}else{
				trs[i].style.display = "none";
			}
		}

		var checkradios = document.all("hide");
		for(var i=0;i<checkradios.length;i++)
	    {
	      if("0"==checkradios[i].value)
		  {
		    checkradios[i].checked=true;
		    break;
		  }
	    }

		var enintext="<OPTION value='None' selected>None</OPTION>"
					+"<OPTION value='WEP'>WEP</OPTION>"
					+"<OPTION value='WPA-PSK'>WPA-PSK</OPTION>"
					+"<OPTION value='WPA2-PSK'>WPA2-PSK</OPTION>"
					+"<OPTION value='WPA-PSK/WPA2-PSK'>WPA-PSK/WPA2-PSK</OPTION>";
		$("select[@name='beacontype']").html("");
		$("select[@name='beacontype']").append(enintext);

		var intext="<input type='hidden' name='wepEncrLevel' value='"+wepEncrLevel+"'>"
		var insectext='';
		if('40-bit'==wepEncrLevel){
			intext=intext+"<input type='text' name='wep_key' value='' class='bk' maxlength=5 size=15><font color='red'>*������5λ�ַ���</font>";
			insectext="<input type='text' name='wep_key_sec' value='' class='bk' maxlength=5 size=15><font color='red'>*</font>"
		}else{
			intext=intext+"<input type='text' name='wep_key' value='' class='bk' maxlength=13 size=15><font color='red'>*������13λ�ַ���</font>";
			insectext="<input type='text' name='wep_key_sec' value='' class='bk' maxlength=13 size=15><font color='red'>*</font>"
		}
		$("td[@name='wep_key_text']").html("");
		$("td[@name='wep_key_text']").append(intext);
		$("td[@name='wep_key_text_next']").html("");
		$("td[@name='wep_key_text_next']").append(insectext);
	}
	else
	{
		var tb2 = document.getElementById("tb2");
		var trs = tb2.getElementsByTagName("tr");

		tb2.style.display = "";
		for (var i=0; i<trs.length; i++) {
			if ( "all"==trs[i].isShow || "edit"==trs[i].isShow) {
				trs[i].style.display = "";
			}else{
				trs[i].style.display = "none";
			}

			if ( "Basic"==beacontype ) {
				if ( "wep_key"==trs[i].isShow) {
					trs[i].style.display = "";
				}else if("wpa_key"==trs[i].isShow){
					trs[i].style.display = "none";
				}
			}else if ( "WPA"==beacontype ) {
				if ( "wpa_key"==trs[i].isShow) {
					trs[i].style.display = "";
				}else if("wep_key"==trs[i].isShow){
					trs[i].style.display = "none";
				}
			}else{
				if ( "wpa_key"==trs[i].isShow ||"wep_key"==trs[i].isShow) {
					trs[i].style.display = "none";
				}
			}
		}

		var checkradios = document.all("hide");
		for(var i=0;i<checkradios.length;i++)
	    {
	      if(hide==checkradios[i].value)
		  {
		    checkradios[i].checked=true;
		    break;
		  }
	    }

		var enintext="";
		if("WEP"==beacontype){
			enintext="<OPTION value='WEP' selected>WEP</OPTION>"
					+"<OPTION value='None'>None</OPTION>"
					+"<OPTION value='WPA-PSK'>WPA-PSK</OPTION>"
					+"<OPTION value='WPA2-PSK'>WPA2-PSK</OPTION>"
					+"<OPTION value='WPA-PSK/WPA2-PSK'>WPA-PSK/WPA2-PSK</OPTION>";
		}else if("WPA-PSK"==beacontype){
			enintext="<OPTION value='WPA-PSK' selected>WPA-PSK</OPTION>"
				+"<OPTION value='None'>None</OPTION>"
				+"<OPTION value='WEP'>WEP</OPTION>"
				+"<OPTION value='WPA2-PSK'>WPA2-PSK</OPTION>"
				+"<OPTION value='WPA-PSK/WPA2-PSK'>WPA-PSK/WPA2-PSK</OPTION>";
		}else if("WPA2-PSK"==beacontype){
			enintext="<OPTION value='WPA2-PSK' selected>WPA2-PSK</OPTION>"
				+"<OPTION value='None'>None</OPTION>"
				+"<OPTION value='WEP'>WEP</OPTION>"
				+"<OPTION value='WPA-PSK'>WPA-PSK</OPTION>"
				+"<OPTION value='WPA-PSK/WPA2-PSK'>WPA-PSK/WPA2-PSK</OPTION>";
		}else if("WPA-PSK/WPA2-PSK"==beacontype){
			enintext="<OPTION value='WPA-PSK/WPA2-PSK' selected>WPA-PSK/WPA2-PSK</OPTION>"
				+ "<OPTION value='None'>None</OPTION>"
				+ "<OPTION value='WEP'>WEP</OPTION>"
				+ "<OPTION value='WPA-PSK'>WPA-PSK</OPTION>"
				+ "<OPTION value='WPA2-PSK'>WPA2-PSK</OPTION>";
		}else{
			enintext="<OPTION value='None' selected>None</OPTION>"
				+"<OPTION value='WEP'>WEP</OPTION>"
				+"<OPTION value='WPA-PSK'>WPA-PSK</OPTION>"
				+"<OPTION value='WPA2-PSK'>WPA2-PSK</OPTION>"
				+"<OPTION value='WPA-PSK/WPA2-PSK'>WPA-PSK/WPA2-PSK</OPTION>";
		}
		$("select[@name='beacontype']").html("");
		$("select[@name='beacontype']").append(enintext);

		var intext="<input type='hidden' name='wepEncrLevel' value='"+wepEncrLevel+"'>"
		var insectext='';
		if('40-bit'==wepEncrLevel){
			intext=intext
						+"<input type='text' name='wep_key' value='"+wep_key
						+"' class='bk' maxlength=5 size=15><font color='red'>*������5λ�ַ���</font>";

			insectext="<input type='text' name='wep_key_sec' value='"+wep_key
						+"' class='bk' maxlength=5 size=15><font color='red'>*</font>"
		}else{
			intext=intext
					+"<input type='text' name='wep_key' value='"+wep_key
					+"' class='bk' maxlength=13 size=15><font color='red'>*������13λ�ַ���</font>";
			insectext="<input type='text' name='wep_key_sec' value='"+wep_key
						+"' class='bk' maxlength=13 size=15><font color='red'>*</font>"
		}
		$("td[@name='wep_key_text']").html("");
		$("td[@name='wep_key_text']").append(intext);
		$("td[@name='wep_key_text_next']").html("");
		$("td[@name='wep_key_text_next']").append(insectext);

		chg_encr(beacontype);
	}
	tr_beacontype.style.display = "";
	parent.dyniframesize();
}


function editWlanConn()
{
	var type = $("input[@name='type']").val();

	if (type == 1){
		add();
	}else {
		edit();
	}
}


function undis()
{
	var subTabForm = document.getElementById("subTabForm");
	subTabForm.style.display = "none";
	parent.dyniframesize();
}

function clearResult()
{
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}
</SCRIPT>
</HEAD>

<input type="hidden" name="wpaEncrMode" />
<input type="hidden" name="basicAuthMode" />
<input type="hidden" name="wepEncrLevel" value="<s:property value='wepEncrLevel'/>">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>

		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<TR>
					<TH colspan="9">WLAN����״̬�༭</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="1" align="right">WLAN����</TD>
					<TD colspan="1">
						<SELECT NAME="apEnable">
							<OPTION VALUE="1" SELECTED>����
							<OPTION VALUE="0">�ر�
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD colspan="2" align="right">SSID�Ƿ�����</TD>
					<TD colspan="2">
						<SELECT NAME="hide">
							<OPTION VALUE="1" SELECTED>��
							<OPTION VALUE="0">��
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD colspan="1" align="right">WPS����ؼ���</TD>
					<TD colspan="2">
						<INPUT TYPE="text" NAME="wpsKeyWord" size="8" maxlength="8" value="<s:property value='wpsKeyWord' default=''/>">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="1" align="right">����ģ�鹦�ʼ���</TD>
					<TD colspan="1">
						<INPUT TYPE="text" NAME="powerlevel"  size="8" maxlength="8" value="<s:property value='powerlevel'/>">
						&nbsp;<font color="red">*</font>
					</TD>
					<TD colspan="2" align="right">��ǰ����</TD>
					<TD colspan="2"><s:property value='powervalue'/></TD>
					<TD colspan="3"></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="9" class="green_foot" align=right>
						<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
						<input type="button" value=" �� �� " class="jianbian" onclick="javascript:config(<s:property value='lanId' default='1'/>,<s:property value='lanWlanId' default='1'/>);">&nbsp;&nbsp;
					</TD>
				</TR>
				<TR>
					<TH colspan="1" width="10%">ID</TH>
					<TH colspan="1" width="20%">SSID</TH>
					<TH colspan="1" width="10%">SSID����</TH>
					<TH colspan="1" width="5%">�㲥</TH>
					<TH colspan="1" width="10%">����ģʽ</TH>
					<TH colspan="1" width="10%">״̬</TH>
					<TH colspan="1" width="10%">�����ŵ�</TH>
					<TH colspan="1" width="10%">��ǰ�ŵ�</TH>
					<TH colspan="1" width="15%">����</TH>
				</TR>
				<s:if test="list.size == 0">
					<TR bgcolor="#FFFFFF">
						<TD colspan="9" class="green_foot" align=left>��������</TD>
					</TR>
				</s:if>
				<s:else>
					<s:iterator var="list" value="list" status="row">
						<TR bgcolor="#FFFFFF">
							<TD align="center">
								<s:property value="#list.lanId"/>-<s:property value="#list.lanWlanId"/>
							</TD>
							<TD>
								<s:property value="#list.ssid" default=""/>
							</TD>
							<TD align="center">
								<s:if test="#list.enable == 1">��</s:if>
								<s:else>��</s:else>
							</TD>
							<TD align="center">
								<s:if test="#list.radioEnable == 1">��</s:if>
								<s:else>��</s:else>
							</TD>
							<TD align="center">
								<s:property value="#list.beacontype"/>
							</TD>
							<TD align="center"><s:property value="#list.status"/></TD>
							<TD align="center"><s:property value="#list.channel"/>channel</TD>
							<TD align="center">
								<s:if test="#list.channelInUse == '' || #list.channelInUse == 'null'">
									<s:property value="#list.channel"/>
								</s:if>
								<s:else>
									<s:property value="#list.channelInUse"/>
								</s:else>
							</TD>
							<TD align="center">
								<IMG SRC="../../images/edit.gif" WIDTH="14" HEIGHT="12" BORDER="0" ALT="�༭"
									onclick="chgForm(2,<s:property value='#list.lanId' default=''/>,
													   <s:property value='#list.lanWlanId' default='1'/>,
													   '<s:property value='#list.ssid' default='1'/>',
													   '<s:property value='#list.beacontype' default='None'/>',
													   '<s:property value='#list.wepKey' default=''/>',
													   '<s:property value='#list.wpaKey' default=''/>',
													   '<s:property value='#list.channel' default=''/>',
													   '<s:property value='#list.wpaEncrMode' default=''/>',
													   '<s:property value='#list.basicAuthMode' default=''/>')"
									onMouseOver="this.style.cursor='hand';">
								&nbsp;&nbsp;
								<IMG SRC="../../images/del.gif" WIDTH="14" HEIGHT="12" BORDER="0" ALT="ɾ��"
									onclick="del(<s:property value='#list.lanId' default='1'/>,
												 <s:property value='#list.lanWlanId' default='1'/>)"
									onMouseOver="this.style.cursor='hand';">
								&nbsp;&nbsp;
								<IMG SRC="../../images/add.gif" WIDTH="14" HEIGHT="12" BORDER="0" ALT="���"
									onclick="chgForm(1,<s:property value='#list.lanId' default='1'/>,'','','','','','','','')"
									onMouseOver="this.style.cursor='hand';">
							</TD>
						</TR>
					</s:iterator>
				</s:else>
			</TABLE>
		</TD>
	</TR>
	<TR id="subTabForm" style="display:none">
		<TD bgcolor=#999999 colspan="9">
			<TABLE id="tb2" border=0 cellspacing=1 cellpadding=1 width="100%">
				<TR isShow="edit" STYLE="display: none">
					<TH align="center" colspan=4 valign="center">
						<B><span id="actLabel">�޸�SSID</span></B>
					</TH>
				</TR>
				<TR isShow="add" STYLE="display: none">
					<TH align="center" colspan=4 valign="center">
						<B><span id="actLabel">����SSID</span></B>
					</TH>
				</TR>
				<TR bgcolor="#FFFFFF" isShow="all">
					<TD width="40%" class=column align="right">SSID</TD>
					<TD width="60%" colspan=3>
						<INPUT TYPE="text" NAME="ssid" maxlength=25 class=bk size=25>
						<font color="red">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr_beacontype" isShow="all" STYLE="display: none">
					<TD class=column align="right">��֤ģʽ</TD>
					<TD colspan=3>
						<SELECT name="beacontype" class="bk" onchange="chg_encr(this.value)">
						</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr_encr" name="tr_encr" isShow="tr_encr" STYLE="display: none">
					<TD class=column align="right">����ģʽ</TD>
					<TD colspan=3>
						<SELECT name="encr_mode" class="bk" onchange="chg_auth(this.value)">
						</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" isShow="wep_key" style="display: none">
					<TD align="right">������Կ:</TD>
					<TD name="wep_key_text"></TD>
				</TR>
				<TR bgcolor="#FFFFFF" isShow="wep_key" style="display: none">
					<TD align="right">��һ�����������Կ:</TD>
					<TD name="wep_key_text_next"></TD>
				</TR>

				<TR bgcolor="#FFFFFF" isShow="wpa_key" style="display: none">
					<TD align="right">������Կ:</TD>
					<TD>
						<input type="text" name="wpa_key" class="bk" maxlength=63 size=15>
						<font color="red">*������8-63λ�ַ���</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" isShow="wpa_key" style="display: none">
					<TD align="right">��һ�����������Կ:</TD>
					<TD>
						<input type="text" name="wpa_key_sec" class="bk" maxlength=63 size=15>
						<font color="red">*</font>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF" isShow="all">
					<TD colspan="4" align="right" class=green_foot>
						<input type="hidden" name="type">
						<input type="hidden" name="lanId">
						<input type="hidden" name="lanWlanId">
						<INPUT TYPE="button" value=" �� �� " onclick="editWlanConn()" class=jianbian>
					</TD>
				</TR>
			</TABLE>
		</td>
	</tr>
</TABLE>
