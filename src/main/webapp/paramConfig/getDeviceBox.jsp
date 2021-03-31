<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.commons.db.PrepareSQL"%>
<%@page import="com.linkage.commons.db.DBUtil"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.commons.util.StringUtil"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>

<%
	request.setCharacterEncoding("GBK");
	String vendor_id = request.getParameter("vendor_id");
	String devicetype_id = request.getParameter("devicetype_id");
	String gw_type = request.getParameter("gw_type");
	String tabName = "tab_gw_device";
	if("4".equals(gw_type)){
		tabName = "stb_tab_gw_device";
	}
	
	PrepareSQL psql=new PrepareSQL("select a.device_id,");
	
	if(DBUtil.GetDB() == 1){
		psql.append("a.oui||'-'||a.device_serialnumber as device_name from "+tabName+" a ");
	}
	// teledb
	else if (DBUtil.GetDB() == 3) {
		psql.append("concat(a.oui, '-', a.device_serialnumber) as device_name from "+tabName+" a ");
	}
	else{
		psql.append("a.oui+'-'+a.device_serialnumber as device_name from "+tabName+" a ");
	}


	if (!user.isAdmin()){
		psql.append(", tab_gw_res_area b ");
	}
	psql.append("where a.device_status=1 ");

	if (!user.isAdmin()){
		psql.append("and b.res_type=1 and a.device_id=b.res_id and b.area_id="+ user.getAreaId() +" ");
	}
	if(!StringUtil.IsEmpty(vendor_id)){
		psql.append("and a.vendor_id='" + vendor_id+"' ");
	}
	if (!StringUtil.IsEmpty(devicetype_id)){
		psql.append(" and a.devicetype_id=" + devicetype_id+" ");
	}
	psql.append("order by a.device_id ");
	
	Cursor cursor = DataSetBean.getCursor(psql.getSQL());
	String strChildList =  FormUtil.createListBox(cursor, "device_id","device_name", false, "", "");
%>
<html>
<body>
<SPAN ID="child"><%=strChildList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("id_device").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>