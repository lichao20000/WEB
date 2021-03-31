<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<jsp:useBean id="office" scope="request" class="com.linkage.litms.resource.OfficeAct"/>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
//DataSetBean db = new DataSetBean();
String str_office_id = request.getParameter("office_id");
Map fields = office.getOfficeInfo(str_office_id);
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.frm.office_id_old.value='<%=(String)fields.get("office_id")%>';
parent.document.frm.office_id.value='<%=(String)fields.get("office_id")%>';
parent.document.frm.office_name.value='<%=(String)fields.get("office_name")%>';
parent.document.frm.staff_id.value ='<%=(String)fields.get("staff_id")%>';
//parent.document.frm.city_id.value ='<%=(String)fields.get("city_id")%>';
parent.document.frm.remark.value ='<%=(String)fields.get("remark")%>';
parent.document.frm.action.value='update';
parent.actLabel.innerHTML = '±à¼­';
parent.officeLabel.innerHTML = '¡¼<%=(String)fields.get("office_name")%>¡½';
//-->
</SCRIPT>