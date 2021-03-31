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
		<TD class=column5 align="center">URL</TD>
		<TD class=column5 align="center">访问平台验证用户名</TD>
		<TD class=column5 align="center">访问平台验证密码</TD>
		<TD class=column5 align="center">DHCP地址</TD>
		<!--  <TD class=column5 align="center">访问终端验证用户名</TD>
		<TD class=column5 align="center">访问终端验证密码</TD>-->
	</tr>
	<tr align="center" bgcolor="#FFFFFF">
		<s:if test="#gwTr069OBJ.pvc=='N/A' || #gwTr069OBJ.pvc=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.pvc"/></TD>
		</s:else>
		<s:if test="#gwTr069OBJ.url=='N/A' || #gwTr069OBJ.url=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.url"/></TD>
		</s:else>
		<s:if test="#gwTr069OBJ.cpeUsername=='N/A' || #gwTr069OBJ.cpeUsername=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.cpeUsername"/></TD>
		</s:else>
		<s:if test="#gwTr069OBJ.cpePasswd=='N/A' || #gwTr069OBJ.cpePasswd=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.cpePasswd"/></TD>
		</s:else>
		<s:if test="#gwTr069OBJ.loopbackIp=='N/A' || #gwTr069OBJ.loopbackIp=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.loopbackIp"/></TD>
		</s:else>
		
		<!--
		<s:if test="#gwTr069OBJ.acsUsername=='N/A' || #gwTr069OBJ.acsUsername=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.acsUsername"/></TD>
		</s:else>
		<s:if test="#gwTr069OBJ.acsPasswd=='N/A' || #gwTr069OBJ.acsPasswd=='null' ">
			<TD align="center">-</TD>
		</s:if>
		<s:else>
			<TD align="center"><s:property value="gwTr069OBJ.acsPasswd"/></TD>
		</s:else>
		-->
	</tr>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="5"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>
