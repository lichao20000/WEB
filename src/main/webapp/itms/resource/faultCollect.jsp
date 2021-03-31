<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>采集</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/faulttreatment/slide.js"/>"></script>
<link href="<s:url value="/css3/css.css"/>" rel="stylesheet"
	type="text/css" />
	<script type="text/javascript">
	function detailDevice(deviceId, lanId, lanWlanId){
		var url = "<s:url value="/gwms/diagnostics/deviceInfo!getGwWlanAsso.action?deviceId="/>"+deviceId+"&lanId="+lanId+"&lanWlanId="+lanWlanId;
		window.open(url,"","left=80,top=80,width=650,height=300,resizable=yes,scrollbars=yes");
	}
	</script>
</head>

<body>
	<div class="it_main">
		<div class="tanchu">
			<div class="yw_tit">业务信息</div>
			<div class="content">
				<div class="itms_tit">
					<h2>宽带上网</h2>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>PVC/VLAN配置</th>
						<%if (LipossGlobals.inArea("js_dx")) { %>
						<th>宽带DHCP开关</th>
						<%} %>
						<th>连接类型</th>
						<th>绑定端口</th>
						<th>连接状态</th>
						<th>DNS(IPV4)</th>
						<th>IP地址(IPV4)</th>
						<th>DNS(IPV6)</th>
						<th>IP地址(IPV6)</th>
						<th>PPPoE账号</th>
						<th>拨号失败错误码</th>
					</tr>
					<s:iterator value="wideNetInfoList" var="wideNetInfo">
						<tr align="center" bgcolor="#FFFFFF">
							<s:if test="accessType==1">
								<td><s:property value="pvc" /></td>
							</s:if>
							<s:else>
								<td><s:property value="vlanid" /></td>
							</s:else>

							<%if (LipossGlobals.inArea("js_dx")) { %>
							<td><s:property value="dhcp_status" /></td>
							<%} %>


							<s:if
								test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="connType" /></td>
							</s:else>
							<s:if
								test="#wideNetInfo.bindPort=='N/A' || #wideNetInfo.bindPort=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="bindPort" /></td>
							</s:else>
							<!--
										<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
											<td>-</td>
										</s:if>
										<s:else>
											<td><s:property value="status"/></td>
										</s:else>
										 -->
							<td><s:property value="status" /></td>
							<s:if test="#wideNetInfo.dns=='N/A' || #wideNetInfo.dns=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="dns" /></td>
							</s:else>

							<!-- <s:if
											test="#wideNetInfo.password=='N/A' || #wideNetInfo.password=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="password" />
											</td>
										</s:else> -->
							<s:if test="#wideNetInfo.ip=='N/A' || #wideNetInfo.ip=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="ip" /></td>
							</s:else>
							<!-- ipv6 dns -->
							<s:if
								test="#wideNetInfo.dns_ipv6=='N/A' || #wideNetInfo.dns_ipv6=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="dns_ipv6" /></td>
							</s:else>
							<!-- ipv6 dns -->
							<!-- ipv6 ip -->
							<s:if
								test="#wideNetInfo.ip_ipv6=='N/A' || #wideNetInfo.ip_ipv6=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="ip_ipv6" /></td>
							</s:else>
							<!-- ipv6 ip -->
							<s:if
								test="#wideNetInfo.username=='N/A' || #wideNetInfo.username=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="username" /></td>
							</s:else>
							<s:if
								test="#wideNetInfo.connError=='N/A' || #wideNetInfo.connError=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="connError" /></td>
							</s:else>
						</tr>
					</s:iterator>
