<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="ServiceSpeedAct" scope="request" class="com.linkage.litms.resource.ServiceSpeedAct" />
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=device.xls" );
%>

<HTML>
<HEAD>
<TITLE>按速率统计用户数量</TITLE>
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
			<TD align="left"><b>按速率统计用户数量</b></TD>
		</TR>
		<TR>
			<TD>
				<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
					<%=ServiceSpeedAct.getHtmlDeviceOnSpeedService(request, 1)%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
</TABLE>
</BODY>
</HTML>