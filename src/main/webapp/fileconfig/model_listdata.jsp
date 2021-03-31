<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="modelManage" scope="request" class="com.linkage.litms.filemanage.ModelManage"/>
<%
	String modeleList = modelManage.getModelHtml(request);
%>
<SCRIPT LANGUAGE="JavaScript">
<%=modeleList%>
</SCRIPT>