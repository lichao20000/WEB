<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		ͳ�ƽ��
	</caption>
	<thead>
		<tr>
			<th>
				����
			</th>
			<th>
				��������
			</th>
			<th>
				�ɹ�
			</th>
			<th>
				δ��
			</th>
			<th>
				ʧ��
			</th>
			<th>
				�ɹ���
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<a href="javascript: countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
						<s:property value="city_name" />
						</a>
					</td>
					<td>
						<a href="javascript: openDev('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','2');">
						<s:property value="allup" /> 
						</a>
					</td>
					<td>
						<a href="javascript: openDev('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','1');">
						<s:property value="successnum" />
						</a>
					</td>
					<td>
						<a href="javascript: openDev('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','0');">
						<s:property value="noupnum" />
						</a>
					</td>
					<td>
						<a href="javascript: openDev('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','-1');">
						<s:property value="failnum" />
						</a>
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
				<!-- <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand'
					onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>')"> -->
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="7">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


