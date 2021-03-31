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
<title>终端组播下移</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<%
	String gwType = request.getParameter("gw_type");
%>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_queryChange('3');
	$("input[@name='gwShare_jiadan']").css("display","none");
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
$(function(){
	
});
var gw_type = "<%= gwType%>";
//查询结果处理
var deviceId="";
var sn="";
var loid="";
var userid="";
function deviceResult(returnVal){
	$("button[@name='subBtn']").attr("disabled", false);
	deviceId = "";
	sn="";
	loid="";
	userid="";
	$("#result1").html("");
	var totalNum = returnVal[0];
	if(returnVal[0] == 0){
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0;i < deviceIdArray.length;i++){
			//遍历出来的deviceId
			deviceId +=  deviceIdArray[i][0]+",";
			sn +=  deviceIdArray[i][2]+",";
			loid +=  deviceIdArray[i][13]+",";
			userid += deviceIdArray[i][14]+",";
		}
		var endIndex1 = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex1);
		
		endIndex1 = sn.lastIndexOf(",");
		sn = sn.substring(0,endIndex1);
		
		endIndex1 = loid.lastIndexOf(",");
		loid = loid.substring(0,endIndex1);
		
		endIndex1 = userid.lastIndexOf(",");
		userid = userid.substring(0,endIndex1);
		
		if(totalNum > 5000){
			alert("设备数量超过5000台，影响到交互性能");
			$("button[@name='subBtn']").attr("disabled", true);
			return;
		}
		// 判断未作策略的数量
		var url = "<s:url value='/gwms/config/serviceManSheet!queryMulticastNum.action'/>"; 
		$.post(url,{} ,function(ajax){
	          if("goon"!=ajax)
	           {
	           		alert(ajax);
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
	}
}

//业务下发
function doBusiness(){
	if('' == deviceId){
		alert("请先查询，并选择设备");
		return false;
	}
	//业务触发
	var url = "<s:url value='/gwms/config/serviceManSheet!setMulticastBatch.action'/>";
	$("button[@name='subBtn']").attr("disabled", true);
	$.post(url,{
		deviceId:deviceId,
		sn:sn,
		loid:loid,
		userId:userid,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='result1']").html("通知后台 : ");
		$("button[@name='subBtn']").attr("disabled", false);
		$("div[@id='result1']").append(ajax);
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
						<div align="center" class="title_bigwhite">批量组播下移</div>
						</td>
					</tr>
				</table>
				<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</TD>
			</TR>
			<TR id="bisShowName">
				<TH colspan="4">批量组播下移</TH>
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
					
					<TR align="left" id="doBiz" STYLE="display:">
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" id="subBtn" onclick="doBusiness()">开始组播下移</button>
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
						<TD colspan="4"><!-- 备注：设备如果不在线，系统会在设备上线后第一时间进行业务下发。 --></TD>
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