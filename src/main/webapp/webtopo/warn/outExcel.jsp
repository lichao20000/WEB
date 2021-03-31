<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=book.xls" );
String strRptData = request.getParameter("rptdata");
strRptData=strRptData.replace("bgcolor=#333333","bgcolor=white");
strRptData=strRptData.replace("border=0","border=1");
%>
<HTML>
<HEAD>
<TITLE>数据导出-Excel</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=GBK">
<style>
/*用于导出excel*/
table{
	background-color:white;
	font-size: 12px;
}
/*告警等级*/
.level_0 {
	background-color:#FFFFFF;
	color:#000000;
	cursor:default;
		size:14px;
}

.level_1 {
	background-color:#E1ECFB;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_2 {
	background-color:#FFEBB5;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_3 {
	background-color:#FFC351;
	color:#000000;
	cursor:default;
	size:14px;
}

.level_4 {
	background-color:#FFB4B2;
	color:#000000;
	cursor:default;
	size:14px;
}
.level_5{
	background-color:#FF0000;
	color:#000000;
	cursor:default;
	size:14px;
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