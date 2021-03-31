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
			<th>
				属地
			</th>
			<th>
				ADSL
			</th>
			<th>
				LAN
			</th>
			<th>
				EPON
			</th>
			<th>
				GPON
			</th>
			<th>
				总数
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="countList.size()>0">
			<s:iterator value="countList">
				<tr>
					<td>
							<s:property value="city_name" />
					</td>
					<td>
						<s:if test="adslNum==0">
							<s:property value="adslNum" />
						</s:if>
						<s:else>
							<a
							href="javascript:openHgwDetail('<s:property value="city_id"/>','1','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="adslNum" /> </a>
						</s:else>
					</td>
					<td>
						<s:if test="lanNum==0">
							<s:property value="lanNum" />
						</s:if>
						<s:else>
							<a
							href="javascript:openHgwDetail('<s:property value="city_id"/>','2','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="lanNum" /> </a>
						</s:else>
					</td>
					<td>
						<s:if test="eponNum==0">
							<s:property value="eponNum" />
						</s:if>
						<s:else>
							<a
							href="javascript:openHgwDetail('<s:property value="city_id"/>','3','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="eponNum" /> </a>
						</s:else>
					</td>
					<td>
						<s:if test="gponNum==0">
							<s:property value="gponNum" />
						</s:if>
						<s:else>
							<a
							href="javascript:openHgwDetail('<s:property value="city_id"/>','4','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="gponNum" /> </a>
						</s:else>
					</td>
					<td>
						<s:if test="totalNum==0">
							<s:property value="totalNum" />
						</s:if>
						<s:else>
							<a
							href="javascript:openHgwDetail('<s:property value="city_id"/>','<s:property value="accessstyle"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="totalNum" /> </a>
						</s:else>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="3">
					该地市下无此终端类型设备！
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="accessstyle"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="9">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


