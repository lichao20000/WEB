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
			<th width="50%">
				属地
			</th>
			<th width="50%">
				用户数
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<a
							href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="wanAccessTypeId"/>','<s:property value="forbidNet"/>','<s:property value="typeId"/>');">
							<s:property value="city_name" /> </a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="wanAccessTypeId"/>','<s:property value="forbidNet"/>','<s:property value="typeId"/>');">
							<s:property value="total" /> </a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="2">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="wanAccessTypeId"/>','<s:property value="forbidNet"/>','<s:property value="typeId"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="2">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


