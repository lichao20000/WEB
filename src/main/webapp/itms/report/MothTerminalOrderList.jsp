<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>终端管理率报表统计</caption>
	<thead>
		<tr>
			<th>本地网</th>
			<th>开户工单总数</th>
			<th>语音业务工单数</th>
			<th>未绑定终端的开户工单总数</th>
			<th>开户工单绑定终端数</th>
			<th>语音业务工单绑定终端数</th>
			<th>终端管理率</th>
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
					<td colspan=7>没有查询到所需要的信息 !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="7" align="left"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>

	</tfoot>
</table>
