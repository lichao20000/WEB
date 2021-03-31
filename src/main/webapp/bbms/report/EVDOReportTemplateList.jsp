<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
						<td align=center>指定时间段内没有统计数据！</td>
					</tr>
				</s:if>
				<s:iterator value="reportResultList" var="serv" status="servSt">
					<tr bgcolor="#FFFFFF">
						<s:if test="#servSt.getIndex()==0">
							<s:iterator var="second">
								<th><s:property /></th>
							</s:iterator>
						</s:if>
						<s:else>
							<s:iterator var="second" status="secondSt">
								<s:if test="#secondSt.getIndex()==0">
									<td  class=column><s:property /></td>
								</s:if>
								<s:else>
									<td align=center><s:property /></td>
								</s:else>
							</s:iterator>
						</s:else>
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
							统计日期：<s:property value="queryDateStr"/>
						</strong>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
