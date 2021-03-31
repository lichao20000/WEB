<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
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
				总开户数
			</th>
			<th>
				MAC比对绑定<br>新建用户
			</th>
			<th>
				手工绑定
			</th>
			<th>
				自助绑定
			</th>
			<th>
				MAC比对绑定
			</th>
			<th>
				有效绑定数
			</th>
			<th>
				自助绑定<br>MAC认证
			</th>
			<th>
				手工绑定<br>MAC认证
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<a
							href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
							<s:property value="city_name" /> </a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','','');">
							<s:property value="allopened" /> </a>

					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','5','','');">
							<s:property value="macuser" /> </a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','1','');">
							<s:property value="handbind" /></a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','2','');">
							<s:property value="selfbind" /></a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','4','');">
							<s:property value="macbind" /></a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','1,2,4','');">
							<s:property value="effectivebind" /></a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','2','1');">
							<s:property value="selfmac" /></a>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','1','1');">
							<s:property value="handmac" /></a>
					</td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9">
				<IMG SRC="<s:url value="../../images/inmp/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="9">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


