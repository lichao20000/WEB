<%--
业务手工工单
Author: Jason
Version: 1.0.0
Date: 2009-09-22
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<HEAD>
<title>业务手工工单</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--//

/** 查询用户或设备对应的信息（下发工单用）*/
function searchSheetInfo() {
	var queryDevSn = $("input[@name='queryDevSn']");
	var queryUsername = $("input[@name='queryUsername']");
	var queryServTypeId = $("select[@name='queryServTypeId']");
	if('' == queryUsername.val() && '' == queryDevSn.val()){
		alert("请输入用户账号或设备序列号进行查询");
		queryUsername.focus();
		return false;
	}
	if('' != queryDevSn.val() && queryDevSn.val().length < 6){
		alert("设备序列号小于6位");
		queryDevSn.focus();
		return false;
	}
	var gw_type = $("input[@name='gw_type']").val();
	
	var url = "<s:url value='/gwms/config/serviceManSheet!queryServUserInfo.action'/>";
	$.post(url,{
		devSn:queryDevSn.val(),
		username:queryUsername.val(),
		servTypeId:queryServTypeId.val(),
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_sheetInfo']").html("");
		$("div[@id='div_sheetInfo']").append(ajax);
	})
}

//接入方式
function chgAccessType(v){
	if(1 == v){
		$("td[@id='id_pvc1']").show();
		$("td[@id='id_pvc2']").show();
		$("td[@id='id_vlan1']").hide();
		$("td[@id='id_vlan2']").hide();
	}else{
		$("td[@id='id_pvc1']").hide();
		$("td[@id='id_pvc2']").hide();
		$("td[@id='id_vlan1']").show();
		$("td[@id='id_vlan2']").show();
	}
}

//上网方式
function chgnetType(v){
	if(2 == v){
		//路由
		$("tr[@id='id_routed']").show();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}else if(3 == v){
		//静态IP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").show();
		$("tr[@id='id_static2']").show();
	}else{
		//桥接和DHCP
		$("tr[@id='id_routed']").hide();
		$("tr[@id='id_static1']").hide();
		$("tr[@id='id_static2']").hide();
	}
}

