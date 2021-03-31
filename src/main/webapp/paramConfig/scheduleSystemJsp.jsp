<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.paramConfig.ScheludeJsp"%>
<%
request.setCharacterEncoding("GBK");
//String strGatherId = request.getParameter("gather_id");
String sys_item = request.getParameter("sys_item");
ScheludeJsp schelude = new ScheludeJsp();

//根据sheet_id获取到gather_id
//String strGatherId = jsp.getGatherId(sheet_id);

schelude.systemManage(sys_item);

//clear parameter 
sys_item = null;
schelude = null;
%>