<html>
<jsp:useBean id="fileSevice" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
	String strList= null;
	request.setCharacterEncoding("GBK");
	strList = fileSevice.allHttpGetResult(request);
	
%>

<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_httpget").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
