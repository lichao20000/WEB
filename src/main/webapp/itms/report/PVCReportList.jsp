<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<thead>
		<tr>
			<th width="25%">
				����
			</th>
			<th width="25%">
				�Ѳ�����
			</th>
			<th width="25%">
				δ������
			</th>
			<th width="25%">
				������
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td>
						<s:if test="isAll==1">
							<strong><s:property value="city_name" /> </strong>
						</s:if>
						<s:else>
							<s:property value="city_name" />
						</s:else>
					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="isItv"/>','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','1','<s:property value="isAll"/>','<s:property value="prodSpecId"/>');">
							<s:property value="detotal" /> </a>

					</td>
					<td>
						<a
							href="javascript:openHgw('<s:property value="isItv"/>','<s:property value="city_id"/>','<s:property value="starttime1"/>','<s:property value="endtime1"/>','0','<s:property value="isAll"/>','<s:property value="prodSpecId"/>');">
							<s:property value="nototal" /> </a>
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
			<td colspan="4">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
					style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="4">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


