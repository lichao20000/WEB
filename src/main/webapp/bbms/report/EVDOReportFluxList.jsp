<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--
EVDO����ͳ�Ʊ���������
��Ҫ���ݣ�ͳ�������Ľ��չʾ
author��zxj
E-mail��qixq@lianchuang.com
since ��2009-11-24
-->
<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr bgcolor="#FFFFFF">
		<td class=column1 height="20">
			<strong>
				<s:property value="titleResult"/>
			</strong>
		</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<s:if test="reportResultList.size()==0">
					<tr bgcolor="#FFFFFF">
						<td align=center>ָ��ʱ�����û��ͳ�����ݣ�</td>
					</tr>
				</s:if>
				<s:else>
					<tr bgcolor="#FFFFFF">
						<th>���</th>
						<th>������������</th>
						<th>������������</th>
						<th>������������</th>
						<th>������������</th>
						<th>С��</th>
					</tr>
				</s:else>
				<s:iterator value="reportResultList" var="serv" status="servSt">
					<tr bgcolor="#FFFFFF">
						<td  class=column><s:property value="count_desc"/></td>
						<td align=center><s:property value="wire_up_flux"/></td>
						<td align=center><s:property value="wire_down_flux"/></td>
						<td align=center><s:property value="wireless_up_flux"/></td>
						<td align=center><s:property value="wireless_down_flux"/></td>
						<td align=center><s:property value="all_time"/></td>
					</tr>
				</s:iterator>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" >
				<tr bgcolor="#FFFFFF">
					<td class=column1 align="center" width="40">
					 	<a href="javascript:queryDataForExcel('<s:property value="reportName"/>','<s:property value="reportType"/>','<s:property value="countType"/>','<s:property value="queryDate"/>');">
				 			<img src="<s:url value="/images/excel.gif"/>"  border="0" width="16" height="16"></img>
					 	</a>
					</td>
					<td class=column1 align="right">
						<strong>
							ͳ�����ڣ�<s:property value="queryDateStr"/>
						</strong>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>