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
			<th >LOID</th>
			<th>�豸���к�</th>
			<th>״̬</th>
			<th>PON�ڷ���⹦��(dBm)</th>
			<th>PON�ڽ��չ⹦��(dBm)</th>
			<th>��ģ���¶�(��)</th>
			<th>��ģ�鹩���ѹ(΢��)</th>
			<th>��ģ��ƫ�õ���(΢��)</th>
			<th>�ϱ�ʱ��</th>
			<th>���ʱ��</th>
		</tr>
	</thead>
	
	<tbody>
		<s:if test="ponstatusList!=null">
			<s:if test="ponstatusList.size()>0">
				<s:iterator value="ponstatusList">
					<tr>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="status" /></td>
						<td><s:property value="tx_power" /></td>
						<td><s:property value="rx_power" /></td>
						<td><s:property value="temperature" /></td>
						<td><s:property value="vottage" /></td>
						<td><s:property value="bais_current" /></td>
						<td><s:property value="upload_time" /></td>
						<td><s:property value="add_time" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				 <td colspan="10">����״̬��������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="10">����״̬��������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10" align="right">
				<lk:pages url="/ids/DeviceMonitoringQuery!queryPonStatusByES.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>