<%--
故障诊断-高级查询-ONU
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
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
	setTimeout("clearResult()", 3000);
}

//config.
function CheckPost() {	
	var deviceId = $("input[@name='deviceId']");
	var ntpserver1 = $("input[@name='ntpserver1']");
	var ntpserver2 = $("input[@name='ntpserver2']");
	var ntpserver3 = $("input[@name='ntpserver3']");
	var ntpserver4 = $("input[@name='ntpserver4']");
	var ntpserver5 = $("input[@name='ntpserver5']");
	var ntpinterval = $("input[@name='ntpinterval']");
	var ntpservertype = $("input[@name='ntpservertype']");
	
	if(!(/^\d+$/.test(ntpinterval.val()))){
		alert("同步间隔时间必须为正整数或0！");
		ntpinterval.focus();
		return;
	}
	
	if(ntpservertype.val()!="0"&&ntpservertype.val()!="1"&&ntpservertype.val()!="2"&&ntpservertype.val()!="3"){
		alert("时间同步方式必须为0、1、2、3中的一个！");
		ntpservertype.focus();
		return;
	}
	
	parent.block();
	var url = "<s:url value='/gwms/diagnostics/onuACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		ntpserver1:ntpserver1.val(),
		ntpserver2:ntpserver2.val(),
		ntpserver3:ntpserver3.val(),
		ntpserver4:ntpserver4.val(),
		ntpserver5:ntpserver5.val(),
		ntpinterval:ntpinterval.val(),
		ntpservertype:ntpservertype.val(),
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
					<TH colspan="4">设备时间同步参数</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">时间服务器1地址</TD>
					<TD align="left">
					<s:if test="ntpserver1=='undefined'||ntpserver1=='null'">
					<input type="text" name="ntpserver1"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver1" value="<s:property value='ntpserver1'/>"/>
					</s:else>						
					</TD>
					<TD align="right">时间服务器2地址</TD>
					<TD align="left">
					<s:if test="ntpserver2=='undefined'||ntpserver2=='null'">
					<input type="text" name="ntpserver2"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver2" value="<s:property value='ntpserver2'/>"/>
					</s:else>						
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">时间服务器3地址</TD>
					<TD align="left">
					<s:if test="ntpserver3=='undefined'||ntpserver3=='null'">
					<input type="text" name="ntpserver3"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver3" value="<s:property value='ntpserver3'/>"/>
					</s:else>						
					</TD>
					<TD align="right">时间服务器4地址</TD>
					<TD align="left">
					<s:if test="ntpserver4=='undefined'||ntpserver4=='null'">
					<input type="text" name="ntpserver4"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver4" value="<s:property value='ntpserver4'/>"/>
					</s:else>						
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">时间服务器5地址</TD>
					<TD align="left">
					<s:if test="ntpserver5=='undefined'||ntpserver5=='null'">
					<input type="text" name="ntpserver5"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver5" value="<s:property value='ntpserver5'/>"/>
					</s:else>						
					</TD>
					<TD align="right">同步间隔时间</TD>
					<TD align="left">
						<input type="text" name="ntpinterval" value="<s:property value='ntpinterval'/>"/>
						<img src="../../images/attention_2.gif" width="15" height="12" title="默认：86400（秒），如果为0表示不自动发起周期同步"/>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">时间同步方式</TD>
					<TD align="left" colspan="3">
						<input type="text" name="ntpservertype" value="<s:property value='ntpservertype'/>"/>
						方式：0,1,2,3；默认：0
						<img src="../../images/attention_2.gif" width="15" height="12" title="0：通过上网通道和时钟服务器同步；1：通过语音通道和时钟服务器同步；2：通过管理通道和时钟服务器同步；3：通过其他通道和时钟服务器同步。"/>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="4" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<input id="button_save" type="button" value=" 保 存 " class="jianbian" onclick="javascript:CheckPost();"></TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td align="center" colspan="2">
					操作结果
					</td>
					<td align="center" id="td_save_result" colspan="2">
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</BODY>
