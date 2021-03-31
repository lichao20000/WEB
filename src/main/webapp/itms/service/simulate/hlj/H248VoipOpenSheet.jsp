<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="/Js/jquery.js"></script>
<script type="text/javascript" src="/Js/CheckFormForm.js"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS资料信息开户</title>
<script type="text/javascript">
	function sendSheet(){
		var servTypeId = $.trim($("input[@name='servTypeId']").val());
		var	operateType	= $.trim($("input[@name='operateType']").val());
		var dealdate = $.trim($("input[@name='dealdate']").val());
		var userType = $.trim($("select[@name='userType']").val());
		var username = $.trim($("input[@name='username']").val());
		var voipTelepone = $.trim($("input[@name='voipTelepone']").val());
		var cityId = $.trim($("select[@name='cityId']").val());
		var regId = $.trim($("input[@name='regId']").val());
		var regIdType = $.trim($("select[@name='regIdType']").val());
		var sipIp = $.trim($("input[@name='sipIp']").val());
		var sipPort = $.trim($("input[@name='sipPort']").val());
		var standSipIp = $.trim($("input[@name='standSipIp']").val());
		var standSipPort = $.trim($("input[@name='standSipPort']").val());
		var linePort = $.trim($("select[@name='linePort']").val());
		var protocolType = $.trim($("select[@name='protocolType']").val());
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
		//业务电话号码
		if(voipTelepone  == ""){
			alert("请输入业务电话号码");
			$("input[@name='voipTelepone']").focus();
			return false;
		}
		//属地
		if(cityId  == ""){
			alert("请选择属地");
			$("select[@name='cityId']").focus();
			return false;
		}
		//终端标识
		var regIdReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+.(voip)$/;
		if(regId  == ""){
			alert("请输入终端标识");
			$("input[@name='regId']").focus();
			return false;
		}else{
			if(!regIdReg.test(regId)){
				alert('请输入有效的终端标识!');
				$("input[@name='regId']").focus();
				return false;
			}
		}
		//终端标识类型
		if(regIdType  == -1){
			alert("请选择终端标识类型");
			$("select[@name='regIdType']").focus();
			return false;
		}
		var myReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		//主用MGC地址
		if(sipIp  == ""){
			alert("请输入主用MGC地址");
			$("input[@name='sipIp']").focus();
			return false;
		}
		//主用MGC器端口
		if(sipPort  == ""){
			alert("请输入主用MGC器端口");
			$("input[@name='sipPort']").focus();
			return false;
		}else{
			if(isNaN(sipPort)){
				alert("您输入的MGC器端口不是数字，请重新输入!");
				$("input[@name='sipPort']").focus();
				return false;
			}
		}
		//备用MGC地址
		if(standSipIp  == ""){
			alert("请输入备用MGC地址");
			$("input[@name='standSipIp']").focus();
			return false;
		}
		//备用MGC端口
		if(standSipPort  == ""){
			alert("请输入备用MGC端口");
			$("input[@name='standSipPort']").focus();
			return false;
		}else{
			if(isNaN(standSipPort)){
				alert("您输入的备用MGC端口不是数字，请重新输入!");
				$("input[@name='standSipPort']").focus();
				return false;
			}
		}
		//终端物理标示
		if(linePort  == -1){
			alert("请选择终端物理标示");
			$("select[@name='linePort']").focus();
			return false;
		}
		//
		if(protocolType  == -1){
			alert("请选择协议类型");
			$("select[@name='protocolType']").focus();
			return false;
		}
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("正在发送，请稍等....");
	    $("button[@name='H248VoipButton']").attr("disabled", true);
	    var url = "<s:url value='/itms/service/simulateHLJSheet!sendSheet.action'/>";
		$.post(url, {
			servTypeId:servTypeId,
			operateType:operateType,
			dealdate:dealdate,
			userType:userType,
			username:username,
			voipTelepone:voipTelepone,
			cityId:cityId,
			regId:regId,
			regIdType:regIdType,
			sipIp:sipIp,
			sipPort:sipPort,
			standSipIp:standSipIp,
			standSipPort:standSipPort,
			linePort:linePort
		}, function(ajax) {
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='H248VoipButton']").attr("disabled", false);
		});
	}
</script>
</head>
<body>
	<form name="frm" method="post" style="text-align: left;">
		<table class="listtable"  width="100%"> 
			<tr class="green_title">
				<td colspan="7">
					<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'> 
					<input	type="hidden" name="operateType" value='<s:property value="operateType" />'>
				<b>E8-C资料信息</b>
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
					<select name="userType" class="bk">
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
					<input type="text" name="username" maxlength=50 class="bk" value="" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" width="20%">业务电话号码</td>
				<td colspan="3">
					<input type="text" name="voipTelepone" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>属地</td>
				<td>
					<s:select list="cityList" name="cityId"
						headerKey="-1" headerValue="请选择属地" listKey="city_id"
						listValue="city_name" value="cityId" cssClass="bk">
					</s:select>&nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">终端标识</td>
				<td colspan="3">
					<input type="text" name="regId" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>终端标识类型</td>
				<td>
					<select name="regIdType" class="bk">
							<option value="-1">==请选择标识类型==</option>
							<option value="0">==IP地址==</option>
							<option value="1">==域名==</option>
							<option value="2">==设备名==</option>
					</select> &nbsp;
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">主用MGC地址</td>
				<td colspan="3">
					<input type="text" name="sipIp" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>主用MGC端口</td>
				<td>
					<input type="text" name="sipPort" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" width="20%">备用MGC地址</td>
				<td colspan="3">
					<input type="text" name="standSipIp" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>备用MGC端口</td>
				<td>
					<input type="text" name="standSipPort" maxlength=50 class="bk" />&nbsp; 
					<font color="#FF0000">* </font>
				</td>
				<td class="column" align="right" width="20%">终端物理标示</td>
				<td colspan="3">
					<select name="linePort" class="bk">
							<option value="-1">==请选择终端物理标示==</option>
							<option value="A0">==A0==</option>
							<option value="A1">==A1==</option>
					</select> &nbsp;<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>协议类型</td>
				<td>
					<select name="protocolType" class="bk">
							<option value="-1">==请选择协议类型==</option>
							<option value="0">==IMS==</option>
							<option value="1">==软交换==</option>
					</select> &nbsp;<font color="#FF0000">*</font>
				</td>
				<!-- <td class="column" align="right" width="20%"></td>-->
				<td colspan="4"></td> 
			</tr>
			<tr>
				<td colspan="7" align="right" class="green_foot" >
					<button name="H248VoipButton" onclick="sendSheet();">&nbsp;发送工单&nbsp;</button>
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