<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map"%>
<%@page import="java.util.HashMap"%>
<jsp:useBean id="LanSpeedReportDeviceAct" scope="request" class="com.linkage.litms.resource.LanSpeedReportDeviceAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String __debug = request.getParameter("__debug");
List list  = null;
String strData = "";

//从request中取出gw_type 1:家庭网关设备 2:企业网关设备
String gw_type_Str = request.getParameter("gw_type");
if (null == gw_type_Str) {
	gw_type_Str = "1";
}
int gw_type = Integer.parseInt(gw_type_Str);

list = LanSpeedReportDeviceAct.getDeviceInfoListByService(request, gw_type);

String prt_Page="";
String cityid = request.getParameter("city_id");
prt_Page = "LanSpeedReportByCity_prt.jsp?city_id="+cityid+"&gw_type="+gw_type+"&isNewOut=1";


String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
Map city_Map = CityDAO.getCityIdCityNameMap();
String detailStr = "DetailDevice('?')";

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
} else {
	String device_id = null;
	String strOper = null;
	String gather_time = null;
	String max_bit_rate = null;
	String downlink = null;
	String city_name = null;
	String username = null;
	String netusername = null;
	String vendor_name = null;
	String device_model = null;
	String gbbroadband = null; 
	String device_serialnumber = null;
	String wan_type = null;
	
    while (fields != null) {
    	
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
        
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long date_temp = Long.valueOf((String)fields.get("gather_time"));  
		gather_time = sdf.format(new Date(date_temp * 1000L));  
		
		max_bit_rate = (String)fields.get("max_bit_rate"); 
		downlink = (String)fields.get("downlink");
		city_name = (String)fields.get("city_name");
		username = (String)fields.get("username"); 
		netusername = (String)fields.get("netusername"); 
		vendor_name = (String)fields.get("vendor_name"); 
		device_model = (String)fields.get("device_model"); 
		gbbroadband = "1".equals((String)fields.get("gbbroadband")) ? "是" : "否"; 
		device_serialnumber = (String)fields.get("device_serialnumber"); 
		wan_type = "1".equals((String)fields.get("wan_type")) ? "桥接" : ("2".equals((String)fields.get("wan_type")) ? "路由" : "其他"); 
		
		
		strData += "<TD class=column2>" + city_name + "</TD>";
		strData += "<TD class=column2>" + max_bit_rate + "</TD>";
		strData += "<TD class=column2>" + downlink + "</TD>";
		strData += "<TD class=column2>" + gather_time + "</TD>";
		strData += "<TD class=column2>" + vendor_name + "</TD>";
		strData += "<TD class=column2>" + device_model + "</TD>";
		strData += "<TD class=column2>" + device_serialnumber + "</TD>";
		strData += "<TD class=column2>" + username + "</TD>";
		strData += "<TD class=column2>" + netusername + "</TD>";
		strData += "<TD class=column2>" + wan_type + "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>";
    strData += "<TR><TD class=column COLSPAN=10><div><a href=\""+prt_Page+"\" alt=\"导出当前页数据到Excel中\" target=\"_blank\">导出到Excel</a></div></TD></TR>";
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/edittable.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

</SCRIPT>
<link rel="stylesheet" href="../css/listview.css" type="text/css">

<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="DeviceSave.jsp" onsubmit="return CheckForm()">
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
  		<td>
  		<table width="100%" align=center  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="left" class="title_bigwhite">
					协商速率不匹配设备详情
				</td>
			</tr>
 		</table>
 		</td>
  		</tr>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
							<TH>地市</TH>
							<TH>LAN口当前速率</TH>
							<TH>签约速率</TH>
							<TH>LAN速率采集时间</TH>
							<TH>厂家</TH>
							<TH>DEVICE_MODEL</TH>
							<TH>设备序列号</TH>
							<TH>LOID</TH>
							<TH>KD</TH>
							<TH>宽带模式</TH>
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