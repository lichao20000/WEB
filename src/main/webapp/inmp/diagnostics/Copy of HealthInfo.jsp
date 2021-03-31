<%--
故障处理-健康库信息
Author: Gongsj
Version: 1.0.0
Date: 2009-08-12
--%>

<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>健康库信息</title>
<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.blockUI.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">

parent.unblock1();

$(document).ready(function(){

	var userId = "<s:property value="userId"/>";
	var deviceId = "<s:property value="deviceId"/>";
	$("input[@name='userId']").val(userId);
	$("input[@name='deviceId']").val(deviceId);

});

/**
 * 把新增的页面显示出来
 */
function getWireInfoHtml() {

	parent.block1();
	var deviceId = $("input[@name='deviceId']");

	var url = "<s:url value='/inmp/diagnostics/HealthInfo!gatherWireInfo.action'/>";
	$.post(url,{
		deviceId:deviceId.val()
	},function(ajax){
		if (ajax == "true") {
			alert("采集成功");
			parent.winNavigate(4);
		} else {
			//$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">编辑失败</FONT>");
			alert(ajax+"！");
			parent.unblock1();
		}
	});

	parent.dyniframesize1();

}

/**
 * 把编辑页面显示出来
 */
function editBussHtml(stv) {
alert(stv);
	if (stv == "1") {
		$("td[@id='wanTitle']").html("<font color='red'>编辑上网业务参数</font>");
		$("button[@name='subBtn']").attr({value:" 编辑保存 "});

		var username = $("input[@name='usernameInput']").val($("td[@id='usernameWanId']").html());
		var password = "";//$("input[@name='passwordInput']").val($("td[@id='passwd']").html());
		var servType = $("input[@name='servTypeInput']").val($("td[@id='servTypeIdWanId']").html());
		var wanType = $("input[@name='wanTypeInput']").val($("td[@id='wanTypeWanId']").html());

		var vlanid = $("input[@name='vlanidInput']").val($("td[@id='vlanidWanId']").html());

		if (null == vlanid || vlanid.val() == '' || vlanid.val() == null) {
			//TODO

			var pvcV = $("td[@id='pvcWanId']").html();
			var vpiV = pvcV.substring(4,5);
			var vciV = pvcV.substring(6);

			var vpi = $("input[@name='vpiInput']").val(vpiV);
			var vci = $("input[@name='vciInput']").val(vciV);

			$("td[@id='editVLAN1']").hide();
			$("td[@id='editVLAN2']").hide();
			$("td[@id='editPVC1']").show();
			$("td[@id='editPVC2']").show();

		} else {

			$("td[@id='editPVC1']").hide();
			$("td[@id='editPVC2']").hide();
			$("td[@id='editVLAN1']").show();
			$("td[@id='editVLAN2']").show();
		}

		var ip = $("input[@name='ipInput']").val($("td[@id='ipAddressWanId']").html());
		var mask = $("input[@name='maskInput']").val($("td[@id='ipMaskWanId']").html());
		var gateway = $("input[@name='gatewayInput']").val($("td[@id='gatewayWanId']").html());
		var dns = $("input[@name='dnsInput']").val($("td[@id='dnsWanId']").html());

		$("td[@id='servTypeId']").html("");
		$("td[@id='servTypeId']").html("<SELECT name='servTypeInput' class='bk'><OPTION value='10'>上网</OPTION></SELECT>");
		$("td[@id='editUsername']").html("宽带账号");


		$("tr[@id='voipAddrTR']").hide();
		$("tr[@id='editBussVoipInfoTR']").hide();
		$("tr[@id='bussInfoBtn']").show();

	} else if (stv == "2") {

		$("td[@id='wanTitle']").html("<font color='red'>编辑IPTV业务参数</font>");
		$("button[@name='subBtn']").attr({value:" 编辑保存 "});

		var username = $("input[@name='usernameInput']").val($("td[@id='usernameIptvId']").html());
		var password = "";//$("input[@name='passwordInput']").val($("td[@id='passwd']").html());
		var servType = $("input[@name='servTypeInput']").val($("td[@id='servTypeIdIptvId']").html());
		var wanType = $("input[@name='wanTypeInput']").val($("td[@id='wanTypeIptvId']").html());

		var vlanid = $("input[@name='vlanidInput']").val($("td[@id='vlanidIptvId']").html());

		if (null == vlanid || vlanid.val() == '' || vlanid.val() == null) {
			//TODO

			var pvcV = $("td[@id='pvcIptvId']").html();
			var vpiV = pvcV.substring(4,5);
			var vciV = pvcV.substring(6);

			var vpi = $("input[@name='vpiInput']").val(vpiV);
			var vci = $("input[@name='vciInput']").val(vciV);

			$("td[@id='editVLAN1']").hide();
			$("td[@id='editVLAN2']").hide();
			$("td[@id='editPVC1']").show();
			$("td[@id='editPVC2']").show();

		} else {
			$("td[@id='editPVC1']").hide();
			$("td[@id='editPVC2']").hide();
			$("td[@id='editVLAN1']").show();
			$("td[@id='editVLAN2']").show();
		}

		var ip = $("input[@name='ipInput']").val($("td[@id='ipAddressIptvId']").html());
		var mask = $("input[@name='maskInput']").val($("td[@id='ipMaskIptvId']").html());
		var gateway = $("input[@name='gatewayInput']").val($("td[@id='gatewayIptvId']").html());
		var dns = $("input[@name='dnsInput']").val($("td[@id='dnsIptvId']").html());

		$("td[@id='servTypeId']").html("");
		$("td[@id='servTypeId']").html("<SELECT name='servTypeInput' class='bk'><OPTION value='11'>IPTV</OPTION></SELECT>");

		$("td[@id='editUsername']").html("IPTV账号:");

		$("tr[@id='voipAddrTR']").hide();
		$("tr[@id='editBussVoipInfoTR']").hide();
		$("tr[@id='bussInfoBtn']").show();

	} else {

		$("td[@id='wanTitle']").html("<font color='red'>编辑VOIP业务参数</font>");
		$("button[@name='subBtn']").attr({value:" 编辑保存 "});

		var username = $("input[@name='usernameInput']").val($("td[@id='usernameVoipId']").html());
		var password = "";//$("input[@name='passwordInput']").val($("td[@id='passwd']").html());
		var servType = $("input[@name='servTypeInput']").val($("td[@id='servTypeIdVoipId']").html());
		var wanType = $("input[@name='wanTypeInput']").val($("td[@id='wanTypeVoipId']").html());

		var vlanid = $("input[@name='vlanidInput']").val($("td[@id='vlanidVoipId']").html());

		if (null == vlanid || vlanid.val() == '' || vlanid.val() == null) {
			//TODO
			var pvcV = $("td[@id='pvcVoipId']").html();
			var vpiV = pvcV.substring(4,5);
			var vciV = pvcV.substring(6);

			var vpi = $("input[@name='vpiInput']").val(vpiV);
			var vci = $("input[@name='vciInput']").val(vciV);

			$("td[@id='editVLAN1']").hide();
			$("td[@id='editVLAN2']").hide();
			$("td[@id='editPVC1']").show();
			$("td[@id='editPVC2']").show();

		} else {
			$("td[@id='editPVC1']").hide();
			$("td[@id='editPVC2']").hide();
			$("td[@id='editVLAN1']").show();
			$("td[@id='editVLAN2']").show();
		}

		var ip = $("input[@name='ipInput']").val($("td[@id='ipAddressVoipId']").html());
		var mask = $("input[@name='maskInput']").val($("td[@id='ipMaskVoipId']").html());
		var gateway = $("input[@name='gatewayInput']").val($("td[@id='gatewayVoipId']").html());
		var dns = $("input[@name='dnsInput']").val($("td[@id='dnsVoipId']").html());


		$("input[@name='proxServerInput']").val($("td[@id='proxServerId']").html());
		$("input[@name='proxPortInput']").val($("td[@id='proxPortId']").html());
		$("input[@name='proxServer2Input']").val($("td[@id='proxServer2Id']").html());
		$("input[@name='proxPort2Input']").val($("td[@id='proxPort2Id']").html());
		$("input[@name='regiServInput']").val($("td[@id='regiServId']").html());
		$("input[@name='regiPortInput']").val($("td[@id='regiPortId']").html());
		$("input[@name='standRegiServInput']").val($("td[@id='standRegiServId']").html());
		$("input[@name='standRegiPortInput']").val($("td[@id='standRegiPortId']").html());
		$("input[@name='outBoundProxyInput']").val($("td[@id='outBoundProxyId']").html());
		$("input[@name='outBoundPortInput']").val($("td[@id='outBoundPortId']").html());
		$("input[@name='standOutBoundProxyInput']").val($("td[@id='standOutBoundProxyId']").html());
		$("input[@name='standOutBoundPortInput']").val($("td[@id='standOutBoundPortId']").html());

		$("td[@id='servTypeId']").html("");
		$("td[@id='servTypeId']").html("<SELECT name='servTypeInput' class='bk'><OPTION value='14'>VOIP</OPTION></SELECT>");

		$("td[@id='editUsername']").html("号码");

		$("tr[@id='editBussVoipInfoTR']").show();
		$("tr[@id='voipAddrTR']").show();
		$("tr[@id='bussInfoBtn']").hide();
	}

	$("tr[@id='editBussInfoTR']").show();
	$("tr[@id='editHealthInfoTR']").hide();


	$("button[@name='subBtn']").click(function(){editBuss(stv);});
	alert(3);
	parent.dyniframesize1();
	alert(4);

}

