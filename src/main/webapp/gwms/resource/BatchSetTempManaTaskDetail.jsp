<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">
function templateDetail(id)
{     
	window.open("<s:url value='/gwms/resource/servTemplate!queryDetail.action' />?id="+id,"","height=800,width=1500,status=yes,toolbar=no,menubar=no,location=no");
}
function resultConvert(res)
{
	switch(res)
	{
		case 0:
			document.write("失效");
			break;
		case 1:
			document.write("生效");
			break;
		default:
			document.write("");
	}
}

function dateConvert(dat)
{
	var d = new Date(dat*1000);
	var year = d.getFullYear();
	var month = d.getMonth()+1;
	var day = d.getDate();
	var h = d.getHours();
	var m = d.getMinutes();
	var s = d.getSeconds();
	
	document.write(year+"/"+month+"/"+day+" "+h+":" + m+":"+s);
}
</script>
</head>

<body>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			批量参数下发任务详情
		</TD>
	</TR>
</TABLE>
<br>
<table class="querytable" width="98%" align="center">
	<tr>
		<!-- <td bgcolor=#999999 colspan="4" class="title_1"><div align="center">批量参数下发任务详情</div></td> -->
		<td colspan="4" class="title_1">批量参数下发任务详情</td>
	</tr>
	<TR>
		<TD class="title_2" align="center" width="15%">任务名称</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.task_name" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">模板名称</TD>
		<TD width="85%" colspan="3">
			<a class='green_link' href="#" onclick="templateDetail('<s:property value="taskResultMap.id" />')"><s:property value="taskResultMap.name" /></a>
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">厂商</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.vendor_id" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">型号</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.device_model_id" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">版本</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.version" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">属地</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.city_name" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">已绑定</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.isbind" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">导入文件</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.file_name" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">操作人</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.acc_loginname" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">触发策略</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.type" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">触发开始日期</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.start_date" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">触发结束日期</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.end_date" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">触发开始时间</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.start_time" />
		</TD>
	</TR>
	<TR>
		<TD class="title_2" align="center" width="15%">触发结束时间</TD>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.end_time" />
		</TD>
	</TR>
	<tr>
		<TD class="title_2" align="center" width="15%">定制时间</td>
		<TD width="85%" colspan="3">
			<s:property value="taskResultMap.add_time" />
		</TD>
	</tr>
</table>
<br>
<br>
</body>