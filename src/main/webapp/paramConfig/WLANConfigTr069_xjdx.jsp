<%@ page import="com.linkage.litms.common.database.*,java.util.*,com.linkage.litms.common.util.*"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	request.setCharacterEncoding("gbk");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function deviceResult(returnVal)
{
	$("tr[@id='trDeviceResult']").css("display","");
	$("td[@id='tdDeviceSn']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='device_id_old_']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
	}
}
	
function query2db()
{
	var device_id=document.all("device_id_old_").value;
	if(device_id == null || ""==device_id){
		alert("请选择设备");
		return false;
	}

	var page="showWLANParam_xjdx.jsp?device_id="+device_id+ "&flag_boolean=true&refresh="+Math.random();
	document.all("childFrm1").src = page;
}

function query2device()
{
	var device_id=document.all("device_id_old_").value;
	if(device_id == null || ""==device_id){
		alert("请选择设备");
		return false;
	}

	var page="showWLANParam_xjdx.jsp?device_id="+device_id+ "&flag_boolean=false&refresh="+Math.random();
	document.all("childFrm1").src = page;
}

function edit(device_id,lan_id,lan_wlan_id,gather_time,ap_enable,powerlevel,enable,
		ssid,hide,beacontype,wep_key,wpa_key,wepEncrLevel,wpaEncrMode,basicAuthMode)
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
    
	document.all("device_id_").value = device_id;
	document.all("lan_id_").value = lan_id;
	document.all("lan_wlan_id_").value =lan_wlan_id;
	document.all("gather_time").value =gather_time;
	document.all("ap_enable").value =ap_enable;
	document.all("powerlevel").value =powerlevel;
	document.all("enable").value =enable;
	document.all("ssid").value =ssid;
	document.all("beacontype").value =beacontype;
	document.all("wpaEncrMode").value =wpaEncrMode;
	document.all("basicAuthMode").value =basicAuthMode;
	
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
	
	document.all("ssid_old").value =ssid;
	document.all("hide_old").value =hide;
	document.all("beacontype_old").value =beacontype;
	
	var intext="<input type='hidden' name='wep_key_old' value='"+wep_key+"'>"
	+"<input type='hidden' name='wepEncrLevel' value='"+wepEncrLevel+"'>"
	var insectext='';
	if('40-bit'==wepEncrLevel){
		intext=intext
					+"<input type='text' name='wep_key' value='"+wep_key
					+"' class='bk' maxlength=5 size=15><font color='red'>*请输入5位字符！</font>";
		
		insectext="<input type='text' name='wep_key_sec' value='"+wep_key
					+"' class='bk' maxlength=5 size=15><font color='red'>*</font>"
	}else{
		intext=intext
				+"<input type='text' name='wep_key' value='"+wep_key
				+"' class='bk' maxlength=13 size=15><font color='red'>*请输入13位字符！</font>";
		insectext="<input type='text' name='wep_key_sec' value='"+wep_key
					+"' class='bk' maxlength=13 size=15><font color='red'>*</font>"
	}
	$("td[@name='wep_key_text']").html("");
	$("td[@name='wep_key_text']").append(intext);
	$("td[@name='wep_key_text_next']").html("");
	$("td[@name='wep_key_text_next']").append(insectext);
	
	document.all("wpa_key").value =wpa_key;
	document.all("wpa_key_sec").value =wpa_key;
	document.all("wpa_key_old").value =wpa_key;

	chg_encr(beacontype);
}

function delWlan_conn(ths, wlan_conn)
{
	if(!confirm("确实要从设备上删除该结点吗？")){
		return false;
	}
	var aWlan_conn = wlan_conn.split("|");
	if(!typeof(aWlan_conn) || !typeof(aWlan_conn.length)){
		alert("删除的结点不存在");
		return false;
	}
	var obj = ths;
	var device_id = aWlan_conn[0];
	var lan_id = aWlan_conn[1];
	var lan_wlan_id = aWlan_conn[2];
	var url = "manageWlan!delWlan.action";
	obj.disabled = true;

	$.post(url,{
      device_id:device_id,
      lan_id:lan_id,
      lan_wlan_id:lan_wlan_id,
      gwType:1
    },function(mesg){
    	if(mesg=='结点删除成功'){
    		query2db();
    	}
    	alert(mesg);
    	obj.disabled = false;
    });
}

