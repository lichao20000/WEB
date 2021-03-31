<%@page import="java.util.HashMap"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
//String strSQL = "select a.*,(select group_name from tab_group where group_oid=a.group_poid) as group_pname from tab_group a";
String strSQL = (String)session.getValue("groupSQL");

int offset = Integer.parseInt(request.getParameter("offset"));
int pagelen = Integer.parseInt(request.getParameter("pagelen"));

Cursor cursor= DataSetBean.getCursor(strSQL,offset,pagelen);
int count = cursor.getRecordSize();
Map fields = cursor.getNext();

//modify 2005-5-11 by yuht CHG-WEB-JSDX-BUG-20050511-YHT-001
//modify 2006-12-14 by lizj 原因：编辑用户组时，如果父用户组选择自己，那么列表里面的父用户组名为空！
//long acc_oid = user.getId();
strSQL = "select group_oid,group_name from tab_group";// where group_oid in (select group_oid from tab_acc_group where acc_oid=" + acc_oid + ") or group_poid in (select group_oid from tab_acc_group where acc_oid=" + acc_oid  + ")";
Cursor c = DataSetBean.getCursor(strSQL);
Map f = c.getNext();
HashMap map = new HashMap();
while(f!=null){
	map.put((String)f.get("group_oid"),(String)f.get("group_name"));
	f = c.getNext();
}
map.put("0"," ");
c = null;
f = null;
//end
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Output Name=\"group_list\" Title=\"用户组列表\" IsSelect=\"true\" ColCount=\"4\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\"组名\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields2\" remark=\"父组名\" datatype=\"String\" />");
	out.println("<Column fieldname=\"fields3\" remark=\"描述\" datatype=\"String\" />");
	out.println("</Columns>");
	int i = 0;
	String bgClr = "#FFFFFF";

	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";

		out.println("<Row primarykey=\""+ fields.get("group_oid") +"\" bgcolor=\""+ bgClr +"\">");
		out.println("<fields1 value=\""+ fields.get("group_name") +"\" />");
		out.println("<fields2 value=\""+ map.get((String)fields.get("group_poid")) +"\" />");
		out.println("<fields3 value=\""+ fields.get("group_desc") +"\" />");
		out.println("</Row>");
		
		i++;
		fields = cursor.getNext();
	}
}
out.println("</Output>");

map.clear();
map = null;
%>
