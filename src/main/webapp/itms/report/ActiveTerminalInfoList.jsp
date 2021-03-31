<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>终端活跃数</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>终端总数</th>
			<th>活跃终端数</th>
			<th>活跃率</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="activeTermainlList!=null">
			<s:if test="activeTermainlList.size()>0">
				<s:iterator value="activeTermainlList">
						<tr>
							<td>
							<a href="javascript:countBycityId('<s:property value="city_id"/>');">
								<s:property value="city_name" />
							</a>
							</td>
							<td>
							<a href="javascript:openDevForAll('<s:property value="city_id"/>');">
								<s:property value="terminal_total" />
							</a>
							</td>
							<td>
							<a href="javascript:openDevForTime('<s:property value="city_id"/>');">
								<s:property value="terminal_activeTotal" />
							</a>
							</td>
							<td><s:property value="percentage" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>系统该地区信息 !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="4" align="right"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>