<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<table class="querytable">
	<TR>
		<th colspan="4">BSS������ϸ��Ϣ</th>
	</tr>
	<s:if test="bssSheetList!=null">
		<s:if test="bssSheetList.size()==1">
			<s:iterator value="bssSheetList">
				<TR>
					<TD class=column width="15%" align='right'>ҵ������</TD>
					<TD width="35%"><s:property value="serv_type" /></TD>
					<TD class=column width="15%" align='right'>��������</TD>
					<TD width="35%"><s:property value="serv_status" /></TD>
				</TR>
				<TR>
					<%if (LipossGlobals.inArea("sd_lt") || LipossGlobals.inArea("sx_lt")) { %>
						<TD class=column width="15%" align='right'>RMS����ʱ��</TD>
					<%}else{ %>
						<TD class=column width="15%" align='right'>ITMS����ʱ��</TD>
					<%} %>
					<TD width="35%"><s:property value="opendate" /></TD>
					<TD class=column width="15%" align='right'>�豸����</TD>   
					<TD width="35%"><s:property value="type_name" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>
						<%if(LipossGlobals.inArea("sx_lt")) { %>
							Ψһ��ʶ
						<%}else{ %>
							LOID
						<%} %>
					</TD>
					<TD width="35%"><s:property value="username" /></TD>
					<TD class=column width="15%" align='right'>����������˺�</TD>
					<TD width="35%"><s:property value="account" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>����</TD>
					<TD width="35%"><s:property value="city_name" /></TD>
					<TD class=column width="15%" align='right'>������ʽ</TD>
					<TD width="35%"><s:property value="wan_type" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>VLANID</TD>
					<TD width="85%" colspan="3"><s:property value="vlanid" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>���д���</TD>
					<TD width="35%"><s:property value="ipoe_upbandwidth" />M</TD>
					<TD class=column width="15%" align='right'>���д���</TD>
					<TD width="35%"><s:property value="ipoe_downbandwidth" />M</TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>ҵ������</TD>
					<TD width="35%"><s:property value="app_type" /></TD>
					<TD class=column width="15%" align='right'>·��״̬</TD>
					<TD width="35%"><s:property value="open_status" /></TD>
				</TR>
				<TR>
					<TD class=column width="15%" align='right'>·�ɵ�ַ</TD>
					<TD width="85%" colspan="3"><s:property value="router_List" /></TD>
				</TR>
				<tr>
					<TD class=column width="15%" align='right'>�ص���Ϣ</TD>
					<TD width="85%" colspan="3">0|||00|||�ɹ�</TD>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>������Ϣ�����ڻ����!</td>
			</tr>
		</s:else>
	</s:if>
	<s:else>
		<tr>
			<td colspan=4>�û���Ϣ����!</td>
		</tr>
	</s:else>
	<TR>
		<td colspan="4" align="right" class=foot>
			<a href="javascript:bssSheetClose()">�ر�</a>
		</td>
	</TR>
</table>
