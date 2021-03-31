<%--
故障诊断-高级查询-MWBAND。
Linkage Technology (NanJing) Co., Ltd
Copyright 2008-2012. All right reserved.
Author: Alex.Yan (yanhj@lianchuang.com)
Version: 1.0.0
Date: 2009-7-8
Desc: 多终端上网.
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='../../Js/inmp/CheckForm.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';

<!--
$(function(){
	init();
});
//get data
function init() {
	
	parent.unblock1();

	var mode = $("select[@name='mode']");
	var totalNumber = $("input[@name='totalNumber']");

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("数据采集:");
	$("div[@id='operResult']").append("<s:property value='result'/>");
	
	totalNumber.val("<s:property value='totalNumber'/>");
	$("select[@name='mode'] option").each(function(){
		if($(this).val() == "<s:property value='mode'/>"){
			$(this).attr("selected",true);
			return;
		}
	});

	if (mode.val() == "0")
	{
		totalNumber.val("0");
		totalNumber.attr("readonly", true);
	} else {
		totalNumber.attr("readonly", false);
	}

	setTimeout("clearResult()", 3000);
}

//change value
function chgmode()
{
	var mode = $("select[@name='mode']");
	var totalNumber = $("input[@name='totalNumber']");
	if (mode.val() == "0")
	{
		totalNumber.val("0");
		totalNumber.attr("readonly", true);
	} else {
		totalNumber.attr("readonly", false);
	}
}

//config.
function CheckPost() {
	var deviceId = $("input[@name='deviceId']");
	var mode = $("select[@name='mode']");
	var totalNumber = $("input[@name='totalNumber']");

	if (IsNumber2(totalNumber.val().trim(),"最大值") == false) {
		totalNumber.focus();
		return;
	} 
	parent.block1();
	var url = "<s:url value='/inmp/diagnostics/mwbandACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		mode:mode.val(),
		totalNumber:totalNumber.val().trim(),
		gw_type:gw_type
	},function(ajax){
		document.all("operResult").style.display = ""
		$("div[@id='operResult']").html("");
		var tmp = ajax.split("|");
		if(tmp[0] == '1'){
			$("div[@id='operResult']").html("通知后台:" + tmp[2]);
			document.getElementById("tr002").style.display = "";
			var url = "<s:url value='/inmp/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = tmp[1];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize1();
            });			
		}else{
			$("div[@id='operResult']").html("通知后台:" + tmp[1]);
		}
		parent.dyniframesize1();
        parent.unblock1();
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
					<TH colspan="1">模式</TH>
					<TH colspan="1">最大值</TH>
					<TH colspan="1">描述</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="15%" align="center">
						<SELECT NAME="mode" onchange="chgmode()">
							<OPTION VALUE="0" SELECTED>关闭
							<OPTION VALUE="1">开启
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD class=column align="center" width="35%"><INPUT TYPE="text" NAME="totalNumber"  value= "0" size="4" maxlength="4">&nbsp;<font color="red">*</font></TD>
					<TD class=column>关闭不限制，开启可以设置最大终端数</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="3" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<input type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckPost();">&nbsp;&nbsp;<input type="reset" value=" 重 置 " class="jianbian"></TD>
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

