<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<%//已配置性能表达式列表
	String device_id=request.getParameter("device_id");
	String expressionid=request.getParameter("expressionid");
	String sql = "";
	// teledb
	if (DBUtil.GetDB() == 3) {
		sql=" delete from pm_map_instance where device_id='"+device_id+"' and expressionid="+expressionid;
		sql+="delete from pm_map where device_id='"+device_id+"' and expressionid="+expressionid;
	}
	else {
		sql=" delete pm_map_instance where device_id='"+device_id+"' and expressionid="+expressionid;
		sql+="delete pm_map where device_id='"+device_id+"' and expressionid="+expressionid;
	}
	com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
	psql.getSQL();
	int result=DataSetBean.executeUpdate(sql);
	if(result==1){
		out.println(true);
	}else{
		out.println(false);
	}
%>