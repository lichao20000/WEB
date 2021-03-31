<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>
<%
	String fileList = fileManage.getHtml(request);
%>
<SCRIPT LANGUAGE="JavaScript">
<%=fileList%>

</SCRIPT>