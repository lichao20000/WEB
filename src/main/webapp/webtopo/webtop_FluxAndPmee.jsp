<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<% 
String device_id= request.getParameter("device_id"); 
%>


<html>
<frameset rows="60%,40%" border="0">
        <frame name="searchForm" scrolling="no" marginheight="0" src="./webtop_Flux.jsp?device_id=<%= device_id%>">
        <frame name="dataForm" scrolling="no" marginheight="0" src="./webtop_pmee.jsp?device_id=<%= device_id%>">
</frameset>
</html>

