<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
function ListToExcel(cityId,startOpenDate,endOpenDate,failCode) {
	var page="<s:url value='/ids/PPPoEFailReasonCount!getPppoeFailReasonInfoExcel.action'/>?"
		+ "city_id=" + cityId
		+ "&failCode=" + failCode
		+ "&startOpenDate=" +startOpenDate
		+ "&endOpenDate=" +endOpenDate;
	document.all("childFrm").src=page;
}
</script>

<table class="listtable">
	<caption>
		PPPOE失败原因统计详细信息
	</caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>LOID</th>
			<th>终端序列号</th>
			<th>终端型号</th>
			<th>pppoe拨号状态</th>
			<th>拨号失败原因</th>
			<th>上报时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="pppoeInfoList.size()>0">
			<s:iterator value="pppoeInfoList">
				<tr>
					<td>
						<s:property value="city_name" />
					</td>
					<td>
						<s:property value="loid" />
					</td>
					<td>
						<s:property value="device_serialnumber" />
					</td>
					<td>
						<s:property value="device_type" />
					</td>
					<td>
						<s:property value="status" />
					</td>
					<td>
						<s:property value="reason" />
					</td>
					<td>
						<s:property value="upload_time" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>
					没有PPPOE失败原因
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
	
		<tr>
			<td colspan=8>
				<span style="float: right;"> <lk:pages
						url="/ids/PPPoEFailReasonCount!getPppoeFailReasonInfo.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /> </span>
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'    
						onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>','<s:property value="failCode"/>')"> 
			</td>
		</tr>


		<TR>
			<TD align="center" colspan=8>
				<button onclick="javascript:window.close();">
					&nbsp;关 闭&nbsp;
				</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="8">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
