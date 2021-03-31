<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查询结果</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
	
</script>
</head>
<body>
	<table class="listtable" id="listTable">
		<caption>查询结果</caption>
		<thead>
			<tr>
				<th width="20%">数图短号位数</th>
				<th width="80%">配置数图值</th>
			</tr>
		</thead>
		<tbody>
		<s:if test="Date != null ">
			<s:if test="Date.size() > 0">
				<s:iterator value="Date">
					<tr>
						<td>
							<s:property value="remark" />
						</td>
						<td>
							<s:property value="digit_map_value" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
			</s:if>
			<s:else>
				<tr>
					<td colspan=7 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
	</tbody>
	</table>
</body>
</html>