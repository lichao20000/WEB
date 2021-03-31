<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String strId = request.getParameter("errid");
	int MsgId;
	String strMsg = "";
	String strJump = "";
	if (null == strId || "".equals(strId))
		MsgId = 0;
	else
		MsgId = Integer.parseInt(strId);	
	switch (MsgId) {
	case 0:
		strMsg = "参数不足，程序无法运行";
		//strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 1:
		strMsg = "时间格式不正确，请检查传入的时间格式";
		//strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 2:
		strMsg = "“请求发起方”不正确，请检查发起方为“网上营业厅”";
		//strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' 返 回 ' class=btn>";
		break;
	case 3:
		strMsg = "该请求为重复请求，请保证请求唯一性";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 4:
		strMsg = "认证码不正确，请检查认证码";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	case 5:
		strMsg = "请求URL不正确，请检查请求URL为：http://bbms.jsinfo.net/UserSelfServ/login.jsp";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	default:
		strMsg = "系统出现未知错误，请重新登录";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' 登 录 ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' 返 回 ' class=btn>";
		break;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb_2312">
<link href="./css/tags.css" rel="stylesheet" type="text/css">
<base target="_top">
</HEAD>

<BODY>
<TABLE cellSpacing=0 cellPadding=0 width=775 align=center border=0>
	<TR>
		<TD valign=top background=./images/title.jpg height=60>
		<TABLE cellSpacing=0 cellPadding=0 width=775 border=0>
			<TR>
				<TD width=59 height=40>&nbsp;</TD>
				<TD class=font1 width=701>错误信息提示</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>

<TABLE border="1" cellspacing="0" cellpadding="0" width=775 align=center>
	<TR bgcolor="#FFFFFF">
		<TD height=50 align=center class="header-left">
		<%
			out.println(strMsg);
		%>
		</TD>
	</TR>
</TABLE>
</BODY>
</HTML>