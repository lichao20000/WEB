<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>
<table width="100%" align="center" >
	<tr>
		<th align="center" class="title_1">目标地址</th>
		<th align="center" class="title_1">成功数</th>
		<th align="center" class="title_1">失败数</th>
		<th align="center" class="title_1">平均响应时间</th>
		<th align="center" class="title_1">最小响应时间</th>
		<th align="center" class="title_1">最大响应时间</th>
	</tr>
	<s:if test="'-1'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="6">				
				<b>设备连接不上</b>
			</td>
		</tr>
	</s:if>
	<s:elseif test="'-7'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="6">				
				<b>设备连接失败</b>
			</td>
		</tr>
	</s:elseif>
	<s:elseif test="'-6'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="6">				
				<b>设备正被操作</b>
			</td>
		</tr>
	</s:elseif>
	<s:elseif test="'-9'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="6">				
				<b>未知错误原因</b>
			</td>
		</tr>
	</s:elseif>
	<s:else>
		<tr>
		    <td bgcolor='#FFFFFF' align="center"><s:property value="pingObj.pingAddr"/></td>
			<td bgcolor='#FFFFFF' align="center"><s:property value="pingObj.succNum"/></td>
			<td bgcolor='#FFFFFF' align="center"><s:property value="pingObj.failNum"/></td>
			<td bgcolor='#FFFFFF' align="center"><s:property value="pingObj.delayAvg"/>ms</td>
			<td bgcolor='#FFFFFF' align="center"><s:property value="pingObj.delayMin"/>ms</td>
			<td bgcolor='#FFFFFF' align="center"><s:property value="pingObj.delayMax"/>ms</td>
		</tr>
	</s:else>
	<tr>
			<th colspan="6" bgcolor='#FFFFFF' align="left" style="color:#FF0000;">				
				响应时间经验值：60ms
			</th>
		</tr>
</table>