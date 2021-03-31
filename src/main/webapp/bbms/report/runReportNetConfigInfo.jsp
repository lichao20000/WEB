<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
</SCRIPT>

<TABLE align="right" border=0 cellspacing=0 cellpadding=0 width="100%" >
	<tr bgcolor="#999999">
		<td colspan="6" >
			<table width="100%" border="0" cellpadding="2" cellspacing="1" >
				<tr align="left" width="25">
					<td colspan="6"  align="left" class="green_title_left">
					   	网关安全配置 
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td  class="column" width="10%" align="center">
					   	访问控制
					</td>
					<td  colspan="5" align="center">
					   	<s:if test='"1"==accessControl'>开启</s:if>
					   	<s:else >关闭</s:else>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column rowspan="2" align="center" width="10%">
					 	内容过滤
					</td>
					<td class=column width="15%" align="center">
						WEB过滤
					</td>
					<td class=column width="15%" align="center">
						收邮件过滤
					</td>
					<td class=column width="15%" align="center">
						发邮件过滤
					</td>	
					<td  class=column width="15%" align="center">
						文件过滤
					</td>
					<td class=column width="15%" align="center">
						日志使能
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="15%" align="center">
						<s:if test='"1"==filterContentMap.http_filter_enabled'>开启</s:if>
						<s:else >关闭</s:else>
					</td>
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.file_filter_enable'>开启</s:if>
						<s:else >关闭</s:else>
					</td>
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.log_enable'>开启</s:if>
						<s:else >关闭</s:else>
					</td>	
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.smtp_filter_enabled'>开启</s:if>
						<s:else >关闭</s:else>
					</td>
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.pop3_filter_enabled'>开启</s:if>
						<s:else >关闭</s:else>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor="#999999">
	   <td>
			<table width="100%" border="0" cellpadding="2" cellspacing="1">
				<tr bgcolor="#FFFFFF">
					<td class=column  align="center" width="10%">
								上网控制
					</td>
					<td class=column width="15%" align="right">
						入侵检测
					</td>
					<td  width="15%" align="center">
						<s:if test='"1"==netSecStr'>开启</s:if>
						<s:else>关闭</s:else>
					</td>
					<td class=column   width="15%" align="right">
						防病毒
					</td>
					<td  width="15%" align="center">
						<s:if test='"1"==netSerVir'>开启</s:if>
						<s:else>关闭</s:else>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column  align="center" width="10%" >
								VPN认证
					</td>
					<td class=column  width="20%" align="right">
						状态
					</td>
					<td   width="20%" align="center">
						<s:if test='"1"==vpnInfo.enable'>开启</s:if>
						<s:else>关闭</s:else>
					</td>
					<td class=column   width="20%" align="right">
						模式
					</td>
					<td   width="20%" align="center">
						<s:property value="vpnInfo.type"/>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column   align="center" width="10%">
								防火墙
					</td>
					<td class=column  width="20%" align="right">
						状态
					</td>
					<td  width="20%" align="center">
						<s:if test='"1"==fireWallMap.enable'>开启</s:if>
						<s:else>关闭</s:else>
					</td>
					<td class=column  width="20%" align="right" >
						级别
					</td>
					<td   width="20%" align="center">
							<s:property value="fireWallMap.firewall_level"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor="#999999">
		<td > 
			<table width="100%" border="0" cellpadding="2" cellspacing="1 border="0" >
			<tr align="left" width="25">
					<td colspan="10"  align="left" class="green_title_left">
					   	网关端口信息 
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td rowspan='<s:property value="lanLen"/>'  class=column align="center">  <!--LAN口需要遍历 界面，行数待定 -->
					   	LAN口
					</td>
					<td class=column  width="10%" align="center">
						名称		
					</td>
					<td class=column  width="10%" align="center">
						开关
					</td>
					<td class=column  width="10%"align="center">
						端口状态
					</td>
					<td class=column width="10%" align="center"	>
						MAC地址
					</td>
					<td class=column width="10%" align="center">
						最大比特率
					</td>
					<td class=column width="10%" align="center">
						发送字节数
					</td>
					<td class=column width="10%" align="center">
						接受字节数
					</td>
					<td class=column width="10%" align="center">
						发送包数
					</td>
					<td class=column width="10%" align="center">
						接收包数
					</td>
				</tr>
			<!-- 遍历后台返回LAN口的结果 -->        
			<s:iterator value="lanLt" >
				<tr bgcolor="#FFFFFF">
					<td align="center" width="10%">
						LAN-<s:property value="lan_eth_id"/>
					</td>
					<td align="center" width="10%">
						<s:if test='"1"==eable'>开启</s:if>
						<s:else>关闭</s:else>
					</td>
					<td align="center" width="10%">
						<s:property value="status"/>
					</td>
					<td align="center" width="10%">
						<s:property value="mac_address"/>
					</td>
					<td align="center" width="10%">
						<s:property value="max_bit_rate"/>
					</td>
					<td align="center" width="10%">
						<s:property value="byte_sent"/>
					</td>
					<td align="center" width="10%">
						<s:property value="byte_rece"/>
					</td>
					<td align="center" width="10%">
						<s:property value="pack_sent"/>
					</td>
					<td align="center" width="10%">
						<s:property value="pack_rece"/>
					</td>
				</tr>
			</s:iterator>
			</table>
		</td>
	</tr>
	 <tr bgcolor="#999999">
	 <td >
	    <table  width="100%" border="0" cellpadding="2" cellspacing="1 border="0" >
			<tr bgcolor="#FFFFFF">
				<td rowspan='<s:property value="wanLen"/>'  class=column align="center">  <!--WAN口需要遍历 界面，行数待定 -->
					   	WAN口
					</td>
					<td class=column width="10%" align="center">
						名称		
					</td>
					<td class=column width="10%" align="center">
						业务类型
					</td>
					<td class=column width="10%" align="center">
						端口状态
					</td>
					<td class=column width="10%" align="center">
						接入方式
					</td>
					<td class=column width="10%" align="center">
						连接类型
					</td>
					<td class=column width="10%" align="center">
						宽带帐号
					</td>
					<td class=column width="10%" align="center">
						宽带密码
					</td>
					<td class=column width="10%" align="center">
						VLAN/PVC
					</td>
					<td class=column width="10%" align="center">
						IP地址
					</td>
			</tr>
			<!-- 遍历后台返回的WAN口结果 -->
			<s:iterator value="wanLt" >
				<tr bgcolor="#FFFFFF"> 
					<td align="center" width="10%">
						WAN<s:property value="wan_conn_id"/>-<s:property value="wan_conn_sess_id"/>
					</td>
					<td align="center" width="10%">
						<s:property value="serv_list"/>
					</td>
					<td align="center" width="10%">
						<s:property value="link_status"/>
					</td>
					<td align="center" width="10%">
						<s:property value="access_type"/>
					</td>
					<td align="center" width="10%">
						<s:property value="conn_type"/>
					</td>
					<td align="center" width="10%">
						<s:property value="username"/>
					</td>
					<td align="center" width="10%">
						<s:property value="password"/>
					</td>
					<td align="center" width="10%">       
						<s:if test='access_type=="DSL"'>
								<s:property value="vpi_id" />
								/<s:property value="vci_id" />
						</s:if>
						<s:else>
								<s:property value="vlan_id" />
						</s:else>
					</td>
					<td align="center" width="10%">
						<s:property value="ip"/>
					</td>
				</tr>
				
			</s:iterator>
		</table>
		<tr bgcolor="#999999">
		<td>
		<table  width="100%" border="0" cellpadding="2" cellspacing="1 border="0" >	
			<tr bgcolor="#FFFFFF">
				<td rowspan='<s:property value="wlanLen"/>'  class=column align="center">  <!--WAN口需要遍历 界面，行数待定 -->
					   	WLAN
					</td>
					<td class=column  width="10%" align="center">
						名称		
					</td>	
					<td class=column  width="10%" align="center">
						开关
					</td>
					<td class=column  width="10%" align="center">
						是否隐藏	
					</td>
					<td class=column  width="10%" align="center">
						发射功率
					</td>
					<td class=column width="10%" align="center">
						最大比特率
					</td>
					<td class=column r width="10%" align="center">
						信道
					</td>
					<td class=column  width="10%" align="center">
						总发送字节数
					</td>
					<td class=column  width="10%" align="center">
						总发送包数
					</td>
					<td class=column  width="10%" align="center">
						总接收包数
					</td>
			</tr>
			<!-- 遍历后台返回的WLAN的结果 -->
			<s:iterator value = "wlanLt"  status="sta">
				<tr bgcolor="#FFFFFF">
					<td align="center"  width="10%">
						WLAN-<s:property value="lan_wlan_id"/>
					</td>
					<td align="center"  width="10%">
						<s:if test='"1"==ap_enable'>开启</s:if>
						<s:else>关闭</s:else>
					</td>
					<td align="center"  width="10%">
						<s:if test='"1"==hide'>是</s:if>
						<s:else>否</s:else>
					</td>
					<td align="center"  width="10%">
						<s:property value = "powerlevel"/>
					</td>
					<td align="center"  width="10%">
						<s:property value = "channel_in_use"/>
					</td>
					<td align="center"  width="10%">
						<s:property value = "total_bytes_sent"/>
					</td>
					<td align="center"  width="10%">
						<s:property value = "total_bytes_received"/>
					</td>
					<td align="center"  width="10%">
						<s:property value = "total_packets_sent"/>
					</td>
					<td align="center"  width="10%">
						<s:property value = "total_packets_received"/>
					</td>
				</tr>
			</s:iterator>
			</table>
		</td>
	</tr>

</TABLE>