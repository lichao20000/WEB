<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%> 
<jsp:useBean id="city" scope="request" class="com.linkage.module.gwms.dao.tabquery.CityDAO"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
//DataSetBean db = new DataSetBean();
String str_city_id = request.getParameter("city_id");

Map fields = city.getCityInfoById(str_city_id);

%>
<SCRIPT LANGUAGE="JavaScript">
//<!--
parent.document.frm.city_id_old.value='<%=(String)fields.get("city_id")%>';
parent.document.frm.city_id.value='<%=(String)fields.get("city_id")%>';
parent.document.frm.city_id.readOnly = true;
parent.document.frm.city_name.value='<%=(String)fields.get("city_name")%>';
parent.document.frm.parent_id.value='<%=(String)fields.get("parent_id")%>';
parent.document.frm.parent_id_old.value='<%=(String)fields.get("parent_id")%>';
parent.document.frm.staff_id.value ='<%=(String)fields.get("staff_id")%>';
parent.document.frm.remark.value ='<%=(String)fields.get("remark")%>';
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';
parent.cityLabel.innerHTML = '¡¼<%=(String)fields.get("city_name")%>¡½';
//-->
</SCRIPT>