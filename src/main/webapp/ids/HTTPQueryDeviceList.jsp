<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
<thead>
			<tr>
				<th>ѡ��</th>
				<th>�豸���к�</th>
				<th>OUI</th>
				<th>����</th>
			</tr>
		</thead>
<tbody>

	<s:if test="date!=null&&date.size()>0">
		<s:iterator value="date">
			<tr align="center">
				<td style="text-align: center;">
					<input type="radio" name="radioUserId" id="radioUserId"  value="1"
						onclick="testCheck('<s:property value="device_serialnumber"/>','<s:property value="oui"/>','<s:property value="city_id"/>',
									 		 '<s:property value="city_name"/>')" />
				</td>
				<td style="text-align: center;">
					<s:property value="device_serialnumber" />
				</td>
				<td style="text-align: center;">
					<s:property value="oui" />
				</td>
				<td style="text-align: center;">
					<s:property value="city_name" />
				</td>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td colspan="4">
				<font color="red">δ�鵽�����������û������ʵ������Ƿ���ȷ��</font>
			</td>
		</tr>
	</s:else>
		</tbody>
</table>