function editWlanConn(e)
{
	var device_id=document.all("device_id_").value;
	var lan_id=document.all("lan_id_").value;
	var lan_wlan_id=document.all("lan_wlan_id_").value;
	
	var checkradios = document.all("hide");
	var hide;
	for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    hide=checkradios[i].value;
	    break;
	  }
    }
	
	var gather_time = document.all("gather_time").value;
	var ap_enable = document.all("ap_enable").value;
	var powerlevel = document.all("powerlevel").value;
	var enable = document.all("enable").value;
	var ssid=document.all("ssid").value;
	var beacontype=document.all("beacontype").value;
	var encr_mode=document.all("encr_mode").value;
	var wep_key=document.all("wep_key").value;
	var wep_key_sec=document.all("wep_key_sec").value;
	var wpa_key=document.all("wpa_key").value;
	var wpa_key_sec=document.all("wpa_key_sec").value;
	var ssid_old=document.all("ssid_old").value;
	var hide_old=document.all("hide_old").value;
	var beacontype_old=document.all("beacontype_old").value;
	var wep_key_old=document.all("wep_key_old").value;
	var wpa_key_old=document.all("wpa_key_old").value;
	
	ssid = ssid.replace(/(^\s*)|(\s*$)/g, ""); 
	if(""==ssid||null==ssid||"null"==ssid){
		alert("请填写SSID！");
		return false;
	}
	
	if("WEP"==beacontype)
	{
		if(""==wep_key||null==wep_key||"null"==wep_key){
			alert("请填写WEP模式密钥！");
			return false;
		}
		
		if(wep_key!=wep_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
		
		var wepEncrLevel=document.all("wepEncrLevel").value;
		if('40-bit'==wepEncrLevel){
			if(wep_key.length!=5){
				alert("请输入5位密码字符！");
				return false;
			}
		}else{
			if(wep_key.length!=13){
				alert("请输入13位密码字符！");
				return false;
			}
		}
	}
	
	if("WPA-PSK"==beacontype || "WPA2-PSK"==beacontype || "WPA-PSK/WPA2-PSK"==beacontype)
	{
		if(""==wpa_key||null==wpa_key||"null"==wpa_key){
			alert("请填写WPA模式密钥！");
			return false;
		}
		
		if(wpa_key!=wpa_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
		
		if(wpa_key.length<8 || wpa_key.length>63){
			alert("请输入8-63位密码字符！");
			return false;
		}
	}
	
	e.disabled = true;
	var url = "manageWlan!edit.action";
	
	$.post(url,{
		device_id:device_id,
		lan_id:lan_id,
		lan_wlan_id:lan_wlan_id,
		gather_time:gather_time,
		ap_enable:ap_enable,
		powerlevel:powerlevel,
		enable:enable,
		ssid:ssid,
		hide:hide,
		beacontype:beacontype,
		encr_mode:encr_mode,
		wepEncrLevel:wepEncrLevel,
		wep_key:wep_key,
		wpa_key:wpa_key,
		ssid_old:ssid_old,
		hide_old:hide_old,
		beacontype_old:beacontype_old,
		wep_key_old:wep_key_old,
		wpa_key_old:wpa_key_old
    },function(mesg){
    	if(mesg=='结点修改成功'){
    		query2db();
    		var tb2 = document.getElementById("tb2");
        	tb2.style.display = "none";
    	}
    	alert(mesg);
    	e.disabled = false;
    });
}

function chg_encr(enc_value) 
{
	var trs = document.getElementById("tb2").getElementsByTagName("tr");
	for (var i=0; i<trs.length; i++) 
	{
		if("tr_encr"==trs[i].isShow)
		{
			if ( "WEP"==enc_value ) 
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
			}
			else if ( "WPA-PSK"==enc_value || "WPA2-PSK"==enc_value || "WPA-PSK/WPA2-PSK"==enc_value) 
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
			}else{
				trs[i].style.display = "none";
			}
		}
		
		if("WEP"==enc_value){
			if ( "wep_key"==trs[i].isShow) {
				trs[i].style.display = "";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "none";
			}
		}else if("WPA-PSK"==enc_value || "WPA2-PSK"==enc_value || "WPA-PSK/WPA2-PSK"==enc_value){
			if ( "wep_key"==trs[i].isShow) {
				trs[i].style.display = "none";
			}else if("wpa_key"==trs[i].isShow){
				trs[i].style.display = "";
			}
		}else{
			if ( "wep_key"==trs[i].isShow || "wpa_key"==trs[i].isShow) {
				trs[i].style.display = "";
				trs[i].style.display = "none";
			}
		}
	}
}

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
}

