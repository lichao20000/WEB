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
	
	var areamName = "<s:property value='instAreaName' />";
	var _dealdate = $("input[@name='dealdate']");
	var _userType = $("select[@name='userType']");
	var _username = $("input[@name='username']");
	var _cityId = $("select[@name='cityId']");
	var _orderType = $("input[@name='orderType']");
	
	var _voipTelepone = $("input[@name='voipTelepone']");
	var _regIdType = $("select[@name='regIdType']");
	var _regId = $("input[@name='regId']");
	//var _voipPasswd = $("input[@name='voipPasswd']");
	var _sipIp = $("input[@name='sipIp']");
	var _sipPort = $("input[@name='sipPort']");
	var _standSipIp = $("input[@name='standSipIp']");
	var _standSipPort = $("input[@name='standSipPort']");
	var _linePort = $("select[@name='linePort']");
	var _voipPort = $("select[@name='voipPort']");
	
	var areaName = $("input[@name='areaName']").val();
	
	
	
	//受理时间
	if(!IsNull(_dealdate.val(), "受理时间")){
		_dealdate.focus();
		return false;
	}
	//用户类型
	
	if("nmg_dx"==areamName){
		if('' == _userType.val() || '-1' == _userType.val()){
			alert("请选择设备类型");
			_userType.focus();
			return false;
		}
	}else{
		if('' == _userType.val() || '-1' == _userType.val()){
			alert("请选择用户类型");
			_userType.focus();
			return false;
		}
	}
	
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	
	
	
	

	if("nmg_dx"==areamName){
		
		//主SIP服务器地址
		if(!IsNull(_sipIp.val(), "主用MGC地址")){
			_sipIp.focus();
			return false;
		}
		//主SIP服务器端口
		if(!IsNull(_sipPort.val(), "主用MGC端口")){
			_sipPort.focus();
			return false;
		}
		//备SIP服务器地址
		if(!IsNull(_standSipIp.val(), "备用MGC地址")){
			_standSipIp.focus();
			return false;
		}
		//备SIP服务器端口
		if(!IsNull(_standSipPort.val(), "备用MGC端口")){
			_standSipPort.focus();
			return false;
		}
	}else{
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
	}
	
	//业务电话号码
	if(!IsNull(_voipTelepone.val(), "业务电话号码")){
		_voipTelepone.focus();
		return false;
	}
	
	if('' == _regIdType.val() || '-1' == _regIdType.val()){
		alert("请选择终端标识类型");
		_regIdType.focus();
		return false;
	}
	
	//语音端口【终端物理标识】
	if('' == _linePort.val() || '-1' == _linePort.val()){
		alert("请选择语音端口");
		_linePort.focus();
		return false;
	}
	
	//终端标识
	if(!IsNull(_regId.val(), "终端标识")){
		_regId.focus();
		return false;
	}
	
	if("nmg_dx"==areaName){
		var _netType = $("select[@name='netType']");
		var _ipAddress = $("input[@name='ipAddress']");
		var _mask = $("input[@name='mask']");
		var _gateway = $("input[@name='gateway']");
		var _dsn = $("input[@name='dsn']");
		
		var _protocol = $("select[@name='protocol']");
		
		
		
		//上网方式
		if(!IsNull(_netType.val(), "上网方式")){
			_netType.focus();
			return false;
		}
		//ip地址
		if(!IsNull(_ipAddress.val(), "IP地址")){
			_ipAddress.focus();
			return false;
		}
		//掩码
		if(!IsNull(_mask.val(), "掩码")){
			_mask.focus();
			return false;
		}
		//网关
		if(!IsNull(_gateway.val(), "网关")){
			_gateway.focus();
			return false;
		}
		//DSN
		if(!IsNull(_dsn.val(), "DSN")){
			_dsn.focus();
			return false;
		}
		//SIP协议
		if('' == _protocol.val() || '-1' == _protocol.val()){
			alert("请选择SIP协议类型");
			_protocol.focus();
			return false;
		}
		if(_protocol.val()=='0'){
			var _rtpPrefix = $("input[@name='rtpPrefix']");
			var _ephemeralTermIDStart = $("input[@name='ephemeralTermIDStart']");
			var _ephemeralTermIDAddLen = $("input[@name='ephemeralTermIDAddLen']");
			if(!IsNull(_rtpPrefix.val(), "临时终结点前缀")){
				_rtpPrefix.focus();
				return false;
			}
			if(!IsNull(_ephemeralTermIDStart.val(), "临时终结点起始")){
				_ephemeralTermIDStart.focus();
				return false;
			}
			if(!IsNull(_ephemeralTermIDAddLen.val(), "临时终结点长度")){
				_ephemeralTermIDAddLen.focus();
				return false;
			}
		}
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
		}
		else
		{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}
