<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	/* function viewTask(taskId){ 
		var page = "<s:url value='/gwms/resource/batchHttpTestMana!getTestSpeedTaskResult.action'/>?taskId="+taskId;// getShowPictureConfigResult
		var win = window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
		win.location.reload();
		//window.location.href=page;
    } */
	
    $(function() {
    	var type='<s:property value="type" />';
    	$(" select option[value='"+type+"']").attr("selected","selected");
    }); 
    
	function viewTask(taskId){ 
		$("div[@id='divDetail']").show();
	    $("div[@id='divDetail']").html("正在查询，请稍等....");
	    var url = "<s:url value='/gwms/resource/batchHttpTestMana!getTaskCount.action'/>";
		$.post(url,{
			taskId:taskId
		},function(ajax){
			$("div[@id='divDetail']").html("");
			$("div[@id='divDetail']").append(ajax);
		});
    }
	
	function viewdetailTask(taskid)
	{
		var page = "<s:url value='/gtms/stb/resource/openDeviceShowPic!getShowPictureConfigDetail.action'/>?taskId="+taskid;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	function disableTask(taskId){
		if(confirm("确定要使任务失效吗？") == true){     //如果用户单击了失效按钮 
			var url = "<s:url value='/gwms/resource/batchHttpTestMana!doDisable.action'/>";
			$.post(url,{
				taskId : taskId
			},function(ajax){
				 if(ajax=='1'){
					 alert("失效操作成功");
				 }else{
					 alert("失效操作失败");
				 }
				 queryTask();
			});   
		}
	}
	
	function deleteTask(taskId){
		if(confirm("确定要删除使任务吗？") == true){     //如果用户单击了删除按钮 
			var url = "<s:url value='/gwms/resource/batchHttpTestMana!doDelete.action'/>";
			$.post(url,{
				taskId : taskId
			},function(ajax){
				 if(ajax=='1'){
					 alert("删除操作成功");
				 }else{
					 alert("删除操作失败");
				 }
				 queryTask();
			});   
		}
	}
	
	function showDesc(taskId,task_desc){
		$("div[@id='divDescDetail']").show();
		$("#descInput").val(task_desc);
		$("#taskIdInput").html(taskId);
	}
	
	
	function commitDesc(){
		var task_id = $("#taskIdInput").html();
		var desc = $("#descInput").val();
		var url = "<s:url value='/gwms/resource/batchHttpTestMana!commitDesc.action'/>";
		$.post(url,{
			taskId : task_id,
			task_desc:desc
		},function(ajax){
			 if(ajax=='1'){
				 alert("描述提交成功");
			 }else{
				 alert("描述提交失败");
			 }
		});
		//提交后隐藏修改描述对话框
		closeDescDetail();
		//重新查询
		queryTask();
	}
	
	
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
    function closeDescDetail(){
		$("div[@id='divDescDetail']").hide();
	}
    
    
    
    
    
   	function queryTask()
	{
   		var starttime = $.trim($("input[@name='startTime']").val());// 开始时间(注册时间)
	    var endtime = $.trim($("input[@name='endTime']").val());    // 结束时间(注册时间)
	    
	    if(starttime > endtime){
	    	alert("开始时间不能大于结束时间");
	    	return;
	    }
		var taskName = $("input[name=taskName]").val();
		var accName = $("input[name=accName]").val();
		//var queryVaild = $("select[name=queryVaild]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var type = $("select[name=type]").val();
		var url = "<s:url value='/gwms/resource/batchHttpTestMana!init.action'/>?taskName="+taskName+"&accName="+accName+"&startTime="+startTime+"&endTime="+endTime+"&type="+type;
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
					您当前的位置：批量测速任务查询管理
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="batchHttpTestMana!init.action" method="post" enctype="multipart/form-data" name="batchexform">
			<input type="hidden" name="deviceModelIds" value=""/>
			<table width="98%" class="querytable" align="center">
			<tr>
				<td class="title_1" width="15%">
						任务名称
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
						src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
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
						src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
						border="0" alt="选择" />
				</td>
				<!-- 
				<td width="35%">
					<span id="endTime"><lk:date id="end_time" name="endTime" dateOffset="0" defaultDate="" type="day" maxDateOffset="0" /></span>
				</td>
				 -->
			</tr>
			<ms:inArea areaCode="hb_lt" notInMode="false">
			<%if (!("ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))){%>
			<tr>
				<TD colspan="1" class="title_1" >
					批量测速模式：
				</TD>
				<TD colspan="3" class="" >
					<SELECT name="type">
							<OPTION value=''>==请选择==</OPTION>
							<OPTION value='1'>下行测速</OPTION>
							<OPTION value='2' >上行测速</OPTION>
					</SELECT>
				</TD>
			</tr>
			<%} %>
			</ms:inArea>
			<tr>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button onclick="queryTask()">
								查 询
							</button>
						</div>
					</td>
				</tr>
		</table>
		<table width="98%" class="listtable" align="center">
			<thead>
				<tr>
					<th align="center" width="20%">
						任务名称
					</th>
					<th align="center" width="26%">
						任务描述
					</th>
					<th align="center" width="7%">
						定制人
					</th>
					<th align="center" width="20%">
						订制时间
					</th>
					<th align="center" width="7%">
						任务状态
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
                                            <td align="center"><s:property value="task_desc_short" /></td>
                                            <td align="center"><s:property value="acc_loginname" /></td>
                                            <td align="center"><s:property value="add_time" /></td>
                                            <td align="center"><s:property value="task_status" /></td>
                                            <td align="center">
                                              <button name="viewButton" 
                                                      onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                              		查看结果
                                              	</button>&nbsp;&nbsp;
                                          	   <button  onclick="javascript:disableTask('<s:property value="task_id"/>')">
                                          	   失效</button>
                                          	   <button  onclick="javascript:deleteTask('<s:property value="task_id"/>')">
                                          	  删除</button>
                                          	  <button  onclick="javascript:showDesc('<s:property value="task_id"/>','<s:property value="task_desc"/>')">
                                          	 编辑描述</button>
                                             </td>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="6" align="right"><lk:pages
                                                url="/gwms/resource/batchHttpTestMana!init.action" styleClass=""
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
		<div id="divDescDetail"
			style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 30%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
			<table width="100%" class="listtable">
				<thead>
					<tr>
						<th align="center">
							任务描述
						</th>
					</tr>
				</thead>
				<tbody>
						<tr>
							<td align="center">
								<textarea id="descInput" style="width:300px;height:80px;"></textarea>
							</td>
							<td id="taskIdInput" style="display: none"></td>
						</tr>
				</tbody>
				<tfoot>
					<tr bgcolor="#FFFFFF">
						<td align="right">
							<button onclick="javascript:commitDesc();">
								提交
							</button>
							<button onclick="javascript:closeDescDetail();">
								关闭
							</button>
						</td>
						
					</tr>
				</tfoot>
			</table>
		</div>
</s:form>
</body>
