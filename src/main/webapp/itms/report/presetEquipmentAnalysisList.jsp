<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>Ԥ���豸�������</caption>
	<thead>
		<tr>
			<th>����</th>
			<th>�豸�ͺ�</th>
			<th>�ѵ���δʹ��</th>
			<th>������δ��</th>
			<th>��ʹ���Ѱ�</th>
			<th>����ʹ��</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="presetList!=null">
			<s:if test="presetList.size()>0">
				<s:iterator value="presetList">
					<tr>
						<td><s:property value="vendor_name" /></td>
						<td><s:property value="model_name" /></td>
						<td><a
							href="javascript:openDevForWbdTerminal('1','<s:property value="vendor_name" />','<s:property value="model_name" />');"><s:property
									value="status1" /></a></td>
						<td><a
							href="javascript:openDevForWbdTerminal('2','<s:property value="vendor_name" />','<s:property value="model_name" />');"><s:property
									value="status2" /></a></td>
						<td><a
							href="javascript:openDevForWbdTerminal('3','<s:property value="vendor_name" />','<s:property value="model_name" />');"><s:property
									value="status3" /></a></td>
						<td><a
							href="javascript:openDevForWbdTerminal('4','<s:property value="vendor_name" />','<s:property value="model_name" />');"><s:property
									value="status4" /></a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>û�в�ѯ������Ҫ����Ϣ !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="6" align="left"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
