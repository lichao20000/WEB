<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		�û�ҵ��ʹ��
	</caption>
	<s:if test="data.size()>0">
		<s:iterator value="data" var="usermap">
			<thead>
				<tr>
					<th colspan="4">
						�û��豸��Ϣ
					</th>
				</tr>
			</thead>
			<tbody>

				<tr>
					<td width="25%" class="green_k">
						�û��˺�
					</td>
					<td width="25%" bgcolor="#FFFFFF">
						<s:property value="username" />
					</td>
					<td width="25%" class="green_k">
						����
					</td>
					<td width="25%" bgcolor="#FFFFFF">
						<s:property value="city_name" />
					</td>
				</tr>
				<tr>
					<td class="green_k">
						�豸���к�
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="device" />
					</td>
					<td class="green_k">
						����
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="vendor_add" />
					</td>
				</tr>
				<tr>
					<td class="green_k">
						�ͺ�
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="device_model" />
					</td>
					<td class="green_k">
						����汾
					</td>
					<td bgcolor="#FFFFFF">
						<s:property value="softwareversion" />
					</td>
				</tr>
				<tr>
					<td colspan="4" align="center" bgcolor="#D0D0FF">
						<strong>ҵ����Ϣ</strong>
					</td>
				</tr>
				<tr>
					<td bgcolor="#E1EEEE" align="center">
						<strong>ҵ��ID</strong>
					</td>
					<td bgcolor="#E1EEEE" align="center">
						<strong>ҵ������</strong>
					</td>
					<td bgcolor="#E1EEEE" align="center">
						<strong>�Ƿ���</strong>
					</td>
					<td bgcolor="#E1EEEE" align="center">
						<strong>�ɼ�ʱ��</strong>
					</td>
				</tr>
				<s:iterator value="#usermap.servicelist" var="servicemap">
					<tr>
						<td width="25%" align="center">
							<s:property value="service_id" />
						</td>
						<td width="25%" align="center">
							<s:property value="service_name" />
						</td>
						<td width="25%" align="center">
							<s:property value="service_result" />
						</td>
						<td width="25%" align="center">
							<s:property value="gather_time" />
						</td>
					</tr>
				</s:iterator>
			</tbody>
			<tr>
				<td height="20" colspan="4">
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
	<tbody>
	<tr>
		<td colspan="4">
				�û����豸û�����ҵ����Ϣ
			</td>
		</tr>
	</tbody>
	</s:else>
	<tfoot>
		<tr>
			<td colspan="4">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel('<s:property value="serviceId"/>','<s:property value="device_serialnumber"/>','<s:property value="userName"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


