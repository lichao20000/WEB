<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>ͳ�ƽ��</caption>
	<thead>
		<tr>
			<th rowspan=2>����</th>
			<th colspan=3>�û���</th>
			<th colspan=3>�豸��</th>
		</tr>
		<tr>
			<th>E8-B</th>
			<th>EB-C</th>
			<th>�����ں�����</th>
			<th>EB-B</th>
			<th>EB-C</th>
			<th>�����ں�����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td><a
						href="javascript:queryByCityId('<s:property value="city_id" />')">
							<s:property value="city_name" />
					</a></td>
					<td><s:property value="e8b_count" /></td>
					<td><s:property value="e8c_count" /></td>
					<td><s:property value="all_e8c_count" /></td>
					<td><s:property value="bind_e8b_count" /></td>
					<td><s:property value="bind_e8c_count" /></td>
					<td><s:property value="bind_all_e8c_count" /></td>
				</tr>
			</s:iterator>
		</s:if>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="9"><IMG SRC="<s:url value="/images/excel.gif"/>"
				BORDER='0' ALT='�����б�' style='cursor: hand'
				onclick="ToExcel('<s:property value="cityId"/>')"></td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="9"><iframe id="childFrm" src=""></iframe></td>
	</tr>

</table>


