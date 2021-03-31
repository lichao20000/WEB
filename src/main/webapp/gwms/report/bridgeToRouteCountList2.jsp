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
			<th width="20%">
				属地
			</th>
			<th width="20%">
				桥接数量
			</th>
			<th width="20%">
				路由数量
			</th>
			<th width="20%">
				桥接占比
			</th>
			<th width="20%">
				路由占比
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<a
							href="javascript:ToExcel('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','1');">
							<s:property value="bridgetotal" /> </a>
					</td>
					<td>
						<a
							href="javascript:ToExcel('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','2');">
							<s:property value="routetotal" /> </a>
					</td>
					<td>
						<s:property value="bridgePercent" />
					</td>
					<td>
						<s:property value="routePercent" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tr STYLE="display: none">
		<td colspan="3">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


