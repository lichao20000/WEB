<%--
Author		: yanhj
Date		: 2006-8-2
Desc		: 增加修改用户组.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="oper" scope="request" class="com.linkage.litms.system.dbimpl.DbOperator"/>

<%
request.setCharacterEncoding("GBK");
String strMsg = oper.operatorDoAct(request);
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
				<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('RoleList.jsp')" value=" 列 表 " class=btn>

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
