<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务查询</title>
<%
/**
 * E8-C终端使用情况
 * 
 * @author gaoyi
 * @version 1.0
 * @since 2013-12-18
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
	
	function ToExcel(){
			parent.ToExcel();
	}
	function openCity(city_id){
			parent.openCity(city_id);
	}
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>E8-C终端统计情况 </caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>终端总数</th>
			<th>新增终端总数</th>
			<th>2+1规格</th>
			<th>4+2规格</th>
			<th>政企终端数</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="terminalList!=null">
			<s:if test="terminalList.size()>0">
				<s:iterator value="terminalList">
						<tr>
							<td>
							<a href="javaScript:openCity('<s:property value="city_id" />');"><s:property value="city_name" /></a>
							</td>
							<td><s:property value="allNum" /></td>
							<td><s:property value="newNum" /></td>
							<td><s:property value="twoNum" /></td>
							<td><s:property value="fourNum" /></td>
							<td><s:property value="egwNum" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=6>系统没有e8-c终端信息 !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=6>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
 
	<tfoot>
		<!-- 
			<tr>
				<td colspan="6" align="right">
			 	<lk:pages
					url="/itms/resource/TerminalE8CInfo!getTerminalE8CInfo.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		-->
		<tr>
			<td colspan="6" align="right"><IMG SRC="/itms/images/excel.gif"
				BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>