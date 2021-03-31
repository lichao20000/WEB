<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<table class="listtable">
	<caption>
		��ѯ���
	</caption>
	<thead>
		<tr>
			<th>
				�豸���к�
			</th>
			<th>
				�û��˺�
			</th>
			<th>
				�û�����
			</th>
			<th>
				�Ƿ��
			</th>
			<th>
				�󶨷�ʽ
			</th>
			<th>
				�ϱ�ʱ��
			</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="hgwList!=null">
			<s:if test="hgwList.size()>0">
				<s:iterator value="hgwList">
					<tr>
						<td>
							<s:property value="device" />
						</td>
						<td>
							<s:property value="username" />
						</td>
						<td>
							<s:property value="serv_type" />
						</td>
						<td>
							<s:property value="is_bind" />
						</td>
						<td>
							<s:property value="bind_type" />
						</td>
						<td>
							<s:property value="inform_time" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<td colspan=6>
					ϵͳû������ϱ���Ϣ!
				</td>
			</s:else>
		</s:if>
		<s:else>
			<td colspan=6>
				ϵͳû��<s:property value="starttime" />����Ϣ!
			</td>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6">
				<lk:pages url="/itms/resource/deviceToUsername.action" styleClass=""
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="6">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>

</table>


