<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>定制任务详情信息</title>
<%
	/**
	 * 定制任务详情信息
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

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	/**
	function ToExcel(){
			parent.ToExcel();
	}
	*/
	
	function queryDetail(task_id,device_id){
        var page =  "<s:url value='/itms/service/monitorFlowInfoQuery!getePonLanInfo.action'/>?task_id="+task_id+"&device_id="+device_id;
		window.open(page,"","left=20,top=20,width=1000,height=600,resizable=no,scrollbars=yes");
	}
	
	function deleteTask(task_id){
		var url = "<s:url value='/itms/service/monitorFlowInfoQuery!deleteMonitorFlow.action'/>";
		$.post(url,{task_id:task_id},function(ajax){
				alert(ajax);
				parent.query();
			});
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>定制任务详情信息</caption>
	<thead>
		<tr>
			<th>任务ID</th>
			<th>设备序列号</th>
			<th>开始时间</th>
			<th>结束时间</th>
			<th>时间间隔（分钟）</th>
			<th>采集次数</th>
			<th>采集状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="monitorList!=null">
			<s:if test="monitorList.size()>0">
				<s:iterator value="monitorList">
						<tr>
							<td><s:property value="task_id" /></td>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="start_time" /></td>
							<td><s:property value="end_time" /></td>
							<td><s:property value="interval" /></td>
							<td><s:property value="times" /></td>
							<td><s:property value="status" /></td>
							<td><a href="javaScript:queryDetail('<s:property value="task_id" />','<s:property value="device_id" />')" ><b>查看采集详情 </b></a>&nbsp;&nbsp;|&nbsp;&nbsp;
								<a href="javaScript:deleteTask('<s:property value="task_id" />')"><b>删除</b></a></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>系统没有该用户的业务信息!</td>
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
			<td colspan="8" align="right">
		 	<lk:pages
				url="/itms/service/monitorFlowInfoQuery!getMonitorFlowQuery.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
<!--  导出Excel
		<tr>
			<td colspan="8" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
-->
	</tfoot>
</table>
</body>
</html>