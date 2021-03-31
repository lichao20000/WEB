<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page
	import="com.linkage.litms.common.util.StringUtils,java.util.Map,java.util.HashMap"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<%
request.setCharacterEncoding("GBK");
String gw_type = request.getParameter("gwType");
if(gw_type == null || gw_type.equals("")){
	gw_type = "1";
}

// 山西联通
String stat_type = request.getParameter("stat_type");
String cityId = request.getParameter("city_id");
String starttime = request.getParameter("starttime");
String endtime = request.getParameter("endtime");

/* String prt_Page = "DeviceList_prt.jsp?stat_type=" + stat_type + "&city_id=" + cityId + "&gw_type=" + gw_type
			+ "&starttime=" + starttime + "&endtime=" + endtime; */

String __debug = request.getParameter("__debug");
List list  = null;
StringBuffer strData = new StringBuffer(10000);
list = DeviceAct.getStatDeviceInfoList4Sxlt(request);
String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
Map area_Map = DeviceAct.getAreaIdMapName();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map =CityDAO.getCityIdCityNameMap();
Map venderMap = null;

if(gw_type.equals("4"))
{	//机顶盒
	venderMap = DeviceAct.getStbVendorMap();
}else{
	venderMap = DeviceAct.getOUIDevMap();
}
String delStr = "DelDevice('?')";
String editStr = "EditDevice('?')";
String detailStr = "DetailDevice('?')";


//add by benyp
String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;
Map deviceTypeMap = null;
if(gw_type.equals("4"))
{	//机顶盒
	deviceTypeMap = DeviceAct.getStbDeviceTypeMap();
}else{
	deviceTypeMap = DeviceAct.getDeviceTypeMap();
}

if (fields == null) {
    strData.append("<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>");
} else {
	String device_id = null;
	String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		device_id_ex = (String)fields.get("device_id_ex");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
		
		
		//add by benyp
		devicetype_id = (String)fields.get("devicetype_id"); 
		//-1为设备型号是“请选择”状态（设备导入时给的默认值）
		devicemodel = "";
		softwareversion = "";
		if(!devicetype_id.equals("-1")){
		
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
		
        //strData += "<TR><TD class=column2 align='center'><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
		
		strData.append("<TR><TD class=column2>" + venderMap.get(fields.get("vendor_id")) + "</TD>");
		
		strData.append("<TD class=column2>" + devicemodel + "</TD>");
		strData.append("<TD class=column2>" + softwareversion + "</TD>");
		
		strData.append("<TD class=column2>" + city_name + "</TD>");
		strData.append("<TD class=column2>" + (String)fields.get("device_serialnumber") + "</TD>");
		//strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
		
		//strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(delStr,"?",device_id) +"\">删除</a>";
		//strOper = "&nbsp;&nbsp;<a href='#' onclick=\""+ StringUtils.replace(editStr,"?",device_id) +"\">编辑</a>";
		strOper = "&nbsp;&nbsp;<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">详细信息</a>";
		strData.append("<TD class=column2  align='center' >" + strOper + "</TD>");
		strData.append("</TR>");
        fields = cursor.getNext();
    }

    strData.append("<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>");
	//strData += "<TR><TD class=column COLSPAN=10><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>批量删除</a>";
	//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>";
	//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"导出当前页数据到Excel中\">导出到CVS</a></div></TD></TR>";
	//strData += "<TR><TD class=column COLSPAN=10><div><a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>";
	//strData += "&nbsp;|&nbsp;<a href=\""+prt_Page+"\" alt=\"导出当前页数据到Excel中\">导出到Excel</a></div></TD></TR>";
	
    if("1".equals(LipossGlobals.getLipossProperty("isReport"))){
   		strData.append("<TR><TD class=column COLSPAN=10>"
   				+ "<IMG SRC=../images/excel.gif BORDER=0 ALT=导出列表 style='cursor: hand' "
   				+ "onclick=\"devToExcel('" + cityId +"','"+ stat_type +"','"+ starttime +"','"+ endtime +"','"+ gw_type + "')\">" 
   				+ "</TD></TR>");
    }
}
String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var area = '<%=area%>'
var gw_type =  '<%=gw_type%>';

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
function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	// 山西联通机顶盒 详细信息
	if(area == 'sx_lt' && gw_type == '4')
	{
		strpage = "DeviceShow.jsp?device_id=" + device_id + "&gw_type=4";
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
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
function devToExcel(cityId, stat_type, starttime, endtime, gwType) {
	var page="<s:url value='/itms/resource/deviceAct!getStatDeviceInfoList4SxltToExcel.action'/>?"
		+ "city_id=" + cityId
		+ "&stat_type=" + stat_type
		+ "&starttime=" + starttime
		+ "&endtime=" + endtime
		+ "&gwType=" + gwType;
	document.all("childFrm").src=page;
}

<%-- function prtview()
{
	
page = "<%=prt_Page%>";
page = page.replace(/%/g,"%25");
	
alert(page);
document.location.href = page;
//document.all("childFrm").src = page;

} --%>

</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="DeviceSave.jsp"
				onsubmit="return CheckForm()">
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table>
								<tr>
									<th class="green_gargtd" style="width: 140px;color: #FFF">
										<%if(stat_type.equals("confirmed")){%>
											已确认设备列表
										<%}else if(stat_type.equals("unconfirmed")){%>
											未确认设备列表
										<%}else if(stat_type.equals("online")){%>
											在线设备列表
										<%}else if(stat_type.equals("offline")){%>
											下线设备列表
										<%}else if(stat_type.equals("unknow")){%>
											未知状态设备列表
										<%}else{%>
											设备列表
										<%}%>
									</th>
									<%-- <td>
										<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
									</td> --%>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#000000>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
								id="outTable">
								<TR>
									<!--<TH><input type=checkbox onclick="SelectAll(this,'chkCheck')">全选</TH>-->

									<TH>
										设备厂商
									</TH>
									<TH>
										型号
									</TH>
									<TH>
										软件版本
									</TH>
									<TH>
										属地
									</TH>
									<TH>
										设备序列号
									</TH>
									<!--<TH>管理域</TH> -->
									<TH>
										操作
									</TH>
								</TR>
								<%=strData%>
							</TABLE>
						</TD>
					</tr>
				</TABLE>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20 align="center">
			<div id="_process"></div>
		</TD>
	</TR>
</TABLE>
<IFRAME name=childFrm SRC="" STYLE="display: none"></IFRAME>
&nbsp;
<div id="debug" style="display: "></div>
<ms:inArea areaCode="sx_lt" notInMode="true">
<%@ include file="../foot.jsp"%>
</ms:inArea>