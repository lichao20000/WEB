<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.system.User"%>
<%@page import="com.linkage.litms.system.UserMap"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.UserRes"%>
<%@page import="com.linkage.litms.system.dbimpl.LogItem"%>
<%@page import="com.linkage.litms.common.util.Encoder"%>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.util.StringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=GBK"%>
<%

request.setCharacterEncoding("GBK");
String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
function CheckForm()
{
	var InstArea = "<%=InstArea%>";

	var new_pwd = document.getElementsByName('new_pwd')[0].value;
	var old_pwd = document.getElementsByName('old_pwd')[0].value;
	var username = document.getElementById('acc_oid').value;
	if(InstArea=="hn_lt")
	{
		if(old_pwd==new_pwd)
		{
			alert("�����벻���������һ��");
			return false;
		}
		if(new_pwd.length < 8)
		{
			alert("�����볤�Ȳ�������8λ");
			return false;
		}
	}
	
	if(InstArea=="nmg_dx" || InstArea=="jx_dx" || InstArea=="nx_dx" || InstArea=="sx_lt" || InstArea=="ah_dx")
	{
		if(old_pwd==new_pwd)
		{
			alert("�����벻���������һ��");
			return false;
		}
		if(new_pwd.length < 9)
		{
			alert("�����볤�Ȳ�������9λ");
			return false;
		}
	}
	// HBLT-RMS-20200420-LH-001	��������������������
	if (InstArea=="hb_lt") {
	    var pattern = new RegExp("[`~!@#$^&*()=%|{}':;',\\[\\].<>/?\\\\]");
		if(old_pwd==new_pwd)
		{
			alert("�����벻���������һ��");
			return false;
		}
		if(new_pwd.length < 8)
		{
			alert("�����볤�Ȳ�������8λ");
			return false;
		}
		var a=0, b=0, c=0,d=0;
		for ( var i = 0; i < new_pwd.length; i++) {
			var ch = new_pwd.substring(i, i + 1);
			if (ch >= '0' && ch <= '9') {
				a=1;
			} else if (ch >= 'a' && ch <= 'z') {
				b=1;
			} else if (ch >= 'A' && ch <= 'Z') {
				c=1;
			}else if (pattern.test(ch)) {
                d=1;
            }
		}
		var count=a+b+c+d;
		if(count < 3){
			alert("��������ϱ���������֡���д��ĸ��Сд��ĸ�������ַ��е�����");
			return false;
		}
	}
		if (InstArea=="jl_lt") {
			var pattern = new RegExp("[`~!@#$^&*()=%|{}':;',\\[\\].<>/?\\\\]");
			if(old_pwd==new_pwd)
			{
				alert("�����벻���������һ��");
				return false;
			}
			if(new_pwd.length < 8)
			{
				alert("�����볤�Ȳ�������8λ");
				return false;
			}
			var a=0, b=0, c=0,d=0;
			for ( var i = 0; i < new_pwd.length; i++) {
				var ch = new_pwd.substring(i, i + 1);
				if (ch >= '0' && ch <= '9') {
					a=1;
				} else if (ch >= 'a' && ch <= 'z') {
					b=1;
				} else if (ch >= 'A' && ch <= 'Z') {
					c=1;
				}else if (pattern.test(ch)) {
					d=1;
				}
			}
			var count=a+b+c+d;
			if(count < 4){
				alert("��������ϱ���������֡���Сд��ĸ�������ַ�");
				return false;
			}



	}
	
	if(InstArea == "hlj_dx") {
		if (!IsPassword(new_pwd, '����', username, InstArea)) {
			return false;
		}
	}
	var obj = document.frm;
	if(obj.new_pwd.value!=obj.renew_pwd.value){
		alert("ȷ�������������벻���");
		obj.renew_pwd.focus();
		obj.renew_pwd.select();
		return false;
	}
	else{
		return true;
	}
}
</SCRIPT>
<BR>

<FORM NAME="frm" METHOD="post" ACTION="OuterPasswordSave.jsp" onsubmit="return CheckForm()">

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" BORDER="0">
<TR><TD>
	<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				�޸�����
			</td>
			<td ><img
					src="../images/attention.gif" width=15 height=12>&nbsp;�޸ĵ�ǰ����Ա��¼����</td>
		</tr>
	</table>
</TD></TR>
<TR>
	<TD valign=top>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="2" align="center">�޸�����</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >�û���</TD>
					<TD><INPUT TYPE="input" NAME="acc_oid" id="acc_oid" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >������</TD>
					<TD><INPUT TYPE="password" NAME="old_pwd" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >������</TD>
					<TD><INPUT TYPE="password" NAME="new_pwd" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >ȷ��������</TD>
					<TD><INPUT TYPE="password" NAME="renew_pwd" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR>
					<TD colspan="2" align="right" class=green_foot>
						<INPUT TYPE="submit" value=" �� �� " class=jianbian>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="modifypwd">
						<INPUT TYPE="reset" value=" �� д " class=jianbian>
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
	</TABLE>
	</TD>
</TR>
</TABLE>

</FORM>
