<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg = request.getParameter("msg");
String returnJsp = request.getParameter("returnJsp");
if("DBerror".equals(strMsg)){
	strMsg = "���ݿ����ʧ�ܣ������²�����";
}else{
	strMsg = "��������ʧ�ܣ������²�����";
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
						<TH align="center">�豸������ʾ��Ϣ</TH>
					</TR>
					<TR  height="50">
						<TD align=center valign=middle class=column><%=strMsg%></TD>
					</TR>
					<TR>
						<TD class=foot align="right">
							<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('<%=returnJsp%>')" value=" �� �� " class=btn>
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