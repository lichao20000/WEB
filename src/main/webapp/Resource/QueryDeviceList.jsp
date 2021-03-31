<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map,java.util.HashMap"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

// 查找设备条件
String str_device_id_ex = request.getParameter("device_id_ex");
   if (str_device_id_ex == null) str_device_id_ex = "";
String str_devicetype_id = request.getParameter("devicetype_id");
   if (str_devicetype_id == null) str_devicetype_id = "";
String str_loopback_ip = request.getParameter("loopback_ip");
   if (str_loopback_ip == null) str_loopback_ip = "";
String str_vendor_id = request.getParameter("vendor_id");
   if (str_vendor_id == null) str_vendor_id = "";
String str_city_id = request.getParameter("city_id");
   if (str_city_id == null) str_city_id = "";
String str_cpe_mc = request.getParameter("cpe_mc");//设备MAC地址
   if (str_cpe_mc == null) str_cpe_mc = "";
String str_gather_id = request.getParameter("gather_id");
   if (str_gather_id == null) str_gather_id = "";
String str_softwareversion = request.getParameter("softwareversion"); //软件版本
   if (str_softwareversion == null) str_softwareversion = "";
String str_status = request.getParameter("status"); //在线状态
   if (str_status == null) str_status = "";
String str_area = request.getParameter("area");
   if (str_area == null) str_area = "";
String str_deviceserial = request.getParameter("device_serial");
   if (str_deviceserial == null) str_deviceserial = "";
String str_servicecode = request.getParameter("service_code");
   if (str_servicecode == null) str_servicecode = "";
String str_device_model_id = request.getParameter("device_model_id");
   if (str_device_model_id == null) str_device_model_id = "";
String str_devType = request.getParameter("type_name");
   if (str_devType == null) str_devType = "";
   
String str_device_version_type = request.getParameter("device_version_type");
   if (str_device_version_type == null) str_device_version_type = "";
   
String starttime = request.getParameter("starttime");
   if (starttime == null) starttime = "";
String endtime = request.getParameter("endtime");
   if (endtime == null) endtime = "";
String deviceStatus = request.getParameter("device_status");
   if (deviceStatus == null) deviceStatus = "-1";

   
String str_gw_type = request.getParameter("gw_type");

String str_he_type = request.getParameter("he_type");


	if(null != str_gw_type && "3".equals(str_gw_type)){
		
		str_gw_type = str_he_type;
	}
	
int gw_type = Integer.parseInt(str_gw_type);

String prt_Page="DeviceList_prt_operate.jsp?gw_type=" + gw_type + "&device_id_ex=" + str_device_id_ex + "&devicetype_id=" + str_devicetype_id + "&loopback_ip=" + str_loopback_ip + "&vendor_id=" + str_vendor_id + "&city_id=" + str_city_id + "&cpe_mc=" + str_cpe_mc + "&gather_id=" + str_gather_id + "&softwareversion=" + str_softwareversion + "&str_status=" + str_status + "&area=" + str_area + "&device_serial=" + str_deviceserial + "&service_code=" + str_servicecode + "&device_model_id=" + str_device_model_id + "&type_name=" + str_devType + "&device_version_type=" + str_device_version_type + "&starttime=" + starttime + "&endtime=" + endtime + "&device_status=" + deviceStatus;
String ref_page = "QueryDeviceList.jsp?gw_type=" + gw_type + "&he_type="+str_he_type+"&device_id_ex=" + str_device_id_ex + "&devicetype_id=" + str_devicetype_id + "&loopback_ip=" + str_loopback_ip + "&vendor_id=" + str_vendor_id + "&city_id=" + str_city_id + "&cpe_mc=" + str_cpe_mc + "&gather_id=" + str_gather_id + "&softwareversion=" + str_softwareversion + "&status=" + str_status + "&area=" + str_area + "&device_serial=" + str_deviceserial + "&service_code=" + str_servicecode + "&device_model_id=" + str_device_model_id + "&type_name=" + str_devType + "&device_version_type=" + str_device_version_type + "&starttime=" + starttime + "&endtime=" + endtime + "&device_status=" + deviceStatus;;    
String __debug = request.getParameter("__debug");
List list  = null;
String strData = "";


list = DeviceAct.QueryDeviceList(request);
String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct.getOUIDevMap();
String delStr = "DelDevice('?')";
String editStr = "EditDevice('?')";
String detailStr = "DetailDevice('?')";
String confirmStr = "ConfirmDev('?')";

