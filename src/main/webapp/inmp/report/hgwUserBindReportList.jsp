<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		绑定率统计
	</caption>
	<thead>
		<tr>
			<th width="25%">
				属地
			</th>
			<th width="25%">
				已绑定数
			</th>
			<th width="25%">
				未绑定数
			</th>
			<th width="25%">
				绑定率
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
					   <a
							href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="usertype"/>','<s:property value="startTime"/>','<s:property value="endTime"/>');">
							<s:property value="city_name" /></a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','1','<s:property value="usertype"/>');">
							<s:property value="detotal" /> </a>

					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','0','<s:property value="usertype"/>');">
							<s:property value="nototal" /> </a>
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
				<IMG SRC="<s:url value="/images/inmp/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="ToExcel('<s:property value="cityId"/>','<s:property value="usertype"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


