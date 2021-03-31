<%--
����Ӧ��-Ӧ�÷���ģ��-�����������Ӳ���
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
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>

		<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';

parent.unblock();
/**
 * ��������ҳ����ʾ����
 */
function addWanHtml() {
	$("tr[@id='wanConnInfo']").show();
	if("none"==$("tr[@id='wanConnInfo']").css("display")){
		return;
	}
	var deviceId = $("input[@name='deviceId']");
 	var url = "<s:url value='/gwms/config/wanACT!addLanInit.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		type:"1"
	},function(ajax){
		$("td[@id='lanInter']").html(ajax);
		parent.dyniframesize();
	});

	$("td[@id='wanTitle']").html("������������");
	$("input[@name='subBtn']").attr({value:" �������� "});
	$("input[@name='subBtn']").unbind();
	$("input[@name='subBtn']").click(function(){addWan();});

	//TODO
	  var acctype = "<s:property value="accessType"/>";
	if ("DSL" == acctype || "ADSL" == acctype) {
		$("td[@id='accessType']").html("ADSL����");
		$("input[@name='accessTypeHid']").val("ADSL");
		$("tr[@id='trpvc1']").show();
		$("tr[@id='trpvc2']").hide();
		$("input[@name='vpi']").show();
		$("input[@name='vci']").show();

	} else if("Ethernet" == acctype){
		$("td[@id='accessType']").html("LAN����");
		$("input[@name='accessTypeHid']").val("LAN")
		$("tr[@id='trpvc1']").hide();
		$("tr[@id='trpvc2']").show();
		$("input[@name='vlanId']").show();
	} else {
		$("td[@id='accessType']").html("PON����");
		$("input[@name='accessTypeHid']").val("PON")
		$("tr[@id='trpvc1']").hide();
		$("tr[@id='trpvc2']").show();
		$("input[@name='vlanId']").show();
	}

	$("select[@name='ipType']").hide();
	$("tr[@id='resultTr']").hide();
	$("tr[@id='tr002']").hide();

	parent.dyniframesize();
}

/**
 * ����WAN����
 */
function addWan() {

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

	var bindPort = "";

	if("DSL"==accessType.val() || "ADSL"==accessType.val()){
		if(!IsNumber(vpi.val(),"vpi")){
			vpi.focus();
			return false;
		}
		if(!IsNumber(vci.val(),"vci")){
			vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(vlanId.val(),"vlanId")){
			vlanId.focus();
			return false;
		}
	}

	if(sessionType.val()=="2"){//IP
		if(ipType.val() == "Static"){
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
	}else{//PPP
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
	}
	var flag = "false";
		//alert($("input[@name='LAN']"));
	//var bindPortcheckLanArr = $("input[@name='LAN']");
	//var bindPortcheckWlanArr = $("input[@name='WLAN']");
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


	var url = "<s:url value='/gwms/config/wanACT!add.action'/>";
	$.post(url,{
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
		//num:totalNum.val(),

		username:username.val(),
		password:password.val(),

		ip:ip.val(),
		gateway:gateway.val(),
		mask:mask.val(),
		dns:dns.val(),
		gw_type:gw_type,
		bindPort:bindPort

	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("tr[@id='tr002']").show();
		$("div[@id='result']").html("");
		$("div[@id='result']").html("֪ͨ��̨:");
		parent.dyniframesize();
		var s = ajax.split("|");
		if (s[0] == "1")
		{
			$("div[@id='result']").append("<FONT COLOR=\"green\">�ɹ�</FONT>");
			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[1];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
		} else if (s[0] == "-3")
		{
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ʧ��" + s[1] + "</FONT>");
		} else {
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ʧ��</FONT>");
		}

		parent.dyniframesize();
	});
}



/**
 * �ѱ༭ҳ����ʾ����
 */
function editWanHtml() {
	$("td[@id='wanTitle']").html("�༭��������");
	$("input[@name='subBtn']").attr({value:" �༭���� "});
	$("input[@name='subBtn']").click(function(){editWan();});
	//$("tr[@id='wanConnInfo']").toggle();
	parent.dyniframesize();

}

/**
 * �༭WAN����
 */
function editWan() {
	alert("������...");
}

/**
 * ��ɾ��ҳ����ʾ����
 */
function delWanHtml(wanId, wanConnId, servList) {
	//$("tr[@id='wanConnInfo']").hide();

	//$("#id=PendingMessage").show();
	if (servList.indexOf("TR069") != -1 || servList.indexOf("BOTH") != -1 || servList.indexOf("Management") != -1) {
		alert("�������ǡ�����ͨ����������ɾ����");
	} else {

		if (confirm("ȷ����Ҫɾ����")) {
			delWan(wanId, wanConnId);
		}

	}

}

/**
 * ɾ��WAN����
 */
function delWan(wanId, wanConnId) {

	$("tr[@id='resultTr']").show();
	$("div[@id='result']").html("");
	$("div[@id='result']").html("���ڽ���ɾ�����������Ժ�...");

	var deviceId = $("input[@name='deviceId']");

	var url = "<s:url value='/gwms/config/wanACT!del.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		wanId:wanId,
		gw_type:gw_type,
		wanConnId:wanConnId

	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("tr[@id='tr002']").show();
		$("div[@id='result']").html("");
		if (ajax == "true")
		{
			//$("div[@id='result']").append("ɾ���ɹ�");
			alert("ɾ���ɹ�");
		} else {
			//$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">ɾ��ʧ��</FONT>");
			alert("ɾ��ʧ��");
		}
		$("tr[@id='resultTr']").hide();
		parent.dyniframesize();
		parent.Using(0);
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

	var sessionType = $("select[@name='sessionType']").val();
	if ("2" == sessionType) {
		return;
	}

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
		$("select[@name='connType']").html("<OPTION value='PPPoE_Bridged'>�Ž�</OPTION><OPTION value='IP_Routed'>·��</OPTION>");
		$("select[@name='ipType']").hide();

	} else if ("2" == e && "Static" == ipType) {

		$("tr[@id='ip_type_2']").show();
		$("tr[@id='ip_type_3']").show();

		$("select[@name='ipType']").show();

		$("select[@name='connType']").show();
		$("select[@name='connType']").html("<OPTION value='IP_Routed'>·��</OPTION>");
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

function reInter(){
	$("td[@id='lanInter']").html("���ڻ�ȡ�˿�...");
	var deviceId = $("input[@name='deviceId']");

	var url = "<s:url value='/gwms/config/wanACT!addLanInit.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		type:"0"
	},function(ajax){
		$("td[@id='lanInter']").html(ajax);
		parent.dyniframesize();
	});
}

