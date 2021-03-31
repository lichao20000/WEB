<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DevBatchRestartQueryACT" scope="request" class="com.linkage.module.itms.resource.act.DevBatchRestartQueryACT" />
<%
	String gw_type = request.getParameter("gw_type");
	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition","attachment; filename=noRestartDevice.xls" );
%>

<HTML>
<HEAD>
<TITLE>设备30天未重启统计</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">
<style>
TD{
  FONT-FAMILY: "宋体", "Tahoma"; FONT-SIZE: 14px;
}
</style>
</HEAD>

<BODY>
<TABLE border=1 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
			<TABLE width="95%" border=1 cellspacing=0 cellpadding=0 align="center">
				<TR>	
				<%if(!"1".equals(gw_type)){ %>
					<TD align="left"><b>超过30天未重启的机顶盒终端数量统计</b></TD>
				<%}else{ %>	
					<TD align="left"><b>超过30天未重启的用户终端数量统计</b></TD>
				<%}%>
				</TR>
				<TR>
					<TD>
						<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
							<%=DevBatchRestartQueryACT.createNotRestartResult(request)%>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</BODY>
</HTML>