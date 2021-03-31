<%--
故障诊断-高级查询-X_COM_PASSWD。
Linkage Technology (NanJing) Co., Ltd
Copyright 2008-2012. All right reserved.
Author: Alex.Yan(yanhj@lianchuang.com)
Version: 1.0.0
Date: 2009-7-9
Desc:维护密码.
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/jquery.js'/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="../../Js/inmp/commFunction.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';

<!--
$(function(){
	init();
});
//get data
function init() {
	
	parent.unblock1();

	var xcompasswd = $("input[@name='xcompasswd']");

	document.all("operResult").style.display = "";
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("数据采集:");
	$("div[@id='operResult']").append("<s:property value='result'/>");
	
	xcompasswd.val("<s:property value='xcompasswd'/>");

	setTimeout("clearResult()", 3000);
	
	<%-- 当前用户点击了超级管理员密码按钮跳转页面后，记录超级权限日志 --%>
	superAuthLog('ShowDevPwd',"查看[<s:property value='deviceSn'/>]设备的电信维护密码[<s:property value='xcompasswd'/>]");
}

//change value
function chgmode()
{
	var mode = $("select[@name='mode']");
	var xcompasswd = $("input[@name='xcompasswd']");
	var randompasswd = $("input[@name='randompasswd']");
	if (mode.val() == "0")
	{
		xcompasswd.val("");
		xcompasswd.attr("readonly", false);
	} 
	else if (mode.val() == "1")
	{
		xcompasswd.val("telecomadmin");
		xcompasswd.attr("readonly", true);
	}
	else {
		xcompasswd.val(randompasswd.val());
		xcompasswd.attr("readonly", true);
	}
}

//config.
function CheckPost() {
	var deviceId = $("input[@name='deviceId']").val();
	var xcompasswd = $("input[@name='xcompasswd']").val();
	var strategyType = $("select[@name='strategyType']").val();
	
	if (xcompasswd.length < 8 || xcompasswd.length > 20)
	{
		alert("密码长度必须8-20位");
		return false;
	}

	var url = "<s:url value='/inmp/diagnostics/xcompasswdACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId,
		xcompasswd:xcompasswd,
		strategyType:strategyType,
		gw_type:gw_type
	},function(ajax){
		document.all("operResult").style.display = ""
		$("div[@id='operResult']").html("");
		$("div[@id='operResult']").html("通知后台:");
		$("div[@id='operResult']").append("<s:property value='result'/>");
	});

	setTimeout("clearResult()", 3000);
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}

//-->
</SCRIPT>

<BODY>

<FORM NAME="frm" METHOD="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<TR align="left">
					<TH colspan="1">密码</TH>
					<TH colspan="1">策略方式</TH>
					<TH colspan="1">描述</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="40%" align="center">
						<SELECT NAME="mode" onchange="chgmode()">
							<OPTION VALUE="0" SELECTED>手工输入
							<OPTION VALUE="1">默认密码
							<OPTION VALUE="2">随机生成
						</SELECT>&nbsp;
						<INPUT TYPE="text" NAME="xcompasswd"  value= "0" size="20" maxlength="20">
						&nbsp;<font color="red">*</font></TD>
					<TD class=column align="center" width="20%">
						<SELECT NAME="strategyType">
							<OPTION VALUE="0" SELECTED>立即执行
							<!-- <OPTION VALUE="1">第一次连到系统 -->
							<OPTION VALUE="2">周期上报
							<!-- <OPTION VALUE="3">重新启动 -->
							<OPTION VALUE="4">下次连到系统
							<OPTION VALUE="5">设备启动
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD class=column>密码建议随机生成，较为安全，密码长度8-20位.</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="3" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<INPUT NAME="randompasswd" TYPE="hidden"  value="<s:property value='randompasswd'/>">
					<input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckPost();">&nbsp;&nbsp;<input type="reset" value=" 重 置 " class="jianbian"></TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>

</BODY>


