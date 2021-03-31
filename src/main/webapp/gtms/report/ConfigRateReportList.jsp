<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		统计结果
	</caption>
	<thead>
		<tr>
			<th>
				属地
			</th>
			<th>
				全量
			</th>
			<th>
				成功量
			</th>
			<th>
				成功率
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td bgcolor="#F4F4F0" align="center">
						<s:if test='isAll=="1"'>
							<s:property value="city_name" />
						</s:if>
						<s:else>
							<a
								href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
								<s:property value="city_name" /> 
							</a>
						</s:else>
					</td>

					<td align="center">
						<s:property value="total" />

					</td>
					<td align="center">
						<s:property value="succ" />
					</td>
					<td align="center">
						<s:property value="rate" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="5">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="endtime1"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


