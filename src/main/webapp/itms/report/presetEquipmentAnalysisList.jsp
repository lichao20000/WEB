<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>预置设备导入分析</caption>
	<thead>
		<tr>
			<th>厂商</th>
			<th>设备型号</th>
			<th>已导入未使用</th>
			<th>已上线未绑定</th>
			<th>已使用已绑定</th>
			<th>曾经使用</th>
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
					<td colspan=6>没有查询到所需要的信息 !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
			<td colspan="6" align="left"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="6"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
