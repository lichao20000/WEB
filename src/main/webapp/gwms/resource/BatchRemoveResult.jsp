<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title> </title>
<%
	/**
		 *  
		 * 
		 * @author hanzezheng
		 * @version 1.0
		 * @since 2010-5-21
		 * @category
		 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	$(function(){
		parent.dyniframesize();
		parent.isNotShow();
	});
	function ToExcel(taskId){
		var page = "<s:url value='/gwms/resource/batchRemoveBind!resultExcel.action'/>?"
				+ "&taskId=" + taskId;
		document.all("childFrm").src=page;
	}
</script>

</head>

<body>
	<table class="listtable">
		<caption>
			解绑结果
		</caption>
		<thead>
			<tr>
				<th>用户</th>
				<th>时间</th>
				<th>文件名</th>
				<th>loid</th>
				<th>设备ID</th>
				<th>结果</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="list">
				<tr bgcolor="#FFFFFF">
					<td><s:property value="acc_loginname"/></td>
					<td><s:property value="upload_date"/></td>
					<td><s:property value="filename"/></td>
					<td><s:property value="loid"/></td>
					<td><s:property value="device_id"/></td>
					<td><s:property value="result_info"/></td>
				</tr>
			</s:iterator>
		</tbody>
		<tfoot>
		<tr>
			<td colspan="6" align="right">
				<span style="float: right;"> <lk:pages url="/gwms/resource/batchRemoveBind!getResultList.action" styleClass="" showType="" isGoTo="true" changeNum="true"/></span>
				<IMG style="float: left;cursor: hand" SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
						onclick="ToExcel('<s:property value="taskId"/>')">
			</td>
		</tr>
	</tfoot>
	<tr STYLE="display: none">
		<td colspan="6">
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
	</table>
		
						
</body>
</html>