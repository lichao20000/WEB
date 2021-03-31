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
				<th>下发成功数</th>
				<th>下发失败数</th>
				<th>下发总数</th>
				<th>成功率</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">
						<s:property value="cityName" />
					</td>
					<td align="center"><a
						href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>','success'
													)">
							<s:property value="totalSucNum" />
					</a>
					</td>
					<td align="center"><a
							href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>','failure'
													)">
						<s:property value="totalFalNum" />
					</a>
					</td>
					<td align="center"><a
							href="javascript:openHgw('<s:property value="cityId"/>','<s:property value="starttime1"/>',
													'<s:property value="endtime1"/>','-1','<s:property value="gwType"/>','total'
													)">
						<s:property value="totalNum" />
					</a>
					</td>
					<td align="center">
						<s:property value="totalSucRate" />
					</td>

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


