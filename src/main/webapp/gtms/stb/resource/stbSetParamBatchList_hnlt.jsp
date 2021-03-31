<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});

function getConParamInfo(deviceId)
{
	parent.getConParamInfo(deviceId);
}

function updateConParamInfo(deviceId)
{
//	var page = "<s:url value='/gtms/stb/resource/stbSetConfParam!getConParamInfo.action'/>?"
//					+"deviceId=" + deviceId;	
	var page = "<s:url value='/gtms/stb/resource/stbSetParamInfoUpdate_hnlt.jsp'/>?"
				+"device_id=" + deviceId;
	window.open(page, "","left=100,top=100,width=1200,height=400,resizable=yes,scrollbars=no");
}

function deleteConParamInfo(deviceId,mac)
{
	parent.deleteConParamInfo(deviceId,mac);
}
</script>

<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th width="10%">设备序列号</th>
			<th width="10%">业务账号</th>
			<th width="8%">MAC地址</th>
			<th width="6%">厂商</th>
			<th width="8%">型号</th>
			<th width="9%">硬件版本</th>
			<th width="9%">软件版本</th>
			<th width="8%">属地</th>
			<th width="8%">最近修改时间</th>
			<th width="8%">最近生效时间</th>
			<th width="9%">操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
				<tr align="center">
					<td><s:property value="sn" /></td>
					<td><s:property value="serv_account" /></td>
					<td><s:property value="mac" /></td>
					<td><s:property value="vendorName" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="hardwareversion" /></td>
					<td><s:property value="softwareversion" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="update_time" /></td>
					<td><s:property value="set_stb_time" /></td>
					<td>
						<a href="javascript:getConParamInfo('<s:property value="device_id"/>')">详细信息</a>
						<s:if test="showType!=3">
						|<a href="javascript:updateConParamInfo('<s:property value="device_id"/>')">编辑</a>
						</s:if>
						<s:if test="showType!=2 && showType!=3">
						|<a href="javascript:deleteConParamInfo('<s:property value="device_id"/>',
														'<s:property value="mac"/>')">删除</a>
						</s:if>	
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11 align=left>没有查询到相关数据！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="11" align="right" height="15">
				<!-- 
				<img src="<s:url value="/images/excel.gif"/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()" align=left />
				-->
				[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/stb/resource/stbSetConfParam!getConfParamList.action" 
					showType=""	isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
