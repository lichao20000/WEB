<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="systemMaintenance" scope="request" class="com.linkage.litms.system.SystemMaintenance"/>
<%
request.setCharacterEncoding("GBK");
String html = systemMaintenance.getPlanlogHtml(request);
%>
<SPAN ID="child"><%=html%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.divdata.innerHTML = child.innerHTML;
//-->
</SCRIPT>