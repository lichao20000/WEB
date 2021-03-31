<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String InstArea = LipossGlobals.getLipossProperty("InstArea.ShortName");
//用户重新登录，从在线列表中删除用户
Object obj = session.getAttribute("curUser");
String username = "";
if(null != obj) {
	UserRes userRes = (UserRes)obj;
	username = userRes.getUser().getAccount();
}
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" src="../Js/md5.js" charset="utf-8"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
function CheckForm()
{
	var username = "<%=username%>";
	var InstArea = "<%=InstArea%>";
	var new_pwd = document.getElementsByName('new_pwd')[0].value;
	var old_pwd = document.getElementsByName('old_pwd')[0].value;
  	var renew_pwd = document.getElementsByName('renew_pwd')[0].value;

	if(InstArea=="hn_lt")
	{
		if(old_pwd==new_pwd)
		{
			alert("新密码不能与旧密码一致");
			return false;
		}if(new_pwd.length < 8)
		{
			alert("新密码长度不能少于8位");
			return false;
		}
	}

	if(InstArea=="nmg_dx" || InstArea=="nx_dx" || InstArea=="sx_lt")
	{
		if(old_pwd==new_pwd)
		{
			alert("新密码不能与旧密码一致");
			return false;
		}if(new_pwd.length < 9)
		{
			alert("新密码长度不能少于9位");
			return false;
		}

	}
	// HBLT-RMS-20200420-LH-001	服务器弱口令整改需求
	if (InstArea=="hb_lt") {
	    var pattern = new RegExp("[`~!@#$^&*()=%|{}':;',\\[\\].<>/?\\\\]");
		if(old_pwd==new_pwd)
		{
			alert("新密码不能与旧密码一致");
			return false;
		}
		if(new_pwd.length < 8)
		{
			alert("新密码长度不能少于8位");
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
			alert("新密码组合必须包含数字、大写字母、小写字母、特殊字符中的三种");
			return false;
		}
	}
	
	if(InstArea == "hlj_dx" || InstArea == "nx_lt") {
		if (!IsPassword(new_pwd, '密码', username,InstArea)) {
			return false;
		}
	}
	var obj = document.frm;
  	obj.new_pwd.value = encode64(new_pwd);
  	obj.old_pwd.value = encode64(old_pwd);
  	obj.renew_pwd.value = encode64(renew_pwd);
	if(obj.new_pwd.value!=obj.renew_pwd.value){
		alert("确认密码与新密码不相符");
		obj.renew_pwd.focus();
		obj.renew_pwd.select();
		return false;
	}
	else{
		return true;
	}
}
var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"
  + "wxyz0123456789+/" + "=";

function encode64(input) {

  var output = "";
  var chr1, chr2, chr3 = "";
  var enc1, enc2, enc3, enc4 = "";
  var i = 0;
  do {
    chr1 = input.charCodeAt(i++);
    chr2 = input.charCodeAt(i++);
    chr3 = input.charCodeAt(i++);
    enc1 = chr1 >> 2;
    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
    enc4 = chr3 & 63;
    if (isNaN(chr2)) {
      enc3 = enc4 = 64;
    } else if (isNaN(chr3)) {
      enc4 = 64;
    }
    output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)
      + keyStr.charAt(enc3) + keyStr.charAt(enc4);
    chr1 = chr2 = chr3 = "";
    enc1 = enc2 = enc3 = enc4 = "";
  } while (i < input.length);

  return output;
}


</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<BR>

<FORM NAME="frm" METHOD="post" ACTION="passwordSave.jsp" onsubmit="return CheckForm()">

<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="98%" BORDER="0">
<TR><TD>
	<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				修改密码
			</td>
			<td ><img
					src="../images/attention.gif" width=15 height=12>&nbsp;修改当前管理员登录密码</td>
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
					<TH  colspan="2" align="center">修改密码</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >旧密码</TD>
					<TD><INPUT TYPE="password" NAME="old_pwd" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >新密码</TD>
					<TD><INPUT TYPE="password" NAME="new_pwd" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >确认新密码</TD>
					<TD><INPUT TYPE="password" NAME="renew_pwd" maxlength=20 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR>
					<TD colspan="2" align="right" class=green_foot>
						<INPUT TYPE="submit" value=" 保 存 " class=jianbian>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="modifypwd">
						<INPUT TYPE="hidden" name="acc_oid" value="<%=user.getAccount()%>">
						<INPUT TYPE="reset" value=" 重 写 " class=jianbian>
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
<%@ include file="../foot.jsp"%>

