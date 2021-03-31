<%--
����Ӧ��-Ӧ�÷���ģ��-����IPTV
Author: Gongsj
Version: 1.0.0
Date: 2009-07-14
--%>

<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>Ӧ�÷���</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

		<SCRIPT LANGUAGE="JavaScript">

		var gw_type = '<s:property value="gw_type"/>';


parent.unblock();

/**
 * ��������ҳ����ʾ����
 */
function addLanVoipHtml() {

	if("none"==$("tr[@id='wanConnInfo']").css("display")){
		$("tr[@id='wanConnInfo']").css("display","block");
		$("tr[@id='voipInfo']").css("display","block");
		$("tr[@id='title']").css("display","block");
	}else{
		$("tr[@id='wanConnInfo']").css("display","none");
		$("tr[@id='voipInfo']").css("display","none");
		$("tr[@id='title']").css("display","none");
		parent.dyniframesize();
		return;
	}
	var deviceId = $("input[@name='deviceId']");
	$("tr[@id='tr002']").hide();
	//var url = "<s:url value='/gwms/config/voipACT!addLanInit.action'/>";
	//$.post(url,{
	//	deviceId:deviceId.val()
	//},function(ajax){
	//	$("div[@id='lanInter']").html(ajax);
	//	parent.dyniframesize();
	//});

	$("td[@id='wanTitle']").html("����VOIP(ͬʱ����WAN���������Ϣ)");
	$("button[@name='subBtn']").attr({value:" �������� "});
	$("button[@name='subBtn']").unbind();
	$("button[@name='subBtn']").click(function(){addLanVoip();});

	//TODO
	if ("DSL" == "<s:property value="accessType"/>") {
		$("td[@id='accessType']").html("ADSL����");
		$("input[@name='accessTypeHid']").val("ADSL")
		$("input[@name='vpi']").show();
		$("input[@name='vci']").show();
		$("input[@name='vlanId']").hide();

	} else if("Ethernet" == "<s:property value="accessType"/>"){
		$("td[@id='accessType']").html("LAN����");
		$("input[@name='accessTypeHid']").val("LAN")
		$("tr[@id='PVC']").hide();
		$("input[@name='vlanId']").show();
	} else {
		$("td[@id='accessType']").html("PON����");
		$("input[@name='accessTypeHid']").val("PON")
		$("tr[@id='PVC']").hide();
		$("input[@name='vlanId']").show();
	}

	$("select[@name='ipType']").hide();
	$("tr[@id='resultTr']").hide();

	//$("tr[@id='wanConnInfo']").toggle();

	parent.dyniframesize();

	if(!checkForm()) {
		alert("�������д�");
		return;
	}
}

function addVoipHtml() {

	if("none"==$("tr[@id='wanConnInfo']").css("display")){
		if("none"==$("tr[@id='voipInfo']").css("display")){
			$("tr[@id='wanConnInfo']").css("display","none");
			$("tr[@id='voipInfo']").css("display","block");
		$("tr[@id='title']").css("display","block");
		}else{
			$("tr[@id='wanConnInfo']").css("display","none");
			$("tr[@id='voipInfo']").css("display","none");
			$("tr[@id='title']").css("display","none");
			return;
		}
	}else{
		$("tr[@id='wanConnInfo']").css("display","none");
		$("tr[@id='voipInfo']").css("display","block");
			$("tr[@id='title']").css("display","block");
	}
	$("tr[@id='tr002']").hide();
	$("td[@id='wanTitle']").html("����VOIP");
	$("button[@name='subBtn']").attr({value:" �������� "});
	$("button[@name='subBtn']").unbind();
	$("button[@name='subBtn']").click(function(){addVoip();});

	parent.dyniframesize();
}

