<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>

<%
String hostip = request.getParameter("hostip");
String attr_id = request.getParameter("attr_id");

Cursor cursor = DataSetBean.getCursor("select instance from tab_objectdef where hostip = '" + hostip + "' and attr_id = " + attr_id);
Map fields = cursor.getNext();

String strInstanceList = "";
while (fields != null){
	strInstanceList = "<input type=checkbox name=instance value=" + fields.get("instance") + ">" + fields.get("instance");
	fields = cursor.getNext();
}

%>

<SCRIPT LANGUAGE="JavaScript">
parent.document.all("divHostIP").innerHTML = "<%=strInstanceList%>";
</SCRIPT>