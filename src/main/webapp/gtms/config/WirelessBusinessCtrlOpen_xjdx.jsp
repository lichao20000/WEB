<%--
Author      : 张四辈
Date		: 2014-9-18
Desc		: 无线的开通
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 String awifi_type = request.getParameter("awifi_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
var deviceId ;
var obj = {"deviceSn":6,"username":2,"kdname":1,"voipPhoneNum":4};
var userInfo;
var userInfoType;
$(function(){
	gwShare_setImport();
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function CheckForm(flag){
	if(!deviceId){
		alert("请选择设备！");
		disButton(false);
		return false;
	}
	if (deviceId.indexOf(",") != -1 && flag == 0) {
		alert("关闭时请选择一台设备！");
		disButton(false);
		return false;
	}
	var strategy_type  = $("select[@id='strategy_type']").val();
	var vlanIdMark = $("#vlanIdMark").val();
	var ssid = $("#ssid").val();
	if(strategy_type=="") {
		alert("请选择策略执行方式");
		disButton(false);
		return false;
	}
	if(vlanIdMark=="" && flag == 1)
	{
	 	alert("请填写VLANIDMARK！");
	 	disButton(false);
	 	return false;
	}
	if(ssid=="" && flag == 1)
	{
		alert("请填写ssid!");
		disButton(false);
		return false;
	}
	return true;
}

//查询出设备
function deviceResult(returnVal){
	$("#doButton").attr("disabled",false);
	deviceId="";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	
	if (returnVal[1] && returnVal[1].length > 0) {
		var res = returnVal[1].split("|");
		userInfoType = obj[res[1].replace(/[\r\n\t\ +]/g, "")];
		userInfo = res[3].replace(/[\r\n\t\ +]/g, "");
	}
	var totalNum = returnVal[0];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			//遍历出来的deviceId
			deviceId +=  deviceIdArray[i][0]+",";
		}
		var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		if(totalNum > 100){
			alert("设备数量超过100台，影响到交互性能");
			$("#doButton1").attr("disabled",true);
			$("#doButton1").attr("disabled",true);
			return;
		}
	$("div[@id='selectedDev']").html("<font size=2>共有"+totalNum+"台设备</font>");
	}
	else{
		//单个查询
		deviceId=  deviceId = 	returnVal[2][0][0] ;
		if(deviceId==""){
			$("#selectedDev").html("该用户不存在或未绑定终端！");
		}
	}
}

function doExecute(flag){	
	var strategy_type  = $("select[@id='strategy_type']").val();
	var vlanIdMark  = 	$("#vlanIdMark").val();
	var ssid = $("#ssid").val();
	var wireless_port  = $("select[@id='wireless_port']").val();
	var buss_level  = $("select[@id='buss_level']").val();

	disButton(true);
	if(CheckForm(flag)) {
		var url = "<s:url value='/gtms/config/wirelessBusnissCtrl!doConfigAll.action'/>";
		$.post(url,{
			strategy_type : strategy_type, 
            flag : flag, 	// 1 open 0 close
            ssid : ssid,
            vlanIdMark : vlanIdMark,
            wireless_port : wireless_port,
            buss_level : buss_level,
            deviceIds : deviceId,
            gw_type: <%=gwType%>,
            awifi_type :　<%=awifi_type%>,
            url: "http://192.168.18.69:58080/ItmsService/services/CallService",
            userInfo: userInfo,
            userInfoType: userInfoType,
            ssidNum: 3,
            type:2
         },function(ajax){
	         	$("#resultDIV").html("");
	         	disButton(false);
				if("1"==ajax){
					$("#resultDIV").append("后台执行成功");
				}else if ("-4"==ajax){
					$("#resultDIV").append("后台执行失败");
				}else{
					$("#resultDIV").append("后台执行异常");
				}
          });
	}
}

function disButton(flat){	
	$("#doButton1").attr("disabled", flat);
	$("#doButton2").attr("disabled", flat);
}
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									无线业务开通
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">

								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_wirelessOpen.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						无线业务开通
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post">
							<input type="hidden" name="deviceIds" value="" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="tr_strategybs"  >

												<TD align="right" width="10%">
													策略方式：
												</TD>
												<TD width="20%">
													<select id="strategy_type" name ="strategy_type">
														<option value="1">立即执行</option>
														<option value="2">终端上报执行</option>
														<option value="3">终端重启后执行</option>
													</select>
												</TD>
												<TD align="right" width="15%">
													VLANIDMARK
												</TD>
												<td align="left" width="20%">
													<input type="text" id="vlanIdMark" name="vlanIdMark">
												</td>
												<TD align="right" width="15%">
													SSID
												</TD>
												<td align="left" width="20%">
													<input type="text" id="ssid" name="ssid">
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="10%">
													无线开通端口：
												</TD>
												<TD width="20%">
													<select id="wireless_port" name ="wireless_port">
														<option value="3" selected="selected">SSID2</option>
														<!-- <option value="4">SSID4</option> -->
													</select>
												</TD>
												<TD align="right" width="10%">
													业务优先级：
												</TD>
												<TD width="20%">
													<select id="buss_level" name ="buss_level">
														<option value="0">0</option>
														<option value="7">7</option>
													</select>
												</TD>
												<TD colspan="2"></TD>
												<!-- <TD colspan="2" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute(1, 0)" value=" 开 通 业 务 " class=btn>
												</TD> -->
											</TR>
											<TR>
												<TD colspan="6" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="doButton1" name="doButton1" onclick="doExecute(1)" value=" 开 通 业 务 " class=btn>
													<INPUT TYPE="button" id="doButton2" name="doButton2" onclick="doExecute(0)" value=" 关 闭 业 务 " class=btn>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="left" class="green_foot">
														<div id="resultDIV"></div>
													</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>