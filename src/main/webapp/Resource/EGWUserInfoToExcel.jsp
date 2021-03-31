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
	//所有EGW用户列表
	list = EGWUserInfoAct.getEGWUsersCursorExcel(request);
	Cursor cursor = (Cursor) list.get(0);
	Map map = cursor.getNext();
%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<HTML>
<HEAD>
<TITLE>导出企业网关用户信息</TITLE>
<META HTTP-EQUIV="Content-Type"
	CONTENT="Application/msexcel; charset=gb2312">
<style>
TD {
	FONT-FAMILY: "宋体", "Tahoma";
	FONT-SIZE: 12px;
	mso-number-format:"\@";
}
</style>

</HEAD>

<BODY>
<TABLE border=1 cellspacing=0 cellpadding=2 borderColorLight="#000000"
	borderColorDark="#FFFFFF" width="90%">
	<TR>
		<TD><B>用户帐户</B></TD>
		<TD><B>客户名称</B></TD>
		<TD><B>属地</B></TD>
		<TD><B>是否绑定设备</B></TD>
		<TD><B>受理时间</B></TD>
	</TR>
	<%
		//在从数据库中 统计 业务属地对应的数据
		DeviceAct devAct = new DeviceAct();
		// 属地MAP 当前用户看到本身及其下属地市
		HashMap cityMap = devAct.getCityMap_All();

		String cust_type = "";
		String bindType = "";
		while (map != null) {
			cust_type = "";
			if ("0".equals((String) map.get("cust_type_id"))) {
				cust_type = "公司客户";
			} else if ("1".equals((String) map.get("cust_type_id"))) {
				cust_type = "网吧客户";
			} else if ("2".equals((String) map.get("cust_type_id"))) {
				cust_type = "个人客户";
			}

			//String user_type_id = (String)map.get("user_type_id");
			//String tmp = "手工添加";
			//if(user_type_id != null && !user_type_id.equals("")){
			//	if(user_type_id.equals("1")){
			//		tmp = "现场安装";
			//	} else if(user_type_id.equals("2")){
			//		tmp = "BSS用户";
			//	} else {
			//		tmp = "手工添加";
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
				bindType = "否";
			else
				bindType = "是";
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