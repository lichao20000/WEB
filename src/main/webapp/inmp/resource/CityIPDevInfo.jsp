<%--
�豸��ѯ���棨������ITMS�ֳ���װ��
Author: ��ɭ��
Version: 1.0.1
Date: 2010-4-30
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border=0 cellspacing=1 cellpadding=1 width="100%" align="center"
	bgcolor="#999999">
	<s:if test="deviceList!=null">
		<tr>
			<td class="green_title" width="5%">
				ѡ��
			</td>
			<td class="green_title" width="10%">
				�豸����
			</td>
			<td class="green_title" width="10%">
				�ͺ�
			</td>
			<td class="green_title" width="15%">
				����汾
			</td>
			<td class="green_title" width="20%">
				�豸���к�
			</td>
			<td class="green_title" width="10%">
				������IP
			</td>
			<td class="green_title" width="8%">
				�ն�����
			</td>
			<td class="green_title" width="8%">
				����
			</td>
			<td class="green_title" width="10%">
				��״̬
			</td>
			<td class="green_title" width="10%">
				����
			</td>
		</tr>



		<s:iterator value="deviceList" var="deviceList">
			<tr bgcolor="#FFFFFF">
				<td class=column align="center">
					<input type=radio name="radioDeviceId" value="1"
						onclick="deviceOnclick('<s:property value="device_id"/>',
											'<s:property value="city_id"/>',
											'<s:property value="oui"/>',
											'<s:property value="device_serialnumber"/>',
											'<s:property value="IpCity_id"/>',
											'<s:property value="cpe_allocatedstatus"/>',
											'<s:property value="flag"/>',
											'<s:property value="manage"/>',
											'<s:property value="device_type"/>')" />
				</td>
				<td class=column align="center">
					<s:property value="vendor_add" />
				</td>
				<td class=column align="center">
					<s:property value="device_model" />
				</td>
				<td class=column align="center">
					<s:property value="softwareversion" />
				</td>
				<td class=column align="center">
					<s:property value="oui" />
					-
					<s:property value="device_serialnumber" />
				</td>
				<td class=column align="center">
					<s:property value="loopback_ip" />
				</td>
				<td class=column align="center">
					<s:property value="device_type" />
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
				<TD class=column align="center">
					<IMG SRC="<s:url value="../../images/inmp/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
						onclick="DetailDevice('<s:property value="device_id"/>')"
						style="cursor: hand">
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
