<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>查询结果</caption>
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>属地</th>
				<th>不规范版本终端数</th>
				<th>规范版本终端数</th>
				<th>终端总数</th>
				<th>规范比率</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center"><a
						href="javascript:countBycityId('<s:property value="cityId"/>','<s:property value="accessType"/>','<s:property value="userType"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="isActive"/>');">
							<s:property value="cityName" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('2','<s:property value="cityId"/>','<s:property value="accessType"/>','<s:property value="userType"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="isActive"/>');">
							<s:property value="notNormNum" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('1','<s:property value="cityId"/>','<s:property value="accessType"/>','<s:property value="userType"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="isActive"/>');">
							<s:property value="normNum" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('3','<s:property value="cityId"/>','<s:property value="accessType"/>','<s:property value="userType"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="vendorId"/>','<s:property value="deviceModelId"/>','<s:property value="isActive"/>');">
							<s:property value="totalNum" />
					</a></td>
					<td align="center"><s:property value="normRate" /> </a></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='5'><IMG SRC="<s:url value="/images/excel.gif"/>"
					BORDER='0' ALT='导出列表' style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="accessType"/>',
										 '<s:property value="userType"/>','<s:property value="starttime1"/>',
						                 '<s:property value="endtime1"/>','<s:property value="vendorId"/>',
						                 '<s:property value="deviceModelId"/>','<s:property value="deviceTypeId"/>','<s:property value="isActive"/>')">
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">没有相关信息</td>
			</tr>
		</tfoot>
	</s:else>
	<tr STYLE="display: none">
		<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>


