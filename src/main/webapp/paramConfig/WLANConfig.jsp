<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");

%>
<SCRIPT LANGUAGE="JavaScript">
var device_id ="<%=device_id%>";

function CheckForm(){
	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("��ѡ���豸��");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			device_id = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			  device_id = oselect[i].value;
			  num++;
			  break;
			}
		}

 	}
 	if(num ==0){
		alert("��ѡ���豸��");
		return false;
	}
	return true;
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: ����"></span>
		</td>
	</tr>
</table>
</center>
</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION="">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											����ʵ������
										</div>
									</td>
									<td><img src="../images/attention_2.gif" width="15"
										height="12">�����͹ر����ߡ�
									</td>

								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							�豸��ѯ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<td colspan="4">
							<div id="selectDevice"><span>������....</span></div>
						</td>
					</TR>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF" id="wlan_type" style="display:none">
								    <TD align="right">
										���÷�ʽ:
									</TD>
									<TD colspan=3>
										<input type="radio" name="type" id="rd1" class=btn><label for="rd1">TR069</label>&nbsp;
										<input type="radio" name="type" checked id="rd2" class=btn><label for="rd2">SNMP</label>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_status" style="display:none">
								    <TD align="right">
										����״̬:
									</TD>
									<TD colspan=3>
										<input type="radio" name="status" checked id="rd3" class=btn><label for="rd3">����</label>&nbsp;
										<input type="radio" name="status" id="rd4" class=btn><label for="rd4">�ر�</label>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_model" style="display:none">
								    <TD align="right">
										����ģʽ:
									</TD>
									<TD colspan=3>
										<span id="modelHere"></span>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_channel" style="display:none">
								    <TD align="right">
										�����ŵ�:
									</TD>
									<TD colspan=3>
										<input type="text" name="channel" id="rd7" value="0">(ȡֵ 0~13�� 0�����Զ�ѡ��)
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_power" style="display:none">
								    <TD align="right">
										���书��:
									</TD>
									<TD colspan=3>
										<input type="text" name="power" id="rd7" value="20">(ȡֵ 1~20��Ĭ��Ϊ20)
									</TD>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" id="bt_set" style="display:none" onclick="this.blur();ExecMod();" value=" �� �� " class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="this.blur();getStatus();" value=" �� ȡ " class=btn >
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								
					<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
						<TH colspan="4">�������</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
						<td colspan="4" valign="top" class=column>
						<div id="div_ping"
							style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
						</td>
					</TR>
							</table>
						</td>
					</tr>
					<TR>
						<TD HEIGHT=20>&nbsp;</TD>
					</TR>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm4 SRC="" STYLE="display:none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
							
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//��String������trim���������˿ո�
// Trim() , Ltrim() , RTrim()

String.prototype.Trim = function() 
{ 
return this.replace(/(^\s*)|(\s*$)/g, ""); 
} 
String.prototype.LTrim = function() 
{ 
return this.replace(/(^\s*)/g, ""); 
} 
String.prototype.RTrim = function() 
{ 
return this.replace(/(\s*$)/g, ""); 
} 

