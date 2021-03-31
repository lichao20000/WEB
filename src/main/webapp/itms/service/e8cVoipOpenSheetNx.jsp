<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>VOIP H.248模拟工单</title>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});
function CheckForm(){
	var _wanType = $("select[@name='wanType']");	// IP获取方式
	var _username = $("input[@name='username']");   // LOID
	var _devType = $("select[@name='devType']");    // 设备类型
	var _cityId = $("select[@name='cityId']");      // 属地
	//var _vlanid = $("input[@name='vlanid']");       // vlanid
	//var _vpiid = $("input[@name='vpiid']");
	//var _vciid = $("input[@name='vciid']");
	var _ipaddress = $("input[@name='ipaddress']"); // IP地址
	var _ipmask = $("input[name='ipmask']");        //掩码
	var _gateway = $("input[name='gateway']");      // 网关
	var _adslSer = $("input[name='adslSer']");      // DNS值
	var _sipIp = $("input[@name='sipIp']");         // 主MGC服务器地址
	var _sipPort = $("input[@name='sipPort']");     // 主MGC服务器端口
	var _standSipIp = $("input[@name='standSipIp']");      // 备MGC服务器地址
	var _standSipPort = $("input[@name='standSipPort']");  // 备MGC服务器端口
	var _regIdType = $("select[@name='regIdType']");       // 终端标识类型
	var _regId = $("input[@name='regId']");                // 终端标识
	var _voipPort = $("select[@name='voipPort']");         // 终端物理标识
	var _voipTelepone = $("input[@name='voipTelepone']");  // 业务电话号码
	var _operateType = $("input[@name='operateType']");   
	
	//IP获取方式
	if(_wanType.val() == "-1"){
		alert("请选择IP获取方式");
		_wanType.focus();
		return false;
	}
	//用户账号(LOID)
	if(!IsNull(_username.val(), "用户账号")){
		_username.focus();
		return false;
	}
	//设备类型
	if('' == _devType.val() || '-1' == _devType.val()){
		alert("请选择设备类型");
		_devType.focus();
		return false;
	}
	//属地
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("请选择属地");
		_cityId.focus();
		return false;
	}
	// _vlanid
	//if(!IsNull(_vlanid.val(), "vlanid")){
	//	_vlanid.focus();
	//	return false;
	//}
	// vpiid
	//if(!IsNull(_vpiid.val(), "PVC")){
	//	_vpiid.focus();
	//	return false;
	//}
	// vciid
	//if(!IsNull(_vciid.val(), "PVC")){
	//	_vciid.focus();
	//	return false;
	//}
	
	// 当 IP获取方式 为"STATIC"时 IP地址、掩码、网管、DNS值才为必填项
	if(_wanType.val() == "3"){ 
		//IP地址
		if(!IsNull(_ipaddress.val(), "IP地址")){
			_ipaddress.focus();
			return false;
		}else if(!reg_verify(_ipaddress.val())){
			alert("请输入合法的IP地址！");
			_ipaddress.focus();
			return false;
		}
		// 掩码
		if(!IsNull(_ipmask.val(), "掩码")){
			_ipmask.focus();
			return false;
		}else if(!IsMask(_ipmask.val())){
			_ipmask.focus();
			return false;
		}
		// 网关
		if(!IsNull(_gateway.val(), "网关")){
			_gateway.focus();
			return false;
		}else if(!reg_verify(_gateway.val())){
			alert("请输入合法的网关地址！");
			_gateway.focus();
			return false;
		}
		// DNS值
		if(!IsNull(_adslSer.val(), "DNS值")){
			_adslSer.focus();
			return false;
		}else if(!reg_verify(_adslSer.val())){
			alert("请输入合法的DNS地址！");
			_adslSer.focus();
			return false;
		}
	}
	
	//主MGC服务器地址
	if(!IsNull(_sipIp.val(), "主MGC服务器地址")){
		_sipIp.focus();
		return false;
	}
	//else if(!reg_verify(_sipIp.val())){
	//	alert("请输入合法的主MGC服务器地址！");
	//	_sipIp.focus();
	//	return false;
	//}
	//主MGC服务器端口
	if(!IsNull(_sipPort.val(), "主MGC服务器端口")){
		_sipPort.focus();
		return false;
	}else if(!IsNumber(_sipPort.val(),"主MGC服务器端口")){
		_sipPort.focus();
		return false;
	}
	//备MGC服务器地址
	if(!IsNull(_standSipIp.val(), "备MGC服务器地址")){
		_standSipIp.focus();
		return false;
	}
	//else if(!reg_verify(_standSipIp.val())){
	//	alert("请输入合法的备MGC服务器地址！");
	//	_standSipIp.focus();
	//	return false;
	//}
	//备MGC服务器端口
	if(!IsNull(_standSipPort.val(), "备MGC服务器端口")){
		_standSipPort.focus();
		return false;
	}else if(!IsNumber(_standSipPort.val(),"备MGC服务器端口")){
		_standSipPort.focus();
		return false;
	}
	//终端标识类型
	if('' == _regIdType.val() || '-1' == _regIdType.val()){
		alert("请选择终端标识类型");
		_regIdType.focus();
		return false;
	}
	//终端标识
	if(!IsNull(_regId.val(), "终端标识")){
		_regId.focus();
		return false;
	}
	// 终端物理标识
	if('' == _voipPort.val() || '-1' == _voipPort.val()){
		alert("请选择终端物理标识");
		_voipPort.focus();
		return false;
	}
	//业务电话号码
	if(!IsNull(_voipTelepone.val(), "业务电话号码")){
		_voipTelepone.focus();
		return false;
	}
	if(hasUsername == 0){
		alert("请输入正确的LOID");
		return false;
	}
	//document.frm.submit();
	var url = "<s:url value='/itms/service/simulateSheetNx!sendSheet.action'/>";
	
	$.post(url,{
		wanType:_wanType.val(),     // IP获取方式
		username:_username.val(),   // LOID
		devType:_devType.val(),     // 设备类型
		cityId:_cityId.val(),       // 属地
		//vlanid:_vlanid.val(),       // vlanid
		//vpiid:_vpiid.val(),
		//vciid:_vciid.val(),
		ipaddress:_ipaddress.val(),  // IP地址
		ipmask:_ipmask.val(),        //掩码
		gateway:_gateway.val(),      // 网关
		adslSer:_adslSer.val(),      // DNS值
		mgcIp:_sipIp.val(),          // 主MGC服务器地址
		mgcPort:_sipPort.val(),      // 主MGC服务器端口
		standMgcIp:_standSipIp.val(),      // 备MGC服务器地址
		standMgcPort:_standSipPort.val(),  // 备MGC服务器端口
		regIdType:_regIdType.val(),        // 终端标识类型
		regId:encodeURIComponent(_regId.val()), // 终端标识
		voipPort:_voipPort.val(),        	    // 终端物理标识
		voipTelepone:_voipTelepone.val(),   // 业务电话号码
		operateType:_operateType.val()
	},function(ajax){
		alert(ajax);
		window.location.reload();
		//window.close();
	});
	
}

