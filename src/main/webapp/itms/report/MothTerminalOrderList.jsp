<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>�ն˹����ʱ���ͳ��</caption>
	<thead>
		<tr>
			<th>������</th>
			<th>������������</th>
			<th>����ҵ�񹤵���</th>
			<th>δ���ն˵Ŀ�����������</th>
			<th>�����������ն���</th>
			<th>����ҵ�񹤵����ն���</th>
			<th>�ն˹�����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="mothOrderList!=null">
			<s:if test="mothOrderList.size()>0">
				<s:iterator value="mothOrderList">
						<tr>
							<s:if test='isMonth == "false"'>
							<td><a href="javascript:countBycityId('<s:property value="city_id"/>');"><s:property value="city_name" /></a></td>
							<td><a href="javascript:openDevForWbdTerminal('<s:property value="city_id"/>','1');"><s:property value="khNum" /></a></td>
							<td><a href="javascript:openDevForWbdTerminal('<s:property value="city_id"/>','2');"><s:property value="yyNum" /></a></td>
							<td><a href="javascript:openDevForWbdTerminal('<s:property value="city_id"/>','3');"><s:property value="khWBdNum" /></a></td>
							<td><a href="javascript:openDevForWbdTerminal('<s:property value="city_id"/>','4');"><s:property value="khBdNum" /></a></td>
							<td><a href="javascript:openDevForWbdTerminal('<s:property value="city_id"/>','5');"><s:property value="yyBdNum" /></a></td>
							</s:if><s:else>
							<td><a href="javascript:countBycityId('<s:property value="city_id"/>');"><s:property value="city_name" /></a></td>
							<td><s:property value="khNum" /></td>
							<td><s:property value="yyNum" /></td>
							<td><a href="javascript:openDevForWbdTerminal('<s:property value="city_id"/>','3');"><s:property value="khWBdNum" /></a></td>
							<td><s:property value="khBdNum" /></td>
							<td><s:property value="yyBdNum" /></td>
							</s:else>
							<td><s:property value="percentage" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7>û�в�ѯ������Ҫ����Ϣ !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="7" align="left"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>

	</tfoot>
</table>
