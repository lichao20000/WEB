<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.*"%>

<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String deviceStatus = request.getParameter("device_status");
	String cpeCurrentstatus = request.getParameter("cpe_currentstatus");
	String gw_type = request.getParameter("gw_type");
	
	//add by zhangcong �Ӳ˵����벻��ѯ 2011-06-02
	String no_query = request.getParameter("no_query");

	String prt_Page = "";
	if (deviceStatus == null && cpeCurrentstatus == null) {
		prt_Page = "DeviceList_prt.jsp?device_status=1&gw_type="
				+ gw_type;
	} else if (deviceStatus == null) {
		prt_Page = "DeviceList_prt.jsp?cpe_currentstatus="
				+ cpeCurrentstatus;
	} else {
		prt_Page = "DeviceList_prt.jsp?device_status=" + deviceStatus;
	}
	String __debug = request.getParameter("__debug");
	List list = null;
	StringBuffer strData = new StringBuffer(10000);
	
	//Ĭ��Ϊ��
	Map fields = null;
	Cursor cursor = null;
	String strBar = "";
	
	//add by zhangcong �Ӳ˵����벻��ѯ 2011-06-02
	if(no_query == null || no_query.trim().equalsIgnoreCase(""))
	{
		list = DeviceAct.getDeviceInfoList(request);
		strBar = String.valueOf(list.get(0));
		cursor = (Cursor) list.get(1);
		fields = cursor.getNext();
	}
	
	//Map area_Map = DeviceAct.getAreaIdMapName();
	//Map city_Map = DeviceAct.getCityMap(request);
	Map city_Map = CityDAO.getCityIdCityNameMap();
	Map venderMap = DeviceAct.getOUIDevMap();

	String delStr = "DelDevice('?')";
	String editStr = "EditDevice('?')";
	String detailStr = "DetailDevice('?')";

	//add by benyp
	String devicetype_id = null;
	String devicemodel = null;
	String softwareversion = null;
	String device_model_id = null;
	
	Map deviceTypeMap = DeviceAct.getDeviceTypeMap();
	if (fields == null) {
		strData.append("<TR><TD class=column COLSPAN=10 HEIGHT=30>��ϵͳû���豸��Դ</TD></TR>");
	} else {
		String device_id = null;
		String device_id_ex = null;
		String strOper = null;
		String city_id = null;
		String city_name = null;
		String area_name = "";

		while (fields != null) {
			device_id = (String) fields.get("device_id");
			device_id = device_id.replaceAll("\\+", "%2B");
			device_id = device_id.replaceAll("&", "%26");
			device_id = device_id.replaceAll("#", "%23");
			device_id_ex = (String) fields.get("device_id_ex");

			//add by benyp
			devicetype_id = (String) fields.get("devicetype_id");
			//device_model_id = (String) fields.get("device_model_id");
			
			//-1Ϊ�豸�ͺ��ǡ���ѡ��״̬���豸����ʱ����Ĭ��ֵ��
			devicemodel = "";
			softwareversion = "";
			if (!devicetype_id.equals("-1")) {

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

			area_name = (String) fields.get("loopback_ip");

			city_id = (String) fields.get("city_id");
			city_name = (String) city_Map.get(city_id);
			city_name = city_name == null ? "&nbsp;" : city_name;
			strData.append("<TR><TD class=column2 align='center' nowrap><input type=checkbox name=chkCheck value='"
					+ device_id + "'></TD>");

			strData.append("<TD class=column2 nowrap>" + city_name
					+ "</TD>");
			strData.append("<TD class=column2 nowrap>"
					+ venderMap.get(fields.get("vendor_id")) + "</TD>");
			strData.append("<TD class=column2 nowrap>" + devicemodel
					+ "</TD>");
			
			strData.append("<TD class=column2 nowrap>" + softwareversion
					+ "</TD>");
			strData.append("<TD class=column2 nowrap>"
				+ (String) fields.get("device_serialnumber")
				+ "</TD>");
			
			strData.append("<TD class=column2 nowrap>" + area_name
			+ "</TD>");
			//strData += "<TD class=column2 nowrap>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//������
			String strDel = "DelDevice('" + device_id + "','"
					+ fields.get("oui") + "')";
			strOper = "<a href=javascript:// onclick=" + strDel
					+ ">ɾ��</a>";
			strOper += "&nbsp;|&nbsp;<a href='#' onclick=\""
					+ StringUtils.replace(editStr, "?", device_id)
					+ "\">�༭</a>";
			strOper += "&nbsp;|&nbsp;<a href=javascript:// onclick=\""
					+ StringUtils.replace(detailStr, "?", device_id)
					+ "\">��ϸ��Ϣ</a>";
			strData.append("<TD class=column2 nowrap  align='center'>"
					+ strOper + "</TD>");
			strData.append("</TR>");
			fields = cursor.getNext();
		}

		strData.append("<TR><TD class=column COLSPAN=10 align=right  nowrap>"
				+ strBar + "</TD></TR>");
		strData.append("<TR><TD class=column COLSPAN=10 nowrap><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>����ɾ��</a>");
		//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>";
		//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"������ǰҳ���ݵ�Excel��\">������CVS</a></div></TD></TR>";
		if("1".equals(LipossGlobals.getLipossProperty("isReport"))){
			strData.append("&nbsp;|&nbsp;<a href=\""
					+ prt_Page
					+ "\" alt=\"������ǰҳ���ݵ�Excel��\">������Excel</a></div></TD></TR>");
		}
	}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<script language="JavaScript" src='../Js/jquery.js'></script>
<SCRIPT LANGUAGE="JavaScript">


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
function DelDevice(device_id,vendor_id){
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return false;
	}
	
	var url = "DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&gw_type=" + <%=gw_type%>;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	
	if(confirm("�Ƿ���Ҫɾ���豸��Ӧ���û��ʺ�")){
		pars += "&deluser=true";
	}

	pars += "&vendor_id="+vendor_id;
	
	var myAjax
		= new Ajax.Request(url,
			{method:"post",parameters:pars,onSuccess:showResult,onFailure:showError}						
		   ); 
	
	/* $.ajax({
        type:"get",
        url: url + "?" + pars,
        dataType: "String",
        contentType: "application/json; charset=utf-8",
        success:function(data) {
        	alert(data);
        },
        error: function(e){
        	console.info(e)
            alert('error');

        }
	}); */
	
	return true;
}
function EditDevice(device_id){
	var strpage = "AddDeviceForm.jsp?_action=update&device_id=" + device_id+"&gw_type="+<%=gw_type%>;
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
	pars += "&gw_type=" + <%=gw_type%>;
	pars += strDeviceIDs;
	pars += "&_action=delete";
	
	if(confirm("�Ƿ���Ҫɾ���豸��Ӧ���û��ʺ�")){
		pars += "&deluser=true";
	}
	
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
	pars +="&gw_type=" + <%=gw_type%>;
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

function prtview()
{	
	page = "<%=prt_Page%>";
	page = page.replace(/%/g,"%25");
		
	alert(page);
	document.location.href = page;
	//document.all("childFrm").src = page;
}

function checkForm()
{	
	var serialnumber = document.frm.serialnumber.value;
	if(serialnumber.length<6&&serialnumber.length>0){
		alert("�������������6λ�豸���кŽ��в�ѯ��");
		document.frm.serialnumber.focus();
		return false;
	}
	return true;
}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>  
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return checkForm();">
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">�豸��Դ</td>
						<td>�豸������IP��<input type="text" name="loopback_ip" class=bk value="">
						�豸���кţ�<input type="text" name="serialnumber" class=bk value="">
						<input type="submit" class=jianbian name="query" value=" �� ѯ "></td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
					<TR class=column>
						<%if("2".equals(gw_type)) {%>
						<TH colspan="8" align="center">��ҵ�����豸����</TH>
						<%}else{%>
						<TH colspan="8" align="center">��ͥ�����豸����</TH>
						<%}%>
					</TR>
					<TR>
						<TD class=green_title2><input type=checkbox
							onclick="SelectAll(this,'chkCheck')"></TD>
						<TD class=green_title2>����</TD>
						<TD class=green_title2>����</TD>
						<TD class=green_title2>�ͺ�</TD>
						<TD class=green_title2>�����汾</TD>
						<TD class=green_title2>�豸���к�</TD>
						
						<TD class=green_title2>������IP</TD>
						<TD class=green_title2>����</TD>
					</TR>
					<%=strData%>
					<input type="hidden" name="status" value=<%=deviceStatus%> />
				</TABLE>
				</TD>
			</TR>
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
<%@ include file="../foot.jsp"%>