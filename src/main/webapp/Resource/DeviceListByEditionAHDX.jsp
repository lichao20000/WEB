<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map,java.util.HashMap"%>
<jsp:useBean id="DeviceActAhdx" scope="request" class="com.linkage.litms.resource.DeviceActAhdx"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
//修改
String devicetype = request.getParameter("devicetype"); 
String sqlid = request.getParameter("sqlid"); 
String cityId = request.getParameter("city_id");
String cityIdSelect=request.getParameter("cityIdSelect");//修改处
String citynext = request.getParameter("citynext");
String devicetypeId = request.getParameter("devicetype_id");//版本
String rela_dev_type_id = request.getParameter("rela_dev_type_id");
String gw_type = request.getParameter("gw_type");
String isBind = request.getParameter("isBind");
String startTime = request.getParameter("startTime");
String vendorId = request.getParameter("vendor_id");//厂商
String deviceModelId = request.getParameter("deviceModel_id");//型号

if(null==startTime || "null".equals(startTime)){
	startTime = "";
}
String endTime = request.getParameter("endTime");
if(null==endTime || "null".equals(endTime)){
	endTime = "";
}
String is_normal=request.getParameter("is_normal");
if(null==is_normal || "null".equals(is_normal)){
	is_normal="0";
}
String recentStartTime = request.getParameter("recentStartTime");
if(null==recentStartTime || "null".equals(recentStartTime)){
	recentStartTime = "";
}
String recentEndTime = request.getParameter("recentEndTime");
if(null==recentEndTime || "null".equals(recentEndTime)){
	recentEndTime = "";
}

String prt_Page="DeviceListByEdition_prt_AHDX.jsp?city_id="+cityId+"&rela_dev_type_id="+ rela_dev_type_id+"&devicetype_id="+devicetypeId+"&gw_type="+gw_type+"&isBind="+isBind+"&vendor_id="+vendorId+"&startTime="+startTime+"&endTime="+endTime+"&is_normal="+is_normal+"&gw_type="+gw_type+"&deviceModelId="+deviceModelId+"&devicetype="+devicetype+"&citynext="+citynext+"&recentStartTime="+recentStartTime+"&recentEndTime="+recentEndTime+"&cityIdSelect="+cityIdSelect;

String __debug = request.getParameter("__debug");
List list  = null;
String strData = "";
list = DeviceActAhdx.getDeviceListByVender(request);

String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
Map area_Map = DeviceActAhdx.getAreaIdMapName();
Map city_Map = CityDAO.getCityIdCityNameMap();
Map cityPidMap = CityDAO.getCityIdPidMap();
Map venderMap = DeviceActAhdx.getOUIDevMap(gw_type);
String delStr = "DelDevice('?')";
String editStr = "EditDevice('?')";
String detailStr = "DetailDevice('?','"+gw_type+"')";




//add by benyp
String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;
Map deviceTypeMap = new HashMap();
Cursor cursorTmp;
if("4".equals(gw_type)){
	String sql1 = "select * from stb_tab_devicetype_info a, stb_gw_device_model b where a.device_model_id = b.device_model_id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql1 = "select device_model, softwareversion, devicetype_id from stb_tab_devicetype_info a, stb_gw_device_model b where a.device_model_id = b.device_model_id";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql1);
	psql.getSQL();
	cursorTmp = DataSetBean.getCursor(sql1);
}else{
	String sql2 = "select * from tab_devicetype_info a, gw_device_model b where a.device_model_id = b.device_model_id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql2 = "select device_model, softwareversion, devicetype_id from tab_devicetype_info a, gw_device_model b where a.device_model_id = b.device_model_id";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql2);
	psql.getSQL();
	cursorTmp = DataSetBean.getCursor(sql2);
}

		Map fieldTmp = cursorTmp.getNext();
		while (fieldTmp != null){
			devicemodel = (String)fieldTmp.get("device_model");
			softwareversion = (String)fieldTmp.get("softwareversion");
			devicetype_id = (String)fieldTmp.get("devicetype_id");
			deviceTypeMap.put(devicetype_id,devicemodel+","+softwareversion);
			fieldTmp = cursorTmp.getNext();
		}



if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
} else {
	String device_id = null;
	String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
	String city_parentname = null;
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		device_id_ex = (String)fields.get("device_id_ex");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		city_parentname = (String)city_Map.get(cityPidMap.get(city_id));
		city_parentname = city_parentname == null ? "&nbsp;" : city_parentname;
		devicetype_id = (String)fields.get("devicetype_id"); 
		//-1为设备型号是“请选择”状态（设备导入时给的默认值）
		devicemodel = "";
		softwareversion = "";
		if(!devicetype_id.equals("-1")){
		
			String tmp = (String)deviceTypeMap.get(devicetype_id);
			if(tmp.split(",").length>1){
			if(tmp != "" && null != tmp){
				String[] aa = tmp.split(",");
				devicemodel = aa[0];
				softwareversion = aa[1];
			}}
			else
			{
				if(tmp != "" && null != tmp){
					String[] aa = tmp.split(",");
					devicemodel = aa[0];
					softwareversion = "";
				}
			}
		}
		strData += "<TR><TD class=column2>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
		strData += "<TD class=column2>" + devicemodel + "</TD>";
		strData += "<TD class=column2>" + softwareversion + "</TD>";
		strData += "<TD class=column2>" + city_name + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("device_serialnumber") + "</TD>";
		strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
		strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">详细信息</a>";
		strData += "<TD class=column2>" + strOper + "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>";
	if("1".equals(LipossGlobals.getLipossProperty("isReport"))){
		strData += "<TR><TD class=column COLSPAN=10><div><a href=\""+prt_Page+"\" alt=\"导出当前页数据到Excel中\">导出到Excel</a></div></TD></TR>";
	}
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var __debug = eval(<%=__debug%>);
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
	window.open(strpage,"","left=20,top=20,width=550,height=500,resizable=no,scrollbars=yes");
}
function DelDevice(device_id){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	var url = "DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	var myAjax
		= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onSuccess:showResult,onFailure:showError}						
						   );
	return true;
}
function EditDevice(device_id){
	var strpage = "AddDeviceForm.jsp?_action=update&device_id=" + device_id;
	window.location.href=strpage;
}

function DetailDevice(device_id,gw_type){
	var strpage;
	if("4" == gw_type){
		strpage = "/itms/gtms/stb/resource/gwDeviceQueryStb!queryZeroDeviceDetail.action?gw_type=4&deviceId=" + device_id;
	}else{
		strpage = "DeviceShow.jsp?device_id=" + device_id;
	}
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
	window.location.href="AddDeviceForm.jsp";
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

function prtview()
{
	
page = "<%=prt_Page%>";
page = page.replace(/%/g,"%25");
	
alert(page);
document.location.href = page;
//document.all("childFrm").src = page;

}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="DeviceSave.jsp" onsubmit="return CheckForm()">
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						设备资源
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
							<TD bgcolor="#ffffff" colspan="9" >该系统现有以下网络设备资源&nbsp;&nbsp;</TD>
						</TR>
						<TR>
							<TH>设备厂商</TH>
							<TH>型号</TH>
							<TH>软件版本</TH>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>管理域</TH>
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