<!-- 					<tr> -->
<%-- 								<%if (LipossGlobals.inArea("js_dx")) { %> --%>
<!-- 									<td colspan="5"> -->
<%-- 								<%} else{%> --%>
<!-- 									<td  align="center" colspan="4"> -->
<%-- 								<%} %> --%>
<%-- 										<s:property value="wideNetMsg" /> --%>
<!-- 									</td> -->
<!-- 								</tr> -->
				</table>
			</div>
			<div class="content">
				<div class="itms_tit">
					<h2>IPTV</h2>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>PVC/VLAN</th>
						<%if (LipossGlobals.inArea("js_dx")) { %>
						<th>组播VLAN</th>
						<%} %>
						<th>连接状态</th>
						<th>连接类型</th>
						<th>绑定端口</th>
					</tr>
					<s:iterator value="iptvInfoList" var="wideNetInfo">
						<tr align="center" bgcolor="#FFFFFF">
							<s:if test="accessType==1">
								<td><s:property value="pvc" /></td>
							</s:if>
							<s:else>
								<td><s:property value="vlanid" /></td>
							</s:else>
							<%if (LipossGlobals.inArea("js_dx")) { %>
							<s:if
								test="#wideNetInfo.multicast_vlan=='N/A' || #wideNetInfo.multicast_vlan=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="multicast_vlan" /></td>
							</s:else>
							<%} %>
							<s:if
								test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="status" /></td>
							</s:else>
							<s:if
								test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="connType" /></td>
							</s:else>
							<s:if
								test="#wideNetInfo.bindPort=='N/A' || #wideNetInfo.bindPort=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="bindPort" /></td>
							</s:else>
						</tr>
					</s:iterator>
