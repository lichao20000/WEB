<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../toolbar.jsp"%>

<br>
<form name="frm" action="DeviceOfAllDQueryDevice.jsp" target="childFrm" method="post" onsubmit="return CheckForm()">
<table width="95%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
	<tr>
		<td width="162" align="center" class="title_bigwhite">
			�豸���
		</td>
	</tr>
	
</table>
<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
	<TR bgcolor="#FFFFFF">
		<TH colspan="4">�豸���</TH>
	</TR>
	<tr bgcolor="#FFFFFF">
		<td nowrap class=column width="12%">
			<div align="right">�û��˺ţ�</div>
		</td>
		<td class=column  width="35%">
			<input type="text" name="UserName" id="UserName" class="bk">&nbsp;<font color=red>*</font>&nbsp;
			<input name="" type=submit value=" �� ѯ " title="��ѯ���û��������豸">
		</td>
		<td nowrap class=column  width="15%">
			<div align="right">�û��������豸��</div>
		</td>
		<td class=column  width="38%">
			<div id="div_DeviceInfo">������û��˺Ų�ѯ</div>
			<input type="hidden" name="device_id" value="">
		</td>
	</tr>	
</TABLE>
</form>
<br>

<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
	<tr bgcolor="#FFFFFF">
		<td colspan="10"><img src="../images/attention_2.gif" width="15"
			height="12">&nbsp;<font color='red'>˵�����ɹ����ɼ��������·�㣩�� ʧ�ܣ������豸��������ͨ��</font>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<TH colspan=4>��������</TH>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%" nowrap>
			�ӿڣ�
		</td>
		<td class=column nowrap>
			<span id="divPingInterface" style="display:">
				<select name="Interface" class="bk">
					<option value="-1">
						=���ȡ�ӿ�=
					</option>
				</select> </span> &nbsp;&nbsp;
			<input type="button" value=" �� ȡ " onclick="getPingInterface()">
		</td>
		<td align="right" width="12%" nowrap>����С��
		</td>
		<td class=column><input type="text" name="DataBlockSize"
			class="bk" size="16"  value="32">(byte) 
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%" nowrap>
		  ����IP��
		</td>
		<td class=column>
			<input type="text" name="Host" value="" class="bk">(����202.102.24.35) 
		</td>
		<td align="right" width="12%" nowrap>
		  ����Ŀ��
		</td>
		<td class=column><input type="text" name="NumberOfRepetions"
			class="bk" size="16"  value="2"></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%" nowrap>
		  ��ʱʱ�䣺
		</td>
		<td class=column colspan="3"><input type="text" name="Timeout" class="bk"
			size="16" value="2000">(ms)</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="4" class="green_foot">
		<div align="right">
			<INPUT TYPE="button" value=" �� �� " class=btn onclick="ExecMod('PING')">
		</div>
		</td>
	</tr>
	<TR bgcolor="#FFFFFF">
		<td colspan="4" valign="top" class=column><div id="div_PING">
			</div>
		</td>
	</TR>
</TABLE>
<TABLE id="ATM_table" border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999" style="display:none">
	<tr bgcolor="#FFFFFF">
		<td colspan="10"><img src="../images/attention_2.gif" width="15"
			height="12">&nbsp;<font color='red'>˵�����ɹ����ɼ������������·����ʧ�ܣ������豸�������豸��ͨ��</font>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<TH colspan=4>��·�����</TH>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%">
			WAN���ӣ�
		</td>
		<td class=column nowrap colspan="3">
			<span id="divATMF5Interface"> 
				<select name="ATMF5" class="bk">
					<option value="-1">=���ȡWAN����=</option>
				</select> 
			</span>
			&nbsp;&nbsp;<input type="button" value="�� ȡ" onclick="getInterfaceAct()">
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%">
		  ��ʱʱ�䣺
		</td>
		<td class=column><input type="text" name="AMF_Timeout" class="bk" size="16" value="2000">(ms)</td>
		<td align="right" width="12%">
		  ����Ŀ��
		</td>
		<td class=column><input type="text" name="AMF_NumberOfRepetions" class="bk" size="16" value="2"></td>
	</tr>
		<tr bgcolor="#FFFFFF">
		<td colspan="4" class="blue_foot">
		<div align="right">
			<INPUT TYPE="button" value=" �� �� " class=btn onclick="ExecMod('ATMF')"> 
		</div>
		</td>
	</tr>
	<TR bgcolor="#FFFFFF">
		<td colspan="4" valign="top" class=column>
		<div id="div_ATMF"></div>
		</td>
	</TR>	
</TABLE>
<TABLE id="DSL_table" border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999" style="display:none">
	<tr bgcolor="#FFFFFF">
		<td colspan=""><img src="../images/attention_2.gif" width="15"
			height="12">&nbsp;<font color='red'>˵�������������·</font>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<TH colspan=4>������·���</TH>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="4" class="blue_foot">
		<div align="right">
			<INPUT TYPE="button" value=" �� �� " class=btn onclick="ExecMod('DSL')">
		</div>
		</td>
	</tr>
	<TR bgcolor="#FFFFFF">
		<td colspan="4" valign="top" class=column>
		<div id="div_DSL"></div>
		</td>
	</TR>
</TABLE>
<iframe name="childFrm" id="childFrm" style="display:none"></iframe>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<%=request.getParameter("gw_type")%>';

