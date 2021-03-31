<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="com.linkage.litms.common.util.CommonMap"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page contentType="text/html;charset=GBK"%>

<%
	request.setCharacterEncoding("GBK");
	//DataSetBean db = new DataSetBean();
	String str_gather_id = request.getParameter("gather_id");
	String str_gather_name = request.getParameter("gather_name");
	
	Map gatherMap = CommonMap.getGatherMap();
	str_gather_name = (String)gatherMap.get(str_gather_id);
	
	String sql = "select * from gw_pm_config_time where gather_id='"
			+ str_gather_id + "' order by gather_id";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql = "select time from gw_pm_config_time where gather_id='"
				+ str_gather_id + "' order by gather_id";
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	Cursor cursor = DataSetBean.getCursor(sql);
	Map fields = cursor.getNext();

	String time = "";

	while (fields != null) {
		time = (String) fields.get("time");
		
		fields = cursor.getNext();
	}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var time = "<%=time%>";
var hour = time.split(":")[0];
var min = time.split(":")[1];

parent.document.frm.gather_time_h.value=hour;
parent.document.frm.gather_time_m.value=min;
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';

parent.document.all.addgatherLabel.style.display="none";
	
parent.document.all.gatherLabel.style.display="";

parent.gatherLabel.innerHTML = '<%=str_gather_name%>';
parent.document.all.add_gather_id.value='<%=str_gather_id%>';
parent.document.frm.hid_gather_id.value='<%=str_gather_id%>';
//-->
</SCRIPT>