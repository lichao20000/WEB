<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%
 String pageTitle = "";
 int type=0;
 if(null!=request.getParameter("type"))
 {
	 type= Integer.parseInt(request.getParameter("type"));
 }
 
 if(type==1){
	 pageTitle = "版本升级策略任务";
 }
 else if (type==2){
	 pageTitle = "配置升级策略任务";
 }
 else if (type==3){
 	 pageTitle = "备份升级策略任务";
 }
 else {
 	 pageTitle = "策略任务";
 }
 
 ArrayList list= versionManage.getAutoUpdateTaskInfo(request);
 String strBar = (String)list.get(0);
 Cursor cursor = (Cursor)list.get(1);
 Map fields = cursor.getNext();
%>
<%@ include file="../toolbar.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>		
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">软件升级管理</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								该系统现有未审核定制策略  
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
					id="outTable">
					<TR>
					  <TH>策略名称</TH>
					  <TH>策略制定时间</TH>
					  <TH>策略制定人</TH>
					  <TH>策略执行时间</TH>
					  <TH>操作</TH>					  
					</TR>
					<%
					String task_id="";
					String userName = "";
					String task_time="";
					String task_name="";					
					int excutetime_type;
					String[] typestr={"","设备初始安装第一次启动时自动升级","设备Periodic Inform自动升级","设备重新启动时自动升级","设备下次连接到ITMS时自动升级","立即执行"};
					if(null!=fields)
					{
						while(null!=fields)
						{
							task_id = (String)fields.get("task_id");
							task_name = (String)fields.get("task_name");
							task_time=new DateTimeUtil(Long.parseLong((String)fields.get("task_time"))*1000).getLongDate();
							userName =(String)fields.get("username");							
							excutetime_type= Integer.parseInt((String)fields.get("type"));							
							
							out.println("<TR align=center>");
							out.println("<TD class=column2>"+task_name+"</TD>");
							out.println("<TD class=column2>"+task_time+"</TD>");
							out.println("<TD class=column2>"+userName+"</TD>");							
							out.println("<TD class=column2>"+typestr[excutetime_type]+"</TD>");	
							out.println("<TD class=column2><a href='./testAutoUpdateTask.jsp?task_id="+task_id+"&task_name="+task_name+"&type="+type+
									"'>测试|</a><a href='./checktask_submit.jsp?type="+type+"&checkResult=pass&task_id="
									+task_id+"&task_name="+task_name+"'>审核通过|</a><a href='./checktask_submit.jsp?type="+type+"&checkResult=fail&task_id="+task_id+"&task_name="+task_name+"'>审核不通过</a></TD>");
							out.println("</TR>");
							fields = cursor.getNext();
						}
						
						out.println("<TR bgcolor='#FFFFFF'><TD colspan=6 align=right>"+ strBar + "</td></tr>");
						//clear
						fields = null;
						cursor = null;
					}
					else
					{
						out.println("<TR bgcolor= #ffffff><TD class=column2 colspan=6 align=center>没有对应的策略</TD></TR>");						
					}
					%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>		
		</TD>
	</TR>
</TABLE>

<br>
<br>
<%@ include file="../foot.jsp"%>
