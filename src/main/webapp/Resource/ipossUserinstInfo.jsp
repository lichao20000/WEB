<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@page
	import="java.util.List,com.linkage.litms.common.util.StringUtils,com.linkage.litms.*"%>
<%@page import="java.util.Map"%>
<%--
	zhaixf(3412) 2008-04-22
	JSDX_ITMS-BUG-YHJ-20080421-001
--%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<jsp:useBean id="UserInstAct" scope="request"
	class="com.linkage.litms.resource.UserInstAct" />

<%
	request.setCharacterEncoding("GBK");
	String deviceStatus = request.getParameter("status");

	String detailStr = "DetailDevice('?')";

	List list = null;
	String strData = "";
	//iposs ��ȡ���豸�б����ݲ���ֻΪ ALL
	if (deviceStatus != null && !"".equals(deviceStatus)
			&& "All".equals(deviceStatus)) {
		list = UserInstAct.getAllDeviceInfoList(request);
	} else {
		list = UserInstAct.getDeviceInfoList(request);
	}

	String strBar = String.valueOf(list.get(0));
	Cursor cursor = (Cursor) list.get(1);
	Map fields = cursor.getNext();
	Map venderMap = DeviceAct.getOUIDevMap();

	//add by benyp
	String devicetype_id = null;
	String devicemodel = null;
	String softwareversion = null;

	//��ȡ�豸��Ϣ
	Map deviceTypeMap = UserInstAct.getDeviceTypeMap();

	//��־λ
	String flag = "";

	if (fields == null) {
		Map map = UserInstAct.getDeviceUserInfo(request);
		if(map != null && !map.isEmpty()){
			strData = "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'>"
				+ "<TR><TD class=column COLSPAN=10 HEIGHT=30>"
				+ "�豸��" + map.get("device_serialnumber")
				+ "�Ѿ���:" + CityDAO.getCityIdCityNameMap().get(map.get("city_id"))
				+ "�û�" + map.get("username")
				+ "��</TD></TR></TABLE>";
		}else{
			strData = "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'>"
				+ "<TR><TD class=column COLSPAN=10 HEIGHT=30>"
				+ "��ϵͳû�з���Ҫ����豸��Դ</TD></TR></TABLE>";
		}
	}else{
		String device_id = null;
		strData += "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'>";
		
		while (fields != null) {
			
			String gw_type = (String)fields.get("gw_type");
			
			//������͵ķָ���

			if ("1".equals(gw_type)){
				if(flag.equals("")){
					strData += "<tr><th colspan='6'>��ͥ�����豸</th></tr>";
					strData += "<TR><td nowrap  class='green_title2'>ѡ��</td>"
							+ "<td class='green_title2'>�豸����</td><td nowrap class='green_title2'>�ͺ�</td>" 
							+ "<td nowrap class='green_title2'>����汾</td><td nowrap class='green_title2'>�豸���к�</td>" 
							+ "<td nowrap class='green_title2'>��ϸ��Ϣ</td></TR>";
							flag = "1";
				}
			}
			else{				
				if(flag.equals("1") || flag.equals("")){
					strData += "<tr><th colspan='6'>��ҵ�����豸</th></tr>";
					strData += "<TR><td nowrap  class='green_title2'>ѡ��</td>"
							+ "<td class='green_title2'>�豸����</td><td nowrap class='green_title2'>�ͺ�</td>" 
							+ "<td nowrap class='green_title2'>����汾</td><td nowrap class='green_title2'>�豸���к�</td>" 
							+ "<td nowrap class='green_title2'>��ϸ��Ϣ</td></TR>";
							flag = "2";
				}
			}
				
			
			device_id = (String) fields.get("device_id");
			device_id = device_id.replaceAll("\\+", "%2B");
			device_id = device_id.replaceAll("&", "%26");
			device_id = device_id.replaceAll("#", "%23");

			//add by benyp
			devicetype_id = (String) fields.get("devicetype_id");
			String[] tmp = (String[])deviceTypeMap.get(devicetype_id);
			if (tmp != null && tmp.length==2) {
				devicemodel = tmp[0];
				softwareversion = tmp[1];			
			}
			else{
				devicemodel = "";
				softwareversion = "";
			}

			strData += "<TR bgcolor=#FFFFFF><TD class=column2 align='center' width='5%'><input type=radio name=chkCheck value='"
					+ device_id
					+ "#"
					+ (String) fields.get("device_serialnumber")
					+ "'></TD>";

			strData += "<TD class=column2 width='20%'>"
					+ venderMap.get(fields.get("vendor_id")) + "</TD>";
			strData += "<TD class=column2 width='10%'>" + devicemodel
					+ "</TD>";
			strData += "<TD class=column2 width='15%'>"
					+ softwareversion + "</TD>";
			strData += "<TD class=column2 width='35%'>"
					+ (String) fields.get("device_serialnumber")
					+ "</TD>";
			strData += "<TD class=column2 width='35%'>"
					+ "<a href='#' onclick=\\\""
					+ StringUtils.replace(detailStr, "?", device_id)
					+ "\\\">��ϸ��Ϣ</a>" + "</TD>";
			strData += "</TR>";
			fields = cursor.getNext();
		}

		strBar = strBar.replaceAll("\"", "'");

		strData += "<TR bgcolor=#FFFFFF><TD class=column COLSPAN=10 align=right>"
				+ strBar + "</TD></TR></TBALE>";

	}
%>
<script LANGUAGE="JavaScript">
parent.document.all("userTable").innerHTML = "<%=strData%>";
parent.document.all("dispTr").style.display="";
</script>




