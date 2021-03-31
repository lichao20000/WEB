<%--
终端业务下发
Author: Jason
Version: 1.0.0
Date: 2010-06-11
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<HEAD>
<title>终端业务下发</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<%
	String gwType = request.getParameter("gw_type");
	String telecom = LipossGlobals.getLipossProperty("telecom");
%>
<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%= gwType%>";


//查询结果处理
function deviceResult(returnVal){

	$("input[@name='devId']").val(returnVal[2][0][0]);
	
	$("td[@id='tdDevice']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	$("td[@id='tdDevice']").append(returnVal[2][0][1] + '-' + returnVal[2][0][2]);
	$("td[@id='tdDeviceCityName']").append(returnVal[2][0][5]);
	
	$("button[@name='subBtn']").attr("disabled", false);
}

//业务下发
function doBusiness(){
	
	var devId = $("input[@name='devId']").val();
	var servTypeId = $("select[@name='queryServTypeId']").val();
	
	if('' == devId){
		alert("请先查询，并选择设备");
		return false;
	}

	//业务触发
	var url = "<s:url value='/gwms/config/serviceManSheet!servDone.action'/>";

	$.post(url,{
		deviceId:devId,
		servTypeId:servTypeId,
		gw_type:gw_type
	},function(ajax){
		//alert(ajax);
		$("div[@id='result1']").html("通知后台");
		if (ajax == "1")
		{
			$("div[@id='result1']").append("<FONT COLOR='green'>成功</FONT>");
		} else {
			$("div[@id='result1']").append("<FONT COLOR='red'>失败:");
			if(ajax == "-5"){
				$("div[@id='result1']").append(" (调用后台失败)");
			} else {
				$("div[@id='result1']").append(" (无对应用户或用户未开通该业务)");
			}
			$("div[@id='result1']").append("</FONT>");
		}
		$("tr[@id='resultTR1']").show();
	});
	//灰化按钮
	$("button[@name='subBtn']").attr("disabled", true);
}

</script>
<%@ include file="../../toolbar.jsp"%>
<FORM NAME="frm" METHOD="post" action="">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD colspan="4">
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">业务下发</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"> 触发BSS业务下发到终端 </td>
					</tr>
				</table>
				<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</TD>
			</TR>
			<TR id="bisShowName">
				<TH colspan="4">业务下发</TH>
			</TR>
			<TR id="bisShowContent">
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: ">
						<TD width="15%" class=column align="right">终端:</TD>
						<TD width="35%" id="tdDevice">请查询并选择设备</TD>
						<TD class=column align="right" nowrap width="15%">属地:</TD>
						<TD width="35%" id="tdDeviceCityName"></TD>
					</TR>
					
					<TR id="bizshow" bgcolor="#FFFFFF" STYLE="display:">
						<TD class=column align="right" nowrap width="15%">业务类型:</TD>
						<TD width="35%" >
							<SELECT name="queryServTypeId" class="bk">
							<OPTION value="0">所有业务</OPTION>
							<OPTION value="10">上网业务</OPTION>
							<OPTION value="11">IPTV业务</OPTION>
							<OPTION value="14">VOIP业务</OPTION>
							<ms:inArea areaCode="js_dx" notInMode="false">
								<OPTION value="16">路由宽带</OPTION>
								<OPTION value="25">天翼看店</OPTION>
							</ms:inArea>
							<ms:inArea areaCode="xj_dx" notInMode="false">
								<OPTION value="15">智能音箱</OPTION>
								<OPTION value="38">VPN业务</OPTION>
								<OPTION value="51">自适应wifi</OPTION>
							</ms:inArea>
							<%if(telecom.equals("CTC")){%>
									<option value="47">云网超宽带业务</option>
							<%}%>
							</SELECT>
						</TD>
						<TD width="15%" class=column align="right">操作类型:</TD>
						<TD width="35%" id="operationTypeTd">开户</TD>
					</TR>
					<TR align="left" id="doBiz" STYLE="display:">
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doBusiness()">业务下发</button>
							<input type="hidden" name="devId" value="">
						</TD>
					</TR>
					<TR id="resultTR1" bgcolor="#FFFFFF" style="display: none">
						<TD class=column align="right" width="15%" id="resultTD1">执行结果</TD>
						<TD colspan="3">
						<DIV id="result1"></DIV>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4">备注：设备如果不在线，系统会在设备上线后第一时间进行业务下发。</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
<%@ include file="../../foot.jsp"%>