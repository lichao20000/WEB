<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">


<script type="text/javascript">
function ListToExcel(vendorId,modelId,endOpenDate,gn) {
	var page="<s:url value='/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevTypeListExcel.action'/>?"
		+ "vendorId=" + vendorId
		+ "&modelId=" + modelId 
		+ "&gn" + gn
		+ "&endOpenDate=" +endOpenDate;
	document.all("childFrm").src=page;
}
</script>

<table class="listtable">
	<caption>
		新增功能部署报表设备型号情况
	</caption>
	<thead>
		<tr>
			<th>区域</th>
			<th>LOID</th>
			<th>终端序列号</th>
			<th>部署时间</th>
			<th>开通状态</th>
			<th>采样周期</th>
			<th>设备型号</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deployDevTypeList.size()>0">
			<s:iterator value="deployDevTypeList">
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
						<s:property value="start_time" />
					</td>
					<td>
						<s:property value="status" />
					</td>
					<td>
						<s:property value="timelist" />
					</td>
					<td>
						<s:property value="device_model" />
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7>
					暂无新增功能部署报表设备型号情况
				</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
	
		<tr>
			<td colspan=7>
				<span style="float: right;"> <lk:pages url="/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevTypeList.action" 
						styleClass="" showType="" isGoTo="true" changeNum="true" /> </span>
						
					<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand'
						onclick="ListToExcel('<s:property value="vendorId"/>','<s:property value="modelId"/>','<s:property value="endOpenDate"/>','<s:property value="gn"/>')"> 
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
