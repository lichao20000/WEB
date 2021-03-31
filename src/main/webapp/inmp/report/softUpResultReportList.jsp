<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="../../css/inmp/css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/global.css" type="text/css">

<table class="listtable">
	<caption>
		统计结果
	</caption>
	<thead>
		<tr>
			<th rowspan="2">
				属地
			</th>
			<th rowspan="2">
				总配置数
			</th>
			<th rowspan="2">
				成功
			</th>
			<th rowspan="2">
				未触发
			</th>
			<th colspan="2">
				失败
			</th>
			<th rowspan="2">
				成功率
			</th>
		</tr>
		<tr>
			<th>
				等待重做
			</th>
			<th>
				彻底失败
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
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','','','');">
							<s:property value="allup" /> </a>

					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','100','1','');">
							<s:property value="successnum" /> </a>
					</td>
					<!-- 
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','100','1','1');">
							<s:property value="sucmananum" /> </a>
					</td>
					
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','100','1','-1');">
							<s:property value="sucnomananum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','100','1','0');">
							<s:property value="sucnogathernum" /> </a>
					</td>-->
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','0','0','');">
							<s:property value="noupnum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','0','not1','');">
							<s:property value="nextnum" /> </a>
					</td>
					<td>
						<a
							href="javascript:openDev('<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','100','not1','');">
							<s:property value="failnum" /> </a>
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
			<td colspan="7">
				<IMG SRC="../../images/inmp/excel.gif" BORDER='0' ALT='导出列表'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="7">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


