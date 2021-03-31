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
	 * e8-c业务查询
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-08-15
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
	
</script>
</head>
<body>
<table class="listtable" id="listTable">
	<caption>实际终端规格与BSS终端规格不一致比对业务信息</caption>
	<thead>
		<tr>
			<th>属地</th>
			<th>用户实际终端规格</th>
			<th>BSS终端规格</th>
			<th>用户量</th>
			<th>占比</th>
		</tr>
	</thead>
	<tbody>
	
		<s:if test="userSpecList!=null">
			<s:if test="userSpecList.size()>0">
				<s:iterator value="userSpecList">
				<tr>
					<td><s:property value="city_name"/> </td>
					<td><s:property value="userspec_name"/> </td>
					<td><s:property value="bssspec_name"/> </td>
					<td><s:property value="molecular"/> </td>
					<td><s:property value="pert"/> </td>
				</tr>
				</s:iterator>	
			</s:if>
			<s:else>
				<td colspan=5>没有符合条件的不一致信息!</td>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=5>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>
</table>
</body>
<script>
</script>

</html>