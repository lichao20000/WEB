<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>长时间不在线终端信息</caption>
	<thead>
		<tr>
			<th rowspan="2">属地</th>
			
			<th colspan="2">大于3个月不在线</th>
			
			<th colspan="2">大于4个月不在线</th>
			
			<th colspan="2">大于5个月不在线</th>
			
			<th colspan="2">大于6个月不在线</th>
		</tr>
		<tr>
			<th>总终端数</th>
			<th>已绑定工单数</th>
			<th>总终端数</th>
			<th>已绑定工单数</th>
			<th>总终端数</th>
			<th>已绑定工单数</th>
			<th>总终端数</th>
			<th>已绑定工单数</th>
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
					<td colspan=9>系统没有该用户的业务信息!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	 
	<tfoot>
		<tr>
			<td colspan="9" align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()" />
			</td>
		</tr>
	</tfoot>
</table>