//add by benyp
String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;
Map deviceTypeMap = DeviceAct.getDeviceTypeMap();

if (fields == null) {
	if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
	{
		strData = "<TR><TD class=column COLSPAN=11 HEIGHT=30>该系统没有设备资源</TD></TR>";
	}else{
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
	}
} else {
	String device_id = null;
	String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
	String cpe_allocatedstatus = null;
	String oui = null;
	String deviceSN = null;
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		device_id_ex = (String)fields.get("device_id_ex");
		if(device_id_ex == null || "".equals(device_id_ex) || "null".equalsIgnoreCase(device_id_ex))
			device_id_ex = "";
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		
		
		//add by benyp
		devicetype_id = (String)fields.get("devicetype_id"); 
		//-1为设备型号是“请选择”状态（设备导入时给的默认值）
		devicemodel = "";
		softwareversion = "";
		if(!"-1".equals(devicetype_id)||devicetype_id==null){
		
			String[] tmp = (String[])deviceTypeMap.get(devicetype_id);
			if (tmp != null && tmp.length==2) {
				devicemodel = tmp[0];
				softwareversion = tmp[1];			
			}
			else{
				devicemodel = "";
				softwareversion = "";
			}
		}
		
		
		
		city_name = city_name == null ? "&nbsp;" : city_name;
       // strData += "<TR><TD class=column2 align='center'><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
		
		strData += "<TR><TD class=column2>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
		//edit by benyp
		strData += "<TD class=column2>" + devicemodel + "</TD>";
		strData += "<TD class=column2>" + softwareversion + "</TD>";
		
		strData += "<TD class=column2>" + city_name + "</TD>";
		deviceSN = (String)fields.get("device_serialnumber");
		strData += "<TD class=column2>" + deviceSN + "</TD>";
		strData += "<TD class=column2>" + device_id_ex + "</TD>";
		//strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
		if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
			String device_version_type =  (String)fields.get("device_version_type");
			if("1".equals(device_version_type)){
				device_version_type= "E8-C";
			}else if ("2".equals(device_version_type)){
				device_version_type= "PON融合";
			}else if ("3".equals(device_version_type)){
				device_version_type= "10GPON";
			}else if ("4".equals(device_version_type)){
				device_version_type= "政企网关";
			}else if ("5".equals(device_version_type)){
				device_version_type= "天翼网关1.0";
			}else if ("6".equals(device_version_type)){
				device_version_type= "天翼网关2.0";
			}else if ("7".equals(device_version_type)){
				device_version_type= "天翼网关3.0";
			}else{
				device_version_type="";
			}
			
		strData += "<TD class=column2>" + device_version_type + "</TD>";
    	}else{
    	strData += "<TD class=column2>" + (String)fields.get("device_type") + "</TD>";	
    	}
		
		String device_status=(String)fields.get("device_status");
		String gigabit_port=(String)fields.get("gigabit_port");
		cpe_allocatedstatus = (String)fields.get("cpe_allocatedstatus");
		if("1".equals(cpe_allocatedstatus)){
			strData += "<TD class=column2>已绑定</TD>";
		}else if("0".equals(cpe_allocatedstatus)){
			strData += "<TD class=column2>未绑定</TD>";
		}else{
			strData += "<TD class=column2>未知状态</TD>";
		}
		
		if("1".equals(device_status)){
			strData += "<TD class=column2>已确认</TD>";
			if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
			{
				if(gigabit_port.equals("1"))
				{
					strData += "<TD class=column2>是</TD>";	
				}else{
					strData += "<TD class=column2>否</TD>";	
				}
			}
			strOper = "&nbsp;&nbsp;<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\"><IMG SRC='../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></a>";
			//strOper += "&nbsp;&nbsp;<a href='#' onclick=\""+ StringUtils.replace(editStr,"?",device_id) +"\"><IMG SRC='../images/edit.gif' BORDER='0' ALT='编辑' style='cursor:hand'></a>";
		}else{
			strData += "<TD class=column2>未确认</TD>";
			if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
			{
				if(gigabit_port.equals("1"))
				{
					strData += "<TD class=column2>是</TD>";	
				}else{
					strData += "<TD class=column2>否</TD>";	
				}
				
			}
			strOper = "&nbsp;&nbsp;<a href='#' onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\"><IMG SRC='../images/view.gif' BORDER='0' ALT='详细信息' style='cursor:hand'></a>";
			strOper += "&nbsp;&nbsp;<a href=javascript:// onclick=\""+ StringUtils.replace(confirmStr,"?",device_id) +"\"><IMG SRC='../images/check1.gif' BORDER='0' ALT='确认设备' style='cursor:hand'></a>";
		}
		//strOper += "&nbsp;&nbsp;<a href=javascript:// onclick=\"refreshDev('"+device_id+"')\"><IMG SRC='../images/refresh.png' BORDER='0' ALT='更新' style='cursor:hand'></a>";
		
		oui = (String)fields.get("oui");
		//strOper += "&nbsp;&nbsp;<a href=javascript:// onclick=\"delDev('"+device_id+"','"+cpe_allocatedstatus+"','"+oui+"','"+deviceSN+"')\"><IMG SRC='../images/del.gif' BORDER='0' ALT='删除' style='cursor:hand'></a>";
		strData += "<TD class=column2>" + strOper + "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
	{
        strData += "<TR><TD class=column COLSPAN=11 align=right>" + strBar + "</TD></TR>";
	}else{
	    strData += "<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>";
	}
	//strData += "<TR><TD class=column COLSPAN=10><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>批量删除</a>";
	//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>";
	//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"导出当前页数据到Excel中\">导出到CVS</a></div></TD></TR>";
	if("1".equals(LipossGlobals.getLipossProperty("isReport"))){
		if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
		{
			strData += "<TR><TD class=green_foot COLSPAN=11><a href=\""+prt_Page+"\" alt=\"导出当前页数据到Excel中\"><IMG SRC='../images/excel.gif' BORDER='0' ALT='导出列表' style='cursor:hand'></a></TD></TR>";

		}else{
			strData += "<TR><TD class=green_foot COLSPAN=10><a href=\""+prt_Page+"\" alt=\"导出当前页数据到Excel中\"><IMG SRC='../images/excel.gif' BORDER='0' ALT='导出列表' style='cursor:hand'></a></TD></TR>";

		}
	}
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
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
/*
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
function showResult(request){
	$("_process").innerHTML="";
	eval(request.responseText);
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
*/
function EditDevice(device_id){
	var strpage = "AddDeviceForm.jsp?_action=update&device_id=" + device_id;
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function ConfirmDev(device_id){
	if(!confirm("确定要确认该设备！！！")){
		return false;
	}
	var url = "./DeviceSave.jsp";
	var gw_type = "<%= gw_type%>";
	var _action = "status";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action
	},function(ajax){	
		alert($.trim(ajax));
	});
	return true;
}

