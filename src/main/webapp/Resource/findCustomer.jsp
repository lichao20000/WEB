<%@ page language="java" contentType="text/html; charset=gb2312"%>
<%@ page import="com.linkage.litms.system.UserRes" %>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<html>
<%
//��ҵ����
String type = request.getParameter("type");
String value = request.getParameter("value");

//�������ع���
UserRes userRes = (UserRes) request.getSession().getAttribute("curUser");
String cityId = userRes.getCityId();

String entNamesList = HGWUserInfoAct.getENamesList_BBMS(type,value,cityId);
%>

<%= entNamesList%> &nbsp;<font color="#FF0000">*</font>

</html>