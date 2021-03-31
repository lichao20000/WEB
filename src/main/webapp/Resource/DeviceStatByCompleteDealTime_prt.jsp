<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceStatByCompleteDealTimeAct" scope="request" class="com.linkage.litms.resource.DeviceStatByCompleteDealTimeAct" />
<%
request.setCharacterEncoding("GBK");
response.setContentType("Application/msexcel");

response.setHeader("Content-disposition","attachment; filename=device.xls" );
%>

<HTML>
<HEAD>
<TITLE>按用户上线的时间统计设备版本信息</TITLE>
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
			<TD align="left"><b>按用户上线的时间统计设备版本信息</b></TD>
		</TR>
		<TR>
			<TD>
				<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
					<%=DeviceStatByCompleteDealTimeAct.createResult(request)%> 
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
</TABLE>
</BODY>
</HTML>