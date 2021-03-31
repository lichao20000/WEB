<%--
手工工单
Author: Gongsj
Version: 1.0.0
Date: 2009-07-29
--%>

<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>手工工单</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

$(document).ready(function(){
	
	var deviceId = "<s:property value="deviceId"/>";
	var sheetType = "<s:property value="sheetType"/>";

    var deviceSn = "<s:property value="deviceSn"/>";
    var userAccount = "<s:property value="userAccount"/>";
	
	if (deviceSn != undefined && deviceSn != "") {
		$("tr[@id='resultTR']").hide();
		$("td[@id='wanTitle']").html("业务参数");
		$("tr[@id='balnkInfo']").show();
		$("tr[@id='wanConnInfo']").show();
		$("input[@name='vlanId']").hide();
		$("select[@name='ipType']").hide();

		$("input[@name='deviceId']").val(deviceId);
		
		if (sheetType == "17") {
			//上网
			$("td[@id='sheetTypeTd']").html("");
			$("td[@id='sheetTypeTd']").html("<SELECT name='sheetType' class='bk' onclick= chgSheetType(this.value);><OPTION value='17'>上网</OPTION></SELECT>");
			
		} else if(sheetType == "16") {
			//IPTV
			$("td[@id='sheetTypeTd']").html("");
			$("td[@id='sheetTypeTd']").html("<SELECT name='sheetType' class='bk' onclick= chgSheetType(this.value);><OPTION value='16'>IPTV</OPTION></SELECT>");
		} else {
			$("td[@id='sheetTypeTd']").html("");
			$("td[@id='sheetTypeTd']").html("<SELECT name='sheetType' class='bk' onclick= chgSheetType(this.value);><OPTION value='18'>IPTV</OPTION></SELECT>");
		}
	} else if (deviceId != undefined && deviceId != ""){
		$("tr[@id='resultTR']").show();
		$("td[@id='resultTD']").html("提示：");
		
		$("div[@id='result']").html("");
		$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">未查询到相关设备信息！</FONT>");
	}
});


function onEnterDown() {
	if (event.keyCode==13) {
		searchSheetInfo();
	}
}

/**
 * 上网或IPTV或者VOIP
 */
function chgSheetType(e) {
	if("18"==e){     //如果业务类型是VOIP 
	   for(var i = 1; i<10;i++){
			$("#tr_voip_"+i).show();
	    }
	    $("#tr_bindport").hide();
	} else{
		for(var i = 1; i<10;i++){
			$("#tr_voip_"+i).hide();
	    }
	     $("#tr_bindport").show();
	}
}

/**
 * IP_Routed或PPPoE_Bridged
 */
function chgConnType(e){
	if("IP_Routed"==e){
		$("tr[@id='ppp_type_2']").show();
    } else if("PPPoE_Bridged"==e){
    	$("tr[@id='ppp_type_2']").hide();
    }

}

/**
 * PPP或IP
 */
function chgSessionType(e) {
	var ipType = $("select[@name='ipType']").val();
	var connType = $("select[@name='connType']").val();
	
	if ("1" == e) {
		//PPP方式
		$("tr[@id='ip_type_2']").hide();
		$("tr[@id='ip_type_3']").hide();

		if (connType == 'IP_Routed') {
			$("tr[@id='ppp_type_2']").show();
		} else {
			$("tr[@id='ppp_type_2']").hide();
		}
		
		
		$("select[@name='connType']").show();
		$("select[@name='ipType']").hide();
		
	} else if ("2" == e && "Static" == ipType) {
		$("tr[@id='ip_type_2']").show();
		$("tr[@id='ip_type_3']").show();

		$("select[@name='ipType']").show();
		
		$("select[@name='connType']").hide();
		$("tr[@id='ppp_type_2']").hide();
	}else {
		$("tr[@id='ip_type_2']").hide();
		$("tr[@id='ip_type_3']").hide();

		$("select[@name='ipType']").show();
		
		$("select[@name='connType']").hide();
		$("tr[@id='ppp_type_2']").hide();
	}

}

/**
 * DHCP或Static
 */
