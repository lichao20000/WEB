<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%--
	/**
	 * 规范版本查询列表页面
	 *
	 * @author 姓名(工号) Tel:电话
	 * @version 1.0
	 * @since ${date} ${time}
	 * 
	 * <br>版权：南京联创科技 网管科技部
	 * 
	 */
 --%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>规范版本查询</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

	function updateStatus(upTaskName,upStatus){
		if (confirm("请确定修改此任务状态!")) {
			if("是" == upStatus){
			upStatus ="1";
		}else{
			upStatus ="0";
		}
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!updateStatus.action'/>";
		$.post(url,{
		upTaskName:upTaskName,
		upStatus:upStatus
	},function(ajax){
		if("1" == ajax){
			alert("修改成功");
			window.parent.query();
		}else if("0" == ajax){
			alert("修改失败");
		}
	});
		}
		
}

</script>

</head>

<body >
<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>任务名称</th>
			<th>定制设备数</th>
			<th>成功数</th>
			<th>未做数</th>
			<th>是否执行中</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="doneNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center"><s:property value="status"/></td>
						<td align="center"><a href="javascript:updateStatus('<s:property value="task_name" />','<s:property value="status" />')">修改</a></td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>没有查询到相关版本！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>没有查询到相关版本！</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="6" align="right">
					<lk:pages url="/gwms/resource/batchSoftUpACT!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>