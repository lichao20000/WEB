<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<% 
     
	request.setCharacterEncoding("GBK");
	String strId = request.getParameter("errid");
	System.out.println("strId === " + strId);
	int MsgId;
	String strMsg = "";
	String strJump = "";
	if (strId.equals(""))
		MsgId = 0;
	else
		MsgId = Integer.parseInt(strId);
	switch (MsgId) {
	case 0:
		strMsg = "参数不足，程序无法运行";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 1:
		strMsg = "系统中没有定义此操作员或无法验证用户";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 2:
		strMsg = "此操作员没有定义角色";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 3:
		strMsg = "系统没有定义此操作员或登录已超时，请重新登录";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 4:
		strMsg = "系统出现意外错误";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 5:
		strMsg = "此用户没有权限运行该子系统，请重新登录";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 6:
		strMsg = "您的IP地址不合法，无法访问网络开通系统，请返回";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 7:
		strMsg = "您输入的确认码和系统产生的不一致，请重新登录";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 8:
		strMsg = "错误，此记录可能已被删除，请返回";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 9:
		strMsg = "错误，用户信息没有属地信息，请确认";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 10:
		strMsg = "错误，用户信息不完整，请确认";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 11:
		strMsg = "您的密码已经过期，请与管理员联系";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 12:
		strMsg = "您的账号在当前时间不允许登录，请与管理员联系";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 13:
		strMsg = "您登录的地点不在允许的范围内，请与管理员联系";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 14:
        strMsg = "您失败登录次数太多了，现处于锁定时间，请过段时间再登录";
	    strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
	    break;
	case 15:
        strMsg = "用户验证失败，请确认密码或域是否正确";
	    strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
	    break;
	case 16:
        strMsg = "系统中没有定义此操作员";
	    strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
	    break;
	case 17:
        strMsg = "您失败登录次数太多了，IP地址现处于锁定时间，请过段时间再登录";
	    strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
	    break;
	default:
		strMsg = "系统出现未知错误，请重新登录";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE><%=LipossGlobals.getLipossName()%></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=GBK">
<LINK REL="stylesheet" HREF="../css/css_blue.css" TYPE="text/css">
<base target="_top">
<SCRIPT LANGUAGE="JavaScript">
<!--
function goLogo(){
	this.location = "./login.jsp";
}
//-->
</SCRIPT>
</HEAD>

<BODY scrolling="no">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<p>&nbsp;</p>
		<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">错误信息提示</TH>
					</TR>
					<TR>
						<TD class=column height=50 align=center>
						<%
						out.println(strMsg);
						%>
						</TD>
					</TR>
					<TR>
						<TD class=foot align="right"><%=strJump%></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>&nbsp;</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
</TABLE>
</BODY>
</HTML>
