<%--
FileName	: deviceInfo.jsp
Date		: 2009年6月25日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<tr align="left" id="trvoip" >
		<td colspan="4"  bgcolor=#999999>
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column5 align="center">LAN_ID</TD>
		<TD class=column5 align="center">WLAN_ID</TD>
		<TD class=column5 align="center">索引</TD>
		<TD class=column5 align="center">IP地址</TD>
		<TD class=column5 align="center">MAC地址</TD>
		<TD class=column5 align="center">认证状态</TD>
	</tr>
	<s:iterator value="wlanAssoList" var="wlanAssoList">
		<tr align="center" bgcolor="#FFFFFF">
			<s:if test="#wlanAssoList.lan_id=='N/A' || #wlanAssoList.lan_id=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="lan_id"/></TD>
			</s:else>

			<s:if test="#wlanAssoList.lan_wlan_id=='N/A' || #wlanAssoList.lan_wlan_id=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="lan_wlan_id"/></TD>
			</s:else>

			<s:if test="#wlanAssoList.asso_id=='N/A' || #wlanAssoList.asso_id=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="asso_id"/></TD>
			</s:else>

			<s:if test="#wlanAssoList.ip_address=='N/A' || #wlanAssoList.ip_address=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="ip_address"/></TD>
			</s:else>

			<s:if test="#wlanAssoList.mac_address=='N/A' || #wlanAssoList.mac_address=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="mac_address"/></TD>
			</s:else>

			<s:if test="#wlanAssoList.auth_state=='N/A' || #wlanAssoList.auth_state=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="auth_state"/></TD>
			</s:else>
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="6"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>
		</td>
	</tr>
</TABLE>

