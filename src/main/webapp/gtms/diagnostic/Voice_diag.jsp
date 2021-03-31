
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String deviceId = request.getParameter("deviceId");
	String gwType = request.getParameter("gw_type");
 %>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--//
//查询结果处理

var voipLine = "1";
var gwType = <%=gwType%>;
/**
 * 转换显示
 */
function changeType()
{
	var servTypeId = $("select[@name='queryServTypeId']").val();
	//alert(servTypeId);
	//话机连接性测试
	if("PhoneConnectivityTest" == servTypeId)
	{
		$("tr[@id='conntest']").css("display","");
		$("tr[@id='simtest']").css("display","none");
		$("td[@id='testName']").html("话机连接性测试");
	}else
	{
		//呼叫仿真测试
		$("tr[@id='conntest']").css("display","none");
		$("tr[@id='simtest']").css("display","");
		$("td[@id='testName']").html("呼叫仿真测试");
	}

	//激活按钮
	$("button[@name='connSubBtn']").attr("disabled", false);
	$("button[@name='simSubBtn']").attr("disabled", false);
	
	//激活按钮
	$("button[@name='connQueryBtn']").attr("disabled", false);
	$("button[@name='simQueryBtn']").attr("disabled", false);
	
	//定制结果消失
	$("tr[@id='connresultTR']").css("display","none");
	$("tr[@id='simresultTR']").css("display","none");
	
	//查询按钮消失
	$("tr[@id='connQueryBtnTR']").css("display","none");
	$("tr[@id='simQueryBtnTR']").css("display","none");
	
	//查询结果消失
	$("tr[@id='connQueryResultTR']").css("display","none");
	$("tr[@id='simQueryResultTR']").css("display","none");
	
	parent.dyniframesize();
}

//业务下发
function doBusiness(){
	
	var devId = $("input[@name='devId']").val();
	var testSelector = $("select[@name='queryServTypeId']").val();
	
	if('' == devId){
		alert("请先查询，并选择设备");
		return false;
	}

	//业务触发
	var url = "<s:url value='/gwms/config/voiceConnectionTest!setValue.action'/>";

	//灰化按钮
	//$("button[@name='connSubBtn']").attr("disabled", true);
	//$("button[@name='simSubBtn']").attr("disabled", true);

	voipLine = document.getElementById("line1").checked ? "1" : "2";
	
	//话机连接性测试
	if("PhoneConnectivityTest" == testSelector)
	{
		$("tr[@id='connresultTR']").css("display","");
		$("div[@id='connresult']").html("定制中...");
		//alert(voipLine);
		$.post(url,{
			deviceId:devId,
			testSelector:testSelector,
			voipLine:voipLine,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			$("div[@id='connresult']").html(ajax);
			
			//根据结果，判断是否继续查询
			//if("定制成功" == ajax)
			//{
				$("tr[@id='connQueryBtnTR']").css("display","");
			//}
			parent.dyniframesize();
		});
		
		
	}else
	{
		$("tr[@id='simresultTR']").css("display","");
		$("div[@id='simresult']").html("定制中...");
		//alert(voipLine);
		var calledNumber = $("input[@name='calledNumber']").val();
		var testType = document.getElementById("testType").options[document.getElementById("testType").selectedIndex].value;
		var dailDTMFConfirmEnable = document.getElementById("dailDTMFConfirmEnable").options[document.getElementById("dailDTMFConfirmEnable").selectedIndex].value;
		var dailDTMFConfirmNumber = $("input[@name='dailDTMFConfirmNumber']").val();
		$.post(url,{
			deviceId:devId,
			testSelector:testSelector,
			voipLine:voipLine,
			calledNumber:calledNumber,
			testType:testType,
			dailDTMFConfirmEnable:dailDTMFConfirmEnable,
			dailDTMFConfirmNumber:dailDTMFConfirmNumber,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			$("div[@id='simresult']").html(ajax);
			
			//根据结果，判断是否继续查询
			//if("定制成功" == ajax)
			//{
				$("tr[@id='simQueryBtnTR']").css("display","");
			//}
			parent.dyniframesize();
		});
	}
	parent.dyniframesize();
}

