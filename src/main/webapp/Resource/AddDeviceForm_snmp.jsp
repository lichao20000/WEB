<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.netcutover.SearchSheet"%>
<%@page import="com.linkage.litms.common.database.*,java.util.*"%>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="AdslAct" scope="request"
	class="com.linkage.litms.resource.AdslAct" />
<jsp:useBean id="roleManage" scope="request" 
class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/>
<%
	request.setCharacterEncoding("GBK");
	
	int GwProtocol = LipossGlobals.getGwProtocol();	
	
	//采集点
	String strAdslList = AdslAct.getGatherInfoForm(false, "", "", request);
	//设备厂商
	String strVendorList = DeviceAct.getVendorList(true, "", "");
	
	//
	String strModelList = "<SELECT NAME=device_model CLASS=bk><OPTION VALUE=-1>==请先选择设备厂商==</OPTION></SELECT>";
	//
	String 	strUserList = DeviceAct.getUserOfGroupList(false,"","per_acc_oid");
	
	String 	strCityList = DeviceAct.getCityListSelf(false, "", "", request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var xmlHttp;

function createXMLHttpRequest() {
	  if (window.XMLHttpRequest)
	    	xmlHttp = new XMLHttpRequest();
	  else if (window.ActiveXObject){ 
		    try {
		    	xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		    } 
		    catch (e1){
		    try{
		      xmlHttp = new ActiveXObject("Microsoft.XMLHTTP"); 
		    }catch (e2){}} 
	  }
   return xmlHttp;     
}

function checkCommunity() {
	var obj = document.frm;
	var ip = document.all("loopback_ip").value;
	var gather_id = document.frm.gather_id.value;
	var snmp_ro_community = document.frm.snmp_ro_community.value;
	var snmp_rw_community = document.frm.snmp_rw_community.value;
	if(!IsNull(ip,'设备IP')){
		obj.loopback_ip.focus();
		return false;
	} else if(!IsIPAddr(ip)){
		obj.loopback_ip.focus();
		return false;
	} else if(!IsNull(snmp_ro_community,'设备读口令')){
		obj.snmp_ro_community.focus();
		return false;
	}else if(!IsChose1(gather_id,"采集点")){
		obj.gather_id.focus()
		return false;
	} else{
		document.frm.check_ro_community.value = "检测中，请稍等...";
		
		createXMLHttpRequest();
        var url = "TestCommunity.jsp?ip="+ip+"&gather_id="+gather_id+"&snmp_ro_community="+snmp_ro_community+"&snmp_rw_community="+snmp_rw_community;
        xmlHttp.open("POST", url, true);
        xmlHttp.onreadystatechange = startCallback;
        xmlHttp.send(null);
        
	}
}
    
function startCallback() {

        if (xmlHttp.readyState ==4) {
          if (xmlHttp.status == 200) {
		    var message=xmlHttp.responseText;
		    message=message.trim();
//1:读写口令皆不可读;2读口令准确,写口令不可读;3写口令可读,读口令错误;4读写口令皆可读
            if(message=="1")
		       document.frm.check_ro_community.value = "读写口令皆不可读!";
		   	else if(message=="2") 
		       document.frm.check_ro_community.value = "读口令准确,写口令不可读";
		    else if(message=="3") 
		       document.frm.check_ro_community.value = "写口令可读,读口令错误";
		   	else if(message=="4") 
		       document.frm.check_ro_community.value = "读写口令皆可读";
		    else if(message=="5") 
		       document.frm.check_ro_community.value = "读口令可读";
		   	else if(message=="6") 
		       document.frm.check_ro_community.value = "读口令错误";
		    else if(message=="fail") 
		       document.frm.check_ro_community.value = "检测失败(可能进程异常请联系维护人员) 稍后再试";
           
          }else {
            alert("HTTP error: "+xmlHttp.status);
          }
        }
    }

function changeDev(param){
	
	if(param.value == 1){
		this.location="AddDeviceForm.jsp?gw_type=2";
	}
}

function resetForm(){
	document.all("frm").reset();
}

function showChild(parname){
	if(parname=='city_id'){
		var pid = document.all(parname).value; 
			document.all("childFrm").src= "getOfficeList.jsp?pid=" + pid;
	} else if(parname=="device_model"){
		document.all("childFrm").src = "filterVersion.jsp?model="+ document.frm.device_model.value;

		v = event.srcElement.value;
		if(parseInt(blnSZ)==1 && v=="7300ASAM"){
			idShelf.style.display="";
		}
		else{
			idShelf.style.display="none";
			document.frm.device_shelf.value = "0";
		}
		
	}else if(parname=="vendor_id"){
	
		var o = $("vendor_id");
		var id = o.options[o.selectedIndex].value;
		var url = "getVendorDeviceModel.jsp";
		var pars = "vendor_id=" + id ;
		pars+= "&device_type=snmp";
		var myAjax
			= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onSuccess:getDeviceModel,onFailure:showError}						
						   );

	}else if(parname=="role_id"){
		var o = document.all("role_id");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "getAccoutByRole.jsp?role_id="+ id;
	}
}

