<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>
					����
				</th>
				<th>
					δ����
				</th>
				<th>
					���˳ɹ���
				</th>
				<th>
					����ʧ����
				</th>
				<th>
					���˳ɹ��ʣ��ɹ�/��������
				</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">
							<s:property value="cityname" />

					</td>
					<td align="center">
						<a href="javascript:openHgw('<s:property value="city"/>','0')">
						   				<s:property value="undo" /> </a>
					</td>
					<td align="center">
						<a href="javascript:openHgw('<s:property value="city"/>','1')">
										<s:property value="succ" /> </a>
					</td>
					<td align="center">
						<a href="javascript:openHgw('<s:property value="city"/>','2')">
										<s:property value="fail" /> </a>
					</td>
					<td align="center">
										<s:property value="rate" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan='5'>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
						style='cursor: hand'
						onclick="ToExcel('<s:property value="cityId"/>','<s:property value="checkState"/>')">
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">
					û�������Ϣ
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


