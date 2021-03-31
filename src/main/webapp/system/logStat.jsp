<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.system.dbimpl.LogItem"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.linkage.litms.system.ModuleLog" %>
<%@page import="java.util.Iterator"%>
<%
HashMap moduleLogMap =LogItem.getInstance().getModuleLog(request);
String start_day= request.getParameter("start_day");
String end_day = request.getParameter("end_day");
String param ="";
if(null==start_day)
{
	start_day ="";
}
if(null==end_day)
{
	end_day="";
}

//有日期则加入param中
if(null!=start_day&&!"".equals(start_day))
{	
	param+="&start_day="+start_day;
}

if(null!=end_day&&!"".equals(end_day))
{
	param+="&end_day="+end_day;
}

//有填写时间的情况下
if(!"".equals(param))
{
	param =param.substring(1,param.length());
}

%>
<script language="JavaScript">
function selectModule(moduleName)
{
   document.all("module_name").value=moduleName;
   frm1.submit();
}
</script>
<form name="frm" method="post" action="logStat.jsp">
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
						<td width="162" align="center" class="title_bigwhite">访问量统计</td>
						<td><img src="../images/attention_2.gif" width="15" height="12">
						默认系统运行以来的模块访问量</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR bgcolor="#ffffff" align=center>
						<TD colspan=2 align="left">
							开始日期：
							<input type="text" name="start_day" class=bk value="<%=start_day %>" size=20 readonly> 
							<input type="button" value="▼" class=jianbian onClick="showCalendar('day',event)" id="start_day"> &nbsp;&nbsp;
							结束日期：
							<input type="text" name="end_day" class=bk value="<%=end_day%>" size=20 readonly> 
							<input type="button" value="▼" class=jianbian onClick="showCalendar('day',event)" id="end_day"> &nbsp;&nbsp;
							<input type="submit" value="查 询"class=btn >
						</TD>						
					</TR>
				    <TR>
						<TH>模块名称</TH>
						<TH>访问量</TH>
					</TR>
					<%
					if(0==moduleLogMap.size())
					{
						out.println("<TR align=center><TD class=column colspan=2>没有访问量</TD></TR>");
					}
					else
					{
						Iterator it = moduleLogMap.values().iterator();
						ModuleLog moduleLog = null;						
						while(it.hasNext())
						{
							moduleLog = (ModuleLog)it.next();
							out.println("<TR align=center>");
							out.println("<TD class=column><a href=\"javascript:selectModule('"+moduleLog.getModuleName()+"')\">"+moduleLog.getModuleName()+"</a></TD>");
							out.println("<TD class=column>"+moduleLog.getModuleLogNum()+"</TD>");
							out.println("</TR>");
						}
					}
					
					//clear
					moduleLogMap = null;
					%>
				</TABLE>
			</TD>
		</TR>	
		</TABLE>
		</TD>
		</TR>
		</TABLE>
</form>
<form name="frm1" method="post" action="logStatDetai.jsp?<%=param %>">
<input type="hidden" name ="module_name" value="">
</form>
<%@ include file="../foot.jsp"%>	
			