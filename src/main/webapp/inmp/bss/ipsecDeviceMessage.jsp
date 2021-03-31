<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>基本信息</title>
<link href="<s:url value="../../css/inmp/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/js/jquerysplitpage-linkage.js"/>"></script>
<script type="text/javascript">
	/* $(document).ready(function() { 
	 var device_id_judge = '<s:property value="devicemap.deviceid" />';
	 if(device_id_judge.length>0){
	 checkcon();
	 }
	 });  */
	function showOperation() {
		if ($('#historyMsgTR').css("display") == "none") {
			$('#historyMsgTR').show();
			$('#historyTitle').text("隐藏");
			quertHistoryMsg();
		} else {
			$('#historyMsgTR').hide();
			$('#historyTitle').text("查看");
		}
	}
	function quertHistoryMsg() {
		var url = "deviceCustomer!queryHistoryOperation.action";
		var deviceId = '<s:property value="deviceMap.DEVICEID" />';
		$.post(url, {
			deviceId : deviceId
		}, function(ajax) {
			$("#historyMsgTD").html(ajax);
		});
	}
	function checkcon() {
		$("#connmsg").html('<span class="icon_linking">正在交互</span>');
		var device_id_judge = '<s:property value="device_id" />';
		if (null != device_id_judge && "" != device_id_judge) {
			var url = "<s:url value='/itms/service/ipsecSheetServ!queryConnCondition.action'/>";
			$.post(url,{
								device_id_judge : device_id_judge
							},
							function(ajax) {
								if ("1" == ajax) {
									$("#connmsg").html('<span style="color:green">正常交互</span>');
								} else if ("-6" == ajax) {
									$("#connmsg").html('<span style="color:blue">设备正被操作，请稍后再试</span>');
								} else {
									$("#connmsg").html('<span style="color:red">不正常交互</span>');
								}
							});
		}
	}
	function generateComPw() {
		var url = "deviceCustomer!generateComPwd.action";
		$.post(url, {
			gw_type : "<s:property value="deviceMap['GWTYPE']" />"
		}, function(ajax) {
			$("#compw_span_new").html(ajax);
		});

	}
	function setComPw() {
		var paramValue = $("#compw_span_new").text();
		if (null == paramValue || '' == paramValue) {
			alert("请先设置密码!");
			return false;
		}
		showMsgDlg();
		var url = "deviceCustomer!modyfiyConPwd.action";
		var deviceId = '<s:property value="deviceMap.DEVICEID" />';
		$.post(url, {
			deviceId : deviceId,
			paramValue : paramValue
		}, function(ajax) {
			closeMsgDlg();
			if (ajax == "-2") {
				alert("数据出错，请检查程序！");
				$('#compw_span_new').innerHTML = "";
			} else if (ajax == "-1") {
				alert("密码设置失败,请确保设备连接正常！");
				$('#compw_span_new').innerHTML = "";
			} else if (ajax == "0") {
				alert("密码设置成功，但数据库更新失败，请联系管理员！");
			} else if (ajax == "1") {
				alert("密码设置成功！");
				document.all.compw_span.innerHTML = $('#compw_span_new').val()
						.trim();
				$('#compw_span_new').innerHTML = "";
			} else {
				alert("未知错误！");
				$('#compw_span_new').innerHTML = "";
			}
			inspireBtns();
		});
		disableBtns();
	}
	function showMsgDlg() {
		w = document.body.clientWidth;
		h = document.body.clientHeight;

		l = (w - 250) / 2;
		t = (h - 60) / 2;
		PendingMessage.style.left = l;
		PendingMessage.style.top = t;
		PendingMessage.style.display = "";
	}
	function closeMsgDlg() {
		PendingMessage.style.display = "none";
	}

	function disableBtns() {
		document.all.compwGen.disabled = true;
		document.all.compwSet.disabled = true;
	}

	function inspireBtns() {
		document.all.compwGen.disabled = false;
		document.all.compwSet.disabled = false;
	}
</script>

