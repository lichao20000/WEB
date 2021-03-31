<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">		
function updateTask(taskId,status)
{
	//任务激活，需要再验证
	if(status=='1')
	{
		var url = "<s:url value='/gtms/stb/resource/batchReboot!getErrData.action'/>";
	    $.post(url,{
			taskId:taskId
		},function(ajax){
			var s=ajax.split(",");
			var flag=true;
	    	if(s[0]!='0')
	    	{
	    		var mess="导入账号数："+s[1]+", 异常账号数："+s[0]+",是否继续激活？";
	    		flag=confirm(mess);
	    	}
	    	
	    	if(!flag){
	    		return;
	    	}
	    	
	    	var url = "<s:url value='/gtms/stb/resource/batchReboot!updateTask.action'/>";
		    $.post(url,{
				taskId:taskId,
				status:status
			},function(ajax){
		    	var s = ajax.split(",");
		    	alert(s[1]);
		    	if(s[0]=="1"){
		    		var cityId = $("select[name=cityId]").val();
		    		var vendorId = $("select[name=vendorId]").val();
		    		var status = $("select[name=status]").val();
		    		var showType=$("input[name=showType]").val();
		    		var url = "<s:url value='/gtms/stb/resource/batchReboot!queryInit.action'/>?"
		    					+ "cityId="+cityId
		    					+ "&vendorId="+vendorId
		    					+ "&status="+status;
		    					+ "&showType="+showType;
		    		location.reload();
		    	}
		    });
	    	
	    });
	}
	else
	{
		var url = "<s:url value='/gtms/stb/resource/batchReboot!updateTask.action'/>";
	    $.post(url,{
			taskId:taskId,
			status:status
		},function(ajax){
	    	var s = ajax.split(",");
	    	alert(s[1]);
	    	if(s[0]=="1"){
	    		var cityId = $("select[name=cityId]").val();
	    		var vendorId = $("select[name=vendorId]").val();
	    		var status = $("select[name=status]").val();
	    		var showType=$("input[name=showType]").val();
	    		var url = "<s:url value='/gtms/stb/resource/batchReboot!queryInit.action'/>?"
	    					+ "cityId="+cityId
	    					+ "&vendorId="+vendorId
	    					+ "&status="+status;
	    					+ "&showType="+showType;
	    		location.reload();
	    	}
	    });
	}
}

    
function viewdetailTask(taskid)
{
	var page = "<s:url value='/gtms/stb/resource/batchReboot!getTaskInfo.action'/>?taskId="+taskid;
	window.open(page,"","left=20,top=20,width=800,height=400,resizable=no,scrollbars=yes");
}

function count(taskid)
{
	var page = "<s:url value='/gtms/stb/resource/batchReboot!count.action'/>?taskId="+taskid;
	window.open(page,"","left=20,top=20,width=800,height=400,resizable=no,scrollbars=yes");
}
	
function queryTask()
{
	var cityId = $("select[name=cityId]").val();
	var vendorId = $("select[name=vendorId]").val();
	var status = $("select[name=status]").val();
	var showType=$("input[name=showType]").val();
	
	var url = "<s:url value='/gtms/stb/resource/batchReboot!queryInit.action'/>?"
				+ "cityId="+cityId
				+ "&vendorId="+vendorId
				+ "&status="+status
				+ "&showType="+showType;
    window.location.href=url;
}
</SCRIPT>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：机顶盒批量重启任务管理
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" class="querytable" align="center">
	<tr>
		<TD class="title_2" align="center" width="10%">属地</TD>
		<TD width="15%">
			<s:select list="cityList" name="cityId" headerKey="00"
				headerValue="省中心" listKey="city_id" listValue="city_name"
				value="cityId" cssClass="bk" theme="simple">
			</s:select>
		</TD>
		<TD class="title_2" align="center" width="10%">厂商</TD>
		<TD width="25%">
			<s:select list="vendorList" name="vendorId" headerKey="-1"
				headerValue="全部" listKey="vendor_id" listValue="vendor_add"
				value="vendorId" cssClass="bk" theme="simple">
			</s:select>
		</TD>
		<td class="title_2" align="center">状态</td>
		<td width="15%">
			<s:select name="status" value="status" list="#{'1':'已激活','-1':'已失效'}" 
				cssClass="bk" theme="simple" headerKey="" headerValue="全部" >
			</s:select>
		</td>
		<td class="title_2" align="center">
			<button onclick="queryTask()"> 查 询 </button>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
	</tr>
</table>

<br>
<br>
<input type="hidden" name="showType" value="<s:property value='showType' />">
<table width="98%" class="listtable" align="center">
	<thead>
		<tr>
		    <th align="center" width="15%">任务简要描述</th>
		    <th align="center" width="5%">定制类型</th>
			<th align="center" width="5%">属地</th>
			<th align="center" width="5%">厂商</th>
			<th align="center" width="5%">定制人</th>
            <th align="center" width="8%">定制时间</th>
            <th align="center" width="8%">更新时间</th>
			<th align="center" width="5%">状态</th>
			<th align="center" width="20%">操作</th>
		</tr>
	</thead>
	<s:if test="data!=null && data.size()>0">
		<tbody>
			<s:iterator value="data">
				<tr>
					<td align="center"><s:property value="task_desc" /></td>
					<td align="center"><s:property value="data_desc" /></td>
					<td align="center"><s:property value="city_name" /></td>
					<td align="center"><s:property value="vendor_name" /></td>
					<td align="center"><s:property value="acc_loginname" /></td>
					<td align="center"><s:property value="add_time" /></td>
					<td align="center"><s:property value="update_time" /></td>
					<s:if test='status=="1" || status=="4"'>
						<td align="center">已激活</td>
						<td align="center">
							<s:if test='showType!=1'>
								<button onclick="javascript:updateTask('<s:property value="task_id" />','-2')">
									失效
								</button>
								<s:if test="showType!=2">
									<button onclick="javascript:updateTask('<s:property value="task_id" />','2')">
										删除
									</button>
								</s:if>
							</s:if>
                            <button onclick="javascript:count('<s:property value="task_id"/>')">
 								结果统计
                            </button>
							<button onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
								查看详细
							</button>
						</td>
					</s:if>
					<s:elseif test='status=="-1"' >
						<td align="center">已失效</td>
						<td align="center">
							<s:if test='showType!=1'>
								<button onclick="javascript:updateTask('<s:property value="task_id" />','1')">
									激活
								</button>
								<s:if test="showType!=2">
									<button onclick="javascript:updateTask('<s:property value="task_id" />','2')">
										删除
									</button>
								</s:if>
							</s:if>
							<button onclick="javascript:count('<s:property value="task_id"/>')">
								结果统计
							</button>
							<button onclick="javascript:viewdetailTask('<s:property value="task_id"/>')">
								查看详细
							</button>
						</td>
					</s:elseif>
					<s:else>
						<td align="center">数据处理中</td>
						<td align="center">
							<s:if test='showType!=1'>
								<button disabled >激活</button>
								<s:if test="showType!=2">
									<button disabled >删除</button>
								</s:if>
							</s:if>
							<button disabled >结果统计</button>
							<button disabled >查看详细</button>
						</td>
					</s:else>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">
				<td colspan="9" align="right">
					<lk:pages url="/gtms/stb/resource/batchReboot!queryInit.action"
						styleClass="" showType="" isGoTo="true" changeNum="false" />
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tbody>
			<tr>
				<td colspan="9">
					<font color="red">没有定制的任务</font>
				</td>
			</tr>
		</tbody>
	</s:else>
</table>
</body>
