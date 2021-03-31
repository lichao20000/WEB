<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>长时间在线在线光猫批量重启管理</title>
<link rel="stylesheet" href="<s:url value='/css3/c_table.css'/>" type="text/css">
<link rel="stylesheet" href="<s:url value='/css3/global.css'/>" type="text/css">
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="<s:url value='/Js/jQuerySplitPage-linkage.js'/>"></script>

<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});
</script>
</head>

<body>
	<table class="listtable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th>任务号</th>
				<th>任务描述</th>
				<th>执行状态</th>
				<th>定制时间</th>
				<th>执行时间</th>
				<th>执行数量</th>
				<th>执行成功数</th>
				<th>执行失败数</th>
				<th>待执行数</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		
			<s:if test="taskList!=null">
				<s:if test="taskList.size()>0">
					<s:iterator value="taskList">
						<tr>
							<td align="center"><s:property value="task_name" /></td>
							<td align="center"><s:property value="task_desc" /></td>
							<td align="center"><s:property value="task_status" /></td>
							<td align="center"><s:property value="add_time" /></td>
							<td align="center"><s:property value="start_time" /></td>
							<td align="center">
							 <a href="javascript:detail('<s:property value="task_name" />','total')"><s:property value="total" /></a>
							</td>
							<td align="center">
							 <a href="javascript:detail('<s:property value="task_name" />','succ')"><s:property value="succ" /></a>
							</td>
							<td align="center">
							 <a href="javascript:detail('<s:property value="task_name" />','fail')"><s:property value="fail" /></a>
							</td>
							<td align="center">
							  <a href="javascript:detail('<s:property value="task_name" />','wait')"><s:property value="wait" /></a>
							</td>
							<td align="center">
							<!-- 未执行的任务可以删除，暂停 -->
								<s:if test="1==status">
									<a href="javascript:operTask('<s:property value="task_name" />','1')">删除</a>|
									<a href="javascript:operTask('<s:property value="task_name" />','2')">暂停</a>
								</s:if> 
								<s:elseif test="-2==status">
								<a href="javascript:operTask('<s:property value="task_name" />','1')">删除</a>|
								  <a href="javascript:operTask('<s:property value="task_name" />','3')">激活</a>
								</s:elseif> 
								<s:elseif test="2!=status&&3!=status">
								 <a href="javascript:operTask('<s:property value="task_name" />','1')">删除</a>
								</s:elseif>
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=10>没有查询到相关任务！</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=10>没有查询到相关任务！</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="11" align="right">
					<lk:pages url="/gwms/resource/batchRestartManagerACT!qryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true"  />
				</td>
			</tr>
			 <tr>
				<td colspan="14" align="right">
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
					style='cursor: hand' onclick="javascript:toExcel()"></td>
			</tr> 
		</tfoot>

	</table>
</body>
<script>

function operTask(taskId,operType)
{
	parent.operTask(taskId,operType);
}


function toExcel(){
	parent.toExcel();
}

function detail(taskId,type){
	parent.detail(taskId,type);
}
</script>
</html>