function ExecMod(type){
	var device_id = document.frm.device_id.value;
	var page = null;
	if(type == "PING"){
		if(!CheckParamOfPING()) return ;
		page = "jt_device_zendan_from1_save.jsp?device_id=" +device_id+ "&gw_type="+gw_type+"&devicetype=tr069&Interface=" +document.all("Interface").value+ "&Host=" +document.all("Host").value+ "&NumberOfRepetitions=" +document.all("NumberOfRepetions").value+ "&Timeout=" +document.all("Timeout").value+ "&DataBlockSize="  + document.all("DataBlockSize").value;
		document.getElementById("div_PING").innerHTML = "����������Ͻ���������ĵȴ�....";
	}else if(type == "ATMF"){
		if(!CheckParamOfAMF()) return ;
		page = "jt_device_zendan_from2_save.jsp?device_id=" +device_id+ "&gw_type="+gw_type+"&NumberOfRepetions=" +document.all("AMF_NumberOfRepetions").value+ "&Timeout=" +document.all("Timeout").value + "&ATMF5="+document.all("ATMF5").value;
		document.getElementById("div_ATMF").innerHTML = "����������Ͻ���������ĵȴ�....";
	}else if(type == "DSL"){
		if(device_id == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		page = "jt_device_zendan_from5_save.jsp?device_id=" + device_id+"&gw_type="+gw_type;
		document.getElementById("div_DSL").innerHTML = "����������Ͻ���������ĵȴ�....";
	}
	document.getElementById("childFrm").src = page;
}
function setDevice(_device_id,_gather_id,_info){
	device_id = _device_id;
	gather_id = _gather_id;
	if(_device_id == ""){
		document.frm.device_id.value = "";
		document.getElementById("div_DeviceInfo").innerHTML = "��ǰ�û��޹����豸��";
	}else{
		document.frm.device_id.value = _device_id;
		document.getElementById("div_DeviceInfo").innerHTML = "<font color=blue>" + _info + "</font>";
	}
}
function CheckForm(){
	if(document.getElementById("UserName").value == ""){
		alert("�������û���/�ʺ�");
		return false;
	}
	return true;
}
function CheckParamOfPING(){
	var rule = /^[0-9]*[1-9][0-9]*$/;//������ʽ��/��/֮��    	
	var obj = document.all;
	if(obj("Interface").value == -1){
		alert("��ѡ��ӿ�!");
		obj("Interface").focus();
		return false;
	} else if(!IsNull(obj("DataBlockSize").value,'����С')){
		obj("DataBlockSize").focus();
		return false;
	}
	else if(!rule.test(obj("DataBlockSize").value)){
		alert("����С ���������0������");
        return false;
    }
    else  if(!IsNull(obj("Host").value,'����IP')){
		obj("Host").focus();
		return false;
	}		
    else if(Trim(obj("Host").value)!="" && !IsIPAddr(obj("Host").value)){
		obj("Host").focus();
		return false;
	}
	else if(!IsNull(obj("NumberOfRepetions").value,'NumberOfRepetions')){
			obj("NumberOfRepetions").focus();
			return false;
	}
	else if(!rule.test(obj("NumberOfRepetions").value)){
          alert("NumberOfRepetions  ���������0������");
          return false;
    }
    else  if(!IsNull(obj("Timeout").value,'��ʱʱ��')){
		obj("Timeout").focus();
		return false;
	}
	else if(obj("Timeout").value!="" && !IsNumber(obj("Timeout").value,"Timeout")){	
		obj("Timeout").focus();
		obj("Timeout").select();
		return false;
	}
	return true;
}
function CheckParamOfAMF(){
	var rule = /^[0-9]*[1-9][0-9]*$/;//������ʽ��/��/֮��    	
	var obj = document.all;
	if(obj("ATMF5").value == -1){
		alert("��ѡ��WAN����!");
		return false;
	}
	if(!IsNull(obj("AMF_NumberOfRepetions").value,'AMF_NumberOfRepetions')){
		obj("AMF_NumberOfRepetions").focus();
		return false;
	}	
	  else if(!rule.test(obj("AMF_NumberOfRepetions").value)){
	  alert("AMF_NumberOfRepetions  ���������0������");
	  return false;
	}
	 else  if(!IsNull(obj("AMF_Timeout").value,'��ʱʱ��')){
		obj("AMF_Timeout").focus();
		return false;
	}
	else if(obj("AMF_Timeout").value!="" && !IsNumber(obj("AMF_Timeout").value,"��ʱʱ��")){	
		obj("AMF_Timeout").focus();
		obj("AMF_Timeout").select();
		return false;
	}	
	return true;
}
function getPingInterface(){
	var _device_id = document.frm.device_id.value;

	if(_device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
	var page = "getPingInterface.jsp";
	page += "?device_id="+_device_id;
	page += "&gw_type="+gw_type;
	page += "&TT="+new Date();
	document.getElementById("childFrm").src = page;
	//window.open(page);
}
function setPingInterface(html){
	if(html == ""){
			alert("��ȡʧ��,�豸�������ӻ��豸��æ!");
	}else{
			document.getElementById("divPingInterface").innerHTML = html;
	}
}
function getInterfaceAct(){
	var _device_id =  document.frm.device_id.value;
	if(_device_id == ""){
		alert("���Ȳ�ѯ�豸!");
		return false;
	}
	getATMInterface(_device_id);
}
function getATMInterface(_device_id){
		var page = "getATMF5LOOPInterface.jsp";
		page += "?device_id="+_device_id;
		page += "&TT="+new Date();
		document.all("childFrm").src = page;
}

function startATM() {
	document.all.ATM_table.style.display = "";
}

function startDSL() {
	document.all.DSL_table.style.display = "";
}

</SCRIPT>
<%@ include file="../foot.jsp"%>