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

// ɽ����ͨ
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
{	//������
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
{	//������
	deviceTypeMap = DeviceAct.getStbDeviceTypeMap();
}else{
	deviceTypeMap = DeviceAct.getDeviceTypeMap();
}

if (fields == null) {
    strData.append("<TR><TD class=column COLSPAN=10 HEIGHT=30>��ϵͳû���豸��Դ</TD></TR>");
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
		
        //strData += "<TR><TD class=column2 align='center'><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
		
		strData.append("<TR><TD class=column2>" + venderMap.get(fields.get("vendor_id")) + "</TD>");
		
		strData.append("<TD class=column2>" + devicemodel + "</TD>");
		strData.append("<TD class=column2>" + softwareversion + "</TD>");
		
		strData.append("<TD class=column2>" + city_name + "</TD>");
		strData.append("<TD class=column2>" + (String)fields.get("device_serialnumber") + "</TD>");
		//strData += "<TD class=column2>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//������
		
		//strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(delStr,"?",device_id) +"\">ɾ��</a>";
		//strOper = "&nbsp;&nbsp;<a href='#' onclick=\""+ StringUtils.replace(editStr,"?",device_id) +"\">�༭</a>";
		strOper = "&nbsp;&nbsp;<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">��ϸ��Ϣ</a>";
		strData.append("<TD class=column2  align='center' >" + strOper + "</TD>");
		strData.append("</TR>");
        fields = cursor.getNext();
    }

    strData.append("<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>");
	//strData += "<TR><TD class=column COLSPAN=10><div><a href=javascript:// onclick='DeleteMore(\"chkCheck\")'>����ɾ��</a>";
	//strData += "&nbsp;|&nbsp;<a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>";
	//strData += "&nbsp;|&nbsp;<a href=\"javascript:void(0);\" onClick=\"initialize('outTable',1,0)\" alt=\"������ǰҳ���ݵ�Excel��\">������CVS</a></div></TD></TR>";
	//strData += "<TR><TD class=column COLSPAN=10><div><a href=javascript:// onclick='ConfirmDev(\"chkCheck\")'>ȷ���豸</a>";
	//strData += "&nbsp;|&nbsp;<a href=\""+prt_Page+"\" alt=\"������ǰҳ���ݵ�Excel��\">������Excel</a></div></TD></TR>";
	
    if("1".equals(LipossGlobals.getLipossProperty("isReport"))){
   		strData.append("<TR><TD class=column COLSPAN=10>"
   				+ "<IMG SRC=../images/excel.gif BORDER=0 ALT=�����б� style='cursor: hand' "
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
	window.open(strpage,"","left=20,top=20,width=550,height=500,resizable=no,scrollbars=yes");
}
function DelDevice(device_id){
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
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
	// ɽ����ͨ������ ��ϸ��Ϣ
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
//�����쳣���÷���
function showError(request){
	$("_process").innerHTML="";
	//if(__debug)
		$(debug).innerHTML = request.responseText;
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
											��ȷ���豸�б�
										<%}else if(stat_type.equals("unconfirmed")){%>
											δȷ���豸�б�
										<%}else if(stat_type.equals("online")){%>
											�����豸�б�
										<%}else if(stat_type.equals("offline")){%>
											�����豸�б�
										<%}else if(stat_type.equals("unknow")){%>
											δ֪״̬�豸�б�
										<%}else{%>
											�豸�б�
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
									<!--<TH><input type=checkbox onclick="SelectAll(this,'chkCheck')">ȫѡ</TH>-->

									<TH>
										�豸����
									</TH>
									<TH>
										�ͺ�
									</TH>
									<TH>
										����汾
									</TH>
									<TH>
										����
									</TH>
									<TH>
										�豸���к�
									</TH>
									<!--<TH>������</TH> -->
									<TH>
										����
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