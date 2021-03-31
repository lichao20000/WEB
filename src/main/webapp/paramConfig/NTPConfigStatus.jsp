<%--
FileName	: IpLineSave.jsp
Author		: liuli
Date		: 2007-3-06
Note		: ÕÊºÅÔö¼Ó¡¢ĞŞ¸Ä¡¢É¾³ı²Ù×÷
--%>
<html>
<%@ page import="java.util.Map"%>
<jsp:useBean id="configMgr" scope="request" class="com.linkage.litms.paramConfig.ConfigureManager"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	Map map = configMgr.getNTPStatus(request);
	int status = 2;
	String main_server = "";
	String second_server = "";
	if (null != map && 0 < map.size()) {
		status = 1;
		if (1 == map.size()) {
			main_server = (String)map.get("server_1");
		}
		if (2 <= map.size()) {
			main_server = (String)map.get("server_1");
			second_server = (String)map.get("server_2");
		}
	}

%>

<body>
<SCRIPT LANGUAGE="JavaScript">
	var status = "<%=status%>";
	var main_server = "<%=main_server%>";
	var second_server = "<%=second_server%>";
	parent.setStatus(status, main_server, second_server);
</SCRIPT>
</body>
</html>
