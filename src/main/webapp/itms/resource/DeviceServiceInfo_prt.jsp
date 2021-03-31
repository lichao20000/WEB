<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=device.xls" );
%>

<HTML>
<HEAD>
<TITLE>版本统计优化</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">
<style>
TD{
  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;
}
</style>
</HEAD>
<script type="text/javascript">
</script>
<BODY>
<TABLE border=1 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="95%" border=1 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD align="center"><b>版本统计优化</b></TD>
		</TR>
		<TR>
			<TD>
				<%=request.getAttribute("tempTable") %>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
</TABLE>
</BODY>
</HTML>