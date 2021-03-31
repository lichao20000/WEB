<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>
<table class="listtable" width="100%" align="center">
	<tr class='blue_foot'>
		<td bgcolor='#FFFFFF' nowrap>成功数</td>
		<td bgcolor='#FFFFFF' nowrap>失败数</td>
		<td bgcolor='#FFFFFF' nowrap>平均响应时间</td>
		<td bgcolor='#FFFFFF' nowrap>最小响应时间</td>
		<td bgcolor='#FFFFFF' nowrap>最大响应时间</td>
	</tr>
	<s:if test="'-1'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				设备连接不上
			</td>
		</tr>
	</s:if>
	<s:elseif test="'-7'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				设备连接失败
			</td>
		</tr>
	</s:elseif>
	<s:elseif test="'-6'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				设备正被操作
			</td>
		</tr>
	</s:elseif>
	<s:elseif test="'-9'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				未知错误原因
			</td>
		</tr>
	</s:elseif>
	<s:else>
		<tr class='blue_foot'>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.succNum"/></td>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.failNum"/></td>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.delayAvg"/>ms</td>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.delayMin"/>ms</td>
			<td bgcolor='#FFFFFF' ><s:property value="pingObj.delayMax"/>ms</td>
		</tr>
	</s:else>
</table>