function add(device_id,wepEncrLevel) 
{
	var tb2 = document.getElementById("tb2");
	var trs = tb2.getElementsByTagName("tr");
	tb2.style.display = "";
	
	document.all("device_id_").value = device_id;
	document.all("beacontype").value = "None";
	
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
	
	var intext="<input type='hidden' name='wep_key_old' value=''>"
				+"<input type='hidden' name='wepEncrLevel' value='"+wepEncrLevel+"'>"
	var insectext='';
	if('40-bit'==wepEncrLevel){
		intext=intext+"<input type='text' name='wep_key' value='' class='bk' maxlength=5 size=15><font color='red'>*请输入5位字符！</font>";
		insectext="<input type='text' name='wep_key_sec' value='' class='bk' maxlength=5 size=15><font color='red'>*</font>"
	}else{
		intext=intext+"<input type='text' name='wep_key' value='' class='bk' maxlength=13 size=15><font color='red'>*请输入13位字符！</font>";
		insectext="<input type='text' name='wep_key_sec' value='' class='bk' maxlength=13 size=15><font color='red'>*</font>"
	}
	$("td[@name='wep_key_text']").html("");
	$("td[@name='wep_key_text']").append(intext);
	$("td[@name='wep_key_text_next']").html("");
	$("td[@name='wep_key_text_next']").append(insectext);
	
	document.all("ssid").value = "";
	document.all("wep_key").value = "";
	document.all("wep_key_sec").value = "";;
	
	document.all("wpa_key").value = "";;
	document.all("wpa_key_sec").value = "";
}

