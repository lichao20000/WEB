<%--
ITMS系统IPTV多终端业务开通
Author: 王森博
Version: 1.0.0
Date: 2010-03-22
--%>

<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>应用服务</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

		<SCRIPT LANGUAGE="JavaScript">
parent.unblock();
/**
 * 把编辑的页面显示出来
 */
var isHaveWlan = "false";

function editIptvHtml(bindportStr,wanId,wanConnId,wanConnSessId) {

	$("tr[@id='iptvconfig']").show();

	var deviceId = $("input[@name='deviceId']");

	$("input[@name='wanId']").val(wanId);
	$("input[@name='wanConnId']").val(wanConnId);
	$("input[@name='wanConnSessId']").val(wanConnSessId);

	if(bindportStr.indexOf("WLANConfiguration")!=-1){
		$("#configType>option[value='1']").remove();
		isHaveWlan = "true";
	}
	var url = "<s:url value='/gwms/config/iptvACT!editWlanInit.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		type:"1",
		bindPort:bindportStr
	},function(ajax){
		$("div[@id='lanInter']").html(ajax);
		parent.dyniframesize();
	});
	chgConfigType();

	parent.dyniframesize();

}

/**
 * 配置
 */
function configIptv() {

	var deviceId = $.trim($("input[@name='deviceId']").val());
	var wanId = $.trim($("input[@name='wanId']").val());//i
	var wanConnId = $.trim($("input[@name='wanConnId']").val());//j
	var wanConnSessId = $.trim($("input[@name='wanConnSessId']").val());//k
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var LANPortList = $.trim($("input[@name='LANPortList']").val());

	var configType = $("select[@name='configType']").val();

	var port="";
	$("input[@name='LAN'][@checked]").each(function(){
    		port = port + $(this).val()+",";
    });
    if(configType=="0"){
   		if(LANPortList == port){
			alert("你没有更改任何配置项，无需配置！");
			return false;
		}
		if(isHaveWlan=="true"){
			port = port+"InternetGatewayDevice.LANDevice.1.WLANConfiguration.2,";
		}
   	}
   	if(configType=="1"){
		port = port+"InternetGatewayDevice.LANDevice.1.WLANConfiguration.2,";
   	}
    var portList = port.substring(0, port.length-1);

	var url = "<s:url value='/gwms/config/iptvACT!edit.action'/>";
	$.post(url,{
		deviceId:deviceId,
		deviceSn:deviceSn,

		wanId:wanId,
		wanConnId:wanConnId,
		wanConnSessId:wanConnSessId,

		bindPort:portList,
		configType:configType
	},function(ajax){

		$("tr[@id='resultTr']").show();
		$("tr[@id='tr002']").show();
		$("div[@id='result']").html("");
		var s = ajax.split(";");
		if (s[0] == "1")
		{
			$("div[@id='result']").append(s[1]);
			var strategyurl = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = s[2];
			$.post(strategyurl,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
            parent.dyniframesize();
		} else {
			$("div[@id='result']").append("<FONT COLOR=\"#FF0000\">"+s[1]+"</FONT>");
		}

		parent.dyniframesize();
	});
}


function reInter(){
	$("div[@id='lanInter']").html("正在获取端口...");
	var deviceId = $("input[@name='deviceId']");
	var LANPortList = $.trim($("input[@name='LANPortList']").val());
	var url = "<s:url value='/gwms/config/iptvACT!editWlanInit.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		type:"0",
		bindPort:LANPortList
	},function(ajax){
		$("div[@id='lanInter']").html(ajax);
		parent.dyniframesize();
	});
}

//配置方式
function chgConfigType(){
    var configType = $("select[@name='configType']").val();
	if('0' == configType){
		$("tr[@id='wireless']").hide();
		parent.dyniframesize();
	}else{
		$("tr[@id='wireless']").show();
		parent.dyniframesize();
	}
}

