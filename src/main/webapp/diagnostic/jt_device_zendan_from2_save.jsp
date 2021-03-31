<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.FileSevice"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	String strList= null;
	request.setCharacterEncoding("GBK");
	strList = DeviceAct.ATMF5LOOPList(request);

%>

<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ATMF").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
