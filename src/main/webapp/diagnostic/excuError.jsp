<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String strMsg = request.getParameter("msg");
String returnJsp = request.getParameter("returnJsp");
if(strMsg != null && strMsg.equals("DBerror")){
	strMsg = "���ݿ����ʧ�ܣ������²��� ��";
}else if(strMsg != null && strMsg.equals("connError")){
	strMsg = "��ȡ�豸���ʧ�ܣ������豸�������ϣ�";
}else if(strMsg != null && strMsg.equals("noNodeError")){
	strMsg = "���ն��豸����ص�����PVC ��";
}else if(strMsg != null && strMsg.equals("noTemplateError")){
	strMsg = "ҵ��û��ģ�� ��";
}else {
	strMsg = "��������ʧ�ܣ������²��� ��";
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
						<TH align="center">����������ʾ��Ϣ</TH>
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
<BR><BR>
<%@ include file="../foot.jsp"%>