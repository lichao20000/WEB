<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> 
function query(){
	var url = "<s:url value="/gwms/sysConfig/resourceBindMemConfig!getUserInfo.action"/>";
	var username = $("input[@name='username']").val().trim();
	var usernameType = $("SELECT[@name='usernameType']").val().trim();
	if(""==username){
		alert("请输入待查询的帐号！");
	}
	$("div[@id='userInfo']").hide();
	$("div[@id='userInfoDbMem']").hide();
	$.post(url,{
		usernameType:usernameType,
		username:username
    },function(mesg){
		$("div[@id='userInfo']").show();
		$("div[@id='userInfo']").html(mesg);
		parent.dyniframesize();
    });
}

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>

</head>
<body>
<form name="form1">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=18>&nbsp;</TD>
	</TR>
	<TR  id="tr2"  STYLE="display" >
  		<TD>
			<table class="querytable">
				<TR>
					<th colspan="2">
						用户信息
					</th>
				</tr>

				<TR>
					<TD class=column width="15%" align='right'>
						<SELECT name="usernameType">
							<option value="1">
								LOID
							</option>
							<option value="2">
								上网宽带账号
							</option>
							<option value="3">
								IPTV宽带账号
							</option>
							<option value="4">
								VoIP认证号码
							</option>
							<option value="5">
								VoIP电话号码
							</option>
						</SELECT>
					</TD>
					<TD width="75%">
						<input type="text" name="username" size="30" maxlength="30" class=bk />
					</TD>
				</TR>
				<TR>
					<td colspan="2" align="right" class=foot>
						<button onclick="query()">
							&nbsp;查 询&nbsp;
						</button>
					</td>
				</TR>
			</table>
		</TD>
	</TR>
	<tr>
		<td>
			<div id="userInfo"></div>
		</td>
	</tr>
	<tr>
		<td>
			<div id="userInfoDbMem"></div>
		</td>
	</tr>
</TABLE>
</form>
</body>
