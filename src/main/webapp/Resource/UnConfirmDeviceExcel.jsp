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

			str_gw_type = "1".equals(fields.get("gw_type")) ? "��ͥ�����豸" : "��ҵ�����豸";
			
			devicetype_id = (String)fields.get("devicetype_id"); 
			//-1Ϊ�豸�ͺ��ǡ���ѡ��״̬���豸����ʱ����Ĭ��ֵ��
			
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
		deviceBuilder.append("<tr><td>û��δȷ�ϵ��豸��Դ<td></tr>");

	}
	deviceList = deviceBuilder.toString();
%>

<html>
<head>
<META HTTP-EQUIV="Content-Type"
	CONTENT="Application/msexcel; charset=gb2312">
<title>����δȷ���豸</title>

</head>
<body>
<table border=1 cellspacing=0 cellpadding=0>
	<tr>
		<th>�豸����</th>
		<th>�ͺ�</th>
		<th>����汾</th>
		<th>����</th>
		<th>�豸���к�</th>
		<th>�豸����</th>
	</tr>
	<%=deviceList%>
</table>
</body>
</html>