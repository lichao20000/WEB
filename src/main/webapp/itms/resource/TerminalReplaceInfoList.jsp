<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>终端替换率统计报表</title>
<%
/**
 * 终端替换率统计报表
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
</script>
</head>
<body>

<table class="listtable" id="listTable">
	<caption>终端替换率统计情况 </caption>
	<thead>
		<tr>
			<th>厂商</th>
			<th>终端更换量</th>
			<th>终端总数</th>
			<th>终端替换率</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="terminalList!=null">
			<s:if test="terminalList.size()>0">
				<s:iterator value="terminalList">
						<tr>
							<td><s:property value="vendor_name" /></td>
							<td><s:property value="oper_num" /></td>
							<td><s:property value="all_num" /></td>
							<td><s:property value="percentage" /></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=4>系统没有终端替换率统计信息 !</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=4>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="4" align="right"><IMG SRC="/itms/images/excel.gif" BORDER='0' ALT='导出列表' style='cursor: hand' onclick="ToExcel()"  />
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>