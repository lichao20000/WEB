<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.resource.FileSevice"%>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
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
		page = "showDevice.jsp?gather_id="+document.frm.gather_id.value +"&vendor_id="+document.frm.vendor_id.value+ "&devicetype_id="+document.frm.softwareversion.value+"&flag=paramInstanceadd_Config&refresh="+Math.random();
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
									参数实例管理
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									配置终端的时间服务器。
									<input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>根据设备版本查询&nbsp;&nbsp;
			                        <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">根据用户查询设备&nbsp;&nbsp;
			                        <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType">根据设备序列号查询
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													NTP 配置
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
											<TR bgcolor="#FFFFFF" id="ntp_type" style="display:">
											    <TD align="right">
													配置方式:
												</TD>
												<TD colspan=3>
													<input type="radio" name="type" id="rd1" class=btn><label for="rd1">TR069</label>&nbsp;
													<input type="radio" name="type" checked id="rd2" class=btn><label for="rd2">SNMP</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_status" style="display:none">
											    <TD align="right">
													服务状态:
												</TD>
												<TD colspan=3>
													<input type="radio" name="status" checked id="rd3" onclick="changeStatus(this);" class=btn><label for="rd3">开启</label>&nbsp;
													<input type="radio" name="status" id="rd4" onclick="changeStatus(this);" class=btn><label for="rd4">关闭</label>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_1st" style="display:none">
											    <TD align="right">
													主 NTP 服务器地址或域名:
												</TD>
												<TD colspan=3>
													<input type="text" name="main_ntp_server" class=bk>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="ntp_2nd" style="display:none">
											    <TD align="right">
													备 NTP 服务器地址或域名:
												</TD>
												<TD colspan=3>
													<input type="text" name="second_ntp_server" class=bk>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="bt_set" style="display:none" onclick="ExecMod();" value=" 设 置 " class=btn >
													<INPUT TYPE="button" id="bt_get" onclick="getStatus();" value=" 获 取 " class=btn >
												</TD>
											</TR>

					<TR bgcolor="#FFFFFF" id="tr001" style="display:none">
						<TH colspan="4">操作结果</TH>
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
<script language="javascript">
<%if ("0".equals(checkType)){%>
	document.frm.gather_id.value = "<%=gather_id%>";
	document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	document.frm.vendor_id.value = "<%=vendor_id%>";
	
	page = "showDevicetype.jsp?vendor_id=<%=vendor_id%>&defaultValue=<%=devicetype_id%>";
	document.all("childFrm1").src = page;
	page = "showDeviceVersion.jsp?vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&defaultValue=<%=softwareversion%>";
	document.all("childFrm2").src = page;
	page = "showDevice.jsp?gather_id=<%=gather_id%>&vendor_id=<%=vendor_id%>&devicetype_id=<%=softwareversion%>&flag=paramInstanceadd_Config&refresh="+Math.random();
	document.all("childFrm").src = page;
<%}else if("1".equals(checkType)){%>
    document.all("telephone").value="<%=telephone%>";
    document.all("hguser").value="<%=hguser%>";
    selectHgw();
    ShowDialog(1);
    relateDevice();  
<%}%>
</script>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//给String类增加trim方法，过滤空格
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
		        page = "NTPConfigStatus.jsp?device_id=" +did+ "&oid_type=1" + "&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&refresh=" + new Date().getTime();
				document.all("div_ping").innerHTML = "正在获取设备NTP状态，请耐心等待....";
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
		        page = "NTPConfigSave.jsp?device_id=" +did+ "&oid_type=1" + "&main_ntp_server=" + document.forms[0].main_ntp_server.value + "&second_ntp_server=" + document.forms[0].second_ntp_server.value + "&type=" + (document.getElementById("rd1").checked ? "1" : "2") + "&status=" + (document.getElementById("rd3").checked ? "1" : "2") + "&refresh=" + new Date().getTime();
//				alert(page);
				document.all("div_ping").innerHTML = "正在载入诊断结果，请耐心等待....";
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
			if (m.Trim() == "" && s.Trim() == "") {
				alert("请至少配置一个服务器地址或域名");
				return false;
			}
			if (m == s) {
				alert("请配置不同的服务器地址或域名");
				return false;
			}
		}
		return true;
	}

	function setStatus(st, ms, ss) {

		document.getElementById("ntp_status").style.display = "";
		//document.getElementById("ntp_type").style.display = "";
		document.getElementById("ntp_1st").style.display = "";		
		document.getElementById("ntp_2nd").style.display = "";

		document.getElementById("bt_set").style.display = "";
		
		var sHtml = "当前服务状态：" + (st == 1 ? '启用' : '禁用');
		if (st == 1) {
			sHtml += "<br>主 NTP 服务器地址或域名：" + ms;
			sHtml += "<br>备 NTP 服务器地址或域名：" + ss;
		}
		document.all("div_ping").innerHTML = sHtml;

		if (st == 1) {
			document.getElementById("rd3").checked = true;
			document.forms[0].main_ntp_server.disabled = false;
			document.forms[0].second_ntp_server.disabled = false;
			document.getElementById("ntp_1st").style.display = "";
			document.getElementById("ntp_2nd").style.display = "";
			document.forms[0].main_ntp_server.value = ms;
			document.forms[0].second_ntp_server.value = ss;
		} else {
			document.getElementById("rd4").checked = true;
			document.forms[0].main_ntp_server.disabled = true;
			document.forms[0].second_ntp_server.disabled = true;
			document.getElementById("ntp_1st").style.display = "none";
			document.getElementById("ntp_2nd").style.display = "none";
		}
	}

	function changeStatus(o) {
		if (o.id == "rd3") {
			document.forms[0].main_ntp_server.disabled = false;
			document.forms[0].second_ntp_server.disabled = false;
			document.getElementById("ntp_1st").style.display = "";
			document.getElementById("ntp_2nd").style.display = "";
		} else {
			document.forms[0].main_ntp_server.disabled = true;
			document.forms[0].second_ntp_server.disabled = true;
			document.getElementById("ntp_1st").style.display = "none";
			document.getElementById("ntp_2nd").style.display = "none";
		}
	}
//-->
</SCRIPT>