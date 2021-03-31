<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map"%>
<jsp:useBean id="DeviceActSpeed" scope="request" class="com.linkage.litms.resource.DeviceActSpeed"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=device.xls" );

List list  = null;
String strData = "";
String isOut = request.getParameter("isOut");

String gw_type_Str = request.getParameter("gw_type");
if (null == gw_type_Str) {
	gw_type_Str = "1";
}
int gw_type = Integer.parseInt(gw_type_Str);

list = DeviceActSpeed.getDeviceInfoListBySpeedExcel(request, gw_type);

Cursor cursor = (Cursor)list.get(0);
Map fields = cursor.getNext();
Map area_Map = DeviceActSpeed.getAreaIdMapName();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceActSpeed.getOUIDevMap();
String devicetype_id = null;
String devicemodel = null;
String softwareversion = null;
Map deviceTypeMap = new HashMap();
String sql = "select * from tab_devicetype_info a,gw_device_model b where a.device_model_id = b.device_model_id";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select device_model, softwareversion, devicetype_id from tab_devicetype_info a,gw_device_model b where a.device_model_id = b.device_model_id";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();
Cursor cursorTmp = DataSetBean.getCursor(sql);
Map fieldTmp = cursorTmp.getNext();
while (fieldTmp != null){
	devicemodel = (String)fieldTmp.get("device_model");
	softwareversion = (String)fieldTmp.get("softwareversion");
	devicetype_id = (String)fieldTmp.get("devicetype_id");
	
	deviceTypeMap.put(devicetype_id,devicemodel+","+softwareversion);
	
	fieldTmp = cursorTmp.getNext();
}

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>该系统没有设备资源</TD></TR>";
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
		devicetype_id = (String)fields.get("devicetype_id"); 
		//-1为设备型号是“请选择”状态（设备导入时给的默认值）
		devicemodel = "";
		softwareversion = "";
		if(!devicetype_id.equals("-1")){
		
			String tmp = (String)deviceTypeMap.get(devicetype_id);
			if(tmp != "" && null != tmp){
				String[] aa = tmp.split(",");
				devicemodel = aa[0];
				softwareversion = aa[1];
			}
		}
        //strData += "<TR><TD class=column2 align='center'><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
		strData += "<TR><TD align=center>" +  venderMap.get(fields.get("vendor_id")) + "</TD>";
		strData += "<TD align=center>" + devicemodel + "</TD>";
		strData += "<TD align=center>" + softwareversion + "</TD>";
		strData += "<TD align=center>" + city_name + "</TD>";
		strData += "<TD align=center>" + (String)fields.get("device_serialnumber") + "</TD>";
		strData += "<TD class=column2>" + (String)fields.get("loid") + "</TD>";
		strData += "<TD align=center>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//管理域
		
		//strData += "<TD class=column2>" + strOper + "</TD>";
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
<TABLE border=1 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=1 cellpadding=0 align="center">
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