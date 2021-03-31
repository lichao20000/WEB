<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="updateManage" scope="request" class="com.linkage.litms.software.UpdateManage"/>
<%
request.setCharacterEncoding("GBK");

String task_name = request.getParameter("task_name");

ArrayList list = updateManage.getUpdateTaskDetail(request);
String strBar = (String)list.get(0);
Cursor cursor =(Cursor)list.get(1);
Map fields = cursor.getNext();
String successPercent =(String)list.get(2);
%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>	
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						"<%=task_name %>"策略
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
							<TD bgcolor="#ffffff" colspan="8" >该策略的详细工单信息&nbsp;&nbsp;&nbsp;成功率：<%=successPercent %>%</TD>
						</TR>
						<TR>							
							<TH nowrap>OUI</TH>
							<TH nowrap>设备序列号</TH>
							<TH nowrap>工单编号</TH>
							<TH nowrap>用户帐号</TH>
							<TH nowrap>执行结果</TH>
							<TH nowrap>开始时间</TH>
							<TH nowrap>结束时间</TH>
							<TH nowrap>失败原因描述</TH>
						</TR>
						<%
						if(fields!=null)
						{							
							String opeResult ="";							
							String startTime="";
							String endTime="";							
							while(fields!=null)
							{
								out.println("<TR bgcolor= #ffffff >");
								out.println("<TD class=column align=center nowrap>"+fields.get("oui")+"</TD>");
								out.println("<TD class=column align=center nowrap>"+fields.get("device_serialnumber")+"</TD>");
								out.println("<TD class=column align=center nowrap>"+fields.get("sheet_id")+"</TD>");
								out.println("<TD class=column align=center nowrap>"+fields.get("username")+"</TD>");
								
								opeResult = (String)fields.get("exec_status");
								if("".equals(opeResult))
								{
									opeResult ="";
								}
								else if("1".equals(opeResult))
								{
			                    	opeResult = (String) fields.get("fault_code");                           
				                    if(opeResult.equals("1"))
				                    {
				                    	opeResult = "执行结束";
				                    }
				                    else if(opeResult.equals("-1"))
				                    {
				                    	opeResult = "连接不上";
				                    }
				                    else if(opeResult.equals("-2"))
				                    {
				                    	opeResult = "连接超时";
				                    }
				                    else if(opeResult.equals("-3"))
				                    {
				                    	opeResult = "未找到相关工单";
				                    }
				                    else if(opeResult.equals("-4"))
				                    {
				                    	opeResult = "未找到相关设备";                   
				                    }
				                    else if(opeResult.equals("-5"))
				                    {
				                    	opeResult = "未找到相关RPC命令";                                         
				                    }
				                    else if(opeResult.equals("-6"))
				                    {
				                    	opeResult = "设备正被操作";                       
				                    } 
				                }
				                else if ("-1".equals(opeResult)){
				                	opeResult = "等待执行"; 
				                }
								else
								{
				                	opeResult = "正在执行";
				                }       
								out.println("<TD class=column align=center nowrap>"+opeResult+"</TD>");
								
								//开始时间
								startTime = (String)fields.get("start_time");
								if(!"".equals(startTime))
								{
									startTime = new DateTimeUtil(Long.parseLong(startTime)*1000).getLongDate();
								}
								out.println("<TD class=column align=center nowrap>"+startTime+"</TD>");
								
								//结束时间
								endTime = (String)fields.get("end_time");
								if(!"".equals(endTime))
								{
									endTime = new DateTimeUtil(Long.parseLong(endTime)*1000).getLongDate();
								}
								out.println("<TD class=column align=center nowrap>"+endTime+"</TD>");
								
								
								out.println("<TD class=column align=center nowrap>"+fields.get("fault_desc")+"</TD>");
								out.println("</TR>");
								
								fields = cursor.getNext();
							}							
							
							out.println("<TR bgcolor='#FFFFFF'><TD colspan=8 align=right>"+ strBar + "</td></tr>");
						}
						else
						{
							out.println("<TR bgcolor='#FFFFFF'><TD colspan=8 align=center>没有工单信息</TD></TR>");
						}
						
						//clear
						fields = null;
						cursor = null;						
						%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</TD>
</TR>
</TABLE>
					
<%@ include file="../foot.jsp"%>					
