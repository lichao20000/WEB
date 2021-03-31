<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.commons.db.PrepareSQL"%>
<%@page import="com.linkage.commons.db.DBUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>

<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
	request.setCharacterEncoding("GBK");
	String vendor_id = request.getParameter("vendor_id");
	String flag = request.getParameter("flag");
	String gw_type = request.getParameter("gw_type");
	
	PrepareSQL psql = new PrepareSQL("select a.devicetype_id,"); 
	if(DBUtil.GetDB() == 1){
		 psql.append("b.device_model||'('||a.softwareversion||')' as device_model ");
	 }
	// teledb
	else if (DBUtil.GetDB() == 3) {
		psql.append("concat(b.device_model, '(', a.softwareversion, ')') as device_model ");
	}
	else{
		 psql.append("b.device_model+'('+a.softwareversion+')' as device_model ");
	 }

     if("4".equals(gw_type)){
    	 psql.append("from stb_tab_devicetype_info a,stb_gw_device_model b ");
     }else{
    	 psql.append("from tab_devicetype_info a,gw_device_model b ");
     }
	 
	 psql.append("where a.device_model_id=b.device_model_id and a.vendor_id=b.vendor_id and a.vendor_id='"+vendor_id+"'");
	 
	Cursor cursor = DataSetBean.getCursor(psql.getSQL());
	String _element = "";
	String strChildList = "";
	if(flag != null && !flag.equals("")){
		_element = "_div_devicetype";
		strChildList = FormUtil.createListBox(cursor, "devicetype_id","device_model", true, "", "_devicetype_id");
	} else {
		_element = "div_devicetype";
		strChildList = FormUtil.createListBox(cursor, "devicetype_id","device_model", false, "", "");
	}
%>
<html>
<body>
<SPAN ID="child"><%=strChildList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("<%=_element%>").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>