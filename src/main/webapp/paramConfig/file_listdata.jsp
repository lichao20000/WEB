<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
	String fileList = fileManage.getConfigHtml(request);
%>
<SCRIPT LANGUAGE="JavaScript">
<%=fileList%>
</SCRIPT>