<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>预置受限制设备信息</caption>
	<thead>
		<tr>
			<th width="50%">本地网</th>
			<th width="50%">受限设备总数</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null">
			<s:if test="data.size()>0">
				<s:iterator value="data">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><a
							href="javascript:openDevForWbdTerminal('<s:property value="city_id" />');"><s:property
									value="ninum" /></a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=2>没有查询到所需要的信息 !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=2>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="2" align="left"><IMG SRC="../../images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand'
				onclick="ToExcel('<s:property value="city_id"/>')"></td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="2"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
