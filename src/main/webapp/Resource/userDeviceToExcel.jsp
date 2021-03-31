<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=user.xls" );

long areaid = curUser.getAreaId();

//-----------------------------------------------------------------
String gw_type = request.getParameter("gw_type");

String tab_name = "";

//家庭网关
if (gw_type == null || gw_type.equals("") || gw_type.equals("1")) {

	tab_name = "tab_hgwcustomer";
	gw_type = "1";
//企业网关
} else if ("2".equals(gw_type)) {
	tab_name = "tab_egwcustomer";
	gw_type = "2";
//snmp设备
} else {
	tab_name = "tab_egwcustomer";
	gw_type = "3";
}
//-----------------------------------------------------------------

String sql = "";


if (curUser.getUser().isAdmin()) {
	
	if (!"3".equals(gw_type)){
		sql = "select b.username,a.oui,a.vendor_id,a.device_serialnumber,a.devicetype_id,a.city_id,a.gather_id,b.serv_type_id"
			+ " from tab_gw_device a, "
			+ tab_name
			+ "  b"
			+ " where a.device_id = b.device_id"
			+ " and a.cpe_allocatedstatus=1 and b.user_state in ('1','2')";
	}
	else{
		sql = "select b.username,a.device_serialnumber,a.device_model,a.city_id,a.gather_id"
			+ " from tab_deviceresource a, cus_radiuscustomer b"
			+ " where a.device_id = b.device_id"
			+ " and a.device_status != -1 and b.user_state in ('1','2')";
	}
	
} else {
	
	if (!"3".equals(gw_type)){
		sql = "select b.username,a.oui,a.vendor_id,a.device_serialnumber,a.devicetype_id,a.city_id,a.gather_id,b.serv_type_id"
			+ " from tab_gw_device a, "
			+ tab_name
			+ " b, tab_gw_res_area c"
			+ " where a.device_id = b.device_id and c.res_id = a.device_id and c.res_type=1 and c.area_id="
			+ areaid
			+ " and a.cpe_allocatedstatus=1 and b.user_state in ('1','2')";
	}
	else{
		sql = "select b.username,a.device_serialnumber,a.device_model,a.city_id,a.gather_id"
			+ " from tab_deviceresource a, cus_radiuscustomer b, tab_gw_res_area c"
			+ " where a.device_id = b.device_id and c.res_id = a.device_id and c.res_type=1 and c.area_id="
			+ areaid
			+ " and a.device_status != -1 and b.user_state = in ('1','2')";
	}
	
}
sql += " order by a.city_id";

Cursor cursor = DeviceAct.getSth(sql);
Map fields = cursor.getNext();


Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct.getOUIDevMap();

//所有设备型号对应信息
Map deviceTypeMap = DeviceAct.getDeviceTypeMap();
%>
<HTML>
<HEAD>
<TITLE>导出用户设备信息</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">
<style>
TD{
  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 12px;
}
</style>
</HEAD>
<BODY>
	<table width="100%" border=0 cellspacing=1 cellpadding=2>
		<tr>
			<th>属地</th>
			<th>设备厂商</th>
			<th>设备型号</th>
			<th>设备序列号</th>
			<th>ADSL帐号</th>
			<th>IPTV帐号</th>
		</tr>
					<%
							if (fields != null) {
							while (fields != null) {
								String device_id = (String) fields.get("device_id");

								String username1 = "";
								String username2 = "";


								String serviceID1 = "";
								String serviceID2 = "";


//								if (!"3".equals(gw_type)){
//									if (gw_type.equals("1")) {
//										serviceID1 = "10";
//										serviceID2 = "11";
//
//									} else {
//										serviceID1 = "50";
//										serviceID2 = "51";
//
//									}
									
								//ADSL帐号
								
							
							username1 = (String) fields.get("username");

							//显示地市
							String city_name = "";
							if (city_Map.get(fields.get("city_id")) != null) {
								city_name = (String) city_Map.get(fields
								.get("city_id"));
							}
							
							//厂商
							String vendor_str = "";
							//if ("3".equals(gw_type)){
								vendor_str = (String)venderMap.get(fields.get("vendor_id"));
							//}
							//else{
								//vendor_str = (String)venderMap.get(fields.get("oui"));
							//}
							
							//型号
							String deviceType_str = "";
							if ("3".equals(gw_type)){
								deviceType_str = (String)fields.get("device_model");
							}
							else{
								String[] tmp = (String[])deviceTypeMap.get(fields.get("devicetype_id"));
								if (tmp != null && tmp.length==2) {
									deviceType_str = tmp[0];		
								}
							}
					%>
					<tr>
						<td class=column width="10%" nowrap>
							<%=city_name%>
						</td>
						<td class=column width="15%" nowrap>
							<%=vendor_str%>
						</td>
						<td class=column width="15%" nowrap>
							<%=deviceType_str%>
						</td>
						<td class=column width="30%" nowrap>
							<%=fields.get("device_serialnumber")%>
						</td>
						<td class=column width="15%" nowrap>
							<%=username1%>
						</td>
						<td class=column width="15%" nowrap>
							<%=username2%>
						</td>
					</tr>
		<%
					fields = cursor.getNext();
				}
			} else {
		%>
			<tr>
				<td colspan=6 align=left class=column>
					系统没有关联的用户信息设备
				</td>
			</tr>
		<%
			}
		%>
	</table>
</BODY>
</HTML>