function addVoip() {

	var deviceId = $("input[@name='deviceId']");
	var accessType = $("input[@name='accessTypeHid']");
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

	var proxyServer = $("input[@name='proxyServer']");
	var proxyPort = $("input[@name='proxyPort']");
	var proxyServer2 = $("input[@name='proxyServer2']");
	var proxyPort2 = $("input[@name='proxyPort2']");

	var regiServ = $("input[@name='regi_serv']");
	var regiPort = $("input[@name='regi_port']");
	var standRegiServ = $("input[@name='stand_regi_serv']");
	var standRegiPort = $("input[@name='stand_regi_port']");
	var outBoundProxy = $("input[@name='out_bound_proxy']");
	var outBoundPort = $("input[@name='out_bound_port']");
	var standOutBoundProxy = $("input[@name='stand_out_bound_proxy']");
	var standOutBoundPort = $("input[@name='stand_out_bound_port']");
	var authUserName = $("input[@name='authUserName']");
	var authPassword = $("input[@name='authPassword']");
	var enable = $("select[@name='enable']");
	var bindPort = "";

	var url = "<s:url value='/gwms/config/voipACT!add.action'/>";
	$.post(url,{

		isAddWan:0,
		deviceId:deviceId.val(),
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
		bindPort:bindPort,
		proxServ:proxyServer.val(),
		proxPort:proxyPort.val(),
		proxServ2:proxyServer2.val(),
		proxPort2:proxyPort2.val(),
		regiServ:regiServ.val(),
		regiPort:regiPort.val(),
		standRegiServ:standRegiServ.val(),
		standRegiPort:standRegiPort.val(),
		outBoundProxy:outBoundProxy.val(),
		outBoundPort:outBoundPort.val(),
		standOutBoundProxy:standOutBoundProxy.val(),
		standOutBoundPort:standOutBoundPort.val(),
		authUserName:authUserName.val(),
		authPassword:authPassword.val(),
		enable:enable.val()

	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("tr[@id='tr002']").show();
		$("div[@id='result']").html("");
		$("div[@id='result']").html("֪ͨ��̨:");
		var s = ajax.split(";");
		if (s[0] == "true")
		{
			$("div[@id='result']").append("�ɹ�");
			var strategyurl = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[1];
			$.post(strategyurl,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
            parent.dyniframesize();
		} else {
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ʧ��</FONT>");
		}

		parent.dyniframesize();
	});
}

/**
 * ����VOIP����
 */
function addLanVoip() {

	var deviceId = $("input[@name='deviceId']");
	var accessType = $("input[@name='accessTypeHid']");
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


	var authUserName = $("input[@name='authUserName']");
	var authPassword = $("input[@name='authPassword']");
	var enable = $("select[@name='enable']");

	var bindPort = "";

	if(!IsNumber(vpi.val(),"vpi")){
		vpi.focus();
		return false;
	}
	if(!IsNumber(vci.val(),"vci")){
		vci.focus();
		return false;
	}

	if(connType.val()=="IP_Routed"){
		if(!IsNull(username.val(),"PPPoE�˺�")){
			username.focus();
			return false;
		}
		if(!IsNull(password.val(),"PPPoE����")){
			password.focus();
			return false;
		}
	}

	if(sessionType.val()=="2"){
		if(!IsIPAddr2(ip.val(),"IP��ַ")){
			ip.focus();
			return false;
		}
		if(!IsIPAddr2(gateway.val(),"����")){
			gateway.focus();
			return false;
		}
		if(!IsIPAddr2(mask.val(),"��������")){
			mask.focus();
			return false;
		}
		if(!IsIPAddr2(dns.val(),"DNS")){
			dns.focus();
			return false;
		}
	}

	var flag = "false";
	$("[name='LAN'][checked]").each(function(){
		if("true"==flag){
			bindPort+=",";
		}
		bindPort+=$(this).val();
		flag="true";
	})
	$("[name='WLAN'][checked]").each(function(){
		if("true"==flag){
			bindPort+=",";
		}
		bindPort+=$(this).val();
		flag="true";
	})


	var url = "<s:url value='/gwms/config/voipACT!add.action'/>";
	$.post(url,{

		isAddWan:1,
		deviceId:deviceId.val(),
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
		bindPort:bindPort,

		authUserName:authUserName.val(),
		authPassword:authPassword.val(),
		enable:enable.val()

	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("tr[@id='tr002']").show();
		$("div[@id='result']").html("");
		$("div[@id='result']").html("֪ͨ��̨:");
		var s = ajax.split(";");
		if (s[0] == "true")
		{
			$("div[@id='result']").append("�ɹ�");
			var strategyurl = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[1];
			$.post(strategyurl,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
            parent.dyniframesize();
		} else {
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ʧ��</FONT>");
		}

		parent.dyniframesize();
	});
}

/**
 * �ѱ༭ҳ����ʾ����
 */
function editVoipHtml() {
	$("td[@id='wanTitle']").html("�༭VOIP");
	$("button[@name='subBtn']").attr({value:" �༭���� "});
	$("button[@name='subBtn']").click(function(){editVoip();});
	//$("tr[@id='wanConnInfo']").toggle();
	parent.dyniframesize();

}

/**
 * �༭WAN����
 */
function editVoip() {
	alert("������...");
}


/**
 * ��ɾ��ҳ����ʾ����
 */
function delVoipHtml(wanId, wanConnId) {
	//$("tr[@id='wanConnInfo']").hide();

	if (confirm("ȷ����Ҫɾ����")) {
		delVoip(wanId, wanConnId);
	}

}

/**
 * ɾ��WAN����
 */
function delVoip(wanId, wanConnId) {

	var deviceId = $("input[@name='deviceId']");

	var url = "<s:url value='/gwms/config/voipACT!del.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		wanId:wanId,
		gw_type:gw_type,
		wanConnId:wanConnId

	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("div[@id='result']").html("");
		if (ajax == "true")
		{
			//$("div[@id='result']").append("ɾ���ɹ�");
			alert("ɾ���ɹ�");
		} else {
			//$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ɾ��ʧ��</FONT>");
			alert("ɾ��ʧ��");
		}

		parent.dyniframesize();
		parent.Using(2);
	});
}