function doQuery()
{
	var devId = $("input[@name='devId']").val();
	var testSelector = $("select[@name='queryServTypeId']").val();
	voipLine = document.getElementById("line1").checked ? "1" : "2";
	//alert(devId);
	//alert(testSelector);
	if('' == devId){
		alert("请先查询，并选择设备");
		return false;
	}
	
	//业务触发
	var url = "<s:url value='/gwms/config/voiceConnectionTest!getValue.action'/>";

	//灰化按钮
	$("button[@name='connQueryBtn']").attr("disabled", true);
	$("button[@name='simQueryBtn']").attr("disabled", true);
	
	//话机连接性测试
	if("PhoneConnectivityTest" == testSelector)
	{

		$("tr[@id='connQueryResultTR']").css("display","");
		$("div[@id='connQueryResult']").html("定制中...");
		$.post(url,{
			deviceId:devId,
			voipLine:voipLine,
			testSelector:testSelector,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			//查询结果为"连接","未连接"
			//0 在页面换成 话机不在线，1在页面换成 话机连接正常
			if(ajax=="0"){
				ajax="话机不在线";
			}
			if(ajax=="1"){
				ajax="话机连接正常";
			}
			
			$("div[@id='connQueryResult']").html(ajax);
		});
	}else
	{

		$("tr[@id='simQueryResultTR']").css("display","");
		$("div[@id='simQueryResult']").html("定制中...");
		$.post(url,{
			deviceId:devId,
			testSelector:testSelector,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			var res = ajax.split("###");
			if("获取失败" != res[0])
			{
				//测试状态 肯定是Complete
				//测试结论 Success Fail 
				//媒体流模拟按键确认结果 true false
				//错误原因 CallerFailReason
				var resHtml = "测试状态:"+res[0];
				resHtml = "," + resHtml + "测试结论" + res[1];
				resHtml = "," + resHtml + "媒体流模拟按键确认结果" + res[2];
				if("" != res[3])
				{
					resHtml = "," + resHtml + "错误原因" + res[3];
				}
				$("div[@id='simQueryResult']").html(resHtml);
			}else
			{
				$("div[@id='simQueryResult']").html(res[0]);
			}
		});
	}
	parent.dyniframesize();
}
//-->
</script>
<FORM NAME="frm" METHOD="post" action="">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TH colspan="4">定制语音故障诊断</TH>
			</TR>
			<TR>
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: none">
						<TD width="15%" class=column align="right">终端:</TD>
						<TD width="35%" id="tdDevice">请查询并选择设备</TD>
						<TD class=column align="right" nowrap width="15%">属地:</TD>
						<TD width="35%" id="tdDeviceCityName"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">模板选择:</TD>
						<TD class=column width="35%"><SELECT
							name="queryServTypeId" class="bk" onchange="changeType()">
							<OPTION value="PhoneConnectivityTest">话机连接性测试</OPTION>
							<option value="X_CT-COM_SimulateTest">呼叫仿真测试</option>
						</SELECT> <input type="hidden" name="devId" value="<%=deviceId %>"></TD>
						<td nowrap class=column align="right" width="15%">语音端口:</td>
						<td class=column>
							<input type="radio" name="type" id="line1" class=btn value="1" checked>语音1&nbsp;
							<input type="radio" name="type" id="line2" class=btn value="2">语音2
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>

			<tr>
				<TH colspan="4"><label id="testName">话机连接性测试</label></TH>
			</tr>
			<tr id="conntest" style="display: ">
				<TD colspan="4" bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr bgcolor="#FFFFFF">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="connSubBtn" onclick="doBusiness()">诊断</button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="connresultTR" style="display: none">
						<TD class=column width="15%" align="right">诊断结果:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="connresult"></DIV>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="connQueryBtnTR" style="display: none">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="connQueryBtn" onclick="doQuery()">
						查 询 状 态 </button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="connQueryResultTR" style="display: none">
						<TD class=column width="15%" align="right">测试结果:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="connQueryResult"></DIV>
						</TD>
					</tr>
				</table>
				</TD>
			</tr>
			<tr id="simtest" style="display: none">
				<TD colspan="4" bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr bgcolor="#FFFFFF">
						<td nowrap class=column align="right" width="15%">仿真测试类型:</td>
						<td class=column width="35%"><SELECT name="testType"
							id="testType" class="bk"">
							<OPTION value="Caller" selected>主叫仿真</OPTION>
							<option value="Called">被叫仿真</option>
							<option value="None">取消仿真</option>
						</SELECT></td>
						<td nowrap class=column align="right" width="15%">按键确认:</td>
						<td class=column width="35%"><SELECT
							name="dailDTMFConfirmEnable" id="dailDTMFConfirmEnable"
							class="bk"">
							<OPTION value="1" selected>开启</OPTION>
							<option value="0">关闭</option>
						</SELECT></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap class=column align="right" width="15%">被叫号码:</td>
						<td class=column width="35%"><input type="text" class="bk"
							name="calledNumber" id="calledNumber" value="10000"></td>
						<td nowrap class=column align="right" width="15%">流媒体模拟按键:</td>
						<td class=column><input type="text" class="bk"
							name="dailDTMFConfirmNumber" id="dailDTMFConfirmNumber">
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="simSubBtn" onclick="doBusiness()">诊断</button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="simresultTR" style="display:none">
						<TD class=column width="15%" align="right">诊断结果:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="simresult"></DIV>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="simQueryBtnTR" style="display:none">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="simQueryBtn" onclick="doQuery()">
						查 询 状 态 </button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="simQueryResultTR" style="display:none">
						<TD class=column width="15%" align="right">测试结果:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="simQueryResult"></DIV>
						</TD>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD colspan="4" bgcolor="#FFFFFF">备注：语音模板定制测试为实时测试，终端必须在线。</TD>
			</TR>
			<tr>
				<td height="15"></td>
			</tr>
			<TR>
				<TD colspan="4" bgcolor="#999999" id="ppp_type_2"
					style="display: none">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR id="resultTR1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="50%" id="resultTD1">测试结果</TD>
						<TD width="50%" id="resultTD2">
						<DIV id="result2"></DIV>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>