<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%@taglib prefix="lk" uri="/linkage"%> 

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK">
	<title>Insert title here</title>
	<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<style type="text/css">
	/* 选中行样式 */
	.select{
		background-color: #E8E8FF;
	}
	</style>
<script type="text/javascript"	src="<s:url value="/lims/itv/zeroconf/js/rightMenu.js"/>"></script>
	<script type="text/javascript">
$(function(){
		parent.dyniframesize();
});

function showDetail(vendor_id,device_model_id,task_id,queryType)
{
	page = "<s:url value='/gtms/stb/resource/openAdvertQuery!queryTotalList.action'/>?vendorId=" + vendor_id+"&&deviceModelId="+device_model_id+"&&taskId="+task_id+"&&queryType="+queryType;
    window.open(page,"","left=200,top=100,width=800,height=400,resizable=yes,scrollbars=yes");
}
</script>
</head>
<body>
	<table class="listtable" align="center" width="100%">
		<thead>
			<tr align="center">
				<th width="">厂商</th>
				<th width="">设备型号</th>
				<th width="">软件版本</th>
				<th width="">总配置数</th>
				<th width="">成功</th>
				<th width="">失败</th>
				<th width="">未触发</th>
				<th width="">成功率</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="list.size>0">
			<s:iterator value="list">
				<tr>
					<td><s:property value="vendor_name"/></td>
					<td><s:property value="device_model_name"/></td>
					<td><s:property value="devicetype_name"/></td>
					<td><a href="javascript:showDetail('<s:property value="vendor_id"/>','<s:property value="devicetype_id"/>','<s:property value="task_id"/>','<s:property value="3"/>')"><s:property value="total"/></a></td>
					<td><a href="javascript:showDetail('<s:property value="vendor_id"/>','<s:property value="devicetype_id"/>','<s:property value="task_id"/>','<s:property value="0"/>')"><s:property value="success"/></a></td>
					<td><a href="javascript:showDetail('<s:property value="vendor_id"/>','<s:property value="devicetype_id"/>','<s:property value="task_id"/>','<s:property value="1"/>')"><s:property value="fails"/></a></td>
					<td><a href="javascript:showDetail('<s:property value="vendor_id"/>','<s:property value="devicetype_id"/>','<s:property value="task_id"/>','<s:property value="2"/>')"><s:property value="noTrigger"/></a></td>
					<td><s:property value="pert"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="8">找不到相关数据...</td>
			</tr>
		</s:else>
		</tbody>	
		<tfoot>
		<tr>
		</tr>
	</tfoot>
	</table>
</body>
</html>