function getDeviceModel(request){
	$("strModelList").innerHTML = request.responseText;
}
function showError(request){
	//alert(request.responseText);
	$("debug").innerHTML = request.responseText;
}
function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.device_name.value,'设备名称')){
		obj.device_name.focus();
		return false;
	}
	else if(!IsNull(obj.loopback_ip.value,'设备域名')){
		obj.loopback_ip.focus();
		return false;
	}
	else if(!IsNull(obj.snmp_ro_community.value,'设备读口令')){
		obj.snmp_ro_community.focus();
		return false;
	}
	else if(!IsNull(obj.buy_time.value,'入网运行时间')){
		obj.buy_time.focus();
		return false;
	}
	else if(!IsNull(obj.complete_time.value,'最近一次维保开始时间')){
		obj.complete_time.focus();
		return false;
	}
	else if(obj.vendor_id.value== -1){
		alert("设备厂商必须选择！");
		obj.vendor_id.focus()
		return false;
	}
	else if(obj.device_model.value == -1){
		alert("请选择设备型号!");
		obj.device_model.focus()
		return false;
	}
	else if(obj.device_serialnumber.value == ""){
		alert("请输入设备序列号！");
		obj.device_serialnumber.focus()
		return false;
	}
	else if(obj.city_id.value == -1){
		alert("请选择属地！");
		obj.city_id.focus()
		return false;
	}
	else if(obj.gather_id.value == -1){
		alert("请选择采集机！");
		obj.gather_id.focus()
		return false;
	}
	else if(!IsNull(obj.staff_id.value,"员工代码")){
		obj.staff_id.focus();
		obj.staff_id.select();
		return false;
	}
	else{
		return true;
	}	
}

