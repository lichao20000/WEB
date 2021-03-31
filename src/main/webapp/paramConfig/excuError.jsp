<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg = request.getParameter("msg");
String returnJsp = request.getParameter("returnJsp");
if("DBerror".equals(strMsg)){
	strMsg = "数据库操作失败，请重新操作！";
}else{
	strMsg = "工单发送失败，请重新操作！";
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
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="60%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">设备操作提示信息</TH>
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
<%@ include file="../foot.jsp"%>