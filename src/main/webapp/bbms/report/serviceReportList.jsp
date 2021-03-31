<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.*"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<table class="listtable">
	<caption>
		<s:property value="stat_day"/>
		业务使用统计<s:if test='reportType=="week"'>周报表</s:if>
		<s:elseif test='reportType=="month"'>月报表</s:elseif>
	</caption>
	<s:if test="titleList.size()>0">
		<thead>

			<tr>
				<th>
					属地
				</th>
				<s:iterator value="titleList">
					<th>
						<s:property value="service_name" />
					</th>
				</s:iterator>
			</tr>
		</thead>

		<tbody>
			<s:if test="data.size()>0">
				<s:iterator value="data" var="map1">
					<tr>
						<td align="center">
							<s:if test='isAll=="1"'>
								<s:property value="city_name" />
							</s:if>
							<s:else>
								<a
									href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="serviceId"/>','<s:property value="reportType"/>','<s:property value="stat_day"/>');">
									<s:property value="city_name" /> </a>
							</s:else>							
						</td>	
						<s:iterator value="titleList" var="map2">
							<td align="center">
								<s:iterator value="map1" var="map1id" status="st">
										<s:if test="key==service_name">
											<s:property value="value" />
										</s:if> 
								</s:iterator>
							</td>
						</s:iterator>
					</tr>
				</s:iterator>
			</s:if>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='<s:property value="titleList.size()+1" />'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ToExcel('<s:property value="cityId"/>','<s:property value="serviceId"/>','<s:property value="reportType"/>','<s:property value="stat_day"/>')">
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">
					相关业务信息尚未统计
				</td>
			</tr>
		</tfoot>
	</s:else>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


