<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
request.setCharacterEncoding("GBK");

%>

<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="radio"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>

<SCRIPT LANGUAGE="JavaScript">

var did = "";;

function CheckForm(){
	
	if(!deviceSelectedCheck()){
		alert("��ѡ���豸��");
		return false;
	}

	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				did = oSelect[i].value;
			}
		}
	}
	if(oSelect.checked){
		did = oSelect.value;
	}
	return true;
}

</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
			<div id="filepath" style="display:none"></div>
				<input type="hidden" name="strDevice" value="">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									����ʵ������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									�����ն˵�ʱ���������
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
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
						<td colspan="4">
							<div id="selectDevice"><span>������....</span></div>
						</td>
					</TR>
					<TH colspan="4" align="center">
							SNMP ��������
					</TH>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													���÷�ʽ
												</TD>
												<TD colspan=3 width="85%">
													<input type="radio" name="type" id="rd1" class=btn value="1" checked><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" id="rd2" class=btn value="2"><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td align="right">Э��汾</td>
												<td colspan="3">
												<input type="radio" name="SNMPVersion" id="SNMPVersionV1" value="1" onclick="snmpVersionChange(false);"><label for="SNMPVersionV1">V1</label>&nbsp;
												<input type="radio" name="SNMPVersion" id="SNMPVersionV2" value="2" onclick="snmpVersionChange(false);"><label for="SNMPVersionV2">V2</label>&nbsp;
												<input type="radio" name="SNMPVersion" id="SNMPVersionV3" checked value="3" onclick="snmpVersionChange(true);"><label for="SNMPVersionV3">V3</label>
												</td>
											</TR>
											<!-- SNMPVersion == 1 (V3) -->
											<TR bgcolor="#FFFFFF" name="snmpv3">
												<td align="right" width="15%">��Ȩ�û�</td>
												<td width="35%"><input type="text" name="SecurityUserName"></td>
												<td align="right" width="15%">��Ȩ��Կ</td>
												<td width="35%"><input type="text" name="AuthPasswd"></td>
											</TR>
											<TR bgcolor="#FFFFFF" name="snmpv3">
												<td align="right">˽Կ</td>
												<td colspan="3"><input type="text" name="PrivacyPasswd"></td>
											</TR>
											
											<!-- SNMPVersion == 0 (v1/v2) -->
											<TR bgcolor="#FFFFFF" name="notsnmpv3" style="display:none" >
												<td align="right" width="15%">������</td>
												<td width="35%"><input type="text" name="ReadOnlyCommunity"></td>
												<td align="right" width="15%">д����</td>
												<td width="35%"><input type="text" name="ReadWriteCommunity"></td>
											</TR>
											<TR bgcolor="#FFFFFF" name="notsnmpv3" style="display:none">
												<td align="right">Trap����</td>
												<td colspan="3"><input type="text" name="TrapCommunity"></td>
											</TR>
											
											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" onclick="ExecMod();" value=" �� �� " class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="getStatus();" value=" �� ȡ " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
												<TH colspan="4">�������</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display:none">
												<td colspan="4" valign="top" class=column>
												<div id="div_ping"
													style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
												</td>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	//�Ƿ�չʾV3�汾�������ֶ�
	function snmpVersionChange(flag){
		if(flag==true){
			$("tr[@name='snmpv3']").show();
			$("tr[@name='notsnmpv3']").hide();
		}else{
			$("tr[@name='snmpv3']").hide();
			$("tr[@name='notsnmpv3']").show();
		}
	}
	//SNMP �汾�ֶμ��
	function checkSNMPVersion(){
		var v = $("input[@name='SNMPVersion'][@checked]").val()
		if(v == "3"){
			// V3
			if(document.frm.SecurityUserName.value == ""){
				alert("�������Ȩ�û�");
				document.frm.SecurityUserName.focus();
				return false;
			}
			if(document.frm.AuthPasswd.value == ""){
				alert("�������Ȩ��Կ");
				document.frm.AuthPasswd.focus();
				return false;
			}
			if(document.frm.PrivacyPasswd.value == ""){
				alert("������˽Կ");
				document.frm.PrivacyPasswd.focus();
				return false;
			}
		}else{
			// V1  V2
			if(document.frm.ReadOnlyCommunity.value == ""){
				alert("�����������");
				document.frm.ReadOnlyCommunity.focus();
				return false;
			}
			if(document.frm.ReadWriteCommunity.value == ""){
				alert("������д����");
				document.frm.ReadWriteCommunity.focus();
				return false;
			}
			if(document.frm.TrapCommunity.value == ""){
				alert("������Trap����");
				document.frm.TrapCommunity.focus();
				return false;
			}
			
		}
		return true;
	}
	function getStatus() {
     		if(CheckForm()){
		        page = "SNMPConfigStatus.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "���ڻ�ȡ�豸SNMP����������Ϣ�������ĵȴ�....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}
	}

     function ExecMod(){
     		if(CheckForm()){
     			if(checkSNMPVersion() == false) return false;
     			
		        page = "SNMPConfigSave.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&SNMPVersion=" + $("input[@name='SNMPVersion'][@checked]").val()
		        	+ "&SecurityUserName=" + document.frm.SecurityUserName.value 
		        	+ "&AuthPasswd=" + document.frm.AuthPasswd.value 
		        	+ "&PrivacyPasswd=" + document.frm.PrivacyPasswd.value 
		        	+ "&ReadOnlyCommunity=" + document.frm.ReadOnlyCommunity.value 
		        	+ "&ReadWriteCommunity=" + document.frm.ReadWriteCommunity.value 
		        	+ "&TrapCommunity=" + document.frm.TrapCommunity.value 
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "���������豸SNMP����������Ϣ�������ĵȴ�....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
//-->
</SCRIPT>