<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<jsp:useBean id="sysManage" scope="request" class="com.linkage.litms.system.SysManage"/>
<jsp:useBean id="sysManagerClient" scope="request" class="com.linkage.litms.common.corba.SysManagerClient"/>

<%
request.setCharacterEncoding("GBK");
//入库成功
int backup_id = sysManage.tabOperation(request);

if(backup_id < 0){
	response.sendRedirect("backup_fail.jsp?redirect=tab_backup.jsp");
	return;
}

// 开始调用corba接口

// corba接口返回的成功与失败结果 1:success 0 : fail

String strSql = "";
short s1 = 1;
int _result = sysManagerClient.dbManager(backup_id,s1);

strSql = "select * from tab_db_backup where backup_id=" + backup_id;
// teledb
if (DBUtil.GetDB() == 3) {
	strSql = "select exec_status, tab_name, tab_name_bk, execute_time, complet_time" +
			" from tab_db_backup where backup_id=" + backup_id;
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSql);
psql.getSQL();

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function init(){
    var tmpSql = "<%=strSql%>";
    var result = "<%=_result%>";
	if(result == 1){
		alert("调用后台接口成功！");
	} else {
		alert("调用后台接口失败！");
	}
	var page = "backup_List.jsp?tmpSql=" + tmpSql + "";
	document.all("childFrm").src = page;
	showMsgDlg();
}
function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: 宋体">请稍等······</span>
		</td>
	</tr>
</table>
</center>
</div>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<FORM name="frm" action="" >
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR><td>
			<Div id="idList">
			</Div>
			</td>
		</TR>
	</TABLE>
</TD></TR>
	<TR>
		<TD HEIGHT=10>&nbsp;<IFRAME ID=childFrm SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm2 SRC="about:blank" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript">
<!--
init();
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>