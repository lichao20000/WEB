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
				ͳ�ƽ��
			</th>
		</tr>
	</thead>
	<tr>
		<td rowspan="1" align="center" bgcolor="#E1EEEE">
			����
		</td>
		<td colspan="1" align="center" height="27" bgcolor="#E1EEEE">
			�Žӿ���
		</td>
		<td colspan="1" align="center" height="27" bgcolor="#E1EEEE">
			·�ɿ���
		</td>
	</tr>
	<tbody>
		<tr>
			<td bgcolor="#F4F4F0" align="center">
				ʡ����
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
				�Ͼ�
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
				����
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
				����
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
				����
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
				��
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
				����
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
				̩��
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
				����
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
				��ͨ
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
				�γ�
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
				����
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
				���Ƹ�
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
				��Ǩ
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
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
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


