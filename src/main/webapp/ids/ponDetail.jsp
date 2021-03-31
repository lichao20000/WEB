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
			<th>PON�ڷ��͵İ���</th>
			<th>PON�ڽ��յİ���</th>
			<th>PON�ڷ��͵��ֽ���</th>
			<th>PON�ڽ��յ��ֽ���</th>
			<th>PON�ڷ��ͷ���֡��</th>
			<th>PON�ڽ��յ�FEC����֡��</th>
			<th>PON�ڽ��յ�HEC����֡��</th>
			<th>�ϱ�ʱ��</th>
			<th>���ʱ��</th>
		</tr>
	</thead>
	
	<tbody>
		<s:if test="ponList!=null">
			<s:if test="ponList.size()>0">
				<s:iterator value="ponList">
					<tr>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="packets_sent" /></td>
						<td><s:property value="packets_received" /></td>
						<td><s:property value="bytes_sent" /></td>
						<td><s:property value="bytes_received" /></td>
						<td><s:property value="drop_packets" /></td>
						<td><s:property value="fecerror" /></td>
						<td><s:property value="hecerror" /></td>
						<td><s:property value="upload_time" /></td>
						<td><s:property value="add_time" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				 <td colspan="11">PON�ڼ�������ݣ�</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="11">PON�ڼ�������ݣ�</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="11" align="right">
				<lk:pages url="/ids/DeviceMonitoringQuery!queryPONByES.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>