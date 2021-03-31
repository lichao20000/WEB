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
		
		if (sheetType == "1") {
			//上网
			$("td[@id='sheetTypeTd']").html("");
			$("td[@id='sheetTypeTd']").html("<SELECT name='sheetType' class='bk' onclick= chgSheetType(this.value);><OPTION value='1'>上网</OPTION></SELECT>");
			
		} else {
			//IPTV
			$("td[@id='sheetTypeTd']").html("");
			$("td[@id='sheetTypeTd']").html("<SELECT name='sheetType' class='bk' onclick= chgSheetType(this.value);><OPTION value='2'>IPTV</OPTION></SELECT>");
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
 * 上网或IPTV
 */
function chgSheetType(e) {
	if("1"==e){
		$("select[@name='servList']").html("<OPTION value='INTERNET'>INTERNET</OPTION>");
	} else {
		$("select[@name='servList']").html("<OPTION value='OTHER'>OTHER</OPTION>");
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
	var deviceSn = $("input[@name='deviceSn']");
	var userAccount = $("input[@name='userAccount']");
	
	if(deviceSn.val().length<6&&deviceSn.val().length>0){
		alert("请输入至少最后六位设备序列号！");
		document.frm.deviceSn.focus();
		return false;
	}
	
	var url = "<s:url value='/gwms/config/businessSheetACT!search.action'/>";
	$.post(url,{
		deviceSn:deviceSn.val(),
		userAccount:userAccount.val()
		
	},function(ajax){
		if (ajax == "1") {
			$("tr[@id='resultTR']").show();
			$("td[@id='resultTD']").html("提示：");
			
			$("div[@id='result']").html("");
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">“用户账号”和“设备序列号”至少输入一个！</FONT>");
			
		} else if (ajax == "2") {
			$("tr[@id='resultTR']").show();
			$("td[@id='resultTD']").html("提示：");
			
			$("div[@id='result']").html("");
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">未查询到相关设备信息！</FONT>");
			
		} else if (ajax == "3") {
			$("tr[@id='resultTR']").show();
			$("td[@id='resultTD']").html("提示：");
			
			$("div[@id='result']").html("");
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">根据指定条件查出多于一台设备，请输入更详细的查询条件！</FONT>");
			
		} else {
			$("tr[@id='resultTR']").hide();
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
	var servList = $("select[@name='servList']");
	var connType = $("select[@name='connType']");
	var vpi = $("input[@name='vpi']");
	var vci = $("input[@name='vci']");
	var ipType = $("select[@name='ipType']");
	var vlanId = $("input[@name='vlanId']");
	var natEnable = $("input[@name='natEnable'][@checked]");
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
	
	if(!IsNumber(vpi.val(),"vpi")){
		vpi.focus();
		return false;
	}
	if(!IsNumber(vci.val(),"vci")){
		vci.focus();
		return false;
	}
	
	if(connType.val()=="IP_Routed"){
		if(!IsNull(username.val(),"PPPoE账号")){
			username.focus();
			return false;
		}
		if(!IsNull(password.val(),"PPPoE密码")){
			password.focus();
			return false;
		}
	}
	
	if(sessionType.val()=="2"){
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
    
   
	var url = "<s:url value='/gwms/config/businessSheetACT!add.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),

		sheetType:sheetType.val(),
		
		accessType:accessType.val(),
		
		sessionType:sessionType.val(),
		servList:servList.val(),
		
		connType:connType.val(),
		vpi:vpi.val(),
		vci:vci.val(),

		ipType:ipType.val(),
		vlanId:vlanId.val(),

		natEnable:natEnable.val(),

		username:username.val(),
		password:password.val(),

		ip:ip.val(),
		gateway:gateway.val(),
		mask:mask.val(),
		dns:dns.val(),

		bindPort:bindPort
		
	},function(ajax){
		
		$("tr[@id='resultTR1']").show();
		$("td[@id='resultTD1']").html("执行结果：");
		
		$("div[@id='result1']").html("通知后台");
		if (ajax == "true")
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
									<OPTION value="1">上网</OPTION>
									<OPTION value="2">IPTV</OPTION>
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
			
			<TR align="left" id="wanConnInfo" STYLE="display:none">
			<TD colspan="4"  bgcolor=#999999>
			
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
							<tr align="center">
								<td colspan="4" class="green_title" id="wanTitle"></td>
							</tr>
							
							<TR bgcolor="#FFFFFF" STYLE="display:">
								<TD width="15%" class=column align="right">上行方式:</TD>
									
								<TD width="35%">
									<SELECT name="accessType" class="bk" onclick="chgAccessType(this.value)">
										<OPTION value="ADSL">ADSL上行</OPTION>
										<OPTION value="LAN">LAN上行</OPTION>
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
										<OPTION value="PPPoE_Bridged">桥接</OPTION>
										<OPTION value="IP_Routed">路由</OPTION>
									</SELECT>
								</TD>
								
								<TD width="15%" class=column align="right">地址类型:</TD>
								<TD width="35%" id="ipTypeTd">
									<SELECT name="ipType" class="bk" onclick="chgIpType(this.value)">
										<OPTION value="Static">静态IP</OPTION>
										<OPTION value="DHCP">DHCP</OPTION>
										
									</SELECT>
								</TD>
								
							</TR>
							
							<TR bgcolor="#FFFFFF" STYLE="display:">
								
								<TD class=column align="right" nowrap width="15%">服务类型:</TD>
								<TD width="35%">
									<SELECT class="bk" name="servList" >
										<OPTION value='INTERNET'>INTERNET</OPTION>
									</SELECT>
								</TD>
								
								<TD class=column align="right" width="15%">NAT:</TD>
								<TD width="35%">
									<input type="radio" value="1" name="natEnable">启用
									<input type="radio" value="2" name="natEnable" checked>禁用
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
							
							<TR id="ppp_type_2" bgcolor="#FFFFFF" STYLE="display:none">
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
							
							<TR bgcolor="#FFFFFF">
								
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
















