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
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
    
	//点击数字二级页面
	function getdetailList(task_id){
		var page = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!getdetailList.action'/>?task_id="+task_id;
		window.open(page,"","left=20,top=20,width=700,height=600,resizable=yes,scrollbars=yes");
	}
	
	function disableTask(task_id){
		if(confirm("确定要使任务失效吗？") == true){     //如果用户单击了失效按钮 
			var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!doDisable.action'/>";
			$.post(url,{
				task_id : task_id
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
	
	function ableTask(task_id){
		if(confirm("确定要重新生效吗？") == true){     //如果用户单击了失效按钮 
			var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!doAble.action'/>";
			$.post(url,{
				task_id : task_id
			},function(ajax){
				 if(ajax=='1'){
					 alert("生效操作成功");
				 }else{
					 alert("生效操作失败");
				 }
				 queryTask();
			});   
		}
	}
    
   	function queryTask()
	{
   		var starttime = $.trim($("input[@name='startTime']").val());// 开始时间(注册时间)
	    var endtime = $.trim($("input[@name='endTime']").val());    // 结束时间(注册时间)
	    
		var taskName = $("input[name=taskName]").val();
		var accName = $("input[name=accName]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!queryBlackDeviceTask.action'/>?taskName="+taskName+"&accName="+accName+"&startTime="+startTime+"&endTime="+endTime;
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
					您当前的位置：升级黑名单任务管理
				</TD>
			</TR>
		</TABLE>
		<br>
		<form name="batchexform" method="post" action="">
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
			</tr>
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
	                                            <td align="center"><s:property value="acc_loginname" /></td>
	                                            <td align="center"><s:property value="add_time" /></td>
	                                            <td align="center"><s:property value="status" /></td>
	                                            <td align="center">
	                                              <button onclick="javascript:getdetailList('<s:property value="task_id"/>')">
	                                              		查看详情
	                                              </button>&nbsp;&nbsp;
	                                          	   <s:if test="status!='正在执行中'&&status!='失效'">
		                                          	   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                                          	   <button  onclick="javascript:disableTask('<s:property value="task_id"/>')">
		                                          	   		失效
		                                          	   </button>
	                                          	   </s:if>
	                                          	   <s:if test="status=='失效'">
		                                          	   <button  onclick="javascript:ableTask('<s:property value="task_id"/>')">
		                                          	   		重新生效
		                                          	   </button>&nbsp;&nbsp;
	                                          	   </s:if>
	                                             </td>
	                                        </tr>
	                                </s:iterator>
	                        </tbody>
	                        <tfoot>
	                                <tr bgcolor="#FFFFFF">
	                                        <td colspan="6" align="right"><lk:pages
	                                                url="/gtms/stb/resource/stbUpgradeBlackList!queryBlackDeviceTask.action" styleClass=""
	                                                showType="" isGoTo="true" /></td>
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
		</form>
</body>
