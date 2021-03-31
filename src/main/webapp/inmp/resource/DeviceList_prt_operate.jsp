<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List,java.util.*"%>
<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");


response.setHeader("Content-disposition","attachment; filename=device.xls" );

String __debug = request.getParameter("__debug");
String strData = "";
Cursor cursor = DeviceAct.getStatDeviceInfoListPRT(request);
//Cursor cursor = DeviceAct.QueryDeviceList(request);
Map fields = cursor.getNext();
Map area_Map = DeviceAct.getAreaIdMapName();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct.getOUIDevMap();
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
	StringBuilder strHtmlBuilder = new StringBuilder(100000);
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
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
		device_id_ex = (String)fields.get("device_id_ex");
		if(device_id_ex == null)
			device_id_ex = "";
        //strData += "<TR><TD class=column2 align='center'><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
				//strData += "<TD align=center>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
        strHtmlBuilder.append("<TR>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(venderMap.get(fields.get("vendor_id")));
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(devicemodel);
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(softwareversion);
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(city_name);
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(fields.get("device_serialnumber"));
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(device_id_ex);
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		strHtmlBuilder.append(fields.get("device_type"));
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("<TD align=center>");
		String device_status=(String)fields.get("device_status");
		if("1".equals(device_status)){
			strHtmlBuilder.append("已确认");
		}else{
			strHtmlBuilder.append("未确认");
		}
		strHtmlBuilder.append("</TD>");
		strHtmlBuilder.append("</TR>");
        fields = cursor.getNext();
    }
   strData = strHtmlBuilder.toString();
}
%>
<HTML>


<HEAD>
<TITLE>设备统计导入数据</TITLE>


<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">


<style>
TD{

  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;

}
</style>
</HEAD>
<BODY>
<TABLE boder=1 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td align="center"><b>设备资源统计</b></td>
		</tr>
			<TR>
				<TD>
					<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH>设备厂商</TH>
							<TH>型号</TH>
							<TH>软件版本</TH>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>LOID</TH>
							<TH>终端类型</TH>
							<TH>确认状态</TH>
						</TR> 
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>	
</TD></TR>
</TABLE>
</BODY>
</HTML>