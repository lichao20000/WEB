<%--
ҵ���ֹ�����
Author: Jason
Version: 1.0.0
Date: 2009-09-22
--%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<HEAD>
<title>ҵ���ֹ�����</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
<!--//
var hasCust = '<s:property value="hasCust"/>';
var hasServUser = '<s:property value="hasServUser"/>';
if('-1' == hasCust){
	//alert("δ��ѯ���û�,����Ĳ�ѯ�������²�ѯ");
	$("div[@id='div_sheetInfo']").html("<table width='100%'><tr><td bgcolor='#FFFFFF'>"
			+ "<font color='red'>δ��ѯ���û�,����Ĳ�ѯ�������²�ѯ</font></td></tr></table>");
}else if('-2' == hasCust){
	//alert("��ѯ����û�,���������ϸ�Ĳ�ѯ����");
	$("div[@id='div_sheetInfo']").html("<table width='100%'><tr><td bgcolor='#FFFFFF'>"
			+ "<font color='red'>��ѯ����û�,���������ϸ�Ĳ�ѯ����</font></td></tr></table>");
}

//��ʼ��ҳ����ʾ
function initShow(){
	var servTypeId = '<s:property value="servTypeId"/>';
	var userId = '<s:property value="userId"/>';
	var deviceId = '<s:property value="deviceId"/>';
	var accessType = '<s:property value="accessType"/>';
	var vlanid = '<s:property value="vlanid"/>';
	var vpi = '<s:property value="vpi"/>';
	var vci = '<s:property value="vci"/>';
	var netType = '<s:property value="netType"/>';
	var pppoeUsername = '<s:property value="pppoeUsername"/>';
	var pppoePasswd = '<s:property value="pppoePasswd"/>';
	var ip = '<s:property value="ip"/>';
	var gateway = '<s:property value="gateway"/>';
	var mask = '<s:property value="mask"/>';
	var dns = '<s:property value="dns"/>';
	var voipUsername = '<s:property value="voipUsername"/>';
	var voipPasswd = '<s:property value="voipPasswd"/>';
	
	var cityId = '<s:property value="cityId"/>';
	var officeId = '<s:property value="officeId"/>'
	
	var oui = '<s:property value="oui"/>';
	var devSn = '<s:property value="devSn"/>';

	$("input[@name='servTypeId']").val(servTypeId);
	$("input[@name='userId']").val(userId);
	$("input[@name='deviceId']").val(deviceId);
	$("input[@name='cityId']").val(cityId);
	$("input[@name='officeId']").val(officeId);
	$("input[@name='hasServUser']").val(hasServUser);
	$("input[@name='oui']").val(oui);
	$("input[@name='devSn']").val(devSn);
	
	if('14' == servTypeId){
		//voip
		$("tr[@id='id_bindport']").hide();
		$("tr[@id='id_voip']").show();
	}else{
		//IPTV������
		$("tr[@id='id_bindport']").show();
		$("tr[@id='id_voip']").hide();
	}
	
	if('-1' != hasServUser){
		$("select[@name='accessType']").val(accessType);
		$("select[@name='netType']").val(netType);
		chgAccessType(accessType);
		chgnetType(netType);
		$("input[@name='vlanid']").val(vlanid);
		$("input[@name='vpi']").val(vpi);
		$("input[@name='vci']").val(vci);
		
		$("input[@name='pppoeUsername']").val(pppoeUsername);
		$("input[@name='pppoePasswd']").val(pppoePasswd);
		$("input[@name='ip']").val(ip);
		$("input[@name='gateway']").val(gateway);
		$("input[@name='mask']").val(mask);
		$("input[@name='dns']").val(dns);
		$("input[@name='voipUsername']").val(voipUsername);
		$("input[@name='voipPasswd']").val(voipPasswd);
	}
}

//�󶨶˿�
/**
fuction bindLanPort(){

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
	return bindPort;
}
**/


