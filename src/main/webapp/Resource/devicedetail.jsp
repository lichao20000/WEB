<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*,com.linkage.litms.resource.*,java.util.*" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<jsp:useBean id="OfficeAct" scope="request" class="com.linkage.litms.resource.OfficeAct"/>
<jsp:useBean id="ZoneAct" scope="request" class="com.linkage.litms.resource.ZoneAct"/>

<%
request.setCharacterEncoding("GBK");


String device_id = request.getParameter("device_id");

//属地MAP
Map map = new HashMap();
map = CityDAO.getCityIdCityNameMap();

 


DeviceAct deviceact = new DeviceAct();

Map officeMap = OfficeAct.getOfficeMap();

Map zoneMap = ZoneAct.getOfficeMap();
// 设备厂商Map
Map venderMap = deviceact.getOUIDevMap();

if(device_id == null) response.sendRedirect("../error.jsp?errid=0");

String sql = "select a.*,b.device_mac from tab_gw_device a,tab_devres_desc b where a.device_id=b.device_id and a.device_id='" + device_id + "'";
// teledb
if (DBUtil.GetDB() == 3) {
    sql = "select a.device_name, a.loopback_ip, a.oui, a.device_serialnumber, a.device_model_id, a.device_swv," +
			" a.city_id, a.office_id, a.zone_id, a.device_addr, b.device_mac" +
			" from tab_gw_device a,tab_devres_desc b where a.device_id=b.device_id and a.device_id='" + device_id + "'";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();

Map fields = DataSetBean.getRecord(sql);

String device_name = "";
String loopback_ip = "";
String device_mac = "";
String oui = "";
String device_serialnumber = "";

String device_swv = "";

String city_id = "";
String office_id = "";

String device_model = "";

String zone_id = "";
String device_addr = "";

if(fields != null){

	device_name = (String)fields.get("device_name");
	if(device_name == null){
		device_name = "";
	}
	
	loopback_ip = (String)fields.get("loopback_ip");
	if(loopback_ip == null){
		loopback_ip = "";
	}
	
	device_mac = (String)fields.get("device_mac");
	if(device_mac == null ){
		device_mac = "";
	}
	
	oui = (String)fields.get("oui");
	if(oui == null){
		oui = "";
	} else {
		oui =(String) venderMap.get(oui)== null?"":(String) venderMap.get(oui);
	}
	
	device_serialnumber =  (String)fields.get("device_serialnumber");
	if(device_serialnumber == null){
		device_serialnumber = "";
	}
	Map modelMap = DeviceAct.getDevice_Model();
	device_model = (String)fields.get("device_model_id");
	if(device_model != null){
		device_model = (String)modelMap.get(device_model);
	}else{
		device_model = "";
	}
	
	device_swv = (String)fields.get("device_swv");
	if(device_swv == null){
		device_swv = "";
	}
	
	city_id = (String)fields.get("city_id");
	
	if(city_id == null){
		city_id = "";
	} else {
		city_id = (String)map.get("city_id")== null?"":(String)map.get("city_id");
	}
	
	office_id = (String)fields.get("office_id");
	if(office_id == null){
		office_id = "";
	} else {
		office_id = (String)map.get("office_id")== null?"":(String)map.get("office_id");
	}
	
	zone_id = (String)fields.get("zone_id");
	if(zone_id == null){
		zone_id = "";
	} else {
		zone_id = (String)map.get("zone_id")== null?"":(String)map.get("office_id");
	}
	
	device_addr = (String)fields.get("device_addr");
	if(device_addr == null){
		device_addr = "";
	}
	
}

%>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="99%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							设备监控
						</td>
					</tr>
				</table>
			</td>
		</TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="4" align="center">显示〖<%=device_name%>〗设备</TH>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">厂商代码</TD>
					<TD><%=oui%></TD>
					<TD class=column align="right">设备型号</TD>
					<TD ><%=device_model%></TD>					
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">软件版本</TD>
					<TD><%=device_swv%></TD>
					<TD class=column align="right">设备序列号</TD>
					<TD colspan="1"><%=device_serialnumber%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">设备域名</TD>
					<TD><%=loopback_ip%></TD>
					<TD class=column align="right">MAC地址</TD>
					<TD><%=device_mac%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">属地标识</TD>
					<TD><%=city_id%></TD>
					<TD class=column align="right">局向标识</TD>
					<TD> <%=office_id%></TD>
				</TR>
				<TR bgcolor="#FFFFFF" height="20">
					<TD class=column align="right">小区标识</TD>
					<TD><%=zone_id%></TD>
					<TD class=column align="right">设备详细地址</TD>
					<TD><%=device_addr%></TD>
				</TR>

				<TR>
					<TD colspan="4" align="center" class=foot>
						<INPUT TYPE="submit" value=" 关 闭 " class=btn onclick="javascript:window.close();">
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<br>
<br>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../foot.jsp"%>