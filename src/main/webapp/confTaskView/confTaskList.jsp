<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>配置任务视图</title>
<%
	/**
		 * 配置任务视图界面
		 * 
		 * @author qixueqi(4174)
		 * @version 1.0
		 * @since 2008-12-18
		 * @category
		 */
%>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">

$(function(){
	
	var curPage = "<s:property value="curPage_splitPage"/>";
	var num = "<s:property value="num_splitPage"/>";
	var maxPage = "<s:property value="maxPage_splitPage"/>";
	var paramList = "<s:property value="paramList_splitPage"/>";
	
	//初始化翻页按钮
	$.initPage(
		"<s:url value="/confTaskView/ConfTaskView!goPage.action"/>", 
		"#toolbar", 
		curPage, 
		num, 
		maxPage, 
		paramList
	);
	
	var msg = '<s:property value="msg"/>';
	if (msg != ''){
		alert(msg);
	}
})

//详细情况查询
function task_id_info(task_id){
	//var url = "<s:url value="/servStrategy/ServStrategy!servStrategyFrame.action"/>?task_id="+task_id;
	var url = "<s:url value="/gwms/service/servStrategyView!init.action"/>?taskId="+task_id;
	parent.location.href = url;
}

</script>

</head>

<body>
<form name="selectList" action="" method="post"/>
	<input type="hidden" name="task_name" value="<s:property value="task_name"/>">
	<input type="hidden" name="order_time_start" value="<s:property value="order_time_start"/>">
	<input type="hidden" name="order_time_end" value="<s:property value="order_time_end"/>">
	<input type="hidden" name="is_check" value="<s:property value="is_check"/>">
	<input type="hidden" name="is_over" value="<s:property value="is_over"/>">
</form>
	<table border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#999999">
					<tr>
						<th>任务名称</th>
						<th>定制人</th>
						<th>定制时间</th>
						<th>审核状态</th>
						<th>任务状态</th>
						<th>完成率</th>
						<th>成功率</th>
						<th>操作</th>
					</tr>
					<s:iterator value="confTaskList">
						<tr bgcolor="#FFFFFF">
							<td class=column1><s:property value="task_name"/></td>
							<td class=column1><s:property value="order_acc_oid"/></td>
							<td class=column1><s:property value="order_time"/></td>
							<td class=column1><s:property value="is_check"/></td>
							<td class=column1><s:property value="is_over"/></td>
							<td class=column1><s:property value="comp_perc"/></td>
							<td class=column1><s:property value="succ_perc"/></td>
							<td align="center" class=column1>
								<a href="#" onclick="task_id_info('<s:property value="task_id"/>')">详细</a>
							</td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
		<tr>
			<td height='12'>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#999999">
					<tr><td class="green_foot" align="right"><div id="toolbar"></div></td></tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>