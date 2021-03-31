<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">

<table class="listtable">
	<caption>
		�������
	</caption>
	<s:if test="data.size()>0">
		<thead>

			<tr>
				<th>
					ҵ���˺�
				</th>
				<th>
					������MAC
				</th>
				<th>
					���ʱ��
				</th>
				<th>
					����Ա
				</th>
				<th>
					��ע
				</th>
			</tr>
		</thead>

		<tbody>
			<s:iterator value="data" var="map1">
				<tr>
					<td align="center">
						<s:property value="username" />
					</td>
					<td align="center">
						<s:property value="mac" />
					</td>
					<td align="center">
						<s:property value="addtime" />
					</td>
					<td align="center">
						<s:property value="per_name" />
					</td>
					<td align="center">
						<s:property value="remark" />
					</td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="5" align="right"></td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tfoot>
			<tr>
				<td align="left">
					û����ذ���Ϣ
				</td>
			</tr>
		</tfoot>
	</s:else>
	<tr STYLE="display: none">
		<td colspan="5">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


