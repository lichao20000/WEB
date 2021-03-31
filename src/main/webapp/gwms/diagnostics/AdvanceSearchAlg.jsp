<%--
故障诊断-高级查询-ALG。
Linkage Technology (NanJing) Co., Ltd
Copyright 2008-2012. All right reserved.
Author: Alex.Yan (yanhj@lianchuang.com)
Version: 1.0.0
Date: 2009-6-29
Desc: ALG
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';
<!--

$(function(){
	init();
});
//get data
function init() {

	parent.unblock();
	
	var h323Enab = $("input[@name='h323Enab']");
	var sipEnab = $("input[@name='sipEnab']");
	var rtspEnab = $("input[@name='rtspEnab']");
	var l2tpEnab = $("input[@name='l2tpEnab']");
	var ipsecEnab = $("input[@name='ipsecEnab']");
	
	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("数据采集:");
	$("div[@id='operResult']").append("<s:property value='result'/>");

	if ("1" == '<s:property value="h323Enab"/>') {
		h323Enab.attr("checked", true);
	}

	if ("1" == '<s:property value="sipEnab"/>') {
		sipEnab.attr("checked", true);
	}

	if ("1" == '<s:property value="rtspEnab"/>') {
		rtspEnab.attr("checked", true);
	}

	if ("1" == '<s:property value="l2tpEnab"/>') {
		l2tpEnab.attr("checked", true);
	}

	if ("1" == '<s:property value="ipsecEnab"/>') {
		ipsecEnab.attr("checked", true);
	}

	setTimeout("clearResult()", 3000);
	
}

//config.
function CheckForm() {
	var deviceId = $("input[@name='deviceId']");
	var h323Enab = $("input[@name='h323Enab']");
	var sipEnab = $("input[@name='sipEnab']");
	var rtspEnab = $("input[@name='rtspEnab']");
	var l2tpEnab = $("input[@name='l2tpEnab']");
	var ipsecEnab = $("input[@name='ipsecEnab']");

	if (h323Enab.attr("checked") == true) {
		h323Enab.val("1");
	} else {
		h323Enab.val("0");
	}

	if (sipEnab.attr("checked") == true) {
		sipEnab.val("1");
	} else {
		sipEnab.val("0");
	}

	if (rtspEnab.attr("checked") == true) {
		rtspEnab.val("1");
	} else {
		rtspEnab.val("0");
	}

	if (l2tpEnab.attr("checked") == true) {
		l2tpEnab.val("1");
	} else {
		l2tpEnab.val("0");
	}

	if (ipsecEnab.attr("checked") == true) {
		ipsecEnab.val("1");
	} else {
		ipsecEnab.val("0");
	}

	var url = "<s:url value='/gwms/diagnostics/algACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		h323Enab:h323Enab.val(),
		sipEnab:sipEnab.val(),
		rtspEnab:rtspEnab.val(),
		l2tpEnab:l2tpEnab.val(),
		ipsecEnab:ipsecEnab.val(),
		gw_type:gw_type
	},function(ajax){
		document.all("operResult").style.display = ""
		$("tr[@id='tr002']").show();
		var s = ajax.split("|");
		var desc = "失败";
		if (s[0] == "1"){
			desc = "成功";
			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[1];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });	
		}
		$("div[@id='operResult']").html("");
		$("div[@id='operResult']").html("通知后台:");
		$("div[@id='operResult']").append(desc);
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
					<TH colspan="1">状态</TH>
					<TH colspan="1">ALG</TH>
					<TH colspan="1">描述</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="15%" align="center"><input type="checkbox" name="h323Enab""></TD>
					<TD class=column align="center" width="35%">H323开关</TD>
					<TD class=column>H323:选中开启，否则关闭</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="15%" align="center"><input type="checkbox" name="sipEnab"></TD>
					<TD class=column align="center" width="35%">SIP开关</TD>
					<TD class=column>SIP:选中开启，否则关闭</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="15%" align="center"><input type="checkbox" name="rtspEnab"></TD>
					<TD class=column align="center" width="35%">RTSP开关</TD>
					<TD class=column>RTSP:选中开启，否则关闭</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="15%" align="center"><input type="checkbox" name="l2tpEnab"></TD>
					<TD class=column  align="center" width="35%">L2TP开关</TD>
					<TD class=column>L2TP:选中开启，否则关闭</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="15%" align="center"><input type="checkbox" name="ipsecEnab"></TD>
					<TD class=column align="center" width="35%">IPSEC开关</TD>
					<TD class=column>IPSEC:选中开启，否则关闭</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="3" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckForm();">&nbsp;&nbsp;<input type="reset" value=" 重 置 " class="jianbian"></TD>
				</TR>
				<TR id="resultTr" bgcolor="#FFFFFF" style="display:none">
					<TD class=column align="right" width="15%">执行结果:</TD>
					<TD colspan="3">
						<DIV id="result"></DIV>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td colspan="4" valign="top" class=column>
						<div id="div_strategy"
							style="width: 100%; z-index: 1; top: 100px">
						</div>
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>

</BODY>


