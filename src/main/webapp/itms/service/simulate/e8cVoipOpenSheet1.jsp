<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>BSS模拟工单</title>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});
function CheckForm(){
	var _dealdate = $("input[@name='dealdate']");
	var _devType = $("select[@name='devType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("input[@name='orderType']");
	
	var _voipTelepone = $("input[@name='voipTelepone']");
	var _voipUsername = $("input[@name='voipUsername']");
	var _voipPasswd = $("input[@name='voipPasswd']");
	var _sipIp = $("input[@name='sipIp']");
	var _sipPort = $("input[@name='sipPort']");
	var _standSipIp = $("input[@name='standSipIp']");
	var _standSipPort = $("input[@name='standSipPort']");
	
	var _registrarServer = $("input[@name='registrarServer']");
	var _registrarServerPort = $("input[@name='registrarServerPort']");
	var _standRegistrarServer = $("input[@name='standRegistrarServer']");
	var _standRegistrarServerPort = $("input[@name='standRegistrarServerPort']");
	
	var _outboundProxy = $("input[@name='outboundProxy']");
	var _outboundProxyPort = $("input[@name='outboundProxyPort']");
	var _standOutboundProxy = $("input[@name='standOutboundProxy']");
	var _standOutboundProxyPort = $("input[@name='standOutboundProxyPort']");
	var _linePort = $("select[@name='linePort']");
	var _protocol = $("select[@name='protocol']");
	//受理时间
	if(!IsNull(_dealdate.val(), "受理时间")){
		_dealdate.focus();
		return false;
	}
	//设备类型
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择设备类型");
		_orderType.focus();
		return false;
	}
	//用户账号
	if(!IsNull(_username.val(), "用户账号")){
		_username.focus();
		return false;
	}
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	//VOIP认证账号
	if(!IsNull(_voipUsername.val(), "VOIP认证账号")){
		_voipUsername.focus();
		return false;
	}
	//VOIP认证密码
	if(!IsNull(_voipPasswd.val(), "VOIP认证密码")){
		_voipPasswd.focus();
		return false;
	}
	//主SIP服务器地址
	if(!IsNull(_sipIp.val(), "主SIP服务器地址")){
		_sipIp.focus();
		return false;
	}
	//主SIP服务器端口
	if(!IsNull(_sipPort.val(), "主SIP服务器端口")){
		_sipPort.focus();
		return false;
	}
	//备SIP服务器地址
	if(!IsNull(_standSipIp.val(), "备SIP服务器地址")){
		_standSipIp.focus();
		return false;
	}
	//备SIP服务器端口
	if(!IsNull(_standSipPort.val(), "备SIP服务器端口")){
		_standSipPort.focus();
		return false;
	}
	
	//主RegistrarServer
	if(!IsNull(_registrarServer.val(), "主RegistrarServer")){
		_registrarServer.focus();
		return false;
	}
	//主RegistrarServerPort
	if(!IsNull(_registrarServerPort.val(), "主RegistrarServerPort")){
		_registrarServerPort.focus();
		return false;
	}
	//备RegistrarServer
	if(!IsNull(_standRegistrarServer.val(), "备RegistrarServer")){
		_standRegistrarServer.focus();
		return false;
	}
	//备RegistrarServerPort
	if(!IsNull(_standRegistrarServerPort.val(), "备RegistrarServerPort")){
		_standRegistrarServerPort.focus();
		return false;
	}//主OutboundProxy
	if(!IsNull(_outboundProxy.val(), "主OutboundProxy")){
		_outboundProxy.focus();
		return false;
	}
	//主OutboundProxyPort
	if(!IsNull(_outboundProxyPort.val(), "主OutboundProxyPort")){
		_outboundProxyPort.focus();
		return false;
	}
	//备OutboundProxy
	if(!IsNull(_standOutboundProxy.val(), "备OutboundProxy")){
		_standOutboundProxy.focus();
		return false;
	}
	//备OutboundProxyPort
	if(!IsNull(_standOutboundProxyPort.val(), "备OutboundProxyPort")){
		_standOutboundProxyPort.focus();
		return false;
	}
	
	//业务电话号码
	if(!IsNull(_voipTelepone.val(), "业务电话号码")){
		_voipTelepone.focus();
		return false;
	}
	//语音端口
	if('' == _linePort.val() || '-1' == _linePort.val()){
		alert("请选择语音端口");
		_linePort.focus();
		return false;
	}
	//协议类型
	if('' == _protocol.val() || '-1' == _protocol.val()){
		alert("请选择协议类型");
		_protocol.focus();
		return false;
	}
	if(hasUsername == 0){
		alert("请输入正确的LOID");
		return false;
	}
	document.frm.submit();
}
var hasUsername = 0;
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
function checkUserInfo(){
	var _username = $("input[@name='username']").val();
	var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
	$.post(url, {
		gw_type : gwType,
		username : _username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1")
		{
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
			$("tr[@id='vlanid']").css("display",none);
			$("tr[@id='pvc']").css("display","none");
		}
		else
		{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}
</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/itms/service/simulateSheet!sendSheet.action'/>"
	method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4"><input type="hidden" name="servTypeId"
							value='<s:property value="servTypeId" />'> <input
							type="hidden" name="operateType"
							value='<s:property value="operateType" />'>
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR>
								<TD align="center"><font size="2"><b>VOIP开户信息</b></font></TD>
							</TR>
						</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">受理时间</TD>
						<TD width="30%"><input type="text" name="dealdate"
							value='<s:property value="dealdate" />' readonly class=bk>
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="选择">&nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>LOID</TD>
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD><INPUT TYPE="text" id="username" NAME="username" disabled="disabled"
							onblur="checkUserInfo()" maxlength=50 class=bk value="请先选择设备类型 ">&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>
						</s:if>
						<s:else>
							<TD><INPUT TYPE="text" id="username" NAME="username" disabled="disabled"
							onblur="checkUserInfo()" maxlength=50 class=bk value="请先选择用户类型 ">&nbsp;
							<font id="usernameDiv" color="#FF0000">* </font></TD>
						</s:else>

					</TR>

					<TR bgcolor="#FFFFFF">
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD class=column align="right" width="20%">设备类型</TD>
						</s:if>
						<s:else>
							<TD class=column align="right" width="20%">用户类型</TD>
						</s:else>
						<TD width="30%"><select name="devType" class="bk" onchange="change(this)">
							<option  value="-1">==请选择==</option>
							<option  value="1">==家庭网关==</option>
							<option  value="2">==企业网关==</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">属地</TD>
						<TD width="30%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="请选择属地" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
						&nbsp; <font color="#FF0000">*</font></TD>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>VOIP认证账号</TD>
						<TD><INPUT TYPE="text" NAME="voipUsername" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>VOIP认证密码</TD>
						<TD><INPUT TYPE="text" NAME="voipPasswd" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>主SIP服务器地址</TD>
						<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>主SIP服务器端口</TD>
						<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>备SIP服务器地址</TD>
						<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>备SIP服务器端口</TD>
						<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>主RegistrarServer</TD>
						<TD><INPUT TYPE="text" NAME="registrarServer" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>主RegistrarServerPort</TD>
						<TD><INPUT TYPE="text" NAME="registrarServerPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>备RegistrarServer</TD>
						<TD><INPUT TYPE="text" NAME="standRegistrarServer" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>备RegistrarServerPort</TD>
						<TD><INPUT TYPE="text" NAME="standRegistrarServerPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>主OutboundProxy</TD>
						<TD><INPUT TYPE="text" NAME="outboundProxy" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>主OutboundProxyPort</TD>
						<TD><INPUT TYPE="text" NAME="outboundProxyPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>备OutboundProxy</TD>
						<TD><INPUT TYPE="text" NAME="standOutboundProxy" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>备OutboundProxyPort</TD>
						<TD><INPUT TYPE="text" NAME="standOutboundProxyPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>业务电话号码</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>语音端口</TD>
						<TD><select name="linePort" class=bk>
							<option value="-1">==请选择语音端口==</option>
							<option value="V1">V1</option>
							<option value="V2">V2</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>SIP协议类型</TD>
						<TD colspan="3"><select name="protocol" class=bk>
							<option value="-1">==请选择协议类型==</option>
							<option value="1">软交换</option>
							<option value="0">IMS</option>
						</select> &nbsp; <font color="#FF0000">*</font></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot"><button onclick="CheckForm()">
						&nbsp;发送工单&nbsp;
						</button></TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
</body>
</html>