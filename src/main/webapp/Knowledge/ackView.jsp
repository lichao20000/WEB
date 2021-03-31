<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="../timelater.jsp"%>
<%
request.setCharacterEncoding("GBK");
int id = Integer.parseInt(request.getParameter("id"));
Knowledge knowledge = KnowledgeCommon.getKnowledge(id);
Date date = new Date(knowledge.createtime*1000);
String date_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
Date  AckDate=new Date(knowledge.ack_CreateTime*1000);
String ack_CreateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(AckDate);
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
<html>

<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>知识库</title>
</head>
<body>
<br>
<table align="center" border=0 cellspacing=1 cellpadding=2 width="70%" bgcolor="#000000">
	<tr>
		<td class=column width=20% align=right>主题：</td>
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
		<td class=column align=right>入库时间：</td>
                <td class=column><%=date_str%></td>
	</tr>
	<tr>
		<td class=column align=right>告警创建时间：</td>
                <td class=column><%=ack_CreateTime%></td>
	</tr>
	<tr>
		<td class=column align=right valign="top">内容：</td>
                <td class=column><%=knowledge.content%></td>
	</tr>
	<tr>
		<td class=column align=right>设备类型：</td>
                <td class=column><%=knowledge.deviceType%></td>
	</tr>
	<tr>
		<td class=column align=right>设备IP：</td>
                <td class=column><%=knowledge.sourceIp%></td>
	</tr>
	<tr>
		<td class=column align=right>告警创建者类型：</td>
                <td class=column><%=knowledge.creatorType%></td>
	</tr>
	<tr>
		<td class=column align=right>告警设备名称：</td>
                <td class=column><%=knowledge.sourceName%></td>
	</tr>
    <tr>
		<td class=column align=right>属地：</td>
                <td class=column><%=knowledge.gather_Id%></td>
	</tr>
	<tr>
		<td class=column align=right colspan="2">
<%
String username = (String)user.getAccount();
if(username!=null && username.equals(knowledge.creator)){
%>
                    <input type="button" onclick="javascript:window.close();" value="关闭窗口" class=jianbian style="width:60px">                   
<%
}
%>                   
	  </td>
	</tr>
</table>
<br><br>
<%@include file="../foot.jsp"%>