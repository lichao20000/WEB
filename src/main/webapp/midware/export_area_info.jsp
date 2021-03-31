<%@ page contentType="application/vnd.ms-excel;charset=GBK"%>
<%@ page import="com.linkage.litms.midware.ExportMidwareData" %>

<%
response.setHeader("Content-Disposition","attachment; filename=batch_area_info.xls");
ExportMidwareData.getAreaExcelFile(response.getOutputStream());
%>

