<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.module.gwms.util.StringUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.*"%>
<link rel="stylesheet" href="../../css/inmp/css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/global.css" type="text/css">

<table class="listtable">
	<caption>
		统计结果
	</caption>
	<s:if test="titleList.size()>0">
		<thead>

			<tr>
				<th>
					属地
				</th>
				<th>
					总开户数
				</th>
				<th>
					同步用户数
				</th>
				<s:iterator value="titleList">
					<th>
						<s:property value="type_name" />
					</th>
				</s:iterator>
				<th>
					已绑定用户数
				</th>
				<th>
					自动绑定率
				</th>
			</tr>
		</thead>

		<tbody>
			<s:if test="data.size()>0">
				<s:iterator value="data" var="map1">
					<tr>
						<td align="center">

							<a
								href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="usertype"/>','<s:property value="is_active"/>');">
								<s:property value="city_name" /> </a>

						</td>
						<td align="center">
							<a
								href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','','','<s:property value="usertype"/>','<s:property value="is_active"/>');">
								<s:property value="allopened" /> </a>
						</td>
						<td align="center">
							<a
								href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','4','','','<s:property value="usertype"/>','<s:property value="is_active"/>');">
								<s:property value="synCount" />
							</a>
						</td>
						<s:iterator value="titleList" var="map2">
							<td align="center">
								<s:iterator value="map1" var="map1id" status="st">
									<a
										href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','<s:property value="userline"/>','','<s:property value="usertype"/>','<s:property value="is_active"/>');">
										<s:if test="key==type_name">
											<s:property value="value" />
										</s:if> </a>
								</s:iterator>
							</td>
						</s:iterator>

						<td align="center">
							<a
								href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','all','','<s:property value="usertype"/>','<s:property value="is_active"/>');">
								<s:property value="bindnum" /> </a>
						</td>
						<td align="center">
							<s:property value="percent" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='<s:property value="titleList.size()+5" />'>
					<IMG SRC="<s:url value="../../images/inmp/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','<s:property value="usertype"/>','<s:property value="is_active"/>')">
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


