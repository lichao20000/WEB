<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.FormUtil"%>

<%
String gather_id = request.getParameter("gather_id");

Cursor cursor = DataSetBean.getCursor("select distinct hostip from tab_objectdef where gather_id = '" + gather_id + "'");
String strHostIPList = FormUtil.createListBox(cursor,"hostip","hostip",true,"","");
%>

<SCRIPT LANGUAGE="JavaScript">
parent.document.all("divHostIP").innerHTML = "<%=strHostIPList%>";
</SCRIPT>