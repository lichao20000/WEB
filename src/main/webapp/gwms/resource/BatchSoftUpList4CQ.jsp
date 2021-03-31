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
<title>升级任务列表</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
	$(function(){
		parent.dyniframesize();
	});

	
	function updateStatus(task_id,type){
		
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!updateCount.action'/>";
		$.post(url,{
			task_id:task_id,
			type:type
		},function(ajax){
			if("ok" == ajax){
				doUpdate(task_id,type);
			}else if("tooMuch" == ajax){
				var  bln   =   window.confirm("需要更新的数量大于40W,建议晚上执行!(点击是继续更新)");  
			    if(bln == true) {
			    	doUpdate(task_id,type);
			    }else{
			    	return false;
			    }
			}
		});
	}
	
	
	function doUpdate(task_id,type){
		
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!update.action'/>";
		$.post(url,{
			task_id:task_id,
			type:type
		},function(ajax){
			if("1" == ajax){
				alert("修改成功");
				window.parent.query();
			}else if("0" == ajax){
				alert("修改失败");
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
	
	
	function del(task_id){
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
	
	
	function detail(task_id,task_name){
		/* var url = "<s:url value='/gwms/resource/batchSoftUpACT!queryTaskGyCity.action'/>";
		$.post(url,{
			task_id:task_id,
			task_name:task_name
		},function(ajax){
			if("1" == ajax){
				alert("修改成功");
				window.parent.query();
			}else if("0" == ajax){
				alert("修改失败");
			}
		}); */
		
		
		var url = "<s:url value='/gwms/resource/batchSoftUpACT!queryTaskGyCity.action?task_id='/>"+task_id+"&&task_name="+encodeURI(encodeURI(task_name));
		window.open(url ,"","left=80,top=80,width=800,height=600,resizable=yes,scrollbars=yes");
		
		/* var form = document.getElementById("mainForm");
		form.action = "<s:url value='/gwms/resource/batchSoftUpACT!queryList.action'/>";
		form.submit(); */
	}
	
	function task_detail(task_id,task_name){
		var url = "<s:url value='/gwms/resource/software!queryTaskDetailById.action?taskId='/>"+task_id;
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
			<ms:inArea areaCode="cq_dx" notInMode="false">
			<th>模式</th>
			<th>状态</th>
			</ms:inArea>
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
						<td align="center"><s:property value="task_name" /></td>
						<ms:inArea areaCode="cq_dx" notInMode="false">
						<td align="center"><s:property value="task_model" /></td>
						<s:if test="status==1">
							<td align="center">正常</td>
						</s:if>
						<s:else>
							<td align="center">暂停</td>
						</s:else>
						</ms:inArea>
						
						<td align="center"><s:property value="set_time" /></td>
						<td align="center"><s:property value="totalNum" /></td>
						<td align="center"><s:property value="succNum" /></td>
						<td align="center"><s:property value="failNum" /></td>
						<td align="center"><s:property value="unDoneNum"/></td>
						<td align="center">
						<ms:inArea areaCode="cq_dx,sx_lt" notInMode="false">
						<s:if test="status==1"><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">暂停</a></s:if>
						<s:else><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">启动</a></s:else>
						</ms:inArea>
						<a href="javascript:del('<s:property value="task_id" />')">删除</a>
						<a href="javascript:detail('<s:property value="task_id" />','<s:property value="task_name" />')">详情</a>
						<ms:inArea areaCode="sx_lt" notInMode="false">
						<a href="javascript:task_detail('<s:property value="task_id" />','<s:property value="task_name" />')">任务</a>
						</ms:inArea>
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
					<lk:pages url="/gwms/resource/batchSoftUpACT!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>