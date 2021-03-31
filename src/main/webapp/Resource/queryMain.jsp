<%@page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
%>

<html>
<HEAD>
<TITLE><%=com.linkage.litms.LipossGlobals.getLipossName()%></TITLE>
</HEAD>
<frameset rows="40%,60%" border="0" framespacing="0" id="mainFrame">
	<frame src="userDevListForm.jsp?gw_type=<%= gw_type%>" name="queryFrame" TOPMARGIN="0"
		LEFTMARGIN="0" scrolling="no" MARGINHEIGHT="0" MARGINWIDTH="0"
		FRAMEBORDER="0"></frame>
	<frame src="" name="dataFrame" TOPMARGIN="0" LEFTMARGIN="0"
		scrolling="auto" MARGINHEIGHT="0" MARGINWIDTH="0" FRAMEBORDER="0"></frame>
</frameset>
</html>