<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
<%
	/**
	 *  预检预修告警信息
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2014-02-18
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
function queryDev(taskid){
	var url = "<s:url value='/ids/httpQualityTest!getTaskDevList.action'/>?taskid="+taskid;
	window.open(url,"","left=60,top=60,width=920,height=500,resizable=yes,scrollbars=yes");
}
</script>
</head>
<body>
<table class="green_gargtd">
				<tr>
					<th>批量HTTP下载拨测</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
				</tr>
</table>
	<table class="querytable" id="listTable">
		<tr>
			<th colspan=4>查看详细</th>
		</tr>
		<tr>
			<td width="15%">任务编号：</td>
			<td><s:property value="map.task_id"/></td>	
		</tr>
		<tr>
			<td width="15%">任务名称：</td>
			<td><s:property value="map.task_name"/></td>	
		</tr>
		<tr>
			<td width="15%">定制人：</td>
			<td><s:property value="map.acc_loginname"/></td>	
		</tr>
		<tr>
			<td width="15%">定制时间：</td>
			<td><s:property value="map.add_time"/></td>	
		</tr>
		<tr>
			<td width="15%">URL：</td>
			<td><s:property value="map.url"/></td>	
		</tr>
		<tr>
		<td colspan="2">
			<input type="button" id="btn" value="查看设备列表" onclick="queryDev('<s:property value="map.task_id" />')"/>
		</td>
		</tr>
	</table>
</body>
</html>