<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="code" scope="request" class="com.linkage.litms.system.CodeList"/>

<%
request.setCharacterEncoding("GBK");
//String strSQL = "select id,name,type,type_id from tab_code order by type,type_id";
int offset = Integer.parseInt(request.getParameter("offset"));
int pagelen = Integer.parseInt(request.getParameter("pagelen"));

Cursor cursor= (Cursor)code.getCodeList(request).get(1);
int count = cursor.getRecordSize();
Map fields = cursor.getNext();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Output Name=\"code_list\" Title=\"代码列表\" IsSelect=\"true\" ColCount=\"4\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\"代码类别\"  />");
	out.println("<Column fieldname=\"fields2\" remark=\"代码类别索引号\"  />");
	out.println("<Column fieldname=\"fields3\" remark=\"代码值\"  />");
	out.println("</Columns>");
	int i = 0;
	String bgClr = "#FFFFFF";
	String tmp;
	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";
		//tmp = (String)fields.get("TYPE_ID".toLowerCase());
		//tmp = (tmp==null)?"":tmp;
		out.println("<Row primarykey=\""+ fields.get("ID".toLowerCase()) +"\" bgcolor=\""+ bgClr +"\">");
		out.println("<fields1 value=\""+ fields.get("TYPE".toLowerCase()) +"\" />");
		out.println("<fields2 value=\""+ fields.get("TYPE_ID".toLowerCase()) +"\" />");
		out.println("<fields3 value=\""+ fields.get("NAME".toLowerCase()) +"\" />");
		out.println("</Row>");
		
		i++;
		fields = cursor.getNext();
	}
}
out.println("</Output>");
%>

