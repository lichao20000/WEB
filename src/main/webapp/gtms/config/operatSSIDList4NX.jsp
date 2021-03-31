<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>

<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css"></link>
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css"></link>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/My97DatePicker/WdatePicker.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>
<script type="text/javascript">
$(function() {
	parent.dyniframesize();
});

function toExcel()
{
	parent.toExcel();
}
</script>

<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th width="10%">任务名称</th>
			<th width="18%">属地</th>
			<th width="8%">厂商</th>
			<th width="8%">型号</th>
			<th width="10%">设备序列号</th>
			<th width="8%">时间</th>
			<th width="8%">状态</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null && data.size()>0">
			<s:iterator value="data">
				<tr align="center">
					<td><s:property value="task_name" /></td>
					<td><s:property value="city_name" /></td>
					<td><s:property value="vendor_name" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="sn" /></td>
					<td><s:property value="operat_time" /></td>
					<td><s:property value="result_desc" /></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan=7 align=left>没有查询到相关数据！</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan=7 align="right" height="15">
				<img src="<s:url value='/images/excel.gif'/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()" align=left />
				[ 统计总数 : <s:property value='queryCount' /> ]&nbsp;
				<lk:pages url="/gtms/config/operatSSID!queryList.action" 
					showType="" isGoTo="true" changeNum="true" />
			</td>
		</tr>
	</tfoot>
</table>
