<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.ArrayList"%>
<jsp:useBean id="UseParameterACT" scope="request" class="com.linkage.module.gtms.config.action.UseParameterACT"/>
<%
ArrayList logList = UseParameterACT.getOpertionLog(request);
String strBar = (String) logList.get(0);
Cursor cursor = (Cursor) logList.get(1);
Map fields = cursor.getNext();
String content = request.getParameter("content");
if(content==null){
	content = "";
}
%>
<html>
<title>上网方式修改为路由日志查询</title>
<head>
</head>
<body>
<FORM NAME="frm">
	<table width="98%" border=0 align="center" cellpadding="0"	cellspacing="0">
		<TR><TD HEIGHT=20>&nbsp;</TD></TR>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="160" align="center" class="title_bigwhite">
							上网方式修改为路由日志
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" height="10" border="0" cellspacing="0" cellpadding="0" >
					<tr>
						宽带账号
						<input type="text" name="content" value="<%=content%>"
							   class="bk">
						&nbsp;&nbsp;&nbsp;
						<input type="button" name="ls" value=" 查 询 " class=jianbian
							   onclick="javascript:frm.submit();">
					</tr>
				</table>

			</td>
		</tr>
		<TR bgcolor=#999999>
			<TD>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<TH>操作账号</TH>
						<TH>操作人</TH>
						<TH>操作时间</TH>
					</tr>
					<%
						if(null!=fields)
						{
							while(null!=fields)
							{
								out.println("<TR align='center'>");
								out.println("<TD class=column>"+fields.get("operation_name")+"</TD>");
								out.println("<TD class=column>"+fields.get("acc_loginname")+"</TD>");
								out.println("<TD class=column>"+new DateTimeUtil(Long.parseLong((String) fields.get("operation_time")) * 1000).getLongDate()+"</TD>");
								fields = cursor.getNext();
							}
							out.println("<TR><TD class=column colspan=4 align=right>"	+ strBar + "</td></tr>");

							//clear
							cursor = null;
							fields = null;
						}
						else
						{
							out.println("<TR align='center'><TD class=column colspan=4>没有数据</TD></TR>");
						}
					%>
				</TABLE>
			</TD>
		</TR>
	</table>
</FORM>
</body>
</html>

<%@ include file="../foot.jsp"%>

