<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="EGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.EGWUserInfoAct" />
<%@ page
	import="java.text.SimpleDateFormat,java.util.*,com.linkage.litms.*,com.linkage.litms.resource.DeviceAct"%>
<%--
	zhaixf(3412) 2008-04-22
	JSDX_ITMS-BUG-YHJ-20080421-001
--%>
<%
	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition", "attachment; filename=user.xls");
	ArrayList list = new ArrayList();
	list.clear();
	//����EGW�û��б�
	list = EGWUserInfoAct.getEGWUsersCursorExcel(request);
	Cursor cursor = (Cursor) list.get(0);
	Map map = cursor.getNext();
%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<HTML>
<HEAD>
<TITLE>������ҵ�����û���Ϣ</TITLE>
<META HTTP-EQUIV="Content-Type"
	CONTENT="Application/msexcel; charset=gb2312">
<style>
TD {
	FONT-FAMILY: "����", "Tahoma";
	FONT-SIZE: 12px;
	mso-number-format:"\@";
}
</style>

</HEAD>

<BODY>
<TABLE border=1 cellspacing=0 cellpadding=2 borderColorLight="#000000"
	borderColorDark="#FFFFFF" width="90%">
	<TR>
		<TD><B>�û��ʻ�</B></TD>
		<TD><B>�ͻ�����</B></TD>
		<TD><B>����</B></TD>
		<TD><B>�Ƿ���豸</B></TD>
		<TD><B>����ʱ��</B></TD>
	</TR>
	<%
		//�ڴ����ݿ��� ͳ�� ҵ�����ض�Ӧ������
		DeviceAct devAct = new DeviceAct();
		// ����MAP ��ǰ�û�������������������
		HashMap cityMap = devAct.getCityMap_All();

		String cust_type = "";
		String bindType = "";
		while (map != null) {
			cust_type = "";
			if ("0".equals((String) map.get("cust_type_id"))) {
				cust_type = "��˾�ͻ�";
			} else if ("1".equals((String) map.get("cust_type_id"))) {
				cust_type = "���ɿͻ�";
			} else if ("2".equals((String) map.get("cust_type_id"))) {
				cust_type = "���˿ͻ�";
			}

			//String user_type_id = (String)map.get("user_type_id");
			//String tmp = "�ֹ����";
			//if(user_type_id != null && !user_type_id.equals("")){
			//	if(user_type_id.equals("1")){
			//		tmp = "�ֳ���װ";
			//	} else if(user_type_id.equals("2")){
			//		tmp = "BSS�û�";
			//	} else {
			//		tmp = "�ֹ����";
			//	}
			//}

			String cityId = (String) map.get("city_id");
			String cityName = (String) cityMap.get(cityId);
			if (cityName == null || "".equals(cityName))
				cityName = "-";

			String customer_name = (String)map.get("customer_name");
			if(customer_name == null || "".equals(customer_name))
				customer_name = "";
			
			Calendar time = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			String formatTime = "";
			if (map.get("dealdate") != null && !map.get("dealdate").equals("")
					&& !map.get("dealdate").equals("0")) {
				time.setTimeInMillis((Long.parseLong((String) map.get("dealdate"))) * 1000);
				formatTime = df.format(time.getTime());
			} else {
				formatTime = "-";
			}

			String serv_type = (String) map.get("serv_type_id");
			serv_type = (Global.Serv_type_Map.get(serv_type) == null) ? ""
					: (String) Global.Serv_type_Map.get(serv_type);

			if (map.get("device_serialnumber").equals("")
					|| map.get("device_serialnumber") == null)
				bindType = "��";
			else
				bindType = "��";
	%>
	<TR>
		<TD><%=(String) map.get("username")%></TD>
		<TD><%=customer_name%></TD>
		<TD><%=cityName%></TD>
		<TD><%=bindType%></TD>
		<TD><%=formatTime%></TD>
	</TR>
	<%
		map = cursor.getNext();
		}
	%>
</TABLE>
</BODY>
</HTML>