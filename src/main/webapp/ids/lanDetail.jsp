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
			<th>设备序列号</th>
			<th>LAN口编号</th>
			<th>LAN口状态</th>
			<th>LAN口发送的包数</th>
			<th>LAN口接收的包数</th>
			<th>LAN口发送的字节数</th>
			<th>LAN口接收的字节数</th>
			<th>上报时间</th>
			<th>入库时间</th>
		</tr>
	</thead>
	
	<tbody>
		<s:if test="lanList!=null">
			<s:if test="lanList.size()>0">
				<s:iterator value="lanList">
					<tr>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="lan_interface_config_id" /></td>
						<td><s:property value="status" /></td>
						<td><s:property value="packets_sent" /></td>
						<td><s:property value="packets_received" /></td>
						<td><s:property value="bytes_sent" /></td>
						<td><s:property value="bytes_received" /></td>
						<td><s:property value="upload_time" /></td>
						<td><s:property value="add_time" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
				 <td colspan="10">语音状态监控无数据！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="10">语音状态监控无数据！</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="10" align="right">
				<lk:pages url="/ids/DeviceMonitoringQuery!queryLANByES.action" styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>