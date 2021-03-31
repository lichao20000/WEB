<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="serverManage" scope="request" class="com.linkage.litms.filemanage.ServerManage"/>
<%
	String serverList = serverManage.getHtml(request);
%>
<SCRIPT LANGUAGE="JavaScript">
<%=serverList%>
</SCRIPT>