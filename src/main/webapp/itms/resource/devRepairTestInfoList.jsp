<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
	$(function() {
		parent.document.getElementById("queryButton").disabled = false;
		parent.dyniframesize();
	});

	function getRepairDevDetail(device_serialnumber,attribution_city) {
		// 中文乱码解决
		attribution_city = encodeURI(attribution_city);  
		attribution_city = encodeURI(attribution_city);  
		var page = "<s:url value='/itms/resource/devRepairTestInfo!queryRepairDevDetail.action'/>?device_serialnumber="+device_serialnumber+"&attribution_city="+attribution_city;
		window.open(page,"","left=20,top=20,width=1200,height=600,resizable=no,scrollbars=yes");
	};
</script>
<div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		class="it_table">
		<tbody>
			<tr>
				<th>维修厂家</th>
				<th>保内保外</th>
				<th>设备序列号</th>
				<th>终端厂家</th>
				<th>设备型号</th>
				<th>检测结果</th>
				<th>发往城市</th>
				<th>发往区县</th>
				<th>返修出厂日期</th>
				<th>现网使用合格情况</th>
				<th>操作</th>
			</tr>
			<s:if test="DevRepairTestInfoList != null ">
				<s:if test="DevRepairTestInfoList.size() > 0">
					<s:iterator value="DevRepairTestInfoList">
						<tr>
							<td><s:property value="repair_vendor" /></td>
							<td><s:property value="insurance_status" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="device_vendor" /></td>
							<td><s:property value="device_model" /></td>
							<td><s:property value="check_result" /></td>
							<td><s:property value="attribution_city" /></td>
							<td><s:property value="send_city" /></td>
							<td><s:property value="manufacture_date" /></td>
							<td><s:property value="qualified_status" /></td>
							<td><a class="itta_more" href="javascript:void(0);"
								onclick="getRepairDevDetail('<s:property value="device_serialnumber" />','<s:property value="attribution_city" />')">详细信息</a></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=11 align=left>没有查询到相关数据！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11 align=left>没有查询到相关数据！</td>
				</tr>
			</s:else>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="11" align="right">[ 统计总数 : <s:property value='queryCount'/> ]&nbsp;<lk:pages
						url="/itms/resource/devRepairTestInfo!queryRepairDev.action"
						styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</tfoot>
	</table>
</div>
