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
		<td rowspan="2" align="center" bgcolor="#E1EEEE">
			����
		</td>
		<td colspan="2" align="center" height="27" bgcolor="#E1EEEE">
			δ���û���
		</td>
		<td colspan="2" align="center" height="27" bgcolor="#E1EEEE">
			δ���ն���
		</td>
	</tr>
	<tr class="">
		<td align="center" bgcolor="#F4F4F0">
			IPOSSû��ͬ������Ӧ�û���¼
		</td>
		<td align="center" bgcolor="#F4F4F0">
			IPOSSͬ�����û���Ӧ��MAC��ַ��ITMS�в�����
		</td>
		<td align="center" bgcolor="#F4F4F0">
			�ն�δ�ϱ�MAC��ַ
		</td>
		<td align="center" bgcolor="#F4F4F0">
			�ն��ϱ���MAC��ַ����IPOSSͬ��������û����Ӧ��¼
		</td>
	</tr>
	<tbody>

		<s:if test="data.size()>0">
			<s:iterator value="data">
				<tr>
					<td bgcolor="#F4F4F0" align="center">
						<s:if test='isAll=="1"'>
							<s:property value="city_name" />
						</s:if>
						<s:else>
							<a
								href="javascript:countBycityId('<s:property value="city_id"/>','<s:property value="starttime"/>','<s:property value="endtime"/>');">
								<s:property value="city_name" /> </a>
						</s:else>
					</td>
					<td align="center">
						<s:property value="nobindUserNoIposs" />

					</td>
					<td align="center">
						<s:property value="noBindUserNoMac" />
					</td>
					<td align="center">
						<s:property value="noBindDevcieNoMac" />
					</td>
					<td align="center">
						<s:property value="noBindDeviceNoIposs" />
					</td>

				</tr>
			</s:iterator>
		</s:if>
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