// 默认隐藏
$(function(){
	$('.temp').hide();
});
// SIP协议类型选择事件
$(function(){
	$('#protocol').change(function(){
		if($('#protocol').val() == '0'){
			$('.temp').show();
			return;
		}
		else{
			$('.temp').hide();
		}
	});
});
</script>
</head>
<body>
<form name="frm"
	action="<s:url value='/itms/service/simulateNMGSheet!sendSheet.action'/>"
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
								<TD align="center"><font size="2"><b>H248 VOIP开户信息</b></font></TD>
								<input type="hidden" name="areaName" id="areaName" value='<s:property value='instAreaName'/>' />
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
						<TD width="30%"><select name="userType" class="bk" onchange="change(this)">
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
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD class=column align="right" nowrap>主用MGC地址</TD>
							<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>主用MGC端口</TD>
							<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:if>
						<s:else>
							<TD class=column align="right" nowrap>主SIP服务器地址</TD>
							<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>主SIP服务器端口</TD>
							
							<TD><INPUT TYPE="text" NAME="sipPort" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:else>
					</TR>
					<TR bgcolor="#FFFFFF">
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD class=column align="right" nowrap>备用MGC地址</TD>
							<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>备用MGC端口</TD>
							<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:if>
						<s:else>
							<TD class=column align="right" nowrap>备SIP服务器地址</TD>
							<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>备SIP服务器端口</TD>
							<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</s:else>
						
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>终端标识类型</TD>
						<TD><select name="regIdType" class="bk">
							<option value="-1">==请选择标识类型==</option>
							<option value="0">==IP地址==</option>
							<option value="1">==域名==</option>
							<option value="2">==设备名==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>终端标识</TD>
						<TD><INPUT TYPE="text" NAME="regId" maxlength=20 onblur="checkInfo()"
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>终端物理标识</TD>
						<s:if test='%{instAreaName=="nmg_dx"}'>
							<TD><select name="linePort" class="bk">
							<option value="-1">==请选择物理标识==</option>
							<!--<option value="A0">==A0==</option>
							<option value="A1">==A1==</option>-->
							<s:iterator value="linePortList">
								<option value="<s:property value="value"/>">
								==<s:property value="text" />==
								</option>
							</s:iterator>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						</s:if>
						<s:else>
							<TD><select name="linePort" class="bk">
							<option value="-1">==请选择物理标识==</option>
							<option value="A1">==A1==</option>
							<option value="A2">==A2==</option>
							<option value="AL1">==AL1==</option>
							<option value="AL2">==AL2==</option>
							<option value="AG58900">==AG58900==</option>
							<option value="AG58901">==AG58901==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						</s:else>
					
						<TD class=column align="right" nowrap>业务电话号码</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<s:if test='%{instAreaName=="nmg_dx"}'>
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>上网方式</TD>
							<TD><select name="netType" class="bk">
								<option value="-1">==请选择上网方式==</option>
								<option value="3">==静态IP==</option>
							</select> &nbsp;<font color="#FF0000">*</font></TD>
							<TD class=column align="right" nowrap>IP地址</TD>
							<TD><INPUT TYPE="text" NAME="ipAddress" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>掩码</TD>
							<TD><INPUT TYPE="text" NAME="mask" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>网关</TD>
							<TD><INPUT TYPE="text" NAME="gateway" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>DNS</TD>
							<TD><INPUT TYPE="text" NAME="dsn" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>备用DNS</TD>
							<TD><INPUT TYPE="text" NAME="dns2" maxlength=20
								class=bk value=""></TD>
						</TR>
						
						<TR bgcolor="#FFFFFF">
							<TD class=column align="right" nowrap>协议类型</TD>
							<TD colspan=3>
								<select name="protocol" class=bk id="protocol">
									<option value="-1">==请选择协议类型==</option>
									<option value="1">NGN</option>
									<option value="0">IMS</option>
								</select>&nbsp; <font color="#FF0000">*</font>
							</TD>
						</TR>
						
						<TR bgcolor="#FFFFFF" class="temp">
							<TD class=column align="right" nowrap>临时终结点前缀</TD>
							<TD><INPUT TYPE="text" NAME="rtpPrefix" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
							<TD class=column align="right" nowrap>临时终结点起始</TD>
							<TD><INPUT TYPE="text" NAME="ephemeralTermIDStart" maxlength=20
								class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						</TR>
						<TR bgcolor="#FFFFFF" class="temp">
							<TD class=column align="right" nowrap>临时终结点长度</TD>
							<TD colspan=3>
								<INPUT TYPE="text" NAME="ephemeralTermIDAddLen" maxlength=20 class=bk value="">&nbsp; <font color="#FF0000">* </font>
							</TD>
						</TR>
						
					</s:if>
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
