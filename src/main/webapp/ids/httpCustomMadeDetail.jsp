<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="../../Js/jquery.js"></script>
<script type="text/javascript" src="../../Js/jQuerySplitPage-linkage.js"></script>


<script type="text/javascript">
function deviceShow(deviceId) {
	var page = "<s:url value='/Resource/DeviceShow.jsp'/>?device_id="+deviceId;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
};
</script>


<table width="100%" class="listtable" id=userTable>
	<thead>
		<tr>
			<th width="5%">����</th>
			<th width="10%">�豸���к�</th>
			<th width="10%">����˺�</th>
			<th width="15%">IP��ַ</th>
			<th width="10%">���Զ���ʱ��</th>
			<th width="10%">���ؿ�ʼʱ��</th>
			<th width="10%">���ؽ���ʱ��</th>
			<th width="10%">�����ֽ���</th>
			<th width="15%">���ٽ��</th>
			<th width="5%">����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="list != null ">
			<s:if test="list.size() > 0">
				<s:iterator value="list">
					<tr>
						<td align="center"><s:property value="cityName" /></td>
						<td align="center"><s:property value="deviceSerialnumber" /></td>
						<td align="center"><s:property value="userName" /></td>
						<td align="center"><s:property value="IpAddr" /></td>
						<td align="center"><s:property value="taskMadeTime" /></td>
						<td align="center"><s:property value="loadBeginTime" /></td>
						<td align="center"><s:property value="loadEndTime" /></td>
						<td align="center"><s:property value="receiveBities" /></td>
						<td align="center"><s:property value="testResult" /></td>
						<td align="center"><IMG
							SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�鿴�豸��ϸ'
							onclick="deviceShow('<s:property value='deviceId' />')"
							style='cursor: hand'></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10 align=left>û�в�ѯ��������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=10 align=left>û�в�ѯ��������ݣ�</td>
			</tr>
		</s:else>
	<tfoot>
		<tr>
			<td colspan="10" align="right"><lk:pages
					url="/ids/httpCustomMadeQuery!getTaskDevList.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
	</tbody>
</table>