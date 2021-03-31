<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="UserInstAct" scope="request" class="com.linkage.litms.resource.UserInstAct"/>
<%
request.setCharacterEncoding("GBK");
String type = request.getParameter("type");
String result = null;
if("state".equals(type)){
	result = UserInstAct.getUserInstState(request);
}else if("detail".equals(type)){
	result = UserInstAct.getDetailUserInst(request);
}
out.println(result);
UserInstAct = null;
%>
