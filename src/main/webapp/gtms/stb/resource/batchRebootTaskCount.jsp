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
function queryDevList(taskid,city_id,status)
{
	var page = "<s:url value='/gtms/stb/resource/batchReboot!queryDevList.action'/>"
				+ "?taskId="+taskid
				+ "&cityId="+city_id
				+ "&status="+status;
	window.open(page,"","left=20,top=20,width=1200,height=400,resizable=no,scrollbars=yes");
}

function toExcel()
{
	var taskId = $("input[name=taskId]").val();
	var url = "<s:url value='/gtms/stb/resource/batchReboot!exportTaskResult.action'/>?"
				+ "taskId="+taskId;
    window.location.href=url;
}
</SCRIPT>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：机顶盒批量重启任务结果统计
		</TD>
	</TR>
</TABLE>

<br>
<br>
<input type="hidden" name="taskId" value="<s:property value="taskId" />">
<table width="98%" class="listtable" align="center">
	<thead>
		<tr>
			<th align="center" width="10%">属地</th>
			<th align="center" width="10%">任务触发成功</th>
			<th align="center" width="10%">待触发</th>
            <th align="center" width="10%">不支持</th>
            <th align="center" width="10%">小计</th>
			<th align="center" width="10%">总量占比</th>
			<th align="center" width="10%">成功占比</th>
		</tr>
	</thead>
	<s:if test="data!=null && data.size()>0">
		<tbody>
			<s:iterator value="data">
				<tr>
					<td align="center"><s:property value="city_name" /></td>
					<td align="center">
						<s:if test="suss==0">
							<s:property value='suss'/>
						</s:if>
						<s:elseif test="city_id==-1">
							<s:property value='suss'/>
						</s:elseif>
						<s:else>
							<a href="javascript:queryDevList('<s:property value="task_id" />',
															'<s:property value="city_id" />','1')">
								<s:property value='suss'/>
							</a>
						</s:else>
					</td>
					<td align="center">
						<s:if test="unDo==0">
							<s:property value='unDo'/>
						</s:if>
						<s:elseif test="city_id==-1">
							<s:property value='unDo'/>
						</s:elseif>
						<s:else>
							<a href="javascript:queryDevList('<s:property value="task_id" />',
															'<s:property value="city_id" />','0')">
								<s:property value='unDo'/>
							</a>
						</s:else>
					</td>
					<td align="center">
						<s:if test="noDo==0">
							<s:property value='noDo'/>
						</s:if>
						<s:elseif test="city_id==-1">
							<s:property value='noDo'/>
						</s:elseif>
						<s:else>
							<a href="javascript:queryDevList('<s:property value="task_id" />',
															'<s:property value="city_id" />','2')">
								<s:property value='noDo'/>
							</a>
						</s:else>
					</td>
					<td align="center">
						<s:if test="count==0">
							<s:property value='count'/>
						</s:if>
						<s:elseif test="city_id==-1">
							<s:property value='count'/>
						</s:elseif>
						<s:else>
							<a href="javascript:queryDevList('<s:property value="task_id" />',
															'<s:property value="city_id" />','100')">
								<s:property value='count'/>
							</a>
						</s:else>
					</td>
					<td align="center"><s:property value="countPerc" /></td>
					<td align="center"><s:property value="sussPerc" /></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
			<tr bgcolor="#FFFFFF">
				<td colspan="7" align="left">
					<img src="<s:url value='/images/excel.gif'/>" border='0' alt='导出列表'
							style='cursor: hand' onclick="toExcel()" align=left />
				</td>
			</tr>
		</tfoot>
	</s:if>
	<s:else>
		<tbody>
			<tr>
				<td colspan="9">
					<font color="red">查无数据</font>
				</td>
			</tr>
		</tbody>
	</s:else>
</table>
</body>
