<%--
�Ÿ�·��������ʽ���ʺ�չʾҳ��
Author: �Ŵ�
Version: 1.0.0
Date: 2011-09-22
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript">
showAccessType('<s:property value="accessType"/>','<s:property value="accessTypeName"/>');
</SCRIPT>
<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor=#999999>
	
	<s:if test="internetList!=null">
	<tr>
		<td class="green_title" width="5%">
			ѡ��
		</td>
		<td class="green_title" width="10%">
			ҵ���˺�
		</td>
		<td class="green_title" width="10%">
			����
		</td>
		<td class="green_title" width="10%">
			PVC/VLAN
		</td>
	</tr>
		<s:iterator value="internetList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type="radio" name="radioUserName" value="1" disabled="<s:property value="disabled" />"
						onclick="usernameOnclick(
											 '<s:property value="username"/>',
											 '<s:property value="passwd"/>',
											 '<s:property value="pvc"/>',
											 '<s:property value="user_id" />',
											 '<s:property value="wan_type" />',
											 '<s:property value="accessTypeName" />')" />
				</td>
				<td class=column align="center">
					<s:property value="username" />
				</td>
				<td class=column align="center">
					<s:property value="passwd" />
				</td>
				<td class=column align="center">
					<s:property value="pvc" />
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
	<br>
	<tr bgcolor="#FFFFFF">
				<td class=column align="center" colspan="4">
				<strong>
				<s:if test="errorCode=='noBind'">
					���豸δ���û�����󶨵��û������ڿ������ҵ�������²�ѯ��
				</s:if>
				<s:elseif test="errorCode=='noInterner'">
					���豸�󶨵��û������ڿ������ҵ��
				</s:elseif>
				<s:elseif test="errorCode=='noBridge'">
					���豸�󶨵��û��������Ž�����ҵ��
				</s:elseif>
				</strong>
				</td>
			</tr>
	</s:else>
	</table>