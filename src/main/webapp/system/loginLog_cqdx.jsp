<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@page import="java.util.ArrayList"%>
<meta http-equiv="refresh" content="1000">
<%

ArrayList logList = LogItem.getInstance().getLoginLog(request);
String strBar = (String) logList.get(0);
Cursor cursor = (Cursor) logList.get(1);
Map fields = cursor.getNext();
%>
<script type="text/javascript" src="../Js/jquery.js"></script>
<script> 
	$(function(){
		parent.parent.document.getElementById('dataForm').style.height = $("#datatable").height();
	});
</script> 

<table width="100%" border=0 align="center" cellpadding="0"	cellspacing="0" id="datatable">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<TR bgcolor=#999999>
		<TD>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<TH>操作人</TH>
				<TH>客户端IP</TH>
				<TH>操作时间</TH>								
				<TH>失败原因</TH>				
			</tr>
			<%
			 if(null!=fields)
			 {
				 while(null!=fields)
				 {
					 out.println("<TR align='center'>");
					 out.println("<TD class=column height='20'>"+fields.get("acc_loginname")+"</TD>");
					 out.println("<TD class=column height='20'>"+fields.get("acc_login_ip")+"</TD>");
					 out.println("<TD class=column height='20'>"+new DateTimeUtil(Long.parseLong((String) fields.get("time")) * 1000).getLongDate()+"</TD>");					 
					 out.println("<TD class=column height='20'>"+fields.get("errorlogin_desc")+"</TD>");
					 fields = cursor.getNext();
				 }
				 out.println("<TR><TD class=column colspan=4 align=right height='20'>"	+ strBar + "</td></tr>");
				 
				 //clear
				 cursor = null;
				 fields = null;
			 }
			 else
			 {
				 out.println("<TR align='center'><TD class=column colspan=4 height='20'>没有数据</TD></TR>");
			 }
			%>
		</TABLE>
	</TD>
  </TR>
</table>
