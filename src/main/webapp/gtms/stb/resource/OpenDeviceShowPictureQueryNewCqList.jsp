<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<style type="text/css">
td {
	white-space: nowrap;
	overflow: hidden;
}
</style>

<script language="JavaScript">
	
$(function(){
	parent.dyniframesize();
});	
function viewTask(taskId){ 
	var page = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!getShowPictureConfigResult.action'/>?taskId="+taskId;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}
function viewdetailTask(taskid)
{
	var page = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!getTaskDetail.action'/>?taskId="+taskid;
	window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
}

function deleteTask(taskId){
	if(confirm("删除是不可恢复的，确定要删除吗？") == true){     //如果用户单击了确定按钮 
		var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!doDelete.action'/>";
		$.post(url,{
			taskId : taskId
		},function(ajax){
			 if(ajax=='1'){
				 alert("删除成功");
			 }else{
				 alert("删除失败");
			 }
			 parent.query();
		});   
	}
}
function takeeffect(taskId,status){
	/* var	width=310;    
	var height=150; 
	var url="<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!validateCurUser.action'/>";
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
		return;
	}
	if(returnVal.charAt(0)!="1"){
		alert("用户密码验证失败");
		return;
	} */
		var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!updatestatus.action'/>";
		$.post(url,{
			taskId : taskId,
			status : status
		},function(ajax){
			alert(ajax);
			parent.query();
		});   
}
function loseefficacy(taskId,status){
	/* var	width=310;    
	var height=150; 
	var url="<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!validateCurUser.action'/>";
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
	if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
		return;
	}
	if(returnVal.charAt(0)!="1"){
		alert("用户密码验证失败");
		return;
	} */
		var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!updatestatus.action'/>";
		$.post(url,{
			taskId : taskId,
			status : status
		},function(ajax){
			 alert(ajax);
			 parent.query();
		});   
}
function CloseDetail(){
	$("div[@id='divDetail']").hide();
}

function initQuery()
{
	var startT = '<s:property value="startTime"/>';
	var endT = '<s:property value="endTime"/>';
	if('' != startT)
	{
		$("#start_time").val(startT);
		$("#end_time").val(endT);
	}
}
</script>
</head>

<body>
	<table class="listtable" width="100%" align="center">
		<thead>
			<tr>
				<th align="center" width="20%">
						任务名称
					</th>
					<th align="center" width="20%">
						定制人
					</th>
					<th align="center" width="20%">
						订制时间
					</th>
					<th align="center" width="20%">
						审核状态
					</th>
				<th align="center" width="20%">
						操作
					</th>
			</tr>
		</thead>
		<s:if test="tasklist!=null">
                <s:if test="tasklist.size()>0">
                        <tbody>
                                <s:iterator value="tasklist">
                                        <tr>
                                            <td align="center"><s:property value="task_name" /></td>
                                            <td align="center"><s:property value="acc_loginname" /></td>
                                            <td align="center"><s:property value="add_time" /></td>
                                            	<s:if test="status==1">
                                            <td align="center">已审核</td> </s:if><s:else > <td align="center">未审核</td></s:else>
                                            <td>&nbsp;&nbsp;
                                              <button name="viewButton" 
                                                      onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                              		查看结果
                                              	</button>&nbsp;&nbsp;
                                              <button name="viewDetailButton"
                                                      onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
                                          			    查看详细
                                          	   </button>&nbsp;&nbsp;
                                          	  <s:if test="statushow!=null&&statushow.size()>0">
                                          	   <s:if test="status==1">
                                          	     <button name="viewDetailButton"
                                                      onclick="javascript:takeeffect('<s:property value="task_id"/>','<s:property value="status"/>')">
                                          			取消
                                          	   </button>&nbsp;&nbsp;
                                          	   </s:if><s:elseif test="status==2">
                                          	     <button name="viewDetailButton"
                                                      onclick="javascript:loseefficacy('<s:property value="task_id"/>','<s:property value="status"/>')">
                                          			    审核
                                          	   </button>&nbsp;&nbsp;
                                          	   </s:elseif></s:if>
                                          	   
                                            	 <s:if test="statushow!=null&&statushow.size()>0">
                                            	 <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	   删除</button>
                                            	</s:if><s:else>
                                            	<s:if test="status==2">
                                          	   <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	   删除</button></s:if>
                                          	   </s:else> 
                                             </td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="5" align="right">
                                        <lk:pages url="/gtms/stb/resource/OpenDeviceShowPictureNew!init.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="true" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="5">
							<font color="red">没有定制的任务</font>
						</td>
					</tr>
				</tbody>
			</s:else>

</s:if>
	</table>
</body>
