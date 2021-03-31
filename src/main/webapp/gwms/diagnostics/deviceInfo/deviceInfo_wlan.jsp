<%--
FileName	: deviceInfo.jsp
Date		: 2009年6月25日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column5 align="center">SSID名称</TD>
		<TD class=column5 align="center">输出功率</TD>
		<TD class=column5 align="center">信道类型</TD>
		<TD class=column5 align="center">连接状态</TD>
		<TD class=column5 align="center">连接设备数</TD>
		<TD class=column5 align="center">是否启用</TD>
		<TD class=column5 align="center">隐藏</TD>
		<TD class=column5 align="center">广播</TD>
		<TD class=column5 align="center">加密方式</TD>
	</tr>
	<s:iterator value="wlanList" var="wideNetInfo">
		<tr align="center" bgcolor="#FFFFFF">
			<s:if test="#wideNetInfo.ssid=='N/A' || #wideNetInfo.ssid=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="ssid"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.powervalue=='N/A' || #wideNetInfo.powervalue=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="powervalue"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.channel_in_use=='N/A' || #wideNetInfo.channel_in_use=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="channel_in_use"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="status"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.associatedNum=='N/A' || #wideNetInfo.associatedNum=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center">
				<a href="javascript:detailDevice('<s:property value="device_id"/>','<s:property value="lan_id"/>','<s:property value="lan_wlan_id"/>');">
				<s:property value="associated_num"/>
				</a>
				</TD>
			</s:else>
			<s:if test="#wideNetInfo.enable=='N/A' || #wideNetInfo.enable=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<s:if test="#wideNetInfo.enable==1">
					<TD align="center">是</TD>
				</s:if>
				<s:else>
					<TD align="center">否</TD>
				</s:else>
				<!-- <TD align="center"><s:property value="enable"/></TD> -->
			</s:else>
			<s:if test="#wideNetInfo.hide=='N/A' || #wideNetInfo.hide=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<s:if test="#wideNetInfo.hide==1">
					<TD align="center">是</TD>
				</s:if>
				<s:else>
					<TD align="center">否</TD>
				</s:else>
				<!-- <TD align="center"><s:property value="hide"/></TD> -->
			</s:else>
			<s:if test="#wideNetInfo.radio_enable=='N/A' || #wideNetInfo.radio_enable=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<s:if test="#wideNetInfo.radio_enable==1">
					<TD align="center">是</TD>
				</s:if>
				<s:else>
					<TD align="center">否</TD>
				</s:else>
				<!-- <TD align="center"><s:property value="radio_enable"/></TD> -->
			</s:else>
			<s:if test="#wideNetInfo.beacontype=='N/A' || #wideNetInfo.beacontype=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="beacontype"/></TD>
			</s:else>
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="9"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>
