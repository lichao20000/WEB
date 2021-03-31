<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="zone" scope="request" class="com.linkage.litms.resource.ZoneAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String str_zone_id = request.getParameter("zone_id");
Map fields = zone.getZoneInfo(str_zone_id);
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.frm.zone_id_old.value='<%=(String)fields.get("zone_id")%>';
parent.document.frm.zone_id.value='<%=(String)fields.get("zone_id")%>';
parent.document.frm.zone_name.value='<%=(String)fields.get("zone_name")%>';
parent.document.frm.staff_id.value ='<%=(String)fields.get("staff_id")%>';
parent.document.frm.remark.value ='<%=(String)fields.get("remark")%>';
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';
parent.zoneLabel.innerHTML = '¡¼<%=(String)fields.get("zone_name")%>¡½';
//-->
</SCRIPT>