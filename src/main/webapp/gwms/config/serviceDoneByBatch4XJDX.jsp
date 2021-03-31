<%--
终端业务批量下发
Version: 1.0.0
Date: 2015-08-05
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<HEAD>
<title>终端业务批量下发</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<%
	String gwType = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	//gwShare_queryChange('3');
	//$("input[@name='gwShare_jiadan']").css("display","none");
	//$("input[@name='gwShare_queryResultType']").val("checkbox");
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
$(function(){
	/*var starttime = "20:00:00";
	var endtime = "23:00:00";
	starttime = parseInt(starttime.split(":")[0]) * 3600 + parseInt(starttime.split(":")[1]) * 60 + parseInt(starttime.split(":")[2]);
	endtime = parseInt(endtime.split(":")[0]) * 3600 + parseInt(endtime.split(":")[1]) * 60 + parseInt(endtime.split(":")[2]);
	var currenttime = parseInt(new Date().toLocaleTimeString().split(":")[0]) * 3600 + parseInt(new Date().toLocaleTimeString().split(":")[1]) * 60 + parseInt(new Date().toLocaleTimeString().split(":")[2]);
	if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#queryServTypeId,#subBtn,#gwShare_queryButton,#gwShare_reButto").attr("disabled",true);
			$("#msg").html("此功能仅在20:00-23:00使用");
			$("#msg").css({color:"red"}); 
			$("#gwShare_tr31").hide();
		}*/
});
var gw_type = "<%= gwType%>";
//查询结果处理
var deviceIds;
var has_showModalDialog = !!window.showModalDialog;
function deviceResult(returnVal){
	/*$("button[@name='subBtn']").attr("disabled", false);
	deviceId = "";
	$("#result1").html("");
	var totalNum = returnVal[0];
	if(returnVal[0] == 0){
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0;i < deviceIdArray.length;i++){
			//遍历出来的deviceId
			deviceId +=  deviceIdArray[i][0]+",";
		}
		var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		if(totalNum > 1000){
			alert("设备数量超过1000台，影响到交互性能");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// 判断未作策略的数量
		var url = "<s:url value='/gwms/config/serviceManSheet!queryUndoNum.action'/>"; 
		var maxNum = 30000;
		$.post(url,{} ,function(ajax){
	          var num = parseInt(ajax);
	          if(num > maxNum)
	           {
	           		alert("今天配置数已达到上限，请明日再配置！");
	           		$("#subBtn").attr("disabled",true);
		            return;
	           }
	    });
		$("div[@id='selectedDev']").html("<font size=2>共有"+totalNum+"台设备</font>");
	}else{ //单个查询
		deviceId = returnVal[2][0][0];
		if(deviceId == ""){
			$("#selectedDev").html("该用户不存在或未绑定终端！");
		}
	}*/
	deviceIds="";
	if(returnVal[0]==0){
		if(returnVal[2].length > 1000){
			alert("设备数量超过1000台，影响到交互性能");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// 判断未作策略的数量
		/*	var url = "<s:url value='/gwms/config/serviceManSheet!queryUndoNum.action'/>";
		var maxNum = 30000;
		$.post(url,{} ,function(ajax){
			var num = parseInt(ajax);
			if(num > maxNum)
			{
				alert("今天配置数已达到上限，请明日再配置！");
				$("#subBtn").attr("disabled",true);
				return;
			}
		});*/
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		if(returnVal[0] > 1000){
			alert("设备数量超过1000台，影响到交互性能");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// 判断未作策略的数量
		/*var url = "<s:url value='/gwms/config/serviceManSheet!queryUndoNum.action'/>";
		var maxNum = 30000;
		$.post(url,{} ,function(ajax){
			var num = parseInt(ajax);
			if(num > maxNum)
			{
				alert("今天配置数已达到上限，请明日再配置！");
				$("#subBtn").attr("disabled",true);
				return;
			}
		});*/
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
}

//业务下发
function doBusiness(){
	var batchsql = $("input[@name='deviceIds']").val();
	deviceIds=$("input[@name='deviceIds']").val();
	var servTypeId = $("select[@name='queryServTypeId']").val();
	if('' == deviceIds){
		alert("请先查询，并选择设备");
		return false;
	}
	//业务触发
	var url = "<s:url value='/gwms/config/serviceManSheet!serviceDoneByBatchXJDX.action'/>";
	$("button[@name='subBtn']").attr("disabled", true);
	$.post(url,{
		deviceId:deviceIds,
		servTypeId:servTypeId,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='result1']").html("通知后台 : ");
		$("button[@name='subBtn']").attr("disabled", false);
		if (ajax == "1"){
			$("div[@id='result1']").append("<FONT COLOR='blue'>成功!</FONT>");
		} else {
			$("div[@id='result1']").append("<FONT COLOR='red'>失败!</FONT>");
		}
		$("tr[@id='resultTR1']").show();
		//灰化按钮
		$("button[@name='subBtn']").attr("disabled", true);
	});
	
} 

</script>
</HEAD>
<body>
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
						<div align="center" class="title_bigwhite">批量业务下发</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"><span id="msg">触发BSS业务批量下发到终端 </span></td>
					</tr>
				</table>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<td colspan="4">
					<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</TR>
			<TR id="bisShowName">
				<TH colspan="4">批量业务下发</TH>
			</TR>
			<TR id="bisShowContent">
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr bgcolor="#FFFFFF">
						<td colspan="6">
							<div id="selectedDev">
								请查询设备！
							</div>
						</td>
					</tr>
					<TR id="bizshow" bgcolor="#FFFFFF" STYLE="display:">
						<TD class=column align="right" nowrap width="15%">业务类型:</TD>
						<TD width="35%" >
							<SELECT name="queryServTypeId" id="queryServTypeId" class="bk">
								<OPTION value="0" selected = "selected" >全业务</OPTION>
								<OPTION value="10" >宽带业务</OPTION>
								<OPTION value="11">itv业务</OPTION>
								<OPTION value="14">VOIP业务</OPTION>
								<ms:inArea areaCode="xj_dx" notInMode="false">
									<OPTION value="15">智能音箱</OPTION>
									<OPTION value="20">无线共享WIFI</OPTION>
									<OPTION value="38">VPN业务</OPTION>
									<OPTION value="32">溯源</OPTION>
									<OPTION value="51">wifi配置</OPTION>
								</ms:inArea>
							</SELECT>
						</TD>
						<TD width="15%" class=column align="right">操作类型:</TD>
						<TD width="35%" id="operationTypeTd">开户</TD>
					</TR>
					<TR align="left" id="doBiz" STYLE="display:">
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" id="subBtn" onclick="doBusiness()">业务下发</button>
							<input type="hidden" name="deviceIds" value=""/>
							<input type="hidden" name="param" value=""/>
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