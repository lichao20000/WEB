<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
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
				�ܿ�����
			</th>
			<th>
				MAC�ȶ԰�<br>�½��û�
			</th>
			<th>
				�ֹ���
			</th>
			<th>
				������
			</th>
			<th>
				MAC�ȶ԰�
			</th>
			<th>
				��Ч����
			</th>
			<th>
				������<br>MAC��֤
			</th>
			<th>
				�ֹ���<br>MAC��֤
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
				<IMG SRC="<s:url value="../../images/inmp/excel.gif"/>" BORDER='0' ALT='�����б�'
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