</script>


	</head>

	<body>



		<FORM NAME="frm" METHOD="post" action="">
			<input type="hidden" name="deviceId"
				value="<s:property value="deviceId"/>" />
			<input type="hidden" name="accessTypeHid" value="">
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
				<tr>
					<TD>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
							<tr align="center" height="25">
								<td colspan="3" class="green_title">
									�������
								</td>
							</tr>
							<tr align="center" height="25">
								<td colspan="3" align="right" class="column">
									<!--
						<IMG id='img1' SRC='../../images/add.gif' title='����' style='cursor: pointer; cursor: hand;'
						  name='addImg' onclick="addWanHtml()" onmouseover="this.src='../../images/add2.gif'"
						  onmouseout="this.src='../../images/add.gif'"
						/>
						 -->
									<%
									if(!"sx_lt".equals(InstArea)){
									%>
									<input type="button" name='addImg' onclick="addWanHtml()" value="����"/>
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
												PPPoE�˺�
											</TD>

											<TD class=column5 align="center">
												IP��ַ
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
												DNS
											</TD>
											<TD class=column5 align="center">
												NAT
											</TD>
											<TD class=column5 align="center">
												���Ӵ���
											</TD>
											<!-- <TD class=column5 align="center">���ն�������</TD> -->
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
														<s:property value="connType" />
													</TD>
												</s:else>

												<s:if
													test="#wanConfigList.accessType=='ADSL' || #wanConfigList.accessType=='DSL'">
													<TD align="center" id="pvc">
														PVC:
														<s:property value="vpi" />
														/
														<s:property value="vci" />
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														VLANID:
														<s:property value="vlanId" />
													</TD>
												</s:else>

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
													test="#wanConfigList.bindPort=='N/A' || #wanConfigList.bindPort=='null' || #wanConfigList.bindPort==''">
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
													test="#wanConfigList.servList=='N/A' || #wanConfigList.servList=='null'">
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
													test="#wanConfigList.dns=='N/A' || #wanConfigList.dns=='null' || #wanConfigList.dns==''">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<TD align="center">
														<s:property value="dns" />
													</TD>
												</s:else>

												<s:if
													test="#wanConfigList.natEnable=='N/A' || #wanConfigList.natEnable=='null' ">
													<TD align="center">
														-
													</TD>
												</s:if>
												<s:else>
													<s:if test="#wanConfigList.natEnable==1">
														<TD align="center">
															����
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															δ����
														</TD>
													</s:else>

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

												<!--
							        <s:if test="#wanConfigList.num=='N/A' || #wanConfigList.num=='null' ">

									<TD align="center">-</TD>
									</s:if>
									 <s:else>
							            <TD align="center"><s:property value="num"/></TD>
							        </s:else>
									-->
												<TD class=column5 align="center">

													<!--
										<IMG id='img1' SRC='../images/action_add.gif' title='����' style='cursor: pointer; cursor: hand;'
						  					name='addImg2' onclick="addWanHtml()"
							  			/>

							  			<IMG id='img2' SRC='../../images/edit.gif' title='�༭' style='cursor: pointer; cursor: hand;'
							  			onclick="editWanHtml('<s:property value="wanId"/>',
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
										onmouseover="this.src='../../images/edit2.gif'"  onmouseout="this.src='../../images/edit.gif'"
							  			/>
										-->
												<s:if test="#wanConfigList.servList=='TR069'">

												</s:if>
												<s:else>
													<IMG id='img3' SRC='../../images/del.gif' title='ɾ��'
														 style='cursor: pointer; cursor: hand;'
														 onclick="delWanHtml('<s:property value="wanId"/>',
																 '<s:property value="wanConnId"/>',
																 '<s:property value="servList"/>')"
														 onmouseover="this.src='../../images/del2.gif'"
														 onmouseout="this.src='../../images/del.gif'" />
												</s:else>
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
					<td colspan="1" width="15" class=column>
					</td>
				</tr>
				<tr align="left" id="wanConnInfo" STYLE="display: none">
					<td colspan="4" bgcolor=#999999>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<tr align="center">
								<td colspan="4" width="100%" class="green_title" id="wanTitle"></td>
							</tr>

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

							<TR bgcolor="#FFFFFF" STYLE="display: ">

								<TD class=column align="right" nowrap width="15%">
									��������:
								</TD>
								<TD width="35%">
									<SELECT class="bk" name="servList">
										<OPTION value="INTERNET">
											INTERNET
										</OPTION>
									</SELECT>
								</TD>

								<TD class=column align="right" width="15%">
								<img src="<s:url value="/images/refresh.png" />" border="0" alt="ˢ��" onclick="reInter()">
									�󶨶˿�:
								</TD>
								<TD width="35%" id="lanInter">
									���ڻ�ȡ�˿�...
								</TD>

								<input type="hidden" name="natEnable" value="1" />
								<!--
								<TD class=column align="right" width="15%">NAT:</TD>
								<TD width="35%">
									<input type="radio" value="1" name="natEnable">����
									<input type="radio" value="2" name="natEnable" checked>����
								</TD>
								 -->
							</TR>


							<!--
							<TR id="ip_type_1" bgcolor="#FFFFFF" STYLE="display:none">
								<TD width="15%" class=column align="right">��ַ����:</TD>
								<TD width="35%" >
									<SELECT name="ipType" class="bk" onclick="chgIpType(this.value)">
										<OPTION value="Static">��̬IP</OPTION>
										<OPTION value="DHCP">DHCP</OPTION>

									</SELECT>
								</TD>


								<TD width="15%" class=column align="right">VLAN:</TD>
								<td width="35%">
									<input type="text" name="vlanId" maxlength=20 size=20 class="bk"/>
								</td>

							</TR>
							 -->

							<TR bgcolor="#FFFFFF" id="trpvc1">
								<TD class=column align="right" nowrap width="15%">
									PVC:
								</TD>
								<TD width="35%">
									<input type="text" name="vpi" maxlength=3 size=3 class="bk" />
									/
									<input type="text" name="vci" maxlength=3 size=3 class="bk" />
								</TD>

								<TD width="15%" class=column align="right"></TD>
								<TD width="35%">
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF" id="trpvc2">
								<TD width="15%" class=column align="right">
									VLANID:
								</TD>
								<TD width="35%">
									<input type="text" name="vlanId" maxlength=20 size=20
										class="bk" />
								</TD>

								<TD class=column align="right" nowrap width="15%"></TD>
								<TD width="35%">
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
									<INPUT TYPE="text" NAME="dns" maxlength=30 size=30 class="bk">
								</TD>

							</TR>

							<TR bgcolor="#FFFFFF">
								<TD colspan="4" class=column align="right" nowrap>
									<input type="button" name="subBtn" value="" />
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