function addWlanConn(e) 
{
	var device_id=document.all("device_id_").value;
	
	var checkradios = document.all("hide");
	var hide;
	for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    hide=checkradios[i].value;
	    break;
	  }
    }
	
	var ssid=document.all("ssid").value;
	var beacontype=document.all("beacontype").value;
	var encr_mode=document.all("encr_mode").value;
	var wep_key=document.all("wep_key").value;
	var wep_key_sec=document.all("wep_key_sec").value;
	var wpa_key=document.all("wpa_key").value;
	var wpa_key_sec=document.all("wpa_key_sec").value;
	
	ssid = ssid.replace(/(^\s*)|(\s*$)/g, ""); 
	
	if(""==ssid||null==ssid||"null"==ssid){
		alert("请填写SSID！");
		return false;
	}
	
	if("WEP"==beacontype){
		if(""==wep_key||null==wep_key||"null"==wep_key){
			alert("请填写WEP模式密钥！");
			return false;
		}
		
		if(wep_key!=wep_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}
		
		var wepEncrLevel=document.all("wepEncrLevel").value;
		if(('40-bit'==wepEncrLevel && wep_key.length!=5) 
				|| ('104-bit'==wepEncrLevel && wep_key.length!=13)){
			alert("输入的密钥长度不符规范！");
			return false;
		}
	}
	
	if("WPA-PSK"==beacontype || "WPA2-PSK"==beacontype || "WPA-PSK/WPA2-PSK"==beacontype ){
		if(""==wpa_key||null==wpa_key||"null"==wpa_key){
			alert("请填写WPA模式密钥！");
			return false;
		}
		
		if(wpa_key!=wpa_key_sec){
			alert("两次输入的密钥不一样！");
			return false;
		}

		if(wpa_key.length<8 || wpa_key.length>63){
			alert("输入的密钥长度不符规范！");
			return false;
		}
	}
	
	e.disabled = true;
	var url = "manageWlan!add.action";
	
	$.post(url,{
		device_id:device_id,
		ssid:ssid,
		hide:hide,
		beacontype:beacontype,
		encr_mode:encr_mode,
		wep_key:wep_key,
		wpa_key:wpa_key
    },function(mesg){
    	if(mesg=='结点新增成功'){
    		query2db();	
    	}
    	alert(mesg);
    	
    	tb2.style.display = "none";
    	e.disabled = false;
    });
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD="post">
<input type="hidden" name="device_id_old_" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite" nowrap>
						WLAN配置
					</td>
					<td nowrap>
						<img src="../images/attention_2.gif" width="15" height="12">
					</td>
				</tr>
			</table>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR bgcolor="#FFFFFF">
								<td colspan="4">
									<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
								</td>
							</TR>
							<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
								<td nowrap align="right" class=column>设备属地</td>
								<td id="tdDeviceCityName"></td>
								<td nowrap align="right" class=column>设备序列号</td>
								<td id="tdDeviceSn"></td>
							</TR>
							<TR>
								<TD colspan="4" align="right" CLASS="green_foot">
									<INPUT TYPE="button" value="数据库中查询" class=btn onclick="query2db()">
									&nbsp;
									<INPUT TYPE="button" value="设备实时获取" class=btn onclick="query2device()">
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<tr>
					<TD bgcolor=#999999>
						<div id="div_WLAN_PARAM">
							<span></span>
						</div>
					</td>
				</tr>
			</TABLE>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<input type="hidden" name="device_id_" />
				<input type="hidden" name="lan_id_" />
				<input type="hidden" name="lan_wlan_id_" />
				<input type="hidden" name="gather_time" />
				<input type="hidden" name="ap_enable" />
				<input type="hidden" name="powerlevel" />
				<input type="hidden" name="enable" />
				<input type="hidden" name="wpaEncrMode" />
				<input type="hidden" name="basicAuthMode" />
				<TR>
					<TD bgcolor=#999999>
						<TABLE id="tb2" border=0 cellspacing=1 cellpadding=2 width="100%" STYLE="display: none">
							<TR isShow="edit" STYLE="display: none">
								<TH align="center" colspan=4 valign="center">
									<B><span id="actLabel">修改</span></B>
								</TH>
							</TR>
							<TR isShow="add" STYLE="display: none">
								<TH align="center" colspan=4 valign="center">
									<B><span id="actLabel">新增</span></B>
								</TH>
							</TR>
							<TR bgcolor="#FFFFFF" isShow="all" STYLE="display: none">
								<TD width="40%" class=column align="right">SSID</TD>
								<TD width="60%" colspan=3>
									<input type="hidden" name="ssid_old">
									<INPUT TYPE="text" NAME="ssid" maxlength=25 class=bk size=25>
									<font color="red">*</font>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" isShow="all" STYLE="display: none">
								<TD class=column align="right">是否隐藏</TD>
								<TD colspan=3>
									<input type="hidden" name="hide_old">
									<input type="radio" value="0" name="hide" />否
									<input type="radio" value="1" name="hide" />是
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" isShow="all" STYLE="display: none">
								<TD class=column align="right">认证模式</TD>
								<TD colspan=3>
									<input type="hidden" name="beacontype_old">
									<SELECT name="beacontype" class="bk" onchange="chg_encr(this.value)">
									</SELECT>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" name="tr_encr" isShow="tr_encr" STYLE="display: none">
								<TD class=column align="right">加密模式</TD>
								<TD colspan=3>
									<input type="hidden" name="encr_mode_old">
									<SELECT name="encr_mode" class="bk" onchange="chg_auth(this.value)">
									</SELECT>
								</TD>
							</TR>
							
							<TR bgcolor="#FFFFFF" isShow="wep_key" style="display: none">
								<TD align="right">加密密钥:</TD>
								<TD name="wep_key_text"></TD>
							</TR>
							<TR bgcolor="#FFFFFF" isShow="wep_key" style="display: none">
								<TD align="right">再一次输入加密密钥:</TD>
								<TD name="wep_key_text_next"></TD>
							</TR>
							
							<TR bgcolor="#FFFFFF" isShow="wpa_key" style="display: none">
								<TD align="right">加密密钥:</TD>
								<TD>
									<input type="hidden" name="wpa_key_old">
									<input type="text" name="wpa_key" class="bk" maxlength=63 size=15>
									<font color="red">*请输入8-63位字符！</font>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" isShow="wpa_key" style="display: none">
								<TD align="right">再一次输入加密密钥:</TD>
								<TD>
									<input type="text" name="wpa_key_sec" class="bk" maxlength=63 size=15>
									<font color="red">*</font>
								</TD>
							</TR>
							
							<TR isShow="edit" STYLE="display: none">
								<TD colspan="4" align="right" class=green_foot>
									<INPUT TYPE="button" value="更 新 " onclick="editWlanConn(this)" class=btn>
									&nbsp;&nbsp;
								</TD>
							</TR>
							<TR isShow="add" STYLE="display: none">
								<TD colspan="4" align="right" class=green_foot>
									<INPUT TYPE="button" value="新 增 " onclick="addWlanConn(this)" class=btn>
									&nbsp;&nbsp;
								</TD>
							</TR>
						</TABLE>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
