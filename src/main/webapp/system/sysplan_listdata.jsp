<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="systemMaintenance" scope="request" class="com.linkage.litms.system.SystemMaintenance"/>
<%
request.setCharacterEncoding("GBK");
String html = systemMaintenance.getPlanHtml(request);
%>
<SPAN ID="child"><%=html%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.div_sysplan.innerHTML = child.innerHTML;
//-->
</SCRIPT>