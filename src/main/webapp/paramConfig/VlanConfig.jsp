<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
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

var did;

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
									VLAN ���á�
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
					<TR>
						<TH colspan="4" align="center">
							VLAN ����
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right" width="15%">
													���÷�ʽ:
												</TD>
												<TD colspan=3 width="85%">
													<input type="radio" name="type" id="rd1" class=btn value="1" checked><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" id="rd2" class=btn value="2"><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<tr bgcolor="#FFFFFF"><td colspan="4" height="20"></td></tr>
											<tr bgcolor="#FFFFFF" id="divVlanCount" style="display:none">
												<td align="right">���õ�vlan����</td>
												<td colspan="3"><input type="text" name="vlanCount" value="" readOnly></td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<table id="para" width="100%" border=0 cellspacing=1 cellpadding=2 style="display:none">
													</table>
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" style="display:" onclick="ExecMod();" value=" �� �� " class=btn >
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
	function getStatus() {
     		if(CheckForm()){
		        page = "VlanConfigStatus.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "���ڻ�ȡ�豸VLAN��Ϣ�������ĵȴ�....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}
	}

     function ExecMod(){
     		if(CheckForm()){
			//if (true) {
				var vlanIdx = "";
		        var VLANID = "";
		        var VLanInterface = "";
		        var VlanName = "";
		        var IPInterfaceIPAddress = "";
		        var IPInterfaceSubnetMask = "";
		        if (true) {
		        //���ɲ���
		        if (document.frm.vlan_idx != null){
		        	if (typeof(document.frm.vlan_idx.value) == 'undefined'){
		        		for (var i=0;i<document.frm.vlan_idx.length;i++){
							vlanIdx += document.frm.vlan_idx[i].value + "|";
		        			VLANID += document.frm.vlan_id[i].value + "|";
		        			VLanInterface += document.frm.VLan_Interface[i].value + "|";
		        			VlanName += document.frm.vlan_name[i].value + "|";
		        			IPInterfaceIPAddress += document.frm.vlan_ip[i].value + "|";
		        			IPInterfaceSubnetMask += document.frm.vlan_mask[i].value + "|";
		        		}
		        	}
		        	else{
						vlanIdx += document.frm.vlan_idx.value + "|";
		        		VLANID = document.frm.vlan_id.value + "|";
		        		VLanInterface = document.frm.VLan_Interface.value + "|";
		        		VlanName = document.frm.vlan_name.value + "|";
		        		IPInterfaceIPAddress = document.frm.vlan_ip.value + "|";
		        		IPInterfaceSubnetMask = document.frm.vlan_mask.value + "|";
		        	}
		        }
		        else{
		        	alert('û�п����õ�vlan��');
		        	return false;
		        }
		        }
		        page = "VlanConfigSave.jsp?device_id=" +did 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2")
		        	+ "&vlanIdx=" + vlanIdx
		        	+ "&VLANID=" + VLANID
		        	+ "&VLanInterface=" + VLanInterface
		        	+ "&VlanName=" + VlanName
		        	+ "&IPInterfaceIPAddress=" + IPInterfaceIPAddress
		        	+ "&IPInterfaceSubnetMask=" + IPInterfaceSubnetMask
		        	+ "&refresh=" + new Date().getTime();
		        
		        page2 = "VlanConfigSave.jsp?device_id=26"
		        	+ "&type=" + "1"
		        	+ "&vlanIdx=" + "22|33"
		        	+ "&VLANID=" + "**"
		        	+ "&VLanInterface=" + "**"
		        	+ "&VlanName=" + "**"
		        	+ "&IPInterfaceIPAddress=" + "**"
		        	+ "&IPInterfaceSubnetMask=" + "**"
		        	+ "&refresh=" + new Date().getTime();
				//alert(page);
				document.all("div_ping").innerHTML = "���������豸VLAN��Ϣ�������ĵȴ�....";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
    }
    
    function setParaHtml(htmlStr){
    	document.all("para").style.display = "";
    	$("#para").html("<TR bgcolor='#FFFFFF'><th class='column'>����</th><th class='column'>vlan ID</th><th class='column'>VLAN�󶨶˿��б�</th><th class='column'>vlan����</th><th class='column'>VLAN IP��ַ</th><th class='column'>VLAN ��������</th></TR>"+htmlStr);
    }
//-->
</SCRIPT>