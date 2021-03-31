<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.resource.DeviceAct"%>

<%
request.setCharacterEncoding("GBK");
DeviceAct deviceAct = new DeviceAct();
String device_id = request.getParameter("device_id");
int int_flag  = 0;

if (device_id != null) {
	//测试连接  成功：1 失败： 0
	//FileSevice  fileSevice = new  FileSevice();
	//int_flag  = fileSevice.testConnection(request);
}

String gather_id = request.getParameter("gather_id");
String vendor_id = request.getParameter("vendor_id");
String devicetype_id = request.getParameter("devicetype_id");
String softwareversion = request.getParameter("softwareversion");

String checkType = request.getParameter("checkType");
String hguser = request.getParameter("hguser");
if(null==hguser)
{
	hguser ="";
}
String telephone = request.getParameter("telephone");
if(null==telephone)
{
	telephone ="";
}

//采集点
String gatherList = deviceAct.getGatherList(session, "", "", true);

//设备厂商
String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
var did;
var device_id ="<%=device_id%>";

function CheckForm(){
	var hguser=document.all("hguser").value;
    var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
    var checkType = "";
    for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    checkType = checkradios[i].value;
	    break;
	  }
    }
	if(checkType==0&&document.frm.gather_id.value == -1){
		alert("请选择采集点！");
		document.frm.gather_id.focus();
		return false;
  	}
  	 if(checkType==0&&document.frm.vendor_id.value == -1){
		alert("请选择厂商！");
		document.frm.vendor_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.devicetype_id.value == -1){
		alert("请选择设备型号！");
		document.frm.devicetype_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.softwareversion.value == -1){
			alert("请选择设备版本！");
			document.frm.softwareversion.focus();
			return false;
		}
		
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("请填写用户名或电话号码！");
	  document.all("hguser").focus();
	  return false;
	}
 	
 	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("请选择设备！");
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
			did = oselect[i].value;
			  num++;
			}
		}

 	}
 	if(num ==0){
		alert("请选择设备！");
		return false;
	}
	return true;
}

function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	}
	if(param == "devicetype_id"){
			// document.frm.device.checked = false;
			page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&devicetype_id="+ encodeURIComponent(document.frm.devicetype_id.value);
			document.all("childFrm2").src = page;
		}
	if(param == "softwareversion")
	{		
		page = "showDevice.jsp?gather_id="+document.frm.gather_id.value +"&vendor_id="+document.frm.vendor_id.value+ "&devicetype_id="+document.frm.softwareversion.value+"&flag=paramInstanceadd_Config&gw_type=2&refresh="+Math.random();
		document.all("childFrm").src = page;
	}
	if(param == "vendor_id")
	{
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;	
	}
}

function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("请填写用户名或电话号码！");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="showDevice.jsp?hguser="+hguser+"&telephone="+telephone+"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}

function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(checkType==2 && serialnumber=="")
   {
      alert("请填写设备序列号！");
      document.all("serialnumber").focus();
   }
   else if(checkType==2)
   {
      var page="";
      page="showDevice.jsp?serialnumber="+serialnumber+"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}

function ShowDialog(param)
{
  //根据用户来查询
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
  }
  
  //根据设备版本来查询
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
  }
  
  //根据设备序列号来查询
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
  }
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: 宋体"></span>
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
			<FORM NAME="frm" METHOD="POST" ACTION="ConfigNamePwSave.jsp" onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											参数配置
										</div>
									</td>
									<td><img src="../images/attention_2.gif" width="15"
										height="12">设置宽带账号等信息。
									<input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>根据设备版本查询&nbsp;&nbsp;
			                        <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">根据用户查询设备&nbsp;&nbsp;
			                        <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">根据设备序列号查询
									</td>

								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<TH colspan="4">
										宽带账号信息配置
									</TH>
								</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
											    <TD align="right" width="20%">
													采集点:
												</TD>
												<TD align="left" width="30%">
												    <%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													厂商:
												</TD>
												<TD align="left" width="30%">
													<div id="div_vendor">
														<select name="vendor" class="bk">
															<option value="-1">
																--请先选择采集点--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">
											    <TD align="right" width="20%">
													设备型号:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name=devicetype_id class="bk">
															<option value="-1">
																--请先选择厂商--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													设备版本:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--请先选择设备型号--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="20%">
													用户名:
												</TD>
												<TD width="30%">
													<input type="text" name="hguser" value="" class=bk>
												</TD>
												<TD align="right" width="20%">
													用户电话号码:
												</TD>
												<TD width="30%">
													<input type="text" name="telephone" value="" class=bk>
													<input type="button" class=btn value="查询" onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
											    <TD align="right" width="20%">
													设备序列号:
												</TD>
												<TD width="30%">
													<input type="text" name="serialnumber" value="" class=bk>
												</TD>
												<TD colspan=2>
													<input type="button" class=btn value="查询" onclick="relateDeviceBySerialno()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													设备列表:
													<br>
													<!-- 
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													全选
													-->
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>请选择设备！</span>
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="wlan_type" style="display:none">
											    <TD align="right">
													配置方式:
												</TD>
												<TD colspan=3>
													<input type="radio" name="type" id="rd1" class=btn><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" checked id="rd2" class=btn><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="wlan_status" style="display:none">
											    <TD align="right">
													服务状态:
												</TD>
												<TD colspan=3>
													<input type="radio" name="status" checked id="rd3" class=btn><label for="rd3">开启</label>&nbsp;
													<input type="radio" name="status" id="rd4" class=btn><label for="rd4">关闭</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="wlan_model" style="display:none">
											    <TD align="right">
													网络模式:
												</TD>
												<TD colspan=3>
													<input type="radio" name="model" id="rd5" class=btn><label for="rd5">Dot11b</label>&nbsp;
													<input type="radio" name="model" checked id="rd6" class=btn><label for="rd6">Dot11g</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="wlan_channel" style="display:none">
											    <TD align="right">
													无线信道:
												</TD>
												<TD colspan=3>
													<input type="text" name="channel" id="rd7" value="0">(取值 0~13， 0代表自动选择)
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="wlan_power" style="display:none">
											    <TD align="right">
													发射功率:
												</TD>
												<TD colspan=3>
													<input type="text" name="power" id="rd7" value="20">(取值 1~20，默认为20)
												</TD>
											</TR>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" id="bt_set" style="display:none" onclick="ExecMod();" value=" 设 置 " class=btn >
													<INPUT TYPE="submit" id="bt_get" value=" 获 取 " class=btn >
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								
					<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
						<TH colspan="4">操作结果</TH>
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

	