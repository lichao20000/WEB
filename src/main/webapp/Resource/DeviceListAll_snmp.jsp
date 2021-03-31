<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.*"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
//���õ��豸���� SNMP OR TR069
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
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>��ϵͳû���豸��Դ</TD></TR>";
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
		strData += "<a href=javascript:// onclick=\"DelDevice('" + device_id +"')\">ɾ��</a> | ";
		strData += "<a href='#' onclick=\"EditDevice('" + device_id +"','" + vendor_id + "')\">�༭</a>";
		strData += "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    
    strData += "<TR><TD class=column COLSPAN=10 align=right  nowrap>" + strBar + "</TD></TR>";
	//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>����ɾ��</a>";
	//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>";
	//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"������ǰҳ���ݵ�Excel��\">������CVS</a></div></TD></TR>";
	//strData += "<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>";
	//strData += "<TR><TD class=column COLSPAN=10 nowrap><a href=\\ alt=\"������ǰҳ���ݵ�Excel��\">������Excel</a></div></TD></TR>";
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
						�豸��Դ
					</td>
					<td>
					<%
					
							if(GwProtocol == 0){
					 %>
						<input type="radio" name="dev_type" value="1" onclick="changeDev(this);">TR069�豸
								<input type="radio" name="dev_type" value="2" onclick="changeDev(this);" checked>SNMP�豸
						&nbsp;&nbsp;
						<%
							}						
						 %>
						�ʺţ�<input type="text" name="username" class=bk value="">
						���кţ�<input type="text" name="serialnumber" class=bk value="">
						<input type="submit" name="query" value="��ѯ">
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR class=column>
						<TH colspan="8" align="center">��ҵ�����豸�б�</TH>
						</TR>
						<TR>
							<TH nowrap>�豸����</TH>
							<TH nowrap>�豸����</TH>
							<TH nowrap>�ͺ�</TH>
							<TH nowrap>����</TH>
							<TH nowrap>����汾</TH>
							<TH nowrap>����</TH>
							<TH nowrap>�豸���к�</TH>
							<!--<TH>������</TH> -->
							<TH>����</TH>
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