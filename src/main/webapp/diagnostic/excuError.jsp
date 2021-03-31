<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg = request.getParameter("msg");
String returnJsp = request.getParameter("returnJsp");
if(strMsg != null && strMsg.equals("DBerror")){
	strMsg = "数据库操作失败，请重新操作 ！";
}else if(strMsg != null && strMsg.equals("connError")){
	strMsg = "获取设备结点失败，请检查设备正常连上！";
}else if(strMsg != null && strMsg.equals("noNodeError")){
	strMsg = "此终端设备无相关的上网PVC ！";
}else if(strMsg != null && strMsg.equals("noTemplateError")){
	strMsg = "业务没有模板 ！";
}else {
	strMsg = "工单发送失败，请重新操作 ！";
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
<BR>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="80%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">工单操作提示信息</TH>
					</TR>
					<TR  height="50">
						<TD align=center valign=middle class=column><%=strMsg%></TD>
					</TR>
					<TR>
						<TD class=foot align="right">
							<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('<%=returnJsp%>')" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<BR><BR>
<%@ include file="../foot.jsp"%>