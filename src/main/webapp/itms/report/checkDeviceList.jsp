<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		查询结果
	</caption>
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>
					属地
				</th>
				<th>
					未稽核
				</th>
				<th>
					稽核成功数
				</th>
				<th>
					稽核失败数
				</th>
				<th>
					稽核成功率（成功/稽核数）
				</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">
							<s:property value="cityname" />

					</td>
					<td align="center">
						<a href="javascript:openHgw('<s:property value="city"/>','0')">
						   				<s:property value="undo" /> </a>
					</td>
					<td align="center">
						<a href="javascript:openHgw('<s:property value="city"/>','1')">
										<s:property value="succ" /> </a>
					</td>
					<td align="center">
						<a href="javascript:openHgw('<s:property value="city"/>','2')">
										<s:property value="fail" /> </a>
					</td>
					<td align="center">
										<s:property value="rate" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='5'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ToExcel('<s:property value="cityId"/>','<s:property value="checkState"/>')">
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">
					没有相关信息
				</td>
			</tr>
		</tfoot>
	</s:else>
<tr STYLE="display: none">
		<td colspan="9">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>


