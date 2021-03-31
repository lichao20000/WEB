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
function queryDetail(taskid){
	var url = "<s:url value='/ids/batchPingTest!queryTaskInfo.action'/>?taskid="+taskid;
	window.open(url,"","left=60,top=60,width=920,height=500,resizable=yes,scrollbars=yes");
	
}
function del(taskid){
	var msg = "您真确定要删除吗？请确认！"; 
	if (confirm(msg)==true){ 
		var url = "<s:url value='/ids/batchPingTest!delTask.action'/>";
		$.post(url,{
			taskid : taskid
		},function(ajax){
			parent.doQuery();
			
		});
	}
	
}
function ListToExcel(taskname,acc_loginname,starttime,endtime){
	var page = "<s:url value='/ids/batchPingTest!queryTaskExcel.action'/>?"
		+ "taskname="+taskname
		+ "&acc_loginname="+acc_loginname
		+ "&starttime2=" + starttime
		+ "&endtime2=" + endtime;
	document.all("childFrm").src=page;
}
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<thead>
			<tr>
				<th width="25%">任务名称</th>
				<th width="25%">定制人</th>
				<th width="25%">定制时间</th>
				<th width="25%">操作</th>
			</tr>
		</thead>
		<tbody>
			<s:if test="list!=null">
				<s:if test="list.size()>0">
					<s:iterator value="list">
						<tr id="<s:property value="task_id" />">
							<td align="center"><s:property value="task_name" /></td>
							<td align="center"><s:property value="acc_loginname" /></td>
							<td align="center"><s:property value="add_time" /></td>
							<td align="center">
							<input type="button" name="btn" value="查看详细" onclick="queryDetail('<s:property value="task_id" />')" />&nbsp;&nbsp;
							<input type="button" name="btn2" value="删除" onclick="del('<s:property value="task_id" />')" />
							</td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=4>系统没有匹配到相应信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>系统没有匹配到相应信息!</td>
				</tr>
			</s:else>
		</tbody>

		<tfoot>
			<tr>
				<td colspan="4" align="right"><span  style="float:right"><lk:pages
						url="/ids/batchPingTest!queryTask.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" /></span>
				<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						style='cursor: hand;float: left'
						onclick="ListToExcel('<s:property value="taskname"/>','<s:property value="acc_loginname"/>','<s:property value="starttime2"/>','<s:property value="endtime2"/>')">		
				</td>
			</tr>
	<tr STYLE="display: none">
			<td colspan="8"><iframe id="childFrm" src=""></iframe></td>
	</tr>
		</tfoot>
	</table>
</body>
</html>