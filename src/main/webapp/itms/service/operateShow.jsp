<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ���ѯ</title>
<%
	/**
	 * e8-cҵ���ѯ
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
			<th colspan="2">������ϸ����</th>
		</TR>
		<tr>
			<td class=column width="15%" align='right'>����ID</td>
			<td width="85%"><s:property value="operateMap['bss_sheet_id']" /> </td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>ҵ������</td>
			<td width="85%"><s:property value="operateMap['servType']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>�����ϸ����</td>
			<td width="85%"><s:property value="operateMap['resultId']" /></td>
		</tr>
		<tr>
			<td class=column width="15%" align='right'>����ԭ��</td>
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
								<button onclick="javascript:window.close();" >&nbsp;��&nbsp;&nbsp;��&nbsp;</button>
				</td>
		</TR>
</table>
</body>
<%@ include file="../../foot.jsp"%>