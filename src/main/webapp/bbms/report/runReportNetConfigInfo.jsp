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
					   	���ذ�ȫ���� 
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td  class="column" width="10%" align="center">
					   	���ʿ���
					</td>
					<td  colspan="5" align="center">
					   	<s:if test='"1"==accessControl'>����</s:if>
					   	<s:else >�ر�</s:else>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column rowspan="2" align="center" width="10%">
					 	���ݹ���
					</td>
					<td class=column width="15%" align="center">
						WEB����
					</td>
					<td class=column width="15%" align="center">
						���ʼ�����
					</td>
					<td class=column width="15%" align="center">
						���ʼ�����
					</td>	
					<td  class=column width="15%" align="center">
						�ļ�����
					</td>
					<td class=column width="15%" align="center">
						��־ʹ��
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td width="15%" align="center">
						<s:if test='"1"==filterContentMap.http_filter_enabled'>����</s:if>
						<s:else >�ر�</s:else>
					</td>
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.file_filter_enable'>����</s:if>
						<s:else >�ر�</s:else>
					</td>
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.log_enable'>����</s:if>
						<s:else >�ر�</s:else>
					</td>	
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.smtp_filter_enabled'>����</s:if>
						<s:else >�ر�</s:else>
					</td>
					<td  width="15%"  align="center">
						<s:if test='"1"==filterContentMap.pop3_filter_enabled'>����</s:if>
						<s:else >�ر�</s:else>
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
								��������
					</td>
					<td class=column width="15%" align="right">
						���ּ��
					</td>
					<td  width="15%" align="center">
						<s:if test='"1"==netSecStr'>����</s:if>
						<s:else>�ر�</s:else>
					</td>
					<td class=column   width="15%" align="right">
						������
					</td>
					<td  width="15%" align="center">
						<s:if test='"1"==netSerVir'>����</s:if>
						<s:else>�ر�</s:else>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column  align="center" width="10%" >
								VPN��֤
					</td>
					<td class=column  width="20%" align="right">
						״̬
					</td>
					<td   width="20%" align="center">
						<s:if test='"1"==vpnInfo.enable'>����</s:if>
						<s:else>�ر�</s:else>
					</td>
					<td class=column   width="20%" align="right">
						ģʽ
					</td>
					<td   width="20%" align="center">
						<s:property value="vpnInfo.type"/>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td class=column   align="center" width="10%">
								����ǽ
					</td>
					<td class=column  width="20%" align="right">
						״̬
					</td>
					<td  width="20%" align="center">
						<s:if test='"1"==fireWallMap.enable'>����</s:if>
						<s:else>�ر�</s:else>
					</td>
					<td class=column  width="20%" align="right" >
						����
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
					   	���ض˿���Ϣ 
					</td>
				</tr>
				<tr align="left" bgcolor="#FFFFFF">
					<td rowspan='<s:property value="lanLen"/>'  class=column align="center">  <!--LAN����Ҫ���� ���棬�������� -->
					   	LAN��
					</td>
					<td class=column  width="10%" align="center">
						����		
					</td>
					<td class=column  width="10%" align="center">
						����
					</td>
					<td class=column  width="10%"align="center">
						�˿�״̬
					</td>
					<td class=column width="10%" align="center"	>
						MAC��ַ
					</td>
					<td class=column width="10%" align="center">
						��������
					</td>
					<td class=column width="10%" align="center">
						�����ֽ���
					</td>
					<td class=column width="10%" align="center">
						�����ֽ���
					</td>
					<td class=column width="10%" align="center">
						���Ͱ���
					</td>
					<td class=column width="10%" align="center">
						���հ���
					</td>
				</tr>
			<!-- ������̨����LAN�ڵĽ�� -->        
			<s:iterator value="lanLt" >
				<tr bgcolor="#FFFFFF">
					<td align="center" width="10%">
						LAN-<s:property value="lan_eth_id"/>
					</td>
					<td align="center" width="10%">
						<s:if test='"1"==eable'>����</s:if>
						<s:else>�ر�</s:else>
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
				<td rowspan='<s:property value="wanLen"/>'  class=column align="center">  <!--WAN����Ҫ���� ���棬�������� -->
					   	WAN��
					</td>
					<td class=column width="10%" align="center">
						����		
					</td>
					<td class=column width="10%" align="center">
						ҵ������
					</td>
					<td class=column width="10%" align="center">
						�˿�״̬
					</td>
					<td class=column width="10%" align="center">
						���뷽ʽ
					</td>
					<td class=column width="10%" align="center">
						��������
					</td>
					<td class=column width="10%" align="center">
						����ʺ�
					</td>
					<td class=column width="10%" align="center">
						�������
					</td>
					<td class=column width="10%" align="center">
						VLAN/PVC
					</td>
					<td class=column width="10%" align="center">
						IP��ַ
					</td>
			</tr>
			<!-- ������̨���ص�WAN�ڽ�� -->
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
				<td rowspan='<s:property value="wlanLen"/>'  class=column align="center">  <!--WAN����Ҫ���� ���棬�������� -->
					   	WLAN
					</td>
					<td class=column  width="10%" align="center">
						����		
					</td>	
					<td class=column  width="10%" align="center">
						����
					</td>
					<td class=column  width="10%" align="center">
						�Ƿ�����	
					</td>
					<td class=column  width="10%" align="center">
						���书��
					</td>
					<td class=column width="10%" align="center">
						��������
					</td>
					<td class=column r width="10%" align="center">
						�ŵ�
					</td>
					<td class=column  width="10%" align="center">
						�ܷ����ֽ���
					</td>
					<td class=column  width="10%" align="center">
						�ܷ��Ͱ���
					</td>
					<td class=column  width="10%" align="center">
						�ܽ��հ���
					</td>
			</tr>
			<!-- ������̨���ص�WLAN�Ľ�� -->
			<s:iterator value = "wlanLt"  status="sta">
				<tr bgcolor="#FFFFFF">
					<td align="center"  width="10%">
						WLAN-<s:property value="lan_wlan_id"/>
					</td>
					<td align="center"  width="10%">
						<s:if test='"1"==ap_enable'>����</s:if>
						<s:else>�ر�</s:else>
					</td>
					<td align="center"  width="10%">
						<s:if test='"1"==hide'>��</s:if>
						<s:else>��</s:else>
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