//�Ƿ��������� for Java
function isPosInt(str) {
	var MAX_VALUE = 2147483647;
	var re = /^[1-9]\d*[0-9]?$/g;
	if(str.test(re, str) && str <= MAX_VALUE) {
		return true;
	}
	return false;
}
//�Ƿ������� for Java
function isPosInt_(str) {
	var MAX_VALUE = 2147483647;
	var re = /^[1-9]\d*[0-9]?$/g;
	if(str.test(re, str) && str <= MAX_VALUE) {
		return true;
	}
	return false;
}
String.prototype.test = function(re, str) {
	return re.test(str);
}

	function getStatus() {
		document.getElementById("wlan_channel").style.display = "none";
		document.getElementById("wlan_power").style.display = "none";
		document.getElementById("wlan_status").style.display = "none";
		document.getElementById("wlan_model").style.display = "none";
		document.getElementById("bt_set").style.display = "none";

		document.getElementById("tr001").style.display = "";
		document.getElementById("tr002").style.display = "";
  		if(CheckForm()){
  			page = "WLANConfigSave.jsp?device_id=" + device_id + "&oid_type=1&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&action_type=1&refresh=" + new Date().getTime();
			document.all("div_ping").innerHTML = "���ڻ�ȡ����������Ϣ...";
			document.all("childFrm").src = page;
		}else{
			return false;
		}
	}

     function ExecMod(){
  		if(CheckForm()){
			if (!check2()) return false;
  			page = "WLANConfigSave.jsp?device_id=" + device_id + "&oid_type=1&action_type=2&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&channel=" + document.forms[0].channel.value + "&power=" + document.forms[0].power.value + "&status=" + (document.getElementById("rd3").checked ? "1" : "2") + "&model=" + document.forms[0].modelSel.value + "&refresh=" + new Date().getTime();
			document.all("div_ping").innerHTML = "������������״̬...";
			document.all("childFrm").src = page;  		 
		}else{
			return false;
		}
	 }

	function check2() {
		var m = document.forms[0].channel.value;
		var s = document.forms[0].power.value;
		if (isPosInt(m)) {
			if (m > 13) {
				alert("�����ŵ���ȡֵ��ΧΪ 0~13 ֮�������");
				return false;
			}
		} else if (m != 0) {
			alert("�����ŵ���ȡֵ��ΧΪ 0~13 ֮�������");
			return false;
		} else if (m == 0 && m.length > 1) {
			alert(" 0 û����ô���ɣ�");
			return false;
		}
		if (!isPosInt(s) || s > 20) {
			alert("���书�ʵ�ȡֵ��ΧΪ 1~20 ֮�������");
			return false;
		}
		return true;
	}

	function setStatus(arr) {
		
		var ciscoModel = "<select name='modelSel'><option value='0'>disable<option value='1'>802.11 B/G<option value='2'>802.11 B<option value='3'>802.11 G<option value='4'>802.11 A<option value='5'>802.11 N<option value='6'>802.11 G/N<option value='7'>802.11 A/N<option value='8'>802.11 B/G/N</select>";

		var h3cModel = "<select name='modelSel'><option value='2'>Dot11 b<option value='4'>Dot11 g</select>";

		var oui = arr[0];
		if ("000FE2" == oui) {//h3c
			document.getElementById("modelHere").innerHTML = h3cModel;
			var sHtml = "��ǰ����״̬��" + (arr[1] == 1 ? '����' : (arr[1] == 2 ? '�ر�' : '<δ�ܻ��>'));
			sHtml += "<br>��ǰ����ģʽ��" + (arr[2] == 2 ? 'Dot11b' : (arr[2] == 4 ? 'Dot11g' : '<δ�ܻ��>'));
			sHtml += "<br>��ǰ�����ŵ���" + arr[3];
			sHtml += "<br>��ǰ���书�ʣ�" + arr[4];
			document.all("div_ping").innerHTML = sHtml;

			if (arr[1] == 1) {
				document.getElementById("rd3").checked = true;
			} else {
				document.getElementById("rd4").checked = true;
			}
			if (null != arr[2] && "" != arr[2]) {
				document.forms[0].modelSel.value = arr[2];
			} else {
				document.forms[0].modelSel.value = '4';
			}
			if (null != arr[3] && "" != arr[3]) {
				document.forms[0].channel.value = arr[3];
			}
			if (null != arr[4] && "" != arr[4]) {
				document.forms[0].power.value = arr[4];
			}
			document.getElementById("wlan_channel").style.display = "";
			document.getElementById("wlan_power").style.display = "";
		}
		if ("001AA1" == oui) {//cisco
			document.getElementById("modelHere").innerHTML = ciscoModel;
			var sHtml = "��ǰ����״̬��" + (arr[1] == 1 ? '����' : (arr[1] == 0 ? '�ر�' : '<δ�ܻ��>'));
			var modelNow = "";
			switch (arr[2]){
			case 0:
				modelNow = 'disable';
				break;
			case 1:
				modelNow = '802.11 B/G';
				break;
			case 2:
				modelNow = '802.11 B';
				break;
			case 3:
				modelNow = '802.11 G';
				break;
			case 4:
				modelNow = '802.11 A';
				break;
			case 5:
				modelNow = '802.11 N';
				break;
			case 6:
				modelNow = '802.11 G/N';
				break;
			case 7:
				modelNow = '802.11 A/N';
				break;
			case 8:
				modelNow = '802.11 B/G/N';
				break;
			default:
				modelNow = '<δ�ܻ��>';
			}
			sHtml += "<br>��ǰ����ģʽ��" + modelNow;
			document.all("div_ping").innerHTML = sHtml;
			if (arr[1] == 1) {
				document.getElementById("rd3").checked = true;
			} else {
				document.getElementById("rd4").checked = true;
			}
			if (null != arr[2] && "" != arr[2]) {
				document.forms[0].modelSel.value = arr[2];
			} else {
				document.forms[0].modelSel.value = '1';
			}
		}

		document.getElementById("wlan_status").style.display = "";
		document.getElementById("wlan_model").style.display = "";
		document.getElementById("bt_set").style.display = "";
	}
//-->
</SCRIPT>
	