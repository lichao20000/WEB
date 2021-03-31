<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>配置下发</title>
<link rel="stylesheet" href="<s:url value="/css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/inmp/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/inmp/jQuerySplitPage-linkage.js"/>"></script>
<style type="text/css">
	table.listtable tbody td{
    	word-wrap:break-word;
    	word-break:break-all;
	}
</style>
</head>
<body>
<table class="listtable" id="listTable" style="table-layout: fixed ;">
	<thead>
		<tr>
			<th width="20%" >参数节点名称</th>
			<th width="80%">参数节点路径</th>
			<!--  <th width="43%">参数节点值</th> -->
		</tr>
	</thead>
		<tbody>
		<s:if test='bssIssuedConfigMap["mode_type"]=="10"'>
				<s:if test='bssIssuedConfigMap["access_style_id"]=="1"' >
						<tr>
							<td>链路类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.LinkType</td>
							<!-- <td>EoA</td> -->	
						</tr>
						<tr>
							<td>PVC</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.DestinationAddress</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vpiid']" /></td> -->
							
						</tr>
						<tr>
							<td>链路开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
					</s:if>	
					<s:if test='bssIssuedConfigMap["access_style_id"]=="2"'>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANEthernetLinkConfig.X_CT-COM_VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td>  -->
						</tr>
						<tr>
							<td>802.1p标签</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANEthernetLinkConfig.X_CT-COM_802-1pMark</td>
							<!-- <td>0</td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="3"'>
						<tr>
							<td>是否启用VLAN标志</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANEponLinkConfig.Mode</td>
							<!-- <td>2</td>  -->
							
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANEponLinkConfig.VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="4"'>
						<tr>
							<td>是否启用VLAN标志</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANGponLinkConfig.Mode=2</td>
							<!-- <td>2</td> -->
							
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANGponLinkConfig.VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="1"' >
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<!-- <td>PPPoE_Bridged</td> -->
						</tr>
						
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>INTERNET</td> -->
						</tr>
						
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
						</tr>
						
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td>  -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="2"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
						</tr>
						
						<tr>
							<td>用户名</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Username</td>
							<!-- <td><s:property value="bssIssuedConfigMap['username']" /></td> -->
						</tr>
						
						<tr>
							<td>密码</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Password</td>
							<!-- <td><s:property value="bssIssuedConfigMap['passwd']" /></td> -->
						</tr>
						
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>INTERNET</td>  -->
						</tr>
						
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="3"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
						</tr>
						<tr>
							<td>地址分配方式</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<!-- <td>DHCP</td> -->
						</tr>
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>INTERNET</td>  -->
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="4"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
						</tr>
						<tr>
							<td>地址分配方式</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<!-- <td>Static</td> -->
						</tr>
						<tr>
							<td>ip地址</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ExternalIPAddress</td>
							<!-- <td><s:property value="bssIssuedConfigMap['ipaddress']" /></td>  -->
						</tr>
						<tr>
							<td>子网掩码</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.SubnetMask</td>
							<!-- <td><s:property value="bssIssuedConfigMap['ipmask']" /></td> -->
						</tr>
						<tr>
							<td>默认网关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DefaultGateway</td>
							<!--  <td><s:property value="bssIssuedConfigMap['gateway']" /></td> -->
							
						</tr>
						<tr>
							<td>DNS</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DNSServers</td>
							<!-- <td><s:property value="bssIssuedConfigMap['adsl_ser']" /></td>  -->
						</tr>
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>INTERNET</td>  -->
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td>  -->
						</tr>
					</s:if>
		</s:if>
		
		<s:if test='bssIssuedConfigMap["mode_type"]=="11"'>
					<s:if test='bssIssuedConfigMap["access_style_id"]=="1"' >
						<tr>
							<td>链路类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.LinkType</td>
							<!-- <td>EoA</td> -->
						</tr>
						<tr>
							<td>PVC</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.DestinationAddress</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vpiid']" /></td> -->
						</tr>
						<tr>
							<td>链路开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.Enable</td>
							<!--  <td>1</td> -->
						</tr>
					</s:if>	
					<s:if test='bssIssuedConfigMap["access_style_id"]=="2"'>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANEthernetLinkConfig.X_CT-COM_VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td>  -->
						</tr>
						<tr>
							<td>802.1p标签</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANEthernetLinkConfig.X_CT-COM_802-1pMark</td>
							<!-- <td>0</td> -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="3"'>
						<tr>
							<td>是否启用VLAN标志</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANEponLinkConfig.Mode</td>
							<!-- <td>2</td>  -->
							
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANEponLinkConfig.VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="4"'>
						<tr>
							<td>是否启用VLAN标志</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANGponLinkConfig.Mode=2</td>
							<!-- <td>2</td> -->
							
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANGponLinkConfig.VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
							
						</tr>
					</s:if>
				
					
						<s:if test='bssIssuedConfigMap["wan_type"]=="1"' >
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<!-- <td>PPPoE_Bridged</td> -->
							
						</tr>
						
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>OTHER</td> -->
							
						</tr>
						
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="2"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
							
						</tr>
						
						<tr>
							<td>用户名</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Username</td>
							<!-- <td><s:property value="bssIssuedConfigMap['username']" /></td> -->
							
						</tr>
						
						<tr>
							<td>密码</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Password</td>
							<!-- <td><s:property value="bssIssuedConfigMap['passwd']" /></td> -->
							
						</tr>
						
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>OTHER</td> -->
							
						</tr>
						
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="3"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
							
						</tr>
						<tr>
							<td>地址分配方式</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<!-- <td>DHCP</td> -->
							
						</tr>
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>OTHER</td> -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="4"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
							
						</tr>
						<tr>
							<td>地址分配方式</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<!-- <td>Static</td> -->
							
						</tr>
						<tr>
							<td>ip地址</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ExternalIPAddress</td>
							<!-- <td><s:property value="bssIssuedConfigMap['ipaddress']" /></td> -->
							
						</tr>
						<tr>
							<td>子网掩码</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.SubnetMask</td>
							<!-- <td><s:property value="bssIssuedConfigMap['ipmask']" /></td> -->
							
						</tr>
						<tr>
							<td>默认网关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DefaultGateway</td>
							<!-- <td><s:property value="bssIssuedConfigMap['gateway']" /></td> -->
							
						</tr>
						<tr>
							<td>DNS</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DNSServers</td>
							<!-- <td><s:property value="bssIssuedConfigMap['adsl_ser']" /></td> -->
						</tr>
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>OTHER</td> -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
							
						</tr>
					</s:if>
		</s:if>
		<s:if test='bssIssuedConfigMap["mode_type"]=="14"'>
							<s:if test='bssIssuedConfigMap["access_style_id"]=="1"' >
						<tr>
							<td>链路类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.LinkType</td>
							<!-- <td>EoA</td> -->
							
						</tr>
						<tr>
							<td>PVC</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.DestinationAddress</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vpiid']" /></td> -->
							
						</tr>
						<tr>
							<td>链路开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
					</s:if>	
					<s:if test='bssIssuedConfigMap["access_style_id"]=="2"'>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANEthernetLinkConfig.X_CT-COM_VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
							
						</tr>
						<tr>
							<td>802.1p标签</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANEthernetLinkConfig.X_CT-COM_802-1pMark</td>
							<!-- <td>0</td>  -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="3"'>
						<tr>
							<td>是否启用VLAN标志</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANEponLinkConfig.Mode</td>
							<!-- <td>2</td> -->
							
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANEponLinkConfig.VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
							
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="4"'>
						<tr>
							<td>是否启用VLAN标志</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANGponLinkConfig.Mode=2</td>
							<!-- <td>2</td> -->
							
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.X_CT-COM_WANGponLinkConfig.VLANIDMark</td>
							<!-- <td><s:property value="bssIssuedConfigMap['vlanid']" /></td> -->
							
						</tr>
					</s:if>
					
							<s:if test='bssIssuedConfigMap["wan_type"]=="1"' >
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<!-- <td>PPPoE_Bridged</td> -->
							
						</tr>
						
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>VOIP</td> -->
							
						</tr>
						
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="2"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
							
						</tr>
						
						<tr>
							<td>用户名</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Username</td>
							<!-- <td><s:property value="bssIssuedConfigMap['username']" /></td> -->
						</tr>
						
						<tr>
							<td>密码</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Password</td>
							<!-- <td><s:property value="bssIssuedConfigMap['passwd']" /></td>  -->
							
						</tr>
						
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>VOIP</td> -->
							
						</tr>
						
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="3"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td>  -->
							
						</tr>
						<tr>
							<td>地址分配方式</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<!-- <td>DHCP</td>  -->
							
						</tr>
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>VOIP</td> -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<!-- <td>1</td>  -->
							
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="4"'>
						<tr>
							<td>连接类型</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<!-- <td>IP_Routed</td> -->
							
						</tr>
						<tr>
							<td>地址分配方式</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<!-- <td>Static</td> -->
							
						</tr>
						<tr>
							<td>ip地址</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ExternalIPAddress</td>
							<!-- <td><s:property value="bssIssuedConfigMap['ipaddress']" /></td> -->
							
						</tr>
						<tr>
							<td>子网掩码</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.SubnetMask</td>
							<!-- <td><s:property value="bssIssuedConfigMap['ipmask']" /></td> -->
							
						</tr>
						<tr>
							<td>默认网关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DefaultGateway</td>
							<!--  <td><s:property value="bssIssuedConfigMap['gateway']" /></td> -->
							
						</tr>
						<tr>
							<td>DNS</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DNSServers</td>
							<!-- <td><s:property value="bssIssuedConfigMap['adsl_ser']" /></td> -->
							
						</tr>
						<tr>
							<td>业务列表</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_ServiceList</td>
							<!-- <td>VOIP</td> -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<!-- <td>1</td> -->
							
						</tr>
						<tr>
							<td>绑定端口</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CT-COM_LanInterface</td>
							<!-- <td><s:property value="bssIssuedConfigMap['bind_port']" /></td> -->
							
						</tr>
					</s:if>
					
				<s:if test='bssIssuedConfigMap["protocol"]=="1"'>
						<tr>
							<td>SIP服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>IP服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>备用SIP服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-ProxyServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用SIP服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-ProxyServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>Registrar服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['regi_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>Registrar服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['regi_port']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Registrar服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-RegistrarServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_regi_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Registrar服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-RegistrarServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_regi_port']" /></td> -->
							
						</tr>
						<tr>
							<td>Outbound服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxy</td>
							<!-- <td><s:property value="bssIssuedConfigMap['out_bound_proxy']" /></td> -->
							
						</tr>
						<tr>
							<td>Outbound服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxyPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Outbound服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-OutboundProxy</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td>  -->
							
						</tr>
						<tr>
							<td>备用Outbound服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-OutboundProxyPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>认证账号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthUserName</td>
							<!-- <td><s:property value="bssIssuedConfigMap['voip_username']" /></td> -->
							
						</tr>
						<tr>
							<td>认证密码</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthPassword</td>
							<!-- <td><s:property value="bssIssuedConfigMap['voip_passwd']" /></td>  -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.Enable</td>
							<!-- <td>Enabled</td> -->
							
						</tr>
					</s:if>
				
				
				<s:if test='bssIssuedConfigMap["protocol"]=="0"'>
						<tr>
							<td>SIP服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td>  -->
							
						</tr>
						<tr>
							<td>IP服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>备用SIP服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-ProxyServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用SIP服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-ProxyServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>Registrar服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['regi_serv']" /></td>  -->
							
						</tr>
						<tr>
							<td>Registrar服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['regi_port']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Registrar服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-RegistrarServer</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_regi_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Registrar服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-RegistrarServerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_regi_port']" /></td> -->
							
						</tr>
						<tr>
							<td>Outbound服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxy</td>
							<!-- <td><s:property value="bssIssuedConfigMap['out_bound_proxy']" /></td> -->
							
						</tr>
						<tr>
							<td>Outbound服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxyPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Outbound服务器</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-OutboundProxy</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用Outbound服务器端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CT-COM_Standby-OutboundProxyPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td>  -->
							
						</tr>
						<tr>
							<td>认证账号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthUserName</td>
							<!-- <td><s:property value="bssIssuedConfigMap['voip_username']" /></td> -->
							
						</tr>
						<tr>
							<td>认证密码</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthPassword</td>
							<!-- <td><s:property value="bssIssuedConfigMap['voip_passwd']" /></td> -->
							
						</tr>
						<tr>
							<td>用户电话号码</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.URI</td>
							<!-- <td><s:property value="bssIssuedConfigMap['uri']" /></td>  -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.Enable</td>
							<!-- <td>Enabled</td> -->
							
						</tr>
					</s:if>
					<s:if test='bssIssuedConfigMap["protocol"]=="2"'>
						<tr>
							<td>软交换IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.MediaGatewayControler</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>软交换端口</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.MediaGatewayControlerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>备用软交换IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.Standby-MediaGatewayControler</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_prox_serv']" /></td> -->
							
						</tr>
						<tr>
							<td>备用软交换端口</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.Standby-MediaGatewayControlerPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['stand_prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>UDP端口号</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.MediaGatewayPort</td>
							<!-- <td><s:property value="bssIssuedConfigMap['prox_port']" /></td> -->
							
						</tr>
						<tr>
							<td>终端全局唯一的标识</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.DeviceID</td>
							<!-- <td><s:property value="bssIssuedConfigMap['reg_id']" /></td> -->
							
						</tr>
						<tr>
							<td>终端标识的类型</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CT-COM_H248.DeviceIDType</td>
							<!-- <td><s:property value="bssIssuedConfigMap['reg_id_type']" /></td>  -->
							
						</tr>
						<tr>
							<td>终端的物理标示</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.X_CT-COM_H248.PhysicalTermID</td>
							<!-- <td><s:property value="bssIssuedConfigMap['voip_port']" /></td> -->
							
						</tr>
						<tr>
							<td>开关</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.Enable</td>
							<!-- <td>Enabled</td>  -->
							
						</tr>
					</s:if>
				</s:if>
	</tbody>
	
	<tfoot>
		<TR>
			<TD align="right" colspan=3>
				<a href="javascript:configDetailClose();">&nbsp;关 闭&nbsp;</a>
			</TD>
		</TR>
	</tfoot>
</table>
</body>
</html>
<%@ include file="../foot.jsp"%>