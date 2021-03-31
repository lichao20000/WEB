<%@ include file="../timelater.jsp"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<jsp:useBean id="DeviceAct_Copy" scope="request" class="com.linkage.litms.resource.DeviceAct_Copy"/>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%
request.setCharacterEncoding("GBK");

String cityId = request.getParameter("city_id");
String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");

String prt_Page="DeviceListByCity_prt.jsp?city_id="+cityId+"&startTime="+startTime+"&endTime"+endTime;

String __debug = request.getParameter("__debug");
String binddate = request.getParameter("binddate");
List list  = null;
String strData = "";

if (LipossGlobals.isXJDX()) {
	list = DeviceAct_Copy.getDeviceListByBindTime(request);
}else{
	list = DeviceAct_Copy.getDeviceListByCity(request);
}

String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct_Copy.getOUIDevMap();
String detailStr = "DetailDevice('?')";

//add by benyp
String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;
Map deviceTypeMap = DeviceAct_Copy.getDeviceTypeMap();

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=9 HEIGHT=30>该系统没有设备资源</TD></TR>";
} else {
	String device_id = null;
	//String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		//device_id_ex = (String)fields.get("device_id_ex");
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
		strData += "<TR><TD class=column2>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
		
		strData += "<TD class=column2>" + devicemodel + "</TD>";
		strData += "<TD class=column2>" + softwareversion + "</TD>";
		
		strData += "<TD class=column2>" + city_name + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("device_serialnumber") + "</TD>";
		
		strOper = "<a href=javascript:// onclick=\""+ StringUtils.replace(detailStr,"?",device_id) +"\">详细信息</a>";
		strData += "<TD class=column2>" + strOper + "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column COLSPAN=6 align=right>" + strBar + "</TD></TR>";
    
	//strData += "<TR><TD class=column COLSPAN=10><div><a href=\""+prt_Page+"\" alt=\"导出当前页数据到Excel中\">导出到Excel</a></div></TD></TR>";
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var __debug = eval(<%=__debug%>);

function DetailDevice(device_id){
	var strpage = "../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

//出现异常调用方法
function showError(request){
	$("_process").innerHTML="";
	//if(__debug)
		$(debug).innerHTML = request.responseText;
}

function prtview()
{
	
page = "<%=prt_Page%>";
page = page.replace(/%/g,"%25");

alert(page);
document.location.href = page;

}

//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD="post" ACTION="DeviceSave.jsp" onsubmit="return CheckForm()">
<TABLE boder=0 cellspacing=0 cellpadding=0 width="98%" align="center">
<TR><TD bgcolor=#999999>
	
		<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center">
			<TR>
				<TH colspan="6">
						设备资源
				</TH>
		   </TR>

			<TR>
				<TH>设备厂商</TH>
				<TH>型号</TH>
				<TH>软件版本</TH>
				<TH>属地</TH>
				<TH>设备序列号</TH>
				<TH>操作</TH>
			</TR>
			<%=strData%>
		</TABLE>
	
</TD></TR>
<TR><TD HEIGHT=20 align="center"><div id="_process"></div></TD></TR>
</TABLE>
</FORM>	
<%@ include file="../foot.jsp"%>