<!-- 					<tr> -->
<%-- 									<%if (LipossGlobals.inArea("js_dx")) { %> --%>
<!-- 										<td  align="center" colspan="11"> -->
<%-- 									<%} else{%> --%>
<!-- 									<td  align="center" colspan="11"> -->
<%-- 									<%} %> --%>
<%-- 											<s:property value="iptvMsg" /> --%>
<!-- 										</td> -->
<!-- 					</tr> -->
				</table>
			</div>
			<div class="content">
				<div class="itms_tit">
					<h2>VOIP</h2>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>语音端口</th>
						<th>协议类型</th>
						<th>PVC/VLAN</th>
						<th>DHCP地址</th>
						<th>连接类型</th>
						<th>连接状态</th>
						<s:if test="voipProtocalTypeStr=='H.248'">
							<th>服务器地址</th>
							<th>备用服务器</th>
							<th>全局唯一标识
							</td>
							<th>物理标识</th>
						</s:if>
						<s:else>
							<th>服务器地址</th>
							<th>备用服务器</th>
							<th>注册状态</th>
							<th>注册帐号</th>
							<!-- 隐藏密码
													<td class=column5 align="center">
														密码
													</td>
												-->
						</s:else>
					</tr>
					<s:iterator value="voipInfoList" var="wideNetInfo">
						<tr align="center" bgcolor="#FFFFFF">
							<s:if
								test="#wideNetInfo.line=='N/A' || #wideNetInfo.line=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="line" /></td>
							</s:else>
							<td><s:property value="voipProtocalTypeStr" /></td>
							<s:if test="accessType==1">
								<td><s:property value="pvc" /></td>
							</s:if>
							<s:else>
								<td><s:property value="vlanid" /></td>
							</s:else>


							<s:if test="#wideNetInfo.ip=='N/A' || #wideNetInfo.ip=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="ip" /></td>
							</s:else>
							<s:if
								test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="connType" /></td>
							</s:else>
							<s:if
								test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
								<td>-</td>
							</s:if>
							<s:else>
								<td><s:property value="status" /></td>
							</s:else>
							<s:if test="voipProtocalTypeStr=='H.248'">
								<s:if
									test="#wideNetInfo.media_gateway_controler=='N/A' ||  #wideNetInfo.media_gateway_controler=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="media_gateway_controler" /></td>
								</s:else>
								<s:if
									test="#wideNetInfo.media_gateway_controler_2=='N/A' ||  #wideNetInfo.media_gateway_controler_2=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="media_gateway_controler_2" /></td>
								</s:else>
								<s:if
									test="#wideNetInfo.h248_device_id=='N/A' ||  #wideNetInfo.h248_device_id=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="h248_device_id" /></td>
								</s:else>
								<s:if
									test="#wideNetInfo.physical_term_id=='N/A' ||  #wideNetInfo.physical_term_id=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="physical_term_id" /></td>
								</s:else>
							</s:if>
							<s:else>
								<s:if
									test="#wideNetInfo.prox_serv=='N/A' || #wideNetInfo.prox_serv=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="prox_serv" /></td>
								</s:else>
								<s:if
									test="#wideNetInfo.prox_serv_2=='N/A' || #wideNetInfo.prox_serv_2=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="prox_serv_2" /></td>
								</s:else>
								<s:if test="#wideNetInfo.regist_status=='Up'">
									<td>已注册</td>
								</s:if>
								<s:else>
									<td>未注册</td>
								</s:else>
								<s:if
									test="#wideNetInfo.username=='N/A' || #wideNetInfo.username=='null' ">
									<td>-</td>
								</s:if>
								<s:else>
									<td><s:property value="username" /></td>
								</s:else>

								<!-- 隐藏密码，不可泄露
														<s:if
															test="#wideNetInfo.password=='N/A' || #wideNetInfo.password=='null' ">
															<td>
																-
															</td>
														</s:if>
														<s:else>
															<td>
																<s:property value="password" />
															</td>
														</s:else>
													-->
							</s:else>
						</tr>
					</s:iterator>
				</table>
			</div>
			<s:if test='accessType=="3" || accessType=="4" '>
			<div class="content">
			<s:if test="pon_type=='EPON'">
			<div class="itms_tit">
					<h2>EPON信息</h2>
				</div>
			</s:if>
			<s:elseif test="pon_type=='GPON'">
			<div class="itms_tit">
					<h2>GPON信息</h2>
				</div>
			</s:elseif>
			<s:else>
			<div class="itms_tit">
					<h2>PON信息</h2>
				</div>
			</s:else>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<s:if test="ponInfoOBJArr!=null">
										<s:iterator value="ponInfoOBJArr" var="ponInfoOBJ">
					<tr>
						<th>光路状态</th>
						<th>发射光功率(dBm)</th>
						<th>接收光功率(dBm)</th>
						<th>工作温度(1/256度)</th>
						<th>供电电压(1000微伏)</th>
						<th>偏执电流(2微安)</th>
						<th>发送字节数</th>
					</tr>
					<tr>
						<s:if
							test="#ponInfoOBJ.status=='N/A' || #ponInfoOBJ.status=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="status" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.txpower=='N/A' || #ponInfoOBJ.txpower=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="txpower" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.rxpower=='N/A' || #ponInfoOBJ.rxpower=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="rxpower" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.transceiverTemperature=='N/A' || #ponInfoOBJ.transceiverTemperature=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="transceiverTemperature" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.supplyVottage=='N/A' || #ponInfoOBJ.supplyVottage=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="supplyVottage" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.biasCurrent=='N/A' || #ponInfoOBJ.biasCurrent=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="biasCurrent" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.bytesSent=='N/A' || #ponInfoOBJ.bytesSent=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="bytesSent" /></td>
						</s:else>
					</tr>
					<tr>
						<th>接收字节数</th>
						<th>发送帧个数</th>
						<th>接收帧个数</th>
						<th>发送单波帧数</th>
						<th>接收单波帧数</th>
						<th>发送组播帧数</th>
						<th>接收组播帧数</th>
					</tr>
					<tr>
						<s:if
							test="#ponInfoOBJ.bytesReceived=='N/A' || #ponInfoOBJ.bytesReceived=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="bytesReceived" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.packetsSent=='N/A' || #ponInfoOBJ.packetsSent=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="packetsSent" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.packetsReceived=='N/A' || #ponInfoOBJ.packetsReceived=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="packetsReceived" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.sunicastPackets=='N/A' || #ponInfoOBJ.sunicastPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="sunicastPackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.runicastPackets=='N/A' || #ponInfoOBJ.runicastPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="runicastPackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.smulticastPackets=='N/A' || #ponInfoOBJ.smulticastPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="smulticastPackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.rmulticastPackets=='N/A' || #ponInfoOBJ.rmulticastPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="rmulticastPackets" /></td>
						</s:else>
					</tr>
					<tr>
						<th>发送广播波帧数</th>
						<th>接收广播波帧数</th>
						<th>接收FEC错误帧数</th>
						<th>接收HEC错误帧数</th>
						<th>发送方向丢帧数</th>
						<th>发送PAUSE流控制帧数</th>
						<th>接收PAUSE流控制帧数</th>
					</tr>
					<tr>
						<s:if
							test="#ponInfoOBJ.sbroadcastPackets=='N/A' || #ponInfoOBJ.sbroadcastPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="sbroadcastPackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.rbroadcastPackets=='N/A' || #ponInfoOBJ.rbroadcastPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="rbroadcastPackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.fecError=='N/A' || #ponInfoOBJ.fecError=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="fecError" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.hecError=='N/A' || #ponInfoOBJ.hecError=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="hecError" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.dropPackets=='N/A' || #ponInfoOBJ.dropPackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="dropPackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.spausePackets=='N/A' || #ponInfoOBJ.spausePackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="spausePackets" /></td>
						</s:else>
						<s:if
							test="#ponInfoOBJ.rpausePackets=='N/A' || #ponInfoOBJ.rpausePackets=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="rpausePackets" /></td>
						</s:else>
					</tr>
					</s:iterator>
					</s:if>
					<s:else>
										<tr>
											<td>
												<s:property value="wireMsg" />
											</td>
										</tr>
									</s:else>
				</table>

			</div>
			</s:if>
			<div class="content">
				<div class="itms_tit">
					<h2>管理通道信息</h2>
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="it_table">
					<tr>
						<th>PVC/VLAN</th>
						<th>URL</th>
						<th>登录用户名</th>
						<th>登录密码</th>
						<th>DHCP地址</th>
					</tr>
					<tr>
						<s:if test="#gwTr069OBJ.pvc=='N/A' || #gwTr069OBJ.pvc=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="gwTr069OBJ.pvc" /></td>
						</s:else>
						<s:if test="#gwTr069OBJ.url=='N/A' || #gwTr069OBJ.url=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="gwTr069OBJ.url" /></td>
						</s:else>
						<s:if
							test="#gwTr069OBJ.cpeUsername=='N/A' || #gwTr069OBJ.cpeUsername=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="gwTr069OBJ.cpeUsername" /></td>
						</s:else>
						<s:if
							test="#gwTr069OBJ.cpePasswd=='N/A' || #gwTr069OBJ.cpePasswd=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="gwTr069OBJ.cpePasswd" /></td>
						</s:else>
						<s:if
							test="#gwTr069OBJ.loopbackIp=='N/A' || #gwTr069OBJ.loopbackIp=='null' ">
							<td>-</td>
						</s:if>
						<s:else>
							<td><s:property value="gwTr069OBJ.loopbackIp" /></td>
						</s:else>
					</tr>
				</table>
			</div>
			 <div class="content">
      <div class="itms_tit">
        <h2>LAN侧信息</h2>
      </div>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
      <tr>
      	  <th colspan="7">DHCP地址池</th>
      	  </tr>
      	  <tr>
      	  <th>起始地址</th>
      	  <td>
      	  <s:if test="#gwLanHostconfMap.min_addr=='N/A' || #gwLanHostconfMap.min_addr=='null' ">
				-
		  </s:if>
		  <s:else>
		  <s:property value="gwLanHostconfMap.min_addr" />
		  </s:else>
      	  </td>
      	  <th>结束地址</th>
      	  <td><s:if
											test="#gwLanHostconfMap.max_addr=='N/A' || #gwLanHostconfMap.max_addr=='null' ">
											-
										</s:if>
										<s:else>
											<s:property value="gwLanHostconfMap.max_addr" />
										</s:else></td>
      	  </tr>
      </table>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
        <tr>
          <th>名称</th>
          <th>连接速率</th>
          <th>连接状态</th>
          <th>接收字节数</th>
          <th>发送字节数</th>
          <th>收包数</th>
          <th>发包数</th>
        </tr>
        <s:iterator value="lanEthList" var="lanEthList">
									<tr>
										<s:if
											test="#lanEthList.lan_eth_id=='N/A' || #lanEthList.lan_eth_id=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												LAN
												<s:property value="lan_eth_id" />
											</td>
										</s:else>
										<!-- <s:if test="#lanEthList.mac_address=='N/A' || #lanEthList.mac_address=='null' ">
											<td>-</td>
										</s:if>
										<s:else>
											<td><s:property value="mac_address"/></td>
										</s:else>
										 -->
										<s:if
											test="#lanEthList.max_bit_rate=='N/A' || #lanEthList.max_bit_rate=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="max_bit_rate" />
											</td>
										</s:else>
										<s:if
											test="#lanEthList.status=='N/A' || #lanEthList.status=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="status" />
											</td>
										</s:else>
										<s:if
											test="#lanEthList.byte_rece=='N/A' || #lanEthList.byte_rece=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="byte_rece" />
											</td>
										</s:else>
										<s:if
											test="#lanEthList.byte_sent=='N/A' || #lanEthList.byte_sent=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="byte_sent" />
											</td>
										</s:else>
										<s:if
											test="#lanEthList.pack_rece=='N/A' || #lanEthList.pack_rece=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="pack_rece" />
											</td>
										</s:else>
										<s:if
											test="#lanEthList.pack_sent=='N/A' || #lanEthList.pack_sent=='null' ">
											<td>
												-
											</td>
										</s:if>
										<s:else>
											<td>
												<s:property value="pack_sent" />
											</td>
										</s:else>
									</tr>
								</s:iterator>
      </table>
    </div>
    <div class="content">
      <div class="itms_tit">
        <h2>WLAN侧信息</h2>
      </div>
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="it_table">
        <tr>
          <th>SSID名称</th>
          <th>当前功率</th>
          <th>信道</th>
          <th>连接状态</th>
          <th>连接设备数</th>
          <th>是否启用</th>
          <th>隐藏</th>
          <th>广播</th>
          <th>加密方式</th>
          <th>802.11工作模式</th>
        </tr>
        <s:iterator value="wlanList" var="wideNetInfo">
									<tr align="center" bgcolor="#FFFFFF">
										<s:if
											test="#wideNetInfo.ssid=='N/A' || #wideNetInfo.ssid=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<s:property value="ssid" />
											</TD>
										</s:else>
										<!--
										<s:if test="#wideNetInfo.powervalue=='N/A' || #wideNetInfo.powervalue=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
											<TD align="center"><s:property value="powervalue"/></TD>
										</s:else>
										 -->
										<TD align="center">
											<s:property value="powervalue" />
										</TD>
										<s:if
											test="#wideNetInfo.channel_in_use=='N/A' || #wideNetInfo.channel_in_use=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<s:property value="channel_in_use" />
											</TD>
										</s:else>
										<s:if
											test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<s:property value="status" />
											</TD>
										</s:else>
										<s:if
											test="#wideNetInfo.associatedNum=='N/A' || #wideNetInfo.associatedNum=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<a
													href="javascript:detailDevice('<s:property value="device_id"/>','<s:property value="lan_id"/>','<s:property value="lan_wlan_id"/>');">
													<s:property value="associated_num" /> </a>
											</TD>
										</s:else>
										<!--
										<s:if test="#wideNetInfo.enable=='N/A' || #wideNetInfo.enable=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
										-->
										<s:if test="#wideNetInfo.enable==1">
											<TD align="center">
												是
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												否
											</TD>
										</s:else>
										<!--
										</s:else>
										<s:if test="#wideNetInfo.hide=='N/A' || #wideNetInfo.hide=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
										-->
										<s:if test="#wideNetInfo.hide==1">
											<TD align="center">
												是
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												否
											</TD>
										</s:else>
										<!--
										</s:else>
										<s:if test="#wideNetInfo.radio_enable=='N/A' || #wideNetInfo.radio_enable=='null' ">
											<TD align="center">-</TD>
										</s:if>
										<s:else>
										-->
										<s:if test="#wideNetInfo.radio_enable==1">
											<TD align="center">
												是
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												否
											</TD>
										</s:else>
										<!-- </s:else>-->
										<s:if
											test="#wideNetInfo.beacontype=='N/A' || #wideNetInfo.beacontype=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<s:property value="beacontype" />
											</TD>
										</s:else>
										<s:if
											test="#wideNetInfo.standard=='N/A' || #wideNetInfo.standard=='null' ">
											<TD align="center">
												-
											</TD>
										</s:if>
										<s:else>
											<TD align="center">
												<s:property value="standard" />
											</TD>
										</s:else>
									</tr>
								</s:iterator>
      </table>
    </div>
		</div>
	</div>
</body>
</html>
