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
			设备诊断
		</td>
	</tr>
	
</table>
<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
	<TR bgcolor="#FFFFFF">
		<TH colspan="4">设备诊断</TH>
	</TR>
	<tr bgcolor="#FFFFFF">
		<td nowrap class=column width="12%">
			<div align="right">用户账号：</div>
		</td>
		<td class=column  width="35%">
			<input type="text" name="UserName" id="UserName" class="bk">&nbsp;<font color=red>*</font>&nbsp;
			<input name="" type=submit value=" 查 询 " title="查询出用户关联的设备">
		</td>
		<td nowrap class=column  width="15%">
			<div align="right">用户关联的设备：</div>
		</td>
		<td class=column  width="38%">
			<div id="div_DeviceInfo">请根据用户账号查询</div>
			<input type="hidden" name="device_id" value="">
		</td>
	</tr>	
</TABLE>
</form>
<br>

<TABLE border=0 cellspacing=1 cellpadding=2 width="95%" align=center valign=middle bgcolor="#999999">
	<tr bgcolor="#FFFFFF">
		<td colspan="10"><img src="../images/attention_2.gif" width="15"
			height="12">&nbsp;<font color='red'>说明：成功（可继续检测链路层）； 失败（网关设备和外网不通）</font>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<TH colspan=4>网络层诊断</TH>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%" nowrap>
			接口：
		</td>
		<td class=column nowrap>
			<span id="divPingInterface" style="display:">
				<select name="Interface" class="bk">
					<option value="-1">
						=请获取接口=
					</option>
				</select> </span> &nbsp;&nbsp;
			<input type="button" value=" 获 取 " onclick="getPingInterface()">
		</td>
		<td align="right" width="12%" nowrap>包大小：
		</td>
		<td class=column><input type="text" name="DataBlockSize"
			class="bk" size="16"  value="32">(byte) 
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%" nowrap>
		  测试IP：
		</td>
		<td class=column>
			<input type="text" name="Host" value="" class="bk">(例：202.102.24.35) 
		</td>
		<td align="right" width="12%" nowrap>
		  包数目：
		</td>
		<td class=column><input type="text" name="NumberOfRepetions"
			class="bk" size="16"  value="2"></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%" nowrap>
		  超时时间：
		</td>
		<td class=column colspan="3"><input type="text" name="Timeout" class="bk"
			size="16" value="2000">(ms)</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="4" class="green_foot">
		<div align="right">
			<INPUT TYPE="button" value=" 诊 断 " class=btn onclick="ExecMod('PING')">
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
			height="12">&nbsp;<font color='red'>说明：成功（可继续检测物理线路）；失败（网关设备和上行设备不通）</font>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<TH colspan=4>链路层测试</TH>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%">
			WAN连接：
		</td>
		<td class=column nowrap colspan="3">
			<span id="divATMF5Interface"> 
				<select name="ATMF5" class="bk">
					<option value="-1">=请获取WAN连接=</option>
				</select> 
			</span>
			&nbsp;&nbsp;<input type="button" value="获 取" onclick="getInterfaceAct()">
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" width="12%">
		  超时时间：
		</td>
		<td class=column><input type="text" name="AMF_Timeout" class="bk" size="16" value="2000">(ms)</td>
		<td align="right" width="12%">
		  包数目：
		</td>
		<td class=column><input type="text" name="AMF_NumberOfRepetions" class="bk" size="16" value="2"></td>
	</tr>
		<tr bgcolor="#FFFFFF">
		<td colspan="4" class="blue_foot">
		<div align="right">
			<INPUT TYPE="button" value=" 诊 断 " class=btn onclick="ExecMod('ATMF')"> 
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
			height="12">&nbsp;<font color='red'>说明：检测物理线路</font>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<TH colspan=4>物理线路检测</TH>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="4" class="blue_foot">
		<div align="right">
			<INPUT TYPE="button" value=" 诊 断 " class=btn onclick="ExecMod('DSL')">
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
		document.getElementById("div_PING").innerHTML = "正在载入诊断结果，请耐心等待....";
	}else if(type == "ATMF"){
		if(!CheckParamOfAMF()) return ;
		page = "jt_device_zendan_from2_save.jsp?device_id=" +device_id+ "&gw_type="+gw_type+"&NumberOfRepetions=" +document.all("AMF_NumberOfRepetions").value+ "&Timeout=" +document.all("Timeout").value + "&ATMF5="+document.all("ATMF5").value;
		document.getElementById("div_ATMF").innerHTML = "正在载入诊断结果，请耐心等待....";
	}else if(type == "DSL"){
		if(device_id == ""){
			alert("请先查询设备!");
			return false;
		}
		page = "jt_device_zendan_from5_save.jsp?device_id=" + device_id+"&gw_type="+gw_type;
		document.getElementById("div_DSL").innerHTML = "正在载入诊断结果，请耐心等待....";
	}
	document.getElementById("childFrm").src = page;
}
function setDevice(_device_id,_gather_id,_info){
	device_id = _device_id;
	gather_id = _gather_id;
	if(_device_id == ""){
		document.frm.device_id.value = "";
		document.getElementById("div_DeviceInfo").innerHTML = "当前用户无关联设备！";
	}else{
		document.frm.device_id.value = _device_id;
		document.getElementById("div_DeviceInfo").innerHTML = "<font color=blue>" + _info + "</font>";
	}
}
function CheckForm(){
	if(document.getElementById("UserName").value == ""){
		alert("请输入用户名/帐号");
		return false;
	}
	return true;
}
function CheckParamOfPING(){
	var rule = /^[0-9]*[1-9][0-9]*$/;//正则表达式在/与/之间    	
	var obj = document.all;
	if(obj("Interface").value == -1){
		alert("请选择接口!");
		obj("Interface").focus();
		return false;
	} else if(!IsNull(obj("DataBlockSize").value,'包大小')){
		obj("DataBlockSize").focus();
		return false;
	}
	else if(!rule.test(obj("DataBlockSize").value)){
		alert("包大小 请输入大于0的整数");
        return false;
    }
    else  if(!IsNull(obj("Host").value,'测试IP')){
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
          alert("NumberOfRepetions  请输入大于0的整数");
          return false;
    }
    else  if(!IsNull(obj("Timeout").value,'超时时间')){
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
	var rule = /^[0-9]*[1-9][0-9]*$/;//正则表达式在/与/之间    	
	var obj = document.all;
	if(obj("ATMF5").value == -1){
		alert("请选择WAN连接!");
		return false;
	}
	if(!IsNull(obj("AMF_NumberOfRepetions").value,'AMF_NumberOfRepetions')){
		obj("AMF_NumberOfRepetions").focus();
		return false;
	}	
	  else if(!rule.test(obj("AMF_NumberOfRepetions").value)){
	  alert("AMF_NumberOfRepetions  请输入大于0的整数");
	  return false;
	}
	 else  if(!IsNull(obj("AMF_Timeout").value,'超时时间')){
		obj("AMF_Timeout").focus();
		return false;
	}
	else if(obj("AMF_Timeout").value!="" && !IsNumber(obj("AMF_Timeout").value,"超时时间")){	
		obj("AMF_Timeout").focus();
		obj("AMF_Timeout").select();
		return false;
	}	
	return true;
}
function getPingInterface(){
	var _device_id = document.frm.device_id.value;

	if(_device_id == ""){
		alert("请先查询设备!");
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
			alert("获取失败,设备不能连接或设备正忙!");
	}else{
			document.getElementById("divPingInterface").innerHTML = html;
	}
}
function getInterfaceAct(){
	var _device_id =  document.frm.device_id.value;
	if(_device_id == ""){
		alert("请先查询设备!");
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