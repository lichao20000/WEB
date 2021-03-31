<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="sysManage" scope="request" class="com.linkage.litms.system.SysManage"/>
<%
	request.setCharacterEncoding("GBK");
	
	String strList = sysManage.gettableHtml(request);

%>
<html>
<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_table").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>