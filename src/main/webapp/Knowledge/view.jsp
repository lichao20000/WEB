<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
String serialno = request.getParameter("serialno");
String gather_id = request.getParameter("gather_id");
Knowledge knowledge = KnowledgeCommon.getKnowledge(serialno,gather_id);

Date date = new Date(knowledge.createtime*1000);
String date_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

Date ack_date = new Date(knowledge.ack_CreateTime*1000);
String ack_date_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ack_date);
String content = knowledge.content.replaceAll(" ","&nbsp;");
content = content.replaceAll("<","&lt;");
content = content.replaceAll(">","&gt;");
content = content.replaceAll("\n","<br>");
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.knowledge.Knowledge"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.linkage.litms.knowledge.KnowledgeCommon"%>

<br>
<table align="center" border=0 cellspacing=1 cellpadding=2 width="70%" bgcolor="#000000">
	<tr>
		<td class=column width=15% align=right>主题：</td>
                <td class=column><%=knowledge.subject%></td>
	</tr>
	<tr>
		<td class=column align=right>创建者：</td>
                <td class=column><%=knowledge.creator%></td>
	</tr>
	<tr>
		<td class=column align=right>告警原因：</td>
                <td class=column><%=knowledge.warnReason%></td>
	</tr>
	<tr>
		<td class=column align=right>解决方法：</td>
                <td class=column><%=knowledge.warnResove%></td>
	</tr>

	<tr>
		<td class=column align=right>创建时间：</td>
                <td class=column><%=date_str%></td>
	</tr>
	<tr>
		<td class=column align=right valign="top">内容：</td>
                <td class=column><%=content%></td>
	</tr>	
	 <tr>
		<td class=column align=right valign="top">告警设备IP：</td>
                <td class=column><%=knowledge.sourceIp%></td>
	</tr>
	<tr>
		<td class=column align=right valign="top">告警设备名称：</td>
                <td class=column><%=knowledge.sourceName%></td>
	</tr>
	<tr>
		<td class=column align=right valign="top">告警创建者类型：</td>
                <td class=column><%=knowledge.getCreatorType()%></td>
	</tr>
	<tr>
		<td class=column align=right valign="top">采集点：</td>
                <td class=column><%=knowledge.gather_id%></td>
	</tr>
	<tr>
		<td class=column align=right valign="top">告警发生时间：</td>
                <td class=column><%=ack_date_str%></td>
	</tr>	 
	<tr>
		<td class=column align=right colspan="2">
<%
String username = (String)user.getAccount();
if(username!=null && username.equals(knowledge.creator)){
%>
                    <input type="button" onclick="javascript:window.location='edit.jsp?serialno=<%=serialno%>&gather_id=<%=gather_id %>&action=modify';" value="修  改" class="btn" style="width:60px">
                    <input type="button" onclick="javascript:if(confirm('真的要删除吗？')) window.location='save.jsp?serialno=<%=serialno%>&gather_id=<%=gather_id %>&action=delete';" value="删  除" class="btn" style="width:60px">&nbsp;&nbsp;
<%
}
%>
					<!-- 
                    <input type="button" onclick="javascript:window.location='edit.jsp';" value="添  加" class="btn" style="width:60px">
                     -->
                    <input type="button" onclick="javascript:window.location='index.jsp';" value="列  表" class="btn" style="width:60px">
		</td>
	</tr>
</table>
<br><br>
<%@include file="../foot.jsp"%>