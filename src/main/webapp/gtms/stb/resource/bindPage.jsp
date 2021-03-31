<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>绑定页面</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

function nextStep(){
	var servaccount=$.trim($("input[@name='servaccount1']").val());
	var MAC=$.trim($("input[@name='MAC1']").val());
	if(""==servaccount)
	{
	alert("请填写业务账号!");
	return false;
	}
	if(""==MAC)
	{
	alert("请填写MAC地址!");
	return false;
	}
	var url = "<s:url value='gtms/stb/resource/bindPage!getMacList.action'/>";
	$.post(url,{
		MAC:MAC,
		servaccount:servaccount
	},function(ajax){
	    $("div[@id='div_user']").html("");
		$("div[@id='div_user']").append(ajax);
	});	
	document.all("tr_userinfo").style.display="";
}
function userOnclick(mac,servaccount){
	$("input[@name='servaccount']").val(servaccount);
	$("input[@name='MAC']").val(mac);
	$("div[@id='div_bind']").css("display","");
	$("div[@id='div_bind']").html("<input type='button' value='开始绑定' onclick=' query()'></input>");
}
function query()
{
	var servaccount=$.trim($("input[@name='servaccount']").val());
	var MAC=$.trim($("input[@name='MAC']").val());
	if(""==MAC)
	{
		var MAC=$.trim($("input[@name='MAC1']").val());
	}
	/* if(""==servaccount)
	{
	alert("请填写业务账号!");
	return false;
	}
	if(""==MAC)
	{
	alert("请填写MAC地址!");
	return false;
	} */
	var url = "<s:url value='gtms/stb/resource/bindPage!bindCheck.action'/>";
	$.post(url, {
		MAC:MAC,
		servaccount:servaccount
	}, function(ajax) {
		if (ajax == "成功") {
			alert("绑定成功!");
		} else {
			alert(ajax);
		}
	});
}
</script>
</head>

<body>
	<form name="frm" id="frm" method="post"
		action=""
		target="dataForm">
	<input type="hidden" value="" name="servaccount">
		<input type="hidden" value="" name="MAC">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								绑定页面</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /><font color="red">*</font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">绑定页面</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="deviceType21" STYLE="">
							<TD align="right" class=column width="15%">业务账号：</TD>
							<TD  width="35%"><INPUT TYPE="text"
								NAME="servaccount1" maxlength=30 class=bk size=20>&nbsp; <font
								color="red"> *</font></TD>
								<TD align="right" class=column  width="15%">MAC地址：</TD>
							<TD width="35%"><INPUT TYPE="text" NAME="MAC1"
								maxlength=30 class=bk size=20>&nbsp; <font
								color="red"> *</font></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class="foot" width="100%">
								<input type='button' value = '下一步' onclick=" nextStep()"></input>
							</td>
						</TR>
			<TR style="display: none;width: 100%;" id="tr_userinfo">
			<TD colspan="4" width="100%">
				<div id="div_user"
					style="width: 100%;">
				</div>
			</TD>
			</TR>
		<TR style="width: 100%">
			<TD align="center" colspan="4" width="100%">
				<div id="div_bind" style="display: none;text-align: center;width: 100%;" >
				</div>
			</TD>
		</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
