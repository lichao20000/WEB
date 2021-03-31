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
	FileSevice  fileSevice = new  FileSevice();
	int_flag  = fileSevice.testConnection(request);
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
	var custname=document.forms[0].custname.value;
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
		
	if(checkType==1&&""==custname)
	{
	  alert("请填写客户名！");
	  document.forms[0].custname.focus();
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
		var s = DateToDes(document.frm.start.value);
		//alert(s);
		var e = DateToDes(document.frm.end.value);
		//alert(e);

		/*if(!IsNull(document.frm.start.value,"开始时间")){
			document.frm.start.focus();
			document.frm.start.select();
			return false;
		}
		else if(!IsNull(document.frm.end.value,"结束时间")){
			document.frm.end.focus();
			document.frm.end.select();
			return false;
		}*/
		if (!IsNull(document.frm.start.value,"开始时间") || !IsNull(document.frm.end.value,"结束时间")) {
			alert("请选择起始时间！");
			return false;
		}
		if(!IsNull(document.frm.start.value,"开始时间")){
			if(!IsNull(document.frm.end.value,"结束时间")){
				document.frm.startTime.value = "";
				document.frm.endTime.value = "";
				return true;
			} else {
				document.frm.startTime.value = "";
				document.frm.endTime.value = e;
				//alert(document.frm.endTime.value);
				return true;
			}
		} else {
			if(!IsNull(document.frm.end.value,"结束时间")){
				document.frm.startTime.value = s;
				document.frm.endTime.value = "";
				//alert(document.frm.startTime.value);
				return true;
			} else if(s>e){
					alert("开始时间不能大于结束时间");
					return false;
				} else {
					document.frm.startTime.value = s;
					document.frm.endTime.value = e;
					//alert(document.frm.startTime.value);
					//alert(document.frm.endTime.value);
					return true;
				}
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
	if(param == "device_model_id"){
			// document.frm.device.checked = false;
			page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&device_model_id="+ encodeURIComponent(document.frm.device_model_id.value);
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
   var custname=document.forms[0].custname.value;
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(""==custname)
   {
      alert("请填写客户名！");
      document.forms[0].custname.focus();
   } else {
      var page="";
      page="showDevice.jsp?custname="+custname+"&flag=paramInstanceadd_Config&refresh="+Math.random();
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
  //根据客户来查询
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
	document.forms[0].query_type.value="2";
  }
  
  //根据设备版本来查询
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
	 document.forms[0].query_type.value="1";
  }
  
  //根据设备序列号来查询
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
	 document.forms[0].query_type.value="1";
  }
}
</SCRIPT>
<style type='text/css'>
<!--
BODY.bd {
	BACKGROUND-COLOR: #ffffff;
	COLOR: #000000;
	FONT-FAMILY: '宋体', 'Arial';
	FONT-SIZE: 12px;
	MARGIN: 0px
}
TH.thh {
	background-color: #B0E0E6;
	font-size: 9pt;
	color: #000000;
	text-decoration: none;
	font-weight: bold;
	line-height: 22px;
	text-align: center;
}
TR.trr {
	background-color:'#FFFFFF';
}
TD.tdd {
	FONT-FAMILY: '宋体', 'Tahoma';
	FONT-SIZE: 12px;
	background-color:'#FFFFFF';
}
TD.hd {
	background-color:'#EEE8AA';
}
-->
</style>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
			<input type="hidden" name="query_type" value="1"/>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									运行报告生成
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									实时生成运行报告。
									<!-- <input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>高级查询&nbsp;&nbsp;  -->
			                        <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">按用户&nbsp;&nbsp;
			                        <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType" checked>按设备
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
													设备查询
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:none">
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
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:none">
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
													客户名
												</TD>
												<TD width="30%">
													<input type="text" name="custname" value="" class=bk>
												</TD>
												<TD colspan="2">
													<input type="button" class=btn value="查询" onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:">
											    <TD align="right" width="20%" class="column">
													设备序列号
												</TD>
												<TD width="30%">
													<input type="text" name="serialnumber" value="" class=bk>
												</TD>
												<TD colspan=2>
													<input type="button" class=btn value="查询" onclick="relateDeviceBySerialno()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" class="column">
													设备列表
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
											<TR bgcolor="#FFFFFF" id="tr_filelist" STYLE="display:none">
											    <TD align="right">
													未发送报表文件列表:
												</TD>
												<TD>
													<span id="id_filelist"></spna>
												</TD>
												<TD colspan="2" align="left">
													<input type="button" class=btn value="查看报表文件内容" onclick="viewFile()">
													<input type="button" class=btn value="发送报表" onclick="sendFile()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
											    <TD align="right" class="column">
													开始时间
												</TD>
												<TD align="left">
													<INPUT type="text" name="start" size="20" >
													<input type="button" value="▼" onClick="showCalendar('all')">
													<INPUT TYPE="hidden" name="startTime">
												</TD>
												<TD align="right" class="column">
													结束时间
												</TD>
												<TD align="left">
        											<input type="text" name="end" size="20" >
													<input type="button" value="▼" onClick="showCalendar('all')">
													<INPUT TYPE="hidden" name="endTime">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" STYLE="display:none">
											    <TD align="right">
													生成后直接发送
												</TD>
												<TD align="left" colspan="3">
													<INPUT type="checkbox" checked name="isSend"/>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" id="buildSend" onclick="buildAndSend()" value=" 保 存 " class=btn >&nbsp;&nbsp;
													<INPUT TYPE="reset" VALUE=" 取 消 ">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr_filecontext" STYLE="display:none">
												<TD colspan="3">
													<span id="id_filecontext"></span>
												</TD>
												<TD align="left">
													专家意见<TEXTAREA name="suggestion" cols="15" rows="4"></TEXTAREA>
													<input type="button" class="btn" value="保存" onclick="saveSuggestion()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" style="display:none">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" onclick="getReportList()" value="查找报表" class=btn >
													<INPUT TYPE="button" id="realTimeRep" onclick="getRealTimeReport()" value="生成实时数据" class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="id_newreport" style="display:none">
												<TD colspan="4">
													<input type="checkbox" name="data_type" id="dt_1">终端
													<input type="checkbox" name="data_type" id="dt_2">用户
													<input type="checkbox" name="data_type" id="dt_3">客户
													<input type="checkbox" name="data_type" id="dt_4">在线信息
													<input type="checkbox" name="data_type" id="dt_5">WAN&LAN
													<input type="checkbox" name="data_type" id="dt_6">告警
													<input type="checkbox" name="data_type" id="dt_7">流量
													<input type="checkbox" name="data_type" id="dt_8">病毒
													&nbsp;(请选择好定制的内容后点击提交)
												</TD>
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
function IsNull(strValue,strMsg){
	//alert(Trim(strValue).length);
	if(Trim(strValue).length>0) return true;
	else{
		//alert(strMsg+'不能为空');
		return false;
	}
}

