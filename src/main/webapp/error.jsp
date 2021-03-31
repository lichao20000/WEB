<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	request.setCharacterEncoding("GBK");
	String strId = request.getParameter("errid");
	String allowLoginTime = request.getParameter("allowLoginTime")==null? "" : request.getParameter("allowLoginTime").toString();
	
	String acc_loginname = request.getParameter("acc_loginname")==null? "" : request.getParameter("acc_loginname").toString();
	String acc_password = request.getParameter("acc_password") == null? "" : request.getParameter("acc_password").toString();
	String area_name = request.getParameter("area_name") == null ? "" : request.getParameter("area_name").toString();
	String checkCode = request.getParameter("checkCode") == null ? "" : request.getParameter("checkCode").toString();
	
	int MsgId;
	String strMsg = "";
	String strJump = "";
	if ("".equals(strId))
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
		strMsg = "您的IP地址不合法，无法访问系统，请返回";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 13:
		strMsg = "您登录失败次数过多，已被锁定，请稍候登录或寻求解绑";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 14:
		strMsg = "当前帐号已在其他地方登录，是否继续登录";
		strJump = "<INPUT TYPE=button onclick='goLogin()' value=' 继 续 ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 15:
		strMsg = "您的密码过期已被锁定，请与管理员联系";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 16:
		strMsg = "您的账号已被锁定，请等待 "+allowLoginTime+"秒 后重试。";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 17:
		strMsg = "用户名或密码不匹配，请返回重试。";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:goLogo();' value=' 返 回 ' class=btn>";
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
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312">
<LINK REL="stylesheet" HREF="./css/css_green.css" TYPE="text/css">
<base target="_top">
<SCRIPT LANGUAGE="JavaScript">
<!--

function goLogo(){
	this.location = "login.jsp";
	// 甘肃 为this.location = "index.jsp";
}

function goLogin(){
	document.frm.submit();
}
//-->
</SCRIPT>
</HEAD>

<BODY scrolling="no">
<form method="POST" action="<s:url value="/checkuser.jsp"/>" name=frm>
	<input type="hidden" name="deleFlag" value="1">
	<input type="hidden" name="acc_loginname" value="<%=acc_loginname %>">
	<input type="hidden" name="acc_password" value="<%=acc_password %>">
	<input type="hidden" name="area_name" value="<%=area_name %>">
	<input type="hidden" name="checkCode" value="<%=checkCode %>">
</form>
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
					<TR bgcolor="#FFFFFF">
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

<!-- added by yanhj 2007-12-17:焦点 -->
<SCRIPT LANGUAGE="JavaScript">
<!--
document.all.cmdBack.focus();

//-->
//-->
</SCRIPT>
<!-- end 2007-12-17 -->

</HTML>
