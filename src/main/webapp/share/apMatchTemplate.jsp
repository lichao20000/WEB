<%--
Date		: 2012��5��10��
Desc		: Ӧ���ն�ƥ��ģ��.
--%>
<%@ page contentType="text/html; charset=GBK"%>
<%
	request.setCharacterEncoding("gbk");
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<FORM NAME="frm" METHOD="post" ACTION="" onSubmit=""><input
	type="hidden" name="gwShare_matchField_temp0" value="nomatch" /> <input
	type="hidden" name="gwShare_matchField_temp1" value="nomatch" /> <input
	type="hidden" name="gwShare_matchField_temp2" value="nomatch" /> <input
	type="hidden" name="gwShare_matchField_temp3" value="nomatch" /> <input
	type="hidden" name="gwShare_matchField_temp4" value="nomatch" /> <input
	type="hidden" name="gwShare_matchField_temp5" value="nomatch" /> <input
	type="hidden" name="prot_type" value="tr069" />
<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%"
	bgcolor=#999999>
	<TR bgcolor="#FFFFFF">
		<TH colspan="2" align="center" nowrap class=column width="50%">
		ƥ����</TH>
		<TH colspan="2" align="center" nowrap class=column width="50%">
		ƥ�����</TH>

	</TR>
	<TR bgcolor="#FFFFFF">
		<td nowrap align="right" class=column width="15%">����OUI</td>
		<td id="" width="35%"><select name="oui" class="bk">
			<option value="-1">==��ѡ��==</option>
			<option value="����">����</option>
			<option value="��Ϊ">��Ϊ</option>
		</select></td>
		<td nowrap align="center" class=column width="25%"><input
			type="radio" class=jianbian name="match_oui" value="="
			onclick="gwShare_matchField_selected(this,0)" /> == &nbsp;&nbsp; <input
			type="radio" class=jianbian name="match_oui" value="!="
			onclick="gwShare_matchField_selected(this,0)" /> != &nbsp;&nbsp;</td>
		<td id="" width="25%"><input type="button"
			onclick="addMatchExpression(1)" class=jianbian
			name="template_addButton1" value=" �� �� " /></td>

	</TR>

	<TR bgcolor="#FFFFFF">
		<td nowrap align="right" class=column width="15%">ҵ���ʶ</td>
		<td id="" width="35%"><select name="servName" class="bk">
			<option value="CT-COM_AP_Internet">CT-COM_AP_Internet</option>
			<option value="CT-COM_AP_IPTV">CT-COM_AP_IPTV</option>
			<option value="CT-COM_AP_VoIP">CT-COM_AP_VoIP</option>
		</select></td>
		<td nowrap align="center" class=column width="25%"><input
			type="radio" class=jianbian name="match_serv" value="="
			onclick="gwShare_matchField_selected(this,1)" /> == &nbsp;&nbsp; <input
			type="radio" class=jianbian name="match_serv" value="!="
			onclick="gwShare_matchField_selected(this,1)" /> != &nbsp;&nbsp;</td>
		<td id="" width="25%"><input type="button"
			onclick="addMatchExpression(2)" class=jianbian
			name="template_addButton2" value=" �� �� " /></td>

	</TR>
	<TR bgcolor="#FFFFFF">
		<td nowrap align="right" class=column width="15%">�豸���к�</td>
		<td id="" width="35%"><input type="text" name="devicesn"
			maxlength=255 class=bk size=20></td>
		<td nowrap align="center" class=column width="25%"><input
			type="radio" class=jianbian name="match_sn" value="="
			onclick="gwShare_matchField_selected(this,2)" /> == &nbsp;&nbsp; <input
			type="radio" class=jianbian name="match_sn" value="!="
			onclick="gwShare_matchField_selected(this,2)" /> != &nbsp;&nbsp;</td>
		<td id="" width="25%"><input type="button"
			onclick="addMatchExpression(3)" class=jianbian
			name="template_addButton3" value=" �� �� " /></td>

	</TR>
	<TR bgcolor="#FFFFFF">
		<td nowrap align="right" class=column width="15%">�豸����</td>
		<td id="" width="35%"><select name="deviceType" class="bk">
			<option value="-1">==��ѡ��==</option>
			<option value="CT-COM_AP">CT-COM_AP</option>
			<option value="CT-COM_STB">CT-COM_STB</option>
			<option value="CT-COM_Screen4">CT-COM_Screen4</option>
			<option value="CT-COM_HomeCenter">CT-COM_HomeCenter</option>
		</select></td>
		<td nowrap align="center" class=column width="25%"><input
			type="radio" class=jianbian name="match_devicetype" value="="
			onclick="gwShare_matchField_selected(this,3)" /> == &nbsp;&nbsp; <input
			type="radio" class=jianbian name="match_devicetype" value="!="
			onclick="gwShare_matchField_selected(this,3)" /> != &nbsp;&nbsp;</td>
		<td id="" width="25%"><input type="button"
			onclick="addMatchExpression(4)" class=jianbian
			name="template_addButton4" value=" �� �� " /></td>

	</TR>
	<TR bgcolor="#FFFFFF">
		<td nowrap align="right" class=column width="15%">����汾</td>
		<td id="" width="35%"><select name="software" class="bk">
			<option value="-1">==��ѡ��==</option>
			<option value="V1.1">V1.1</option>
			<option value="V1.2">V1.2</option>
		</select></td>
		<td nowrap align="center" class=column width="25%"><input
			type="radio" class=jianbian name="match_software" value="="
			onclick="gwShare_matchField_selected(this,4)" /> == &nbsp;&nbsp; <input
			type="radio" class=jianbian name="match_software" value="!="
			onclick="gwShare_matchField_selected(this,4)" /> != &nbsp;&nbsp;</td>
		<td id="" width="25%"><input type="button"
			onclick="addMatchExpression(5)" class=jianbian
			name="template_addButton5" value=" �� �� " /></td>

	</TR>
	<TR bgcolor="#FFFFFF">
		<td nowrap align="right" class=column width="15%">Ӳ���汾</td>
		<td id="" width="35%"><select name="hardware" class="bk">
			<option value="-1">==��ѡ��==</option>
			<option value="1.1">1.1</option>
			<option value="1.2">1.2</option>
		</select></td>
		<td nowrap align="center" class=column width="25%"><input
			type="radio" class=jianbian name="match_hardware" value="="
			onclick="gwShare_matchField_selected(this,5)" /> == &nbsp;&nbsp; <input
			type="radio" class=jianbian name="match_hardware" value="!="
			onclick="gwShare_matchField_selected(this,5)" /> != &nbsp;&nbsp;</td>
		<td id="" width="25%"><input type="button"
			onclick="addMatchExpression(6)" class=jianbian
			name="template_addButton6" value=" �� �� " /></td>

	</TR>
	<TR bgcolor="#FFFFFF">
		<TH colspan="4" align="center" nowrap class=column>����ģ��ƥ����ʽ</TH>
	</TR>
	<TR bgcolor="#FFFFFF">
		<TD colspan="4" align="left"><textarea cols=150 rows=6
			id="matchExpression" readonly></textarea> &nbsp;&nbsp;<input
			type="button" onclick="clean()" class=jianbian name="reset"
			value=" �� �� " /></TD>

	</TR>

