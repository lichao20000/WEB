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
			<th rowspan="2">
				属地
			</th>
			<th rowspan="2">
				总配置数
			</th>
			<th rowspan="2">
				成功
			</th>
			<th rowspan="2">
				未触发
			</th>
			<th colspan="2">
				失败
			</th>
		</tr>	
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','');">
							<s:property value="city_name" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','');">
							<s:property value="allup" /> </a>

					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','1');">
							<s:property value="successnum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','2');">
							<s:property value="noupnum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','3');">
							<s:property value="failnum" /> </a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="6">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>



