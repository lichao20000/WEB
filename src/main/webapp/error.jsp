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
		strMsg = "�������㣬�����޷�����";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 1:
		strMsg = "ϵͳ��û�ж���˲���Ա���޷���֤�û�";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' �� �� ' class=btn>";
		break;
	case 2:
		strMsg = "�˲���Աû�ж����ɫ";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' �� �� ' class=btn>";
		break;
	case 3:
		strMsg = "ϵͳû�ж���˲���Ա���¼�ѳ�ʱ�������µ�¼";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 4:
		strMsg = "ϵͳ�����������";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 5:
		strMsg = "���û�û��Ȩ�����и���ϵͳ�������µ�¼";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 6:
		strMsg = "����IP��ַ���Ϸ����޷��������翪ͨϵͳ���뷵��";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 7:
		strMsg = "�������ȷ�����ϵͳ�����Ĳ�һ�£������µ�¼";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='goLogo();' value=' �� �� ' class=btn>";
		break;
	case 8:
		strMsg = "���󣬴˼�¼�����ѱ�ɾ�����뷵��";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 9:
		strMsg = "�����û���Ϣû��������Ϣ����ȷ��";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 10:
		strMsg = "�����û���Ϣ����������ȷ��";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 11:
		strMsg = "���������Ѿ����ڣ��������Ա��ϵ";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 12:
		strMsg = "����IP��ַ���Ϸ����޷�����ϵͳ���뷵��";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 13:
		strMsg = "����¼ʧ�ܴ������࣬�ѱ����������Ժ��¼��Ѱ����";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 14:
		strMsg = "��ǰ�ʺ����������ط���¼���Ƿ������¼";
		strJump = "<INPUT TYPE=button onclick='goLogin()' value=' �� �� ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
		break;
	case 15:
		strMsg = "������������ѱ��������������Ա��ϵ";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:goLogo();' value=' �� �� ' class=btn>";
		break;
	case 16:
		strMsg = "�����˺��ѱ���������ȴ� "+allowLoginTime+"�� �����ԡ�";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:goLogo();' value=' �� �� ' class=btn>";
		break;
	case 17:
		strMsg = "�û��������벻ƥ�䣬�뷵�����ԡ�";
		strJump = "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:goLogo();' value=' �� �� ' class=btn>";
		break;
	default:
		strMsg = "ϵͳ����δ֪���������µ�¼";
		strJump = "<INPUT TYPE=button onclick='goLogo()' value=' �� ¼ ' class=btn>";
		strJump = strJump
		+ "&nbsp;&nbsp;<INPUT TYPE='button' NAME='cmdBack' onclick='javascript:history.go(-1);' value=' �� �� ' class=btn>";
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
	// ���� Ϊthis.location = "index.jsp";
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
						<TH align="center">������Ϣ��ʾ</TH>
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

<!-- added by yanhj 2007-12-17:���� -->
<SCRIPT LANGUAGE="JavaScript">
<!--
document.all.cmdBack.focus();

//-->
//-->
</SCRIPT>
<!-- end 2007-12-17 -->

</HTML>
