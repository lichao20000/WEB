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
		<TD class=column5 align="center">����״̬</TD>
		<TD class=column5 align="center">VoIP������</TD>
		<TD class=column5 align="center">���÷�����</TD>
		<TD class=column5 align="center">ע��״̬</TD>
		<TD class=column5 align="center">����</TD>
		<TD class=column5 align="center">����</TD>
	</tr>
	<s:iterator value="voipInfoList" var="wideNetInfo">
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
			<s:if test="#wideNetInfo.status=='N/A' || #wideNetInfo.status=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="status"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.prox_serv_2=='N/A' || #wideNetInfo.prox_serv_2=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="prox_serv"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.prox_serv_2=='N/A' || #wideNetInfo.prox_serv_2=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="prox_serv_2"/></TD>
			</s:else>
			<s:if test="#wideNetInfo.regist_result=='N/A' || #wideNetInfo.regist_result=='null' ">
				<TD align="center">-</TD>
			</s:if>
			<s:else>
				<TD align="center"><s:property value="regist_result"/></TD>
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
		</tr>
	</s:iterator>
	<tr align="center" bgcolor="#FFFFFF">
		<TD align="center" colspan="8"><s:property value="corbaMsg"/></TD>
	</tr>
</TABLE>

