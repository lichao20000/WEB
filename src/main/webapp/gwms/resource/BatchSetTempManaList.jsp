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

	function deviceDetail(type,countNum,task_id){
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!queryDevList.action?countNum='/>"+countNum+"&&type="+type+"&&task_id="+task_id;
		window.open(url ,"","left=140,top=80,width=1000,height=600,resizable=yes,scrollbars=yes");
	}
	
	
	function updateStatus(task_id,type){
		
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!updateCount.action'/>";
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
		
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!update.action'/>";
		$.post(url,{
			task_id:task_id,
			type:type
		},function(ajax){
			if("1" == ajax){
				if('1'==type){
				    alert("暂停成功");
				}
				else{
					alert("启动成功");
				}
				
				window.parent.query();
			}else if("0" == ajax){
				alert("修改失败");
			}
		});
	}
	
	
	
	function doDel(task_id){
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!del.action'/>";
		$.post(url,{
			task_id:task_id
		},function(ajax){
			if("1" == ajax){
				alert("删除成功");
				window.location.reload();
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
	    
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!delCount.action'/>";
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
	
	
	function detail(task_id){
		var url = "<s:url value='/gwms/resource/batchSetTempManaACT!queryTaskDetailById.action?task_id='/>"+task_id;
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
			<th>模板</th>
			<th>任务状态</th>
			<th>定制时间</th>
			<!-- <th>创建者</th> -->
			<th>总数</th>
			<th>成功数</th>
			<th>失败数</th>
			<th>未执行</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="taskList!=null">
			<s:if test="taskList.size()>0">
				<s:iterator value="taskList">
					<tr>
						<td align="center"><s:property value="task_name" /></td>
						<td align="center"><s:property value="template_name" /></td>
						<s:if test="status==1">
							<td align="center">正常</td>
						</s:if>
						<s:else>
							<td align="center">暂停</td>
						</s:else>
						
						<td align="center"><s:property value="add_time" /></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('total','<s:property value="totalNum" />','<s:property value="task_id" />')"><s:property value="totalNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('succ','<s:property value="succNum" />','<s:property value="task_id" />')"><s:property value="succNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('fail','<s:property value="failNum" />','<s:property value="task_id" />')"><s:property value="failNum" /></a></td>
						<td align="center"><a class='green_link' href="#" onclick="deviceDetail('unDone','<s:property value="unDoneNum" />','<s:property value="task_id" />')"><s:property value="unDoneNum" /></a></td>
						<td align="center">
						<s:if test="status==1"><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">暂停</a></s:if>
						<s:else><a href="javascript:updateStatus('<s:property value="task_id" />','<s:property value="status" />')">启动</a></s:else>
						<a href="javascript:del('<s:property value="task_id" />')">删除</a>
						<a href="javascript:detail('<s:property value="task_id" />')">详情</a>
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
					<lk:pages url="/gwms/resource/batchSetTempManaACT!queryList.action" styleClass=""
						showType="" isGoTo="true" changeNum="true" />
				</td>
			</tr>
		</tfoot>
	</tbody>

	

</table>
</body>
</html>