<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>
<table class="listtable" width="100%" align="center">
	<tr class='blue_foot'>
		<td bgcolor='#FFFFFF' nowrap>�ɹ���</td>
		<td bgcolor='#FFFFFF' nowrap>ʧ����</td>
		<td bgcolor='#FFFFFF' nowrap>ƽ����Ӧʱ��</td>
		<td bgcolor='#FFFFFF' nowrap>��С��Ӧʱ��</td>
		<td bgcolor='#FFFFFF' nowrap>�����Ӧʱ��</td>
	</tr>
	<s:if test="'-1'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				�豸���Ӳ���
			</td>
		</tr>
	</s:if>
	<s:elseif test="'-7'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				�豸����ʧ��
			</td>
		</tr>
	</s:elseif>
	<s:elseif test="'-6'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				�豸��������
			</td>
		</tr>
	</s:elseif>
	<s:elseif test="'-9'==pingObj.faultCode">
		<tr bgcolor='#FFFFFF'>
			<td colspan="5">				
				δ֪����ԭ��
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