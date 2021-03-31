<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
	function ListToExcel(cityId,startOpenDate,endOpenDate,temperature,bais_current,voltage) {
		var page = "<s:url value='/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatementDevExcel.action'/>?"
				+ "city_id="+ cityId
				+ "&startOpenDate="+ startOpenDate
				+ "&endOpenDate="+ endOpenDate
				+ "&temperature="+ temperature
				+ "&bais_current="+bais_current
				+ "&voltage="+voltage;
		document.all("childFrm").src = page;
	}
</script>

<table class="listtable">
	<caption>终端物理状态统计详细信息</caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>LOID</th>
			<th>设备序列号</th>
			<th>终端类型</th>
			<th>温度(度) </th>
			<th>电流(2微安)</th>
			<th>电压(100微伏) </th>
			<th>上报时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployDevList!=null">
			<s:if test="deployDevList.size()>0">
				<s:iterator value="deployDevList">
					<tr>
						<td><s:property value="city_name" /></td>
						<td><s:property value="loid" /></td>
						<td><s:property value="device_serialnumber" /></td>
						<td><s:property value="device_type" /></td>
						<td><s:property value="temperature" /></td>
						<td><s:property value="bais_current" /></td>
						<td><s:property value="vottage" /></td>
						<td><s:property value="upload_time" /></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>暂无新增功能部署报表信息</td>
				</tr>
			</s:else>
			
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>

		<tr>
			<td colspan=8><span style="float: right;"> <lk:pages
						url="/itms/resource/TerminalEnvironmentStatementQuery!queryTerminalEnvironmentStatementDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" />
			</span> <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="city_id"/>','<s:property value="startOpenDate"/>','<s:property value="endOpenDate"/>','<s:property value="temperature"/>','<s:property value="bais_current"/>','<s:property value="voltage"/>')">
			</td>
		</tr>

		<TR>
			<TD align="center" colspan=8>
				<button onclick="javascript:window.close();">&nbsp;关
					闭&nbsp;</button>
			</TD>
		</TR>
	</tfoot>

	<tr STYLE="display: none">
		<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>

<%@ include file="/foot.jsp"%>
