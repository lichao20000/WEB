<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css" />
<title>智能网关质量检测页面</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function showBlue() {
		var loid = document.getElementById("loid").value;
		var device = document.getElementById("device").value;
		$("div[@id='div_update']").html("");
		$("div[@id='div_update']").css("display", "");
		if (device.length < 6 && loid.length == 0) {
			alert("设备序列号少于6位！");
		}
		if (("4381LO00410272" == loid && device.length== 0) || (device== "D382C48555FFFE87C"&& loid.length == 0)||(device== "D382C48555FFFE87C" &&loid == "4381LO00410272")) {
			document.all.blue1.style.display = '';
			$("div[@id='div_update']").display = '';
		} else {
			document.all.blue1.style.display = 'none';
			$("div[@id='div_update']").html("当前条件查询的设备不存在！");
		}

	}
</script>
</head>
<body>
	<table width="100%" border="0" cellspacing="1" cellpadding="2"
		bgcolor="#999999">
		<tr>
			<td colspan="4" align="center" class="green_title">设备查询</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td width="15%" align="right">LOID：</td>
			<td width="35%" align="left"><input id="loid" type="text"
				class="bk"/></td>
			<td width="15%" align="right">设备序列号：</td>
			<td width="35%" align="left"><input id="device" type="text"
				class="bk"/> <font color="red">*至少输入后六位</font></td>

		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" align="right"><input type="button" value="查询"
				onclick="showBlue()"/></td>
		</tr>
	</table>

	<div id="space" style="display: none"
		style="width: 100%; z-index: 1; height:50px; top: 100px"></div>

	<table id="blue1" border=0 cellspacing=1 cellpadding=2 width="100%"
		align="center" style="display:none">
		<tr>
			<td colspan="4" align="center" class="green_title">视频质量信息</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4">
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#ffffff">
					<tr bgcolor="#ffffff" class="green_title">
						<td colspan="4" align="left">宽带上网：</td>
					</tr>
					<tr>
						<td colspan="4">
							<table border=0 cellspacing=1 cellpadding=2 width="100%"
								bgcolor="#999999">
								<tr align="center" bgcolor="#ffffff">
									<td class=column5 align="center">PVC/VLAN配置</td>
									<td class=column5 align="center">连接类型</td>
									<td class=column5 align="center">绑定端口</td>
									<td class=column5 align="center">连接状态</td>
									<td class=column5 align="center">DNS(IPV4)</td>
									<td class=column5 align="center">IP地址(IPV4)</td>
									<td class=column5 align="center">DNS(IPV6)</td>
									<td class=column5 align="center">IP地址(IPV6)</td>
									<td class=column5 align="center">PPPoE账号</td>
									<td class=column5 align="center">拨号失败错误码</td>
								</tr>
								<tr align="center" bgcolor="#ffffff">
									<td align="center">41</td>
									<td align="center">桥接</td>
									<td align="center">LAN1</td>
									<td align="center">connected</td>
									<td align="center">123.166.138.87</td>
									<td align="center">123.166.138.87</td>
									<td align="center">-</td>
									<td align="center">-</td>
									<td align="center">n0436ztc5094660</td>
									<td align="center">ERROE_NONE</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4">
				<table  border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#ffffff">
					<tr bgcolor="#ffffff" class="green_title">
						<td colspan="4" align="left">IPTV：</td>
					</tr>
					<tr>
						<td colspan="4">
							<table border=0 cellspacing=1 cellpadding=2 width="100%"
								bgcolor="#999999">
								<tr align="center" bgcolor="#ffffff">
									<td class=column5 align="center">PVC/VLAN</td>
									<td class=column5 align="center">组播VLAN</td>
									<td class=column5 align="center">连接状态</td>
									<td class=column5 align="center">连接类型</td>
									<td class=column5 align="center">绑定端口</td>
									<td class=column5 align="center">下挂设备数量</td>
									<td class=column5 align="center">下挂设备连接状态</td>
								</tr>
								<tr align="center" bgcolor="#ffffff">
									<td align="center">43</td>
									<td align="center">-</td>
									<td align="center">connected</td>
									<td align="center">路由</td>
									<td align="center">PPPoE_Bridge</td>
									<td align="center">3</td>
									<td align="center">connected</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr bgcolor="#ffffff">
			<td colspan="4">
				<table   border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#ffffff">
					<tr bgcolor="#ffffff" class="green_title">
						<td colspan="4" align="left">VOIP：</td>
					</tr>
					<tr>
						<td colspan="4">
							<table border=0 cellspacing=1 cellpadding=2 width="100%"
								bgcolor="#999999">
								<tr align="center" bgcolor="#ffffff">
									<td class=column5 align="center">语音端口</td>
									<td class=column5 align="center">协议类型</td>
									<td class=column5 align="center">PVC/VLAN</td>
									<td class=column5 align="center">DHCP地址</td>
									<td class=column5 align="center">连接类型</td>
									<td class=column5 align="center">连接状态</td>
									<td class=column5 align="center">服务器地址</td>
									<td class=column5 align="center">备用服务器</td>
									<td class=column5 align="center">注册状态</td>
									<td class=column5 align="center">注册账号</td>
									<td class=column5 align="center">下挂设备数量</td>
									<td class=column5 align="center">下挂设备连接状态</td>
								</tr>
								<tr align="center" bgcolor="#ffffff">
									<td align="center">1</td>
									<td align="center">H.248</td>
									<td align="center">45</td>
									<td align="center">172.50.228.4</td>
									<td align="center">IP_Routed</td>
									<td align="center">connected</td>
									<td align="center">192.168.1.100</td>
									<td align="center">192.168.1.101</td>
									<td align="center">-</td>
									<td align="center">A</td>
									<td align="center">2</td>
									<td align="center">connected</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>

		<tr bgcolor="#ffffff">
			<td colspan="4" height="10px"></td>
		</tr>
		<tr>
			<td colspan="4">
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center" bgcolor="#999999">
					<tr>
						<td colspan="4" align="center" class="green_title">用户行为分析信息</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td colspan="4" align="left">宽带业务：</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">用户访问业务类型：</td>
						<td align="left" width="25%">宽带业务</td>
						<td align="center" width="25%" class="column">访问时间 ：</td>
						<td align="left" width="25%">2016/10/18 19:40</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">访问设备IP：</td>
						<td align="left" width="25%">192.168.1.1</td>
						<td align="center" width="25%" class="column">使用时长(min) ：</td>
						<td align="left" width="25%">90</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td colspan="4" align="left">IPTV：</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">用户访问业务类型：</td>
						<td align="left" width="25%">IPTV</td>
						<td align="center" width="25%" class="column">访问时间 ：</td>
						<td align="left" width="25%">2016/10/18 19:40</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">访问设备IP：</td>
						<td align="left" width="25%">192.168.1.1</td>
						<td align="center" width="25%" class="column">使用时长(min) ：</td>
						<td align="left" width="25%">90</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td colspan="4" align="left">VOIP：</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">用户访问业务类型：</td>
						<td align="left" width="25%">VOIP</td>
						<td align="center" width="25%" class="column">访问时间 ：</td>
						<td align="left" width="25%">2016/10/18 19:40</td>

					</tr>
					<tr bgcolor="#ffffff">
						<td align="center" width="25%" class="column">访问设备IP：</td>
						<td align="left" width="25%">192.168.1.1</td>
						<td align="center" width="25%" class="column">使用时长(min)：</td>
						<td align="left" width="25%">90</td>
					</tr>
				</table>
			</td>
		</tr>

	</table>
	<div id="div_update" style="display: none"
		style="width: 100%; z-index: 1; top: 100px"></div>
</body>
</html>