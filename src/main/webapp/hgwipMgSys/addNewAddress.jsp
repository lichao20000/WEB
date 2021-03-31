<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>for add new Ip address</title>
<%
	/**
		 * 新增ip地址管理
		 * 
		 * @author 王志猛(5194) tel:13701409234
		 * @version 1.0
		 * @since 2007-11-06
		 * @category ipmg
		 */
%>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<STYLE type="text/css">
.tr {
	line-height: 22px;
	height: 22px;
}

tr {
	height: 14px;
}

td tr .table {
	border-width: 1px;
	border-style: solid;
	border-collapse: collapse;
	border-spacing: 0px;
}
</STYLE>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
	<script type="text/javascript"
	src="<s:url value="/Js/jquery.blockUI.js"/>"></script>
<script type="text/javascript">
$(function(){
	$().ajaxStart($.blockUI).ajaxStop($.unblockUI);
	$("#netMaskLen").bind("change",function(){
	var va = $(this).attr("value");
		$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/ipMg!getNtMk.action"/>", 
			data: "netMaskLen="+va,
			success:
				function(data)
				{
					$("#netMask").attr("value",data);
				},
			erro:function(xmlR,msg,other){alert(msg);}});
	});
});
function submit()
{
	if((!$.checkIP($("#ipAdr").attr("value")))||($("#ipAdr").attr("value").split(".").length!=4))
	{
		alert("输入的ip不合法，请重新输入！");
		return ;
	}
	$("#wait").show();
	$.ajax({ type: "POST", 
			url: "<s:url value="/hgwipMgSys/ipMg!add.action"/>", 
			data: {"ipAdr":$("#ipAdr").val(),"netMaskLen":$("#netMaskLen").val(),"netMask":$("#netMask").val(),"comment":encodeURIComponent($("#comment").val())},
			success:
				function(data)
				{
					if(data==0)
					{
						alert("新增IP地址成功！");
						window.opener.location.href="<s:url value="/hgwipMgSys/ipMg.action"/>";
						window.close();
					}
					else if(data==-1)
					{
						alert("输入的参数有错误，请重新操作!");
					}
					else if(data==-2)
					{
						alert("网段已经存在，请重新输入ip地址或者掩码");
					}
					else if(data==-3)
					{
						alert("对不起，数据库操作异常！请重新操作，或者稍后再试！");
					}
					else
					{
						alert("系统发生错误，请稍候再试验");
					}
				},
			erro:function(xmlR,msg,other){alert(msg);}});
}
</script>
</head>
<body>
<table width="98%" border="0" align="center" cellpadding="5" cellspacing="0" class="green_gargtd">
	<tr class="title_bigwhite">
		<td colspan="2">新增IP地址资源</td>
	</tr>
</table>
<table width="98%" border=0 align="center" cellpadding="2"	cellspacing="1" bgcolor=#999999>
	<tr bgcolor="#FFFFFF">
		<td width="20%" align=right>子网地址：</td>
		<td width="80%" align=left><input type="text" name="ipAdr" id="ipAdr" /></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="20%" align=right>掩码位数：</td>
		<td width="80%" align=left><select name="netMaskLen" id="netMaskLen">
			<option value="8">8位</option>
			<option value="9">9位</option>
			<option value="10">10位</option>
			<option value="11">11位</option>
			<option value="12">12位</option>
			<option value="13">13位</option>
			<option value="14">14位</option>
			<option value="15">15位</option>
			<option value="16">16位</option>
			<option value="17">17位</option>
			<option value="18">18位</option>
			<option value="19">19位</option>
			<option value="20">20位</option>
			<option value="21">21位</option>
			<option value="22">22位</option>
			<option value="23">23位</option>
			<option value="24">24位</option>
			<option value="25">25位</option>
			<option value="26">26位</option>
			<option value="27">27位</option>
			<option value="28">28位</option>
			<option value="29">29位</option>
			<option value="30">30位</option>
		</select></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td width="20%" align=right>子网掩码：</td>
		<td width="80%" align=left><input type="text" name="netMask" id="netMask" readonly /></td>
	</tr>
	<tr bgcolor="#FFFFFF"><td width="20%" align=right>备注：</td>
	<td width="80%" align=left>
	<textarea rows="3" cols="20" name="comment" id="comment"></textarea>
	</td></tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="2" class="green_foot" align=right><input type="button" name="增加"
			value="增加" onclick="submit();" /></td>
	</tr>
</table>
</body>
</html>
<%@ include file="../foot.jsp"%>