<%--
Author      : 张四辈
Date		: 2014-9-18
Desc		: 无线的开通或关闭
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setImport();
	$("input[@name='gwShare_queryResultType']").val("checkbox");
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
		return false;
	}
	if(vlanIdMark=="")
	{
	 	alert("请填写vlanIdMark！");
	 	return false;
	}
	if(ssid=="")
	{
		alert("请填写ssid!");
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
								<td width="162" align="center" class="title_bigwhite" nowrap>
									无线业务的开通活关闭
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
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						配置升级策略
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION=""
							onsubmit="return CheckForm()">
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
											<TR>
												<TD colspan="6" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="doButton1" name="doButton" onclick="doExecute(1)" value=" 开 通 业 务 " class=btn>
													<INPUT TYPE="button" id="doButton2" name="doButton" onclick="doExecute(0)" value=" 关 闭 业 务 " class=btn>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="left" class="green_foot">
														<div id="resultDIV" />
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
<script type="text/javascript">
var deviceId ;
//查询出设备
function deviceResult(returnVal){	
	$("#doButton1").attr("disabled",false);
	$("#doButton1").attr("disabled",false);
	
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
	
	var url = "<s:url value='/gtms/config/wirelessBusnissCtrl!doConfigAll.action'/>";
	$("#doButton1").attr("disabled",true);
	$("#doButton2").attr("disabled",true);
	if(CheckForm())
	{
		$.post(url,{
			  strategy_type : strategy_type, 
            flag : flag,
            ssid : ssid,
            vlanIdMark : vlanIdMark,
            strategy_type : strategy_type,
            deviceIds : deviceId,
            gw_type: <%=gwType%>
         },function(ajax){
	         	$("#resultDIV").html("");
	         	$("#doButton1").attr("disabled",false);
	        	$("#doButton2").attr("disabled",false);
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
<%@ include file="../../foot.jsp"%>