</TABLE>
</FORM>
<SCRIPT LANGUAGE="JavaScript">
	function gwShare_matchField_selected(value, type) {

		switch (type) {
		case 0: {
			$("input[@name='gwShare_matchField_temp0']").val(value.value);
			break;
		}
		case 1: {
			$("input[@name='gwShare_matchField_temp1']").val(value.value);
			break;
		}
		case 2: {
			$("input[@name='gwShare_matchField_temp2']").val(value.value);
			break;
		}
		case 3: {
			$("input[@name='gwShare_matchField_temp3']").val(value.value);
			break;
		}
		case 4: {
			$("input[@name='gwShare_matchField_temp4']").val(value.value);
			break;
		}
		case 5: {
			$("input[@name='gwShare_matchField_temp5']").val(value.value);
			break;
		}

		}
	}
	function clean() {
		$("#matchExpression").val("");
	}
	function addMatchExpression(type) {
		switch (type) {
		case 1: {
			var oui = $("select[@name='oui']").val();
			var match = $("input[@name='gwShare_matchField_temp0']").val();
			var matchExpression = $("#matchExpression").val();
			if (oui == '-1') {
				alert("��ѡ����oui!");
				return;
			}
			if (match == 'nomatch') {
				alert("��ѡ��ƥ�����!");
				return;
			}
			var ouiExp = "ManufacturerOUI" + match + oui;
			if (matchExpression != '') {
				if (matchExpression.indexOf(ouiExp) > -1) {
					alert("��ƥ������Ѿ����ڣ�" + ouiExp);
					return;
				}
				ouiExp = matchExpression + ";" + ouiExp;
			}
			$("#matchExpression").val(ouiExp);
			break;
		}
		case 2: {
			var servName = $("select[@name='servName']").val();
			var match = $("input[@name='gwShare_matchField_temp1']").val();
			var matchExpression = $("#matchExpression").val();
			if (servName == '-1') {
				alert("��ѡ��ҵ������!");
				return;
			}
			if (match == 'nomatch') {
				alert("��ѡ��ƥ�����!");
				return;
			}
			var servNameExp = "ProductClass" + match + servName;
			if (matchExpression != '') {
				if (matchExpression.indexOf(servNameExp) > -1) {
					alert("��ƥ������Ѿ����ڣ�" + servNameExp);
					return;
				}
				servNameExp = matchExpression + ";" + servNameExp;
			}
			$("#matchExpression").val(servNameExp);
			break;
		}
		case 3: {
			var devicesn = $("input[@name='devicesn']").val();
			var match = $("input[@name='gwShare_matchField_temp2']").val();
			var matchExpression = $("#matchExpression").val();
			if (devicesn == '') {
				alert("����д�ն����к�!");
				return;
			}
			if (match == 'nomatch') {
				alert("��ѡ��ƥ�����!");
				return;
			}
			var devicesnExp = "SerialNumber" + match + devicesn;
			if (matchExpression != '') {
				if (matchExpression.indexOf(devicesnExp) > -1) {
					alert("��ƥ������Ѿ����ڣ�" + devicesnExp);
					return;
				}
				devicesnExp = matchExpression + ";" + devicesnExp;
			}
			$("#matchExpression").val(devicesnExp);
			break;
		}
		case 4: {
			var deviceType = $("select[@name='deviceType']").val();
			var match = $("input[@name='gwShare_matchField_temp3']").val();
			var matchExpression = $("#matchExpression").val();
			if (deviceType == '-1') {
				alert("��ѡ���豸����!");
				return;
			}
			if (match == 'nomatch') {
				alert("��ѡ��ƥ�����!");
				return;
			}
			var deviceTypeExp = "DeviceType" + match + deviceType;
			if (matchExpression != '') {
				if (matchExpression.indexOf(deviceTypeExp) > -1) {
					alert("��ƥ������Ѿ����ڣ�" + deviceTypeExp);
					return;
				}
				deviceTypeExp = matchExpression + ";" + deviceTypeExp;
			}
			$("#matchExpression").val(deviceTypeExp);
			break;
		}
		case 5: {
			var software = $("select[@name='software']").val();
			var match = $("input[@name='gwShare_matchField_temp4']").val();
			var matchExpression = $("#matchExpression").val();
			if (software == '-1') {
				alert("��ѡ������汾!");
				return;
			}
			if (match == 'nomatch') {
				alert("��ѡ��ƥ�����!");
				return;
			}
			var softwareExp = "SoftwareVersion" + match + software;
			if (matchExpression != '') {
				if (matchExpression.indexOf(softwareExp) > -1) {
					alert("��ƥ������Ѿ����ڣ�" + softwareExp);
					return;
				}
				softwareExp = matchExpression + ";" + softwareExp;
			}
			$("#matchExpression").val(softwareExp);
			break;
		}
		case 6: {
			var hardware = $("select[@name='hardware']").val();
			var match = $("input[@name='gwShare_matchField_temp5']").val();
			var matchExpression = $("#matchExpression").val();
			if (hardware == '-1') {
				alert("��ѡ��Ӳ���汾!");
				return;
			}
			if (match == 'nomatch') {
				alert("��ѡ��ƥ�����!");
				return;
			}
			var hardwareExp = "HardwareVersion" + match + hardware;
			if (matchExpression != '') {
				if (matchExpression.indexOf(hardwareExp) > -1) {
					alert("��ƥ������Ѿ����ڣ�" + hardwareExp);
					return;
				}
				hardwareExp = matchExpression + ";" + hardwareExp;
			}
			$("#matchExpression").val(hardwareExp);
			break;
		}
		}

	}
</SCRIPT>