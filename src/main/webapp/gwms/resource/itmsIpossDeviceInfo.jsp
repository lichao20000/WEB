<%--
�豸��ѯ���棨IPOSS�ֳ���װ��
Author: ��ɭ��
Version: 1.0.0
Date: 2009-11-12
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center" bgcolor="#999999">
	<tr>
		<td class="green_title" width="5%">
			ѡ��
		</td>
		<td class="green_title" width="20%">
			�豸����
		</td>
		<td class="green_title" width="15%">
			�豸oui
		</td>
		<td class="green_title" width="20%">
			�ͺ�
		</td>
		<td class="green_title" width="15%">
			����汾
		</td>
		<td class="green_title" width="25%">
			�豸���к�
		</td>
	</tr>

	<tr bgcolor="#FFFFFF">
		<s:if test="deviceList!=null">
		<s:iterator value="deviceList" var="deviceList">
			<td align="center">
				<input type=radio name="radioDeviceId" value="1"
					onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="oui"/>',
											'<s:property value="device_serialnumber"/>')"/>
			</td>
			<td align="center">
				<s:property value="vendor_add" />
			</td>
			<td align="center">
				<s:property value="oui" />
			</td>
			<td align="center">
				<s:property value="device_model" />
			</td>
			<td align="center">
				<s:property value="softwareversion" />
			</td>
			<td align="center">
				<s:property value="device_serialnumber" />
			</td>
		</s:iterator>
		</s:if>
		<s:else>
			<td colspan="7">
				<font color="red">δ�鵽�����������豸�����豸�п��ܲ����ڻ����Ѱ󶨣�</font>
			</td>
		</s:else>
	</tr>
</table>
