<%--
Author      : 宋俊景
Date		: 2015-05-25
Desc		: 宽带DHCP的关闭
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 String gwType = request.getParameter("gw_type");
 String do_type = request.getParameter("do_type");
 %> 
<html>
<head>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	$("#doButton").attr("disabled",true);
	do_type = <%=do_type%>
	if(do_type == 1){
		$("input[@name='gwShare_import']").css("display","none");
		$("input[@name='gwShare_queryResultType']").val("radio");
	}else{
		gwShare_queryChange('3');
		$("input[@name='gwShare_queryResultType']").val("checkbox");
	}
});
function CheckForm(){
	if($("input[@name='deviceId']").val()==""){
		alert("请选择设备！");
		return false;
	}
	return true;
}
var deviceId;
//查询出设备
function deviceResult(returnVal){	
	$("#doButton").attr("disabled",false);
	deviceId = "";
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
		/*var url = "<s:url value='/gtms/config/netByDHCPStop!getUsername.action'/>";
			$.post(url,{
	          deviceIds : deviceId
	       },function(ajax){
	    	   tips = ajax;
	    		$("div[@id='selectedDev']").html("<font size=2>共有"+totalNum+"台设备</font>");
	    		var selectMap= document.getElementById("selectMap"); 
	    		$("#selectMap option").remove();
	    		var usernames = ajax.split(",");
	    		for(var i = 0;i < usernames.length;i++){
	    			if(usernames[i] != ""){
	    				selectMap.options.add(new Option(usernames[i],usernames[i]));
	    			}
	    		} 
	       }); */
			$("div[@id='selectedDev']").html("<font size=2>共有"+totalNum+"台设备</font>");
	}
	else{
		//单个查询
		deviceId= returnVal[2][0][0] ;
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
	do_type = <%=do_type%>;
	if(do_type == 2){
		if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#doButton,#query,#reset,#strategy_type").attr("disabled",true);
			$("#msg").html("此功能仅在22:00-23:59使用");
			$("#gwShare_tr31").hide();
		}
	}
});
function doExecute(){
	var strategy_type  = $("select[@id='strategy_type']").val();
	var username  = $("select[@id='selectMap']").val();
	var url = "<s:url value='/gtms/config/netByDHCPStop!doConfigAll.action'/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("正在操作，请稍等....");
	if(CheckForm()){
		$.post(url,{
            strategy_type : strategy_type,
            deviceIds : deviceId,
            username:username,
            gw_type: <%=gwType%>
         },function(ajax){
        	  $("#doButton").attr("disabled",false);
				if("1"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行成功");
				}else if ("-4"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行失败");
				}else{
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行异常");
				}
				$("#doButton").attr("disabled",false);
				$("div[@id='QueryData']").append(ajax);
				$("div[@id='QueryData']").html("");
				$("button[@name='button']").attr("disabled", false);
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
								<td width="162" align="center" class="title_bigwhite" nowrap>
									<%if("1".equals(do_type)){%>
										关闭宽带DHCP
									<%}else{%>
										批量关闭宽带DHCP
									<%}%>
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
						<%@ include file="/gwms/share/gwShareDeviceQuery_DHCPStop.jsp"%>
					</td>
				</tr>
				<tr>
					<th colspan="4" align="center">
						<%if("1".equals(do_type)){%>
							宽带DHCP关闭
						<%}else{ %>
							关闭宽带DHCP策略
						<%} %>
					</th>
				</tr>
				<tr>
					<td>
						<form name="frm" method="post" action="" onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<table width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<tr>
									<td bgcolor=#999999>
										<table border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="tr_strategybs"  >

												<td align="right" width="10%">
													策略方式：
												</td>
												<td width="20%">
													<select id="strategy_type" name ="strategy_type">
														<option value="1">立即执行</option>
														<option value="2">终端上报执行</option>
														<option value="3">终端重启后执行</option>
													</select>
												</td>
												<%-- <%if("1".equals(do_type)){%>
													<td align="right" width="10%">
													宽带帐号：
												</td>
												<td width="20%">
													<select id="selectMap">
														<option>--请选择设备--</option>
													</select>
												</td>
												<% }else{%>
												<%} %> --%>
											</tr>
											<tr>
												<td colspan="4" align="right" CLASS="green_foot">
												<%if("1".equals(do_type)){%>
													<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute()" value=" 关 闭 业 务 " class=btn>
												<%}else{ %>
													<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute()" value=" 执行" class=btn>
												<%} %>
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
