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
	<s:if test='"-1"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				�豸���Ӳ���
			</td>
		</tr>
	</s:if>
	<s:elseif test='"1"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				�ѳɹ�������
			</td>
		</tr>
	</s:elseif>
	<s:elseif test='"0"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				�ѳɹ�������
			</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-2"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				�豸û����Ӧ
			</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-7"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				�豸û��Ӧ
			</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-6"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				�豸��������
			</td>
		</tr>
	</s:elseif>
	<s:elseif test='"-9"==rebootFlag'>
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">
				δ֪����ԭ��
			</td>
		</tr>
	</s:elseif>
	<s:else>
		<tr class='blue_foot'>
			<td bgcolor='#FFFFFF'>
				δ֪����ԭ��
			</td>
		</tr>
	</s:else>
</table>