/**
 * 编辑WAN连接
 */
function editBuss(stv) {

	var userId = $("input[@name='userId']").val();
	var username = $("input[@name='usernameInput']").val();
	var password = $("input[@name='passwordInput']").val();
	var servType = $("select[@name='servTypeInput']").val();
	var wanType = $("select[@name='wanTypeInput']").val();

	var vpi = $("input[@name='vpiInput']").val();
	var vci = $("input[@name='vciInput']").val();
	if(vci == '') {
		vci = 0;
	}
	var vlanid = $("input[@name='vlanidInput']").val();

	var ip = $("input[@name='ipInput']").val();
	var mask = $("input[@name='maskInput']").val();
	var gateway = $("input[@name='gatewayInput']").val();
	var dns = $("input[@name='dnsInput']").val();
	//var bindport = $("input[@name='bindportInput']").val();

	var proxServer = $("input[@name='proxServerInput']").val();
	var proxPort = $("input[@name='proxPortInput']").val();
	var proxServer2 = $("input[@name='proxServer2Input']").val();
	var proxPort2 = $("input[@name='proxPort2Input']").val();
	var regiServ = $("input[@name='regiServInput']").val();
	var regiPort = $("input[@name='regiPortInput']").val();
	var standRegiServ = $("input[@name='standRegiServInput']").val();
	var standRegiPort = $("input[@name='standRegiPortInput']").val();
	var outBoundProxy = $("input[@name='outBoundProxyInput']").val();
	var outBoundPort = $("input[@name='outBoundPortInput']").val();
	var standOutBoundProxy = $("input[@name='standOutBoundProxyInput']").val();
	var standOutBoundPort = $("input[@name='standOutBoundPortInput']").val();

	var lan1 = $("input[@name='lan1'][@checked]");
	var lan2 = $("input[@name='lan2'][@checked]");
	var lan3 = $("input[@name='lan3'][@checked]");
	var lan4 = $("input[@name='lan4'][@checked]");
	var lan5 = $("input[@name='lan5'][@checked]");
	var lan6 = $("input[@name='lan6'][@checked]");
	var lan7 = $("input[@name='lan7'][@checked]");
	var lan8 = $("input[@name='lan8'][@checked]");

	var bindPort = "";

	if (lan1.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan1.val()+",";
	}

	if (lan2.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan2.val()+",";
	}

	if (lan3.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan3.val()+",";
	}

	if (lan4.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan4.val()+",";
	}

	if (lan5.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan5.val()+",";
	}

	if (lan6.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan6.val()+",";
	}

	if (lan7.val() == undefined) {
		bindPort += "0,";
	} else {
		bindPort += lan7.val()+",";
	}

	if (lan8.val() == undefined) {
		bindPort += "0";
	} else {
		bindPort += lan8.val();
	}

	bindPort = bindPort.replace(new RegExp(",0","g"),"");
	bindPort = bindPort.replace(new RegExp("0,","g"),"");

	var deviceId = $("input[@name='deviceId']").val();

	var url = "<s:url value='/inmp/diagnostics/HealthInfo!editBussInfo.action'/>";

	$.post(url,{
		businessId:stv,
		deviceId:deviceId,
		userId:userId,
		username:username,
		password:password,
		servType:servType,
		wanType:wanType,
		vpi:vpi,
		vci:vci,
		vlanid:vlanid,
		ip:ip,
		mask:mask,
		gateway:gateway,
		dns:dns,
		bindport:bindPort,
		proxServer:proxServer,
		proxPort:proxPort,
		proxServer2:proxServer2,
		proxPort2:proxPort2,
		regiServ:regiServ,
		regiPort:regiPort,
		standRegiServ:standRegiServ,
		standRegiPort:standRegiPort,
		outBoundProxy:outBoundProxy,
		outBoundPort:outBoundPort,
		standOutBoundProxy:standOutBoundProxy,
		standOutBoundPort:standOutBoundPort

	},function(ajax){

		//$("tr[@id='resultTr']").show();
		//$("div[@id='result']").html("");
		if (ajax == "true")
		{
			//$("div[@id='result']").append("编辑成功");
			alert("编辑成功");
			parent.winNavigate(4);
		} else {
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">编辑失败</FONT>");
			//alert("编辑失败");
		}

		parent.dyniframesize1();
	});

}

