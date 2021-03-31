<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table class="listtable" id="listTable">
	<caption>天翼看店业务统计信息</caption>
	<thead>
		<tr>
			<th width="50%">属地</th>
			<th width="50%">天翼看店用户数量</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null">
			<s:if test="data.size()>0">
				<s:iterator value="data">
					<tr>
						<td><a
							href="javascript:countBycityId('<s:property value="city_id" />','<s:property value="userType"/>');"><s:property
									value="city_name" /></a></td>
						<td><a
							href="javascript:openDevForWbdTerminal('<s:property value="city_id" />','<s:property value="userType"/>');"><s:property
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
				onclick="ToExcel('<s:property value="city_id"/>','<s:property value="userType"/>')"></td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="2"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