/* reg_verify - 完全用正则表达式来判断一个字符串是否是合法的IP地址，
如果是则返回true，否则，返回false。*/
function reg_verify(addr){
	//正则表达式
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg)) {
    	//IP地址正确
        return true;
    } else {
    	//IP地址校验失败
         return false;
    }
}




var hasUsername = 0;

function checkUserInfo(){
	var _username = $("input[@name='username']").val();
	var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
	$.post(url, {
		username : _username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1"){
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
			//$("tr[@id='vlanid']").css("display","none");
			//$("tr[@id='pvc']").css("display","none");
		}else{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}

function hiddenElements(){
	var _wanType = $("select[@name='wanType']");	// IP获取方式
	
	if(_wanType.val() == "-1" || _wanType.val() == "3"){
		$("tr[@id='tr01']").css("display","");
		$("tr[@id='tr02']").css("display","");
	}else{
		$("tr[@id='tr01']").css("display","none");
		$("tr[@id='tr02']").css("display","none");
	}
}

function checkInfo(){
	var _regIdType = $("select[@name='regIdType']");       // 终端标识类型
	var _regId = $("input[@name='regId']");                // 终端标识
	
	if("0" == _regIdType.val() && "" != _regId.val()){
		if(!reg_verify(_regId.val())){
			alert("请输入合法的IP地址！");
			_regId.focus();
			return false;
		}
	}
}


</script>
</head>
<body onLoad="hiddenElements()">
<!-- <form name="frm" action="<s:url value='/itms/service/simulateSheetNx!sendSheet.action'/>" method="post"> -->
<form name="frm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
			align="center">
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR class="green_title">
						<TD colspan="4">
						
							<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'> 
							<input type="hidden" name="operateType" value='<s:property value="operateType" />'>
							
							<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
								<TR>
									<TD><font size="2">VOIP(H248)开户信息</font></TD>
									<TD align="right"><IMG SRC="images/up_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">IP获取方式</TD>
						<TD width="29%">
							<select name="wanType" class="bk" onChange="hiddenElements()">
								<option value="-1">==请选择操作类型==</option>
								<option value="3">==STATIC==</option>
								<option value="4">==DHCP==</option>
							</select>&nbsp;<font color="#FF0000">*</font>
					  </TD>
						<TD class=column align="right" nowrap>LOID</TD>
						<TD><INPUT TYPE="text" NAME="username"
							onblur="checkUserInfo()" maxlength=50 class=bk value="">&nbsp;
						<font id="usernameDiv" color="#FF0000">* </font></TD>

					</TR>

					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="20%">设备类型</TD>
						<TD width="29%"><select name="devType" class="bk">
							<option selected value="e8c">==E8-C==</option>
					  </select> &nbsp; <font color="#FF0000">*</font></TD>
						<TD class=column align="right" width="20%">属地</TD>
						<TD width="31%"><s:select list="cityList" name="cityId"
							headerKey="-1" headerValue="请选择属地" listKey="city_id"
							listValue="city_name" value="cityId" cssClass="bk"></s:select>
					  &nbsp; <font color="#FF0000">*</font></TD>
					<TR bgcolor="#FFFFFF" style="display:none"> <!-- 此处的tr暂时隐藏 -->
						<TD class=column align="right" nowrap>vlanid</TD>
						<TD><INPUT TYPE="text" NAME="vlanid" 
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>PVC</TD>
						<TD><INPUT NAME="vpiid" TYPE="text" title="vpiid"
							class=bk value="" size="8">&nbsp;/&nbsp;<INPUT NAME="vciid" TYPE="text" title="vciid"
							class=bk value="" size="8">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr01" style="display: none">
						<TD class=column align="right" nowrap>IP地址</TD>
						<TD><INPUT TYPE="text" NAME="ipaddress" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>掩码</TD>
						<TD><INPUT TYPE="text" NAME="ipmask" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr02" style="display: none">
						<TD class=column align="right" nowrap>网关</TD>
						<TD><INPUT TYPE="text" NAME="gateway" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>DNS值</TD>
						<TD><INPUT TYPE="text" NAME="adslSer" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>主MGC服务器地址</TD>
						<TD><INPUT TYPE="text" NAME="sipIp" maxlength=20 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>主MGC服务器端口</TD>
						<TD><INPUT TYPE="text" NAME="sipPort" maxlength=5 class=bk
							value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap>备MGC服务器地址</TD>
						<TD><INPUT TYPE="text" NAME="standSipIp" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
						<TD class=column align="right" nowrap>备MGC服务器端口</TD>
						<TD><INPUT TYPE="text" NAME="standSipPort" maxlength=5
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
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
						<TD><select name="voipPort" class="bk">
							<option value="-1">==请选择物理标识==</option>
							<option value="A1">==A1==</option>
							<option value="A2">==A2==</option>
							<option value="AL1">==AL1==</option>
							<option value="AL2">==AL2==</option>
							<option value="AG58900">==AG58900==</option>
							<option value="AG58901">==AG58901==</option>
						</select> &nbsp;<font color="#FF0000">*</font></TD>
						<TD class=column align="right" nowrap>业务电话号码</TD>
						<TD><INPUT TYPE="text" NAME="voipTelepone" maxlength=20
							class=bk value="">&nbsp; <font color="#FF0000">* </font></TD>
					</TR>
					<TR>
						<TD colspan="4" align="right" class="green_foot">
							<button onClick="CheckForm()">&nbsp;开&nbsp;&nbsp;户&nbsp;</button>
						</TD>
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