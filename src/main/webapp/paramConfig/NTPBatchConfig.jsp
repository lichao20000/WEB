<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("device_id");
%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var did = "";;
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
			did = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
				did += oselect[i].value + ",";
				num++;
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
<jsp:include page="/share/selectDeviceJs.jsp">
	<jsp:param name="selectType" value="checkbox"/>
	<jsp:param name="jsFunctionName" value=""/>
</jsp:include>
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
									NTP �������á�
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
							NTP ��������
						</TH>
					</TR>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right"  width="15%">
													���÷�ʽ
												</TD>
												<TD colspan=3  width="85%">
													<input type="radio" name="type" id="rd1" class=btn value="1" ><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" id="rd2" class=btn value="2" checked><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_status" style="display:none">
											    <TD align="right">
													����״̬
												</TD>
												<TD colspan=3>
													<input type="radio" name="status" checked id="rd3" onclick="changeStatus(this);" class=btn><label for="rd3">����</label>&nbsp;
													<input type="radio" name="status" id="rd4" onclick="changeStatus(this);" class=btn><label for="rd4">�ر�</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_1st" style="display:none">
											    <TD align="right">
													�� NTP ��������ַ������
												</TD>
												<TD colspan=3>
													<input type="text" name="main_ntp_server" class=bk>
													<span id="iscisco"></span>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_2nd" style="display:none">
											    <TD align="right">
													�� NTP ��������ַ������
												</TD>
												<TD colspan=3>
													<input type="text" name="second_ntp_server" class=bk>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_3nd" style="display:none">
											    <TD align="right">
													�ɹ�����ֵ(%)
												</TD>
												<TD colspan=3>
													<input type="text" name="successpercent" onkeyup="onlyNum(this);" class=bk value="0">
													��ǰ�������óɹ��ʸ�����ֵ�Ž�������,Ĭ��Ϊ0
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_4nd" style="display:none">
											    <TD align="right">
													���Դ���
												</TD>
												<TD colspan=3>
													<input type="text" name="repeatnum" onkeyup="onlyNum(this);" class=bk value="0">
													Ĭ��Ϊ0
												</TD>
											</TR>
											
											
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" onclick="ExecMod();" style="display:none" value=" �� �� " class=btn >
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

	function getStatus() {
     		if(CheckForm()){
		        page = "NTPBatchConfigStatus.jsp?device_id=" +did
		        	+ "&oid_type=1"
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2") 
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "���ڻ�ȡ�豸NTP״̬�������ĵȴ�....";
				document.getElementById("tr001").style.display = "";
				document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}
	}
	
	function ExecMod(){
     		if(CheckForm()){
				if (!check2()) return false;
		        page = "NTPBatchConfigSave.jsp?device_id=" +did 
		        	+ "&oid_type=1" 
		        	+ "&main_ntp_server=" + document.forms[0].main_ntp_server.value 
		        	+ "&second_ntp_server=" + document.forms[0].second_ntp_server.value 
		        	+ "&successpercent=" + document.forms[0].successpercent.value 
		        	+ "&repeatnum=" + document.forms[0].repeatnum.value 
		        	+ "&type=" + (document.getElementById("rd1").checked ? "1" : "2") 
		        	+ "&status=" + (document.getElementById("rd3").checked ? "1" : "2") 
		        	+ "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "����������Ͻ���������ĵȴ�....";
				//document.getElementById("tr001").style.display = "";
				//document.getElementById("tr002").style.display = "";
				document.all("childFrm").src = page;
			}else{
				return false;
			}	
     }
	
    function check2() {
		if (document.getElementById("rd3").checked) {
			var m = document.forms[0].main_ntp_server.value;
			var s = document.forms[0].second_ntp_server.value;
			//var successpercent =  document.forms[0].successpercent.value 
		    //var repeatnum =  document.forms[0].repeatnum.value 
			if (m.Trim() == "" && s.Trim() == "") {
				alert("����������һ����������ַ������");
				return false;
			}
			if (m == s) {
				alert("�����ò�ͬ�ķ�������ַ������");
				return false;
			}
		}
		return true;
	}

	function setStatus() {

		document.getElementById("ntp_status").style.display = "";
		//document.getElementById("ntp_type").style.display = "";
		document.getElementById("ntp_1st").style.display = "";		
		document.getElementById("ntp_2nd").style.display = "";
		document.getElementById("ntp_3nd").style.display = "";
		document.getElementById("ntp_4nd").style.display = "";
		document.getElementById("bt_set").style.display = "";
	}

	function changeStatus(o) {
		if (o.id == "rd3") {
			// document.forms[0].main_ntp_server.disabled = false;
			// document.forms[0].second_ntp_server.disabled = false;
			document.getElementById("ntp_1st").style.display = "";
			document.getElementById("ntp_2nd").style.display = "";
			document.getElementById("ntp_3nd").style.display = "";
			document.getElementById("ntp_4nd").style.display = "";
		} else {
			// document.forms[0].main_ntp_server.disabled = true;
			// document.forms[0].second_ntp_server.disabled = true;
			document.getElementById("ntp_1st").style.display = "none";
			document.getElementById("ntp_2nd").style.display = "none";
			document.getElementById("ntp_3nd").style.display = "none";
			document.getElementById("ntp_4nd").style.display = "none";
		}
	}
	// ���ƽ�����������
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
//-->
</SCRIPT>