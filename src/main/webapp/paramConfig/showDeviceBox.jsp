<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="fileManage" scope="request" class="com.linkage.litms.filemanage.FileManage"/>

<%
	request.setCharacterEncoding("GBK");
	
	String deviceList = fileManage.getDeviceList(request);
%>
<html>
<body>
<SPAN ID="child"><%=deviceList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("id_device").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>