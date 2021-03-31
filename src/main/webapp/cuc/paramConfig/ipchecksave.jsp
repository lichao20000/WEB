<html>
<jsp:useBean id="ctcFileSevice" scope="request" class="com.linkage.module.cuc.diagnostic.CucFileSevice"/>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
	String strList= null;
	request.setCharacterEncoding("GBK");
	strList = ctcFileSevice.allPingResult(request);
	
%>

<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_PING").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
