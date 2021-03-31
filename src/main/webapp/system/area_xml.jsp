<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@ page import="java.util.*,com.linkage.litms.common.database.*"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>

<%
request.setCharacterEncoding("GBK");
Cursor cursor = areaManage.getAreaInfoAll();

if(request.getParameter("area_pid") != null){
	//cursor = areaManage.getAreaInfosOfAreaPId(request.getParameter("area_pid"));
}

String target = "";
String sqlvalue="";
Map fields = cursor.getNext();

out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Tree>");
out.println("<TreeView isManage=\"true\" title=\"ÇøÓò¹ÜÀí\" />");
while(fields != null){
	out.println("<TreeNode title=\""+fields.get("area_name")+"\" href=\""+sqlvalue+"\" id=\""+fields.get("area_id")+"\" remark=\""+fields.get("remark")+"\" target=\""+target+"\" pid=\""+fields.get("area_pid")+"\" layer=\""+fields.get("area_layer")+"\" ishas=\"1\" />");

	fields = cursor.getNext();
}

out.println("</Tree>");

fields = null;
cursor = null;
%>
