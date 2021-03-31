<%
response.setContentType("text/plain");
response.setHeader("Content-disposition","attachment; filename=example.csv" );
out.println("设备名称,OUI,设备序列号,设备型号ID(必须为数字),电信维护密码");
%>
<%@ page contentType="text/html;charset=GBK"%>