function chgIpType(e) {
	var sessType = $("select[@name='sessionType']").val();
	
	if ("DHCP" == e && "2" == sessType) {
		$("tr[@id='ip_type_2']").hide();
		$("tr[@id='ip_type_3']").hide();
	} else if ("Static" == e && "2" == sessType) {

		$("tr[@id='ip_type_2']").show();
		$("tr[@id='ip_type_3']").show();
	}

}
/**
 * IPV6的 DHCP或Static
 */
function chgAddressOrigin(e) {
	var addressOrigin = $("select[@name='address_origin']").val();
	
	if("3"==addressOrigin){
		$("#tr_address_v6_1").show();
		$("#tr_address_v6_2").show();
	}else{
		$("#tr_address_v6_1").hide();
		$("#tr_address_v6_2").hide();
	}
}
/**
 * ADSL或LAN
 */
function chgAccessType(e) {
	if ("ADSL" == e) {
		$("input[@name='vpi']").show();
		$("input[@name='vci']").show();
		$("input[@name='vlanId']").hide();

	} else {
		$("input[@name='vpi']").hide();
		$("input[@name='vci']").hide();
		$("input[@name='vlanId']").show();
	}
}

/**
 * 查询用户或设备对应的信息（下发工单用）
 */
function searchSheetInfo() {
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var userAccount = $.trim($("input[@name='userAccount']").val());
	if(deviceSn.length<6 && deviceSn.length>0){
		alert("请输入至少最后六位设备序列号！");
		document.frm.deviceSn.focus();
		$("tr[@id='balnkInfo']").hide();
		$("tr[@id='wanConnInfo']").hide();
		return false;
	}
	var url = "<s:url value='/gtms/blocTest/businessSheetDispatchInfo!getBaseInfo.action'/>";
	$.post(url,{
		deviceSn:deviceSn,
		userAccount:userAccount
		
	},function(ajax){
			if (ajax == "1") {
			$("tr[@id='resultTR']").show();
			$("td[@id='resultTD']").html("提示：");
			
			$("div[@id='result']").html("");
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">“用户账号”和“设备序列号”至少输入一个！</FONT>");
			$("tr[@id='balnkInfo']").hide();
			$("tr[@id='wanConnInfo']").hide();
			
		} else if (ajax == "2") {
			$("tr[@id='resultTR']").show();
			$("td[@id='resultTD']").html("提示：");
			
			$("div[@id='result']").html("");
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">未查询到相关设备信息！</FONT>");
			$("tr[@id='balnkInfo']").hide();
			$("tr[@id='wanConnInfo']").hide();
			
		} else if (ajax == "3") {
			$("tr[@id='resultTR']").show();
			$("td[@id='resultTD']").html("提示：");
			
			$("div[@id='result']").html("");
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">根据指定条件查出多于一台设备，请输入更详细的查询条件！</FONT>");
			$("tr[@id='balnkInfo']").hide();
			$("tr[@id='wanConnInfo']").hide();
		} else {
			$("tr[@id='resultTR']").hide();
			
		//	$("#paramTR").show();
			
			$("td[@id='wanTitle']").html("业务参数");
			$("tr[@id='balnkInfo']").show();
			$("tr[@id='wanConnInfo']").show();
			$("input[@name='vlanId']").hide();
			$("select[@name='ipType']").hide();
			$("input[@name='deviceId']").val(ajax);

		}
	});
}
/**
 * 下发业务工单
 */
