<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.ArrayList,com.linkage.litms.common.database.*"%>
<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="AdslAct" scope="request" class="com.linkage.litms.resource.AdslAct"/>
<jsp:useBean id="qryp" scope="page" class="com.linkage.litms.common.database.QueryPage" />
<%
request.setCharacterEncoding("GBK");
String strData = "";
long area_id = curUser.getAreaId();
String	sql = "select a.* from tab_deviceresource a where a.resource_type_id=101  and device_id not in (select device_id from cus_radiuscustomer where user_state in ('1','2'))";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select a.device_swv, a.device_name, a.device_id, a.device_id_ex, a.city_id, a.vendor_id, a.device_serialnumber, " +
			"a.device_model, a.vendor_id, a.loopback_ip " +
			" from tab_deviceresource a where a.resource_type_id=101  and device_id not in (select device_id from cus_radiuscustomer where user_state in ('1','2'))";
}

	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	 String stroffset = request.getParameter("offset");
		int pagelen = 10;
		int offset;
		if(stroffset == null) offset = 1;
		else offset = Integer.parseInt(stroffset);
		qryp.initPage(sql,offset,pagelen);
		String strBar = qryp.getPageBar();   
		Cursor cursor = DataSetBean.getCursor(sql,offset,pagelen);
Map fields = cursor.getNext();
Map city_Map = DeviceAct.getCityMap(request);
Map venderMap = DeviceAct.getOUIDevMap();

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>没查询到符合条件的设备</TD></TR>";
} else {
	String device_id = null;
	String device_id_ex = null;
	String city_id = null;
	String city_name = null;
	String vender = null;
	String oui = null;
	String serialnumber = null;	
	String device_name=null;
	String devicemodel=null;
	String softwareversion=null;
	String vendor_id=null;
    while (fields != null) {
        softwareversion = (String)fields.get("device_swv");
		softwareversion = softwareversion == null?"":softwareversion;
    	device_name = (String)fields.get("device_name");
		device_id = (String)fields.get("device_id");
		device_id_ex = (String)fields.get("device_id_ex");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;		
		vender = (String)venderMap.get(fields.get("vendor_id"));		
		serialnumber = (String)fields.get("device_serialnumber");
		devicemodel= (String)fields.get("device_model");
		vendor_id = (String)fields.get("vendor_id");
		strData += "<TR>";
		strData += "<TD class=column1>" + device_name + "</TD>";
		strData += "<TD class=column2>" + vender + "</TD>";
		strData += "<TD class=column2>" + devicemodel + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("loopback_ip") + "</TD>";
		strData += "<TD class=column2>" + softwareversion + "</TD>";
		strData += "<TD class=column2>" + city_name + "</TD>";		
		strData += "<TD class=column2>" + serialnumber + "</TD>";
		strData += "<TD class=column2 nowrap align='center'>";
		strData += "<a href=javascript:// onclick=\"DelDevice('" + device_id +"')\">删除</a> | ";
		strData += "<a href='#' onclick=\"EditDevice('" + device_id +"','" + vendor_id + "')\">编辑</a>";
		strData += "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
//var __debug = eval();
function GoContent(device_id,device_name,resource_type_name,device_model,flag){
	/*
	 flag=1:查看设备资源组信息;
	 flag=2:查看设备详细信息
	*/
	var strpage;
	tmp = device_id;
	tmp = tmp.replace("+","%2B");
	tmp = tmp.replace("&","%26");
	tmp = tmp.replace("#","%23");
	if (flag=="2"){
	  	strpage="DeviceShow.jsp?device_id=" + tmp;
    }
	else if(flag=="1"){	
		strpage="DeviceVersion.jsp?device_id=" + tmp + "&device_name=" + device_name + "&resource_type_name=" + resource_type_name + "&device_model=" + device_model;
	}
	window.open(strpage,"","left=20,top=20,width=550,height=500,resizable=yes,scrollbars=yes");
}
function DelDevice(device_id){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	var url = "DeviceSave_snmp.jsp";
	var pars = "device_id=" + device_id;
	pars += "&tt=" + new Date().getTime();
	pars += "&action=delete";
	document.all("childFrm").src="DeviceSave_snmp.jsp?device_id=" + device_id + "&action=delete" + "&tt=" + new Date().getTime();
}
function EditDevice(device_id,vendor_id){
	var strpage = "EditDeviceForm_snmp.jsp?device_id=" + device_id + "&vendor_id=" + vendor_id + "&device_type=snmp";
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
function showResult(request){
	$("_process").innerHTML="";
	eval(request.responseText);
}
function refresh(){
	window.location.href=window.location.href;
}
//出现异常调用方法
function showError(request){
	$("_process").innerHTML="";
	//if(__debug)
		$(debug).innerHTML = request.responseText;
}
//批量删除设备
function DeleteMore(_name){
	var strDeviceIDs = getDeviceIDByCheck(_name);
	if(strDeviceIDs == ""){
		alert("请选择要删除的设备");
		return false;
	}
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	
	var pars = "tt=" + new Date().getTime();
	pars += strDeviceIDs;
	pars += "&_action=delete";
	var url = "DeviceSave.jsp";
	$("_process").innerHTML="正在执行操作.....";
	var myAjax
		= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onSuccess:showResult,onFailure:showError}						
						   );
}
function ConfirmDev(_name){
	var strDeviceIDs = getDeviceIDByCheck(_name);
	if(strDeviceIDs == ""){
		alert("请选择要确认的设备");
		return false;
	}
	if(!confirm("确定要确认选中的设备吗？")){
		return false;
	}
	var pars = "tt=" + new Date().getTime();
	pars += strDeviceIDs;
	pars += "&_action=status";
	var url = "DeviceSave.jsp";
	var myAjax
		= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onSuccess:showResult,onFailure:showError}						
						   );
}
//根据复选框选中状态，获取设备id
function getDeviceIDByCheck(_name){
	var arrObj = document.all(_name);
	var strDeviceIDs = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = "&device_id=" + arrObj.value;
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				strDeviceIDs += "&device_id=" + arrObj[i].value;
			}
		}
	}
	return strDeviceIDs;
}
//全选
function SelectAll(_this,_name){
	var check = _this.checked;
	var chkArr = $A(document.getElementsByName(_name));
	chkArr.each(function(obj){
		obj.checked = check;
	});
}
function AddDevice(){
	window.location.href="AddDeviceForm.jsp"
}
function refresh(){
	window.location.href=window.location.href;
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

function prtview()
{	
	//page = "";
	page = page.replace(/%/g,"%25");
		
	alert(page);
	document.location.href = page;
	//document.all("childFrm").src = page;
}

function changeDev(param){	
	if(param.value == 1){
		this.location="DeviceListAll.jsp?gw_type=2";
	}
}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">	    
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						未确认设备资源
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
					<TR class=column>
						<TH colspan="8" align="center">企业网关未确认设备列表</TH>
						</TR>
						<TR>	
						    <TH>设备名称</TH>						
							<TH>设备厂商</TH>
							<TH>设备型号</TH>
							<TH>设备域名</TH>
							<TH>软件版本</TH>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>操 作</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>	
</TD></TR>
<TR><TD HEIGHT=20 align="center"><div id="_process"></div></TD></TR>
</TABLE>
<IFRAME name=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;
<div id="debug" style="display:"></div>
<%@ include file="../foot.jsp"%>