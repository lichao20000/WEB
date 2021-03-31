<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务下发失败查询结果展现</title>

<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>

<script type="text/javascript">



</script>

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="99.9%" align="center">
		<tr bgcolor="#FFFFFF">
			<td class=column1 height="25">
				<strong>
					<s:if test='"1".equals(reportType)'>
						业务下发失败原因小时统计
					</s:if>
					<s:if test='"2".equals(reportType)'>
						业务下发失败原因日统计
					</s:if>
					<s:if test='"3".equals(reportType)'>
						业务下发失败原因周统计
					</s:if>
					<s:if test='"4".equals(reportType)'>
						业务下发失败原因月统计
					</s:if>
				</strong>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<s:iterator value="reportResult" var="serv" status="servSt">
						<tr bgcolor="#FFFFFF">
							<s:if test="#servSt.getIndex()==0">
								<s:iterator var="city">
									<th><s:property /></th>
								</s:iterator>
							</s:if>
							<s:else>
								<s:iterator var="city" status="citySt">
									<s:if test="#citySt.getIndex()==0">
										<td  class=column><s:property /></td>
									</s:if>
									<s:else>
										<td  bgcolor=#ffffff align=center><s:property /></td>
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
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center"
					bgcolor="#000000">
					<tr bgcolor="#FFFFFF">
						<td class=column1 align="left" width="100">
						 	<a href="javascript:queryDataForPrint('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>');">
					 			<img src="../../images/print.gif"  border="0" width="16" height="16"></img>
						 	</a>
						 	&nbsp;
						 	<a href="javascript:queryDataForExcel('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>');">
					 			<img src="../../images/excel.gif"  border="0" width="16" height="16"></img>
						 	</a>
						 	&nbsp;
							<a href="javascript:queryDataForPdf('<s:property value="reportType"/>','<s:property value="cityId"/>','<s:property value="longData"/>');">
					 			<img src="../../images/pdf.gif"  border="0" width="16" height="16"></img>
						 	</a>
						</td>
						<td class=column1 align="right">
							<strong>
								<s:if test='"1".equals(reportType)'>
									统计日期：<s:property value="hourDataEnd"/>
								</s:if>
								<s:if test='"2".equals(reportType)'>
									统计日期：<s:property value="dayDataEnd"/>
								</s:if>
								<s:if test='"3".equals(reportType)'>
									统计截止时间：<s:property value="weekDataEnd"/>
								</s:if>
								<s:if test='"4".equals(reportType)'>
									统计截止时间：<s:property value="monthDataEnd"/>
								</s:if>
							</strong>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>