function doBusinessSheet() {
	var deviceId = $("input[@name='deviceId']");
	var sheetType = $("select[@name='sheetType']");
	
	var accessType = $("select[@name='accessType']");
	var sessionType = $("select[@name='sessionType']");
	var connType = $("select[@name='connType']");
	var vpi = $("input[@name='vpi']");
	var vci = $("input[@name='vci']");
	var ipType = $("select[@name='ipType']");
	var vlanId = $("input[@name='vlanId']");
	var totalNum = $("input[@name='totalNumber']");
	
	var username = $("input[@name='username']");
	var password = $("input[@name='password']");
	var ip = $("input[@name='ip']");
	var gateway = $("input[@name='gateway']");
	var mask = $("input[@name='mask']");
	var dns = $("input[@name='dns']");

	var lan1 = $("input[@name='lan1'][@checked]");
	var lan2 = $("input[@name='lan2'][@checked]");
	var lan3 = $("input[@name='lan3'][@checked]");
	var lan4 = $("input[@name='lan4'][@checked]");
	var lan5 = $("input[@name='lan5'][@checked]");
	var lan6 = $("input[@name='lan6'][@checked]");
	var lan7 = $("input[@name='lan7'][@checked]");
	var lan8 = $("input[@name='lan8'][@checked]");
	
	var bindPort = "";
	//IPV6新增加
	var addressOrigin  = $("select[@name='address_origin']");
	var aftr   =  $("input[@name='aftr']");
	var ipv6AddressIp =  $("input[@name='ipv6_address_ip']");
	var ipv6AddressDNS   =   $("input[@name='ipv6_address_dns']");
	var ipv6AddressPrefix  = $("input[@name='ipv6_address_prefix']");
	//var str ="addressOrigin=" +addressOrigin.val()+"aftr"+aftr.val()+"ipv6AddressIp"+ipv6AddressIp.val()+"ipv6AddressDNS"+ipv6AddressDNS.val();
	//IPV6新增加//当为VOIP业务时
	var _voipTelepone = $("input[@name='voipTelepone']");
	var _voipUsername = $("input[@name='voipUsername']");
	var _voipPasswd = $("input[@name='voipPasswd']");
	var _sipIp = $("input[@name='sipIp']");
	var _sipPort = $("input[@name='sipPort']");
	var _standSipIp = $("input[@name='standSipIp']");
	var _standSipPort = $("input[@name='standSipPort']");
	
	var _registrarServer = $("input[@name='registrarServer']");
	var _registrarServerPort = $("input[@name='registrarServerPort']");
	var _standRegistrarServer = $("input[@name='standRegistrarServer']");
	var _standRegistrarServerPort = $("input[@name='standRegistrarServerPort']");
	
	var _outboundProxy = $("input[@name='outboundProxy']");
	var _outboundProxyPort = $("input[@name='outboundProxyPort']");
	var _standOutboundProxy = $("input[@name='standOutboundProxy']");
	var _standOutboundProxyPort = $("input[@name='standOutboundProxyPort']");
	var _linePort = $("select[@name='linePort']");
	var _protocol = $("select[@name='protocol']");
   
	if (lan1.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan1']").val()+",";
	}

	if (lan2.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan2']").val()+",";
	}

	if (lan3.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan3']").val()+",";
	}

	if (lan4.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan4']").val()+",";
	}

	if (lan5.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan5']").val()+",";
	}

	if (lan6.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan6']").val()+",";
	}

	if (lan7.val() == undefined) {
	} else {
		bindPort += $("input[@name='lan7]").val()+",";
	}

	if (lan8.val() == undefined) {
		bindPort = bindPort.substring(0,bindPort.length - 1);
	} else {
		bindPort += $("input[@name='lan8']").val();
	}
	if("ADSL"==accessType.val()){
		if(!IsNumber(vpi.val(),"vpi")){
			vpi.focus();
			return false;
		}
		if(!IsNumber(vci.val(),"vci")){
			vci.focus();
			return false;
		}
	}
	if(connType.val()=="IP_Routed"&& sessionType.val()=="1"){
		if(!IsNull(username.val(),"PPPoE账号")){
			username.focus();
			return false;
		}
		if(!IsNull(password.val(),"PPPoE密码")){
			password.focus();
			return false;
		}
	}
	
	if(sessionType.val()=="2" && ipType=="Static"){
		if(!IsIPAddr2(ip.val(),"IP地址")){
			ip.focus();
			return false;
		}
		if(!IsIPAddr2(gateway.val(),"网关")){
			gateway.focus();
			return false;
		}
		if(!IsIPAddr2(mask.val(),"子网掩码")){
			mask.focus();
			return false;
		}
		if(!IsIPAddr2(dns.val(),"DNS")){
			dns.focus();
			return false;
		}
	}
	//IPV6
	if(!IsNull(aftr.val(),"AFTR")){
			aftr.focus();
			return false;
		}	
	if(addressOrigin.val()=="3"){
		if(!IsNull(ipv6AddressIp.val(),"IPV6地址")){
			ipv6AddressIp.focus();
			return false;
		}  
		if(!IsNull(ipv6AddressDNS.val(),"IpV6DNS")){
			ipv6AddressDNS.focus();
			return false;
		}
		if(!IsNull(ipv6AddressPrefix.val(),"地址前缀")){
			ipv6AddressPrefix.focus();
			return false;
		}
	}
	//IPV6
    /**
     *当业务为VOIP时 验证
     */
    //VOIP认证账号
    if("18"==sheetType.val()){
		if(!IsNull(_voipUsername.val(), "VOIP认证账号")){
			_voipUsername.focus();
			return false;
		}
		//VOIP认证密码
		if(!IsNull(_voipPasswd.val(), "VOIP认证密码")){
			_voipPasswd.focus();
			return false;
		}
		//主SIP服务器地址
		if(!IsNull(_sipIp.val(), "主SIP服务器地址")){
			_sipIp.focus();
			return false;
		}
		//主SIP服务器端口
		if(!IsNull(_sipPort.val(), "主SIP服务器端口")){
			_sipPort.focus();
			return false;
		}
		//备SIP服务器地址
		if(!IsNull(_standSipIp.val(), "备SIP服务器地址")){
			_standSipIp.focus();
			return false;
		}
		//备SIP服务器端口
		if(!IsNull(_standSipPort.val(), "备SIP服务器端口")){
			_standSipPort.focus();
			return false;
		}
		
		//主RegistrarServer
		if(!IsNull(_registrarServer.val(), "主RegistrarServer")){
			_registrarServer.focus();
			return false;
		}
		//主RegistrarServerPort
		if(!IsNull(_registrarServerPort.val(), "主RegistrarServerPort")){
			_registrarServerPort.focus();
			return false;
		}
		//备RegistrarServer
		if(!IsNull(_standRegistrarServer.val(), "备RegistrarServer")){
			_standRegistrarServer.focus();
			return false;
		}
		//备RegistrarServerPort
		if(!IsNull(_standRegistrarServerPort.val(), "备RegistrarServerPort")){
			_standRegistrarServerPort.focus();
			return false;
		}//主OutboundProxy
		if(!IsNull(_outboundProxy.val(), "主OutboundProxy")){
			_outboundProxy.focus();
			return false;
		}
		//主OutboundProxyPort
		if(!IsNull(_outboundProxyPort.val(), "主OutboundProxyPort")){
			_outboundProxyPort.focus();
			return false;
		}
		//备OutboundProxy
		if(!IsNull(_standOutboundProxy.val(), "备OutboundProxy")){
			_standOutboundProxy.focus();
			return false;
		}
		//备OutboundProxyPort
		if(!IsNull(_standOutboundProxyPort.val(), "备OutboundProxyPort")){
			_standOutboundProxyPort.focus();
			return false;
		}
		
		//业务电话号码
		if(!IsNull(_voipTelepone.val(), "业务电话号码")){
			_voipTelepone.focus();
			return false;
		}
		//语音端口
		if('' == _linePort.val() || '-1' == _linePort.val()){
			alert("请选择语音端口");
			_linePort.focus();
			return false;
		}
		//协议类型
		if('' == _protocol.val() || '-1' == _protocol.val()){
			alert("请选择协议类型");
			_protocol.focus();
			return false;
		}
	}
	var url = "<s:url value='/gtms/blocTest/businessSheetDispatchInfo!add.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),

		sheetType:sheetType.val(),
		
		accessType:accessType.val(),
		
		sessionType:sessionType.val(),
		
		connType:connType.val(),
		vpi:vpi.val(),
		vci:vci.val(),

		ipType:ipType.val(),
		vlanId:vlanId.val(),

		username:username.val(),
		password:password.val(),

		ip:ip.val(),
		gateway:gateway.val(),
		mask:mask.val(),
		dns:dns.val(),

		bindPort:bindPort,
		addressOrigin : addressOrigin.val(),
		aftr  : aftr.val(),
		ipv6AddressIp :  ipv6AddressIp.val(),
		ipv6AddressDNS : ipv6AddressDNS.val(),
		ipv6AddressPrefix : ipv6AddressPrefix.val(),
		
		voipTelepone:_voipTelepone.val(),
		voipUsername:_voipUsername.val(),
		voipPasswd:_voipPasswd.val(),
		sipIp:_sipIp.val(),
		sipPort:_sipPort.val(),
		standSipIp:_standSipIp.val(),
		standSipPort:_standSipPort.val(),
		
		registrarServer: _registrarServer.val(),
		registrarServerPort :_registrarServerPort.val(),
		standRegistrarServer:_standRegistrarServer.val(),
		standRegistrarServerPort:_standRegistrarServerPort.val(),
		
		outboundProxy:_outboundProxy.val(),
		outboundProxyPort:_outboundProxyPort.val(), 
		standOutboundProxy:_standOutboundProxy.val(),
		standOutboundProxyPort :_standOutboundProxyPort.val(),
		linePort :_linePort.val(),
		protocol :_protocol.val()
		
	},function(ajax){
		
		$("tr[@id='resultTR1']").show();
		$("td[@id='resultTD1']").html("执行结果：");
		
		$("div[@id='result1']").html("通知后台");
		if (ajax == "1")
		{
			$("div[@id='result1']").append("成功");
		} else {
			$("div[@id='result1']").append("<FONT COLOR=\"#FF0000\">失败</FONT>");
		}

	});
	
}

