<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>

	</caption>
	<thead>
		<tr>
			<th colspan="5">
				统计结果
			</th>
		</tr>
	</thead>
	<tr>
		<td rowspan="2" align="center" bgcolor="#E1EEEE">
			属地
		</td>
		<td colspan="2" align="center" height="27" bgcolor="#E1EEEE">
			未绑定用户数
		</td>
		<td colspan="2" align="center" height="27" bgcolor="#E1EEEE">
			未绑定终端数
		</td>
	</tr>
	<tr class="">
		<td align="center" bgcolor="#F4F4F0">
			IPOSS没有同步到相应用户记录
		</td>
		<td align="center" bgcolor="#F4F4F0">
			IPOSS同步到用户对应的MAC地址在ITMS中不存在
		</td>
		<td align="center" bgcolor="#F4F4F0">
			终端未上报MAC地址
		</td>
		<td align="center" bgcolor="#F4F4F0">
			终端上报的MAC地址，在IPOSS同步数据中没有相应记录
		</td>
	</tr>
	<tbody>

		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td bgcolor="#F4F4F0" align="center">
						<s:if test='isAll=="1"'>
							<s:property value="city_name" />
						</s:if>
						<s:else>
							<a
								href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
								<s:property value="city_name" /> </a>
						</s:else>
					</td>
					<td align="center">
						<s:property value="nobindUserNoIposs" />

					</td>
					<td align="center">
						<s:property value="noBindUserNoMac" />
					</td>
					<td align="center">
						<s:property value="noBindDevcieNoMac" />
					</td>
					<td align="center">
						<s:property value="noBindDeviceNoIposs" />
					</td>

				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="5">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime"/>','<s:property value="endtime"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


