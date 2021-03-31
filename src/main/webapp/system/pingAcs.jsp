<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.system.PingCheck" %>
<%@ include file="../toolbar.jsp"%>
<%
//获取系统中采集的acs状态信息
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
							WEB与ACS的通讯状态
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
						<TD bgcolor="#ffffff" colspan="4" >WEB与ACS的通讯状态&nbsp;&nbsp;<a href="javascript:Refresh();">刷新</a></TD>
					</TR>
					<TR>
						<TH>ACS名称</TH>						
						<TH>不通时间</TH>
						<TH>通讯状态</TH>
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


