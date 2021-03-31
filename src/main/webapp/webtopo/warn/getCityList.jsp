<%@ include file="../../timelater.jsp"%>
<%@page import="com.linkage.litms.common.filter.SelectCityFilter" %>
<%@page import="com.linkage.commons.db.PrepareSQL"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.util.FormUtil" %>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String city_id = curUser.getCityId();
SelectCityFilter CityFilter = new SelectCityFilter(request);
PrepareSQL pSQL = new PrepareSQL("select city_id,city_name,parent_id from tab_city where city_id in (?) order by city_id");
pSQL.setStringExt(1, CityFilter.getAllSubCityIds(city_id, true), false);
Cursor cursor = DataSetBean.getCursor(pSQL.getSQL());
String strCityList = FormUtil.createListBox(cursor, "city_name",
		"city_name", false, city_id, "cityName");
%>
<SPAN ID="child"><%=strCityList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("strCityList").innerHTML = child.innerHTML;
//-->
</SCRIPT>

