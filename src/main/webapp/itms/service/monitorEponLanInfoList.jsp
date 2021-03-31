<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查看采集详情</title>
<%
	/**
	 * 查看采集详情
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2010-09-16
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<style tyle="text/css">
	.myDiv{
		 position: absolute ;
		 top:1px ;
		 left:1px ;
		 height:500px ;
		 width:3200;
	}
</style>	
</head>
<body>
<div class="myDiv">
<table class="listTable"  id="listTable">
	<caption align="left">查看采集详情</caption>
	<thead>
		<tr>
			<th>开始时间</th>
			<th>采集结果</th>
			<th>PON发送字节数</th>
			<th>平均发送速率（KB/S）</th>
			<th>PON接收字节数</th>
			<th>平均接收速率（KB/S）</th>
			<th>LAN1发送字节数</th>
			<th>LAN1平均发送速率（KB/S）</th>
			<th>LAN1接收字节数</th>
			<th>LAN1平均接收速率（KB/S）</th>
			<th>LAN2发送字节数</th>
			<th>LAN2平均发送速率（KB/S）</th>
			<th>LAN2接收字节数</th>
			<th>LAN2平均接收速率（KB/S）</th>
			<th>LAN3发送字节数</th>
			<th>LAN3平均发送速率（KB/S）</th>
			<th>LAN3接收字节数</th>
			<th>LAN3平均接收速率（KB/S）</th>
			<th>LAN4发送字节数</th>
			<th>LAN4平均发送速率（KB/S）</th>
			<th>LAN4接收字节数</th>
			<th>LAN4平均接收速率（KB/S）</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="eponLanList!=null">
			<s:if test="eponLanList.size()>0">
				<s:iterator value="eponLanList">
						<tr>
							<td><s:property value="start_time" /></td>
							<td><s:property value="status" /></td>
							<td><s:property value="pon_send" /></td>
							<td><s:property value="pon_send_average" /></td>
							<td><s:property value="pon_receive" /></td>
							<td><s:property value="pon_receive_average" /></td>
							
							<td><s:property value="lan1_send" /></td>
							<td><s:property value="lan1_send_average" /></td>
							<td><s:property value="lan1_receive" /></td>
							<td><s:property value="lan1_receive_average" /></td>
							
							<td><s:property value="lan2_send" /></td>
							<td><s:property value="lan2_send_average" /></td>
							<td><s:property value="lan2_receive" /></td>
							<td><s:property value="lan2_receive_average" /></td>
							
							<td><s:property value="lan3_send" /></td>
							<td><s:property value="lan3_send_average" /></td>
							<td><s:property value="lan3_receive" /></td>
							<td><s:property value="lan3_receive_average" /></td>
							
							<td><s:property value="lan4_send" /></td>
							<td><s:property value="lan4_send_average" /></td>
							<td><s:property value="lan4_receive" /></td>
							<td><s:property value="lan4_receive_average" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=22>定制任务详细信息不存在!</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=22>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="22" align="right">
		 	<lk:pages
				url="/itms/service/monitorFlowInfoQuery!getePonLanInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
	</tfoot>
</table>
</div>
</body>
</html>