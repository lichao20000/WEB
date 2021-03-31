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
		<td rowspan="1" align="center" bgcolor="#E1EEEE">
			属地
		</td>
		<td colspan="1" align="center" height="27" bgcolor="#E1EEEE">
			桥接开户
		</td>
		<td colspan="1" align="center" height="27" bgcolor="#E1EEEE">
			路由开户
		</td>
	</tr>
	<tbody>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				省中心
			</td>
			<td align="center">
				0
			</td>
			<td align="center">
				3
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				南京
			</td>
			<td align="center">
				43
			</td>
			<td align="center">
				23
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				苏州
			</td>
			<td align="center">
				24
			</td>
			<td align="center">
				12
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				无锡
			</td>
			<td align="center">
				14
			</td>
			<td align="center">
				2
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				常州
			</td>
			<td align="center">
				19
			</td>
			<td align="center">
				3
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				镇江
			</td>
			<td align="center">
				3
			</td>
			<td align="center">
				3
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				扬州
			</td>
			<td align="center">
				6
			</td>
			<td align="center">
				1
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				泰州
			</td>
			<td align="center">
				24
			</td>
			<td align="center">
				34
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				徐州
			</td>
			<td align="center">
				1
			</td>
			<td align="center">
				8
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				南通
			</td>
			<td align="center">
				4
			</td>
			<td align="center">
				8
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				盐城
			</td>
			<td align="center">
				2
			</td>
			<td align="center">
				3
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				淮安
			</td>
			<td align="center">
				2
			</td>
			<td align="center">
				13
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				连云港
			</td>
			<td align="center">
				1
			</td>
			<td align="center">
				1
			</td>
		</tr>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				宿迁
			</td>
			<td align="center">
				1
			</td>
			<td align="center">
				1
			</td>
		</tr>
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


