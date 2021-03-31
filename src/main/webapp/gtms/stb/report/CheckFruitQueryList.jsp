<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>江西检测查询结果页面</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function() {
		parent.dyniframesize();
	});
	
	function ToExcel()
	{
		var mainForm = window.parent.document.getElementById("frm");
		mainForm.action="<s:url value='/gtms/stb/resource/CheckFruitQuery!Derive.action'/>";
		mainForm.submit();
	} 
	</script>

</head>
<body>
	<form name="frm" id="from">
		<table class="listtable" id="listTable">
			<caption>查询结果</caption>
			<thead>
				<tr>
					<th>业务账号</th>
					<th>MAC</th>
					<th>接入方式</th>
					<!-- <th>ITMS接收时间</th> -->
					<th>码率</th>
					<th>丢包</th>
					<th>上报时间</th>
				</tr>
			</thead>

			<s:if test="data!=null">
				<s:if test="data.size()>0">
					<s:iterator value="data">
						<tr>
							<td><s:property value="user_id" /></td>
							<td><s:property value="mac" /></td>
							<td><s:property value="conn_type" /></td>
							<td><s:property value="bitrate" /></td>
							<td><s:property value="package_lost" /></td>
							<td><s:property value="report_time" /></td>
						</tr>
					</s:iterator>
				</s:if>
				<s:else>
					<tr>
						<td colspan=6>系统中没有查询出需要的信息!</td>
					</tr>
				</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>系统中没有查询出需要的信息!</td>
				</tr>
			</s:else>
			<table>
				<tr>
					<td colspan="6" align="left"><img alt="导出表格"
						src="../../../images/excel.gif" border="0" style='cursor: hand'
						onclick="ToExcel()"></td>
					<td colspan="6" align="right">
					<lk:pages url="/gtms/stb/resource/CheckFruitQuery!Query.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
				</tr>
			</table>
		</table>
	</form>
</body>
</html>