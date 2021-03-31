<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>零配置开通成功率统计</caption>
	<thead>
		<tr>
			<th width="25%">地市</th>
			<th width="25%">新装工单数</th>
			<th width="25%">失败工单数</th>
			<th width="25%">零配置开通成功率</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td><s:property value="地市" /></td>
					<td><s:property value="新装工单总数" /></td>
					<td><s:property value="失败工单总数" /></td>
					<td><s:property value="零配置开通率" /></td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4"><IMG SRC="<s:url value="/images/excel.gif"/>"
				BORDER='0' ALT='导出列表' style='cursor: hand'
				onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4"><iframe id="childFrm" src=""></iframe></td>
	</tr>

</table>


