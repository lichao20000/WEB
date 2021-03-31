<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.ArrayList"%>
<%
ArrayList list = LogItem.getInstance().getLogByUserName(request);
String strBar = (String) list.get(0);
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
	<td>
	<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				用户日志信息
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				&nbsp;&nbsp;用户日志信息查询结果
			</td>
		</tr>
	</table>
	</td>
</tr>
<TR><TD>	
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">	 
	  <TR><TD bgcolor=#999999>
	    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="idTable">
		  <tr>
		  <TH>操作人</TH>
		  <TH>操作时间</TH>
		  <TH>日志类型</TH>
		  <TH>操作名称</TH>
		  <TH>操作对象</TH>
		  <TH>操作内容</TH>
		  <TH>操作终端</TH>
		  <TH>操作结果</TH>
		  </tr>
		  <%
				if(null!=fields)
				{
					int logType;
					String logStr="";
					while(null!=fields)
					{
						out.println("<TR bgcolor=#ffffff align=right >");
						out.println("<TD class=column>"+ fields.get("acc_loginname")+"</TD>");
						out.println("<TD class=column>"+ new DateTimeUtil(Long.parseLong((String)fields.get("operation_time"))*1000).getLongDate()+"</TD>");
						logType = Integer.parseInt((String)fields.get("operationlog_type"));
						if(logType==2)
						{
							logStr="系统日志";
						}
						else if(logType==3)
						{
							logStr ="安全日志";
						}
						else
						{
							logStr="操作日志";
						}									
						out.println("<TD class=column>"+logStr+"</TD>");
						out.println("<TD class=column>"+fields.get("operation_name")+"</TD>");
						out.println("<TD class=column>"+fields.get("operation_object")+"</TD>");
						out.println("<TD class=column>"+fields.get("operation_content")+"</TD>");
						out.println("<TD class=column>"+fields.get("operation_device")+"</TD>");
						out.println("<TD class=column>"+fields.get("operation_result")+"</TD>");
						out.println("</tr>");
						fields = cursor.getNext();
					}					
					
					out.println("<TR bgcolor='#FFFFFF'><TD colspan=8 align=right>"
							+ strBar + "</td></tr>");
				}
				else
				{
					out.println("<TR bgcolor=#ffffff>");
					out.println("<TD class=column  colspan=8>没有记录</TD>");
					out.println("</TR>");
				}		  
		        
		        //clear
				fields = null;
				cursor = null;
				list = null;
				%>
			<TR>
			<TD colspan=8 class=green_foot align=right><input type="button" class=btn value=" 返 回 " onclick="javascript:goback()"></TD>
		   </TR>	
			</table>
			</td>
		</tr>
		</table>
		</td>
	</tr>
</table>
<BR><BR><BR>
<script language=javascript>
function goback()
{
window.location.href = "./onlineUserList.jsp";
}

</script>
<%@ include file="../foot.jsp"%>