//-->
</SCRIPT>
</head>
<body>
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
	<tr>
		<TH colspan="4">
			�û�������Ϣ
			<input type="hidden" name="servTypeId">
			<input type="hidden" name="deviceId">
			<input type="hidden" name="userId">
			<input type="hidden" name="hasServUser">
			<input type="hidden" name="oui">
			<input type="hidden" name="devSn">
		</TH>
	</tr>

	<TR bgcolor="#FFFFFF" >
		<TD width="15%" class=column align="right">�û��˺�:</TD>
		<TD width="35%"><s:property value="username"/></TD>
		<TD width="15%" class=column align="right">�û�����:</TD>
		<TD width="35%"><s:property value="realname"/></TD>
	</TR>
	<TR bgcolor="#FFFFFF" >
		<TD width="15%" class=column align="right">����:</TD>
		<TD width="35%">
			<s:property value="cityName"/>
			<input type="hidden" name="cityId">
		</TD>
		<TD width="15%" class=column align="right">����:</TD>
		<TD width="35%"><s:property value="officeName"/>
			<input type="hidden" name="officeId" >
		</TD>
	</TR>
	<TR bgcolor="#FFFFFF" >
		<TD width="15%" class=column align="right">�豸:</TD>
		<TD colspan="3">
			<s:property value="oui"/>-<s:property value="devSn"/>
		</TD>
	</TR>
	
	<tr>
		<TD colspan="4" class="green_title">
			����������Ϣ
		</TD>
	</tr>
	
	<TR bgcolor="#FFFFFF" STYLE="display: ">
		<TD width="15%" class=column align="right">���뷽ʽ:</TD>

		<TD width="35%"><SELECT name="accessType" class="bk"
			onclick="chgAccessType(this.value);">
			<OPTION value="1">ADSL����</OPTION>
			<OPTION value="2">LAN����</OPTION>
			<OPTION value="3">���˽���</OPTION>
		</SELECT></TD>

		<TD id="id_pvc1" width="15%" class=column align="right">PVC:</TD>
		<TD id="id_pvc2" width="35%"><input type="text" name="vpi"
			maxlength=3 size=3 class="bk" />/ <input type="text" name="vci"
			maxlength=3 size=3 class="bk" /></TD>

		<TD id="id_vlan1" width="15%" class=column align="right" style="display:none">VlanID:</TD>
		<TD id="id_vlan2" width="35%" style="display:none"><input type="text" , name="vlanid"
			value="" maxlength=20 size=20 class="bk"></TD>
	</TR>
	
	<TR bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="15%">������ʽ:</TD>
		<TD colspan="3" id="netType"><SELECT name="netType" class="bk"
			onclick="chgnetType(this.value);">
			<OPTION value="1">�Ž�</OPTION>
			<OPTION value="2">·��</OPTION>
			<OPTION value="3">��̬IP</OPTION>
			<OPTION value="4">DHCP</OPTION>
		</SELECT></TD>
	</TR>

	<TR id="id_routed" bgcolor="#FFFFFF" STYLE="display: none">
		<TD width="15%" class=column align="right">PPPoE�˺�:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="pppoeUsername"
			maxlength=20 class="bk" size=20></TD>
		<TD width="15%" class=column align="right">PPPoE����:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="pppoePasswd"
			maxlength=20 class="bk" size=20></TD>
	</TR>

	<TR id="id_static1" bgcolor="#FFFFFF" STYLE="display: none">
		<TD class=column align="right" nowrap width="15%">IP��ַ:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="ip" maxlength=16 size=20
			class="bk"></TD>
		<TD width="15%" class=column align="right">����:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="gateway" maxlength=16
			size=20 class="bk"></TD>
	</TR>
	<TR id="id_static2" bgcolor="#FFFFFF" STYLE="display: none">
		<TD width="15%" class=column align="right">��������:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="mask" maxlength=16
			size=20 class="bk"></TD>
		<TD class=column align="right" width="15%">DNS:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="dns" maxlength=16
			size=20 class="bk"></TD>
	</TR>

	<TR id="id_voip" bgcolor="#FFFFFF" STYLE="display: none">
		<TD width="15%" class=column align="right">VOIP��֤�û���:</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="voipUsername"
			maxlength=30 size=30 class="bk"></TD>
		<TD class=column align="right" width="15%">VOIP��֤����</TD>
		<TD width="35%"><INPUT TYPE="text" NAME="voipPasswd" maxlength=30
			size=30 class="bk"></TD>
	</TR>

	<TR id="id_bindport" bgcolor="#FFFFFF" style="display: none">
		<TD class=column align="right" width="15%">�󶨶˿�:</TD>
		<TD width="35%" colspan="3"><INPUT TYPE="checkbox" NAME="lan1"
			value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.">LAN1
		<INPUT TYPE="checkbox" NAME="lan2"
			value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2.">LAN2
		<INPUT TYPE="checkbox" NAME="lan3"
			value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3.">LAN3
		<INPUT TYPE="checkbox" NAME="lan4"
			value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4.">LAN4
<!-- 
		<INPUT TYPE="checkbox" NAME="lan5"
			value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1">WLAN1
		<INPUT TYPE="checkbox" NAME="lan6"
			value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2">WLAN2
		<INPUT TYPE="checkbox" NAME="lan7"
			value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3">WLAN3
		<INPUT TYPE="checkbox" NAME="lan8"
			value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4">WLAN4
 -->
		</TD>
	</TR>
	<TR bgcolor="#FFFFFF">
		<TD colspan="4" class=foot align="right" nowrap>
		<button type="button" name="subBtn" onclick="doBusinessSheet()">�����·�</button>
		</TD>
	</TR>
	<TR id="resultTR1" bgcolor="#FFFFFF" style="display: none">
		<TD class=column align="right" width="15%" id="resultTD1"></TD>
		<TD colspan="3">
		<DIV id="result1"></DIV>
		</TD>
	</TR>
	<TR bgcolor="#FFFFFF">
		<TD colspan="4">��ע���豸��������ߣ�ϵͳ�����豸���ߺ��һʱ����й����·���</TD>
	</TR>
</TABLE>
</body>

<SCRIPT LANGUAGE="JavaScript">
<!--//
	initShow();
//-->
</SCRIPT>
</html>