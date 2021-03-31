<%--
FileName	: deviceInfo.jsp
Date		: 2009��6��25��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">

<table  border=0 cellspacing=1 cellpadding=2 width="100%" >
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="12%">DHCP��ַ��</TD>
		<TD class=column align="right" nowrap width="12%">��ʼ��ַ:</TD>
		<TD width="32%">
			<s:if test="#gwLanHostconfMap.min_addr=='N/A' || #gwLanHostconfMap.min_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.min_addr"/>
			</s:else>
		</TD>
		<TD class=column align="right" width="12%">������ַ:</TD>
		<TD width="32%">
			<s:if test="#gwLanHostconfMap.max_addr=='N/A' || #gwLanHostconfMap.max_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.max_addr"/>
			</s:else>
		</TD>
	</tr>
	<!--
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="15%">STB��С��ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.stb_min_addr=='N/A' || #gwLanHostconfMap.stb_min_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.stb_min_addr"/>
			</s:else>
		</TD>
		<TD class=column align="right" width="15%">STB����ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.stb_max_addr=='N/A' || #gwLanHostconfMap.stb_max_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.stb_max_addr"/>
			</s:else>
		</TD>
	</tr>
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="15%">�绰��С��ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.phone_min_addr=='N/A' || #gwLanHostconfMap.phone_min_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.phone_min_addr"/>
			</s:else>
		</TD>
		<TD class=column align="right" width="15%">�绰����ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.came_max_addr=='N/A' || #gwLanHostconfMap.came_max_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.came_max_addr"/>
			</s:else>
		</TD>
	</tr>
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="15%">����ͷ��С��ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.came_min_addr=='N/A' || #gwLanHostconfMap.came_min_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.came_min_addr"/>
			</s:else>
		</TD>
		<TD class=column align="right" width="15%">����ͷ����ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.came_max_addr=='N/A' || #gwLanHostconfMap.came_max_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.came_max_addr"/>
			</s:else>
		</TD>
	</tr>
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column align="right" nowrap width="15%">PC��С��ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.pc_min_addr=='N/A' || #gwLanHostconfMap.pc_min_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.pc_min_addr"/>
			</s:else>
		</TD>
		<TD class=column align="right" width="15%">PC����ַ:</TD>
		<TD width="35%">
			<s:if test="#gwLanHostconfMap.pc_max_addr=='N/A' || #gwLanHostconfMap.pc_max_addr=='null' ">
				-
			</s:if>
			<s:else>
				<s:property value="gwLanHostconfMap.pc_max_addr"/>
			</s:else>
		</TD>
	</tr>
	 -->
</table>
<table  border=0 cellspacing=1 cellpadding=2 width="100%" >
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column5 align="center">����</TD>
		<TD class=column5 align="center">MAC��ַ</TD>
		<TD class=column5 align="center">��������</TD>
		<TD class=column5 align="center">״̬</TD>
		<TD class=column5 align="center">�����ֽ���</TD>
		<TD class=column5 align="center">�����ֽ���</TD>
		<TD class=column5 align="center">�հ���</TD>
		<TD class=column5 align="center">������</TD>
	</tr>
	<s:iterator value="lanEthList" var="lanEthList">
		<tr align="center" bgcolor="#FFFFFF">
			<s:if test="#lanEthList.lan_eth_id=='N/A' || #lanEthList.lan_eth_id=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center">LAN<s:property value="lan_eth_id"/></TD>
			</s:else>
			<s:if test="#lanEthList.mac_address=='N/A' || #lanEthList.mac_address=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="mac_address"/></TD>
			</s:else>
			<s:if test="#lanEthList.max_bit_rate=='N/A' || #lanEthList.max_bit_rate=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="max_bit_rate"/></TD>
			</s:else>
			<s:if test="#lanEthList.status=='N/A' || #lanEthList.status=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="status"/></TD>
			</s:else>
			<s:if test="#lanEthList.byte_rece=='N/A' || #lanEthList.byte_rece=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="byte_rece"/></TD>
			</s:else>
			<s:if test="#lanEthList.byte_sent=='N/A' || #lanEthList.byte_sent=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="byte_sent"/></TD>
			</s:else>
			<s:if test="#lanEthList.pack_rece=='N/A' || #lanEthList.pack_rece=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="pack_rece"/></TD>
			</s:else>
			<s:if test="#lanEthList.pack_sent=='N/A' || #lanEthList.pack_sent=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="pack_sent"/></TD>
			</s:else>
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="8"><s:property value="corbaMsg"/></TD>
	</tr>
</table>
