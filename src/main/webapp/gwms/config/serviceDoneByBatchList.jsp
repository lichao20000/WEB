<%@  page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>下发任务列表</title>

	<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
	<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
	<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">

	$(function(){
		parent.dyniframesize();
	});

	function del(task_id){
		var  bln1   =   window.confirm("确认要删除任务吗？(点击是继续)");  
	    if(bln1 != true) {
	    	return;
	    }
	    
		var url = "<s:url value='/gwms/config/serviceManSheet!del.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("1" == ajax){
				alert("删除成功");
				window.parent.query();
			}else if("0" == ajax||"" == ajax){
				alert("删除失败");
			}
		});
	}
	
	
	function detail(task_id,serv_type){
		
		var url = "<s:url value='/gwms/config/serviceManSheet!queryTaskDetail.action?task_id='/>"+task_id+"&&servTypeId="+encodeURI(encodeURI(serv_type));
		window.open(url ,"","left=80,top=80,width=800,height=600,resizable=yes,scrollbars=yes");

	}
	

	

</script>

</head>

<body >
<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>业务类型</th>
			<th>状态</th>
			<th>定制时间</th>
			<th>总数</th>
			<th>成功数</th>
			<th>失败数</th>
			<th>未做数</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<s:if test="servTypeNme==null || servTypeNme==''">
							<td align="center">全部</td>
						</s:if>
						<s:else>
						<td align="center"><s:property value="servTypeNme" /></td>
						</s:else>
						<s:if test="status==1">
							<td align="center">正常</td>
						</s:if>
						<s:else>
							<td align="center">暂停</td>
						</s:else>
						<td align="center"><s:property value="add_time" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="succNum" /></td>
						<td align="center"><s:property value="failNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center">
						<a href="javascript:del('<s:property value="task_id" />')">删除</a>
						<a href="javascript:detail('<s:property value="task_id" />','<s:property value="servTypeId" />')">详情</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=8>没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=8>没有查询到相关信息！</td>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
			<tr>
				<td colspan="8" align="right">
					<lk:pages url="/gwms/config/serviceManSheet!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
</table>
</body>
</html>