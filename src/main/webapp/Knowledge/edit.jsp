<%@ page contentType="text/html;charset=GBK" pageEncoding="GBK"%>
<%@ include file="../timelater.jsp"%>
<%
String action = request.getParameter("action");
String serialno = request.getParameter("serialno");
String gather_id = request.getParameter("gather_id");
String subject,content,warnReason,warnResove;
if("modify".equals(action)){
    Knowledge knowledge = KnowledgeCommon.getKnowledge(serialno,gather_id);
    subject = knowledge.subject;
    content = knowledge.content;
	warnReason = knowledge.warnReason;
	warnResove = knowledge.warnResove;
}
else{
    subject = "";
    content = "";
	warnReason = "";
	warnResove = "";
}
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@page import="com.linkage.litms.knowledge.Knowledge"%>
<%@page import="com.linkage.litms.knowledge.KnowledgeCommon"%>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>知识库</title>
<script language="javascript"><!--
function checkForm(){
    var obj=document.frm;
    if(obj.subject.value==""){
        alert("请输入主题。");
        return false;
    }
    else if(obj.content.value==""){
        alert("请输入内容。");
        return false;
    }
    else
        return true;
}

//--></script>
</head>

<body>
<br>
<form name="frm" method="POST" action="save.jsp?serialno=<%=serialno%>&gather_id=<%=gather_id %>&action=<%=action%>" onsubmit="javascript:return checkForm();">
<table align="center" border=0 cellspacing=1 cellpadding=2 width="70%" bgcolor="#000000">

	<tr>
		<td class=column width=15% align=right>主题：</td>
		<td class=column>
                        <textarea rows="3" name="subject" cols="75" readonly><%=subject%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>内容：</td>
		<td class=column>
                        <textarea rows="10" name="content" cols="75"><%=content%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>告警原因：</td>
		<td class=column>
                        <textarea rows="5" name="warnReason" cols="75"><%=warnReason%></textarea></td>
	</tr>
	<tr>
		<td class=column align=right>解决方法：</td>
		<td class=column>
                        <textarea rows="5" name="warnResove" cols="75"><%=warnResove%></textarea></td>
	</tr>
	<tr>
            <td colspan="2" class=column>
            <table align="center" width=50%><tr align=center><td>
            <input type="submit" value="提  交" name="B1" class="btn" style="width:60px">
            </td>
            <td>
            <input type="reset" value="重  置" name="B2" class="btn" style="width:60px">
            </td></tr></table>
            </td>
	</tr>

</table>
</form>
<br><br>
<%@include file="../foot.jsp"%>