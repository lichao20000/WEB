<%--
FileName	: deviceInfo.jsp
Date		: 2009��6��25��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
	<tr align="center" bgcolor="#FFFFFF">
		<TD class=column5 align="center">PVC����</TD>
		<TD class=column5 align="center">��������</TD>
		<TD class=column5 align="center">�󶨶˿�</TD>
		<TD class=column5 align="center">����״̬</TD>
		<TD class=column5 align="center">DNS</TD>
		<TD class=column5 align="center">PPPoE�˺�</TD>
		<TD class=column5 align="center">����</TD>
		<TD class=column5 align="center">IP��ַ</TD>
		<TD class=column5 align="center">����ʧ�ܴ�����</TD>
	</tr>
	<s:iterator value="wideNetInfoList" var="wideNetInfo">
		<tr align="center" bgcolor="#FFFFFF">
			<s:if test="#wideNetInfo.pvc=='N/A' || #wideNetInfo.pvc=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="pvc"/></TD>
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
			<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="status"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.dns=='N/A' || #wideNetInfo.dns=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="dns"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.username=='N/A' || #wideNetInfo.username=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="username"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.password=='N/A' || #wideNetInfo.password=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="password"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.ip=='N/A' || #wideNetInfo.ip=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="ip"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.connError=='N/A' || #wideNetInfo.connError=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="connError"/></TD>
			</s:else>
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="9"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>
