<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����·�</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
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
			<th width="15%" >�����ڵ�����</th>
			<th width="42%">�����ڵ�·��</th>
			<th width="43%">�����ڵ�ֵ</th>
		</tr>
	</thead>
		<tbody>
		<s:if test='bssIssuedConfigMap["mode_type"]=="10"'>
				<s:if test='bssIssuedConfigMap["access_style_id"]=="1"' >
						<tr>
							<td>��·����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.LinkType</td>
							<td>EoA</td>
						</tr>
						<tr>
							<td>PVC</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.DestinationAddress</td>
							<td><s:property value="bssIssuedConfigMap['vpiid']" /></td>
						</tr>
						<tr>
							<td>��·����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.Enable</td>
							<td>1</td>
						</tr>
					</s:if>	
					<s:if test='bssIssuedConfigMap["access_style_id"]=="2"'>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
						
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="3"'>
						<tr>
							<td>�Ƿ�����VLAN��־</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANEnabled</td>
							<td>2</td>
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="4"'>
						<tr>
							<td>�Ƿ�����VLAN��־</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANEnabled</td>
							<td>2</td>
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="1"' >
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<td>PPPoE_Bridged</td>
						</tr>
						
						<tr>
							<td>ҵ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>INTERNET</td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						
						<tr>
							<td>����˿ڻ�SSID</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="2"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						
						<tr>
							<td>�û���</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Username</td>
							<td><s:property value="bssIssuedConfigMap['username']" /></td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Password</td>
							<td><s:property value="bssIssuedConfigMap['passwd']" /></td>
						</tr>
						
						<tr>
							<td>ҵ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>INTERNET</td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>����˿ڻ�SSID</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="3"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						<tr>
							<td>��ַ���䷽ʽ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<td>DHCP</td>
						</tr>
						<tr>
							<td>ҵ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>INTERNET</td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>����˿ڻ�SSID</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="4"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						<tr>
							<td>��ַ���䷽ʽ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<td>Static</td>
						</tr>
						<tr>
							<td>ip��ַ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ExternalIPAddress</td>
							<td><s:property value="bssIssuedConfigMap['ipaddress']" /></td>
						</tr>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.SubnetMask</td>
							<td><s:property value="bssIssuedConfigMap['ipmask']" /></td>
						</tr>
						<tr>
							<td>Ĭ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DefaultGateway</td>
							<td><s:property value="bssIssuedConfigMap['gateway']" /></td>
						</tr>
						<tr>
							<td>DNS</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DNSServers</td>
							<td><s:property value="bssIssuedConfigMap['adsl_ser']" /></td>
						</tr>
						<tr>
							<td>ҵ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>INTERNET</td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>����˿ڻ�SSID</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
		</s:if>
		
		<s:if test='bssIssuedConfigMap["mode_type"]=="11"'>
					<s:if test='bssIssuedConfigMap["access_style_id"]=="1"' >
						<tr>
							<td>��·����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.LinkType</td>
							<td>EoA</td>
						</tr>
						<tr>
							<td>PVC</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.DestinationAddress</td>
							<td><s:property value="bssIssuedConfigMap['vpiid']" /></td>
						</tr>
						<tr>
							<td>��·����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.Enable</td>
							<td>1</td>
						</tr>
					</s:if>	
					<s:if test='bssIssuedConfigMap["access_style_id"]=="2"'>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
						
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="3"'>
						<tr>
							<td>�Ƿ�����VLAN��־</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANEnabled</td>
							<td>2</td>
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="4"'>
						<tr>
							<td>�Ƿ�����VLAN��־</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANEnabled</td>
							<td>2</td>
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
					</s:if>
				
					
						<s:if test='bssIssuedConfigMap["wan_type"]=="1"' >
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<td>PPPoE_Bridged</td>
						</tr>
						
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>IPTV</td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="2"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						
						<tr>
							<td>�û���</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Username</td>
							<td><s:property value="bssIssuedConfigMap['username']" /></td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Password</td>
							<td><s:property value="bssIssuedConfigMap['passwd']" /></td>
						</tr>
						
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>IPTV</td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="3"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						<tr>
							<td>��ַ���䷽ʽ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<td>DHCP</td>
						</tr>
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>IPTV</td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="4"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						<tr>
							<td>��ַ���䷽ʽ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<td>Static</td>
						</tr>
						<tr>
							<td>ip��ַ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ExternalIPAddress</td>
							<td><s:property value="bssIssuedConfigMap['ipaddress']" /></td>
						</tr>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.SubnetMask</td>
							<td><s:property value="bssIssuedConfigMap['ipmask']" /></td>
						</tr>
						<tr>
							<td>Ĭ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DefaultGateway</td>
							<td><s:property value="bssIssuedConfigMap['gateway']" /></td>
						</tr>
						<tr>
							<td>DNS</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DNSServers</td>
							<td><s:property value="bssIssuedConfigMap['adsl_ser']" /></td>
						</tr>
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>IPTV</td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
		</s:if>
		<s:if test='bssIssuedConfigMap["mode_type"]=="14"'>
							<s:if test='bssIssuedConfigMap["access_style_id"]=="1"' >
						<tr>
							<td>��·����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.LinkType</td>
							<td>EoA</td>
						</tr>
						<tr>
							<td>PVC</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.DestinationAddress</td>
							<td><s:property value="bssIssuedConfigMap['vpiid']" /></td>
						</tr>
						<tr>
							<td>��·����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANDSLLinkConfig.Enable</td>
							<td>1</td>
						</tr>
					</s:if>	
					<s:if test='bssIssuedConfigMap["access_style_id"]=="2"'>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
						
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="3"'>
						<tr>
							<td>�Ƿ�����VLAN��־</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANEnabled</td>
							<td>2</td>
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["access_style_id"]=="4"'>
						<tr>
							<td>�Ƿ�����VLAN��־</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANEnabled</td>
							<td>2</td>
						</tr>
						<tr>
							<td>VLANID</td>  
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{i}.WANIPConnection.{i}.X_CU_VLANID</td>
							<td><s:property value="bssIssuedConfigMap['vlanid']" /></td>
						</tr>
					</s:if>
					
							<s:if test='bssIssuedConfigMap["wan_type"]=="1"' >
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<td>PPPoE_Bridged</td>
						</tr>
						
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>VOIP</td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="2"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						
						<tr>
							<td>�û���</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Username</td>
							<td><s:property value="bssIssuedConfigMap['username']" /></td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Password</td>
							<td><s:property value="bssIssuedConfigMap['passwd']" /></td>
						</tr>
						
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>VOIP</td>
						</tr>
						
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANPPPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="3"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						<tr>
							<td>��ַ���䷽ʽ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<td>DHCP</td>
						</tr>
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>VOIP</td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
					<s:if test='bssIssuedConfigMap["wan_type"]=="4"'>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ConnectionType</td>
							<td>IP_Routed</td>
						</tr>
						<tr>
							<td>��ַ���䷽ʽ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.AddressingType</td>
							<td>Static</td>
						</tr>
						<tr>
							<td>ip��ַ</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.ExternalIPAddress</td>
							<td><s:property value="bssIssuedConfigMap['ipaddress']" /></td>
						</tr>
						<tr>
							<td>��������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.SubnetMask</td>
							<td><s:property value="bssIssuedConfigMap['ipmask']" /></td>
						</tr>
						<tr>
							<td>Ĭ������</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DefaultGateway</td>
							<td><s:property value="bssIssuedConfigMap['gateway']" /></td>
						</tr>
						<tr>
							<td>DNS</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.DNSServers</td>
							<td><s:property value="bssIssuedConfigMap['adsl_ser']" /></td>
						</tr>
						<tr>
							<td>ҵ���б�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_ServiceList</td>
							<td>VOIP</td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.Enable</td>
							<td>1</td>
						</tr>
						<tr>
							<td>�󶨶˿�</td>
							<td>InternetGatewayDevice.WANDevice.{i}.WANConnectionDevice.{j}.WANIPConnection.{k}.X_CU_LanInterface</td>
							<td><s:property value="bssIssuedConfigMap['bind_port']" /></td>
						</tr>
					</s:if>
					
				<s:if test='bssIssuedConfigMap["protocol"]=="1"'>
						<tr>
							<td>�������������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServer</td>
							<td><s:property value="bssIssuedConfigMap['prox_serv']" /></td>
						</tr>
						<tr>
							<td>��������������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServerPort</td>
							<td><s:property value="bssIssuedConfigMap['prox_port']" /></td>
						</tr>
						<tr>
							<td>���ô��������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryProxyServer</td>
							<td><s:property value="bssIssuedConfigMap['stand_prox_serv']" /></td>
						</tr>
						<tr>
							<td>���ô���������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryProxyServerPort</td>
							<td><s:property value="bssIssuedConfigMap['stand_prox_port']" /></td>
						</tr>
						<tr>
							<td>ע�������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServer</td>
							<td><s:property value="bssIssuedConfigMap['regi_serv']" /></td>
						</tr>
						<tr>
							<td>ע��������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServerPort</td>
							<td><s:property value="bssIssuedConfigMap['regi_port']" /></td>
						</tr>
						<tr>
							<td>����ע�������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryRegistrarServer</td>
							<td><s:property value="bssIssuedConfigMap['stand_regi_serv']" /></td>
						</tr>
						<tr>
							<td>����ע��������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryRegistrarServerPort</td>
							<td><s:property value="bssIssuedConfigMap['stand_regi_port']" /></td>
						</tr>
						<tr>
							<td>Outboud��������ַ</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxy</td>
							<td><s:property value="bssIssuedConfigMap['out_bound_proxy']" /></td>
						</tr>
						<tr>
							<td>Outboud�������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxyPort</td>
							<td><s:property value="bssIssuedConfigMap['out_bound_port']" /></td>
						</tr>
						<tr>
							<td>��Outboud��������ַ</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryOutboundProxyServer</td>
							<td><s:property value="bssIssuedConfigMap['stand_out_bound_proxy']" /></td>
						</tr>
						<tr>
							<td>����Outboud�������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryOutboundProxy ServerPort</td>
							<td><s:property value="bssIssuedConfigMap['stand_out_bound_port']" /></td>
						</tr>
						<tr>
							<td>��֤�û���</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthUserName</td>
							<td><s:property value="bssIssuedConfigMap['voip_username']" /></td>
						</tr>
						<tr>
							<td>��֤�û�����</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthPassword</td>
							<td><s:property value="bssIssuedConfigMap['voip_passwd']" /></td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.Enable</td>
							<td>Enabled</td>
						</tr>
					</s:if>
				
				
				<s:if test='bssIssuedConfigMap["protocol"]=="0"'>
						<tr>
							<td>�������������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServer</td>
							<td><s:property value="bssIssuedConfigMap['prox_serv']" /></td>
						</tr>
						<tr>
							<td>��������������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.ProxyServerPort</td>
							<td><s:property value="bssIssuedConfigMap['prox_port']" /></td>
						</tr>
						<tr>
							<td>���ô��������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryProxyServer</td>
							<td><s:property value="bssIssuedConfigMap['stand_prox_serv']" /></td>
						</tr>
						<tr>
							<td>���ô���������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryProxyServerPort</td>
							<td><s:property value="bssIssuedConfigMap['stand_prox_port']" /></td>
						</tr>
						<tr>
							<td>ע�������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServer</td>
							<td><s:property value="bssIssuedConfigMap['regi_serv']" /></td>
						</tr>
						<tr>
							<td>ע��������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.RegistrarServerPort</td>
							<td><s:property value="bssIssuedConfigMap['regi_port']" /></td>
						</tr>
						<tr>
							<td>����ע�������������IP</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryRegistrarServer</td>
							<td><s:property value="bssIssuedConfigMap['stand_regi_serv']" /></td>
						</tr>
						<tr>
							<td>����ע��������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryRegistrarServerPort</td>
							<td><s:property value="bssIssuedConfigMap['stand_regi_port']" /></td>
						</tr>
						<tr>
							<td>Outboud��������ַ</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxy</td>
							<td><s:property value="bssIssuedConfigMap['out_bound_proxy']" /></td>
						</tr>
						<tr>
							<td>Outboud�������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.OutboundProxyPort</td>
							<td><s:property value="bssIssuedConfigMap['out_bound_port']" /></td>
						</tr>
						<tr>
							<td>����Outboud��������ַ</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryOutboundProxyServer</td>
							<td><s:property value="bssIssuedConfigMap['stand_out_bound_proxy']" /></td>
						</tr>
						<tr>
							<td>����Outboud�������˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.SIP.X_CU_SecondaryOutboundProxyServerPort</td>
							<td><s:property value="bssIssuedConfigMap['stand_out_bound_port']" /></td>
						</tr>
						<tr>
							<td>��֤�˺�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthUserName</td>
							<td><s:property value="bssIssuedConfigMap['voip_username']" /></td>
						</tr>
						<tr>
							<td>��֤����</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.AuthPassword</td>
							<td><s:property value="bssIssuedConfigMap['voip_passwd']" /></td>
						</tr>
						<tr>
							<td>�û��绰����</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.SIP.URI</td>
							<td><s:property value="bssIssuedConfigMap['uri']" /></td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.Enable</td>
							<td>Enabled</td>
						</tr>
					</s:if>
					<s:if test='bssIssuedConfigMap["protocol"]=="2"'>
						<tr>
							<td>����MGC��IP������</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.CallAgent1</td>
							<td><s:property value="bssIssuedConfigMap['prox_serv']" /></td>
						</tr>
						<tr>
							<td>����MGC�Ķ˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.CallAgentPort1</td>
							<td><s:property value="bssIssuedConfigMap['prox_port']" /></td>
						</tr>
						<tr>
							<td>����MGC��IP������</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.CallAgent2</td>
							<td><s:property value="bssIssuedConfigMap['stand_prox_serv']" /></td>
						</tr>
						<tr>
							<td>����MGC�Ķ˿�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.CallAgentPort2</td>
							<td><s:property value="bssIssuedConfigMap['stand_prox_port']" /></td>
						</tr>
						<tr>
							<td>UDP�˿ں�</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.MediaGatewayPort</td>
							<td><s:property value="bssIssuedConfigMap['prox_port']" /></td>
						</tr>
						<tr>
							<td>�ն�ȫ��Ψһ�ı�ʶ</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.DeviceID</td>
							<td><s:property value="bssIssuedConfigMap['reg_id']" /></td>
						</tr>
						<tr>
							<td>�ն˱�ʶ������</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.X_CU_H248.DeviceIDType</td>
							<td><s:property value="bssIssuedConfigMap['reg_id_type']" /></td>
						</tr>
						<tr>
							<td>�ն���</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.X_CU_H248.LineName </td>
							<td><s:property value="bssIssuedConfigMap['voip_port']" /></td>
						</tr>
						<tr>
							<td>����</td>
							<td>InternetGatewayDevice.Services.VoiceService.{i}.VoiceProfile.{j}.Line.{k}.Enable</td>
							<td>Enabled</td>
						</tr>
					</s:if>
				</s:if>
	</tbody>
	
	<tfoot>
		<TR>
			<TD align="right" colspan=3>
				<a href="javascript:configDetailClose();">&nbsp;�� ��&nbsp;</a>
			</TD>
		</TR>
	</tfoot>
</table>
</body>
</html>
<%@ include file="../../foot.jsp"%>