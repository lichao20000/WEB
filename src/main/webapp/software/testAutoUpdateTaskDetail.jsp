<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.netcutover.SheetManage"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
String task_name= request.getParameter("task_name");
SheetManage  sheetManager = new SheetManage();
ArrayList list = sheetManager.getTestAutoUpdateTaskDetail(request);
String successPercent =(String)list.get(0);
Cursor cursor =(Cursor)list.get(1);
Map fields = cursor.getNext();

%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>	
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						<%=task_name %>����
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#000000>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
						<TR>
							<TD bgcolor="#ffffff" colspan="8" >�ò��ԵĲ��Թ�����Ϣ&nbsp;&nbsp;&nbsp;�ɹ��ʣ�<%=successPercent %>%</TD>
						</TR>
						<TR>							
							<TH nowrap>OUI</TH>
							<TH nowrap>�豸���к�</TH>
							<TH nowrap>�������</TH>
							<TH nowrap>�û��ʺ�</TH>
							<TH nowrap>ִ�н��</TH>
							<TH nowrap>��ʼʱ��</TH>
							<TH nowrap>����ʱ��</TH>
							<TH nowrap>ʧ��ԭ������</TH>
						</TR>
						<%
						if(null!=fields)
						{							
							String opeResult ="";							
							String startTime="";
							String endTime="";							
							while(null!=fields)
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
								else if(opeResult.equals("1"))
								{
			                    	opeResult = (String) fields.get("fault_code");                           
				                    if(opeResult.equals("1"))
				                    {
				                    	opeResult = "ִ�гɹ�";
				                    }
				                    else if(opeResult.equals("-1"))
				                    {
				                    	opeResult = "���Ӳ���";
				                    }
				                    else if(opeResult.equals("-2"))
				                    {
				                    	opeResult = "���ӳ�ʱ";
				                    }
				                    else if(opeResult.equals("-3"))
				                    {
				                    	opeResult = "δ�ҵ���ع���";
				                    }
				                    else if(opeResult.equals("-4"))
				                    {
				                    	opeResult = "δ�ҵ�����豸";                   
				                    }
				                    else if(opeResult.equals("-5"))
				                    {
				                    	opeResult = "δ�ҵ����RPC����";                                         
				                    }
				                    else if(opeResult.equals("-6"))
				                    {
				                    	opeResult = "�豸��������";                       
				                    } 
				                }
								else
								{
				                	opeResult = "����ִ��";
				                }       
								out.println("<TD class=column align=center nowrap>"+opeResult+"</TD>");
								
								//��ʼʱ��
								startTime = (String)fields.get("start_time");
								if(!"".equals(startTime))
								{
									startTime = new DateTimeUtil(Long.parseLong(startTime)*1000).getLongDate();
								}
								out.println("<TD class=column align=center nowrap>"+startTime+"</TD>");
								
								//����ʱ��
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
						}
						else
						{
							out.println("<TR bgcolor= #ffffff><TD colspan=8 align=center>û�й�����Ϣ</TD></TR>");
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