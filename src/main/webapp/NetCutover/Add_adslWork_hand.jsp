<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.List"%>
<%@ page import="com.linkage.litms.netcutover.AdslSocketMsg,com.linkage.litms.system.dbimpl.DbUserRes"%>
<%
request.setCharacterEncoding("GBK");


DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");

List gather_id = dbUserRes.getUserProcesses();


AdslSocketMsg ads = new AdslSocketMsg((String)gather_id.get(0));
String strMsg = ads.socketMsg(request);
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../head.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">工单发送</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column>
						<%
						out.println(strMsg);
						%>
						</TD>
					</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('WorksheetView_snmp.jsp')" value=" 工单视图 " class=btn> 
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
