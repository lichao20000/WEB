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
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<%@ include file="../../head.jsp"%>
<body>
<table class="querytable">
		<TR>
			<th colspan="2">工单详细内容</th>
		</TR>
		<tr>
			<td class=column width="15%" align='right'>策略ID</td>
			<td width="85%"><s:property value="operateMap['bss_sheet_id']" /> </td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>业务类型</td>
			<td width="85%"><s:property value="operateMap['servType']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>结果详细描述</td>
			<td width="85%"><s:property value="operateMap['resultId']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>错误原因</td>
			<td width="85%" colspan="3">
			<textarea cols="80" rows="6" class=bk readonly="readonly"><s:property value="operateMap['returnt_context']" /></textarea>
			</td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'></td>
			<td width="85%" colspan="3">
			<textarea cols="80" rows="14" class=bk readonly="readonly"><s:property value="operateMap['sheet_context']" /></textarea>
		    </td>
		</tr>
		<TR>
				<td colspan="2" align="center"  class=foot>
								<button onclick="javascript:window.close();" >&nbsp;关&nbsp;&nbsp;闭&nbsp;</button>
				</td>
		</TR>
</table>
</body>
<%@ include file="../../foot.jsp"%>