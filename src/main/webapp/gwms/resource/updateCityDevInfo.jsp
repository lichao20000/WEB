<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<s:if test="deviceList!=null">
		<tr>
			<td class="green_title" >
				ѡ��
			</td>
			<td class="green_title" >
				�豸���к�
			</td>
			<td class="green_title" >
				������IP
			</td>
			<td class="green_title" >
				����
			</td>
			<td class="green_title" >
				��״̬
			</td>

		</tr>

		<s:iterator value="deviceList" var="deviceList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type=radio name="radioDeviceId" value="1"
						onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="device_serialnumber"/>',
											'<s:property value="cpe_allocatedstatus"/>')" />
				</td>
				<td class=column align="center">
					<s:property value="device_serialnumber" />
				</td>
				<td class=column align="center">
					<s:property value="loopback_ip" />
				</td>

				<td class=column align="center">
					<s:property value="city_name" />
				</td>
				<TD class=column align="center">
					<s:if test='cpe_allocatedstatus=="1"'>
					�Ѱ�
					</s:if>
					<s:else>
					δ��
					</s:else>
				</TD>

			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="7" align="center">
				<font color="red">������ѯ���ն�δע�ᵽϵͳ���밴��"�豸δע��Ĵ�����"�������ϣ�</font>
				<%@include file="DevNotRegisterAdvice.jsp"%>
			</td>
		</tr>
	</s:else>

</table>
