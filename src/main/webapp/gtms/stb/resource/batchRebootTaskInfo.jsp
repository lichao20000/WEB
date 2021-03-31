<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>批量重启机顶盒任务详细信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function queryServAccount(status)
{
	var task_id = $("input[name=taskId]").val();
	var data_type = $("input[name=data_type]").val();
	var url = "<s:url value='/gtms/stb/resource/batchReboot!exportTaskServAccount.action'/>"
				+ "?taskId="+task_id
				+ "&data_type="+data_type
				+ "&status="+status;
	window.location.href=url;
}
</SCRIPT>
</head>

<body>
<input type="hidden" name="taskId" value="<s:property value='taskResultMap.task_id'/>">
<input type="hidden" name="data_type" value="<s:property value='taskResultMap.data_type'/>">
<table width="100%" class="querytable" align="center">
	<tr bgcolor="#FFFFFF">
		<td colspan="2" class="title_1" >批量重启机顶盒任务详细信息</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">简要描述</td>
		<td>
			<s:property value='taskResultMap.task_desc'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">详细描述</td>
		<td>
			<s:property value='taskResultMap.task_detail'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">定制人账号</td>
		<td>
			<s:property value='taskResultMap.acc_loginname'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">任务类型</td>
		<td>
			<s:property value='taskResultMap.data_type_desc'/>
		</td>
	</tr>
	<s:if test="data_type==0">
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">属地</td>
			<td>
				<s:property value='taskResultMap.city_name'/>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">厂商</td>
			<td>
				<s:property value='taskResultMap.vendor_add'/>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">触发版本</td>
			<td>
				<s:property value='taskResultMap.device_type'/>
			</td>
		</tr>
	</s:if>
	<s:else>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">文件名称</td>
			<td>
				<s:property value='taskResultMap.file_name'/>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">导入数据量</td>
			<td>
				<s:if test="taskResultMap.all_num==0">
					<s:property value='taskResultMap.all_num'/>
				</s:if>
				<s:else>
					<a href="javascript:queryServAccount(100)">
						<s:property value='taskResultMap.all_num'/>
					</a>
				</s:else>
			</td>
		</tr>
		<tr bgcolor="#FFFFFF">
			<td align="right" class="column" width="15%">导入异常数据量</td>
			<td>
				<s:if test="taskResultMap.err_num==0">
					<s:property value='taskResultMap.err_num'/>
				</s:if>
				<s:else>
					<a href="javascript:queryServAccount(-2)">
						<s:property value='taskResultMap.err_num'/>
					</a>
				</s:else>
			</td>
		</tr>
	</s:else>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">任务状态</td>
		<td>
			<s:property value='taskResultMap.status'/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">定制时间</td>
		<td>
			<s:property value='taskResultMap.add_time' />
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td align="right" class="column" width="15%">最后修改时间</td>
		<td>
			<s:property value='taskResultMap.update_time'/>
		</td>
	</tr>
	
	<TR>
		<TD colspan="2" class=foot>
			<div align="center">
				<button onclick="javascript:window.close();"> 关 闭 </button>
			</div>
		</TD>
	</TR>
</table>
</body>