<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>�����ÿ�ͨ�ɹ���ͳ��</caption>
	<thead>
		<tr>
			<th width="25%">����</th>
			<th width="25%">��װ������</th>
			<th width="25%">ʧ�ܹ�����</th>
			<th width="25%">�����ÿ�ͨ�ɹ���</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td><s:property value="����" /></td>
					<td><s:property value="��װ��������" /></td>
					<td><s:property value="ʧ�ܹ�������" /></td>
					<td><s:property value="�����ÿ�ͨ��" /></td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4"><IMG SRC="<s:url value="/images/excel.gif"/>"
				BORDER='0' ALT='�����б�' style='cursor: hand'
				onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4"><iframe id="childFrm" src=""></iframe></td>
	</tr>

</table>


