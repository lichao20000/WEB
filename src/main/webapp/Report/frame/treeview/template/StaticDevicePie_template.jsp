<%@ include file="../../../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@ page import="com.linkage.litms.resource.VendorAct" %>
<%@ page import="java.util.Map"%>
<%@ include file="../../../../head.jsp"%>
<%@ include file="../../../../toolbar.jsp"%>
<%
 String city_id = request.getParameter("city_id");
String city_name=CityDAO.getCityIdCityNameMap().get(city_id);
VendorAct venAct = new VendorAct(request,response);
String html =venAct.getVendorPerfTable(city_id,city_name);
%>
<link rel="stylesheet" href="../../../../css/css_green.css" type="text/css">
<%=html%>
