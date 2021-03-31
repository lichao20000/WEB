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

function ListToExcel(deviceSerialnumber,starttime,endtime,queryTimeType,loid){
	var page = "<s:url value='/ids/bytesReceivedDetection!queryLanAndPonDataExcel.action'/>?"
		+ "deviceSerialnumber="+deviceSerialnumber
		+ "&starttime="+starttime
		+ "&endtime=" + endtime
		+ "&queryTimeType="+queryTimeType
		+ "&loid="+loid;
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th width="7%">loid</th>
				<th width="7%">设备序列号</th>
				<th width="7%">LAN1口流量统计(M)</th>
				<th width="7%">LAN1口平均速率(KB/s)</th>
				<th width="7%">LAN2口流量统计(M)</th>
				<th width="7%">LAN2口平均速率(KB/s)</th>
				<th width="7%">LAN3口流量统计(M)</th>
				<th width="7%">LAN3口平均速率(KB/s)</th>
				<th width="7%">LAN4口流量统计(M)</th>
				<th width="7%">LAN4口平均速率(KB/s)</th>
				<th width="7%">PON口流量统计(M)</th>
				<th width="7%">上报时间</th>
				<th width="7%">入库时间</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr>
							<td align="center"><s:property value="loid" /></td>
							<td align="center"><s:property value="device_serialnumber" /></td>
							<td align="center"><s:property value="bytes1" /></td>
							<td align="center"><s:property value="bytespert1" /></td>
							<td align="center"><s:property value="bytes2" /></td>
							<td align="center"><s:property value="bytespert2" /></td>
							<td align="center"><s:property value="bytes3" /></td>
							<td align="center"><s:property value="bytespert3" /></td>
							<td align="center"><s:property value="bytes4" /></td>
							<td align="center"><s:property value="bytespert4" /></td>
							<td align="center"><s:property value="ponbytes" /></td>
							<td align="center"><s:property value="upload_time" /></td>
							<td align="center"><s:property value="add_time" /></td>
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
				<td colspan="13" align="right">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand;float: left'
						onclick="ListToExcel('<s:property value="deviceSerialnumber"/>','<s:property value="starttime"/>','<s:property value="endtime"/>','<s:property value="queryTimeType"/>','<s:property value="loid"/>')">
						
						</td>
			</tr>
<tr STYLE="display: none">
			<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
		</tfoot>
	</table>
</body>
</html>