<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=book.xls" );
String strRptData = request.getParameter("rptdata");
%>
<HTML>
<HEAD>
<TITLE>���ݵ���-Excel</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=GBK">
<style>
TD{
  FONT-FAMILY: "����", "Tahoma"; FONT-SIZE: 12px;
}
</style>
</HEAD>

<BODY>
<%=strRptData%>
</BODY>
</HTML>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idDivWin) == "object"){
	parent.idDivWin.style.display = "none";
}
//-->
</SCRIPT>