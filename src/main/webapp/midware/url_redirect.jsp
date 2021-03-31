
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ page import="com.holdfastgroup.util.MD5,java.util.Date,com.linkage.litms.LipossGlobals,com.linkage.litms.midware.*"%>
<%
	request.setCharacterEncoding("gbk");
	String id = request.getParameter("id");

	URLManager urlMgr = new URLManager();

	String host = LipossGlobals.getLipossProperty("midware.host");
	String path = urlMgr.getURLPathById(id);
	long time = new Date().getTime();
	//String login = LipossGlobals.getLipossProperty("midware.login");
	String login = user.getAccount();
	String C = LipossGlobals.getLipossProperty("midware.key");
	String A = login + ":" + path + ":" + time;
	String B = MD5.getMD5((A + ":" + MD5.getMD5(C.getBytes())).getBytes());
	String url = host + path + "?A=" + A + "&B=" + B;
	response.sendRedirect(url);
%>
<%@ include file="../head.jsp"%>
<%@ include file="../foot.jsp"%>