<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
var checkvalue ="<%=device_id%>";
function CheckForm(){
 	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("��ѡ���豸��");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			checkvalue = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			  checkvalue = oselect[i].value;
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
								    <TD align="right" width="15%">
										���÷�ʽ:
									</TD>
									<TD colspan=3 width="85%">
										<input type="radio" name="type" id="rd1" class=btn><label for="rd1">TR069</label>&nbsp;
										<input type="radio" name="type" checked id="rd2" class=btn><label for="rd2">SNMP</label>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_status" style="display:none">
									<td colspan="4" valign="top" class=column>
										<div id="div_ssid" style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="wlan_new_ssid" style="display:none">
									<td colspan="4" valign="top" class=column>
										<div id="div_ssid_new" style="width: 100%; height: 120px; z-index: 1; top: 100px;">
											<table border='0' align='center' width='100%' cellpadding='0' cellspacing='1' bgcolor='#999999'>
												<TR bgcolor="#FFFFFF" id="wlan_add_title">
													<td height="30" colspan="4" align="left" bgcolor='#FFFFFF'>
														�½�SSID�������� (��ʾ������SSID����������ĳSSID������ͬ����ɾ��ԭ��ͬ���Ƶ�SSID��
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" id="wlan_add">
													<td align='center' id="new_idx" bgcolor='#FFFFFF' width='40' nowrap>
													</TD>
													<td align='center' bgcolor='#FFFFFF' width='200' nowrap>
														<input type="text" name="new_ssid_name"/>
													</td>
													<td  align='center' bgcolor='#FFFFFF' width='150' nowrap>
														<input type="radio" name="new_ssid_open" checked id="nso1"/><label for="nso1">����</label>
														<input type="radio" name="new_ssid_open" id="nso2"/><label for="nso2">�ر�</label>
													</td>
													<td  align='left' style='padding-left:30' bgcolor='#FFFFFF' nowrap>
														<input type="radio" name="new_ssid_hide" id="nsh1"/><label for="nsh1">�㲥</label>
														<input type="radio" name="new_ssid_hide" checked id="nsh2"/><label for="nsh2">����</label>
													</td>
												</TR>
											</table>
										</div>
									</td>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" id="bt_set" style="display:none" onclick="this.blur();ExecMod();" value=" �� �� " class=btn >
													<INPUT TYPE="button" id="bt_add" style="display:none" onclick="this.blur();createSSID();" value=" �� �� " class=btn >
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
var MAX_SSID = 32;
var MIN_SSID_INDEX = 1;
var MAX_SSID_INDEX = 1024;

var disp = 1;
var max_idx;//��SSID��������
var ssid_num = 0;
var ssid_idx_arr = null;

var log_msg = "";

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

	/**
	 *	�½�һ��SSID
	 */
	function createSSID() {
		if (ssid_idx_arr == null) {
			max_idx = 1;
		} else {
			var len = ssid_idx_arr.length;
			max_idx = ssid_idx_arr[len - 1];
			//alert("max_idx=" + max_idx);
			if (max_idx > len) {
				for (var i=1; i<len; i++) {
					//alert("i=" + i + "  ssid_ixd_arr[" + (i - 1) + "]=" + ssid_idx_arr[i - 1]);
					if (i < ssid_idx_arr[i - 1]) {
						max_idx = i;
						break;
					}
				}
			} else {
				max_idx++;
			}
		}
		//alert(max_idx);
		showById("wlan_new_ssid");
		document.getElementById("new_idx").innerText = max_idx;
		document.getElementById("bt_add").value = "�½�(�ύ)";
		document.getElementById("bt_add").onclick = createSSIDSave;
	}

	function createSSIDSave() {
		if (!check2()) return false;
		log_msg = "";
		log_msg += "�½�SSID�� IDΪ" + max_idx + "��" + "����Ϊ" + document.forms[0].new_ssid_name.value + "��" + (document.getElementById("nso1").checked ? "������" : "�رգ�") + (document.getElementById("nsh2").checked ? "���أ�" : "�رգ�");
  		page = "WLANConfigSSIDSave.jsp?device_id=" + checkvalue + "&log_msg=" + log_msg + "&new_idx=" + max_idx + "&open=" + (document.getElementById("nso1").checked ? "1" : "2") + "&hide=" + (document.getElementById("nsh2").checked ? "1" : "2") + "&name=" + document.forms[0].new_ssid_name.value + "&oid_type=1&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&action_type=3&refresh=" + new Date().getTime();
		document.all("div_ping").innerHTML = "��������µ�SSID...";
		document.all("childFrm").src = page;
	}
	
	function resetTo() {
		disp = 0;
		getStatus();
	}

	function check2() {
		var m = document.forms[0].new_ssid_name.value;
		if (m.Trim() == "") {
			alert("SSID���Ʋ���Ϊ��");
			return false;
		}
		return true;
	}

	function check3() {
		var m = document.forms[0].new_ssid_name.value;
		if (m.Trim() == "") {
			alert("SSID���Ʋ���Ϊ��");
			return false;
		}
		return true;
	}

	function getStatus() {
		hideById("wlan_new_ssid");
		document.getElementById("bt_add").value = " �� �� ";
		document.getElementById("bt_add").onclick = createSSID;

		document.getElementById("tr001").style.display = "";
		document.getElementById("tr002").style.display = "";
  		if(CheckForm()){
  			page = "WLANConfigSSIDSave.jsp?device_id=" + checkvalue + "&oid_type=1&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&action_type=1&refresh=" + new Date().getTime();
			//alert(page);
			if (disp == 1) {
				document.all("div_ping").innerHTML = "���ڻ�ȡ����������Ϣ...";
			}
			document.all("childFrm").src = page;
		}else{
			return false;
		}
	}

     function ExecMod(){
  		if(CheckForm()){
			//if (!check3()) return false;
			var param = getParam();
  			page = "WLANConfigSSIDSave.jsp?device_id=" + checkvalue + "&param=" + param + "&log_msg=" + log_msg + "&oid_type=1&action_type=2&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&refresh=" + new Date().getTime();
			document.all("div_ping").innerHTML = "������������״̬...";
			//alert(page);
			document.all("childFrm").src = page;  		 
		}else{
			return false;
		}
	 }

	function getParam() {
		log_msg = "";
		var p = "";
		if (ssid_idx_arr == null) return null;
		var len = ssid_idx_arr.length;
		for (var i=0; i<len; i++) {
			var s = "";
			var idx = ssid_idx_arr[i];
			var open_id = idx + "_1_1";
			var hide_id = idx + "_2_2";
			var del_id = idx + "_3_1";
			var name_id = idx + "_4_1";
			log_msg += "SSID " + idx + "��";
			if (document.getElementById(del_id).checked) {
				log_msg += "ɾ��";
			} else {
				log_msg += "����Ϊ " + document.getElementById(name_id).value + "��" + (document.getElementById(open_id).checked ? "������" : "�رգ�") + (document.getElementById(hide_id).checked ? "���أ� " : "�㲥�� ");
			}
			s += idx + "_" + (document.getElementById(open_id).checked ? "1" : "2") + "_" + (document.getElementById(hide_id).checked ? "1" : "2") + "_" + (document.getElementById(del_id).checked ? "1" : "2") + "_" + document.getElementById(name_id).value;

			if ("" == p) {
				p += s;
			} else {
				p += "|" + s;
			}
		}
		//alert(log_msg);
		//alert(p);
		return p;
	}

	function setStatus(idx_list) {
		var sHtml = "��ǰ SSID ����Ϊ��";
		if (idx_list != null && !("" == idx_list)) {
			ssid_idx_arr = idx_list.split("_");
			if (ssid_idx_arr != null) {
				ssid_num = ssid_idx_arr.length;
			} else {
				ssid_num = 0;
			}
		} else {
			ssid_num = 0;
		}
		sHtml += ssid_num;
		if (disp == 0) {
			disp = 1;
			return;
		}
		document.all("div_ping").innerHTML = sHtml;
		showById("bt_set");
		showById("bt_add");
		showById("wlan_status");
	}

	function showById(id) {
		document.getElementById(id).style.display = "";
	}

	function hideById(id) {
		document.getElementById(id).style.display = "none";
	}
//-->
</SCRIPT>
