<%--
Author: ��ɭ��
Version: 1.0.0
Date: 2009-11-11
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<tr>
		<td class="green_title" width="5%">
			ѡ��
		</td>
		<td class="green_title" width="20%">
			�û��˺�
		</td>
		<td class="green_title" width="20%">
			����
		</td>
		<td class="green_title" width="20%">
			���豸oui
		</td>
		<td class="green_title" width="20%">
			���豸���к�
		</td>
		<td class="green_title" width="15%">
			��ϸ��Ϣ
		</td>
	</tr>


	<s:if test="userList!=null">
		<s:iterator value="userList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type="radio" name="radioUserId" value="1"
						onclick="userOnclick('<s:property value="user_id"/>',
									 		 '<s:property value="device_id"/>',
							 				 '<s:property value="city_id"/>',
							 				 '<s:property value="device_serialnumber"/>',
							 				 '<s:property value="username"/>')" />
				</td>
				<td class=column align="center">
					<s:property value="username" />
				</td>
				<td class=column align="center">
					<s:property value="city_name" />
				</td>
				<td class=column align="center">
					<s:property value="oui" />
				</td>
				<td class=column align="center">
					<s:property value="device_serialnumber" />
				</td>
				<td align="center" class=column>
					<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0"
							ALT="��ϸ��Ϣ" onclick="GoContent('<s:property value="user_id"/>')"
							style="cursor: hand">
				</td>
			<tr bgcolor="#FFFFFF">
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="7">
				<font color="red">δ�鵽�����������û������ʵ������Ƿ���ȷ��</font>
			</td>
		</tr>
	</s:else>

</table>
