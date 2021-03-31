<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.ArrayList"%>
<jsp:useBean id="SheetExeRuleAct" scope="request" class="com.linkage.litms.resource.SheetExeRuleAct"/>
<%
request.setCharacterEncoding("GBK");
ArrayList list = new ArrayList();
list.clear();

//所有计划任务
list = SheetExeRuleAct.getSheetRules(request);
String strData = "";
String strBar = String.valueOf(list.get(0));
Cursor cursor = (Cursor)list.get(1);

Map fields = cursor.getNext();
if(fields == null){
	strData = "<TR><TD class=column COLSPAN=8 HEIGHT=30>没有检索到计划任务!</TD></TR>";
}
else{
	while(fields != null){
		String active = "";
		if ("0".equals((String)fields.get("active"))) {
			active = "不启用";
		} else if ("1".equals((String)fields.get("active"))) {
			active = "启用";
		}
		strData += "<TR>";
		strData += "<TD class=column1 align=center>"+ (String)fields.get("task_id") +"</TD>";
		strData += "<TD class=column1 align=center>"+ (String)fields.get("acc_loginname") +"</TD>";
		strData += "<TD class=column1 align=center>"+ (String)fields.get("execute_time") + "</TD>";
		strData += "<TD class=column2 align=center>"+ active + "</TD>";
		strData += "<TD class=column1 align=center>"+ (String)fields.get("plan_desc") + "</TD>";
		strData += "<TD class=column2 align=center><A HREF=SheetExeRuleUpdate.jsp?action=update&task_id="+ (String)fields.get("task_id") 
				+">编辑</A> | <A HREF=SheetExeRuleSave.jsp?action=delete&task_id="
				+(String)fields.get("task_id")
				+ " onclick='return delWarn();'>删除</A></TD>";
		strData += "</TR>";
		fields = cursor.getNext();
	}
	strData += "<TR><TD class=column COLSPAN=8 align=right>" + strBar + "</TD></TR>";
}

fields = null;
list.clear();
list = null;
%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
<!--
//删除时警告
function delWarn(){
	if(confirm("真的要删除该规则吗？\n本操作所删除的不能恢复！！！")){
		return true;
	}
	else{
		return false;
	}
}
//查看规则详细信息
function GoContent(user_id, gather_id){
	var strpage=".jsp?user_id=" + user_id + "&gather_id=" + gather_id;
	window.open(strpage,"","left=20,top=20,width=800,height=600,resizable=yes,scrollbars=yes");
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<br>
<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						计划任务管理
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
							增加以及浏览工单执行规则。
					</td>
					<td align="right"><a href="SheetExeRuleForm.jsp">增加规则</a></td>
				</tr>
			</table>
		</td>
	</tr>		
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR class="blue_title"><TH COLSPAN="9">计划任务工单</TH></TR>
				<TR>
					<TH>任务ID</TH>
					<TH>计划制定人</TH>
					<TH>执行时间</TH>
					<TH>是否启用</TH>
					<TH>计划任务描述</TH>
					<TH width=150>操作</TH>
				</TR>
				<%=strData%>
			</TABLE>
		</TD>
</TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>