//工单下发
function doBusinessSheet(){
	var gw_type = $("input[@name='gw_type']").val();
	
	var servTypeId = $("input[@name='servTypeId']").val();
	var userId = $("input[@name='userId']").val();
	var deviceId = $("input[@name='deviceId']").val();
	var cityId = $("input[@name='cityId']").val();
	var officeId = $("input[@name='officeId']").val();
	var accessType = $("select[@name='accessType']").val();
	var netType = $("select[@name='netType']").val();
	var vlanid = $("input[@name='vlanid']");
	var vpi = $("input[@name='vpi']");
	var vci = $("input[@name='vci']");
	var pppoeUsername = $("input[@name='pppoeUsername']");
	var pppoePasswd = $("input[@name='pppoePasswd']");
	var ip = $("input[@name='ip']");
	var gateway = $("input[@name='gateway']");
	var mask = $("input[@name='mask']");
	var dns = $("input[@name='dns']");
	var voipUsername = $("input[@name='voipUsername']");
	var voipPasswd = $("input[@name='voipPasswd']");
	
	var oui = $("input[@name='oui']");
	var devSn = $("input[@name='devSn']");
	var hasServUser = $("input[@name='hasServUser']");
	var bindPort = "";
	
	if('' == userId || '' == deviceId || '' == servTypeId){
		alert("用户或者设备或者业务类型为空");
		return false;
	}
	//接入方式判断
	if(1 == accessType){
		if(!IsNumber(vpi.val(),"vpi")){
			vpi.focus();
			return false;
		}
		if(!IsNumber(vci.val(),"vci")){
			vci.focus();
			return false;
		}
	}else{
		if(!IsNumber(vlanid.val(),"VlanID")){
			vlanid.focus();
			return false;
		}
	}
	//上网方式
	if(2 == netType){
		if(!IsNull(pppoeUsername.val(),"PPPoE账号")){
			pppoeUsername.focus();
			return false;
		}
		if(!IsNull(pppoePasswd.val(),"PPPoE密码")){
			pppoePasswd.focus();
			return false;
		}
	}else if(3 == netType){
		if(!IsIPAddr2(ip.val(),"IP地址")){
			ip.focus();
			return false;
		}
		if(!IsIPAddr2(gateway.val(),"网关")){
			gateway.focus();
			return false;
		}
		if(!IsIPAddr2(mask.val(),"子网掩码")){
			mask.focus();
			return false;
		}
		if(!IsIPAddr2(dns.val(),"DNS")){
			dns.focus();
			return false;
		}
	}
	//voip业务
	if('14' == servTypeId){
		if('' == officeId || '' == cityId){
			alert("属地, 局向不能为空");
			return false;
		}
		if(!IsNull(voipUsername.val(),"VOIP认证用户名")){
			voipUsername.focus();
			return false;
		}
		if(!IsNull(voipPasswd.val(),"VOIP认证密码")){
			voipPasswd.focus();
			return false;
		}
	}
	
	//工单下发
	var url = "<s:url value='/gwms/config/serviceManSheet!doService.action'/>";

	$.post(url,{
		deviceId:deviceId,
		servTypeId:servTypeId,
		userId:userId,
		accessType:accessType,
		netType:netType,
		vpi:vpi.val(),
		vci:vci.val(),
		vlanid:vlanid.val(),
		pppoeUsername:pppoeUsername.val(),
		pppoePasswd:pppoePasswd.val(),
		ip:ip.val(),
		gateway:gateway.val(),
		mask:mask.val(),
		dns:dns.val(),
		voipUsername:voipUsername.val(),
		voipPasswd:voipPasswd.val(),
		cityId:cityId,
		officeId:officeId,
		bindPort:bindPort,
		hasServUser:hasServUser.val(),
		oui:oui.val(),
		devSn:devSn.val(),
		gw_type:gw_type
	},function(ajax){
		//alert(ajax);
		$("button[@name='searchBtn']").disabled = false;
		
		$("tr[@id='resultTR1']").show();
		$("td[@id='resultTD1']").html("执行结果：");
		
		$("div[@id='result1']").html("通知后台");
		if (ajax == "1")
		{
			$("div[@id='result1']").append("<FONT COLOR='green'>成功</FONT>");
		} else {
			$("div[@id='result1']").append("<FONT COLOR='red'>失败</FONT>");
			if(ajax == "-1"){
				$("div[@id='result1']").append("(策略信息处理失败)");
			}else if (ajax == "-2"){
				$("div[@id='result1']").append("(业务用户信息处理失败)");
			}else if (ajax == "-3"){
				$("div[@id='result1']").append("(局向没有对应的VOIP服务器信息)");
			}
		}
	});
	$("button[@name='searchBtn']").disabled = true;
	$("button[@name='subBtn']").attr("disabled", true);
}
//-->
</script>
</HEAD>
<body>
<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" value="<s:property value='gw_type' />" name="gw_type" />
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">手工工单</div>
						</td>
						<td><img src="../../images/attention_2.gif" width="15"
							height="12"> 终端业务下发</td>
					</tr>
				</table>
				</TD>
			</TR>
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4">用户设备查询</TH>
					</TR>
					<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: ">

						<TD class=column align="right" nowrap width="15%">业务类型:</TD>
						<TD width="35%" id="sheetTypeTd"><SELECT name="queryServTypeId"
							class="bk" >
							<OPTION value="10">上网业务</OPTION>
							<OPTION value="103">无线业务</OPTION>
							<OPTION value="11">IPTV业务</OPTION>
							<!-- <OPTION value="14">VOIP业务</OPTION>  -->
						</SELECT></TD>
						<TD width="15%" class=column align="right">操作类型:</TD>
						<TD width="35%" id="operationTypeTd">开户</TD>
					</TR>

					<TR bgcolor="#FFFFFF" STYLE="display: ">
						<TD class=column align="right" width="15%">用户帐号:</TD>
						<TD width="35%"><input type="text" name="queryUsername"
							class="bk" 
							value="<s:property value="queryDevSn"/>"></TD>
						<TD class=column align="right" nowrap width="15%">设备序列号:</TD>
						<TD width="35%"><input type="text" name="queryDevSn" class="bk"
							value="<s:property value="queryDevSn"/>"> <font color='red'> *至少输入后六位</font>
						</TD>
					</TR>

					<TR bgcolor="#FFFFFF">
						<TD colspan="4" class=foot align="right" nowrap>
						<button type="button" name="searchBtn" onclick="searchSheetInfo();">查 询</button>
						</TD>
					</TR>

				</TABLE>
				</TD>
			</TR>

			<TR align="left" id="wanConnInfo" STYLE="display: ">
				<TD colspan="4" bgcolor=#999999>
					<div id="div_sheetInfo"></div>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>