/**
 * ɾ��VoIP����
 */
function delVoipInfo(profId) {

	var deviceId = $("input[@name='deviceId']");

	var url = "<s:url value='/gwms/config/voipACT!delVoIP.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		profId:profId

	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("div[@id='result']").html("");
		if (ajax == "true")
		{
			//$("div[@id='result']").append("ɾ���ɹ�");
			alert("ɾ���ɹ�");
		} else {
			//$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ɾ��ʧ��</FONT>");
			alert("ɾ��ʧ��");
		}

		parent.dyniframesize();
		parent.Using(2);
	});
}



/**
 * �������ݺϷ���
 */
function checkForm() {
	return true;
}

/**
 * IP_Routed��PPPoE_Bridged
 */
function chgConnType(e){
	if("IP_Routed"==e){
		$("tr[@id='ppp_type_2']").show();
    } else if("PPPoE_Bridged"==e){
    	$("tr[@id='ppp_type_2']").hide();
    }

	parent.dyniframesize();
}

/**
 * PPP��IP
 */
function chgSessionType(e) {
	var ipType = $("select[@name='ipType']").val();
	var connType = $("select[@name='connType']").val();

	if ("1" == e) {
		//PPP��ʽ
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

	parent.dyniframesize();
}

/**
 * DHCP��Static
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

	parent.dyniframesize();
}

function chgAccessType(e) {

}

$(function(){
	parent.dyniframesize();
	});
</script>

	</head>

	<body>
		<FORM NAME="frm" METHOD="post" action="">
			<input type="hidden" name="deviceId"
				value="<s:property value="deviceId"/>" />
			<input type="hidden" name="accessTypeHid" value="">
			<input type="hidden" name="servList" value="OTHER">


			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
				<tr>
					<TD>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
							<tr align="center" height="25">
								<td colspan="3" class="green_title">
									VoIP������Ϣ
								</td>
							</tr>
							<tr align="center" height="25">
								<td colspan="3" align="right" class="column">
									<!-- <IMG id='img1' SRC='../../images/add.gif' title='����' style='cursor: pointer; cursor: hand;'
						  name='addImg' onclick="addLanVoipHtml()" onmouseover="this.src='../../images/add2.gif'"
							  onmouseout="this.src='../../images/add.gif'"
						/>
						 -->
						 			<%
									if(!"sx_lt".equals(InstArea)){
									%>
									<button name='addImg' onclick="addLanVoipHtml()">
										����
									</button>
									<%}%>
								</td>
							</tr>

							<tr align="left" id="trnet" STYLE="display: ">
								<td colspan="3" bgcolor=#999999>
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
										<tr align="center" bgcolor="#FFFFFF">
											<TD class=column5 align="center">
												��������
											</TD>

											<TD class=column5 align="center">
												PVC/VLANID
											</TD>
											<TD class=column5 align="center">
												IP��ַ/PPPoE�˺�
											</TD>

											<TD class=column5 align="center">
												����״̬
											</TD>
											<TD class=column5 align="center">
												�󶨶˿�
											</TD>
											<TD class=column5 align="center">
												��������
											</TD>
											<TD class=column5 align="center">
												���Ӵ���
											</TD>
											<TD class=column5 align="center">
												����
											</TD>
										</tr>
										<s:iterator var="wanConfigList" value="wanConfigList">
											<tr align="center" bgcolor="#FFFFFF">

												<s:if
													test="#wanConfigList.connType=='N/A' || #wanConfigList.connType=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="connStatus" />
													</TD>
												</s:else>

												<s:if
													test="#wanConfigList.vlanId=='N/A' || #wanConfigList.vlanId=='null' ">
													<TD align="center" id="pvc">
														PVC:
														<s:property value="vpi" />
														/
														<s:property value="vci" />
													</TD>

													<s:if
														test="#wanConfigList.username=='N/A' || #wanConfigList.username=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="username" />
														</TD>
													</s:else>

												</s:if>
												<s:else>
													<TD align="center">
														VLANID:
														<s:property value="vlanId" />
													</TD>

													<s:if
														test="#wanConfigList.ip=='N/A' || #wanConfigList.ip=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="ip" />
														</TD>
													</s:else>

												</s:else>

												<s:if
													test="#wanConfigList.connStatus=='N/A' || #wanConfigList.connStatus=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="connStatus" />
													</TD>
												</s:else>

												<s:if
													test="#wanConfigList.bindPort=='N/A' || #wanConfigList.bindPort=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="bindPort" />
													</TD>
												</s:else>

												<s:if
													test="#wanConfigList.servList=='N/A' || #wanConfigList.servList=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="servList" />
													</TD>
												</s:else>

												<s:if
													test="#wanConfigList.connError=='N/A' || #wanConfigList.connError=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="connError" />
													</TD>
												</s:else>

												<TD class=column5 align="center">

													<!--
										<IMG id='img1' SRC='../images/action_add.gif' title='����' style='cursor: pointer; cursor: hand;'
						  					name='addImg2' onclick="addVoipHtml()"
							  			/>

							  			<IMG id='img1' SRC='../../images/edit.gif' title='�༭' style='cursor: pointer; cursor: hand;'
							  			onclick="editVoipHtml('<s:property value="wanId"/>',
														 '<s:property value="wanConnId"/>',
														 '<s:property value="wanConnSessId"/>',
														 '<s:property value="vpi"/>',
														 '<s:property value="vci"/>',
														 '<s:property value="vlanId"/>',
														 '<s:property value="connType"/>',
														 '<s:property value="bindPort"/>',
														 '<s:property value="servList"/>',
														 '<s:property value="connStatus"/>',
														 '<s:property value="ip"/>',
														 '<s:property value="dns"/>',
														 '<s:property value="username"/>',
														 '<s:property value="natEnable"/>',
														 '<s:property value="connError"/>',
														 '<s:property value="num"/>')"
										onmouseover="this.src='../../images/edit2.gif'"  onmouseout="this.src='../../images/edit.gif'"/>
										-->

													<IMG id='img1' SRC='../../images/del.gif' title='ɾ��'
														style='cursor: pointer; cursor: hand;'
														onclick="delVoipHtml('<s:property value="wanId"/>',
														 '<s:property value="wanConnId"/>')"
														onmouseover="this.src='../../images/del2.gif'"
														onmouseout="this.src='../../images/del.gif'" />

												</td>
											</tr>
										</s:iterator>

										<tr align="center" bgcolor="#FFFFFF">
											<TD colspan="11" align="center">
												<s:property value="corbaMsg" />
											</TD>
										</tr>

									</TABLE>
								</td>
							</tr>

						</table>
					</TD>
				</tr>

				<tr height="20">
					<td colspan="1" width="15" class=column></td>
				</tr>
				<!-- չʾVoIP�����Ϣ -->
				<tr id="trvoip" STYLE="display: ">
					<TD>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
							<tr align="center" height="25">
								<td colspan="3" class="green_title">
									VoIP��ϸ��Ϣ
								</td>
							</tr>
							<tr align="center" height="25">
								<td colspan="3" align="right" class="column">
									<!-- <IMG id='img1' SRC='../../images/add.gif' title='����' style='cursor: pointer; cursor: hand;'
						  name='addImg' onclick="addVoipHtml()" onmouseover="this.src='../../images/add2.gif'"
							  onmouseout="this.src='../../images/add.gif'"
						/>
						 -->
						 			<%
									if(!"sx_lt".equals(InstArea)){
									%>
									<button name='addImg' onclick="addVoipHtml()">
										����
									</button>
									<%}%>
								</td>
							</tr>

							<tr align="left">
								<td colspan="3" bgcolor=#999999>
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
										<tr align="center" bgcolor="#FFFFFF"  height="25">
											<TD class=column5 align="center">
												�����˿�
											</TD>
											<TD class=column5 align="center">
												Э������
											</TD>
											<TD class=column5 align="center">
												��������ַ
											</TD>
											<TD class=column5 align="center">
												�������˿�
											</TD>
											<TD class=column5 align="center">
												���õ�ַ
											</TD>
											<TD class=column5 align="center">
												���ö˿�
											</TD>
											<TD class=column5 align="center">
												����
											</TD>
											<TD class=column5 align="center">
												��·״̬
											</TD>
											<TD class=column5 align="center">
												ע���ʺ�
											</TD>
											<TD class=column5 align="center">
												����
											</TD>
											<TD class=column5 align="center">
												����
											</TD>
										</tr>
										<s:iterator value="voipInfoList" var="voipInfo">
										<s:if test="voipProtocalTypeStr=='H.248'">
											<tr align="center" bgcolor="#FFFFFF" height="25">
												<s:if
													test="#voipInfo.line=='N/A' || #voipInfo.line=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="line" />
													</TD>
												</s:else>
												<TD align="center">
													<s:property value="voipProtocalTypeStr" />
												</TD>
												<s:if
													test="#voipInfo.media_gateway_controler=='N/A' || #voipInfo.media_gateway_controler=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="media_gateway_controler" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.media_gateway_controler_port=='N/A' || #voipInfo.media_gateway_controler_port=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="media_gateway_controler_port" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.media_gateway_controler_2=='N/A' || #voipInfo.media_gateway_controler_2=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="media_gateway_controler_2" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.media_gateway_controler_port_2=='N/A' || #voipInfo.media_gateway_controler_port_2=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="media_gateway_controler_port_2" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.enable=='N/A' || #voipInfo.enable=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="enable" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.status=='N/A' || #voipInfo.status=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="status" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.username=='N/A' || #voipInfo.username=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="username" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.password=='N/A' || #voipInfo.password=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="password" />
													</TD>
												</s:else>
												<TD class=column5 align="center">
													<IMG id='img1' SRC='../../images/del.gif' title='ɾ��'
														style='cursor: pointer; cursor: hand;'
														onclick="delVoipInfo('<s:property value="prof_id"/>')"
														onmouseover="this.src='../../images/del2.gif'"
														onmouseout="this.src='../../images/del.gif'" />
												</TD>
											</tr>
										</s:if>
										<s:else>
											<tr align="center" bgcolor="#FFFFFF" height="25">
												<s:if
													test="#voipInfo.line=='N/A' || #voipInfo.line=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="line" />
													</TD>
												</s:else>
												<TD align="center">
													<s:property value="voipProtocalTypeStr" />
												</TD>
												<s:if
													test="#voipInfo.prox_serv=='N/A' || #voipInfo.prox_serv=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="prox_serv" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.prox_port=='N/A' || #voipInfo.prox_port=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="prox_port" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.prox_serv_2=='N/A' || #voipInfo.prox_serv_2=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="prox_serv_2" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.prox_port_2=='N/A' || #voipInfo.prox_port_2=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="prox_port_2" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.enable=='N/A' || #voipInfo.enable=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="enable" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.status=='N/A' || #voipInfo.status=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="status" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.username=='N/A' || #voipInfo.username=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="username" />
													</TD>
												</s:else>
												<s:if
													test="#voipInfo.password=='N/A' || #voipInfo.password=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="password" />
													</TD>
												</s:else>
												<TD class=column5 align="center">
													<IMG id='img1' SRC='../../images/del.gif' title='ɾ��'
														style='cursor: pointer; cursor: hand;'
														onclick="delVoipInfo('<s:property value="prof_id"/>')"
														onmouseover="this.src='../../images/del2.gif'"
														onmouseout="this.src='../../images/del.gif'" />
												</TD>
											</tr>
										</s:else>
										</s:iterator>

										<tr align="center" bgcolor="#FFFFFF">
											<TD colspan="11" align="center">
												<s:property value="voipInfoMsg" />
											</TD>
										</tr>

									</TABLE>
								</td>
							</tr>

						</table>
					</TD>
				</tr>

				<tr height="20">
					<td colspan="1" width="15" class=column></td>
				</tr>
				<tr align="center" id="title">
					<td colspan="4" class="green_title" id="wanTitle"></td>
				</tr>
				<tr align="left" id="wanConnInfo" STYLE="display: none">
					<td colspan="4" bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR bgcolor="#FFFFFF" STYLE="display: ">
								<TD width="15%" class=column align="right">
									���з�ʽ:
								</TD>
								<TD width="35%" id="accessType">
								</TD>

								<TD width="15%" class=column align="right">
									���ӷ�ʽ:
								</TD>
								<TD width="35%">
									<SELECT name="sessionType" class="bk"
										onclick="chgSessionType(this.value)">
										<OPTION value="1">
											PPP����
										</OPTION>
										<OPTION value="2">
											IP��ʽ
										</OPTION>
									</SELECT>
								</td>

							</TR>

							<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: ">

								<TD class=column align="right" nowrap width="15%">
									��������:
								</TD>
								<TD width="35%" id="connTypeTd">

									<SELECT name="connType" class="bk"
										onclick="chgConnType(this.value)">
										<OPTION value="PPPoE_Bridged">
											�Ž�
										</OPTION>
										<OPTION value="IP_Routed">
											·��
										</OPTION>
									</SELECT>
								</TD>

								<TD width="15%" class=column align="right">
									��ַ����:
								</TD>
								<TD width="35%" id="ipTypeTd">
									<SELECT name="ipType" class="bk"
										onclick="chgIpType(this.value)">
										<OPTION value="Static">
											��̬IP
										</OPTION>
										<OPTION value="DHCP">
											DHCP
										</OPTION>

									</SELECT>
								</TD>

							</TR>

							<TR id="PVC" bgcolor="#FFFFFF">
								<TD class=column align="right" nowrap width="15%">
									PVC:
								</TD>
								<TD width="35%">
									<input type="text" name="vpi" maxlength=3 size=3 class="bk" />
									/
									<input type="text" name="vci" maxlength=3 size=3 class="bk" />
								</TD>

								<TD width="15%" class=column align="right">
									VLANID:
								</TD>
								<TD width="35%">
									<input type="text" name="vlanId" maxlength=20 size=20
										class="bk" />
								</TD>
							</TR>

							<TR id="ppp_type_2" bgcolor="#FFFFFF" STYLE="display: none">
								<TD width="15%" class=column align="right">
									PPPoE�˺�:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="username" maxlength=20 class="bk"
										size=20>
								</TD>
								<TD width="15%" class=column align="right">
									PPPoE����:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="password" maxlength=20 class="bk"
										size=20>
								</TD>
							</TR>

							<TR id="ip_type_2" bgcolor="#FFFFFF" STYLE="display: none">
								<TD class=column align="right" nowrap width="15%">
									IP��ַ:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="ip" maxlength=20 size=20 class="bk">
								</TD>
								<TD width="15%" class=column align="right">
									����:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="gateway" maxlength=20 size=20
										class="bk">
								</TD>

							</TR>
							<TR id="ip_type_3" bgcolor="#FFFFFF" STYLE="display: none">
								<TD width="15%" class=column align="right">
									��������:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="mask" maxlength=20 size=20 class="bk">
								</TD>
								<TD class=column align="right" width="15%">
									DNS:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="dns" maxlength=40 size=40 class="bk">
								</TD>

							</TR>

							<!--
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="15%">�󶨶˿�:</TD>
						<TD width="35%" colspan="3">
							<div id="lanInter">���ڻ�ȡ�˿�...</div>
						</TD>
					</TR>
				 -->
						</TABLE>
					</td>
				</tr>

				<tr height="20">
					<td colspan="1" width="15" class=column></td>
				</tr>


				<tr align="left" id="voipInfo" STYLE="display: none">
					<td colspan="4" bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

							<TR bgcolor="#FFFFFF" STYLE="display: ">
								<TD width="15%" class=column align="right">
									VOIP��·��֤�û���:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="authUserName" maxlength=20 size=20
										class="bk">
								</TD>

								<TD width="15%" class=column align="right">
									VOIP��·��֤����:
								</TD>
								<TD width="35%">
									<INPUT TYPE="text" NAME="authPassword" maxlength=20 size=20
										class="bk">
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF" STYLE="display: ">
								<TD width="15%" class=column align="right">
									����:
								</TD>
								<TD width="35%">
									<SELECT name="enable" class="bk">
										<OPTION value="Enabled">
											����
										</OPTION>
										<OPTION value="Disabled">
											����
										</OPTION>
										<OPTION value="Quiescent">
											����
										</OPTION>
									</SELECT>
								</TD>

								<TD width="15%" class=column align="right"></TD>
								<TD width="35%">

								</TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<TD colspan="4" class=column align="right" nowrap>
									<button type="button" name="subBtn" value="" />
								</TD>
							</TR>

							<TR id="resultTr" bgcolor="#FFFFFF" style="display: none">
								<TD class=column align="right" width="15%">
									ִ�н��:
								</TD>
								<TD colspan="3">
									<DIV id="result"></DIV>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
								<td colspan="4" valign="top" class=column>
									<div id="div_strategy"
										style="width: 100%; z-index: 1; top: 100px">
									</div>
								</td>
							</TR>
						</TABLE>
					</td>
				</tr>
			</TABLE>
		</FORM>
	</body>
</html>
