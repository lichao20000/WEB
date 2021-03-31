<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<html>
<head>
<title>��������NTP</title>
<%

String strVendorList = deviceAct.getVendorList(true, "", "");
String str1 = strVendorList.substring(0,strVendorList.indexOf("=="));
String str2 = strVendorList.substring(strVendorList.lastIndexOf("=="));
strVendorList = str1 + "==��ѡ����" + str2;

String strCityList = deviceAct.getCityListSelf(true, "", "",request);
str1 = strCityList.substring(0,strCityList.indexOf("=="));
str2 = strCityList.substring(strCityList.lastIndexOf("=="));
strCityList = str1 + "==��ѡ������" + str2;

%>

<script language="javascript">

function ExecMod(){

	//alert(document.frm.status.value);
	//alert(document.frm.type.value);
	//return false;
	
	var t_obj = document.getElementsByName("device_id");
	var main_server = document.frm.main_ntp_server.value;
	var page = "";
	var flag = 0
	if(t_obj.length < 0){
		alert("��ѡ���豸");
		return false;
	}else{
		for(var i=0; i<t_obj.length; i++){
			if(t_obj[i].checked == true){
				flag = flag + 1
			}
		}
		if(flag==0){
			alert("��ѡ���豸");
			return false;
		}
	}
	if(main_server.trim() == ""){
		alert("����д��NTP��������ַ������");
		return false;
		document.frm.main_ntp_server.focus();
	}

	return true;
}
function showDevice(){

	var city_id = document.frm.city_id.value;
	var vendor_id = document.frm.vendor_id.value;
	var device_model_id = document.frm.device_model_id.value;
	var page = "showConfigDeviceList.jsp?needFilter=false&city_id="+city_id
				+ "&vendor_id="+vendor_id+"&device_model_id="+device_model_id
				+ "&refresh="+Math.random();
	document.all("childFrm").src = page;
}

//ȫѡ����
function selectAll(elmID){
	t_obj = document.getElementsByName(elmID);
	if(!t_obj) return;
	obj = event.srcElement;

	if(obj.checked){
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = true;
		}
	}
	else{
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = false;
		}
	}
}

</script>
</head>
<body onload="load();">
<form name = "frm" method = "post" onsubmit="ExecMod();" target="childFrmResult" action = "BatchNTPConfigSave.jsp">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
							NTP ��������
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
						<TD colspan="4" align="center">
						<table width="90%">
							<tr>
								<td width="20%" align="center">
									<%=strVendorList%>
								</TD>
								<TD width="20%" align="center">
									<div id="div_devicetype">
									<select name="device_model_id" class="bk">
										<option value="-1">
											==��ѡ���ͺ�==
										</option>
									</select>
									</div>
								</TD>
								<TD width="20%" align="center">
									<%=strCityList %>
								</TD>
								<TD width="20%" align="center">
									<input type="button" class=btn value="��ѯ" onclick="showDevice()">
								</TD>
							</tr>
						</table>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" width="20%">�豸�б�: <br>
						<INPUT TYPE="checkbox" onclick="selectAll('device_id')"
							name="device"> ȫѡ</TD>
						<TD colspan="3">
						<div id="div_device"
							style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
						<span>��ѡ���豸��</span></div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
					    <TD align="right">
							���÷�ʽ:
						</TD>
						<TD >
							<input type="radio" name="type" id="rd1" class=btn value="1" ><label for="rd1">TR069</label>&nbsp;
							<input type="radio" name="type" id="rd2" class=btn value="2" checked><label for="rd2">SNMP</label>
						</TD>
					    <TD align="right">
							����״̬:
						</TD>
						<TD >
							<input type="radio" name="status" checked  class=btn id="rd3" value="1"><label for="rd3">����</label>&nbsp;
							<input type="radio" name="status"  class=btn id="rd4" value="2"><label for="rd4">�ر�</label>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="ntp_1st" >
					    <TD align="right">
							�� NTP ��������ַ������:
						</TD>
						<TD >
							<input type="text" name="main_ntp_server" class=bk>
							<span id="iscisco"></span>
						</TD>
					    <TD align="right">
							�� NTP ��������ַ������:
						</TD>
						<TD >
							<input type="text" name="second_ntp_server" class=bk>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="ntp_3nd" >
					    <TD align="right">
							���Գɹ�����ֵ(%)
						</TD>
						<TD >
							<input type="text" name="successpercent" onkeyup="onlyNum(this);" class=bk value="50">
						</TD>

					    <TD align="right">
							�ظ�ִ�еĴ���
						</TD>
						<TD >
							<input type="text" name="repeatnum" onkeyup="onlyNum(this);" class=bk value="3">
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD align="right">
							�����豸����(%)
						</TD>
						<TD >
							<input type="text" name="testpercent" onkeyup="onlyNum(this);" class=bk value="10">
						</TD>
						<TD colspan="2" align="right">
							<INPUT TYPE="submit" value="�� ��" class=btn >&nbsp;&nbsp;&nbsp;
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr001" >
						<TH colspan="4">�������</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr002" >
						<td colspan="4" valign="top" class=column>
						<div id="div_config"
							style="width: 100%; height: 120px; z-index: 1; top: 100px;"></div>
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</form>
</body>
		<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrmResult name="childFrmResult" SRC="" STYLE="display:none"></IFRAME>
<script language="javascript">
<!--


function showChild(param)
{
	var page ="";
	if(param == "vendor_id"){
		page = "showDevicetypeid.jsp?vendor_id="+document.frm.vendor_id.value;
		
		document.all("childFrm1").src = page;
		// �����˼�Ƴ���
		if(document.frm.vendor_id.value == "001AA1"){
			$("input[@name='main_ntp_server']").val("");
			$("input[@name='main_ntp_server']").attr("disabled","true");
			$("#iscisco").html("��NTP����������д");
		}else{
			$("input[@name='main_ntp_server']").attr("disabled","");
			$("#iscisco").html("");
		}
	}
}

// ���ƽ�����������
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
	
	function load(){
		document.all("childFrmResult").src = "BatchNTPConfigSave.jsp";
	}
//-->

</script>
</html>
