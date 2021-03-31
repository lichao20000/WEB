<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="/Js/jquery.js"></script>
<script type="text/javascript" src="/Js/CheckFormForm.js"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS资料信息销户</title>
<script type="text/javascript">
/* var servTypeId = '<s:property value="servTypeId" />'; */
var gwType = 1;
function change(obj){
	var value = obj.value;
	if(value==1){
		gwType = 1;
		$("#username").removeAttr("disabled");
		$("#username").val("");
	}else if(value==2){
		gwType = 2;
		$("#username").removeAttr("disabled");
		$("#username").val("");
	}else{
		$("#username").val("请先选择用户类型!");
		$("#username").attr("disabled","disabled");	
	}
}

//检查LOID和业务帐号
function checkUserInfo(){
	var username = $.trim($("input[@name='username']").val());
	var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
	$.post(url,{
		gw_type : gwType,
		username : username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1"){
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
		}
		else{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}

//发送注销工单
function sendSheet(){
	var servTypeId = $.trim($("input[@name='servTypeId']").val());
	var	operateType	= $.trim($("input[@name='operateType']").val());
	var dealdate = $.trim($("input[@name='dealdate']").val());
	var userType = $.trim($("select[@name='userType']").val());
	var username = $.trim($("input[@name='username']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var voipTelepone = $.trim($("input[@name='voipTelepone']").val());
	//受理时间
	if(dealdate == ""){
		$("input[@name='dealdate']").focus();
		return false;
	}
	//设备类型
	if(userType == -1){
		alert("请选择设备类型");
		$("select[@name='userType']").focus();
		return false;
	}
	//用户账号
	if(username == ""){
		alert("请输入用户账号");
		$("input[@name='username']").focus();
		return false;
	}
	//属地
	if(cityId  == ""){
		alert("请选择属地");
		$("select[@name='cityId']").focus();
		return false;
	}
	if(servTypeId == 15){
		//业务电话号码
		if(voipTelepone  == ""){
			alert("请输入业务电话号码");
			$("input[@name='voipTelepone']").focus();
			return false;
		}
	}
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在发送，请稍等....");
    $("button[@name='e8cStopButton']").attr("disabled", true);
    var url = "<s:url value='/itms/service/simulateHLJSheet!sendSheet.action'/>";
    if(servTypeId == 15){
    	$.post(url, {
    		servTypeId:servTypeId,
    		operateType:operateType,
    		dealdate:dealdate,
    		userType:userType,
    		username:username,
    		cityId:cityId,
    		voipTelepone:voipTelepone
    	}, function(ajax) {
    		$("div[@id='QueryData']").html("");
    		$("div[@id='QueryData']").append(ajax);
    		$("button[@name='e8cStopButton']").attr("disabled", false);
    	});
    }else{
    	$.post(url, {
    		servTypeId:servTypeId,
    		operateType:operateType,
    		dealdate:dealdate,
    		userType:userType,
    		username:username,
    		cityId:cityId
    	}, function(ajax) {
    		$("div[@id='QueryData']").html("");
    		$("div[@id='QueryData']").append(ajax);
    		$("button[@name='e8cStopButton']").attr("disabled", false);
    	});
    }
	
}
</script>
</head>
<body>
	<form name="frm" method="post" style="text-align: left;" id="myTable">
		<table class="listtable"  width="100%"> 
			<tr class="green_title">
				<td colspan="7">
					<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'> 
					<input	type="hidden" name="operateType" value='<s:property value="operateType" />'>
					<b>
						<s:if test="servTypeId==20">
							全业务拆机工单信息
						</s:if>
						<s:if test="servTypeId==22">
							上网业务拆机工单信息
						</s:if>
						<s:if test="servTypeId==15">
							VOIPH248 拆机工单信息
						</s:if>
					</b>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" width="20%">受理时间</td>
				<td width="30%">
					<input type="text" name="dealdate"	value='<s:property value="dealdate" />' 
						readonly class="bk">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择">&nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">设备类型</td>
				<td colspan="3">
					<select name="userType" class="bk" onchange="change(this)">
							<option value="-1">==请选择==</option>
							<option value="1" >==家庭网关==</option>
							<option value="2">==企业网关==</option>
					</select> &nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>LOID</td>
				<td>
					<input type="text" id="username" name="username" maxlength=50 class="bk"  value="请先选择设备类型" disabled="disabled" onblur="checkUserInfo()"/>&nbsp; 
					<font color="#FF0000" id="usernameDiv">* </font>
				</td>
				<td class="column" align="right" width="20%">属地</td>
				<td colspan="3">
					<s:select list="cityList" name="cityId"
						headerKey="-1" headerValue="请选择属地" listKey="city_id"
						listValue="city_name" value="cityId" cssClass="bk">
					</s:select>&nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<s:if test="servTypeId==15">
				<tr style="background-color: #ffffff;">
					<td class="column" align="right" nowrap>业务电话号码</td>
					<td>
						<input type="text" name="voipTelepone" maxlength=50 class="bk" />&nbsp; 
						<font color="#FF0000">* </font>
					</td>
					<td colspan="4"></td>
				</tr>
			</s:if>
			<tr>
				<td colspan="7" align="right" class="green_foot" >
					<button name="e8cStopButton" onclick="sendSheet();">&nbsp;发送工单&nbsp;</button>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td height="30"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td>
					<div id="QueryData" style="width: 105%; z-index: 1; top: 100px;text-align: left;">
						正在发送，请稍等....
					</div>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
		</table>
	</form>
</body>
</html>