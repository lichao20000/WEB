<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.flux.UserManagerDev"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.flux.FluxUnit"%>
<%-- 
    benyp(5260)
    Req:GZDX-REQ-20080325-ZYX-001
--%>
<%
	request.setCharacterEncoding("GBK");
		response.setContentType("Application/msexcel");
		response.setHeader("Content-disposition","attachment; filename=fluxdata.xls");
		String[] searchKind = request.getParameterValues("kind");
		String type = request.getParameter("type");
		UserManagerDev umd = new UserManagerDev();
		umd.setRequest(request);
		FluxUnit fu = FluxUnit.getFluxUnit(session);
		double unit = fu.getFluxBase();
		Cursor cursor = umd.getFluxDataCursor();
		String data = umd.getFluxDataForTable(cursor, unit);
%>
<HTML>
<HEAD>
<TITLE>数据导出-Excel</TITLE>
<META HTTP-EQUIV="Content-Type"
	CONTENT="Application/msexcel; charset=GBK">
<style>
TD {
	FONT-FAMILY: "宋体", "Tahoma";
	FONT-SIZE: 12px;
}
</style>
</HEAD>
<BODY>
<table width="100%"  border="1" cellspacing="1" cellpadding="2">
<%=data%>
</table>
</BODY>
</HTML>
