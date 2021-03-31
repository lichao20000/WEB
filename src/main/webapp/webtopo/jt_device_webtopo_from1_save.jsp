<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: 帐号增加、修改、删除操作
--%>
<html>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.Filedevice"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	String strList= null;
	request.setCharacterEncoding("GBK");
	strList = DeviceAct.PingList(request);
%>

<body>
<SPAN ID="child"><%=strList%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("div_ping").innerHTML = child.innerHTML;
</SCRIPT>
</body>
</html>
