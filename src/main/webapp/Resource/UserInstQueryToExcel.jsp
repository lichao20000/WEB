<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="UserInstAct" scope="request" class="com.linkage.litms.resource.UserInstAct"/>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=user.xls" );
String result = null;
result = UserInstAct.getDetailUserInstExcel(request);
//out.println(result);
UserInstAct = null;
%>
<HTML>
<HEAD>
<TITLE>导出用户设备信息</TITLE>
</HEAD>
<body>
<%= result%>
</body>
</HTML>