<%@  page  language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>

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
<title>组播参数统计列表</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

	function del(taskId){
		if(confirm("删除是不可恢复的，确定要删除吗？") == true){     //如果用户单击了确定按钮 
			var url = "<s:url value='/gwms/config/setMulticastBatch!doDelete.action'/>";
			$.post(url,{
				taskId : taskId
			},function(ajax){
				 if(ajax=='1'){
					 alert("删除成功");
				 }else{
					 alert("删除失败");
				 }
				 queryTask();
			});   
		}
	}
	
/* 	function del(task_id){
		var  bln1   =   window.confirm("确认要删除任务吗？(点击是继续)");  
	    if(bln1 != true) {
	    	return;
	    }
	    
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!delCount.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("ok" == ajax){
				doDel(task_id);
			}else if("tooMuch" == ajax){
				var  bln   =   window.confirm("需要删除的数量大于40W,建议晚上执行!(点击是继续删除)");  
			    if(bln == true) {
			    	doDel(task_id);
			    }else{
			    	return false;
			    }
			}
		});
	} 
		function doDel(task_id){
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!del.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("1" == ajax){
				alert("删除成功");
				window.parent.query();
			}else if("0" == ajax){
				alert("删除失败");
			}
		});
	}

	*/
	function queryTask(){
		// 普通方式提交
		parent.query();
	}
	
	function detail(task_id,task_name){
		
		var url = "<s:url value='/gwms/resource/setMulticastBatchCount!queryMulticastBatchDetail.action?task_id='/>"+task_id+"&task_name="+task_name;
		window.open(url ,"","left=80,top=80,width=800,height=600,resizable=yes,scrollbars=yes");
		
	}
	

</script>

</head>

<body >
<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
<INPUT TYPE="hidden" NAME="task_id" id="task_id" maxlength=60 class=bk size=20>
<INPUT TYPE="hidden" NAME="task_name" id="task_name" maxlength=60 class=bk size=20>
</FORM>
<table class="listtable">
	<caption>查询结果</caption>
	<thead>
		<tr>
			<th>任务名称</th>
			<th>任务ID</th>
			<th>定制时间</th>
			<th>总数</th>
			<th>成功数</th>
			<th>失败数</th>
			<th>未做数</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="data!=null">
			<s:if test="data.size()>0">
				<s:iterator value="data">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<td align="center"><s:property value="task_id" /></td>
						<td align="center"><s:property value="add_time" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="succNum" /></td>
						<td align="center"><s:property value="failNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center">
						<a href="javascript:del('<s:property value="task_id" />')">删除</a>
						<a href="javascript:detail('<s:property value="task_id" />','<s:property value="task_name" />')">详情</a>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=9>没有查询到相关信息！</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=9>没有查询到相关信息！</td>
			</tr>
		</s:else>
		<tfoot>
			<tr>
				<td colspan="9" align="right">
					<lk:pages url="/gwms/resource/setMulticastBatchCount!queryMulticastBatchList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>