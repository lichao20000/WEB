<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String title = request.getParameter("title");
Cursor cursor = (Cursor)session.getValue("legal_client_data");
int count = cursor.getRecordSize();
Map fields = cursor.getNext();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Output Name=\"legal_client_list\" Title=\""+title+"列表\" IsSelect=\"true\" ColCount=\"3\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\""+title+"标识\"  />");
	out.println("<Column fieldname=\"fields2\" remark=\"IP地址\"  />");
	out.println("</Columns>");
	int i = 0;
	String bgClr = "#FFFFFF";
	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";

		out.println("<Row primarykey=\""+ fields.get("SYSTEMID".toLowerCase()) +"\" bgcolor=\""+ bgClr +"\">");
		out.println("<fields1 value=\""+ fields.get("SYSTEMID".toLowerCase()) +"\" />");
		out.println("<fields2 value=\""+ fields.get("IPADDR".toLowerCase()) +"\" />");
		out.println("</Row>");
		
		i++;
		fields = cursor.getNext();
	}
}
out.println("</Output>");

session.putValue("legal_client_data",null);
%>