</script>

	</head>

	<body>
		<FORM NAME="frm" METHOD="post" action="">
			<input type="hidden" name="deviceId"
				value="<s:property value="deviceId"/>" />
			<input type="hidden" name="deviceSn"
				value="<s:property value="deviceSn"/>" />
			<input type="hidden" name="wanId" value="">
			<input type="hidden" name="wanConnId" value="">
			<input type="hidden" name="wanConnSessId" value="">

			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable"
				align="center">
				<tr>
					<TD>
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
							<tr align="center" height="25">
								<td colspan="3" class="green_title">
									IPTV多终端
								</td>
							</tr>
							<tr align="left" id="trnet">
								<td colspan="3" bgcolor=#999999>
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
										<tr align="center" bgcolor="#FFFFFF">
											<TD class=column5 align="center">
												连接类型
											</TD>
											<TD class=column5 align="center">
												PVC/VLANID
											</TD>

											<TD class=column5 align="center">
												连接状态
											</TD>
											<TD class=column5 align="center">
												绑定端口
											</TD>
											<TD class=column5 align="center">
												服务类型
											</TD>
											<TD class=column5 align="center">
												连接错误
											</TD>
											<TD class=column5 align="center">
												操作
											</TD>
										</tr>
										<s:if test="wanConfigList.size()>0">
											<s:iterator var="wanConfigList" value="wanConfigList">
												<tr align="center" bgcolor="#FFFFFF">
													<s:if
														test="#wanConfigList.connType=='N/A' || #wanConfigList.connType=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="connType" />
														</TD>
													</s:else>
													<s:if test="accessType=='DSL'">
														<TD align="center">
															PVC:
															<s:property value="vpi" />
															/
															<s:property value="vci" />
														</TD>

													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="vlanid" />
														</TD>
													</s:else>

													<s:if
														test="#wanConfigList.connStatus=='N/A' || #wanConfigList.connStatus=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="connStatus" />
														</TD>
													</s:else>

													<s:if
														test="#wanConfigList.bindPort=='N/A' || #wanConfigList.bindPort=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="bindPort" />
														</TD>
													</s:else>

													<s:if
														test="#wanConfigList.servList=='N/A' || #wanConfigList.servList=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="servList" />
														</TD>
													</s:else>

													<s:if
														test="#wanConfigList.connError=='N/A' || #wanConfigList.connError=='null' ">
														<TD align="center">
															-
														</TD>
													</s:if>
													<s:else>
														<TD align="center">
															<s:property value="connError" />
														</TD>
													</s:else>

													<TD align="center">

														<IMG id='img1' SRC='../../images/edit.gif' title='编辑'
															style='cursor: pointer; cursor: hand;'
															onclick="editIptvHtml('<s:property value="bindportStr"/>','<s:property value="wanId"/>','<s:property value="wanConnId"/>','<s:property value="wanConnSessId"/>')"
															onmouseover="this.src='../../images/edit2.gif'"
															onmouseout="this.src='../../images/edit.gif'" />
													</td>
												</tr>
											</s:iterator>
										</s:if>
										<tr align="center" bgcolor="#FFFFFF">
											<TD colspan="11" align="center">
												<s:property value="corbaMsg" />
											</TD>
										</tr>

									</TABLE>
								</td>
							</tr>

						</table>
					</TD>
				</tr>

				<tr height="20">
					<td colspan="1" width="15"></td>
				</tr>
				<tr id="iptvconfig" STYLE="display: none">
					<td colspan="4" bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<tr align="center">
								<td colspan="4" class="green_title">
									参数选择
								</td>
							</tr>

							<TR bgcolor="#FFFFFF" STYLE="display: ">
								<TD width="15%" class=column align="right">
									配置方式：
								</TD>
								<TD width="85%" colspan="3">

									<select name="configType" id="configType" class="bk"
										onchange="chgConfigType()">
										<option value="1">
											==配置有线+无线==
										</option>
										<option value="0" selected="selected">
											==配置有线==
										</option>
									</select>
								</td>

							</TR>
							<TR bgcolor="#FFFFFF">

								<TD class=column align="right" width="15%">
									<img src="<s:url value="/images/refresh.png" />" border="0"
										alt="实时获取" onclick="reInter()">
									绑定端口：
								</TD>
								<TD width="85%" colspan="3">
									<div id="lanInter">
										正在获取端口...
									</div>
								</TD>
							</TR>


							<TR id="wireless" bgcolor="#FFFFFF" STYLE="display: none">
								<TD width="15%" class=column align="right">
									SSID2：
								</TD>
								<TD width="35%">
									iTV+序列号最后五位
								</TD>
								<TD width="15%" class=column align="right">
									WPA认证密钥：
								</TD>
								<TD width="35%">
									设备序列号后8位

								</TD>
							</TR>


							<TR bgcolor="#FFFFFF">
								<TD colspan="4" align="right" class="green_foot">
									<button onclick="configIptv();">
										配 置
									</button>
								</TD>
							</TR>

							<TR id="resultTr" bgcolor="#FFFFFF" style="display: none">
								<TD class=column align="right" width="15%">
									执行结果:
								</TD>
								<TD colspan="3">
									<DIV id="result"></DIV>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
								<td colspan="4" valign="top" class=column>
									<div id="div_strategy"
										style="width: 100%; z-index: 1; top: 100px">
									</div>
								</td>
							</TR>
						</TABLE>
					</td>
				</tr>
			</TABLE>
		</FORM>
	</body>
</html>














