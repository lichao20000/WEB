<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>
<table class="listtable" width="100%" align="center">
	<s:if test='"-1"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">设备连接不上</td>
		</tr>
	</s:if>
	<s:elseif test='"1"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">已成功恢复出厂设置！</td>
		</tr>
	</s:elseif>
	<s:elseif test='"0"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">已成功恢复出厂设置！</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-2"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">设备没有响应</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-7"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">设备没响应</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-6"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">设备正被操作</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-9"==restoreFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">未知错误原因</td>
		</tr>
	</s:elseif>
	<s:else>
		<tr class='blue_foot'>
			<td bgcolor='#FFFFFF'>未知错误原因</td>
		</tr>
	</s:else>
</table>