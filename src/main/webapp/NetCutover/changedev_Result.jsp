<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%
request.setCharacterEncoding("GBK");
String username = request.getParameter("username");
String oui = request.getParameter("oui");
String device_serialnumber = request.getParameter("device_serialnumber");
String oui_new = request.getParameter("oui_new");
String device_serialnumber_new = request.getParameter("device_serialnumber_new");
%>
<link rel="stylesheet" href="../css/listview.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript">
<!--
function retum()
{
	window.history.go(-1);
}
//-->
</SCRIPT>
<FORM name="frm" action="" method="post">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD>
		</TR>
		<TR>
			<TD>
				<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											更换设备
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										更换前后设备信息。&nbsp;&nbsp;
									</td>

								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
								<TR>
									<TH colspan="4">用户【<%=username%>】绑定的设备信息</TH>
								</TR>							
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										原绑定设备
									</TD>
									<TD align="left" width="80%" colspan="3">
										<%=oui%> - <%=device_serialnumber%>
									</TD>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										更换后设备
									</TD>
									<TD align="left" width="80%" colspan="3">
										<font color="green"><%=oui_new%> - <%=device_serialnumber_new%></font>
									</TD>
								</TR>
								<TR  bgcolor="#ffffff">
									<TD colspan="4" width="60%" align="right">
										<font color="green">『提示信息』：设备尚未连接本系统，待连接后将发送工单，请【返回】</font>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="button" value=" 返 回 " onclick="retum();">
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</form>
<%@ include file="../foot.jsp"%>
