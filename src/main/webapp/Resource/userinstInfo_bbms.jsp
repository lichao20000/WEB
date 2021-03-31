<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page
	import="java.util.List,com.linkage.litms.common.util.StringUtils,com.linkage.litms.*"%>
<%@page import="java.util.Map"%>

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
	StringBuffer strBuff = new StringBuffer();

	if (deviceStatus != null && !"".equals(deviceStatus)
			&& "All".equals(deviceStatus)) {
		list = UserInstAct.getAllDeviceInfoList_bbms(request);
	} else {
		list = UserInstAct.getDeviceInfoList_bbms(request);
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

	if (fields == null) {
		strBuff.append("<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><TR><TD COLSPAN=10 HEIGHT=30 bgcolor='#FFFFFF'>");
		strBuff.append("<img src='../images/attention.gif' width='15' height='12'>&nbsp;<font color='red'>δ��ѯ���豸</font>&nbsp;������ԭ��1:<font color='red'>�豸δ�ϱ�</font>[�ɼ������]; 2:<font color='red'>�豸�Ѱ�</font>[��ͨ�����豸��������ѯ]��</TD></TR></TABLE>");
		strData = strBuff.toString();
	} else {
		String device_id = null;
		strData += "<TABLE border=0 cellspacing=1 cellpadding=2 width='100%'><TR><TH nowrap>ѡ��</TH>"
		+ "<TH>�豸����</TH><TH nowrap>�ͺ�</TH><TH nowrap>����汾</TH><TH nowrap>�豸���к�</TH><TH nowrap>��ϸ��Ϣ</TH></TR>";

		while (fields != null) {
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

			strData += "<TD class=column2 width='15%'>"
					+ venderMap.get(fields.get("vendor_id")) + "</TD>";
			strData += "<TD class=column2 width='15%'>" + devicemodel
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




