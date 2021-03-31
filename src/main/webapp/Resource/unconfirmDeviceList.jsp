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
	strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>�������豸���к����6λ��IP��ַ��ѯ��</TD></TR>";
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
	    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>��ϵͳû���豸��Դ</TD></TR>";
	} else {
		String device_id = null;
		String device_id_ex = null;
		String strOper = null;
		String city_id = null;
		String city_name = null;
		//String gw_type = null;ylע�ͣ��ñ����ظ�����
		
	    while (fields != null) {
			device_id = (String)fields.get("device_id");
	        device_id = device_id.replaceAll("\\+", "%2B");
	        device_id = device_id.replaceAll("&", "%26");
	        device_id = device_id.replaceAll("#", "%23");
			device_id_ex = (String)fields.get("device_id_ex");
			
			devicetype_id = (String)fields.get("devicetype_id"); 
			//-1Ϊ�豸�ͺ��ǡ���ѡ��״̬���豸����ʱ����Ĭ��ֵ��
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
			//	gw_type = "��ͥ����";
			//} else {
			//	gw_type = "��ҵ����";
			//}
			//strData += "<TD class=column2 nowrap>" + gw_type + "</TD>";
			//strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(delStr,"?",device_id) +"\">ɾ��</a>";
			//strOper = "&nbsp;&nbsp;<a href='#' onclick=\""+ StringUtils.replace(editStr,"?",device_id) +"\">�༭</a>";
			strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">��ϸ��Ϣ</a>";
			strData += "<TD class=column2 nowrap  align='center'>" + strOper + "</TD>";
			strData += "</TR>";
	        fields = cursor.getNext();
	    }
	    
	    strData += "<TR><TD class=column COLSPAN=10 align=right  nowrap>" + strBar + "</TD></TR>";
		//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>����ɾ��</a>";
		//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>";
		//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"������ǰҳ���ݵ�Excel��\">������CVS</a></div></TD></TR>";
		//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a></div></TD></TR>";
		strData += "<TR><TD class=column COLSPAN=10 nowrap>&nbsp;"
		 	+ "<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>&nbsp;"
		 	+ "&nbsp;&nbsp;&nbsp;&nbsp;<a href='UnConfirmDeviceExcel.jsp?"
			+ "device_serialnumber="+device_serialnumber
			+"&loopback_ip="+loopback_ip+"'>����δȷ���豸�б�</a></TD></TR>";
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
	 flag=1:�鿴�豸��Դ����Ϣ;
	 flag=2:�鿴�豸��ϸ��Ϣ
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
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
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
//�����쳣���÷���
function showError(request){
	$("_process").innerHTML="";
	//if(__debug)
		$(debug).innerHTML = request.responseText;
}
//����ɾ���豸
function DeleteMore(_name){
	var strDeviceIDs = getDeviceIDByCheck(_name);
	if(strDeviceIDs == ""){
		alert("��ѡ��Ҫɾ�����豸");
		return false;
	}
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return false;
	}
	
	var pars = "tt=" + new Date().getTime();
	pars += strDeviceIDs;
	pars += "&_action=delete";
	var url = "DeviceSave.jsp";
	$("_process").innerHTML="����ִ�в���.....";
	var myAjax
		= new Ajax.Request(
							url,
							{method:"post",parameters:pars,onSuccess:showResult,onFailure:showError}						
						   );
}
function ConfirmDev(_name){
	var strDeviceIDs = getDeviceIDByCheck(_name);
	if(strDeviceIDs == ""){
		alert("��ѡ��Ҫȷ�ϵ��豸");
		return false;
	}
	if(!confirm("ȷ��Ҫȷ��ѡ�е��豸��")){
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
//���ݸ�ѡ��ѡ��״̬����ȡ�豸id
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
//ȫѡ
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
		alert("�������������6λ�豸���кŽ��в�ѯ��");
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
						�豸��Դ
					</td>
					<td>
						ʱ��Ϊ�豸ע��ʱ��	  
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
							�豸��ѯ
						</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							�豸���к�
						</td>
						<td width='35%' align="left">
							<input name="device_serialnumber" type="text" class='bk'>
						</td>
						<td class="column" width='15%' align="right">
							�豸IP
						</td>
						<td width='35%' align="left">
							<input name="loopback_ip" type="text" class='bk'
								value="<s:property value='loopback_ip'/>">
						</td>
					</TR>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							��ʼʱ��
						</td>
						<td width='35%' align="left">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15"
								height="12" border="0" alt="ѡ��">
						</td>
						<td class="column" width='15%' align="right">
							����ʱ��
						</td>
						<td width='35%' align="left">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td>
					</TR>
					

					<TR>
						<td class="green_foot" colspan="4" align="right">
							<input type="submit" name="query" value=" �� ѯ ">
							
						</TD>
					</TR>

				</TABLE>
			</TD>
		</TR>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR class=column>
						<TH colspan="9" align="center">δȷ���豸�б�</TH>
						</TR>
						<TR>
							<TH nowrap><input type=checkbox onclick="SelectAll(this,'chkCheck')">ȫѡ</TH>
							
							<TH nowrap width="10%">�豸����</TH>
							<TH nowrap width="10%">�ͺ�</TH>
							<TH nowrap width="15%">����汾</TH>
							<TH nowrap width="10%">����</TH>
							<TH nowrap width="15%">�豸���к�</TH>
							<TH nowrap width="15%">�豸IP</TH>
							<TH nowrap width="10%">ע��ʱ��</TH>
							<!--<TH>������</TH> -->
							<TH width="15%">����</TH>
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