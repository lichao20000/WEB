<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.system.PingCheck" %>
<%@ include file="../toolbar.jsp"%>
<%
//��ȡϵͳ�вɼ���acs״̬��Ϣ
String dataStr = PingCheck.getInstance().getLatestPingInfo();
%>
<SCRIPT LANGUAGE="JavaScript">
function Refresh()
{
	n = Math.round(Math.random()*1000);
	this.location = "?number="+ n;
}
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="95%">
<TR>
		<TD height="20">&nbsp;</TD>
</TR>
	<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							WEB��ACS��ͨѶ״̬
						</TD>
					</TR>
				</TABLE>
			</TD>
	</TR>
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TD bgcolor="#ffffff" colspan="4" >WEB��ACS��ͨѶ״̬&nbsp;&nbsp;<a href="javascript:Refresh();">ˢ��</a></TD>
					</TR>
					<TR>
						<TH>ACS����</TH>						
						<TH>��ͨʱ��</TH>
						<TH>ͨѶ״̬</TH>
					</TR>
					<%out.println(dataStr);%>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20></TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>


