<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  预检预修告警信息
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
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

<script type="text/javascript">
$(function() {
	$("#trData",parent.document).hide();
	$("#btn",parent.document).attr('disabled',false);
	parent.dyniframesize();
});

function ListToExcel(serialnumber,starttime3,endtime3){
	var page = "<s:url value='/ids/batchPingTest!queryPingResultExcel.action'/>?"
		+ "serialnumber="+serialnumber
		+ "&starttime3="+starttime3
		+ "&endtime3=" + endtime3;
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th width="5%">属地</th>
				<th width="10%">设备厂商</th>
				<th width="5%">型号</th>
				<th width="10%">设备序列号</th>
				<th width="10%">测试时间</th>
				<th width="5%">通道类型</th>
				<th width="5%">成功数</th>
				<th width="5%">失败数</th>
				<th width="10%">平均响应时间</th>
				<th width="10%">最小响应时间</th>
				<th width="10%">最大响应时间</th>
				<th width="5%">丢包率</th>
				<th width="10%">测试URL</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr>
							<td align="center"><s:property value="city_name" /></td>
							<td align="center"><s:property value="vendor_name" /></td>
							<td align="center"><s:property value="device_model" /></td>
							<td align="center"><s:property value="device_serialnumber" /></td>
							<td align="center"><s:property value="test_time" /></td>
							<td align="center"><s:property value="ping_type" /></td>
							<td align="center"><s:property value="success_count" /></td>
							<td align="center"><s:property value="failure_count" /></td>
							<td align="center"><s:property value="avg_resp_time" /></td>
							<td align="center"><s:property value="min_resp_time" /></td>
							<td align="center"><s:property value="max_resp_time" /></td>
							<td align="center"><s:property value="packet_loss_rate" /></td>
							<td align="center"><s:property value="url" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=13>系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=13>系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="13" align="right"><span  style="float:right"><lk:pages
						url="/ids/batchPingTest!queryPingResult.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></span>
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand;float: left'
						onclick="ListToExcel('<s:property value="serialnumber"/>','<s:property value="starttime3"/>','<s:property value="endtime3"/>')">
						
						</td>
			</tr>
<tr STYLE="display: none">
			<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
		</tfoot>
	</table>
</body>
</html>