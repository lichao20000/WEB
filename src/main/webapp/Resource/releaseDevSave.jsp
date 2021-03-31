<%--
FileName	: DeviceSave_snmp.jsp
Author		: Dolphin
Date		: 2002-9-24
Note		: 设备资源增加、修改、删除操作
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="userInstAct" scope="request" class="com.linkage.litms.resource.UserInstAct" />
<%
	request.setCharacterEncoding("GBK");
	String strMsg = userInstAct.releaseAct(request);
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">设备用户解绑提示信息</TH>
					</TR>
					<TR height="50">
						<TD align=center valign=middle class=column>
						<%
						out.println(strMsg);
						%>
						</TD>
					</TR>
					<TR>
						<TD class=foot align="right"> 
						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
