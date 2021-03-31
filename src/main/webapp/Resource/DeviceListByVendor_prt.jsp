<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
	String isNewOut = request.getParameter("isNewOut");
	String gw_type_Str = request.getParameter("gw_type");

	if (null == gw_type_Str) {
		gw_type_Str = "1";
	}
	int gw_type = Integer.parseInt(gw_type_Str);
	if("1".equals(isNewOut)){
		int res  = DeviceAct.toEXcelByVendor(request, gw_type);
		response.setContentType("text/html;charset=utf-8");
		if(res>0){

			response.getWriter().write("<script>alert('导出任务已经生成，请稍后在 \"报表下载任务管理页面\" 下载');</script>");

		}else{
			response.getWriter().write("<script>alert('导出任务生成失败');</script>");

		}

		return;
	}

request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");


response.setHeader("Content-disposition","attachment; filename=deviceVendor.xls" );

String strData = "";

Cursor cursor = DeviceAct.getDeviceListByVenderExcel(request);
Map fields = cursor.getNext();
Map area_Map = DeviceAct.getAreaIdMapName();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct.getOUIDevMap();
String delStr = "DelDevice('?')";
String editStr = "EditDevice('?')";
String detailStr = "DetailDevice('?')";

if (fields == null) {
    strData = "<TR><TD COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
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
		strData += "<TR><TD>" + device_id_ex + "</TD>";
		strData += "<TD>" + venderMap.get(fields.get("vendor_id")) + "</TD>";
        strData += "<TD>" + fields.get("device_name") + "</TD>";
        strData += "<TD>" + fields.get("loopback_ip") + "</TD>";
		strData += "<TD>" + city_name + "</TD>";
		strData += "<TD align=left>" + (String)fields.get("device_serialnumber") + "</TD>";
		strData += "<TD>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
		strData += "</TR>";
        fields = cursor.getNext();
    }
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
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td><b>设备资源列表</b></td>
		</tr>
			<TR>
				<TD>
					<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
						<TR>
							<TH>设备编码</TH>
							<TH>设备厂商</TH>
							<TH>设备名称</TH>
							<TH>设备IP</TH>
							<TH>属地</TH>
							<TH>设备序列号</TH>
							<TH>管理域</TH>
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