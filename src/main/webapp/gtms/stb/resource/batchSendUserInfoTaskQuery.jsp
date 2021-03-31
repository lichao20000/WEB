<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		
	});	
	function viewTask(taskId){ 
		var page = "<s:url value='/gtms/stb/resource/batchSendUserInfo!getShowBatchSendUserInfoTaskResult.action'/>?taskId="+taskId;
		window.open(page,"","left=20,top=20,width=800,height=500,resizable=no,scrollbars=yes");
    }
	
	function viewdetailTask(taskid)
	{
		var page = "<s:url value='/gtms/stb/resource/batchSendUserInfo!getShowBatchSendUserInfoTaskDetail.action'/>?taskId="+taskid;
		window.open(page,"","left=20,top=20,width=800,height=400,resizable=no,scrollbars=yes");
	}
	
	function deleteTask(taskId){
		if(confirm("删除是不可恢复的，确定要删除吗？") == true){     //如果用户单击了确定按钮 
			var url = "<s:url value='/gtms/stb/resource/batchSendUserInfo!doDelete.action'/>";
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
	
	function beNotActived(taskId){
		if(confirm("设置任务失效是不可恢复的，确定要设置失效吗？") == true){     //如果用户单击了确定按钮 
			var url = "<s:url value='/gtms/stb/resource/batchSendUserInfo!beNotActived.action'/>";
			$.post(url,{
				taskId : taskId
			},function(ajax){
				 if(ajax=='1'){
					 alert("设置成功");
					 $("#"+taskId).html("失效");
				 }else{
					 alert("设置失败");
				 }
				// queryTask();
			});   
		}
	}
	
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
   	function queryTask()
	{
		var taskName = $("input[name=taskName]").val();
		var accName = $("input[name=accName]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var isActive = $("select[name=isActive]").val();
		var url = "<s:url value='/gtms/stb/resource/batchSendUserInfo!initImport.action'/>?taskName="+taskName+"&accName="+accName+"&startTime="+startTime+"&endTime="+endTime+"&isActive="+isActive;
	    window.location.href=url;
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
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：根据业务账号进行批量业务下发任务管理界面
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="batchConfigNode!initImport.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="deviceModelIds" value=""/>
			<table width="98%" class="querytable" align="center">
			<tr>
				<td class="title_1" width="15%">
						名称
				</td>
				<td width="35%">
					<input type="text" id="taskName" name="taskName" value="<s:property value="taskName" />" size="26">
				</td>
				<td class="title_1" width="15%">
						定制人
				</td>
				<td width="35%">
					<input type="text" id="accName" name="accName" value="<s:property value="accName" />" size="26">
				</td>
			</tr>
			<tr>
			<td class="title_1" width="15%">
						开始时间
				</td>
				<td>
					<input type="text" name="startTime" id="start_time" class='bk' readonly
						value="<s:property value='startTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
				<!-- 
				<td width="35%" >
					<span id="startTime"><lk:date id="start_time" name="startTime" type="day" defaultDate="" maxDateOffset="0" dateOffset="-15" /></span>
				</td>
				 -->
				<td class="title_1" width="15%">
					结束时间
				</td>
				<td>
					<input type="text" name="endTime" id="end_time" class='bk' readonly
						value="<s:property value='endTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="选择" />
				</td>
				<!-- 
				<td width="35%">
					<span id="endTime"><lk:date id="end_time" name="endTime" dateOffset="0" defaultDate="" type="day" maxDateOffset="0" /></span>
				</td>
				 -->
			</tr>
			<tr>
			<td class="title_1" width="15%">
						是否有效
				</td>
				<td>
					<select id="isActive" name="isActive" >
					<option value="-1">-全部-</option>
					<option value="1">-是-</option>
					<option value="0">-否-</option>
					</select>
				</td>
				<td width="15%" colspan="2">
					&nbsp;
				</td>
			</tr>
			<tr>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button onclick="queryTask()">
								查询
							</button>
						</div>
					</td>
				</tr>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="17%">
						任务名称
					</th>
					<th align="center" width="17%">
						定制人
					</th>
					<th align="center" width="16%">
						订制时间
					</th>
					<th align="center" width="16%">
						更新时间
					</th>
					<th align="center" width="12%">
						是否有效
					</th>
					<th align="center" width="22%">
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
                                            <td align="center"><s:property value="update_time" /></td>
                                            <td align="center" id="<s:property value="task_id"/>"><s:property value="is_active" /></td>
                                            <td>&nbsp;&nbsp;
                                              <button name="viewButton" 
                                                      onclick="javascript:beNotActived('<s:property value="task_id"/>')">
                                              		失效
                                              </button>&nbsp;&nbsp;
                                          	   <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	   删除</button>&nbsp;&nbsp;
                                              <button name="viewButton" 
                                                      onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                              		查看结果
                                              	</button>&nbsp;&nbsp;
                                              <button name="viewDetailButton"
                                                      onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
                                          			    查看详细
                                          	   </button>
                                             </td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="6" align="right"><lk:pages
                                                url="/gtms/stb/resource/batchSendUserInfo!initImport.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="false" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="6">
							<font color="red">没有定制的任务</font>
						</td>
					</tr>
				</tbody>
			</s:else>

</s:if>
		</table>
		<div id="divDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
</s:form>
</body>
