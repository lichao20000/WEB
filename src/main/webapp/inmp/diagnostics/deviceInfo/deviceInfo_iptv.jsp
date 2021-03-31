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
		<TD class=column5 align="center">PVC配置</TD>
		<TD class=column5 align="center">连接状态</TD>
		<TD class=column5 align="center">连接类型</TD>
		<TD class=column5 align="center">绑定端口</TD>
	</tr>
	<s:iterator value="iptvInfoList" var="wideNetInfo">
		<tr align="center" bgcolor="#FFFFFF">

			<s:if test="#wideNetInfo.pvc=='N/A' || #wideNetInfo.pvc=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="pvc"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="status"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.connType=='N/A' || #wideNetInfo.connType=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="connType"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.bindPort=='N/A' || #wideNetInfo.bindPort=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="bindPort"/></TD>
			</s:else>
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="4"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>

