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
			<th>设备序列号</th>
			<th>状态</th>
			<th>PON口发射光功率(dBm)</th>
			<th>PON口接收光功率(dBm)</th>
			<th>光模块温度(度)</th>
			<th>光模块供电电压(微伏)</th>
			<th>光模块偏置电流(微安)</th>
			<th>上报时间</th>
			<th>入库时间</th>
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
				 <td colspan="10">物理状态监控无数据！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan="10">物理状态监控无数据！</td>
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