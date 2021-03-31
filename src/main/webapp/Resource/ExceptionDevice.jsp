<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * 异常设备列表
 * @author 王志猛(5194) tel：1234567890123
 * @version 1.0
 * @since 2008-6-11 上午11:17:15
 *
 * 版权：南京联创科技 网管科技部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>异常设备处理</title>
<script type="text/javascript"
	src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css"></link>
<link rel="stylesheet" href="<s:url value="/css/liulu.css"/>" type="text/css">
<style type="text/css">
.table,.table tr,.table td ,.table th{
	border-style: solid;
	border-width: 1px;
	border-color: black;
	border-collapse: collapse;
	border-spacing: 0px;
}

.table {
	width: 100%;
}
</style>
<script type="text/javascript">
$(function(){
	var d = new Date();
	var e= new Date(d.getTime()-24*3600000);
	if(($("input[@name='startDate']").val()=="")||($("input[@name='endDate']").val()==""))
	{
		$("input[@name='startDate']").val(e.getYear()+"-"+(e.getMonth()+1)+"-"+e.getDate()+" 00:00:00");
		$("input[@name='endDate']").val(d.getYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" 23:59:59");
	}
});
function doExpDev(status,device_id,exception_time)
{
if(status==0)
{
alert("该异常设备还没有处理。");
return;
}
var url="<s:url value="/bbms/ExceptionDevice!expDev.action"/>?viewStaus="+status+"&device_id="+device_id+"&exception_time="+exception_time;
$.open(url,"400px","200px","100px","100px","false");
}
</script>
</head>
<body>
<table class="table">
	<form action="<s:url value="/bbms/ExceptionDevice!ExpDevList.action"/>" method="post" name="frm">
	<tr>
		<th colspan="4">异常设备列表查询</th>
	</tr>
	<tr>
		<td class="column">异常起始发生时间</td>
		<td><input type="text" name="startDate" value="<s:property value="startDate"/>" readonly style="background-color: #DFDFFF;color: #6a6a6a;"/>
		<button onclick="new WdatePicker(document.frm.startDate,'%Y-%M-%D %h:%m:%s',true,'whyGreen');">▼</button>
		</td>
		<td class="column">异常结束发生时间</td>
		<td><input type="text" name="endDate" value="<s:property value="endDate"/>" readonly style="background-color: #DFDFFF;color: #6a6a6a;"/>
		<button onclick="new WdatePicker(document.frm.endDate,'%Y-%M-%D %h:%m:%s',true,'whyGreen');">▼</button>
		</td>
	</tr>
	<tr><td class="column">处理状态</td><td colspan="3">
	<label for="ndeal">未处理</label><input type="radio" value="0" name="dealstatus" id="ndeal" <s:property value="dealstatus==0?'checked':''"/>/>
	<label for="ydeal">已处理</label><input type="radio" value="1" name="dealstatus" id="ydeal" <s:property value="dealstatus==1?'checked':''"/>/>
	<label for="adeal">全部</label><input type="radio" value="2" name="dealstatus" id="adeal" <s:property value="dealstatus==2?'checked':''"/>/>
	</td></tr>
	<tr>
		<td colspan="4" align="right">
		<button type="submit" class="jianbian" style="margin-left:10px;">查询</button>
		</td>
	</tr>
	</form>
</table>
<s:if test="expDevList!=null">
	<table class="table" style="margin-top:10px;">
		<tr>
			<th>设备信息</th>
			<th>异常产生时间</th>
			<th>异常原因</th>
			<th>系统配置</th>
			<th>设备配置</th>
			<th>处理状态</th>
			<th>操作</th>
		</tr>
		<s:iterator value="expDevList">
			<tr>
				<td><s:property value="device_info" /></td>
				<td><s:date name="exception_time" /></td>
				<td><s:property value="exceptionType" /></td>
				<td><s:property value="acs_config" /></td>
				<td><s:property value="cpe_config" /></td>
				<td><s:property value="status==0?'未处理':'已处理'" /></td>
				<td><a href="javascript://" onclick="doExpDev(3,'<s:property value="device_id"/>',<s:property value="exception_mark"/>);">处理该异常</a>&nbsp;|&nbsp;<a
					href="javascript://" onclick="doExpDev(<s:property value="status"/>,'<s:property value="device_id"/>',<s:property value="exception_mark"/>);">查看处理信息</a></td>
			</tr>
		</s:iterator>
	</table>
</s:if>
</body>
</html>