/**
 * 编辑线路信息
 */
function editHealthHtml() {
	$("td[@id='wanTitle']").html("<font color='red'>编辑健康库信息</font>");
	$("button[@name='subBtn']").attr({value:" 编辑保存 "});


	var upAttenTemp = $("td[@id='upAttenId']").html();
	if (upAttenTemp != null && upAttenTemp != undefined) {
		$("input[@name='upAttenMinInput']").val(upAttenTemp.substring(0, upAttenTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='upAttenMaxInput']").val(upAttenTemp.substring(upAttenTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	var downAttenTemp = $("td[@id='downAttenId']").html();
	if (downAttenTemp != null && downAttenTemp != undefined) {
		$("input[@name='downAttenMinInput']").val(downAttenTemp.substring(0, downAttenTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='downAttenMaxInput']").val(downAttenTemp.substring(downAttenTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	var upMaxRateTemp = $("td[@id='upMaxRateId']").html();
	if (upMaxRateTemp != null && upMaxRateTemp != undefined) {
		$("input[@name='upMaxRateMinInput']").val(upMaxRateTemp.substring(0, upMaxRateTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='upMaxRateMaxInput']").val(upMaxRateTemp.substring(upMaxRateTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	var downMaxRateTemp = $("td[@id='downMaxRateId']").html();
	if (downMaxRateTemp != null && downMaxRateTemp != undefined) {
		$("input[@name='downMaxRateMinInput']").val(downMaxRateTemp.substring(0, downMaxRateTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='downMaxRateMaxInput']").val(downMaxRateTemp.substring(downMaxRateTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	var upNoiseTemp = $("td[@id='upNoiseId']").html();
	if (upNoiseTemp != null && upNoiseTemp != undefined) {
		$("input[@name='upNoiseMinInput']").val(upNoiseTemp.substring(0, upNoiseTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='upNoiseMaxInput']").val(upNoiseTemp.substring(upNoiseTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	var downNoiseTemp = $("td[@id='downNoiseId']").html();
	if (downNoiseTemp != null && downNoiseTemp != undefined) {
		$("input[@name='downNoiseMinInput']").val(downNoiseTemp.substring(0, downNoiseTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='downNoiseMaxInput']").val(downNoiseTemp.substring(downNoiseTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	var interDepthTemp = $("td[@id='interDepthId']").html();
	if (interDepthTemp != null && interDepthTemp != undefined) {
		$("input[@name='interDepthMinInput']").val(interDepthTemp.substring(0, interDepthTemp.indexOf('—')).replace(/(^\s*)|(\s*$)/g, ""));
		$("input[@name='interDepthMaxInput']").val(interDepthTemp.substring(interDepthTemp.indexOf('—')+1).replace(/(^\s*)|(\s*$)/g, ""));
	}

	//$("input[@name='datePathInput']").val($("td[@id='datePathId']").html());

	//$("input[@name='powerlevelInput']").val($("td[@id='powerlevelId']").html());
	$("input[@name='powervalueInput']").val($("td[@id='powervalueId']").html());

	$("tr[@id='editBussInfoTR']").hide();
	$("tr[@id='editBussVoipInfoTR']").hide();
	$("tr[@id='editHealthInfoTR']").show();

	$("button[@name='subBtn']").click(function(){editHealth();});

	parent.dyniframesize1();

}

/**
 * 健康信息编辑入库
 */
function editHealth() {
	var upAttenMin = $("input[@name='upAttenMinInput']").val();
	var upAttenMax = $("input[@name='upAttenMaxInput']").val();
	var downAttenMin = $("input[@name='downAttenMinInput']").val();
	var downAttenMax = $("input[@name='downAttenMaxInput']").val();
	var upMaxRateMin = $("input[@name='upMaxRateMinInput']").val();
	var upMaxRateMax = $("input[@name='upMaxRateMaxInput']").val();
	var downMaxRateMin = $("input[@name='downMaxRateMinInput']").val();

	var downMaxRateMax = $("input[@name='downMaxRateMaxInput']").val();
	var upNoiseMin = $("input[@name='upNoiseMinInput']").val();
	var upNoiseMax = $("input[@name='upNoiseMaxInput']").val();
	var downNoiseMin = $("input[@name='downNoiseMinInput']").val();
	var downNoiseMax = $("input[@name='downNoiseMaxInput']").val();
	var interDepthMin = $("input[@name='interDepthMinInput']").val();
	var interDepthMax = $("input[@name='interDepthMaxInput']").val();
	var datePath = $("select[@name='datePathInput']").val();

	//var powerlevel = $("select[@name='powerlevelInput']").val();
	var powervalue = $("input[@name='powervalueInput']").val();

	var deviceId = $("input[@name='deviceId']").val();

	var url = "<s:url value='/inmp/diagnostics/HealthInfo!editHealthInfo.action'/>";

	$.post(url,{
		deviceId:deviceId,
		upAttenMin:upAttenMin,
		upAttenMax:upAttenMax,
		downAttenMin:downAttenMin,
		downAttenMax:downAttenMax,
		upMaxRateMin:upMaxRateMin,
		upMaxRateMax:upMaxRateMax,
		downMaxRateMin:downMaxRateMin,
		downMaxRateMax:downMaxRateMax,
		upNoiseMin:upNoiseMin,
		upNoiseMax:upNoiseMax,
		downNoiseMin:downNoiseMin,
		downNoiseMax:downNoiseMax,
		interDepthMin:interDepthMin,
		interDepthMax:interDepthMax,
		datePath:datePath,
		//powerlevel:powerlevel,
		powervalue:powervalue

	},function(ajax){

		//$("tr[@id='resultTr']").show();
		//$("div[@id='result']").html("");
		if (ajax == "true")
		{
			//$("div[@id='result']").append("编辑成功");
			alert("编辑成功");
			parent.winNavigate(4);
		} else {
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">编辑失败</FONT>");
			//alert("编辑失败");
		}

		parent.dyniframesize1();

	});
}

/**
 * 获得业务相关信息
 */
function getBussHtml() {
	var userId = $("input[@name='userId']").val();

	var url = "<s:url value='/inmp/diagnostics/HealthInfo!getBussInfo.action'/>";

	$.post(url,{
		userId:userId
	},function(ajax){

		if (ajax == "true") {
			alert("更新成功");
		} else {
			alert("更新失败");
		}

		parent.dyniframesize1();
	});
}

/**
 * 检查表单数据合法性
 */
function checkForm() {
	return true;
}

/**
 * IP_Routed或PPPoE_Bridged
 */
function chgWanType(e){
	if("3"==e){
    	//$("tr[@id='ppp_type_1']").hide();
		$("tr[@id='ip_type_1']").show();
		$("tr[@id='ip_type_2']").show();
    } else if("2"==e){
    	//$("tr[@id='ppp_type_1']").show();
		$("tr[@id='ip_type_1']").hide();
		$("tr[@id='ip_type_2']").hide();
    } else {
    	//$("tr[@id='ppp_type_1']").hide();
		$("tr[@id='ip_type_1']").hide();
		$("tr[@id='ip_type_2']").hide();
    }

	parent.dyniframesize1();
}

/**
 * PPP或IP
 */
function chgSessionType(e) {
	var ipType = $("select[@name='ipType']").val();
	var connType = $("select[@name='connType']").val();

	if ("1" == e) {
		//PPP方式
		$("tr[@id='ip_type_2']").hide();
		$("tr[@id='ip_type_3']").hide();

		if (connType == 'IP_Routed') {
			$("tr[@id='ppp_type_2']").show();
		} else {
			$("tr[@id='ppp_type_2']").hide();
		}


		$("select[@name='connType']").show();
		$("select[@name='ipType']").hide();

	} else if ("2" == e && "Static" == ipType) {
		$("tr[@id='ip_type_2']").show();
		$("tr[@id='ip_type_3']").show();

		$("select[@name='ipType']").show();

		$("select[@name='connType']").hide();
		$("tr[@id='ppp_type_2']").hide();
	}

	parent.dyniframesize1();
}

/**
 * DHCP或Static
 */
function chgIpType(e) {
	var sessType = $("select[@name='sessionType']").val();

	if ("DHCP" == e && "2" == sessType) {
		$("tr[@id='ip_type_2']").hide();
		$("tr[@id='ip_type_3']").hide();
	} else if ("Static" == e && "2" == sessType) {

		$("tr[@id='ip_type_2']").show();
		$("tr[@id='ip_type_3']").show();

	}

	parent.dyniframesize1();
}



</script>


</head>

<body>



<FORM NAME="frm" METHOD="post" action="">
<input type="hidden" name="userId" value="" />
<input type="hidden" name="deviceId" value="" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<tr>
		<TD>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" >

				<tr align="left" height="25">
					<td colspan="3" class="green_title">
						健康库信息
					</td>
				</tr>
				<tr align="left" id="wireInfo_detail" STYLE="display:">
					<td colspan="4"  bgcolor=#999999>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
							<tr align="left">
								 <td colspan="4" align="right" class="column">
									<button name='addImg' onclick="getWireInfoHtml()">重新采集</button> &nbsp;

									<button name='addImg' onclick="editHealthHtml()">编辑</button>
								</td>
							</tr>

							 <s:if test="null == wireInfoObjArr || wireInfoObjArr.length ==0">
							 	<tr align="left" bgcolor="#FFFFFF">
							 		<TD colspan="4" align="left">采集线路：<s:property value="corbaMsg"/></TD>
							 	</tr>
							 </s:if>
							 <s:else>
							 	<s:iterator value="wireInfoObjArr" var="wideNetInfo">

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">线路状态:</TD>
									<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
										<TD align="left" width="35%">-</TD>
									</s:if>
									<s:else>
										<TD align="left" width="35%"><s:property value="status"/></TD>
									</s:else>

									<TD width="15%" class="column" align="right">线路协议:</TD>
									<s:if test="#wideNetInfo.modType=='N/A' || #wideNetInfo.modType=='null' ">
										<TD align="left" width="35%">-</TD>
									</s:if>
									<s:else>
										<TD align="left" width="35%"><s:property value="modType"/></TD>
									</s:else>
								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">上行线路衰减(dB):</TD>
									<TD width="35%" id="upAttenId">
										<s:property value="upAttenMin"/> — <s:property value="upAttenMax"/>
									</TD>

									<TD width="15%" class="column" align="right">下行线路衰减(dB):</TD>
									<TD width="35%" id="downAttenId">
										<s:property value="downAttenMin"/> — <s:property value="downAttenMax"/>
									</TD>
								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">上行速率(Kbps):</TD>
									<TD align="left" width="35%"  id="upMaxRateId">
										<s:property value="upMaxRateMin"/> — <s:property value="upMaxRateMax"/>
									</TD>

									<TD width="15%" class="column" align="right">下行速率(Kbps):</TD>
									<TD align="left" width="35%" id="downMaxRateId">
										<s:property value="downMaxRateMin"/> — <s:property value="downMaxRateMax"/>
									</TD>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">上行噪声裕量(dB):</TD>
									<TD width="35%" id="upNoiseId">
										<s:property value="upNoiseMin"/> — <s:property value="upNoiseMax"/>
									</TD>

									<TD class="column" align="right" width="15%">下行噪声裕量(dB):</TD>
									<TD width="35%" id="downNoiseId">
										<s:property value="downNoiseMin"/> — <s:property value="downNoiseMax"/>
									</TD>
								</TR>

								<TR bgcolor="#FFFFFF">
									<TD class="column" align="right"  width="15%">交织深度:</TD>
									<TD width="35%" id="interDepthId">
										<s:property value="interDepthMin"/> — <s:property value="interDepthMax"/>
									</TD>

									<TD width="15%" class="column" align="right">数据路径:</TD>

									<s:if test="#wideNetInfo.datePath=='Fast'">
										<TD align="left" id="datePathId" width="35%">快速</TD>
									</s:if>
									<s:elseif test="#wideNetInfo.datePath=='Interleaved'">
							            <TD align="left" id="datePathId" width="35%">交织（低错误率）</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="datePathId" width="35%">未知</TD>
							        </s:else>

								</TR>
								</s:iterator>

							 </s:else>


							<s:if test="null == wlanInfoObj">
							 	<tr align="left" bgcolor="#FFFFFF">
							 		<TD colspan="4" align="left">采集无线：<s:property value="corbaMsg"/>！</TD>
							 	</tr>
							 </s:if>
							 <s:else>
							 	<TR bgcolor="#FFFFFF">
									<TD class="column" align="right"  width="15%">WLAN功率级别:</TD>
									<TD width="35%" id="powerlevelId">
										<s:property value="wlanInfoObj.powerlevel"/>
									</TD>

									<TD width="15%" class="column" align="right">WLAN输出功率:</TD>
									<TD align="left" width="35%" id="powervalueId"><s:property value="wlanInfoObj.powervalue"/></TD>

								</TR>
							 </s:else>

						</TABLE>
					</td>
				</tr>

				<TR>
					<TD HEIGHT=20>&nbsp;</TD>
				</TR>

				<tr align="left" height="25">
					<td colspan="3" class="green_title">
						上网业务参数
					</td>
				</tr>

				<tr align="left" id="bussInfo_detail" STYLE="display:">
					<td colspan="4"  bgcolor=#999999>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >


							<s:if test="null == wanBussObj">
								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="left" colspan="4">该设备暂无上网参数信息！</TD>

								</TR>
							</s:if>
							<s:else>
								<tr align="left">
								 <td colspan="4" align="right" class="column">
									<!-- <button name='getWanBtn' onclick="getBussHtml()">重新获取上网参数</button> &nbsp;&nbsp; -->
									<button name='editWanBtn' onclick="editBussHtml(1)">编辑</button>
								</td>
								</tr>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">宽带账号:</TD>
										<s:if test="#wanBussObj.username=='N/A' || #wanBussObj.username=='null' ">
											<TD align="left" width="35%" id="usernameWanId">-</TD>
										</s:if>
										 <s:else>
								            <TD align="left" width="35%" id="usernameWanId"><s:property value="wanBussObj.username"/></TD>
								        </s:else>

									<TD width="15%" class="column" align="right">密码:</TD>
										<s:if test="#wanBussObj.passwd=='N/A' || #wanBussObj.passwd=='null' ">
											<TD align="left" width="35%" id="passwd">-</TD>
										</s:if>
										 <s:else>
								            <TD align="left" width="35%" id="passwd">******</TD>
								        </s:else>

								</TR>
								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">业务类型:</TD>
									<s:if test="wanBussObj.servTypeId==11">
										<TD align="left" id="servTypeIdWanId" width="35%">IPTV</TD>
									</s:if>
									<s:elseif test="wanBussObj.servTypeId==14">
							            <TD align="left" id="servTypeIdWanId" width="35%">VOIP</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="servTypeIdWanId" width="35%">上网</TD>
							        </s:else>

									<TD width="15%" class="column" align="right">连接类型:</TD>
									<s:if test="wanBussObj.wanType==4">
										<TD align="left" id="wanTypeWanId" width="35%">DHCP</TD>
									</s:if>
									<s:elseif test="wanBussObj.wanType==3">
							            <TD align="left" id="wanTypeWanId" width="35%">静态IP</TD>
							        </s:elseif>
							        <s:elseif test="wanBussObj.wanType==2">
							            <TD align="left" id="wanTypeWanId" width="35%">路由</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="wanType" width="35%">桥接</TD>
							        </s:else>


								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD class="column" align="right"  width="15%">PVC/VLAN配置:</TD>
									<s:if test="wanBussObj.vlanid=='N/A' || wanBussObj.vlanid=='null' || wanBussObj.vlanid==0">
										<TD align="left" id="pvcWanId" width="35%" colspan="3">PVC:<s:property value="wanBussObj.vpiid"/>/<s:property value="wanBussObj.vciid"/></TD>
										<TD align="left" id="vlanidWanId" width="35%" style="display:none"></TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="vlanidWanId" width="35%" colspan="3"><s:property value="wanBussObj.vlanid"/></TD>
							            <TD align="left" id="pvcWanId" width="35%" style="display:none"></TD>
							        </s:else>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">IP:</TD>
									<s:if test="#wanBussObj.ipAddress=='N/A' || #wanBussObj.ipAddress=='null' ">
										<TD align="left" id="ipAddressWanId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="ipAddressWanId" width="35%"><s:property value="wanBussObj.ipAddress"/></TD>
							        </s:else>

									<TD width="15%" class="column" align="right">子网掩码:</TD>
									<s:if test="#wanBussObj.ipMask=='N/A' || #wanBussObj.ipMask=='null' ">
										<TD align="left" id="ipMaskWanId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="ipMaskWanId" width="35%"><s:property value="wanBussObj.ipMask"/></TD>
							        </s:else>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">网关:</TD>
									<s:if test="#wanBussObj.gateway=='N/A' || #wanBussObj.gateway=='null' ">
										<TD align="left" id="gatewayWanId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="gatewayWanId" width="35%"><s:property value="wanBussObj.gateway"/></TD>
							        </s:else>

									<TD class="column" align="right" width="15%">DNS:</TD>
									<s:if test="#wanBussObj.dns=='N/A' || #wanBussObj.dns=='null' ">
										<TD align="left" id="dnsWanId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="dnsWanId" width="35%"><s:property value="wanBussObj.dns"/></TD>
							        </s:else>
								</TR>

								<TR bgcolor="#FFFFFF">
									<TD class="column" align="right"  width="15%">绑定端口:</TD>
									<s:if test="wanBussObj.bindPort=='N/A' || wanBussObj.bindPort=='null' ">
									<TD align="left" id="bindPortWanId" width="35%" colspan="3"></TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="bindPortWanId" width="35%" colspan="3"><s:property value="wanBussObj.bindPort"/></TD>
							        </s:else>


								</TR>
							</s:else>




						</TABLE>
					</td>
				</tr>

				<TR>
					<TD HEIGHT=20>&nbsp;</TD>
				</TR>

				<tr align="left" height="25">
					<td colspan="3" class="green_title">
						IPTV业务参数
					</td>
				</tr>

				<tr align="left" id="bussInfo_detail" STYLE="display:">
					<td colspan="4"  bgcolor=#999999>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >

							<s:if test="null == iptvBussObj">
								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="left" colspan="4">该设备暂无IPTV参数信息！</TD>
								</TR>
							</s:if>
							<s:else>
								<tr align="left">
									 <td colspan="4" align="right" class="column">
										<!-- <button name='addImg' onclick="getBussHtml()">重新获取IPTV参数</button> &nbsp;&nbsp;-->
										<button name='addImg' onclick="editBussHtml(2)">编辑</button>
									</td>
								</tr>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">IPTV账号:</TD>
										<s:if test="#iptvBussObj.username=='N/A' || #iptvBussObj.username=='null' ">
											<TD align="left" width="35%" id="usernameIptvId">-</TD>
										</s:if>
										 <s:else>
								            <TD align="left" width="35%" id="usernameIptvId"><s:property value="iptvBussObj.username"/></TD>
								        </s:else>

									<TD width="15%" class="column" align="right">密码:</TD>
										<s:if test="#iptvBussObj.passwd=='N/A' || #iptvBussObj.passwd=='null' ">
											<TD align="left" width="35%">-</TD>
										</s:if>
										 <s:else>
								            <TD align="left" width="35%">******</TD>
								        </s:else>

								</TR>
								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">业务类型:</TD>
									<s:if test="iptvBussObj.servTypeId==11">
										<TD align="left" id="servTypeIdIptvId" width="35%">IPTV</TD>
									</s:if>
									<s:elseif test="iptvBussObj.servTypeId=='14'">
							            <TD align="left" id="servTypeIdIptvId" width="35%">VOIP</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="servTypeIdIptvId" width="35%">上网</TD>
							        </s:else>

									<TD width="15%" class="column" align="right">连接类型:</TD>
									<s:if test="iptvBussObj.wanType==4">
										<TD align="left" id="wanTypeIptvId" width="35%">DHCP</TD>
									</s:if>
									<s:elseif test="iptvBussObj.wanType==3">
							            <TD align="left" id="wanTypeIptvId" width="35%">静态IP</TD>
							        </s:elseif>
							        <s:elseif test="iptvBussObj.wanType==2">
							            <TD align="left" id="wanTypeIptvId" width="35%">路由</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="wanType" width="35%">桥接</TD>
							        </s:else>


								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD class="column" align="right"  width="15%">PVC/VLAN:</TD>
									<s:if test="iptvBussObj.vlanid=='N/A' || iptvBussObj.vlanid=='null' || iptvBussObj.vlanid==0">
										<TD align="left" id="pvcIptvId" width="35%" colspan="3">PVC:<s:property value="iptvBussObj.vpiid"/>/<s:property value="iptvBussObj.vciid"/></TD>
										<TD align="left" id="vlanidIptvId" width="35%" style="display:none"></TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="vlanidIptvId" width="35%" colspan="3"><s:property value="iptvBussObj.vlanid"/></TD>
							            <TD align="left" id="pvcIptvId" width="35%" style="display:none"></TD>
							        </s:else>


								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">IP:</TD>
									<s:if test="#iptvBussObj.ipAddress=='N/A' || #iptvBussObj.ipAddress=='null' ">
										<TD align="left" id="ipAddressIptvId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="ipAddressIptvId" width="35%"><s:property value="iptvBussObj.ipAddress"/></TD>
							        </s:else>

									<TD width="15%" class="column" align="right">子网掩码:</TD>
									<s:if test="#iptvBussObj.ipMask=='N/A' || #iptvBussObj.ipMask=='null' ">
										<TD align="left" id="ipMaskIptvId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="ipMaskIptvId" width="35%"><s:property value="iptvBussObj.ipMask"/></TD>
							        </s:else>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">网关:</TD>
									<s:if test="#iptvBussObj.gateway=='N/A' || #iptvBussObj.gateway=='null' ">
										<TD align="left" id="gatewayIptvId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="gatewayIptvId" width="35%"><s:property value="iptvBussObj.gateway"/></TD>
							        </s:else>

									<TD class="column" align="right" width="15%">DNS:</TD>
									<s:if test="#iptvBussObj.dns=='N/A' || #iptvBussObj.dns=='null' ">
										<TD align="left" id="dnsIptvId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="dnsIptvId" width="35%"><s:property value="iptvBussObj.dns"/></TD>
							        </s:else>
								</TR>

								<TR bgcolor="#FFFFFF">
									<TD class="column" align="right"  width="15%">绑定端口:</TD>
									<s:if test="iptvBussObj.bindPort=='N/A' || iptvBussObj.bindPort=='null' ">
									<TD align="left" id="bindPortIptvId" width="35%" colspan="3"></TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="bindPortIptvId" width="35%" colspan="3"><s:property value="iptvBussObj.bindPort"/></TD>
							        </s:else>


								</TR>
							</s:else>


						</TABLE>
					</td>
				</tr>


				<TR>
					<TD HEIGHT=20>&nbsp;</TD>
				</TR>

				<tr align="left" height="25">
					<td colspan="3" class="green_title">
						VOIP业务参数
					</td>
				</tr>

				<tr align="left" id="bussInfo_detail" STYLE="display:">
					<td colspan="4"  bgcolor=#999999>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
							<s:if test="null == voipBussObj">
								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="left" colspan="4">该设备暂无VOIP参数信息！</TD>
								</TR>
							</s:if>
							<s:else>
								<tr align="left">
									<td colspan="4" align="right" class="column">
										<!-- <button name='addImg' onclick="getBussHtml()">重新获取VOIP参数</button> &nbsp;&nbsp;-->
										<button name='addImg' onclick="editBussHtml(3)">编辑</button>
									</td>
								</tr>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">号码:</TD>
										<s:if test="#voipBussObj.username=='N/A' || #voipBussObj.username=='null' ">
											<TD align="left" width="35%" id="usernameVoipId">-</TD>
										</s:if>
										 <s:else>
								            <TD align="left" width="35%" id="usernameVoipId"><s:property value="voipBussObj.username"/></TD>
								        </s:else>

									<TD width="15%" class="column" align="right">密码:</TD>
										<s:if test="#voipBussObj.passwd=='N/A' || #voipBussObj.passwd=='null' ">
											<TD align="left" width="35%">-</TD>
										</s:if>
										 <s:else>
								            <TD align="left" width="35%">******</TD>
								        </s:else>

								</TR>
								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">业务类型:</TD>
									<s:if test="voipBussObj.servTypeId==11">
										<TD align="left" id="servTypeIdVoipId" width="35%">IPTV</TD>
									</s:if>
									<s:elseif test="voipBussObj.servTypeId==14">
							            <TD align="left" id="servTypeIdVoipId" width="35%">VOIP</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="servTypeIdVoipId" width="35%">上网</TD>
							        </s:else>

									<TD width="15%" class="column" align="right">连接类型:</TD>
									<s:if test="voipBussObj.wanType==4">
										<TD align="left" id="wanTypeVoipId" width="35%">DHCP</TD>
									</s:if>
									<s:elseif test="voipBussObj.wanType==3">
							            <TD align="left" id="wanTypeVoipId" width="35%">静态IP</TD>
							        </s:elseif>
							        <s:elseif test="voipBussObj.wanType==2">
							            <TD align="left" id="wanTypeVoipId" width="35%">路由</TD>
							        </s:elseif>
									 <s:else>
							            <TD align="left" id="wanTypeVoipId" width="35%">桥接</TD>
							        </s:else>


								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD class="column" align="right"  width="15%">PVC/VLAN:</TD>
									<s:if test="voipBussObj.vlanid=='N/A' || voipBussObj.vlanid=='null' || voipBussObj.vlanid==0">
										<TD align="left" id="pvcVoipId" width="35%" colspan="3">PVC:<s:property value="voipBussObj.vpiid"/>/<s:property value="voipBussObj.vciid"/></TD>
										<TD align="left" id="vlanidVoipId" width="35%" style="display:none"></TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="vlanidVoipId" width="35%" colspan="3"><s:property value="voipBussObj.vlanid"/></TD>
							            <TD align="left" id="vlanidVoipId" width="35%" style="display:none"></TD>
							        </s:else>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">IP:</TD>
									<s:if test="#voipBussObj.ipAddress=='N/A' || #voipBussObj.ipAddress=='null' ">
										<TD align="left" id="ipAddressIptvId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="ipAddressVoipId" width="35%"><s:property value="voipBussObj.ipAddress"/></TD>
							        </s:else>

									<TD width="15%" class="column" align="right">子网掩码:</TD>
									<s:if test="#voipBussObj.ipMask=='N/A' || #voipBussObj.ipMask=='null' ">
										<TD align="left" id="ipMaskVoipId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="ipMaskVoipId" width="35%"><s:property value="voipBussObj.ipMask"/></TD>
							        </s:else>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">

									<TD class="column" align="right"  width="15%">网关:</TD>
									<s:if test="#voipBussObj.gateway=='N/A' || #voipBussObj.gateway=='null' ">
										<TD align="left" id="gatewayVoipId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="gatewayVoipId" width="35%"><s:property value="voipBussObj.gateway"/></TD>
							        </s:else>

									<TD class="column" align="right" width="15%">DNS:</TD>
									<s:if test="#voipBussObj.dns=='N/A' || #voipBussObj.dns=='null' ">
										<TD align="left" id="dnsVoipId" width="35%">-</TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="dnsVoipId" width="35%"><s:property value="voipBussObj.dns"/></TD>
							        </s:else>
								</TR>


								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">主地址:</TD>
									<TD align="left" id="proxServerId" width="35%"><s:property value="voipAddrObj.proxServer"/></TD>

									<TD width="15%" class="column" align="right">主端口:</TD>
									<TD align="left" id="proxPortId" width="35%"><s:property value="voipAddrObj.proxPort"/></TD>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">备用地址:</TD>
									<TD align="left" id="proxServer2Id" width="35%"><s:property value="voipAddrObj.proxServer2"/></TD>

									<TD width="15%" class="column" align="right">备用端口:</TD>
									<TD align="left" id="proxPort2Id" width="35%"><s:property value="voipAddrObj.proxPort2"/></TD>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">注册地址:</TD>
									<TD align="left" id="regiServId" width="35%"><s:property value="voipAddrObj.regiServ"/></TD>

									<TD width="15%" class="column" align="right">注册端口:</TD>
									<TD align="left" id="regiPortId" width="35%"><s:property value="voipAddrObj.regiPort"/></TD>
								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">备用注册地址:</TD>
									<TD align="left" id="standRegiServId" width="35%"><s:property value="voipAddrObj.standRegiServ"/></TD>

									<TD width="15%" class="column" align="right">备用注册端口:</TD>
									<TD align="left" id="standRegiPortId" width="35%"><s:property value="voipAddrObj.standRegiPort"/></TD>

								</TR>


								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">外部绑定地址:</TD>
									<TD align="left" id="outBoundProxyId" width="35%"><s:property value="voipAddrObj.outBoundProxy"/></TD>

									<TD width="15%" class="column" align="right">外部绑定端口:</TD>
									<TD align="left" id="outBoundPortId" width="35%"><s:property value="voipAddrObj.outBoundPort"/></TD>

								</TR>

								<TR bgcolor="#FFFFFF" STYLE="display:">
									<TD width="15%" class="column" align="right">备用外部绑定地址:</TD>
									<TD align="left" id="standOutBoundProxyId" width="35%"><s:property value="voipAddrObj.standOutBoundProxy"/></TD>

									<TD width="15%" class="column" align="right">备用外部绑定端口:</TD>
									<TD align="left" id="standOutBoundPortId" width="35%"><s:property value="voipAddrObj.standOutBoundPort"/></TD>

								</TR>

								<TR bgcolor="#FFFFFF">
									<TD class="column" align="right"  width="15%">绑定端口:</TD>
									<s:if test="voipBussObj.bindPort=='N/A' || voipBussObj.bindPort=='null' ">
									<TD align="left" id="bindPortVoipId" width="35%" colspan="3"></TD>
									</s:if>
									 <s:else>
							            <TD align="left" id="bindPortVoipId" width="35%" colspan="3"><s:property value="voipBussObj.bindPort"/></TD>
							        </s:else>

								</TR>
							</s:else>


						</TABLE>
					</td>
				</tr>


			</table>
		</TD>
	</tr>

	<tr height="20">
		<td colspan="1"  width="15" class="column">
		</td>
	</tr>

	<tr align="left" id="editHealthInfoTR" STYLE="display:none">
		<td colspan="4"  bgcolor=#999999>

			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<tr align="left">
					<td colspan="4" class="green_title" id="wanTitle"></td>
				</tr>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">上行线路衰减(dB):</TD>
					<TD width="35%">
						<input type="text" name="upAttenMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="upAttenMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

					<TD width="15%" class="column" align="right">下行线路衰减(dB):</TD>
					<TD width="35%">
						<input type="text" name="downAttenMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="downAttenMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">上行速率(Kbps):</TD>
					<TD width="35%">
						<input type="text" name="upMaxRateMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="upMaxRateMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

					<TD width="15%" class="column" align="right">下行速率(Kbps):</TD>
					<TD width="35%">
						<input type="text" name="downMaxRateMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="downMaxRateMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">上行噪声裕量(dB):</TD>
					<TD width="35%">
						<input type="text" name="upNoiseMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="upNoiseMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

					<TD width="15%" class="column" align="right">下行噪声裕量(dB):</TD>
					<TD width="35%">
						<input type="text" name="downNoiseMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="downNoiseMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

				</TR>
				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">交织深度:</TD>
					<TD width="35%">
						<input type="text" name="interDepthMinInput" maxlength=6 size=6 class="bk"/> —
						<input type="text" name="interDepthMaxInput" maxlength=6 size=6 class="bk"/>

					</TD>

					<TD width="15%" class="column" align="right">数据路径:</TD>
					<TD width="35%">
						<SELECT name="datePathInput" class="bk">
							<OPTION value="Fast">快速存取</OPTION>
							<OPTION value="Interleaved">交叉存取</OPTION>
						</SELECT>
					</TD>

				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<!--
					<TD width="15%" class="column" align="right">功率级别:</TD>
					<TD width="35%">
						<SELECT name="powerlevelInput" class="bk">
							<OPTION value="1">1级</OPTION>
							<OPTION value="2">2级</OPTION>
							<OPTION value="3">3级</OPTION>
							<OPTION value="4">4级</OPTION>
							<OPTION value="5">5级</OPTION>
						</SELECT>

					</TD>
					 -->
					<TD width="15%" class="column" align="right">WLAN输出功率:</TD>
					<TD width="35%" colspan="3">
						<input type="text" name="powervalueInput" maxlength=6 size=6 class="bk"/>

					</TD>

				</TR>

				<TR bgcolor="#FFFFFF">
					<TD colspan="4" class="column" align="right"  >
						<button type="button" name="subBtn" value="" />
					</TD>
				</TR>

				<TR id="resultTr" bgcolor="#FFFFFF" style="display:none">
					<TD class="column" align="right" width="15%">执行结果:</TD>
					<TD colspan="3">
						<DIV id="result"></DIV>
					</TD>
				</TR>

				<tr height="20">
					<td colspan="4"  width="15" class="column">
					</td>
				</tr>

			</TABLE>
		</td>

	</tr>

	<tr align="left" id="editBussInfoTR" STYLE="display:none">
		<td colspan="4"  bgcolor=#999999>

			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
				<tr align="left">
					<td colspan="4" class="green_title" id="wanTitle"></td>
				</tr>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">业务类型:</TD>
					<TD width="35%" id="servTypeId">
						<SELECT name="servTypeInput" class="bk">
							<OPTION value="10">上网</OPTION>
							<OPTION value="11">IPTV</OPTION>
							<OPTION value="14">VOIP</OPTION>
						</SELECT>

					</TD>

					<TD width="15%" class="column" align="right">连接类型:</TD>
					<TD width="35%" id="wanTypeId">
						<SELECT name="wanTypeInput" class="bk" onclick="chgWanType(this.value)">
							<OPTION value="1">桥接</OPTION>
							<OPTION value="2">路由</OPTION>
							<OPTION value="3">静态IP</OPTION>
							<OPTION value="4">DHCP</OPTION>
						</SELECT>

					</TD>

				</TR>

				<TR bgcolor="#FFFFFF" id="ppp_type_1" STYLE="display:">
					<TD width="15%" class="column" align="right" id="editUsername"></TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="usernameInput" maxlength="20" class="bk" size="20">
					</TD>
					<TD width="15%" class="column" align="right">密码:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="passwordInput" maxlength=20 class="bk" size=20>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">

					<TD class="column" align="right" id="editPVC1" width="15%" STYLE="display:none">PVC/VLAN:</TD>
					<TD width="35%" id="editPVC2" STYLE="display:none" colspan="3">
						<input type="text" name="vpiInput" maxlength=3 size=3 class="bk"/>/
						<input type="text" name="vciInput" maxlength=3 size=3 class="bk"/>
					</TD>

					<TD width="15%" class="column" id="editVLAN1" align="right" STYLE="display:none">PVC/VLAN:</TD>
					<TD width="35%" id="editVLAN2" STYLE="display:none" colspan="3">
						<input name="vlanidInput" type="text" maxlength="20" class="bk" size="20"/>
					</TD>

				</TR>

				<TR id="ip_type_1" bgcolor="#FFFFFF" STYLE="display:none">

					<TD class="column" align="right"  width="15%">IP:</TD>
					<TD width="35%">
						<input name="ipInput" type="text" maxlength="20" class="bk" size="20"/>
					</TD>

					<TD class="column" align="right" width="15%">子网掩码:</TD>
					<TD width="35%">
						<input name="maskInput" type="text" maxlength="20" class="bk" size="20"/>
					</TD>

				</TR>


				<TR id="ip_type_2" bgcolor="#FFFFFF" STYLE="display:none">
					<TD class="column" align="right"  width="15%">网关:</TD>
					<TD width="35%" colspan="3">
						<input name="gatewayInput" type="text" maxlength="20" class="bk" size="20"/>
					</TD>

				</TR>

				<TR bgcolor="#FFFFFF">
					<TD width="15%" class="column" align="right">DNS:</TD>
					<TD width="35%" colspan="3">
						<input name="dnsInput" type="text" maxlength="20" class="bk" size="20"/>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD width="15%" class="column" align="right">绑定端口:</TD>
					<TD width="35%" colspan="3">
							<INPUT TYPE="checkbox" NAME="lan1" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.1.">LAN1
							<INPUT TYPE="checkbox" NAME="lan2" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2.">LAN2
							<INPUT TYPE="checkbox" NAME="lan3" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.3.">LAN3
							<INPUT TYPE="checkbox" NAME="lan4" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.4.">LAN4

							<INPUT TYPE="checkbox" NAME="lan5" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.">WLAN1
							<INPUT TYPE="checkbox" NAME="lan6" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.2.">WLAN2
							<INPUT TYPE="checkbox" NAME="lan7" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.3.">WLAN3
							<INPUT TYPE="checkbox" NAME="lan8" value="InternetGatewayDevice.LANDevice.1.WLANConfiguration.4.">WLAN4
					</TD>

				</TR>

				<TR bgcolor="#FFFFFF" id="bussInfoBtn">
					<TD colspan="4" class="column" align="right"  >
						<button type="button" name="subBtn" value="" />
					</TD>
				</TR>

				<TR id="resultTr" bgcolor="#FFFFFF" style="display:none">
					<TD class="column" align="right" width="15%">执行结果:</TD>
					<TD colspan="3">
						<DIV id="result"></DIV>
					</TD>
				</TR>

			</TABLE>
		</td>

	</tr>

	<tr align="left" id="voipAddrTR" STYLE="display:none">
		<td colspan="4" class="green_title"><font color="red">VOIP服务器地址信息</font></td>
	</tr>

	<tr align="left" id="editBussVoipInfoTR" STYLE="display:none">
		<td colspan="4"  bgcolor=#999999>

			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">主地址:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="proxServerInput" maxlength="20" class="bk" size="20">

					</TD>

					<TD width="15%" class="column" align="right">主端口:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="proxPortInput" maxlength="20" class="bk" size="20">

					</TD>

				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">备用地址:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="proxServer2Input" maxlength="20" class="bk" size="20">
					</TD>
					<TD width="15%" class="column" align="right">备用端口:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="proxPort2Input" maxlength=20 class="bk" size=20>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">注册地址:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="regiServInput" maxlength="20" class="bk" size="20">
					</TD>
					<TD width="15%" class="column" align="right">注册端口:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="regiPortInput" maxlength=20 class="bk" size=20>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">备用注册地址:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="standRegiServInput" maxlength="20" class="bk" size="20">
					</TD>
					<TD width="15%" class="column" align="right">备用注册端口:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="standRegiPortInput" maxlength=20 class="bk" size=20>
					</TD>
				</TR>


				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">外部绑定地址:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="outBoundProxyInput" maxlength="20" class="bk" size="20">
					</TD>
					<TD width="15%" class="column" align="right">外部绑定端口:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="outBoundPortInput" maxlength=20 class="bk" size=20>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF" STYLE="display:">
					<TD width="15%" class="column" align="right">备用外部绑定地址:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="standOutBoundProxyInput" maxlength="20" class="bk" size="20">
					</TD>
					<TD width="15%" class="column" align="right">备用外部绑定端口:</TD>
					<TD width="35%">
						<INPUT TYPE="text" NAME="standOutBoundPortInput" maxlength=20 class="bk" size=20>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF">
					<TD colspan="4" class="column" align="right"  >
						<button type="button" name="subBtn" value="" />
					</TD>
				</TR>

				<tr height="20">
					<td colspan="4"  width="15" class="column">
					</td>
				</tr>

			</TABLE>
		</td>

	</tr>

</TABLE>
</FORM>
</body>
</html>














