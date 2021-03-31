<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
</script>

<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>LOID</th>
			<th>�豸���к�</th>
			<th>������·���</th>
			<th>�Ƿ�����</th>
			<th>��·״̬</th>
			<th>�ϱ�ʱ��</th>
			<th>���ʱ��</th>
			<th colspan="4">���һ������ע��ʧ��ԭ��</th>
		</tr>
	</thead>
	
	<tbody>
		<s:if test="voicestatusList!=null">
			<s:if test="voicestatusList.size()>0">
				<s:iterator value="voicestatusList">
					<tr>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="line_id" /></td>
						<td><s:property value="enabled" /></td>
						<td><s:property value="status" /></td>
						<td><s:property value="upload_time" /></td>
						<td><s:property value="add_time" /></td>
						<td colspan="4"><s:property value="reason" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				 <td colspan="11">����״̬��������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="11">����״̬��������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="11" align="right">
				<lk:pages url="/ids/DeviceMonitoringQuery!queryVoicestatusByES.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>