<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="StatisticalLordMGCAddressACT" scope="request" class="com.linkage.module.itms.report.act.StatisticalLordMGCAddressACT" />
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");
response.setHeader("Content-disposition","attachment; filename=MGC.xls" );
%>

<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">
<style>
TD{
  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;
}
</style>
</HEAD>

<BODY>
<TABLE border=1 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="95%" border=1 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD>
				<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
					<%=StatisticalLordMGCAddressACT.getMGCExcel(request)%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
</TABLE>
</BODY>
</HTML>