</head>
<body>
	<div id="PendingMessage"
		style="position: absolute; z-index: 3; top: 240px; left: 250px; width: 250; height: 60; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
		<center>
			<table border="0">
				<tr>
					<td valign="middle"><img src="../images/cursor_hourglas.gif"
						border="0" WIDTH="30" HEIGHT="30"></td>
					<td>&nbsp;&nbsp;</td>
					<td valign="middle"><span id=txtLoading
						style="font-size: 12px; font-family: 宋体">正在设置密码，请稍等…</span></td>
				</tr>
			</table>
		</center>
	</div>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD>
				<TABLE width="99%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<s:if test="ipsecDeviceInfo.size()>0&&ipsecDeviceInfo!=null">
									<s:iterator value="ipsecDeviceInfo">
										<TR>
											<TH colspan="4" align="center">设备〖 <s:property
													value="device_serialnumber" />〗详细信息
											</TH>
										</TR>
										<tr bgcolor="#FFFFFF" height="20">
											<td width="15%" class=column align="right">设备id</td>
											<td width="35%"><s:property value="device_id" /></td>

											<td width="15%" class=column align="right">设备型号</td>
											<td width="35%"><s:property value="device_model" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">设备厂商(oui)</td>
											<td><s:property value="manufacturer" />(<s:property
													value="oui" />)</td>

											<td class=column align="right">序列号</td>
											<td><s:property value="device_serialnumber" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">硬件版本</td>
											<td><s:property value="handware_version" /></td>

											<td class=column align="right">特别版本</td>
											<td><s:property value="spec_version" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">软件版本</td>
											<td><s:property value="software_version" />
											<td class=column align="right">设备类型</td>
											<td><s:property value="device_type" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">最大包数</td>
											<td><s:property value="maxenvelopes" /></td>

											<td class=column align="right">mac 地址</td>
											<td><s:property value="cpe_mac" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">设备编号</td>
											<td><s:property value="device_id" /></td>

											<td class=column align="right">管理域</td>
											<td><s:property value="device_area_name" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">设备组id</td>
											<td><s:property value="gw_type" /></td>

											<td class=column align="right">设备组名称</td>
											<td><s:property value="gw_type_name" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">终端规格</td>
											<td><s:property value="spec_name" /></td>
											<td class=column align="right">地址方式</td>
											<td><s:property value="ipType" /></td>
										</tr>
									</s:iterator>
								</s:if>
								<TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="center" colspan=4>设备业务信息</TD>
								</TR>
								<tr bgcolor="#FFFFFF" height="20">
									<td class=column align="right">当前开通业务</td>
									<td><s:property
											value="null==ServiceStatByDevice||ServiceStatByDevice.size==0?'无':ServiceStatByDevice.size+'个业务'" /></td>
									<td class=column align="right">是否激活</td>
									<td><s:property
											value="null==ServiceStatByDevice||ServiceStatByDevice.size==0?'无':''" /></td>
								</tr>
								<s:if
									test="null!=ServiceStatByDevice&&ServiceStatByDevice.size>0">
									<s:iterator value="ServiceStatByDevice" var="item">
										<tr bgcolor="#FFFFFF" height="20">
											<td></td>
											<td><s:property value="item[2]" /></td>
											<td></td>
											<td><s:property
													value="'1'==item[1]? '已激活' :'0'==item[1]?'未激活':'激活失败'" /></td>
										</tr>
									</s:iterator>
								</s:if>
								<TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="center" colspan=4>设备动态信息</TD>
								</TR>
								<s:if test="ipsecDeviceInfo.size()>0&&ipsecDeviceInfo!=null">
									<s:iterator value="ipsecDeviceInfo">
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">设备与平台正常交互状态</td>
											<TD colspan="3" width="30%">
											<div class="nolink" id="connMsg" style="display:inline">
											<span id="icon_linking"><s:property value="online_status" /></span>&nbsp;&nbsp; 
											</div>
											<input name="onlineStatusGet" type="button" value="检测交互状态" class="btn_g" onclick="getOnlineStatus()"></TD>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">设备属地 <input type="hidden"
												name="cityid" value="<s:property value='city_id' />">
											</td>
											<td><s:property value="city_name" /></td>
											<td class=column align="right">注册系统时间</td>
											<td><s:property value="complete_time" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">ip地址</td>
											<td><s:property value="loopback_ip" /></td>

											<td class=column align="right">最近更新时间</td>
											<td><s:property value="last_time" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">设备当前注册状态</td>
											<td><s:property value="device_status" /></td>

											<td class=column align="right">设备配置模板</td>
											<td><textarea cols=40 rows=4 readonly>
													<s:property value="deviceModelTemplate" />
												</textarea></td>
										</tr>
										<TR bgcolor="#FFFFFF" height="20">
											<TD class=column align="center" valign="top" colspan=4>
												IPSEC操作历史信息 〖 <a href="javascript:showOperation()"
												style="color: 808080; font-size: 9pt;">查看</a>〗
											</TD>
										</TR>
										<tr id="historyMsgTR" style="display: none;">
											<td colspan="4" id="historyMsgTD"></td>
										</tr>
										<TR bgcolor="#FFFFFF" height="20">
											<TD class=column align="center" colspan="4">当前配置参数</TD>
										</TR>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">CPE用户名</td>
											<td><s:property value="cpe_username" /></td>

											<td class=column align="right">CPE密码</td>
											<td><s:property value="cpe_passwd" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">ACS用户名</td>
											<td><s:property value="acs_username" /></td>

											<td class=column align="right">ACS密码</td>
											<td><s:property value="acs_passwd" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">电信维护账号</td>
											<td><s:property value="x_com_username" /></td>

											<td class=column align="right">电信维护密码</td>
											<td><span id="compw_span"><s:property
														value="ipsecDeviceInfo['x_com_passwd']" /></span>&nbsp; <span
												id="compw_span_new"></span>&nbsp; <!-- <input name="compw_span_hid" value="" type="hidden"/> -->
												<input name="compwGen" type="button" value="生成"
												class="btn_g" onclick="generateComPw()"> <input
												name="compwSet" type="button" value="设置" class="btn_g"
												onclick="setComPw()"></td>
										</tr>
										<TR bgcolor="#FFFFFF" height="20">
											<TD class=column align="center" colspan=4>用户基本信息</TD>
										</TR>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">用户账号</td>
											<td><a
												href="javascript:GoContent(<s:property value="user_id" />','<s:property value="gw_type" />')"><s:property
														value="username" /></a></td>

											<td class=column align="right">业务类型</td>
											<td><s:property value="service_name" /></td>
										</tr>
									</s:iterator>
								</s:if>
								<TR>
									<TD colspan="4" align="center" class=foot><INPUT
										TYPE="submit" value=" 关 闭 " class=jianbian
										onclick="javascript:window.close();"></TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</body>
</html>