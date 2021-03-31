<%--
模拟BSS工单
Author: Jason
Version: 1.0.0
Date: 2009-10-13
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<title> BSS模拟工单 </title>
<script type="text/javascript">
<!--//
//显示工单参数信息
function showSheet(){
	var _servTypeId = $("select[@name='servTypeId']");
	var _operateType = $("select[@name='operateType']");
	if('-1' == _servTypeId.val()){
		alert("请选择业务类型");
		_servTypeId.focus();
		return;
	}
	if('-1' == _operateType.val()){
		alert("请选择操作类型");
		_operateType.focus();
		return;
	}
	var url = "<s:url value='/bbms/resource/sendBssSheet!showSheet.action'/>";

	$.post(url,{
		servTypeId:_servTypeId.val(),
		operateType:_operateType.val()
	},function(ajax){
		//alert(ajax);
		$("div[@id='div_sheet']").html("");
		$("div[@id='div_sheet']").append(ajax);
	});
}


//-->
</script>
</head>
<body>
<form name="main_frm" action="" onsubmit="return CheckForm();" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">BSS模拟工单</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"></td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
							<div id="div_title">BSS模拟工单</div>
							<input type="hidden" name="userId" value="<s:property value="userId"/>">
						</TH>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right"width="20%">业务类型</TD>
						<TD width="30%">
							<select name="servTypeId" class=bk>
								<option value="-1">==请选择业务类型==</option>
								<option value="60">上网业务</option>
								<option value="70">EVDO业务</option>
							</select>&nbsp;
							<font color="#FF0000">* </font>
						</TD>
						<TD class=column align="right" width="20%">操作类型</TD>
						<TD width="30%">
							<select name="operateType" class=bk onchange="">
								<option value="-1">==请选择操作类型==</option>
								<option value="1">开户</option>
								<option value="2">暂停</option>
								<option value="3">销户</option>
								<option value="4">复机</option>
							</select>&nbsp;
							<font color="#FF0000">* </font>
						</TD>
					</TR>
					
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<INPUT TYPE="button" value=" 模拟工单 " class="btn" onclick="showSheet();">
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td>
					<div id="div_sheet"></div>
				</td>
			</tr>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>