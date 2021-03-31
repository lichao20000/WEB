<%--
故障诊断-高级查询-UPNP
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';

$(function(){
	init();
});
//get data
function init() {
	
	parent.unblock();
	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("数据采集:");
	$("div[@id='operResult']").append("<s:property value='result'/>");
	if("<s:property value='result'/>" == "成功！"){
		$("select[@name='upnpParamValue'] option").each(function(){
			if($(this).val() == "<s:property value='upnpParamValue'/>"){
				$(this).attr("selected",true);
				return;
			}
		});
	}
	setTimeout("clearResult()", 3000);
}

//config.
function CheckPost() {
	var deviceId = $("input[@name='deviceId']");
	var upnpParamValue = $("select[@name='upnpParamValue']");
	parent.block();
	var url = "<s:url value='/gwms/diagnostics/upnpACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		upnpParamValue:upnpParamValue.val(),
		gw_type:gw_type
	},function(ajax){
		document.all("tr002").style.display = ""
		$("td[@id='td_save_result']").html(ajax);
		parent.dyniframesize();
        parent.unblock();
	});

	setTimeout("clearResult()", 3000);
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}
</SCRIPT>

<BODY>

<FORM NAME="frm" METHOD="post">
<TABLE style="width: 100%;" border=0 cellspacing=0 cellpadding=0>
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE style="width: 100%;" border=0 cellspacing=1 cellpadding=2>
				<TR align="center">
					<TH colspan="2">UPNP参数</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="center">UPNP</TD>
					<TD align="center">
						<SELECT NAME="upnpParamValue">
							<OPTION VALUE="0" SELECTED>关闭
							<OPTION VALUE="1">开启
						</SELECT>&nbsp;<font color="red">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="3" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<input id="button_save" type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckPost();"></TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td align="center">
					操作结果
					</td>
					<td align="center" id="td_save_result">
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</BODY>
