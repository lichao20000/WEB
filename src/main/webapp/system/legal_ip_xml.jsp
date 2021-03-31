<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/xml;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
Cursor cursor = (Cursor)session.getValue("legal_ip_data");
int count = cursor.getRecordSize();
Map fields = cursor.getNext();
out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
out.println("<Output Name=\"legal_ip_list\" Title=\"合法模块IP列表\" IsSelect=\"true\" ColCount=\"4\">");
if(count>0){
	out.println("<Columns>");
	out.println("<Column fieldname=\"fields1\" remark=\"IP地址\"  />");
	out.println("<Column fieldname=\"fields2\" remark=\"校验字节串\"  />");
	out.println("<Column fieldname=\"fields3\" remark=\"描述\"  />");
	out.println("</Columns>");
	int i = 0;
	String bgClr = "#FFFFFF";
	while(fields != null){
		if((i % 2) == 0)
			bgClr = "#FFFFFF";
		else
			bgClr = "#F6F6F6";

		out.println("<Row primarykey=\""+ fields.get("LEGAL_IP".toLowerCase()) +"\" bgcolor=\""+ bgClr +"\">");
		out.println("<fields1 value=\""+ fields.get("LEGAL_IP".toLowerCase()) +"\" />");
		out.println("<fields2 value=\""+ fields.get("IP_PIN".toLowerCase()) +"\" />");
		out.println("<fields3 value=\""+ fields.get("IP_DESC".toLowerCase()) +"\" />");
		out.println("</Row>");
		
		i++;
		fields = cursor.getNext();
	}
}
out.println("</Output>");

session.putValue("legal_ip_data",null);
%>

