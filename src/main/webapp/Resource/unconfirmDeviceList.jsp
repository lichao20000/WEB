<%@ include file="../timelater.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.*"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>

<jsp:useBean id="commonAct" scope="request" class="com.linkage.litms.resource.commonAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String device_serialnumber = request.getParameter("device_serialnumber");
if(device_serialnumber == null){
	device_serialnumber = "";
}
String loopback_ip = request.getParameter("loopback_ip");
if(loopback_ip == null){
	loopback_ip = "";
}

String strData = "";
String __debug = request.getParameter("__debug");
String gw_type = request.getParameter("gw_type");

if ("".equals(device_serialnumber) && "".equals(loopback_ip)) {
	strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>请输入设备序列号最后6位或IP地址查询！</TD></TR>";
} else {
	List list  = null;
	list = commonAct.getUnconfirmDeviceInfoList(request);
	String strBar = String.valueOf(list.get(0)); 
	Cursor cursor = (Cursor)list.get(1);
	Map fields = cursor.getNext();
	Map area_Map = commonAct.getAreaIdMapName();
	//Map city_Map = DeviceAct.getCityMap(request);
	Map city_Map = CityDAO.getCityIdCityNameMap();
	Map venderMap = commonAct.getOUIDevMap();
	
	String delStr = "DelDevice('?')";
	String editStr = "EditDevice('?')";
	String detailStr = "DetailDevice('?')";
	//add by benyp
	String devicetype_id = null;
	String devicemodel = null;
	String softwareversion = null;
	Map deviceTypeMap = DeviceAct.getDeviceTypeMap();


	if (fields == null) {
	    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
	} else {
		String device_id = null;
		String device_id_ex = null;
		String strOper = null;
		String city_id = null;
		String city_name = null;
		//String gw_type = null;yl注释，该变量重复定义
		
	    while (fields != null) {
			device_id = (String)fields.get("device_id");
	        device_id = device_id.replaceAll("\\+", "%2B");
	        device_id = device_id.replaceAll("&", "%26");
	        device_id = device_id.replaceAll("#", "%23");
			device_id_ex = (String)fields.get("device_id_ex");
			
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
			

			
			city_id = (String)fields.get("city_id");
			city_name = (String)city_Map.get(city_id);
			city_name = city_name == null ? "&nbsp;" : city_name;
	    strData += "<TR><TD class=column2 align='center' nowrap><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
			
			strData += "<TD class=column2 nowrap>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
			strData += "<TD class=column2 nowrap>" + devicemodel + "</TD>";
			strData += "<TD class=column2 nowrap>" + softwareversion + "</TD>";
			
			strData += "<TD class=column2 nowrap>" + city_name + "</TD>";
			strData += "<TD class=column2 nowrap>" + fields.get("device_serialnumber") + "</TD>";
			strData += "<TD class=column2 nowrap>" + fields.get("loopback_ip") + "</TD>";
			String complete_time = "";
			try
			{
				long date = Long.valueOf(fields.get("complete_time").toString());
				DateTimeUtil dt = new DateTimeUtil(date * 1000);
				complete_time = dt.getDate();
			}
			catch (NumberFormatException e)
			{
				complete_time = "";
			}
			catch (Exception e)
			{
				complete_time = "";
			}
			strData += "<TD class=column2 nowrap>" + complete_time + "</TD>";
			//gw_type = (String)fields.get("gw_type");
			//if(gw_type.equals("1")){
			//	gw_type = "家庭网关";
			//} else {
			//	gw_type = "企业网关";
			//}
			//strData += "<TD class=column2 nowrap>" + gw_type + "</TD>";
			//strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(delStr,"?",device_id) +"\">删除</a>";
			//strOper = "&nbsp;&nbsp;<a href='#' onclick=\""+ StringUtils.replace(editStr,"?",device_id) +"\">编辑</a>";
			strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">详细信息</a>";
			strData += "<TD class=column2 nowrap  align='center'>" + strOper + "</TD>";
			strData += "</TR>";
	        fields = cursor.getNext();
	    }
	    
	    strData += "<TR><TD class=column COLSPAN=10 align=right  nowrap>" + strBar + "</TD></TR>";
		//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>批量删除</a>";
		//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>";
		//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"导出当前页数据到Excel中\">导出到CVS</a></div></TD></TR>";
		//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a></div></TD></TR>";
		strData += "<TR><TD class=column COLSPAN=10 nowrap>&nbsp;"
		 	+ "<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>确认设备</a>&nbsp;"
		 	+ "&nbsp;&nbsp;&nbsp;&nbsp;<a href='UnConfirmDeviceExcel.jsp?"
			+ "device_serialnumber="+device_serialnumber
			+"&loopback_ip="+loopback_ip+"'>导出未确认设备列表</a></TD></TR>";
	}
}



%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
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
	window.open(strpage,"","left=20,top=20,width=550,height=500,resizable=yes,scrollbars=yes");
}
function DelDevice(device_id){
	if(!confirm("真的要删除该网络设备吗？\n本操作所删除的不能恢复！！！")){
		return false;
	}
	var url = "DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	pars += "&gw_type="+<%=gw_type%>;
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
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--

/**
function prtview()
{	
	page = "";
	page = page.replace(/%/g,"%25");
		
	alert(page);
	document.location.href = page;
	//document.all("childFrm").src = page;
}
*/

function changeDev(param){
	if(param.value == 2){
		this.location="DeviceListAll_snmp.jsp";
	}
}

function checkForm()
{	
	var device_serialnumber = document.frm.device_serialnumber.value;
	if(device_serialnumber.length<6&&device_serialnumber.length>0){
		alert("请至少输入最后6位设备序列号进行查询！");
		document.frm.device_serialnumber.focus();
		return false;
	}
	return true;
}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return checkForm();">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						设备资源
					</td>
					<td>
						时间为设备注册时间	  
					</td>
				</tr>
				
			</table>
			</td>
		</tr>
<tr>
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="4">
							设备查询
						</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							设备序列号
						</td>
						<td width='35%' align="left">
							<input name="device_serialnumber" type="text" class='bk'>
						</td>
						<td class="column" width='15%' align="right">
							设备IP
						</td>
						<td width='35%' align="left">
							<input name="loopback_ip" type="text" class='bk'
								value="<s:property value='loopback_ip'/>">
						</td>
					</TR>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							开始时间
						</td>
						<td width='35%' align="left">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15"
								height="12" border="0" alt="选择">
						</td>
						<td class="column" width='15%' align="right">
							结束时间
						</td>
						<td width='35%' align="left">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
						</td>
					</TR>
					

					<TR>
						<td class="green_foot" colspan="4" align="right">
							<input type="submit" name="query" value=" 查 询 ">
							
						</TD>
					</TR>

				</TABLE>
			</TD>
		</TR>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR class=column>
						<TH colspan="9" align="center">未确认设备列表</TH>
						</TR>
						<TR>
							<TH nowrap><input type=checkbox onclick="SelectAll(this,'chkCheck')">全选</TH>
							
							<TH nowrap width="10%">设备厂商</TH>
							<TH nowrap width="10%">型号</TH>
							<TH nowrap width="15%">软件版本</TH>
							<TH nowrap width="10%">属地</TH>
							<TH nowrap width="15%">设备序列号</TH>
							<TH nowrap width="15%">设备IP</TH>
							<TH nowrap width="10%">注册时间</TH>
							<!--<TH>管理域</TH> -->
							<TH width="15%">操作</TH>
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