</SCRIPT>

</HEAD>

<BODY>

<FORM NAME="frm" METHOD="post" action="">

<input type="hidden" name="deviceId" value="" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">工单管理</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"> 在“终端上”开通某一业务（上网、IPTV）</td>
					</tr>
				</table>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH colspan="4">下发业务工单</TH>
						</TR>
						<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: ">
	
							<TD class=column align="right" nowrap width="15%">业务类型:</TD>
							<TD width="35%" id="sheetTypeTd">
								<SELECT name="sheetType" class="bk" onclick= chgSheetType(this.value);>
									<OPTION value="17">上网</OPTION>
								</SELECT>
							</TD>
	
							<TD width="15%" class=column align="right">操作类型:</TD>
							<TD width="35%" id="operationTypeTd">开户</TD>

						</TR>
	
						<TR bgcolor="#FFFFFF" STYLE="display: ">
	
							<TD class=column align="right" width="15%">用户帐号:</TD>
							<TD width="35%">
								<input type="text" name="userAccount" class="bk" ONKEYDOWN="onEnterDown()" value="<s:property value="userAccount"/>">
							</TD>
							
							<TD class=column align="right" nowrap width="15%">设备序列号:</TD>
							<TD width="35%">
								<input type="text" name="deviceSn" class="bk" ONKEYDOWN="onEnterDown()" value="<s:property value="deviceSn"/>"><font color="red">至少最后六位</font>
							</TD>
							
						</TR>
	
						<TR bgcolor="#FFFFFF">
							<TD colspan="4" class=column align="right" nowrap >
								<button type="button" name="searchBtn" onclick="searchSheetInfo()">查 询</button>
							</TD>
						</TR>
						
						<TR id="resultTR" bgcolor="#FFFFFF" style="display:none">
							<TD class=column align="right" width="15%" id="resultTD"></TD>
							<TD colspan="3">
								<DIV id="result"></DIV>
							</TD>
						</TR>
						
					</TABLE>
				</TD>
			</TR>
			
			<TR height="20" id="balnkInfo" STYLE="display:none">
				<TD colspan="1"  width="15" class=column>
				</TD>
			</TR>
			<TR aligh="left" id="paramTR"  bgcolor="#FFFFFF" style="display:none">
				<TD class=column  id="paramTD" bgcolor="#999999" >
					<div id ="param"></div>
				</TD>
			</TR>
			<TR align="left" id="wanConnInfo" STYLE="display:none">
			<TD colspan="4"  bgcolor=#999999>
			
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
							<tr align="center">
								<td align="left" colspan="4" class="green_title" id="wanTitle"></td>
							</tr>
							
							<TR bgcolor="#FFFFFF" STYLE="display:">
								<TD width="15%" class=column align="right">上行方式:</TD>
								<TD width="35%">
									<SELECT name="accessType" class="bk" onclick="chgAccessType(this.value)">
										<OPTION value="ADSL">ADSL上行</OPTION>
										<OPTION value="LAN">LAN上行</OPTION>
										<OPTION value="PON">PON上行</OPTION>
									</SELECT>
								</TD>
								
								<TD width="15%" class=column align="right">连接方式:</TD>
								<TD width="35%">
									<SELECT name="sessionType" class="bk" onclick="chgSessionType(this.value)">
										<OPTION value="1">PPP拨号</OPTION>
										<OPTION value="2">IP方式</OPTION>
									</SELECT>
								</TD>
								
							</TR>
							
							<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display:">
								
								<TD class=column align="right" nowrap width="15%">连接类型:</TD>
								<TD width="35%" id="connTypeTd">
									
									<SELECT name="connType" class="bk" onclick="chgConnType(this.value)">
										<OPTION value="IP_Routed" >路由</OPTION>
									</SELECT>
								</TD>
								
								<TD width="15%" class=column align="right">地址类型:</TD>
								<TD width="35%" id="ipTypeTd">
									<SELECT name="ipType" class="bk" onclick="chgIpType(this.value)">
										<OPTION value="DHCP">DHCP</OPTION>
										
									</SELECT>
								</TD>
								
							</TR>
							
							
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" nowrap width="15%">PVC:</TD>
								<TD width="35%">
									<input type="text" name="vpi" maxlength=3 size=3 class="bk"/>/
									<input type="text" name="vci" maxlength=3 size=3 class="bk"/>
								</TD>
								
								<TD width="15%" class=column align="right">VLANID:</TD>
								<TD width="35%"> 
									<input type="text" name="vlanId" maxlength=20 size=20 class="bk"/>
								</TD>
									
							</TR>
							
							<TR id="ppp_type_2" bgcolor="#FFFFFF" >
								<TD width="15%" class=column align="right">PPPoE账号:</TD>
								<TD width="35%"  >
									<INPUT TYPE="text" NAME="username" maxlength=20 class="bk" size=20>
								</TD>
								<TD width="15%" class=column align="right">PPPoE密码:</TD>
								<TD width="35%" >
									<INPUT TYPE="text" NAME="password" maxlength=20 class="bk" size=20>
								</TD>
							</TR>
							
							<TR id="ip_type_2" bgcolor="#FFFFFF" STYLE="display:none">
								<TD class=column align="right" nowrap width="15%">IP地址:</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="ip" maxlength=20 size=20 class="bk">
								</TD>
								<TD width="15%" class=column align="right">网关:</TD>
								<TD width="35%" >
									<INPUT TYPE="text" NAME="gateway" maxlength=20 size=20 class="bk">
								</TD>
								
							</TR>
							<TR id="ip_type_3" bgcolor="#FFFFFF" STYLE="display:none">
								<TD width="15%" class=column align="right">子网掩码:</TD>
								<TD width="35%" >
									<INPUT TYPE="text" NAME="mask" maxlength=20 size=20 class="bk">
								</TD>
								<TD class=column align="right" width="15%">DNS:</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="dns" maxlength=40 size=40 class="bk">
								</TD>
								
							</TR>
							
							<TR bgcolor="#FFFFFF" id="tr_bindport">
								
								<TD class=column align="right" width="15%">绑定端口:</TD>
								<TD width="35%" colspan="3">
									
									<INPUT TYPE="checkbox" NAME="lan1" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.">LAN1
									<INPUT TYPE="checkbox" NAME="lan2" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2.">LAN2
									<INPUT TYPE="checkbox" NAME="lan3" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3.">LAN3
									<INPUT TYPE="checkbox" NAME="lan4" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4.">LAN4
									
									<INPUT TYPE="checkbox" NAME="lan5" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1">WLAN1
									<INPUT TYPE="checkbox" NAME="lan6" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2">WLAN2
									<INPUT TYPE="checkbox" NAME="lan7" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3">WLAN3
									<INPUT TYPE="checkbox" NAME="lan8" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4">WLAN4
									
								</TD>
								
							</TR>
							<!--IPV6  -->
							<TR id="tr_address_origin" bgcolor="#FFFFFF" STYLE="display:">
								
								<TD class=column align="right" nowrap width="15%">地址分配机制:</TD>
								<TD width="35%" id="td_address_origin">
									
									<SELECT name="address_origin" class="bk" onclick="chgAddressOrigin(this.value)">
										<OPTION value="1">AutoConfigured</OPTION>
										<OPTION value="2" >DHCPv6</OPTION>
										<OPTION value="3" >Static</OPTION>
									</SELECT>
								</TD>
								
								<TD width="15%" class=column align="right">AFTR:</TD>
								<TD width="35%" >
									<INPUT TYPE="text" NAME="aftr" maxlength=39 size=20 class="bk">
								</TD>
								
							</TR>
							<TR id="tr_address_v6_1" bgcolor="#FFFFFF" STYLE="display:none">
								
								<TD class=column align="right" nowrap width="15%">IP地址:</TD>
								<TD width="35%" >
									<INPUT TYPE="text" NAME="ipv6_address_ip" maxlength=39 size=20 class="bk">
								</TD>
								
								<TD width="15%" class=column align="right">DNS:</TD>
								<TD width="35%" >
									<INPUT TYPE="text" NAME="ipv6_address_dns" maxlength=39 size=20 class="bk">
								</TD>
								
							</TR>
							<TR id="tr_address_v6_2" bgcolor="#FFFFFF" STYLE="display:none">
								
								<TD class=column align="right" nowrap width="15%">地址前缀:</TD>
								<TD width="85%"  colspan="3">
									<INPUT TYPE="text" NAME="ipv6_address_prefix" maxlength=39 size=20 class="bk">
								</TD>
							</TR>
							<!--IPV6  -->
							<!-- VOIP -->
							<TR bgcolor="#FFFFFF" id="tr_voip_1" style="display:none">
								<TD class=column align="right" nowrap>VOIP认证账号</TD>
								<TD><INPUT TYPE="text" NAME="voipUsername" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>VOIP认证密码</TD>
								<TD><INPUT TYPE="text" NAME="voipPasswd" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_2" style="display:none">
								<TD class=column align="right" nowrap>主SIP服务器地址</TD>
								<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
									value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>主SIP服务器端口</TD>
								<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
									value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_3" style="display:none">
								<TD class=column align="right" nowrap>备SIP服务器地址</TD>
								<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>备SIP服务器端口</TD>
								<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
					
							<TR bgcolor="#FFFFFF" id="tr_voip_4" style="display:none">
								<TD class=column align="right" nowrap>主RegistrarServer</TD>
								<TD><INPUT TYPE="text" NAME="registrarServer" maxlength=20 class=bk
									value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>主RegistrarServerPort</TD>
								<TD><INPUT TYPE="text" NAME="registrarServerPort" maxlength=20 class=bk
									value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_5" style="display:none">
								<TD class=column align="right" nowrap>备RegistrarServer</TD>
								<TD><INPUT TYPE="text" NAME="standRegistrarServer" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>备RegistrarServerPort</TD>
								<TD><INPUT TYPE="text" NAME="standRegistrarServerPort" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_6" style="display:none">
								<TD class=column align="right" nowrap>主OutboundProxy</TD>
								<TD><INPUT TYPE="text" NAME="outboundProxy" maxlength=20 class=bk
									value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>主OutboundProxyPort</TD>
								<TD><INPUT TYPE="text" NAME="outboundProxyPort" maxlength=20 class=bk
									value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_7" style="display:none">
								<TD class=column align="right" nowrap>备OutboundProxy</TD>
								<TD><INPUT TYPE="text" NAME="standOutboundProxy" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>备OutboundProxyPort</TD>
								<TD><INPUT TYPE="text" NAME="standOutboundProxyPort" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_8" style="display:none">
								<TD class=column align="right" nowrap>业务电话号码</TD>
								<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
									class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
								<TD class=column align="right" nowrap>语音端口</TD>
								<TD><select name="linePort" class=bk>
									<option value="-1">==请选择语音端口==</option>
									<option value="V1">V1</option>
									<option value="V2">V2</option>
								</select> &nbsp; <font color="#FF0000">*</font></TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr_voip_9" style="display:none">
								<TD class=column align="right" nowrap>SIP协议类型</TD>
								<TD colspan="3"><select name="protocol" class=bk>
									<option value="-1">==请选择协议类型==</option>
									<option value="1">软交换</option>
									<option value="0">IMS</option>
								</select> &nbsp; <font color="#FF0000">*</font></TD>
							</TR>
							<!-- VOIP -->
							<TR bgcolor="#FFFFFF">
								<TD colspan="4" class=column align="right" nowrap >
									<button type="button" name="subBtn" onclick="doBusinessSheet()" >下发业务工单</button>
								</TD>
							</TR>
							
							<TR id="resultTR1" bgcolor="#FFFFFF" style="display:none">
								<TD class=column align="right" width="15%"  id="resultTD1"></TD>
								<TD colspan="3">
									<DIV id="result1"></DIV>
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
</BODY>
</HTML>