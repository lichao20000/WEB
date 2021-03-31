<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 String do_type = request.getParameter("do_type");
 request.setAttribute("CurrencyType","1");
 %> 
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	gwShare_queryChange('3');
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	
});
function downloadCase() {
	var url = "<s:url value='/itms/resource/downloadCurrencyCase!download.action'/>";
		document.getElementById("frm").action = url;
		document.getElementById("frm").submit();
		document.getElementById("frm").reset();
	}
function CheckForm(){
	if($("input[@name='deviceId']").val()==""){
		alert("请选择设备！");
		return false;
	}
	var strategy_type  = $("select[@id='strategy_type']").val();
	var vlanIdMark  = 	$("#VLANIDMARK").val();
	var ssid = $("#ssid").val();
	if(strategy_type=="")
	{
		alert("请选择策略执行方式");
		$("#doButton").attr("disabled",false);
		return false;
	}
	if(vlanIdMark=="")
	{
	 	alert("请填写vlanIdMark！");
	 	$("#doButton").attr("disabled",false);
	 	return false;
	}
	if(ssid=="")
	{
		alert("请填写ssid!");
		$("#doButton").attr("disabled",false);
		$("tr[@id='trData']").hide();
		$("div[@id='QueryData']").html("");
		return false;
	}
	return true;
}

var deviceId ;
var ssidArr;
var vlanIdArr;
var priorityArr;
var channelArr;
var wlanportArr;   
//查询出设备
function deviceResult(returnVal){	
	$("#doButton").attr("disabled",false);
	deviceId="";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	
	var totalNum = returnVal[0];
	if(returnVal[0]==0){
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		ssidArr="";
		vlanIdArr="";
		priorityArr="";
		channelArr="";
		wlanportArr="";
		for(var i=0 ;i<deviceIdArray.length;i++){
			//遍历出来的deviceId
			deviceId +=  deviceIdArray[i][0]+",";
			ssidArr += $.trim(deviceIdArray[i][6])+",";
			vlanIdArr += $.trim(deviceIdArray[i][7])+",";
			priorityArr += $.trim(deviceIdArray[i][8])+",";
			channelArr += $.trim(deviceIdArray[i][9])+",";
			wlanportArr += $.trim(deviceIdArray[i][10])+",";
		}
		var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		endIndex = ssidArr.lastIndexOf(",");
		ssidArr = ssidArr.substring(0,endIndex);
		endIndex = vlanIdArr.lastIndexOf(",");
		vlanIdArr = vlanIdArr.substring(0,endIndex);
		endIndex = priorityArr.lastIndexOf(",");
		priorityArr = priorityArr.substring(0,endIndex);
		endIndex = channelArr.lastIndexOf(",");
		channelArr = channelArr.substring(0,endIndex);
		endIndex = wlanportArr.lastIndexOf(","); 
		wlanportArr = wlanportArr.substring(0,endIndex);   
		if(totalNum > 2000){
			alert("设备数量超过2000台，影响到交互性能");
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
$(function(){
	var starttime = "22:00:00";
	var endtime = "23:59:59";
	starttime = parseInt(starttime.split(":")[0]) * 3600 + parseInt(starttime.split(":")[1]) * 60 + parseInt(starttime.split(":")[2]);
	endtime = parseInt(endtime.split(":")[0]) * 3600 + parseInt(endtime.split(":")[1]) * 60 + parseInt(endtime.split(":")[2]);
	var currenttime = parseInt(new Date().toLocaleTimeString().split(":")[0]) * 3600 + parseInt(new Date().toLocaleTimeString().split(":")[1]) * 60 + parseInt(new Date().toLocaleTimeString().split(":")[2]);
	if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#doButton,#query,#reset,#strategy_type").attr("disabled",true);
			$("#msg").html("此功能仅在22:00-23:59使用");
			$("#gwShare_tr31").hide();
		}
});
function doExecute(do_type){
	var strategy_type = $.trim($("select[@id='strategy_type']").val());
	var vlanIdMark = null;
	var ssid = null;
	var wireless_port = null;
	var channel  = null;
	var buss_level  = null;
	var awifi_type ='<s:property value="awifi_type" />';
	var url = "<s:url value='/gtms/config/wirelessBusnissCtrl!doConfigAll4Special.action'/>";
		ssid = ssidArr;
		vlanIdMark = vlanIdArr;
		channel = channelArr;
		buss_level = priorityArr;
		//wireless_port = 3;
		wireless_port = wlanportArr;      

		if(do_type == 1){
			$("tr[@id='trData']").show();
			$("div[@id='QueryData']").html("正在开通，请稍等....");
			$("#doButton").attr("disabled",true);
		}else{
			$("div[@id='QueryData']").html("正在刷新，请稍等....");
			$("#doButton").attr("disabled",true);
		}
		
		
	if(CheckForm()){
		$.post(url,{
			strategy_type : strategy_type, 
            ssid : ssid,
            vlanIdMark : vlanIdMark,
            wireless_port : wireless_port,
            buss_level : buss_level,
            deviceIds : deviceId,
            gw_type: <%=gwType%>,
            do_type: do_type,
			channel : channel,
			awifi_type : 1
         },function(ajax){
					$("div[@id='QueryData']").html("");
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
</script>

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
								<td id="title1"  width="162" align="center" class="title_bigwhite" nowrap >
									批量无线业务开通（通用）
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
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_common.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						批量无线业务开通（通用）
					</TH>
				</TR>
				<tr >
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION=""
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="do_type" value="" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD id="main1" bgcolor=#999999>
										<TABLE  border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
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
												<TD align="right" width="14%">
													<div id="download" align="left">
														<input type="radio" width="22%" name="caseDownload" value="0" onclick="downloadCase()">xls案例下载 </input>
														<input type="radio" width="22%" name="caseDownload" value="1" onclick="downloadCase()">txt案例下载 </input>
													</div>
												</TD>
											
													<TD colspan="2" align="right" CLASS="green_foot">
														<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute(1)" value=" 执行 " disabled="disabled" class=btn>
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
								<tr>
								<td height="20">
								</td>
								</tr>
								<tr id="trData" style="display: none">
									<td class="colum">
										<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
											正在开通，请稍等....
										</div>
									</td>
								</tr>
								<tr>
									<td height="20">
									</td>
								</tr>
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
