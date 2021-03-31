<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.*"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
//配置的设备类型 SNMP OR TR069
int GwProtocol = LipossGlobals.getGwProtocol();	

Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct.getOUIDevMap();
List list  = null;
String strData = "";
list = DeviceAct.getSnmpDeviceInfoList(request);
String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
		
if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
} else {
	String device_id = null;
	String device_name=null;
	String city_id = null;
	String city_name = null;
	String devicemodel = null;
	String softwareversion = null;
	String vendor_id = null;
	
	String device_serialnumber = null;
	
    while (fields != null) {
		device_id = (String)fields.get("device_id");
		
		device_name = (String)fields.get("device_name");
		
		devicemodel= (String)fields.get("device_model");
				
		softwareversion = (String)fields.get("device_swv");
		
		vendor_id = (String)fields.get("vendor_id");
		
		softwareversion = softwareversion == null?"":softwareversion;

		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		
		device_serialnumber = (String)fields.get("device_serialnumber");
		
    	
		strData += "<TD class=column2 nowrap>" + device_name + "</TD>";
		strData += "<TD class=column2 nowrap>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
		strData += "<TD class=column2 nowrap>" + devicemodel + "</TD>";
		strData += "<TD class=column2 nowrap>" +  (String)fields.get("loopback_ip") + "</TD>";
		strData += "<TD class=column2 nowrap>" + softwareversion + "</TD>";		
		strData += "<TD class=column2 nowrap>" + city_name + "</TD>";
		strData += "<TD class=column2 nowrap>" + (String)fields.get("device_serialnumber") + "</TD>";
		strData += "<TD class=column2 nowrap align='center'>";
		strData += "<a href=javascript:// onclick=\"DelDevice('" + device_id +"')\">删除</a> | ";
		strData += "<a href='#' onclick=\"EditDevice('" + device_id +"','" + vendor_id + "')\">编辑</a>";
		strData += "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    
    strData += "<TR><TD class=column COLSPAN=10 align=right  nowrap>" + strBar + "</TD></TR>";
	//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>批量删除</a>";
	//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>";
	//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"导出当前页数据到Excel中\">导出到CVS</a></div></TD></TR>";
	//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>";
	//strData += "<TR><TD class=column COLSPAN=10 nowrap><a href=\\ alt=\"导出当前页数据到Excel中\">导出到Excel</a></div></TD></TR>";
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
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						设备资源
					</td>
					<td>
					<%
					
							if(GwProtocol == 0){
					 %>
						<input type="radio" name="dev_type" value="1" onclick="changeDev(this);">TR069设备
								<input type="radio" name="dev_type" value="2" onclick="changeDev(this);" checked>SNMP设备
						&nbsp;&nbsp;
						<%
							}						
						 %>
						帐号：<input type="text" name="username" class=bk value="">
						序列号：<input type="text" name="serialnumber" class=bk value="">
						<input type="submit" name="query" value="查询">
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR class=column>
						<TH colspan="8" align="center">企业网关设备列表</TH>
						</TR>
						<TR>
							<TH nowrap>设备名称</TH>
							<TH nowrap>设备厂商</TH>
							<TH nowrap>型号</TH>
							<TH nowrap>域名</TH>
							<TH nowrap>软件版本</TH>
							<TH nowrap>属地</TH>
							<TH nowrap>设备序列号</TH>
							<!--<TH>管理域</TH> -->
							<TH>操作</TH>
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