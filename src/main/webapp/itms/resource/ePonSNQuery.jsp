<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>序列号烧制</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<%
	String gw_type = request.getParameter("gw_type");
 %>
<script language="javascript">
var gw_type = '<%= gw_type%>'
<!--//

function checkForm(){

	var username = $.trim($("input[@name='username']").val());
	var voipUsername = $.trim($("input[@name='voipUsername']").val());
	var voipPhone = $.trim($("input[@name='voipPhone']").val());
	
	if(""==username && ""==voipUsername && ""==voipPhone){
		alert("请至少输入一项查询条件!");
		return false;
	}else{
		return true;
	}
}

//提交查询
function doQuery(){
	if(false == checkForm()){
		return false;
	}
	
	var username = $.trim($("input[@name='username']").val());
	var voipUsername = $.trim($("input[@name='voipUsername']").val());
	var voipPhone = $.trim($("input[@name='voipPhone']").val());
	
	document.getElementById("dataUser").innerHTML = "正在查询，请稍后..."
	var url = '<s:url value="/itms/resource/ePonSNQuery.action"/>';
	$.post(url,{
		username:username,
		voipUsername:voipUsername,
		voipPhone:voipPhone,
		gw_type:gw_type
    },function(mesg){
    	document.getElementById("dataUser").innerHTML = mesg;
    });
}


//-->
</script>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center height="auto">
	<TR><TD HEIGHT=20>&nbsp;</TD></TR>
	<tr>
		<td>
		<table width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite" >序列号烧制</td>
				<td>至少输入一项查询条件！</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<form name=frm action="" method="POST" target="dataForm">
		<table width="100%" border="0" cellspacing="1" cellpadding="2"
			bgcolor="#999999">
			<TR><TH colspan="4">用户信息查询</TH></TR>
			<tr bgcolor=#ffffff id="user1">
				<td class=column align=right width="15%">用户账号</td>
				<td width="35%"><input type="text" maxlength="40" name="username"></td>
				<td class=column align=right width="15%">VOIP认证账号</td>
				<td width="35%"><input type="text" maxlength="40" name="voipUsername"></td>
			</tr>
			<tr bgcolor=#ffffff id="user2">
				<td class=column align=right width="15%">VOIP电话号码</td>
				<td width="35%"><input type="text" maxlength="40" name="voipPhone"></td>
				<td class=column align=right width="15%"></td>
				<td width="35%"></td>
			</tr>
			<tr bgcolor=#ffffff id="user3">
				<td class=green_foot colspan=4 align=right>
					<input type="button" class="jianbian" value=" 查 询 " onclick="doQuery()">
				</td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr id="rsdata" style="display: ">
		<td>
			<div id="dataUser" align=center>
			</div>
		</td>
	</tr>
	<tr>
		<td height=20>&nbsp;</td>
	</tr>
</TABLE>
<%@ include file="../../foot.jsp"%>
</body>
</html>