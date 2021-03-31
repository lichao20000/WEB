<html>
<jsp:useBean id="fileSevice" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
	request.setCharacterEncoding("GBK");
	String strList = fileSevice.allPingResult(request);
	
%>

<body>
<SCRIPT LANGUAGE="JavaScript">
  if(parent.setPingResult){
    parent.setPingResult("<%=strList%>");
  }
</SCRIPT>
</body>
</html>
