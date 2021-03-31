<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%
request.setCharacterEncoding("GBK");
String city_id = request.getParameter("city_id");
SelectCityFilter City = new SelectCityFilter(request);
String pid = City.getPidByCity_id(city_id);//如无父id这返回""
String city_name = City.getNameByCity_id(city_id);
String strCityList = City.Next_layer_CitySQL(city_id,true,"","");
strCityList += "<span id='my_city"+city_id+"'></span>";

%>
<body>
<!-- 用于调试时使用 -->
<%=strCityList%>
</body>
<SCRIPT LANGUAGE="JavaScript">
parent.document.all("my_city<%=pid%>").innerHTML = "<%=strCityList%>";
parent.document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
</SCRIPT>