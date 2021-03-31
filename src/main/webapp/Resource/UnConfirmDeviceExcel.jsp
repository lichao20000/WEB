<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page
	import="com.linkage.litms.resource.UnfirmDeviceAct,com.linkage.litms.common.database.*,java.util.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>

<jsp:useBean id="commonAct" scope="request" class="com.linkage.litms.resource.commonAct"/>
<%
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition","attachment; filename=unfirmDevice.xls" );
	String deviceList = "";
	StringBuilder deviceBuilder = null;
	Cursor cursor = UnfirmDeviceAct.unConfirmDeviceList(request);
	Map fields = cursor.getNext();
	if (fields != null && !fields.isEmpty()) {
		String city_id = "";
		String city_name = "";
		String str_gw_type = "";
		String devicetype_id = "";
		String devicemodel = "";
		String softwareversion = "";
		deviceBuilder = new StringBuilder(100000);
		Map city_Map = CityDAO.getCityIdPidMap();
		Map venderMap = commonAct.getOUIDevMap();
		Map deviceTypeMap = UnfirmDeviceAct.getDeviceTypeMap();
		while (fields != null && !fields.isEmpty()) {

			city_id = (String) fields.get("city_id");
			city_name = (String) city_Map.get(city_id);
			city_name = city_name == null ? "&nbsp;" : city_name;

			str_gw_type = "1".equals(fields.get("gw_type")) ? "家庭网关设备" : "企业网关设备";
			
			devicetype_id = (String)fields.get("devicetype_id"); 
			//-1为设备型号是“请选择”状态（设备导入时给的默认值）
			
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
			deviceBuilder.append("<tr>");
			
			deviceBuilder.append("<td>");
			deviceBuilder.append(venderMap.get(fields.get("oui")));
			deviceBuilder.append("</td>");
			
			deviceBuilder.append("<td>");
			deviceBuilder.append(devicemodel);
			deviceBuilder.append("</td>");
			
			deviceBuilder.append("<td>");
			deviceBuilder.append(softwareversion);
			deviceBuilder.append("</td>");
			
			deviceBuilder.append("<td>");
			deviceBuilder.append(city_name);
			deviceBuilder.append("</td>");
			
			deviceBuilder.append("<td>");
			deviceBuilder.append(fields.get("device_serialnumber"));
			deviceBuilder.append("</td>");
			
			deviceBuilder.append("<td>");
			deviceBuilder.append(str_gw_type);
			deviceBuilder.append("</td>");
			
			deviceBuilder.append("</tr>");

			fields = cursor.getNext();
		}
	} else {
		deviceBuilder = new StringBuilder(50);
		deviceBuilder.append("<tr><td>没有未确认的设备资源<td></tr>");

	}
	deviceList = deviceBuilder.toString();
%>

<html>
<head>
<META HTTP-EQUIV="Content-Type"
	CONTENT="Application/msexcel; charset=gb2312">
<title>导出未确认设备</title>

</head>
<body>
<table border=1 cellspacing=0 cellpadding=0>
	<tr>
		<th>设备厂商</th>
		<th>型号</th>
		<th>软件版本</th>
		<th>属地</th>
		<th>设备序列号</th>
		<th>设备类型</th>
	</tr>
	<%=deviceList%>
</table>
</body>
</html>