<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.tree.Module"%>
<jsp:useBean id="userManage" scope="request" class="com.linkage.litms.system.dbimpl.DbUserManager"/>

<%
request.setCharacterEncoding("GBK");
String strMsg = userManage.userInfoDoAct(request);
String strAction = request.getParameter("action");

if("操作成功！".equals(strMsg)){
	Module.clearModuleRoleMap();
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR><TD valign=top>
  <TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" width="65%" align="center" ONSELECTSTART="return false;" nowrap>
	<TR height="10" nowrap><TD></TD></TR>
	<TR>
	  <TD bgcolor="#999999">
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR>
				<TH align="center">操作提示信息</TH>
			</TR>
				<TR  height="50">
					<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
				</TR>
			<TR>
				<TD class=foot align="right">
				<%if(!strAction.equals("modifypwd")){%>
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('UserList.jsp')" value=" 列 表 " class=btn>
				<%}%>

				<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.back();" value=" 返 回 " class=btn>
				</TD>
			</TR>
		</TABLE>	  
	  </TD>
	</TR>
	<TR height="20" nowrap>
	  <TD></TD>
	</TR>
  </TABLE>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
