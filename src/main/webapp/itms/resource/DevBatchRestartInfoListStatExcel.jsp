<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DevBatchRestartQueryACT" scope="request" class="com.linkage.module.itms.resource.act.DevBatchRestartQueryACT" />
<%
	String gw_type = request.getParameter("gw_type");
	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");
	response.setHeader("Content-disposition","attachment; filename=device.xls" );
%>

<HTML>
<HEAD>
<TITLE>�豸��������ͳ��</TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">
<style>
TD{
  FONT-FAMILY: "����", "Tahoma"; FONT-SIZE: 14px;
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
					<TD align="left"><b>��������������ͳ��</b></TD>
				<%}else{ %>	
					<TD align="left"><b>��è��������ͳ��</b></TD>
				<%}%>
				</TR>
				<TR>
					<TD>
						<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
							<%=DevBatchRestartQueryACT.createResult(request)%>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</BODY>
</HTML>