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
$(function(){
	gwShare_setImport();
	$("input[@name='gwShare_queryResultType']").val("radio");
});
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
		return false;
	}
	return true;
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
								<%
									if(awifi_type.equals("1")){
								%>
										<td width="162" align="center" class="title_bigwhite" nowrap>
											awifi无线业务开通
										</td>
									<%}else{%>
										<td width="162" align="center" class="title_bigwhite" nowrap>
											校园网无线业务开通
										</td>
									<%}%>
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
						<%@ include file="/gwms/share/gwShareDeviceQuery_wirelessOpenSingle.jsp"%>
					</td>
				</TR>
				<TR>
				<%
					if(awifi_type.equals("1")){
				%>
					<TH colspan="4" align="center">
						awifi无线业务开通
					</TH>
				<%}else{%>
					<TH colspan="4" align="center">
						校园网无线业务开通
					</TH>
				<%}%>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION=""
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="do_type" value="" />
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
												<%if(awifi_type.equals("1")){%>
														<input type="text" id="vlanIdMark" name="vlanIdMark" value="32" readonly="readonly" disabled>
													<%}else{%>
														<input type="text" id="vlanIdMark" name="vlanIdMark" value="33" readonly="readonly" disabled>
													<%} %>
												</td>
												<TD align="right" width="15%">
													SSID
												</TD>
												<td align="left" width="20%">
													<%if(awifi_type.equals("1")){%>
														<input type="text" id="ssid" name="ssid" value="aWiFi" readonly="readonly" disabled/>
													<%}else{%>
														<input type="text" id="ssid" name="ssid" value="ChinaNet-EDU" readonly="readonly" disabled/>
													<%} %>
												</td>
											</tr>
											<TR>
												<TD align="right" width="10%" CLASS="green_foot">
													无线开通端口：
												</TD>
												<TD width="20%" CLASS="green_foot">
													<%if(awifi_type.equals("1")){%>
														<input id="wireless_port" name ="wireless_port" value="SSID4" readonly="readonly" disabled/>
													<%}else{%>
														<input id="wireless_port" name ="wireless_port" value="SSID3" readonly="readonly" disabled/>
													<%} %>
												</TD>
												<TD align="right" width="10%" CLASS="green_foot">
													业务优先级：
												</TD>
												<TD width="20%" CLASS="green_foot">
													<%if(awifi_type.equals("1")){%>
														<input id="buss_level" name ="buss_level" type="text" readonly="readonly" value="0"  disabled/>
													<%}else{%>
														<input id="buss_level" name ="buss_level" type="text" readonly="readonly" value="7" disabled/>
													<%} %>
												</TD>
												<TD colspan="2" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute(1,1)" value=" 开 通 业 务 " disabled class=btn>
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
<script type="text/javascript">
var deviceId ;
//查询出设备
function deviceResult(returnVal){	
	$("#doButton").attr("disabled",false);
	
	deviceId="";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	
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
			alert("设备数量超过1000台，影响到交互性能");
			$("#doButton1").attr("disabled",true);
			$("#doButton1").attr("disabled",true);
			return;
		}
		var tips = "";
		var isUserUrl = "<s:url value='/gtms/config/wirelessBusnissCtrl!isBindUser.action'/>";
		var isHaveUser;
		$.post(isUserUrl, {
	          deviceIds : deviceId
	       },function(ajax){
	    	 /*   isHaveUser = ajax;
	   		if(isHaveUser == 0){
	   			alert("设备未绑定用户");
	   			tips = "设备未绑定用户";
	   			$("#doButton").attr("disabled",true);
	   			return;
	   		} */
	   		$("div[@id='selectedDev']").html("<font size=2>共有"+totalNum+"台设备</font>" + "  <font color='red'>"+tips+"</font>");
	      });
	}
	else{
		//单个查询
		deviceId=  deviceId = 	returnVal[2][0][0] ;
		if(deviceId==""){
			$("#selectedDev").html("该用户不存在或未绑定终端！");
		}
	}
}

function doExecute(flag,do_type){
	//var do_type = "<s:property value = 'do_type' />";
	var strategy_type  = $("select[@id='strategy_type']").val();
	var vlanIdMark  = 	$("#vlanIdMark").val();
	var ssid = $("#ssid").val();
	var wireless_port  = $("input[@id='wireless_port']").val();
	if(wireless_port == "SSID3"){
		wireless_port = 3;
	}else{
		wireless_port = 4;
	}
	var buss_level  = $("input[@id='buss_level']").val();
	var url = "<s:url value='/gtms/config/wirelessBusnissCtrl!doConfig.action'/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("正在开通，请稍等....");
	if(CheckForm())
	{
		$.post(url,{
			strategy_type : strategy_type, 
            flag : flag,
            ssid : ssid,
            vlanIdMark : vlanIdMark,
            wireless_port : wireless_port,
            buss_level : buss_level,
            deviceIds : deviceId,
            gw_type: <%=gwType%>,
			do_type: do_type,
			awifi_type :　<%=awifi_type%>
         },function(ajax){
	         	/* $("#doButton").attr("disabled",false);
				if("1"==ajax){
					$("#resultDIV").append("后台执行成功");
				}else if ("-4"==ajax){
					$("#resultDIV").append("后台执行失败");
				}else{
					$("#resultDIV").append("后台执行异常");
				} */
				$("#doButton").attr("disabled",false);
				 $("div[@id='QueryData']").html("");
				 $("div[@id='QueryData']").append(ajax);
				$("button[@name='button']").attr("disabled", false);
          });
	}
	//alert(deviceId);
	
}
</script>
<%@ include file="../../foot.jsp"%>
