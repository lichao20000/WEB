<%@page import="java.io.IOException"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page errorPage="../dgError.jsp" %>
<%
String strSQL = "select id,name,type,type_id from tab_code order by type,type_id";
Cursor cursor= DataSetBean.getCursor(strSQL);
Map fields = cursor.getNext();
boolean isSuccess = false;
try{
	String path = LipossGlobals.getLipossHome() + File.separator + "Js" + File.separator + "code.xml";
	PrintWriter xml = new PrintWriter(new FileOutputStream(path),true);
	xml.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
	xml.println("<CODE>");

	while(fields != null){
		xml.println("<"+fields.get("TYPE".toLowerCase())+" sid=\""+ fields.get("ID".toLowerCase())+"\" value=\""+ fields.get("NAME".toLowerCase()) +"\" tid=\""+ fields.get("TYPE_ID".toLowerCase()) +"\" />");

		fields = cursor.getNext();
	}

	xml.println("</CODE>");

	xml.close();
	isSuccess = true;
}
catch(IOException e){
}
%>