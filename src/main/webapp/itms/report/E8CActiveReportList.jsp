<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<form id="selectForm" name="selectForm" action="" target="childFrm">
<input type="hidden" name="cityId" value='<s:property value="cityId"/>'/>
<input type="hidden" name="isActive" value='<s:property value="isActive"/>'/>
<input type="hidden" name="startDealdate" value='<s:property value="startDealdate"/>'/>
<input type="hidden" name="endDealdate" value='<s:property value="endDealdate"/>'/>
<table class="listtable">
	<caption>
		查询结果
	</caption>
	<thead>
		<tr>
			<th width="25%">
				属地
			</th>
			<th width="25%">
				<s:property value="ttitle1" />
			</th>
			<th width="25%">
				<s:property value="ttitle2" />
			</th>
			<th width="25%">
				<s:property value="ttitle3" />
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list.size()>0">
			<s:iterator value="list">
				<tr>
					<td>
							<s:property value="city_name" />
					</td>
					<td>
						<a
							href="javascript:openCus('<s:property value="cityId"/>','<s:property value="isActive"/>','<s:property value="startDealdate"/>','<s:property value="endDealdate"/>','1');">
							<s:property value="totals" /> </a>

					</td>
					<td>
						<a
							href="javascript:openCus('<s:property value="cityId"/>','<s:property value="isActive"/>','<s:property value="startDealdate"/>','<s:property value="endDealdate"/>','2');">
							<s:property value="total" /> </a>
					</td>
					<td>
						<s:property value="percent" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>','<s:property value="isActive"/>','<s:property value="startDealdate"/>','<s:property value="endDealdate"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" name="childFrm" src=""></iframe>
		</td>
	</tr>

</table>

</form>
