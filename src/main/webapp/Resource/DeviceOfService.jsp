<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%
String strData = DeviceAct.getDeviceOfService();

%>
<%@ include file="../head.jsp"%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
<TR>
	<TD valign=top>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							已经开通业务的设备
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH >业务名称</TH>
						<TH >OUI</TH>
						<TH >设备序列号</TH>
					</TR>
					<%=strData%>
				</TABLE>
			</TD>
		</TR>
		<TR><TD></TD></TR>
	</TABLE>
	</TD>
</TR>
</TABLE>
<%
DeviceAct = null;
%>
<%@ include file="../foot.jsp"%>