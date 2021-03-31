<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.*"%>
<%@page import="java.util.List,java.text.SimpleDateFormat,java.util.Date"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map,java.util.HashMap"%>
<jsp:useBean id="rrctMgr" scope="request" class="com.linkage.litms.report.RRcTMgr"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

// 查找条件

String query_type = request.getParameter("query_type");
   if (query_type == null) query_type = "";

String device_serialnumber = request.getParameter("device_serialnumber");
   if (device_serialnumber == null) device_serialnumber = "";

String user_name = request.getParameter("user_name");
   if (user_name == null) user_name = "";

String customer_name = request.getParameter("customer_name");
   if (customer_name == null) customer_name = "";

String is_send = request.getParameter("is_send");
   if (is_send == null) is_send = "";

String report_type = request.getParameter("report_type");
   if (report_type == null) report_type = "";

String startTime = request.getParameter("startTime");
   if (startTime == null) startTime = "";

String endTime = request.getParameter("endTime");
   if (endTime == null) endTime = "";


String prt_Page="RRcTMgr.jsp?device_serialnumber=" + device_serialnumber + "&device_serialnumber=" + device_serialnumber + "&user_name=" + user_name + "&customer_name=" + customer_name + "&is_send=" + is_send + "&report_type=" + report_type + "&startTime=" + startTime + "&endTime=" + endTime;

List list  = null;
String strData = "";
list = rrctMgr.queryReportList(request);

%>
<%@ include file="../head.jsp"%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<%@ include file="../toolbar.jsp"%>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="98%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="">
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						运行报告查询
					</td>
				</tr>
			</table>
			</td>
		</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outTable">
<%

String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();

if (fields == null) {
	%>
    <TR><TD class="column" COLSPAN="7" HEIGHT="30">没有查询到符合条件的报表记录</TD></TR>
	<%
} else {
		%>
						<TR>
							<TH><input type=checkbox onclick="selectAll(this,'chkCheck')"></TH>
							<TH>序号</TH>
							<TH>设备序列号</TH>
							<%--<TH>设备序列号</TH>--%>
							<TH>创建时间</TH>
							<TH>报表类型</TH>
							<TH>发送状态</TH>
							<TH>专家建议</TH>
							<TH>操作</TH>
						</TR>
		<%
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	int count=1;

    while (fields != null) {
		String device_serialnumber_ = (String)fields.get("device_serialnumber");
		long buildTime = Long.parseLong((String)fields.get("build_time"));
		String time = df.format(new Date(buildTime));
		String reportType = (String)fields.get("report_type");
		String isSend = (String)fields.get("is_send");
		String isSuggest = (String)fields.get("is_suggest");
		String filePath = (String)fields.get("file_path");
		String suggestion = fields.get("is_suggest") == "0" ? "无" :  "有";
		String remark = fields.get("remark") == null ? "" : (String)fields.get("remark");
		%>
        <TR>
			<TD class=column2 align=center><input type=checkbox name="chkCheck" value=''></TD>
			<TD class=column2 align='right'><%=count++%></TD>
			<TD class=column2><%=device_serialnumber_%></TD>
			<TD class=column2 align='right'><%=time%></TD>
			<TD class=column2 align=center><%="0".equals(reportType) ? "实时报表" : ("1".equals(reportType) ? "日报表" : ("2".equals(reportType) ? "周报表" : ("3".equals(reportType) ? "月报表" : ("4".equals(reportType) ? "年报表" : ("5".equals(reportType) ? "手动报表" : "(未知)")))))%></TD>
			<TD class=column2 align=center><%="0".equals(isSend) ? "未发送" : ("1".equals(isSend) ? "已发送" : ("2".equals(isSend) ? "未发送(未配置邮箱)" : ("3".equals(isSend) ? "邮件发送失败" : "(未知)")))%></TD>
			<TD class=column2 align=center><%=suggestion%></TD>
			<TD class=column2 align=center>
				<IMG SRC="../images/del.gif" WIDTH="16" HEIGHT="16" BORDER="0" ALT="删除" onclick="delFile('<%=filePath%>')" style="cursor:hand">&nbsp;
				<IMG SRC="../images/edit.gif" WIDTH="16" HEIGHT="16" BORDER="0" ALT="编辑" onclick="viewFile('<%=filePath%>')" style="cursor:hand">&nbsp;
				<IMG SRC="../images/mail.gif" WIDTH="14" HEIGHT="14" BORDER="0" ALT="发送" onclick="sendFile('<%=filePath%>')" style="cursor:hand">
			</TD>
		</TR>
		<%
        fields = cursor.getNext();
    }
	%>
    <TR><TD class=column COLSPAN=10 align=right><%=strBar%></TD></TR>
	<%--<TR><TD class=green_foot COLSPAN=10><a href="javascript://" onclick="delAll('chkCheck')">批量删除</a></TD></TR>--%>
	<%
}
%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>	
</TD></TR>
<TR><TD HEIGHT=20 align="center"><div id="_process"></div></TD></TR>
</TABLE>
<IFRAME name=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;
<div id="debug" style="display:"></div>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function viewFile(file_path) {
  		page = "running_report_build_query_view.jsp?file_path=" + file_path;
		window.open(page,"","left=20,top=20,width=1000,height=750,resizable=yes,scrollbars=yes");
	}

	function sendFile(file_path) {	
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=4&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}

	function delFile(file_path) {
		if (!confirm("确定要删除？")) return false;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=6&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}

	function selectAll(_this, _name) {
		var status = _this.checked;
		var names = document.getElementsByName(_name);
		if (names != null) {
			for (var i=0; i<names.length; i++) {
				names[i].checked = status;
			}
		}
	}
//-->
</SCRIPT>