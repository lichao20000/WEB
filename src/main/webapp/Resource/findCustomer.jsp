<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.linkage.litms.system.UserRes" %>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<html>
<%
//企业名称
String type = request.getParameter("type");
String value = request.getParameter("value");

//增加属地过滤
UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
String cityId = userRes.getCityId();

String entNamesList = HGWUserInfoAct.getENamesList_BBMS(type,value,cityId);
%>

<%= entNamesList%> &nbsp;<font color="#FF0000">*</font>

</html>