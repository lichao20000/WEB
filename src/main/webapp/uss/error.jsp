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
		strMsg = "�������㣬�����޷�����";
		//strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 1:
		strMsg = "ʱ���ʽ����ȷ�����鴫���ʱ���ʽ";
		//strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' �� �� ' class=btn>";
		break;
	case 2:
		strMsg = "�������𷽡�����ȷ�����鷢��Ϊ������Ӫҵ����";
		//strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' �� �� ' class=btn>";
		break;
	case 3:
		strMsg = "������Ϊ�ظ������뱣֤����Ψһ��";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 4:
		strMsg = "��֤�벻��ȷ��������֤��";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 5:
		strMsg = "����URL����ȷ����������URLΪ��http://bbms.jsinfo.net/UserSelfServ/login.jsp";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	default:
		strMsg = "ϵͳ����δ֪���������µ�¼";
		//strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		//strJump = strJump
		//+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
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
				<TD class=font1 width=701>������Ϣ��ʾ</TD>
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