<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.io.IOException"%>
<%@ include file="./timelater.jsp"%>
<%
	String strRptData = request.getParameter("rptdata");
	response.setContentType("text/plain");
	response.setHeader("Content-Disposition",
			"attachment;filename=book.txt");
	ServletOutputStream sosStream = null;
	try {
		sosStream = response.getOutputStream();
		sosStream.write(strRptData.getBytes());
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		sosStream.flush();
		sosStream.close();
	}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idDivWin) == "object"){
	parent.idDivWin.style.display = "none";
}
//-->
</SCRIPT>