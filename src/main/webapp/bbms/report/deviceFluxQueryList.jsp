<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		�豸����ͳ��
	</caption>
	<thead>
		<tr>
			<th>
				�豸���к�
			</th>
			<th>
				�ͻ�����
			</th>
			<th>
				�û��ʺ�
			</th>
			<th>
				�ɼ�ʱ��
			</th>
			<th>
				�����ֽ���
			</th>
			<th>
				�����ֽ���
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="customer_name" />
					</td>
					<td>
						<s:property value="username" />
					</td>
					<td>
						<s:property value="collecttime" />
					</td>
					<td>
						<s:property value="ifinoctets" />
					</td>
					<td>
						<s:property value="ifoutoctets" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="queryType" />',
									 '<s:property value="deviceSN" />',
									 '<s:property value="loopbackIp" />',
									 '<s:property value="userName" />',
									 '<s:property value="stat_time" />',
									 '<s:property value="end_time" />')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>