function refresh(){
	window.location.href=window.location.href;
}
function refreshDev(device_id){
	var url = '<s:url value='/gwms/resource/refDelDev!refresh.action'/>';
	var ref_page = "<%=ref_page%>";
	$.post(url,{
		deviceId:device_id
	},function(ajax){	
	    alert(ajax);
		window.location.href=ref_page;
	});
}
function delDev(device_id,cpe_allocatedstatus,oui,deviceSN){
	var url = '<s:url value='/gwms/resource/refDelDev!delete.action'/>';
	var ref_page = "<%=ref_page%>";
	var gw_type="<%=gw_type%>";
	$.post(url,{
		deviceId:device_id,
		cpe_allocatedstatus:cpe_allocatedstatus,
		oui:oui,
		deviceSN:deviceSN,
		gw_type:gw_type
	},function(ajax){	
	    alert(ajax);
		window.location.href=ref_page;
	});
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
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
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
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
						<!--  	<TH><input type=checkbox onclick="SelectAll(this,'chkCheck')"></TH>-->
							
							<TH>设备厂商</TH>
							<TH>型号</TH>
							<TH>软件版本</TH>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>
								<ms:inArea areaCode="sx_lt">
									唯一标识
								</ms:inArea>
								<ms:inArea areaCode="sx_lt" notInMode="true">
									LOID
								</ms:inArea>
							</TH>
							<% if("gs_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
							<TH>设备版本类型</TH>
							<%}else{%>
							<TH>终端类型</TH>
							<%} %>
							<TH>绑定状态</TH>
							<TH>确认状态</TH>
							<% if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
							<TH>是否支持光猫标识</TH>
							<%} %>
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