</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="DeviceSave_snmp.jsp" target="childFrm" onSubmit="return CheckForm();">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="162" align="center" class="title_bigwhite">设备资源</TD>
						<TD>
							<IMG src="../images/attention_2.gif" width="15"	height="12">
								<%
									if(GwProtocol == 0){
								 %>
								 确认添加的设备类型
								<input type="radio" name="dev_type" value="1" onclick="changeDev(this);" >TR069设备
								<input type="radio" name="dev_type" value="2" onclick="changeDev(this);" checked>SNMP设备
								<%
									} else {
								%>
								带<font color="#FF0000">*</font>的表单必须填写或选择.
								<%
									}
								
								 %>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>

				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class=column>
						<TH colspan="4" align="center">添加网络设备</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="12%">设备名称</TD>
						<TD class=column width="46%"><INPUT TYPE="text"
							NAME="device_name" value=""
							maxlength=50 class=bk title="请按照省公司设备命名规范填写"> &nbsp;<font
							color="#FF0000">*</font></TD>
						<TD class=column align="right" height="23" width="13%">属地</TD>
						<TD height="23" class="column"><%=strCityList%><font color="#FF0000">*</font>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="13%">设备域名</TD>
						<TD class="column" width="29%" ><font color="#FF0000">
						<input type="text" name="loopback_ip"
							value="" maxlength=255 class=bk>
						&nbsp;<font color="#FF0000">*</font></font></TD>
						<TD class=column align="right" width="13%">采集机</TD>
						<TD width="29%" class=column><%=strAdslList%><font
							color="#FF0000">*</font> &nbsp;</TD>	
						
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" height="17" width="13%">设备厂商</TD>
						<TD class="column" height="17"><%=strVendorList%><font
							color="#FF0000">*</font>
						<TD class=column align="right" height="17" width="13%">设备型号</TD>
						<TD class=column ><span id=strModelList><%=strModelList%><font
							color="#FF0000">*</font></span></TD>

					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="13%">设备用途</TD>
						<TD class="column" width="29%"><input type="text"
							name="device_useto" maxlength=50 class="bk"></TD>
						<TD class=column align="right" width="12%">设备序列号</TD>
						<TD class=column width="46%"><input type="text"
							name="device_serialnumber" maxlength=50 class=bk>&nbsp;<font
							color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="13%">设备管理责任人</TD>
						<TD class="column" width="37%" nowrap><%=strUserList%>
						</TD>
						<TD height="28" class="column" width="12%">
							<div align="right">操作人</div>
						</TD>
						<TD height="28" class="column" width="46%"><input type="text"
							name="staff_id" maxlength=10 class=bk
							value="<%=user.getAccount()%>"> &nbsp;<font
							color="#FF0000">*</font></TD>
					</TR>


					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="13%"><font color="red">读</font>Snmp字串</TD>
						<TD class="column" width="29%"><input type="text"
							name="snmp_ro_community" maxlength=50 class="bk"> &nbsp;<font
							color="#FF0000">*</font></TD>
						<TD class=column align="right" width="12%"><font color="red">写</font>Snmp字串</TD>
						<TD class=column width="46%"><input type="text"
							name="snmp_rw_community" maxlength=50 class=bk>&nbsp;<font color="#FF0000">*</font>&nbsp;
					
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="13%">入网运行时间</TD>
						<TD class="column" colspan="3"><input type="text"
							name="buy_time" class=bk> <input type="button" value="▼"
							class=btn onClick="showCalendar('day',event)" name="button2">
						&nbsp;<font color="#FF0000">*</font> <input type="hidden" name="hidden_buy_time" class="bk">
						 &nbsp;&nbsp;最近一次维保开始时间 <input
							type="text" name="complete_time" class=bk> <input
							type="button" value="▼" class=btn onClick="showCalendar('day',event)"
							name="button"> &nbsp;<font color="#FF0000">*</font> 
							<input type="hidden" name="hidden_complete_time" class="bk">
						&nbsp;&nbsp;维保年限 <select name="service_year" class="bk">
							<option value="0">0年</option>
							<option value="1">1年</option>
							<option value="2">2年</option>
							<option value="3">3年</option>
							<option value="4">4年</option>
							<option value="5">5年</option>
							<option value="6">6年</option>
							<option value="7">7年</option>
							<option value="8">8年</option>
							<option value="9">9年</option>
							<option value="10">10年</option>
						</select></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD width="12%" class=column>
						<div align="right">备注</div>
						</TD>
						<TD width="46%" class=column colspan=3><input type="text"
							name="remark" maxlength=255 class=bk size=40></TD>
					</TR>
					<TR>
						<TD colspan="4" align="center" class=foot><INPUT
							TYPE="submit" value=" 保 存 " class=btn> &nbsp;&nbsp; <INPUT
							TYPE="reset" value=" 重 写 " class=btn> <INPUT
							TYPE="hidden" name="action" value="add"></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
</FORM>
	<TR>
		<TD>&nbsp;</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm name="childFrm" SRC="" STYLE="display:none"></IFRAME>
		&nbsp;</TD>
	</TR>
</TABLE>
	
<div id="debug"></div>
<SCRIPT LANGUAGE="JavaScript">
<!--
//document.all.enterdate.innerText = CurrentTime();
//update
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
