<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="roleManage" scope="request" class="com.linkage.litms.system.dbimpl.RoleManagerSyb"/>
<%--
	zhaixf(3412) 2008-05-14
	req:NJLC_HG-BUG-ZHOUMF-20080508-001
--%>
<%
request.setCharacterEncoding("GBK");

Cursor cursor= roleManage.getAllRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
//Cursor cursor= roleManage.getRolesByRolePid(Integer.parseInt(String.valueOf(user.getRoleId())));
int count = cursor.getRecordSize();
Map fields = cursor.getNext();

out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Output Name=\"role_list\" Title=\"角色列表\" IsSelect=\"true\" ColCount=\"4\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\"角色名称\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields2\" remark=\"描述\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields3\" remark=\"是否可编辑\" datatype=\"Boolean\" />");
	out.println("</Columns>");
	int i = 0;
	String flg = "";
	String bgClr = "#FFFFFF";
	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";
		/* 2008-3-4 Zhaof
		 * 增加判断语句，如果是当前用户角色，则不显示
		 */
		if (!(fields.get("role_id").equals(user.getRoleId()))) {
			
			if(fields.get("acc_oid").equals(String.valueOf(user.getId()))){
				flg = "1";
			}
			out.println("<Row primarykey=\""+ fields.get("role_id") + "|||" + flg + "|||" + String.valueOf(user.getRoleId()) +"\" bgcolor=\""+ bgClr +"\">");
			out.println("<fields1 value=\"");
			out.println(String.valueOf(fields.get("role_name")).replaceAll("&", "&amp;"));
			out.println("\"/>");
			out.println("<fields2 value=\"");
			out.println(String.valueOf(fields.get("role_desc")).replaceAll("&", "&amp;"));
			out.println("\"/>");
			out.println("<fields3 value=\"");
			out.println("1".equals(flg)? "是":"否");
			out.println("\"/>");
			out.println("</Row>");
			i++;
			flg = "";
		}
		fields = cursor.getNext();
		//out.println("<![CDATA[");
		//out.println("]]>");
	}
}
out.println("</Output>");

//clear
fields = null;
cursor = null;
%>

