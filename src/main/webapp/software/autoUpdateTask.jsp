<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList"%>
<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage"/>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%


 // add by lizhaojun 20080418  ������ִ����ɵĲ��� update������ɡ�
 
 
 DataSetBean.executeUpdate("update tab_device_autoupdate set is_over=1 where task_id in "
 						 + "(select a.task_id from tab_device_autoupdate a,tab_autoupdate_sheet b "
 						 + " where a.is_check=1 and a.is_over=0 and b.is_over=1 and a.task_id=b.task_id)");

 String pageTitle = "";
 int type=0;
 if(null!=request.getParameter("type")){
	 type= Integer.parseInt(request.getParameter("type"));
 }
 
 if(type==1){
	 pageTitle = "�汾������������";
 }
 else if (type==2){
	 pageTitle = "�����·���������";
 }
 else if (type==3){
 	 pageTitle = "����������������";
 }
 else {
 	 pageTitle = "��������";
 }
 
 ArrayList list= versionManage.getAutoUpdateTaskInfo(request);
 String strBar = (String)list.get(0);
 Cursor cursor = (Cursor)list.get(1);
 Map fields = cursor.getNext();
%>

<script type="text/javascript">
<!--

function changeStat(stat,task_id){
	var page = "changeStat.jsp?stat=" + stat + "&task_id=" + task_id;
	document.all("childFrm").src = page;
}

//-->
</script>

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
						<td width="162" align="center" class="title_bigwhite"><%=pageTitle%></td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
								��ϵͳ�������ж��Ʋ���  
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"id="outTable">
					<TR>
					  <TH>��������</TH>
					  <TH>�����ƶ�ʱ��</TH>
					  <TH>�����ƶ���</TH>
					  <TH>����ִ��ʱ��</TH>
					  <TH>�Ƿ����</TH>
					  <TH>�Ƿ����</TH>
					  <TH>����</TH>
					</TR>
					<%
					String task_id="";
					String userName = "";
					String task_time="";
					String task_name="";
					int is_check=0;
					int is_over =0;
					int excutetime_type;
					String[] typestr={"","�豸��ʼ��װ��һ������ʱ�Զ�ִ��","�豸Periodic Inform�Զ�ִ��","�豸��������ʱ�Զ�ִ��","�豸�´����ӵ�ITMSʱ�Զ�ִ��","����ִ��"};
					boolean isFinish = false;
					
					if(null!=fields)
					{
						while(null!=fields)
						{
							task_id = (String)fields.get("task_id");
							task_name = (String)fields.get("task_name");
							task_time=new DateTimeUtil(Long.parseLong((String)fields.get("task_time"))*1000).getLongDate();
							userName =(String)fields.get("username");							
							excutetime_type= Integer.parseInt((String)fields.get("type"));
							is_check = Integer.parseInt((String)fields.get("is_check"));
							is_over = Integer.parseInt((String)fields.get("is_over"));
							
							out.println("<TR align=center>");
							out.println("<TD class=column2><a href ='autoUpdateTaskDetail.jsp?task_id="+task_id+"&task_name="+task_name+"&type="+type+"'>"+task_name+"</a></TD>");
							out.println("<TD class=column2>"+task_time+"</TD>");
							out.println("<TD class=column2>"+userName+"</TD>");							
							out.println("<TD class=column2>"+typestr[excutetime_type]+"</TD>");
							if(is_check==1)
							{
								out.println("<TD class=column2>���ͨ��</TD>");
							}
							else if(is_check==2)
							{
								out.println("<TD class=column2>��˲�ͨ��</TD>");
							}
							else
							{
								out.println("<TD class=column2>δ���</TD>");
							}
							
							
							//isFinish = versionManage.isTaskFinish(task_id);
							//�����˲�ͨ����δ��ˣ�������Ϊδ���
							if(is_check!=1)
							{
								if (is_over == -1){
									out.println("<TD class=column2>�ѳ���</TD>");
								}
								else{
									out.println("<TD class=column2>δ���</TD>");	
								}
							}
							else
							{
								if(is_over == 1)
								{
									out.println("<TD class=column2>�����</TD>");
								}
								else if (is_over == -1)
								{
									out.println("<TD class=column2>�ѳ���</TD>");
								}
								else
								{
									out.println("<TD class=column2>δ���</TD>");
								}
							}
							
							if (type == 3 || type == 2){
								if (is_over == 0){
									out.println("<TD class=column2><a href='#' onclick=changeStat('-1','"+task_id+"')>����</a></TD>");
								}
								else if (is_over == -1){
									out.println("<TD class=column2><a href='#' onclick=changeStat('0','"+task_id+"')>�ָ�</a></TD>");
								}
								else{
									out.println("<TD class=column2></TD>");
								}
							} else{
								out.println("<TD class=column2></TD>");
							}
							
							out.println("</TR>");
							fields = cursor.getNext();
						}
						
						out.println("<TR bgcolor='#FFFFFF'><TD colspan=7 align=right>"+ strBar + "</td></tr>");
						//clear
						fields = null;
						cursor = null;
					}
					else
					{
						out.println("<TR><TD class=column2 colspan=7 align=center>û�ж�Ӧ�Ĳ���</TD></TR>");						
					}
					%>
				</TABLE>
				</TD>
			</TR>
		</TABLE>		
		</TD>
	</TR>
	<tr><td><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME></td></tr>
</TABLE>
<BR>
<BR>
<%@ include file="../foot.jsp"%>
