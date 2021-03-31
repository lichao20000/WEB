<%--
Date		: 20200901
Desc		: 批量删除光猫业务信息数据
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 String countMax1 = request.getParameter("countMax");
 String upMax1 = request.getParameter("upMax");
 %> 
<html>
<head>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	$("#doButton").attr("disabled",true);
	gwShare_queryChange('3');
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function CheckForm(){
	/* if($("input[@name='deviceId']").val()==""){
		alert("请选择用户！");
		return false;
	} */
	return true;
}
var deviceId;
var netusername;
var city_id;
var loid;
var oui;
var device_serialnumber;
var faultListStr;
var userId;


//查询出用户
function deviceResult(returnVal){
	$("#doButton").attr("disabled",false);
	deviceId = "";
	netusername = "";
	city_id = "";
	loid = "";
	oui = "";
	device_serialnumber = "";
	userId = "";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	var totalNum = returnVal[0];
	faultListStr = returnVal[3];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[4];
		var deviceIdArray = returnVal[2];
		$("input[@name='resStr']").val(deviceIdArray);
		$("div[@id='selectedDev']").html("<font size=2>共有"+totalNum+"个用户</font>");
	}
	else{
		//单个查询
		deviceId= returnVal[2][0][3] ;
		netusername = returnVal[2][0][1] ;
		city_id = returnVal[2][0][2] ;
		loid = returnVal[2][0][0] ;
		oui = returnVal[2][0][4] ;
		device_serialnumber = returnVal[2][0][5] ;
		userId = returnVal[2][0][6] ;
		if(deviceId==""){
			$("#selectedDev").html("该用户不存在或未绑定终端！");
		}
	}
}
function doExecute(){
	var strategy_type  = $("select[@id='strategy_type']").val();
	var deviceList11  = $("input[@name='resStr']").val();
	var username  = $("select[@id='selectMap']").val();
	var url = "<s:url value="/gtms/config/delWanConnToolsAct!doConfigAll.action"/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("正在操作，请稍等....");
	
	if(CheckForm()){
		$.post(url,{
			resStr : deviceList11
         },function(ajax){
				if("1"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行成功");
				}else if ("-4"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行失败");
				}else{
					$("#resultDIV").html("");
					$("#resultDIV").append(ajax);
				}
				$("div[@id='QueryData']").append(ajax);
				$("div[@id='QueryData']").html("");
				$("button[@name='button']").attr("disabled", true);
          });
	}
} 

</script>
</head>
<%@ include file="../../toolbar.jsp"%>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td HEIGHT=20>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="200" align="center" class="title_bigwhite" nowrap>
									批量光猫业务参数删除业务
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">
									<span id="msg" style="color: red"></span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gtms/config/delWanConnQuery.jsp"%>
					</td>
				</tr>
				<tr>
					<th colspan="4" align="center">
						批量光猫业务参数删除业务
					</th>
				</tr>
				<tr>
					<td>
						<form name="frm" method="post" action="" onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="resStr" id="resStr" value="<s:property value="resStr"/>" />
							<table width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<tr>
									<td bgcolor=#999999>
										<table border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev">
														请查询用户！
													</div>
												</td>
											</tr>
											<tr>
												<td colspan="6" align="right" CLASS="green_foot">
												<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute()" value=" 执行" class=btn>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
													<td colspan="6" align="left" class="green_foot">
														<div id="resultDIV" />
													</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
								<td height="20">
								</td>
								</tr>
								<tr id="trData" style="display: none">
									<td class="colum">
										<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
											正在操作，请稍等....
										</div>
									</td>
								</tr>
								<tr>
									<td height="20">
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</td>
	</tr>
</table>
</body>
</html>
<%@ include file="../../foot.jsp"%>
