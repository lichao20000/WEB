<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="roleManage" scope="request" class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/>

<%
request.setCharacterEncoding("GBK");

//Cursor cursor= roleManage.getAllRoles();
Cursor cursor= roleManage.getRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
int count = cursor.getRecordSize();
Map fields = cursor.getNext();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Output Name=\"role_list\" Title=\"角色列表\" IsSelect=\"true\" ColCount=\"3\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\"角色名称\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields2\" remark=\"描述\" datatype=\"String\" />");
	out.println("</Columns>");
	int i = 0;
	String bgClr = "#FFFFFF";

	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";

		out.println("<Row primarykey=\""+ fields.get("role_id") +"\" bgcolor=\""+ bgClr +"\">");
		out.println("<fields1 value=\""+ fields.get("role_name") +"\" />");
		out.println("<fields2 value=\""+ fields.get("role_desc") +"\" />");
		out.println("</Row>");
		
		i++;
		fields = cursor.getNext();
	}
}
out.println("</Output>");

//clear
fields = null;
cursor = null;
%>