function Trim(strValue){
	var v = strValue;
	var i = 0;
	while(i<v.length){
	  if(v.substring(i,i+1)!=' '){
		v = v.substring(i,v.length) 
		break;
	  }
	  i = i + 1;
	  if(i==v.length){
        v="";
      }
	}

	i = v.length;
	while(i>0){
	  if(v.substring(i-1,i)!=' '){
	    v = v.substring(0,i);
		break;
	  }	
	  i = i - 1;
	}

	return v;
}
function DateToDes(v){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf(" ");
		if(pos != -1){
			d = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		dt = new Date(m+"/"+d+"/"+y+" "+v);
		var s  = dt.getTime();
		return s;
	}
	else
		return 0;
}
	function buildAndSend() {
		if (!CheckForm()) return;
		//alert(document.forms[0].startTime.value);
  		page = "running_report_build_save.jsp?device_id=" + did + "&query_type=" + document.forms[0].query_type.value + "&startTime=" + document.forms[0].startTime.value + "&endTime=" + document.forms[0].endTime.value + "&isSend=" + (document.forms[0].isSend.checked ? "1" : "0") + "&action_type=7&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}
	function getReportList() {
		if (!CheckForm()) return;
		//alert(did);
  		page = "running_report_build_save.jsp?device_id=" + did + "&action_type=1&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}

	function sendFile() {
		var file_path = document.forms[0].fileList.value ;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=4&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}

	function viewFile() {
		var file_path = document.forms[0].fileList.value ;
		//alert(file_path);
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=2&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}
	
	function getRealTimeReport() {
		showId("id_newreport");
		document.getElementById("realTimeRep").value = "生成实时数据(提交)";
		document.getElementById("realTimeRep").onclick = getRealTimeReportSave;
	}
	function getRealTimeReportSave() {
		document.getElementById("realTimeRep").value = "生成实时数据(提交)";
		document.getElementById("realTimeRep").onclick = getRealTimeReportSave;
		if (!CheckForm()) return;
		var str = "";
		str += (document.getElementById("dt_1").checked ? "1" : "0") + "," + (document.getElementById("dt_2").checked ? "1" : "0") + "," + (document.getElementById("dt_3").checked ? "1" : "0") + "," + (document.getElementById("dt_4").checked ? "1" : "0") + "," + (document.getElementById("dt_5").checked ? "1" : "0") + "," + (document.getElementById("dt_6").checked ? "1" : "0") + "," + (document.getElementById("dt_7").checked ? "1" : "0") + "," + (document.getElementById("dt_8").checked ? "1" : "0");
		var gather_time = new Date().getTime();
		//alert(str);
  		page = "running_report_build_save.jsp?device_id=" + did + "&param=" + str + "&gather_time=" + gather_time + "&action_type=5&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}

	function saveSuggestion() {
		var file_path = document.forms[0].fileList.value ;
		var suggestion = document.forms[0].suggestion.value;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&suggestion=" + suggestion + "&action_type=3&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}

	function setStatus(type, suggestion) {
		if (type == 1) {
		} else if (type == 2) {
			//alert(ret);
			//setId("id_filecontext", ret);
			document.forms[0].suggestion.value = suggestion;
			showId("tr_filecontext");
		}
	}

	function setId(id, msg) {
		document.getElementById(id).innerHTML = msg;
	}

	function showId(id) {
		document.getElementById(id).style.display = "";
	}

	function hideId(id) {
		document.getElementById(id).style.display = "none";
	}
//-->
</SCRIPT>