<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="UserInstAct" scope="request" class="com.linkage.litms.resource.UserInstAct"/>
<%
request.setCharacterEncoding("GBK");
String result = null;
result = UserInstAct.getDetailUserInstState(request);
out.println(result);
UserInstAct = null;
%>
