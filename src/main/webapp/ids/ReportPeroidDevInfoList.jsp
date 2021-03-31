<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>定制设备信息查询</title>
<%
	/**
	 * 定制设备信息查询
	 * 
	 * @author zhangsb3
	 * @version 1.0
	 * @since 2014-5-12
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
	$(function() {
		parent.dyniframesize();
	});
	
	function ListToExcel(taskId) {
		var page = "<s:url value='/ids/reportPeroid!getDevInfoExcel.action'/>?"
				+ "taskId="+ taskId;
		document.all("childFrm").src = page;
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<thead>
		<tr>
			<th>设备序列号</th>
			<th>状态</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="devList!=null">
			<s:if test="devList.size()>0">
				<s:iterator value="devList">
						<tr>
							<td><s:property value="device_serialnumber" /></td>
							<td><s:property value="status" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=2>没有设备信息!</td>
				</tr>
			</s:else>
		</s:if>
	</tbody>
	
	<tfoot>
		<tr>
			<td colspan="2" align="right">
		 	<lk:pages
				url="/ids/reportPeroid!getDevInfo.action"
				styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
		</tr>
		<tr>
			<td colspan="2">
			<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
				style='cursor: hand'
				onclick="ListToExcel('<s:property value="taskId"/>')">
			</td>
		</tr>
	</tfoot>
	
	<tr STYLE="display: none">
		<td colspan="3"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
</body>
</html>