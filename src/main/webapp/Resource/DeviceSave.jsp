<%@ include file="../timelater.jsp"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%-- <%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %> --%>

<%@ page contentType="text/html;charset=GBK"%>
<%--
	zhaixf(3412) 2008-05-04
	req:增加设备判断功能，如果设备已存在则不能添加
--%>
<%
	// Logger logger = LoggerFactory.getLogger(this.getClass());
	//此页面只需要将输出结果输出要页面中，调用页面只需要是
	request.setCharacterEncoding("GBK");
	String resultDevice = "";
	String action = request.getParameter("_action");
	
	if(action.equals("add")){
		String oui = request.getParameter("vendor_id");
		String device_serialnumber = request.getParameter("device_serialnumber");

		String sql = "select * from tab_gw_device where oui='"
					+ oui +"' and device_serialnumber='"
					+ device_serialnumber + "'";
		// teledb
		if (DBUtil.GetDB() == 3) {
			sql = "select device_serialnumber from tab_gw_device where oui='"
					+ oui +"' and device_serialnumber='"
					+ device_serialnumber + "'";
		}
		com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
		psql.getSQL();
		if(DataSetBean.getRecord(sql) != null){
			resultDevice = "<script language=\"javascript\">alert('该设备已存在,请继续添加其他设备!')</script>";
		}else{
			resultDevice = DeviceAct.getDeviceActMsg(request);
		}
	}else{
		resultDevice = DeviceAct.getDeviceActMsg(request);
	}
	out.println(resultDevice);
%>


<%=resultDevice%>