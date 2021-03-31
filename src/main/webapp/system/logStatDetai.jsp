<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.system.dbimpl.LogItem"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%
String module_name= request.getParameter("module_name");
HashMap itemLogMap =LogItem.getInstance().getModuleItemLog(request);
%>

<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>		
		<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">模块访问量</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TD bgcolor="#ffffff" colspan="2"><%=module_name%>下的功能访问量&nbsp;&nbsp;</TD>
					</TR>
					<TR>
					  <TH>功能点名称</TH>
					  <TH>访问量</TH>					  				  
					</TR>
					<%
					if(0==itemLogMap.size())
					{
						out.println("<TR><TD colspan=2 align=center class=column>没有功能点信息</TD></TR>");
					}
					else
					{
						Iterator it =itemLogMap.keySet().iterator();
						String item_name="";
						while(it.hasNext())
						{
							item_name = (String)it.next();
							out.println("<TR align=center>");
							out.println("<TD class=column>"+item_name+"</TD>");
							out.println("<TD class=column>"+itemLogMap.get(item_name)+"</TD>");
							out.println("</TR>");
						}
					}
					
					//clear
					itemLogMap = null;
					%>
					<TR><TD colspan=2 align=right class=column><input type="button"  class=btn value="返 回" onClick="JavaScript:history.go(-1)"></TD></TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD>
</TR>
</TABLE>
<%@ include file="../foot.jsp"%>				
					
