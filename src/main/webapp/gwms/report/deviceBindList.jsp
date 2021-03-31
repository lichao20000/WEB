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
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>
					属地
				</th>
				<th>
					注册终端数
				</th>
				<th>
					近期活跃数
				</th>
				<th>
					自动绑定用户数
				</th>
				<th>
					绑定率
				</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">

						<a href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>','<s:property value="accessType"/>','<s:property value="usertype"/>');">
							<s:property value="city_name" /> </a>

					</td>
					<td align="center">
						<a
							href="javascript:openHgw('0','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>','<s:property value="accessType"/>','<s:property value="usertype"/>');">
							<s:property value="haveRegisteDeviceNum" /> </a>
					</td>
					<td align="center">
						<a
							href="javascript:openHgw('1','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>','<s:property value="accessType"/>','<s:property value="usertype"/>');">
							<s:property value="recentActiveDeviceNum" /> </a>
					</td>
					<td align="center">
						<a
							href="javascript:openHgw('2','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>','<s:property value="accessType"/>','<s:property value="usertype"/>');">
							<s:property value="autoBindUserNum" /> </a>
					</td>
					<td align="center">
						<s:property value="percent" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='5'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						                 '<s:property value="endtime1"/>','<s:property value="vendorId"/>',
						                 '<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>',
						                 '<s:property value="accessType"/>','<s:property value="usertype"/>')">
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">
					没有相关绑定信息
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


