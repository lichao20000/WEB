<%@page import="com.linkage.litms.LipossGlobals"%>
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
		<lk:res />
		<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		
	});	
	
	
	 function sendTask(taskId,operate){
		var	width=310;    
		var height=150; 
		var url="<s:url value='/gtms/stb/resource/batchConfigNode!validateCurUser.action'/>";
		//url=url+"?versionPath="+version_path;
		//url=url+"&softwareversion="+softwareversion;
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
		if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
			return;
		}
		if(returnVal.substring(0,1)!="1"){
			alert("用户密码验证失败");
			return;
		}
		var url = "<s:url value='/gtms/stb/resource/batchConfigNode!sendTask.action'/>";
	    $.post(url,{
			taskId:taskId,
			operate:operate
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		queryTask();
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("失效任务失败！");
	    	}
	    });
    }
	function validTask(taskId,version_path,softwareversion){
		var	width=310;    
		var height=150; 
		var url="<s:url value='/gtms/stb/resource/batchConfigNode!validateCurUser.action'/>";
		//url=url+"?versionPath="+version_path;
		//url=url+"&softwareversion="+softwareversion;
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');
		if(returnVal=="closewin" || typeof(returnVal)=='undefined'){
			return;
		}
		if(returnVal.substring(0,1)!="1"){
			alert("用户密码验证失败");
			return;
		}
		var url = "<s:url value='/gtms/stb/resource/softUpgrade!validExcelBatchTask.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
	    	var s = ajax.split(",");
	    	if(s[0]=="1"){
	    		alert(s[1]);
	    		queryTask();
	    	}else if(s[0]=="0"){
	    		alert(s[1]);
	    	}else{
	    		alert("激活任务失败！");
	    	}
	    });
	}
	
	function viewTask(taskId){ 
		var page = "<s:url value='/gtms/stb/resource/batchConfigNode!getBatchConfigResult.action'/>?taskId="+taskId;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
    }
	
	function viewdetailTask(taskid)
	{
		var page = "<s:url value='/gtms/stb/resource/batchConfigNode!getBatchConfigDetail.action'/>?taskId="+taskid;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
    function CloseDetail(){
		$("div[@id='divDetail']").hide();
	}
    
   	function queryTask()
	{
		var taskName = $("input[name=taskName]").val();
		var queryVaild = $("select[name=queryVaild]").val();
		var startTime = $("#start_time").val();
		var endTime = $("#end_time").val();
		var url = "<s:url value='/gtms/stb/resource/batchConfigNode!initImport.action'/>?queryVaild="+queryVaild+"&taskName="+taskName+"&startTime="+startTime+"&endTime="+endTime;
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
					您当前的位置：批量策略任务管理界面
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
					<input type="text" id="taskName" name="taskName" value="" size="26">
				</td>
				<td class="title_1" width="20%">状态</td>
				<td width="30%">
					<s:select name="queryVaild" value="queryVaild" list="#{'1':'已激活','0':'已失效'}" cssClass="bk" theme="simple" headerKey="-1" headerValue="全部" >
					</s:select>
				</td>		
				
			</tr>
			<tr>
			<td class="title_1" width="15%">
						开始时间
				</td>
				<td width="35%" >
					<span id="startTime"><lk:date id="start_time" name="startTime" type="day" defaultDate="" maxDateOffset="0" dateOffset="-15" /></span>
				</td>
				<td class="title_1" width="15%">
					结束时间
				</td>
				<td width="35%">
					<span id="endTime"><lk:date id="end_time" name="endTime" dateOffset="0" defaultDate="" type="day" maxDateOffset="0" /></span>
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
					<th align="center" width="8%">
						任务名称
					</th>
					<th align="center" width="8%">
						定制人
					</th>
					<th align="center" width="8%">
						订制时间
					</th>
					<th align="center" width="8%">
						更新时间
					</th>
					<th align="center" width="8%">
						状态
					</th>
					<th align="center" width="12%">
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
                                               	<s:if test='status == "1"'>
	                                                <td align="center">已激活</td>
	                                                <td align="center">
	                                                 <s:if test='accoid==acc_id||areaId=="1"'>
	                                                     <button name="cancerButton"
	                                                      		onclick="javascript:sendTask('<s:property value="task_id"/>','2')">
	                                                              		  失效
	                                                      </button>
	                                                 </s:if>     
	                                                      <button name="delButton" disabled="disabled"
	                                                             onclick="javascript:sendTask('<s:property value="task_id"/>','3')">
	                                                        			删除
	                                                       </button>
	                                                        <button name="viewButton"
	                                                                onclick="javascript:viewTask('<s:property value="task_id"/>')">
	                                                        	查看结果</button>
	                                                        <button name="viewDetailButton"
	                                                                onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
	                                                    	    查看详细</button>
	                                                </td>
                                                </s:if>
                                                <s:else>
                                                        <td align="center">已失效</td>
                                                        <td align="center">
	                                               	    <s:if test='accoid==acc_id||areaId=="1"'>
                                                        <button
                                                        <%
                                                        if ("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
                                                        	&& !"2".equals(request.getAttribute("status"))    
                                                        	&& !"-1".equals(request.getAttribute("status"))){
                                                        	%>
                                                        	disabled="disabled"
                                                        	<%
                                                        }
                                                        %>
                                                         name="cancerButton"
                                                                  	onclick="javascript:sendTask('<s:property value="task_id"/>','1')">
                                                           		   激活
                                                       	</button>
														</s:if>
                                                        <button name="delButton"
                                                                onclick="javascript:sendTask('<s:property value="task_id"/>','3')">
                                                        		删除
                                                        </button>
                                                        <button name="viewButton"
                                                                onclick="javascript:viewTask('<s:property value="task_id"/>')">
                                                       			 查看结果
                                                       	</button>
                                                        <button name="viewDetailButton"
                                                                onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
                                                      		  查看详细</button>
                                                        </td>
                                                </s:else>
                                        </tr>
                                </s:iterator>
                        </tbody>
                        <tfoot>
                                <tr bgcolor="#FFFFFF">
                                        <td colspan="8" align="right"><lk:pages
                                                url="/gtms/stb/resource/batchConfigNode!initImport.action" styleClass=""
                                                showType="" isGoTo="true" changeNum="false" /></td>
                                </tr>
                        </tfoot>
                </s:if>
			<s:else>
				<tbody>
					<tr>
						<td colspan="8">
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
