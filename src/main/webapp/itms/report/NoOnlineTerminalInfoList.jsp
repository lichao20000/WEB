<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>��ʱ�䲻�����ն���Ϣ</caption>
	<thead>
		<tr>
			<th rowspan="2">����</th>
			
			<th colspan="2">����3���²�����</th>
			
			<th colspan="2">����4���²�����</th>
			
			<th colspan="2">����5���²�����</th>
			
			<th colspan="2">����6���²�����</th>
		</tr>
		<tr>
			<th>���ն���</th>
			<th>�Ѱ󶨹�����</th>
			<th>���ն���</th>
			<th>�Ѱ󶨹�����</th>
			<th>���ն���</th>
			<th>�Ѱ󶨹�����</th>
			<th>���ն���</th>
			<th>�Ѱ󶨹�����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="noOnlineList!=null">
			<s:if test="noOnlineList.size()>0">
				<s:iterator value="noOnlineList">
						<tr>
							<td>
							<a href="javascript:countBycityId('<s:property value="city_id"/>');">
								<s:property value="city_name" />
							</a>
							</td>
							<td><s:property value="total3" /></td>
							<td><s:property value="total3Bd" /></td>
							<td><s:property value="total4" /></td>
							<td><s:property value="total4Bd" /></td>
							<td><s:property value="total5" /></td>
							<td><s:property value="total5Bd" /></td>
							<td><s:property value="total6" /></td>
							<td><s:property value="total6Bd" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>ϵͳû�и��û���ҵ����Ϣ!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>ϵͳû�д��û�!</td>
			</tr>
		</s:else>
	</tbody>
	 
	<tfoot>
		<tr>
			<td colspan="9" align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()" />
			</td>
		</tr>
	</tfoot>
</table>
