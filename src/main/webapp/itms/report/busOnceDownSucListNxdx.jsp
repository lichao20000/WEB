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
				<th>本地网</th>
				<th>宽带一次下发成功率</th>
				<th>IPTV一次下发成功率</th>
				<th>VoIP一次下发成功率</th>
				<th>总下发成功率</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center"><a
						href="javascript:countBycityId('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="gwType"/>','<s:property value="gwShare_vendorId"/>','<s:property value="gwShare_deviceModelId"/>','<s:property value="gwShare_devicetypeId"/>');">
							<s:property value="cityName" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						   							'<s:property value="endtime1"/>','10','<s:property value="gwType"/>',
						   							'<s:property value="gwShare_vendorId"/>','<s:property value="gwShare_deviceModelId"/>','<s:property value="gwShare_devicetypeId"/>')">
							<s:property value="broadbandSucRate" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','11','<s:property value="gwType"/>',
													'<s:property value="gwShare_vendorId"/>','<s:property value="gwShare_deviceModelId"/>','<s:property value="gwShare_devicetypeId"/>')">
							<s:property value="iptvSucRate" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','14','<s:property value="gwType"/>',
													'<s:property value="gwShare_vendorId"/>','<s:property value="gwShare_deviceModelId"/>','<s:property value="gwShare_devicetypeId"/>')">
							<s:property value="voipSucRate" />
					</a></td>
					<td align="center"><a
						href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>',
													'<s:property value="gwShare_vendorId"/>','<s:property value="gwShare_deviceModelId"/>','<s:property value="gwShare_devicetypeId"/>')">
							<s:property value="totalSucRate" />
					</a></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='5'><IMG SRC="<s:url value="/images/excel.gif"/>"
					BORDER='0' ALT='导出列表' style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>',
						                 '<s:property value="endtime1"/>','<s:property value="gwType"/>',
						                 '<s:property value="gwShare_vendorId"/>','<s:property value="gwShare_deviceModelId"/>','<s:property value="gwShare_devicetypeId"/>')">
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


