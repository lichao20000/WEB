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
				桥接用户
			</th>
			<th rowspan="2">
				路由用户
			</th>
			<th rowspan="2">
				E8-B
			</th>
			<th rowspan="2">
				E8-C
			</th>
			<th rowspan="2">
				悦me
			</th>
			<th rowspan="2">
				支持WIFI
			</th>
			<th rowspan="2">
				不支持WIFI
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
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','wan_type1');">
							<s:property value="wan_type1" /> </a>

					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','wan_type2');">
							<s:property value="wan_type2" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','device_type_e8b');">
							<s:property value="device_type_e8b" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','device_type_e8c');">
							<s:property value="device_type_e8c" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','device_type_e8cme');">
							<s:property value="device_type_e8cme" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','wlan_num_support');">
							<s:property value="wlan_num_support" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','wlan_num_not_support');">
							<s:property value="wlan_num_not_support" /> </a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="8">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="8">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


